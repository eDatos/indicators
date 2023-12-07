package es.gobcan.istac.indicators.core.serviceimpl.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.domain.Data;
import es.gobcan.istac.indicators.core.domain.DataGpe;
import es.gobcan.istac.indicators.core.domain.DataStructure;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

public class GpeUtils {

    private GpeUtils() {
    }

    public static Data gpeDataToData(DataGpe data, Map<String, String> variableElementsByCodesOfCodelist) throws MetamacException {
        if (data == null) {
            return null;
        }

        // Value Codes
        data.setValueCodes(convertValueCodesToVariableElementCodes(data.getValueCodes(), data.getSpatialVariables(), variableElementsByCodesOfCodelist));

        // Data
        data.processData(variableElementsByCodesOfCodelist);

        return data;
    }

    public static DataStructure gpeDataStructureToDataStructure(DataStructure dataStructure, Map<String, String> variableElementsByCodesOfCodelist) throws MetamacException {
        if (dataStructure == null) {
            return null;
        }

        // Value Codes
        dataStructure.setValueCodes(convertValueCodesToVariableElementCodes(dataStructure.getValueCodes(), dataStructure.getSpatialVariables(), variableElementsByCodesOfCodelist));

        return dataStructure;
    }

    private static Map<String, List<String>> convertValueCodesToVariableElementCodes(Map<String, List<String>> valueCodes, List<String> geographicalDimensionsId,
            Map<String, String> variableElementsByCodesOfCodelist) throws MetamacException {

        if (geographicalDimensionsId == null) {
            return valueCodes;
        }

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

    /*
     * Private methods that get data from jaxi
     */
    public static DataGpe jsonGpeToData(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, DataGpe.class);
    }

}
