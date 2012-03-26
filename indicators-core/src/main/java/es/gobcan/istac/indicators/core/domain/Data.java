package es.gobcan.istac.indicators.core.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonProperty;
import org.siemac.metamac.core.common.exception.MetamacException;

/**
 * Data, contains values
 */
public class Data extends DataStructure {
    
    private Map<Set<String>,DataContent> data;
    
    @JsonProperty("data")
    public void processData(List<DataContent> dataList) {
        data = new HashMap<Set<String>, DataContent>();
        for (DataContent content : dataList) {
            Set<String> dims = new HashSet<String>(content.getDimCodes());
            data.put(dims, content);
        }
    }
    
    /*
     * Values must be specifed in same order as variables
     */
    public DataContent getDataContent(List<String> values) throws MetamacException {
        if (values.size() != (getStub().size() + getHeading().size())) {
            throw new MetamacException();
        }
        Set<String> dimCodes = new HashSet<String>();
        for (String value: values) {
            dimCodes.add(value);
        }
        
        return data.get(dimCodes);
    }
}
