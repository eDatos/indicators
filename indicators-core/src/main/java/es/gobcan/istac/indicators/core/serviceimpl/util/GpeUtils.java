package es.gobcan.istac.indicators.core.serviceimpl.util;

import static org.siemac.edatos.core.common.constants.shared.RegularExpressionConstants.REG_EXP_URL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.gobcan.istac.indicators.core.domain.Data;
import es.gobcan.istac.indicators.core.domain.DataGpe;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

public class GpeUtils {

    private static final Logger LOG                 = LoggerFactory.getLogger(GpeUtils.class);

    private static final String OPERATION_SEPARATOR = "_";

    private GpeUtils() {
    }

    public static Data GpeDataToData(DataGpe data, Map<String, String> variableElementsByCodesOfCodelist) throws MetamacException {
        if (data == null) {
            return null;
        }

        // Value Codes
        data.setValueCodes(convertValueCodesToVariableElementCodes(data.getValueCodes(), data.getSpatialVariables(), variableElementsByCodesOfCodelist));

        // Data
        data.processData(variableElementsByCodesOfCodelist);

        return data;
    }

    private static Map<String, List<String>> convertValueCodesToVariableElementCodes(Map<String, List<String>> valueCodes, List<String> geographicalDimensionsId,
            Map<String, String> variableElementsByCodesOfCodelist) throws MetamacException {
        if (geographicalDimensionsId != null) {
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
        }

        return valueCodes;
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

    /*
     * Private methods that get data from jaxi
     */
    public static DataGpe jsonGpeToData(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, DataGpe.class);
    }

}
