package es.gobcan.istac.indicators.core.dto.serviceapi;



/**
 * Dto for data source
 */
public class DataSourceDto extends DataSourceDtoBase {
    private static final long serialVersionUID = 1L;
    
    public static final String OBS_VALUE = "##OBS_VALUE##";

    public DataSourceDto() {
    }
    
    public DataSourceVariableDto getOtherVariable(String variable) {
        if (variable == null) {
            return null;
        }
        for (DataSourceVariableDto dataSourceVariableDto: getOtherVariables()) {
            if (dataSourceVariableDto.getVariable().equals(variable)) {
                return dataSourceVariableDto;
            }
        }
        return null;
    }
    
    public boolean isAbsoluteMethodObsValue() {
        return OBS_VALUE.equals(getAbsoluteMethod());
    }
    
    public void setAbsoluteMethodObsValue() {
        setAbsoluteMethod(OBS_VALUE);
    }
}
