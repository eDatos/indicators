package es.gobcan.istac.indicators.web.client.model.ds;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class IndicatorInstanceDS extends DataSource {

    public static String UUID                        = "inst-uuid";
    public static String TITLE                       = "inst-titleint";
    public static String IND_UUID                    = "inst-ind-uuid";
    public static String IND_TEXT                    = "inst-ind-title";
    public static String TIME_SELECTION_TYPE         = "inst-time-sel";
    public static String TIME_VALUE                  = "inst-temp";
    public static String TIME_GRANULARITY            = "inst-temp-gran";
    public static String GEOGRAPHICAL_SELECTION_TYPE = "inst-geo-sel";
    public static String GEOGRAPHICAL_VALUES         = "inst-geo";
    public static String GEOGRAPHICAL_GRANULARITY    = "inst-geo-gran";

    public IndicatorInstanceDS() {
        DataSourceIntegerField uuid = new DataSourceIntegerField(UUID, getConstants().systemStrucIndInstanceUuid());
        uuid.setPrimaryKey(true);
        addField(uuid);

        DataSourceTextField title = new DataSourceTextField(TITLE, getConstants().systemStrucIndInstanceTitleField());
        addField(title);
    }

}
