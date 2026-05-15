package es.gobcan.istac.indicators.core.serviceimpl.util;

import static org.siemac.edatos.core.common.util.GeneratorUrnUtils.generateSdmxCodelistUrn;
import static org.siemac.edatos.core.common.util.shared.UrnUtils.splitUrnItemScheme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Code;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.AttributeValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ComponentType;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Data;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataAttribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataAttributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataStructureDefinition;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Dimension;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionType;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Dimensions;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedAttributeValue;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedAttributeValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedDimensionValue;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedDimensionValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.NonEnumeratedDimensionValue;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.NonEnumeratedDimensionValues;

import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.GeographicalValueRepository;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.mapper.Do2DtoMapper;
import es.gobcan.istac.indicators.core.service.SrmRestInternalService;

public abstract class CommonMetamacUtils {

    public static String          DATA_SEPARATOR         = " | ";
    public Map<String, String>    variableElementsByCode = new HashMap<String, String>();

    public SrmRestInternalService srmRestInternalService;
    protected String              languageDefault;

    protected abstract Dimensions getDimensions();

    protected abstract Attributes getAttributes();

    protected abstract Data getData();

    protected abstract DataStructureDefinition getRelatedDsd();

    public static String[] dataToDataArray(String data) {
        return StringUtils.splitByWholeSeparatorPreserveAllTokens(data, DATA_SEPARATOR);
    }
    public String extractContVariable() {
        return extractSpecificDimensionFromDimensions(getDimensions(), DimensionType.MEASURE_DIMENSION);
    }
    public static String extractSpecificDimensionFromDimensions(Dimensions dimensions, DimensionType dimensionType) {
        for (Dimension dimension : dimensions.getDimensions()) {
            if (dimensionType.equals(dimension.getType())) {
                return dimension.getId();
            }
        }

        return null;
    }

    public static String extractValueForDefaultLanguage(InternationalString internationalString) {
        // Find in Dimensions or Attributes
        if (internationalString != null && !internationalString.getTexts().isEmpty()) {
            // Only one locale was received in the API, the default locale. Therefore, this code is valid.
            LocalisedString localisedString = internationalString.getTexts().iterator().next();
            return localisedString.getValue();
        }
        return null;
    }

    public static String extractValueForDefaultLanguage(InternationalString internationalString, String languageDefault) {
        if (internationalString == null) {
            return null;
        }
        String firstValue = null;
        for (LocalisedString localisedString : internationalString.getTexts()) {
            if (firstValue == null) {
                firstValue = localisedString.getValue();
            }
            if (localisedString.getLang().equals(languageDefault)) {
                return localisedString.getValue();
            }
        }
        return firstValue;
    }

    public static List<String> extractVariablesFromDimensions(Dimensions dimensions) {
        List<String> result = new ArrayList<String>();
        for (Dimension dimension : dimensions.getDimensions()) {
            result.add(dimension.getId());
        }

        return result;
    }


    public static List<String> extractCodeUrnOfSpecificTypeAttribute(Attributes attributes, DataAttributes dataAttributes, ComponentType componentType) {
        List<String> spatialValues = new ArrayList<String>();
        if (attributes == null || dataAttributes == null) {
            return spatialValues;
        }

        for (Attribute attribute : attributes.getAttributes()) {
            if (componentType.equals(attribute.getType())) {
                AttributeValues attributeValues = attribute.getAttributeValues();
                if (attributeValues instanceof EnumeratedAttributeValues) {

                    for (EnumeratedAttributeValue value : ((EnumeratedAttributeValues) attributeValues).getValues()) {
                        spatialValues.add(value.getUrn());
                    }

                }
            }
        }

        return spatialValues;
    }

    public static String extractSpecificAttributeValuesByType(Attributes attributes, DataAttributes dataAttributes, ComponentType componentType) {
        if (attributes == null || dataAttributes == null) {
            return null;
        }

        for (Attribute attribute : attributes.getAttributes()) {
            if (componentType.equals(attribute.getType())) {
                for (DataAttribute dataAttribute : dataAttributes.getAttributes()) {
                    if (attribute.getId().equals(dataAttribute.getId())) {
                        return dataAttribute.getValue();
                    }
                }
            }
        }

        return null;
    }

