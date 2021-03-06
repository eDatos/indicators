package es.gobcan.istac.indicators.core.constants;

public class IndicatorsConstants {

    private IndicatorsConstants() {
    }

    public static final String LOCALE_SPANISH                            = "es";
    public static final String LOCALE_ENGLISH                            = "en";
    public static final String SECURITY_APPLICATION_ID                   = "GESTOR_INDICADORES";

    // Translations
    public static final String TRANSLATION_TIME_GRANULARITY              = "TIME_GRANULARITY";
    public static final String TRANSLATION_TIME_VALUE_YEARLY             = "TIME_VALUE.YEARLY";
    public static final String TRANSLATION_TIME_VALUE_BIYEARLY           = "TIME_VALUE.BIYEARLY";
    public static final String TRANSLATION_TIME_VALUE_QUARTERLY          = "TIME_VALUE.QUARTERLY";
    public static final String TRANSLATION_TIME_VALUE_FOUR_MONTHLY       = "TIME_VALUE.FOUR_MONTHLY";
    public static final String TRANSLATION_TIME_VALUE_MONTHLY            = "TIME_VALUE.MONTHLY";
    public static final String TRANSLATION_TIME_VALUE_WEEKLY             = "TIME_VALUE.WEEKLY";
    public static final String TRANSLATION_TIME_VALUE_DAILY              = "TIME_VALUE.DAILY";
    public static final String TRANSLATION_TIME_VALUE_HOURLY             = "TIME_VALUE.HOURLY";
    public static final String TRANSLATION_YEAR_IN_LABEL                 = "{yyyy}";
    public static final String TRANSLATION_MONTH_IN_LABEL                = "{MM}";
    public static final String TRANSLATION_WEEK_IN_LABEL                 = "{ww}";
    public static final String TRANSLATION_DAY_IN_LABEL                  = "{dd}";
    public static final String TRANSLATION_HOUR_IN_LABEL                 = "{HH}";
    public static final String TRANSLATION_MINUTES_IN_LABEL              = "{mm}";
    public static final String TRANSLATION_SECONDS_IN_LABEL              = "{ss}";
    // Measure translations
    public static final String TRANSLATION_MEASURE_DIMENSION             = "MEASURE_DIMENSION";
    // Other translations
    public static final String TRANSLATION_UNIT_MULTIPLIER               = "UNIT_MULTIPLIER";
    public static final String TRANSLATION_METADATA_ATTRIBUTE            = "METADATA_ATTRIBUTE";

    // Importation and exportation
    public static final String TSV_SEPARATOR                             = "\t";
    public static final String TSV_LINE_SEPARATOR                        = "\n";
    public static final String TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR = "#";
    public static final String TSV_HEADER_ENVIRONMENT_SEPARATOR          = "-";
    public static final String TSV_HEADER_PRODUCTION                     = "production";
    public static final String TSV_HEADER_DIFFUSION                      = "diffusion";

    public static final String TSV_HEADER_CODE                           = "code";
    public static final String TSV_HEADER_NOTIFY_POPULATION_ERRORS       = "notify_population_errors";
    public static final String TSV_HEADER_TITLE                          = "title";
    public static final String TSV_HEADER_SUBJECT_TITLE                  = "subject_title";
    public static final String TSV_HEADER_VERSION_NUMBER                 = "version_number";
    public static final String TSV_HEADER_SUBJECT_CODE                   = "subject_code";
    public static final String TSV_HEADER_PROC_STATUS                    = "proc_status";
    public static final String TSV_HEADER_NEEDS_UPDATE                   = "needs_update";
    public static final String TSV_HEADER_NEEDS_BE_POPULATED             = "needs_be_populated";
    public static final String TSV_HEADER_PRODUCTION_VALIDATION_DATE     = "production_validation_date";
    public static final String TSV_HEADER_PRODUCTION_VALIDATION_USER     = "production_validation_user";
    public static final String TSV_HEADER_DIFFUSION_VALIDATION_DATE      = "diffusion_validation_date";
    public static final String TSV_HEADER_DIFFUSION_VALIDATION_USER      = "diffusion_validation_user";
    public static final String TSV_HEADER_PUBLICATION_DATE               = "publication_date";
    public static final String TSV_HEADER_PUBLICATION_USER               = "publication_user";
    public static final String TSV_HEADER_PUBLICATION_FAILED_DATE        = "publication_failed_date";
    public static final String TSV_HEADER_PUBLICATION_FAILED_USER        = "publication_failed_user";
    public static final String TSV_HEADER_ARCHIVE_DATE                   = "archive_date";
    public static final String TSV_HEADER_ARCHIVE_USER                   = "archive_user";
    public static final String TSV_HEADER_CREATED_DATE                   = "created_date";
    public static final String TSV_HEADER_CREATED_BY                     = "created_by";

    public static final String TSV_EXPORTATION_ENCODING                  = "UTF-8";

    // For building urls at core level
    // Check also both urlrewrite.xml and other uses as ApiDocController
    public static final String API_VERSION_1_0                           = "v1.0";
    public static final String API_INDICATORS_INDICATORS                 = "indicators";

    public static final String DOT_1_NOT_APPLICABLE                      = ".";
    public static final String DOT_2_UNAVAILABLE                         = "..";
    public static final String DOT_3_HIDDEN_BY_IMPRECISION               = "...";
    public static final String DOT_4_HIDDEN_BY_SECRET                    = "....";
    public static final String DOT_5_INCLUDED_ELSEWHERE                  = ".....";
    public static final String DOT_6_UNAVAILABLE_BY_HOLIDAYS             = "......";

}