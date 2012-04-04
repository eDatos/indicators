package es.gobcan.istac.indicators.web.client.model.ds;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;


public class DimensionDS extends DataSource {
    
    public static String UUID = "uuid-dim";
    public static String TITLE = "titleint-dim";
    
    public DimensionDS() {
        DataSourceIntegerField uuid = new DataSourceIntegerField(UUID, getConstants().systemStrucDimUuid());
        uuid.setPrimaryKey(true);
        addField(uuid);
        
        DataSourceTextField title = new DataSourceTextField(TITLE, getConstants().systemStrucDimTitleField());
        addField(title);
    }

}
