package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Query;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Data;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataStructureDefinition;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Dimensions;

import es.gobcan.istac.edatos.dataset.repository.dto.AttributeInstanceDto;
import es.gobcan.istac.indicators.core.domain.DataContent;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorDataDimensionTypeEnum;
import es.gobcan.istac.indicators.core.enume.domain.MetamacSelectionEnum;
import es.gobcan.istac.indicators.core.enume.domain.QueryEnvironmentEnum;
import es.gobcan.istac.indicators.core.service.SrmRestInternalService;

public class QueryMetamacUtils extends CommonMetamacUtils {

    protected Query query;

    public QueryMetamacUtils(SrmRestInternalService srmRestInternalService, Query query, String languageDefault) {
        this.srmRestInternalService = srmRestInternalService;
        this.query = query;
        this.languageDefault = languageDefault;
    }

    public es.gobcan.istac.indicators.core.domain.Data queryMetamacToData() throws IOException, MetamacException {
        if (query == null) {
            return null;
        }

        es.gobcan.istac.indicators.core.domain.Data target = new es.gobcan.istac.indicators.core.domain.Data();

        // Metamac
        target.setQueryEnvironmentEnum(QueryEnvironmentEnum.METAMAC);

        target.setMetamacResourceType(MetamacSelectionEnum.QUERY);

        // UUid
        target.setUuid(query.getUrn());

        // Title
        target.setTitle(extractValueForDefaultLanguage(query.getName(), languageDefault));

        // PX Uri
        target.setPxUri(query.getUrn());

        target.setStub(extractStub());

        // Heading
        target.setHeading(extractHeading());

        // Value Labels
        target.setValueLabels(extractValuesCoverages());

        // Value Codes
        target.setValueCodes(extractCodesCoverages());

        // Temporal Variables
        target.setTemporalVariable(extractTemporalVariable());

        // Temporal Value
        target.setTemporalValue(extractTemporalValue());

        // Spatial Variables
        target.setSpatialVariables(extractSpatialVariableList());
        target.setGeographicalValueDto(extractGeographicalValueDto());

        // Cont Variable
        target.setContVariable(extractContVariable());

        // Notes: We do not need it for calculations.

        // Source: We do not need it for calculations.

        Resource statisticalOperation = query.getMetadata().getStatisticalOperation();

        // Survey Code
        target.setSurveyCode(statisticalOperation.getId());

        // Survey Title
        target.setSurveyTitle(extractValueForDefaultLanguage(statisticalOperation.getName(), languageDefault));

        // Publishers
        Resource maintainer = query.getMetadata().getMaintainer();
        String extractValueForDefaultLanguage = extractValueForDefaultLanguage(maintainer.getName(), languageDefault);
        if (StringUtils.isEmpty(extractValueForDefaultLanguage)) {
            target.setPublishers(Collections.emptyList());
        } else {
            target.setPublishers(Arrays.asList(extractValueForDefaultLanguage));
        }

        QueryMetamacDatasetAccess queryMetamacDatasetAccess = new QueryMetamacDatasetAccess(query, variableElementsByCode, null);
        // Data
        target.processData(extractData(query, target.getSpatialVariables(), queryMetamacDatasetAccess));

        target.processObservationsAttributesMap(queryMetamacDatasetAccess.getAttributesMetadataMap());

        // Dataset/Dimension attributes.
        // A separate access object initialized with the spatial dimensions is required so that
        // geographical codes are remapped to variable element IDs, consistent with how observation
        // dim-codes are stored. The outer queryMetamacDatasetAccess was created with null spatial
        // dims and therefore stores raw geo codes, which would produce inconsistent codesByDimension
        // values in the AttributeInstanceDto list.
        QueryMetamacDatasetAccess accessForAttributes = new QueryMetamacDatasetAccess(query, variableElementsByCode, target.getSpatialVariables());
        target.setDatasetAndDimensionAttributes(accessForAttributes.extractDatasetAndDimensionAttributeInstances(buildSourceDimToIndicatorDimMap()));
        target.setMultilingualAttributeIds(accessForAttributes.getMultilingualAttributeIds());

        // VariablesInOrder
        target.setVariablesInOrder(extractVariablesFromDimensions(query.getMetadata().getDimensions()));

        return target;
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

    /**
     * Builds a mapping from each source API dimension ID to its corresponding indicator dimension
     * type name (GEOGRAPHICAL, TIME, MEASURE). Used to translate source dimension IDs when
     * building DIMENSION-level {@link AttributeInstanceDto} instances.
     */
    private Map<String, String> buildSourceDimToIndicatorDimMap() {
        Map<String, String> map = new HashMap<String, String>();
        List<String> spatials = extractSpatialVariableList();
        for (String spatialDim : spatials) {
            map.put(spatialDim, IndicatorDataDimensionTypeEnum.GEOGRAPHICAL.name());
        }
        String temporalDim = extractTemporalVariable();
        if (temporalDim != null) {
            map.put(temporalDim, IndicatorDataDimensionTypeEnum.TIME.name());
        }
        String measDim = extractContVariable();
        if (measDim != null) {
            map.put(measDim, IndicatorDataDimensionTypeEnum.MEASURE.name());
        }
        return map;
    }

    @Override
    protected Dimensions getDimensions() {
        return this.query.getMetadata().getDimensions();
    }
    @Override
    protected Attributes getAttributes() {
        return this.query.getMetadata().getAttributes();
    }
    @Override
    protected Data getData() {
        return this.query.getData();
    }
    @Override
    protected DataStructureDefinition getRelatedDsd() {
        return this.query.getMetadata().getRelatedDsd();
    }
}
