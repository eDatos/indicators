package es.gobcan.istac.indicators.web.client.model.ds;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;


public class IndicatorInstanceDS extends DataSource {
    public static String FIELD_UUID = "uuid-indinst";
    public static String FIELD_INTERNATIONAL_TITLE = "titleint-indinst";
    public static String FIELD_INDICATOR_UUID = "ind_uuid-indinst";
    
    public IndicatorInstanceDS() {
        DataSourceIntegerField uuid = new DataSourceIntegerField(FIELD_UUID, getConstants().systemStrucIndInstanceUuid());
        uuid.setPrimaryKey(true);
        addField(uuid);
        
        DataSourceTextField title = new DataSourceTextField(FIELD_INTERNATIONAL_TITLE, getConstants().systemStrucIndInstanceTitleField());
        addField(title);
    }

}