    public static String extractUrnCodelistFromUrnCode(String urnCode) {
        String[] params = UrnUtils.splitUrnItem(urnCode);
        String agencyId = params[0];
        String resourceId = params[1];
        String version = params[2];

        String[] agenciesID = agencyId.contains(".") ? agencyId.split(".") : new String[]{agencyId};
        return generateSdmxCodelistUrn(agenciesID, resourceId, version);
    }

    public static GeographicalValueRepository getGeographicalValueRepository() {
        return ApplicationContextProvider.getApplicationContext().getBean(GeographicalValueRepository.class);
    }

    public static Do2DtoMapper getDo2DtoMapper() {
        return ApplicationContextProvider.getApplicationContext().getBean(Do2DtoMapper.class);
    }

    public Map<String, List<String>> extractCodesCoverages() {
        return extractCoverages(false);

    }

    public Map<String, List<String>> extractValuesCoverages() {
        return extractCoverages(true);

    }
    public Map<String, List<String>> extractCoverages(boolean trylabels) {
        Map<String, List<String>> result = new HashMap<String, List<String>>();

        Dimensions dimensions = getDimensions();

        if (dimensions == null) {
            return result;
        }

        for (Dimension dimension : dimensions.getDimensions()) {
            List<String> valuesResult = new LinkedList<String>();
            DimensionValues dimensionValues = dimension.getDimensionValues();

            if (dimensionValues instanceof NonEnumeratedDimensionValues) {
                List<NonEnumeratedDimensionValue> values = ((NonEnumeratedDimensionValues) dimensionValues).getValues();
                for (NonEnumeratedDimensionValue nonEnumeratedDimensionValue : values) {
                    String extractValue;
                    if (trylabels) {
                        extractValue = extractValueForDefaultLanguage(nonEnumeratedDimensionValue.getName(), languageDefault);
                        valuesResult.add(extractValue);
                    } else {
                        extractValue = nonEnumeratedDimensionValue.getId();
                    }
                    valuesResult.add(extractValue);
                }
            } else {
                List<EnumeratedDimensionValue> values = ((EnumeratedDimensionValues) dimensionValues).getValues();
                for (EnumeratedDimensionValue enumeratedDimensionValue : values) {
                    String extractValue;
                    InternationalString name;
                    String valueId;

                    if (DimensionType.GEOGRAPHIC_DIMENSION.equals(dimension.getType())) {
                        name = enumeratedDimensionValue.getVariableElement().getName();
                        valueId = enumeratedDimensionValue.getVariableElement().getId();
                        if (variableElementsByCode.get(enumeratedDimensionValue.getId()) == null) {
                            variableElementsByCode.put(enumeratedDimensionValue.getId(), valueId);
                        }
                    } else {
                        name = enumeratedDimensionValue.getName();
                        valueId = enumeratedDimensionValue.getId();
                    }

                    if (trylabels) {
                        extractValue = extractValueForDefaultLanguage(name, languageDefault);
                    } else {
                        extractValue = valueId;
                    }
                    valuesResult.add(extractValue);
                }

            }
            result.put(dimension.getId(), valuesResult);
        }

        return result;
    }

    public String extractGeographicalCodelistUrn() {

        // first try dimension
        Dimensions dimensions = getDimensions();

        if (dimensions == null) {
            return null;
        }

        for (Dimension dimension : dimensions.getDimensions()) {
            if (DimensionType.GEOGRAPHIC_DIMENSION.equals(dimension.getType())) {

                DimensionValues dimensionValues = dimension.getDimensionValues();

                if (dimensionValues instanceof EnumeratedDimensionValues) {
                    List<EnumeratedDimensionValue> values = ((EnumeratedDimensionValues) dimensionValues).getValues();

                    if (values != null && !values.isEmpty()) {
                        EnumeratedDimensionValue firstValue = values.get(0);
                        return extractUrnCodelistFromUrnCode(firstValue.getUrn());
                    }
                }
            }
        }

        // spatial attribute
        List<String> extractSpatialValues = extractCodeUrnOfSpecificTypeAttribute(getAttributes(), getData().getAttributes(), ComponentType.SPATIAL);
        if (extractSpatialValues != null && !extractSpatialValues.isEmpty()) {
            return extractUrnCodelistFromUrnCode(extractSpatialValues.get(0));
        }

        return null;
    }

