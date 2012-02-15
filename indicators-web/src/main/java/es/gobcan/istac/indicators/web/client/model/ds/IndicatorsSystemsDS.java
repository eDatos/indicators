package es.gobcan.istac.indicators.web.client.model.ds;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class IndicatorsSystemsDS extends DataSource {
    public static String FIELD_UUID = "uuid-sys";
    public static String FIELD_CODE = "code-sys";
    public static String FIELD_VERSION = "version-sys";
    public static String FIELD_INTERNATIONAL_TITLE = "titleint-sys";
    
    public IndicatorsSystemsDS() {
        DataSourceIntegerField uuid = new DataSourceIntegerField(FIELD_UUID, getConstants().indicDetailIdentifier());
        uuid.setPrimaryKey(true);
        addField(uuid);
        
        DataSourceIntegerField code = new DataSourceIntegerField(FIELD_CODE, getConstants().indicDetailIdentifier());
        addField(code);
        
        DataSourceTextField name = new DataSourceTextField(FIELD_INTERNATIONAL_TITLE, getConstants().indicDetailName());
        addField(name);
    }
}
