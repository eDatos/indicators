package es.gobcan.istac.indicators.core.serviceimpl.util;

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
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Attribute;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Attributes;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.ComponentType;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Data;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.DataAttribute;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.DataAttributes;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dimension;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.DimensionType;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.DimensionValues;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dimensions;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.EnumeratedDimensionValue;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.EnumeratedDimensionValues;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.NonEnumeratedDimensionValue;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.NonEnumeratedDimensionValues;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Query;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.QueryMetadata;

import es.gobcan.istac.indicators.core.domain.DataContent;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.domain.GeographicalValueRepository;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.enume.domain.QueryEnvironmentEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.mapper.Do2DtoMapper;

public class QueryMetamacUtils {

    public static es.gobcan.istac.indicators.core.domain.Data queryMetamacToData(Query query) throws IOException, MetamacException {
        if (query == null) {
            return null;
        }

        es.gobcan.istac.indicators.core.domain.Data target = new es.gobcan.istac.indicators.core.domain.Data();

        // Metamac
        target.setQueryEnvironmentEnum(QueryEnvironmentEnum.METAMAC);

        // UUid
        target.setUuid(query.getUrn());

        // Title
        target.setTitle(extractValueForDefaultLanguage(query.getName()));

        // PX Uri
        target.setPxUri(query.getUrn());

        // Stub
        target.setStub(extractStub(query.getMetadata()));

        // Heading
        target.setHeading(extractHeading(query.getMetadata()));

        // Value Labels
        target.setValueLabels(extractValuesCoverages(query.getMetadata()));

        // Value Codes
        target.setValueCodes(extractCodesCoverages(query.getMetadata()));

        // Temporal Variables
        target.setTemporalVariable(extractTemporalVariable(query.getMetadata()));

        // Temporal Value
        target.setTemporalValue(extractTemporalValue(query));

        // Spatial Variables
        target.setSpatialVariables(extractSpatialVariableList(query.getMetadata()));
        target.setGeographicalValueDto(extractGeographicalValueDto(query));

        // Cont Variable
        target.setContVariable(extractContVariable(query.getMetadata()));

        // Notes: We do not need it for calculations.

        // Source: We do not need it for calculations.

        Resource statisticalOperation = query.getMetadata().getStatisticalOperation();

        // Survey Code
        target.setSurveyCode(statisticalOperation.getId());

        // Survey Title
        target.setSurveyTitle(extractValueForDefaultLanguage(statisticalOperation.getName()));

        // Publishers
        Resource maintainer = query.getMetadata().getMaintainer();
        String extractValueForDefaultLanguage = extractValueForDefaultLanguage(maintainer.getName());
        if (StringUtils.isEmpty(extractValueForDefaultLanguage)) {
            target.setPublishers(Collections.emptyList());
        } else {
            target.setPublishers(Arrays.asList(extractValueForDefaultLanguage));
        }

        // Data
        target.processData(extractData(query));

        // VariablesInOrder
        target.setVariablesInOrder(QueryMetamacUtils.extractVariablesFromDimensions(query.getMetadata().getDimensions()));

        return target;
    }

    private static List<String> extractHeading(QueryMetadata metadata) {
        List<String> result = new LinkedList<String>();

        for (String dimensionId : metadata.getRelatedDsd().getHeading().getDimensionIds()) {
            result.add(dimensionId);
        }

        return result;
    }

    private static List<String> extractStub(QueryMetadata metadata) {
        List<String> result = new LinkedList<String>();

        for (String dimensionId : metadata.getRelatedDsd().getStub().getDimensionIds()) {
            result.add(dimensionId);
        }

        return result;
    }

    public static String extractTemporalVariable(QueryMetadata metadata) {
        return extractSpecificDimensionFromDimensions(metadata.getDimensions(), DimensionType.TIME_DIMENSION);
    }

    public static String extractTemporalValue(Query query) {
        return extractSpecificAttributeValuesByType(query.getMetadata().getAttributes(), query.getData().getAttributes(), ComponentType.TEMPORAL);
    }

    public static List<String> extractSpatialVariableList(QueryMetadata metadata) {
        List<String> result = new ArrayList<>(1);
        String extractSpatialVariable = extractSpatialVariable(metadata);
        if (!StringUtils.isEmpty(extractSpatialVariable)) {
            result.add(extractSpatialVariable);
        }
        return result;
    }