    protected List<String> extractHeading() {
        List<String> result = new LinkedList<String>();

        for (String dimensionId : getRelatedDsd().getHeading().getDimensionIds()) {
            result.add(dimensionId);
        }

        return result;
    }

    protected List<String> extractStub() {
        List<String> result = new LinkedList<String>();

        for (String dimensionId : getRelatedDsd().getStub().getDimensionIds()) {
            result.add(dimensionId);
        }

        return result;
    }

    public String extractTemporalVariable() {
        return extractSpecificDimensionFromDimensions(getDimensions(), DimensionType.TIME_DIMENSION);
    }

    public String extractTemporalValue() {
        return extractSpecificAttributeValuesByType(getAttributes(), getData().getAttributes(), ComponentType.TEMPORAL);
    }

    public List<String> extractSpatialVariableList() {
        List<String> result = new ArrayList<>(1);
        String extractSpatialVariable = extractSpatialVariable();
        if (!StringUtils.isEmpty(extractSpatialVariable)) {
            result.add(extractSpatialVariable);
        }
        return result;
    }

    private String extractSpatialVariable() {
        return extractSpecificDimensionFromDimensions(getDimensions(), DimensionType.GEOGRAPHIC_DIMENSION);
    }

    public GeographicalValueDto extractGeographicalValueDto() throws MetamacException {
        String extractSpatialValue = extractSpatialValue();
        if (StringUtils.isEmpty(extractSpatialValue)) {
            return null;
        }

        String variableElementCode = getVariableElementByCodeUrn(extractSpatialValue);

        // Retrieve
        GeographicalValue geographicalValue = getGeographicalValueRepository().findGeographicalValueByCode(variableElementCode);
        if (geographicalValue == null) {
            throw new MetamacException(ServiceExceptionType.GEOGRAPHICAL_VALUE_NOT_FOUND_WITH_CODE, variableElementCode);
        }

        return getDo2DtoMapper().geographicalValueDoToDto(geographicalValue);
    }

    private String extractSpatialValue() {
        return extractSpecificAttributeValuesByType(getAttributes(), getData().getAttributes(), ComponentType.SPATIAL);
    }

    private String getVariableElementByCodeUrn(String spatialValue) throws MetamacException {
        List<String> spatialAttributeCodeUrn = extractCodeUrnOfSpecificTypeAttribute(getAttributes(), getData().getAttributes(), ComponentType.SPATIAL);

        if (spatialAttributeCodeUrn.isEmpty()) {
            throw new MetamacException(ServiceExceptionType.GEOGRAPHICAL_VALUE_NOT_FOUND_WITH_CODE, spatialValue);
        }

        Code code = srmRestInternalService.retrieveCodeOfCodelist(spatialAttributeCodeUrn.get(0));

        if (code != null && code.getVariableElement() != null && code.getVariableElement().getId() != null) {
            return code.getVariableElement().getId();
        } else {
            throw new MetamacException(ServiceExceptionType.GEOGRAPHICAL_VARIABLE_ELEMENT_NOT_FOUND_WITH_CODE, spatialValue);
        }

    }

    public static String getUrnWithoutVersion(String datasetVersionUrn) {
        String[] params = splitUrnItemScheme(datasetVersionUrn);
        String[] agencyId = {params[0]};
        String resourceId = params[1];

        return GeneratorUrnUtils.generateSiemacStatisticalResourceDatasetUrn(agencyId, resourceId);
    }

}
