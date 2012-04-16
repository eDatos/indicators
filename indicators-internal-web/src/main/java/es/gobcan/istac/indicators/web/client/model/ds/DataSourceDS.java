package es.gobcan.istac.indicators.web.client.model.ds;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class DataSourceDS extends DataSource {

    public static String UUID                              = "ds-uuid";
    public static String QUERY                             = "ds-query";
    public static String PX                                = "ds-px";
    public static String TIME_VARIABLE                     = "ds-time-var";
    public static String TIME_VALUE                        = "ds-time-value";
    public static String GEO_VARIABLE                      = "ds-geo-var";
    public static String GEO_VALUE                         = "ds-geo-val";
    public static String MEASURE_VARIABLE                  = "ds-meas-var";
    public static String SOURCE_SURVEY_CODE                = "ds-sur-code";
    public static String SOURCE_SURVEY_TITLE               = "ds-sur-title";
    public static String SOURCE_SURVEY_ACRONYM             = "ds-sur-acro";
    public static String SOURCE_SURVEY_URL                 = "ds-sur-url";
    public static String PUBLISHERS                        = "ds-pub";

    public static String RATE_DERIVATION_METHOD_VIEW       = "rate-method";
    public static String RATE_DERIVATION_METHOD_CALCULATED = "rate-method-cal";      // Not mapped in DTO
    public static String RATE_DERIVATION_METHOD_LOAD       = "rate-method-load";     // Not mapped in DTO
    public static String RATE_DERIVATION_METHOD_LOAD_VIEW  = "rate-method-load-view"; // Not mapped in DTO

    public static String RATE_DERIVATION_METHOD_TYPE_VIEW  = "rate-method-type-view"; // Not mapped in DTO
    public static String RATE_DERIVATION_METHOD_TYPE       = "rate-method-type";

    public static String RATE_DERIVATION_ROUNDING          = "rate-rounding";

    public static String OTHER_VARIABLES                   = "ds-other-var";

    public static String VARIABLE_NAME                     = "var-name";
    public static String VARIABLE_CATEGORY                 = "var-cat";
    public static String VARIABLE_DTO                      = "var-dto";

    public static String DTO                               = "ds-dto";

    public DataSourceDS() {
        DataSourceTextField uuid = new DataSourceTextField(UUID, getConstants().dataSourceUuid());
        uuid.setPrimaryKey(true);
        addField(uuid);
    }

}
