package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.common.v1_0.domain.Resource;
import org.siemac.metamac.rest.statistical_resources.v1_0.domain.Dataset;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Attributes;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Data;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.DataStructureDefinition;
import org.siemac.metamac.statistical_resources.rest.common.v1_0.domain.Dimensions;

import es.gobcan.istac.indicators.core.domain.DataContent;
import es.gobcan.istac.indicators.core.enume.domain.MetamacSelectionEnum;
import es.gobcan.istac.indicators.core.enume.domain.QueryEnvironmentEnum;
import es.gobcan.istac.indicators.core.service.SrmRestInternalService;

public class DatasetMetamacUtils extends CommonMetamacUtils {

    protected Dataset dataset;

    public DatasetMetamacUtils(SrmRestInternalService srmRestInternalService, Dataset dataset) {
        this.srmRestInternalService = srmRestInternalService;
        this.dataset = dataset;
    }

    public es.gobcan.istac.indicators.core.domain.Data datasetMetamacToData() throws IOException, MetamacException {
        if (this.dataset == null) {
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

    @Override
    protected Dimensions getDimensions() {
        return this.dataset.getMetadata().getDimensions();
    }
    @Override
    protected Attributes getAttributes() {
        return this.dataset.getMetadata().getAttributes();
    }
    @Override
    protected DataStructureDefinition getRelatedDsd() {
        return this.dataset.getMetadata().getRelatedDsd();
    }
    @Override
    protected Data getData() {
        return this.dataset.getData();
    }
}
