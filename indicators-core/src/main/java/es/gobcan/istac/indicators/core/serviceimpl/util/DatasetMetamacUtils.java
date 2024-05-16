package es.gobcan.istac.indicators.core.serviceimpl.util;

import static org.siemac.edatos.core.common.util.GeneratorUrnUtils.generateSdmxCodelistUrn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dataset;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Code;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.AttributeValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ComponentType;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Data;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataAttribute;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataAttributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DatasetMetadataBase;
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

import es.gobcan.istac.indicators.core.domain.DataContent;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.GeographicalValueRepository;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.enume.domain.MetamacSelectionEnum;
import es.gobcan.istac.indicators.core.enume.domain.QueryEnvironmentEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.mapper.Do2DtoMapper;
import es.gobcan.istac.indicators.core.service.SrmRestInternalService;

public class DatasetMetamacUtils {

    private Map<String, String> variableElementsByCode = new HashMap<String, String>();

    private SrmRestInternalService srmRestInternalService;

    public DatasetMetamacUtils(SrmRestInternalService srmRestInternalService) {
        this.srmRestInternalService = srmRestInternalService;
    }

    public es.gobcan.istac.indicators.core.domain.Data datasetMetamacToData(Dataset dataset) throws IOException, MetamacException {
        if (dataset == null) {
            return null;
        }

        es.gobcan.istac.indicators.core.domain.Data target = new es.gobcan.istac.indicators.core.domain.Data();

        // Metamac
        target.setQueryEnvironmentEnum(QueryEnvironmentEnum.METAMAC);

        target.setMetamacResourceType(MetamacSelectionEnum.DATASET);

        // UUid
        target.setUuid(dataset.getUrn());

        // Title
        target.setTitle(extractValueForDefaultLanguage(dataset.getName()));

        // PX Uri
        target.setPxUri(dataset.getUrn());

        // Stub
        target.setStub(extractStub(dataset.getMetadata()));

        // Heading
        target.setHeading(extractHeading(dataset.getMetadata()));

        // Value Labels
        target.setValueLabels(extractValuesCoverages(dataset.getMetadata()));

        // Value Codes
        target.setValueCodes(extractCodesCoverages(dataset.getMetadata()));

        // Temporal Variables
        target.setTemporalVariable(extractTemporalVariable(dataset.getMetadata()));

        // Temporal Value
        target.setTemporalValue(extractTemporalValue(dataset));

        // Spatial Variables
        target.setSpatialVariables(extractSpatialVariableList(dataset.getMetadata()));
        target.setGeographicalValueDto(extractGeographicalValueDto(dataset));

        // Cont Variable
        target.setContVariable(extractContVariable(dataset.getMetadata()));

        // Notes: We do not need it for calculations.

        // Source: We do not need it for calculations.

        Resource statisticalOperation = dataset.getMetadata().getStatisticalOperation();

        // Survey Code
        target.setSurveyCode(statisticalOperation.getId());

        // Survey Title
        target.setSurveyTitle(extractValueForDefaultLanguage(statisticalOperation.getName()));

        // Publishers
        Resource maintainer = dataset.getMetadata().getMaintainer();
        String extractValueForDefaultLanguage = extractValueForDefaultLanguage(maintainer.getName());
        if (StringUtils.isEmpty(extractValueForDefaultLanguage)) {
            target.setPublishers(Collections.emptyList());
        } else {
            target.setPublishers(Arrays.asList(extractValueForDefaultLanguage));
        }

        DatasetMetamacDatasetAccess datasetMetamacDatasetAccess = new DatasetMetamacDatasetAccess(dataset, variableElementsByCode, null);
        // Data
        target.processData(extractData(dataset, target.getSpatialVariables(), datasetMetamacDatasetAccess));

        target.processObservationsAttributesMap(datasetMetamacDatasetAccess.getAttributesMetadataMap());

        // VariablesInOrder
        target.setVariablesInOrder(extractVariablesFromDimensions(dataset.getMetadata().getDimensions()));

        return target;
    }

