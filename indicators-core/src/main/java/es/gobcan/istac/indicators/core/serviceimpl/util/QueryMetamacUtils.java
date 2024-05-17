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
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Query;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.Code;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.ComponentType;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Data;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Dimension;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionType;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DimensionValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Dimensions;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedDimensionValue;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.EnumeratedDimensionValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.NonEnumeratedDimensionValue;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.NonEnumeratedDimensionValues;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.QueryMetadataBase;

import es.gobcan.istac.indicators.core.domain.DataContent;
import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.enume.domain.MetamacSelectionEnum;
import es.gobcan.istac.indicators.core.enume.domain.QueryEnvironmentEnum;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;
import es.gobcan.istac.indicators.core.service.SrmRestInternalService;

public class QueryMetamacUtils {

    private Map<String, String> variableElementsByCode = new HashMap<String, String>();

    private SrmRestInternalService srmRestInternalService;

    public QueryMetamacUtils(SrmRestInternalService srmRestInternalService) {
        this.srmRestInternalService = srmRestInternalService;
    }

    public es.gobcan.istac.indicators.core.domain.Data queryMetamacToData(Query query) throws IOException, MetamacException {
        if (query == null) {
            return null;
        }

        es.gobcan.istac.indicators.core.domain.Data target = new es.gobcan.istac.indicators.core.domain.Data();

        // Metamac
        target.setQueryEnvironmentEnum(QueryEnvironmentEnum.METAMAC);

        target.setMetamacResourceType(MetamacSelectionEnum.CONSULTA);

        // UUid
        target.setUuid(query.getUrn());

        // Title
        target.setTitle(CommonMetamacUtils.extractValueForDefaultLanguage(query.getName()));

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
        target.setSurveyTitle(CommonMetamacUtils.extractValueForDefaultLanguage(statisticalOperation.getName()));

        // Publishers
        Resource maintainer = query.getMetadata().getMaintainer();
        String extractValueForDefaultLanguage = CommonMetamacUtils.extractValueForDefaultLanguage(maintainer.getName());
        if (StringUtils.isEmpty(extractValueForDefaultLanguage)) {
            target.setPublishers(Collections.emptyList());
        } else {
            target.setPublishers(Arrays.asList(extractValueForDefaultLanguage));
        }

        QueryMetamacDatasetAccess queryMetamacDatasetAccess = new QueryMetamacDatasetAccess(query, variableElementsByCode, null);
        // Data
        target.processData(extractData(query, target.getSpatialVariables(), queryMetamacDatasetAccess));

        target.processObservationsAttributesMap(queryMetamacDatasetAccess.getAttributesMetadataMap());

        // VariablesInOrder
        target.setVariablesInOrder(CommonMetamacUtils.extractVariablesFromDimensions(query.getMetadata().getDimensions()));

        return target;
    }

    private List<String> extractHeading(QueryMetadataBase metadata) {
        List<String> result = new LinkedList<String>();

        for (String dimensionId : metadata.getRelatedDsd().getHeading().getDimensionIds()) {
            result.add(dimensionId);
        }

        return result;
    }

    private List<String> extractStub(QueryMetadataBase metadata) {
        List<String> result = new LinkedList<String>();

        for (String dimensionId : metadata.getRelatedDsd().getStub().getDimensionIds()) {
            result.add(dimensionId);
        }

        return result;
    }

    public String extractTemporalVariable(QueryMetadataBase metadata) {
        return CommonMetamacUtils.extractSpecificDimensionFromDimensions(metadata.getDimensions(), DimensionType.TIME_DIMENSION);
    }

    public String extractTemporalValue(Query query) {
        return CommonMetamacUtils.extractSpecificAttributeValuesByType(query.getMetadata().getAttributes(), query.getData().getAttributes(), ComponentType.TEMPORAL);
    }

    public List<String> extractSpatialVariableList(QueryMetadataBase metadata) {
        List<String> result = new ArrayList<>(1);
        String extractSpatialVariable = extractSpatialVariable(metadata);
        if (!StringUtils.isEmpty(extractSpatialVariable)) {
            result.add(extractSpatialVariable);
        }
        return result;
    }

