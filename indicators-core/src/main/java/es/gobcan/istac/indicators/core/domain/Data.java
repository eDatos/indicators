package es.gobcan.istac.indicators.core.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.siemac.metamac.core.common.exception.MetamacException;

/**
 * Data, contains values
 */
public class Data extends DataStructure {
    
    private Map<String,DataContent> data;
    
    @JsonProperty("data")
    public void processData(List<DataContent> dataList) {
        data = new HashMap<String, DataContent>();
        for (DataContent content : dataList) {
            String key = StringUtils.join(content.getDimCodes(), "#");
            data.put(key, content);
        }
    }
    
    /*
     * All variables must be selected
     */
    public DataContent getDataContent(Map<String,String> dimensionsCodes) throws MetamacException {
        List<String> dimCodes = new ArrayList<String>();
        List<String> variables = new ArrayList<String>(getStub());
        variables.addAll(getHeading());
        for (String dim: variables) {
            String dimCodeFound = null;
            for (Entry<String,String> entry : dimensionsCodes.entrySet()) {
                if (dim.equals(entry.getKey())) {
                    dimCodeFound = entry.getValue();
                }
            }
            if (dimCodeFound != null) {
                dimCodes.add(dimCodeFound);
            } else {
                return null;
            }
        }
        String key = StringUtils.join(dimCodes,"#");
        return data.get(key);
    }
    
    public boolean hasContVariable() {
        return !StringUtils.isBlank(getContVariable());
    }
}
