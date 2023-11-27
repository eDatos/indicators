package es.gobcan.istac.indicators.core.serviceimpl.util;

import static org.siemac.edatos.core.common.constants.shared.RegularExpressionConstants.REG_EXP_URL;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.gobcan.istac.indicators.core.domain.Data;
import es.gobcan.istac.indicators.core.domain.DataContent;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

public class GpeUtils {

    private static final Logger LOG                 = LoggerFactory.getLogger(GpeUtils.class);

    private static final String OPERATION_SEPARATOR = "_";

    private GpeUtils() {
    }

    public static Data GpeDataToData(String uuid, Data data, Map<String, String> variableElementsByCodesOfCodelist) throws MetamacException {
        if (data == null) {
            return null;
        }

        // Value Codes
        data.setValueCodes(convertValueCodesToVariableElementCodes(data.getValueCodes(), data.getSpatialVariables(), variableElementsByCodesOfCodelist));

        // Data
        data.processData(jsonGpeValuesToDataContent(data, data.getSpatialVariables(), variableElementsByCodesOfCodelist));

        return data;
    }

    private static Map<String, List<String>> convertValueCodesToVariableElementCodes(Map<String, List<String>> valueCodes, List<String> geographicalDimensionsId,
            Map<String, String> variableElementsByCodesOfCodelist) throws MetamacException {

        for (Map.Entry<String, List<String>> codes : valueCodes.entrySet()) {
            if (geographicalDimensionsId.contains(codes.getKey())) {
                List<String> geographicalVariableElementsCode = new ArrayList<String>();
                for (String value : codes.getValue()) {
                    String variableElement = variableElementsByCodesOfCodelist.get(value);
                    if (variableElement != null) {
                        geographicalVariableElementsCode.add(variableElement);
                    } else {
                        throw new MetamacException(ServiceExceptionType.GEOGRAPHICAL_VARIABLE_ELEMENT_NOT_FOUND_WITH_CODE, value);
                    }

                }
                codes.setValue(geographicalVariableElementsCode);
            }
        }

        return valueCodes;
    }

    public static List<DataContent> jsonGpeValuesToDataContent(Data data, List<String> geographicalDimensionsId, Map<String, String> variableElementsByCodesOfCodelist) throws MetamacException {
        List<DataContent> result = new LinkedList<DataContent>();

        int numDimensions = data.getId().size();

        Stack<DataOrderingStackElement> stack = new Stack<DataOrderingStackElement>();
        stack.push(new DataOrderingStackElement(null, -1, null, new LinkedList<>()));

        JsonStatDatasetAccess jsonStatDatasetAccess = new JsonStatDatasetAccess(jsonStatData, geographicalDimensionsId, variableElementsByCodesOfCodelist);

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