    private String extractSpatialVariable(QueryMetadataBase metadata) {
        return CommonMetamacUtils.extractSpecificDimensionFromDimensions(metadata.getDimensions(), DimensionType.GEOGRAPHIC_DIMENSION);
    }

    private String extractSpatialValue(Query query) {
        return CommonMetamacUtils.extractSpecificAttributeValuesByType(query.getMetadata().getAttributes(), query.getData().getAttributes(), ComponentType.SPATIAL);
    }

    private String getVariableElementByCodeUrn(Query query, String spatialValue) throws MetamacException {
        List<String> spatialAttributeCodeUrn = CommonMetamacUtils.extractCodeUrnOfSpecificTypeAttribute(query.getMetadata().getAttributes(), query.getData().getAttributes(), ComponentType.SPATIAL);

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

    public GeographicalValueDto extractGeographicalValueDto(Query query) throws MetamacException {
        String extractSpatialValue = extractSpatialValue(query);
        if (StringUtils.isEmpty(extractSpatialValue)) {
            return null;
        }

        String variableElementCode = getVariableElementByCodeUrn(query, extractSpatialValue);

        // Retrieve
        GeographicalValue geographicalValue = CommonMetamacUtils.getGeographicalValueRepository().findGeographicalValueByCode(variableElementCode);
        if (geographicalValue == null) {
            throw new MetamacException(ServiceExceptionType.GEOGRAPHICAL_VALUE_NOT_FOUND_WITH_CODE, variableElementCode);
        }

        return CommonMetamacUtils.getDo2DtoMapper().geographicalValueDoToDto(geographicalValue);
    }

    public String extractContVariable(QueryMetadataBase metadata) {
        return CommonMetamacUtils.extractSpecificDimensionFromDimensions(metadata.getDimensions(), DimensionType.MEASURE_DIMENSION);
    }

    public String extractGeographicalCodelistUrn(Query query) {

        // first try dimension
        Dimensions dimensions = query.getMetadata().getDimensions();

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
                        return CommonMetamacUtils.extractUrnCodelistFromUrnCode(firstValue.getUrn());
                    }
                }
            }
        }

        // spatial attribute
        List<String> extractSpatialValues = CommonMetamacUtils.extractCodeUrnOfSpecificTypeAttribute(query.getMetadata().getAttributes(), query.getData().getAttributes(), ComponentType.SPATIAL);
        if (extractSpatialValues != null && !extractSpatialValues.isEmpty()) {
            return CommonMetamacUtils.extractUrnCodelistFromUrnCode(extractSpatialValues.get(0));
        }

        return null;
    }

    public Map<String, List<String>> extractCodesCoverages(QueryMetadataBase metadata) {
        return extractCoverages(metadata, false);

    }

    public Map<String, List<String>> extractValuesCoverages(QueryMetadataBase metadata) {
        return extractCoverages(metadata, true);

    }

    private Map<String, List<String>> extractCoverages(QueryMetadataBase metadata, boolean trylabels) {
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
                        extractValue = CommonMetamacUtils.extractValueForDefaultLanguage(nonEnumeratedDimensionValue.getName());
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
                        extractValue = CommonMetamacUtils.extractValueForDefaultLanguage(name);
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

    private List<DataContent> extractData(Query query, List<String> geographicalDimensionsId, QueryMetamacDatasetAccess queryMetamacDatasetAccess) throws MetamacException {

        List<DataContent> result = new LinkedList<DataContent>();

        Data data = query.getData();

        if (data.getDimensions() == null) {
            return result;
        }

        int numDimensions = data.getDimensions().getDimensions().size();
        queryMetamacDatasetAccess = new QueryMetamacDatasetAccess(query, variableElementsByCode, geographicalDimensionsId);

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
                dataContent.setValue(queryMetamacDatasetAccess.getObservations()[index]);
                dataContent.setAttributesObservations(queryMetamacDatasetAccess.getObservationsAttributes(index));
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

}
