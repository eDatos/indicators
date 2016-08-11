package es.gobcan.istac.indicators.web.client.model.ds;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class DataDefinitionDS extends DataSource {

    public static String UUID   = "dd-uuid";
    public static String NAME   = "dd-name";
    public static String PX_URI = "dd-px-uri";

    public static String DTO    = "dd-dto";

    public DataDefinitionDS() {
        DataSourceTextField uuid = new DataSourceTextField(UUID, getConstants().indicDetailTitle());
        uuid.setPrimaryKey(true);
        addField(uuid);
    }
}
