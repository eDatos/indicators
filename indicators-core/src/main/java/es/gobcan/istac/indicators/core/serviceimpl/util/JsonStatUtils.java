package es.gobcan.istac.indicators.core.serviceimpl.util;

import static org.siemac.edatos.core.common.constants.shared.RegularExpressionConstants.REG_EXP_URL;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.gobcan.istac.indicators.core.domain.Data;
import es.gobcan.istac.indicators.core.domain.DataContent;
import es.gobcan.istac.indicators.core.domain.jsonstat.JsonStatData;
import es.gobcan.istac.indicators.core.enume.domain.QueryEnvironmentEnum;

public class JsonStatUtils {

    private static final Logger LOG                 = LoggerFactory.getLogger(JsonStatUtils.class);

    private static final String OPERATION_SEPARATOR = "_";

    private JsonStatUtils() {
    }

    public static Data jsonStatDataToData(String uuid, JsonStatData jsonStatData) {
        if (jsonStatData == null) {
            return null;
        }

        Data target = new Data();

        // TODO EDATOS-3380 Aclarar con Rita/Javi? si estos mapeos son correctos y ver que pasa con los que faltan

        // JSON-stat
        target.setQueryEnvironmentEnum(QueryEnvironmentEnum.JSON_STAT);

        // Uuid
        target.setUuid(uuid);

        // Title
        target.setTitle(jsonStatData.getLabel());

        // PX Uri
        // target.setPxUri(pxUri);

        // Stub: Not necessary in JSON-stat
        // target.setStub(extractStub(jsonStatData));

        // Heading: Not necessary in JSON-stat
        // target.setHeading(extractHeading(jsonStatData));

        // Value Labels
        target.setValueLabels(jsonStatData.getValueLabels());

        // Value Codes
        target.setValueCodes(jsonStatData.getValueCodes());

        // Temporal Variables
        target.setTemporalVariable(jsonStatData.getTemporalVariable());

        // Temporal Value: Not necessary in JSON-stat
        // target.setTemporalValue(temporalValue);

        // Spatial Variables spatialVariables
        target.setSpatialVariables(JsonStatUtils.toList(jsonStatData.getSpatialVariable()));

        // Spatial Variables geographicalValueDto: Not necessary in JSON-stat
        // target.setGeographicalValueDto(geographicalValueDto);

        // Cont Variable
        target.setContVariable(jsonStatData.getContVariable());

        // Notes: We do not need it for calculations.

        // Source: We do not need it for calculations.

        // Survey Code
        target.setSurveyCode(JsonStatUtils.getOperationCode(jsonStatData.getSurveyCode()));

        // Survey Title
        target.setSurveyTitle(jsonStatData.getSurveyTitle());

        // Publishers
        target.setPublishers(JsonStatUtils.toList(jsonStatData.getSource()));

        // Data
        target.processData(jsonStatDataValuesToDataContent(jsonStatData));

        // VariablesInOrder
        target.setVariablesInOrder(extractVariablesFromDimensions(jsonStatData));

        return target;
    }

    private static List<String> extractVariablesFromDimensions(JsonStatData jsonStatData) {
        List<String> result = new ArrayList<>();
        for (String dimensionId : jsonStatData.getId()) {
            result.add(jsonStatData.getDimension(dimensionId).getLabel());
        }

        return result;
    }

    public static List<DataContent> jsonStatDataValuesToDataContent(JsonStatData jsonStatData) {
        List<DataContent> result = new LinkedList<DataContent>();

        int numDimensions = jsonStatData.getId().size();

        Stack<DataOrderingStackElement> stack = new Stack<DataOrderingStackElement>();
        stack.push(new DataOrderingStackElement(null, -1, null, new LinkedList<>()));

        JsonStatDatasetAccess jsonStatDatasetAccess = new JsonStatDatasetAccess(jsonStatData);

        int observationIndex = 0;
        while (stack.size() > 0) {
            DataOrderingStackElement elem = stack.pop();

            int dimensionPosition = elem.getDimensionPosition();
            List<String> dimCodes = elem.getDimCodes();

            if (dimCodes.size() == numDimensions) {
                DataContent dataContent = new DataContent();
                dataContent.setDimCodes(dimCodes);
                dataContent.setValue(jsonStatDatasetAccess.getObservations()[observationIndex++]);
                result.add(dataContent);
            } else {
                String dimensionId = jsonStatDatasetAccess.getDimensionsOrderedForData().get(dimensionPosition + 1);
                List<String> dimensionValues = jsonStatDatasetAccess.getDimensionValuesOrderedForData(dimensionId);
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

    public static String getOperationCode(String surveyCode) {
        if (surveyCode != null) {
            return surveyCode.split(OPERATION_SEPARATOR)[0];
        }

        return null;
    }

    public static List<String> toList(String value) {
        List<String> returnedList = new ArrayList();

        if (value != null) {
            returnedList.add(value);
        }

        return returnedList;
    }

    public static boolean checkUuidIsUrl(String uuid) {
        return uuid != null && uuid.matches(REG_EXP_URL);
    }

}
