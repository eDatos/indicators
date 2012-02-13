package es.gobcan.istac.indicators.web.client.model.ds;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;


public class IndicatorDS extends DataSource {
    public static String FIELD_IDENTIFIER = "id-ind";
    public static String FIELD_VERSION = "version-ind";
    public static String FIELD_INTERNATIONAL_NAME= "nameint-ind";
    
    public IndicatorDS() {
        
        DataSourceIntegerField id = new DataSourceIntegerField(FIELD_IDENTIFIER, getConstants().indicDetailIdentifier());
        addField(id);
        id.setPrimaryKey(true);
        
        DataSourceIntegerField version = new DataSourceIntegerField(FIELD_VERSION, getConstants().indicDetailVersion());
        addField(version);
        
        DataSourceTextField name = new DataSourceTextField(FIELD_INTERNATIONAL_NAME, getConstants().indicDetailName());
        addField(name);
        
        setClientOnly(true);
    }
    
}
