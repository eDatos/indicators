package es.gobcan.istac.indicators.web.client.model.ds;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class DataSourceDS extends DataSource {

    public static String UUID                              = "ds-uuid";
    public static String QUERY_UUID                        = "ds-query-uuid";
    public static String QUERY_TEXT                        = "ds-query-text";            // Not mapped in DTO
    public static String PX                                = "ds-px";
    public static String QUERY_METAMAC                     = "ds-query-metamac";
    public static String QUERY_ENVIRONMENT                 = "ds-query-environment";

    public static String ABSOLUTE_METHOD                   = "ds-abs-met";
    public static String ABSOLUTE_METHOD_VIEW              = "ds-abs-met-view";

    public static String TIME_VARIABLE                     = "ds-time-var";
    public static String TIME_VALUE                        = "ds-time-value";
    public static String TIME_VALUE_METAMAC                = "ds-time-value-metamac";
    public static String TIME_VALUE_JSON_STAT              = "ds-time-value-json-stat";
    public static String GEO_VARIABLE                      = "ds-geo-var";
    public static String GEO_VALUE                         = "ds-geo-val";
    public static String GEO_VALUE_UUID_METAMAC            = "ds-geo-va-uuid-metamac";
    public static String GEO_VALUE_TEXT_METAMAC            = "ds-geo-val-text-metamac";
    public static String GEO_VALUE_TEXT_JSON_STAT          = "ds-geo-val-text-json-stat";
    public static String MEASURE_VARIABLE                  = "ds-meas-var";
    public static String SOURCE_SURVEY_CODE                = "ds-sur-code";
    public static String SOURCE_SURVEY_TITLE               = "ds-sur-title";
    public static String SOURCE_SURVEY_ACRONYM             = "ds-sur-acro";
    public static String SOURCE_SURVEY_URL                 = "ds-sur-url";
    public static String PUBLISHERS                        = "ds-pub";

    public static String RATE_DERIVATION_METHOD_VIEW       = "rate-method";
    public static String RATE_DERIVATION_METHOD_CALCULATED = "rate-method-cal";          // Not mapped in DTO
    public static String RATE_DERIVATION_METHOD_LOAD       = "rate-method-load";         // Not mapped in DTO
    public static String RATE_DERIVATION_METHOD_LOAD_VIEW  = "rate-method-load-view";    // Not mapped in DTO

    public static String RATE_DERIVATION_METHOD_TYPE_VIEW  = "rate-method-type-view";    // Not mapped in DTO
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
