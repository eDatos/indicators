package es.gobcan.istac.indicators.core.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.enume.domain.QueryEnvironmentEnum;

/**
 * Data, contains values
 */
public class Data extends DataStructure {

    private QueryEnvironmentEnum     queryEnvironmentEnum = null;
    private String                   temporalValue        = null;
    private GeographicalValueDto     geographicalValueDto = null;
    private Map<String, DataContent> data;
    private List<String>             variablesInOrder     = new ArrayList<String>();
    
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
    public DataContent getDataContent(Map<String, String> dimensionsCodes) throws MetamacException {
        List<String> dimCodes = new ArrayList<String>();

        // In Metamac the variables order is independent of Heading and Stub
        if (variablesInOrder.isEmpty() && (queryEnvironmentEnum == null || QueryEnvironmentEnum.GPE.equals(getQueryEnvironmentEnum()))) {
            variablesInOrder.addAll(getStub());
            variablesInOrder.addAll(getHeading());
        }

        for (String dim : variablesInOrder) {
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
    
    public void setVariablesInOrder(List<String> variablesInOrder) {
        this.variablesInOrder = variablesInOrder;
    }
    
    public boolean hasContVariable() {
        return !StringUtils.isBlank(getContVariable());
    }
    
    public void setQueryEnvironmentEnum(QueryEnvironmentEnum queryEnvironmentEnum) {
        this.queryEnvironmentEnum = queryEnvironmentEnum;
    }
    
    
    public QueryEnvironmentEnum getQueryEnvironmentEnum() {
        return queryEnvironmentEnum;
    }

    public String getTemporalValue() {
        return temporalValue;
    }

    public void setTemporalValue(String temporalValue) {
        this.temporalValue = temporalValue;
    }

    public GeographicalValueDto getGeographicalValueDto() {
        return geographicalValueDto;
    }

    public void setGeographicalValueDto(GeographicalValueDto geographicalValueDto) {
        this.geographicalValueDto = geographicalValueDto;
    }
}
