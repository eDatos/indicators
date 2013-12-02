package es.gobcan.istac.indicators.web.client.model.ds;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class GeoGranularityDS extends DataSource {

    public static String UUID  = "geo-granularity-uuid";
    public static String CODE  = "geo-granularity-code";
    public static String TITLE = "geo-granularity-title";

    public static String DTO   = "geo-granularity-dto";

    public GeoGranularityDS() {
        DataSourceTextField uuid = new DataSourceTextField(UUID, getConstants().geoGranularityUuid());
        uuid.setPrimaryKey(true);
        addField(uuid);
    }
}
