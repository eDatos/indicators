package es.gobcan.istac.indicators.core.dto.serviceapi;



/**
 * Dto for data source
 */
public class DataSourceDto extends DataSourceDtoBase {
    private static final long serialVersionUID = 1L;

    public DataSourceDto() {
    }
    
    public DatasourceVariableDto getOtherVariable(String variable) {
        if (variable == null) {
            return null;
        }
        for (DatasourceVariableDto datasourceVariableDto: getOtherVariables()) {
            if (datasourceVariableDto.getVariable().equals(variable)) {
                return datasourceVariableDto;
            }
        }
        return null;
    }
}