    private List<String> extractHeading(DatasetMetadataBase metadata) {
        List<String> result = new LinkedList<String>();

        for (String dimensionId : metadata.getRelatedDsd().getHeading().getDimensionIds()) {
            result.add(dimensionId);
        }

        return result;
    }

    private List<String> extractStub(DatasetMetadataBase metadata) {
        List<String> result = new LinkedList<String>();

        for (String dimensionId : metadata.getRelatedDsd().getStub().getDimensionIds()) {
            result.add(dimensionId);
        }

        return result;
    }

    public String extractTemporalVariable(DatasetMetadataBase metadata) {
        return extractSpecificDimensionFromDimensions(metadata.getDimensions(), DimensionType.TIME_DIMENSION);
    }

    public String extractTemporalValue(Dataset dataset) {
        return extractSpecificAttributeValuesByType(dataset.getMetadata().getAttributes(), dataset.getData().getAttributes(), ComponentType.TEMPORAL);
    }

    public List<String> extractSpatialVariableList(DatasetMetadataBase metadata) {
        List<String> result = new ArrayList<>(1);
        String extractSpatialVariable = extractSpatialVariable(metadata);
        if (!StringUtils.isEmpty(extractSpatialVariable)) {
            result.add(extractSpatialVariable);
        }
        return result;
    }

    private String extractSpatialVariable(DatasetMetadataBase metadata) {
        return extractSpecificDimensionFromDimensions(metadata.getDimensions(), DimensionType.GEOGRAPHIC_DIMENSION);
    }

    private String extractSpatialValue(Dataset dataset) {
        return extractSpecificAttributeValuesByType(dataset.getMetadata().getAttributes(), dataset.getData().getAttributes(), ComponentType.SPATIAL);
    }

