package es.gobcan.istac.indicators.web.client.model.ds;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class GeoValueDS extends DataSource {

    public static String UUID              = "geo-value-uuid";
    public static String CODE              = "geo-value-code";
    public static String TITLE             = "geo-value-title";
    public static String GRANULARITY       = "geo-value-granularity";
    public static String GRANULARITY_TITLE = "geo-value-granularity-title";
    public static String LATITUDE          = "geo-value-latitude";
    public static String LONGITUDE         = "geo-value-longitude";
    public static String ORDER             = "geo-value-order";

    public static String DTO               = "geo-value-dto";

    public GeoValueDS() {
        DataSourceTextField uuid = new DataSourceTextField(UUID, getConstants().geoValueUuid());
        uuid.setPrimaryKey(true);
        addField(uuid);
    }
}