    private static String extractSpatialVariable(QueryMetadata metadata) {
        return extractSpecificDimensionFromDimensions(metadata.getDimensions(), DimensionType.GEOGRAPHIC_DIMENSION);
    }

    private static String extractSpatialValue(Query query) {
        return extractSpecificAttributeValuesByType(query.getMetadata().getAttributes(), query.getData().getAttributes(), ComponentType.SPATIAL);
    }

    public static GeographicalValueDto extractGeographicalValueDto(Query query) throws MetamacException {
        String extractSpatialValue = extractSpatialValue(query);
        if (StringUtils.isEmpty(extractSpatialValue)) {
            return null;
        }

        // Retrieve
        GeographicalValue geographicalValue = getGeographicalValueRepository().findGeographicalValueByCode(extractSpatialValue);
        if (geographicalValue == null) {
            throw new MetamacException(ServiceExceptionType.GEOGRAPHICAL_VALUE_NOT_FOUND_WITH_CODE, extractSpatialValue);
        }

        return getDo2DtoMapper().geographicalValueDoToDto(geographicalValue);
    }

    public static String extractContVariable(QueryMetadata metadata) {
        return extractSpecificDimensionFromDimensions(metadata.getDimensions(), DimensionType.MEASURE_DIMENSION);
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

    public static List<String> extractVariablesFromDimensions(Dimensions dimensions) {
        List<String> result = new ArrayList<String>();
        for (Dimension dimension : dimensions.getDimensions()) {
            result.add(dimension.getId());
        }

        return result;
    }

    private static String extractSpecificDimensionFromDimensions(Dimensions dimensions, DimensionType dimensionType) {
        for (Dimension dimension : dimensions.getDimensions()) {
            if (dimensionType.equals(dimension.getType())) {
                return dimension.getId();
            }
        }

        return null;
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

    public static Map<String, List<String>> extractCodesCoverages(QueryMetadata metadata) {
        return extractCoverages(metadata, false);

    }

    public static Map<String, List<String>> extractValuesCoverages(QueryMetadata metadata) {
        return extractCoverages(metadata, true);

    }

    private static Map<String, List<String>> extractCoverages(QueryMetadata metadata, boolean trylabels) {
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
                    if (trylabels) {
                        extractValue = extractValueForDefaultLanguage(enumeratedDimensionValue.getName());
                    } else {
                        extractValue = enumeratedDimensionValue.getId();
                    }
                    valuesResult.add(extractValue);
                }

            }
            result.put(dimension.getId(), valuesResult);
        }

        return result;
    }

    private static List<DataContent> extractData(Query query) throws MetamacException {
        List<DataContent> result = new LinkedList<DataContent>();

        Data data = query.getData();

        if (data.getDimensions() == null) {
            return result;
        }

        int numDimensions = data.getDimensions().getDimensions().size();
        QueryMetamacDatasetAccess queryMetamacDatasetAccess = new QueryMetamacDatasetAccess(query);

        Stack<DataOrderingStackElement> stack = new Stack<DataOrderingStackElement>();
        stack.push(new DataOrderingStackElement(null, -1, null, new LinkedList<>()));

        int observationIndex = 0;
        while (stack.size() > 0) {
            DataOrderingStackElement elem = stack.pop();

            int dimensionPosition = elem.getDimensionPosition();
            List<String> dimCodes = elem.getDimCodes();

            if (dimCodes.size() == numDimensions) {
                // We have all dimensions here
                DataContent dataContent = new DataContent();
                dataContent.setDimCodes(dimCodes);
                dataContent.setValue(queryMetamacDatasetAccess.getObservations()[observationIndex++]);
                result.add(dataContent);
            } else {
                String dimensionId = queryMetamacDatasetAccess.getDimensionsOrderedForData().get(dimensionPosition + 1);
                List<String> dimensionValues = queryMetamacDatasetAccess.getDimensionValuesOrderedForData(dimensionId);
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

    public static GeographicalValueRepository getGeographicalValueRepository() {
        return ApplicationContextProvider.getApplicationContext().getBean(GeographicalValueRepository.class);
    }

    public static Do2DtoMapper getDo2DtoMapper() {
        return ApplicationContextProvider.getApplicationContext().getBean(Do2DtoMapper.class);
    }

}