    private String getVariableElementByCodeUrn(Dataset dataset, String spatialValue) throws MetamacException {
        List<String> spatialAttributeCodeUrn = extractCodeUrnOfSpecificTypeAttribute(dataset.getMetadata().getAttributes(), dataset.getData().getAttributes(), ComponentType.SPATIAL);

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

    public GeographicalValueDto extractGeographicalValueDto(Dataset dataset) throws MetamacException {
        String extractSpatialValue = extractSpatialValue(dataset);
        if (StringUtils.isEmpty(extractSpatialValue)) {
            return null;
        }

        String variableElementCode = getVariableElementByCodeUrn(dataset, extractSpatialValue);

        // Retrieve
        GeographicalValue geographicalValue = getGeographicalValueRepository().findGeographicalValueByCode(variableElementCode);
        if (geographicalValue == null) {
            throw new MetamacException(ServiceExceptionType.GEOGRAPHICAL_VALUE_NOT_FOUND_WITH_CODE, variableElementCode);
        }

        return getDo2DtoMapper().geographicalValueDoToDto(geographicalValue);
    }

    public String extractContVariable(DatasetMetadataBase metadata) {
        return extractSpecificDimensionFromDimensions(metadata.getDimensions(), DimensionType.MEASURE_DIMENSION);
    }

    public String extractValueForDefaultLanguage(InternationalString internationalString) {
        // Find in Dimensions or Attributes
        if (internationalString != null && !internationalString.getTexts().isEmpty()) {
            // Only one locale was received in the API, the default locale. Therefore, this code is valid.
            LocalisedString localisedString = internationalString.getTexts().iterator().next();
            return localisedString.getValue();
        }
        return null;
    }

    public List<String> extractVariablesFromDimensions(Dimensions dimensions) {
        List<String> result = new ArrayList<String>();
        for (Dimension dimension : dimensions.getDimensions()) {
            result.add(dimension.getId());
        }

        return result;
    }

    private String extractSpecificDimensionFromDimensions(Dimensions dimensions, DimensionType dimensionType) {
        for (Dimension dimension : dimensions.getDimensions()) {
            if (dimensionType.equals(dimension.getType())) {
                return dimension.getId();
            }
        }

        return null;
    }

    private List<String> extractCodeUrnOfSpecificTypeAttribute(Attributes attributes, DataAttributes dataAttributes, ComponentType componentType) {
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

    public String extractSpecificAttributeValuesByType(Attributes attributes, DataAttributes dataAttributes, ComponentType componentType) {
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

    public String extractGeographicalCodelistUrn(Dataset dataset) {

        // first try dimension
        Dimensions dimensions = dataset.getMetadata().getDimensions();

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
        List<String> extractSpatialValues = extractCodeUrnOfSpecificTypeAttribute(dataset.getMetadata().getAttributes(), dataset.getData().getAttributes(), ComponentType.SPATIAL);
        if (extractSpatialValues != null && !extractSpatialValues.isEmpty()) {
            return extractUrnCodelistFromUrnCode(extractSpatialValues.get(0));
        }

        return null;
    }

    private String extractUrnCodelistFromUrnCode(String urnCode) {
        String[] params = UrnUtils.splitUrnItem(urnCode);
        String agencyId = params[0];
        String resourceId = params[1];
        String version = params[2];

        String[] agenciesID = agencyId.contains(".") ? agencyId.split(".") : new String[]{agencyId};
        return generateSdmxCodelistUrn(agenciesID, resourceId, version);
    }

    public Map<String, List<String>> extractCodesCoverages(DatasetMetadataBase metadata) {
        return extractCoverages(metadata, false);

    }

    public Map<String, List<String>> extractValuesCoverages(DatasetMetadataBase metadata) {
        return extractCoverages(metadata, true);

    }

    private Map<String, List<String>> extractCoverages(DatasetMetadataBase metadata, boolean trylabels) {
        Map<String, List<String>> result = new HashMap<String, List<String>>();

        Dimensions dimensions = metadata.getDimensions();

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
                        extractValue = extractValueForDefaultLanguage(nonEnumeratedDimensionValue.getName());
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
                        extractValue = extractValueForDefaultLanguage(name);
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

    private List<DataContent> extractData(Dataset dataset, List<String> geographicalDimensionsId, DatasetMetamacDatasetAccess datasetMetamacDatasetAccess) throws MetamacException {

        List<DataContent> result = new LinkedList<DataContent>();

        Data data = dataset.getData();

        if (data.getDimensions() == null) {
            return result;
        }

        int numDimensions = data.getDimensions().getDimensions().size();
        datasetMetamacDatasetAccess = new DatasetMetamacDatasetAccess(dataset, variableElementsByCode, geographicalDimensionsId);

        Stack<DataOrderingStackElement> stack = new Stack<DataOrderingStackElement>();
        stack.push(new DataOrderingStackElement(null, -1, null, new LinkedList<>()));

        int observationIndex = 0;
        while (stack.size() > 0) {
            DataOrderingStackElement elem = stack.pop();

            int dimensionPosition = elem.getDimensionPosition();
            List<String> dimCodes = elem.getDimCodes();

            if (dimCodes.size() == numDimensions) {
                int index = observationIndex++;
                // We have all dimensions here
                DataContent dataContent = new DataContent();
                dataContent.setDimCodes(dimCodes);
                dataContent.setValue(datasetMetamacDatasetAccess.getObservations()[index]);
                dataContent.setAttributesObservations(datasetMetamacDatasetAccess.getObservationsAttributes(index));
                result.add(dataContent);
            } else {
                String dimensionId = datasetMetamacDatasetAccess.getDimensionsOrderedForData().get(dimensionPosition + 1);
                List<String> dimensionValues = datasetMetamacDatasetAccess.getDimensionValuesOrderedForData(dimensionId);
                for (int i = dimensionValues.size() - 1; i >= 0; i--) {
                    LinkedList<String> nextDimCodes = new LinkedList<String>();
                    nextDimCodes.addAll(dimCodes);
                    nextDimCodes.add(dimensionValues.get(i));
                    DataOrderingStackElement temp = new DataOrderingStackElement(dimensionId, dimensionPosition + 1, dimensionValues.get(i), nextDimCodes);
                    stack.push(temp);
                }
            }
        }

        return result;
    }

    public GeographicalValueRepository getGeographicalValueRepository() {
        return ApplicationContextProvider.getApplicationContext().getBean(GeographicalValueRepository.class);
    }

    public Do2DtoMapper getDo2DtoMapper() {
        return ApplicationContextProvider.getApplicationContext().getBean(Do2DtoMapper.class);
    }

}
