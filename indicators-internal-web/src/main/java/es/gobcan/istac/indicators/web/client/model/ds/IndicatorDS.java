package es.gobcan.istac.indicators.web.client.model.ds;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class IndicatorDS extends DataSource {

    // IDENTIFIERS
    public static String UUID                                  = "ind-uuid";
    public static String VERSION_NUMBER                        = "ind-version";
    public static String CODE                                  = "ind-code";
    public static String PROC_STATUS                           = "ind-status";
    public static String TITLE                                 = "ind-title";
    public static String ACRONYM                               = "ind-acron";
    // CONTENT CLASSIFIERS
    public static String SUBJECT                               = "ind-sub";
    public static String SUBJECT_CODE                          = "ind-sub-code";
    public static String SUBJECT_TITLE                         = "ind-sub-title";
    // CONTENT DESCRIPTORS
    public static String CONCEPT_DESCRIPTION                   = "ind-concept";
    // PRODUCTION DESCRIPTORS
    public static String PRODUCTION_VERSION                    = "ind-prod-v-";
    public static String PRODUCTION_VALIDATION_DATE            = "ind-prod-date";
    public static String PRODUCTION_VALIDATION_USER            = "ind-prod-user";
    // QUANTITY
    public static String QUANTITY_UNIT_UUID                    = "q-unit-uuid";
    public static String QUANTITY_UNIT_MULTIPLIER              = "q-unit-mul";
    public static String QUANTITY_SIGNIFICANT_DIGITS           = "q-sig-dig";
    public static String QUANTITY_DECIMAL_PLACES               = "q-dec";
    public static String QUANTITY_MINIMUM                      = "q-min";
    public static String QUANTITY_MAXIMUM                      = "q-max";
    public static String QUANTITY_DENOMINATOR_INDICATOR_UUID   = "q-den";
    public static String QUANTITY_DENOMINATOR_INDICATOR_TEXT   = "q-den-dtext";      // Not mapped in DTO
    public static String QUANTITY_NUMERATOR_INDICATOR_UUID     = "q-num";
    public static String QUANTITY_NUMERATOR_INDICATOR_TEXT     = "q-num-dtext";      // Not mapped in DTO
    public static String QUANTITY_IS_PERCENTAGE                = "q-is-perc";
    public static String QUANTITY_INDEX_BASE_TYPE              = "q-base-type";      // Not mapped in DTO
    public static String QUANTITY_BASE_VALUE                   = "q-value";
    public static String QUANTITY_BASE_TIME                    = "q-time";
    public static String QUANTITY_BASE_LOCATION                = "q-loc";
    public static String QUANTITY_BASE_QUANTITY_INDICATOR_UUID = "q-ind-uuid";
    public static String QUANTITY_BASE_QUANTITY_INDICATOR_TEXT = "q-ind-uuid-dtext"; // Not mapped in DTO
    public static String QUANTITY_TYPE                         = "q-type";
    public static String QUANTITY_TYPE_TEXT                    = "q-type-text";      // Not mapped in DTO
    public static String QUANTITY_PERCENTAGE_OF                = "q-perc-of";
    // DIFUSSION DESCRIPTORS
    public static String DIFFUSION_VALIDATION_DATE             = "ind-diff-date";
    public static String DIFFUSION_VALIDATION_USER             = "ind-diff-user";
    // PUBLICATION DESCRIPTORS
    public static String PUBLICATION_VERSION                   = "ind-pun-ver";
    public static String PUBLICATION_FAILED_DATE               = "ind-pub-fail-date";
    public static String PUBLICATION_FAILED_USER               = "ind-pub-fail-user";
    public static String PUBLICATION_DATE                      = "ind-pub-date";
    public static String PUBLICATION_USER                      = "ind-pub-user";
    public static String ARCHIVED_VERSION                      = "ind-arch-ver";
    public static String ARCHIVED_DATE                         = "ind-arch-date";
    public static String ARCHIVED_USER                         = "ind-arch-user";
    // ANNOTATIONS
    public static String NOTES                                 = "ind-notes";
    public static String COMMENTS                              = "ind-comments";

    public static String NEEDS_UPDATE                          = "ind-needs-update";

    public static String DTO                                   = "ind-dto";

    public IndicatorDS() {
        DataSourceIntegerField uuid = new DataSourceIntegerField(UUID, getConstants().indicDetailIdentifier());
        uuid.setPrimaryKey(true);
        addField(uuid);

        DataSourceIntegerField code = new DataSourceIntegerField(CODE, getConstants().indicDetailIdentifier());
        addField(code);

        DataSourceTextField name = new DataSourceTextField(TITLE, getConstants().indicDetailTitle());
        addField(name);
    }

}
