package es.gobcan.istac.indicators.core.error;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.siemac.metamac.core.common.lang.LocaleUtil;

public enum ServiceExceptionType {

    SERVICE_INVALID_PARAMETER_COLLECTION_EMPTY("0201"),
    SERVICE_INVALID_PARAMETER_UNEXPECTED("0202"),
    SERVICE_INVALID_PARAMETER_REQUIRED("0203"),
    SERVICE_INVALID_NOT_FOUND("0204"),

    SERVICE_VALIDATION_CONSTRAINT_ENUMERATED("0401"),
    SERVICE_VALIDATION_CONSTRAINT_CARDINALITY_MAX("0402"),
    SERVICE_VALIDATION_COLLECTION_EMPTY("0403"),
    SERVICE_VALIDATION_METADATA_REQUIRED("0404"),
    SERVICE_VALIDATION_METADATA_MUST_BE_EMPTY("0405"),
    SERVICE_VALIDATION_METADATA_UNMODIFIABLE("0406"),
    SERVICE_VALIDATION_METADATA_INCORRECT("0407"),

    SERVICE_INDICATORS_SYSTEM_NOT_FOUND("0501"),
    SERVICE_INDICATORS_SYSTEM_VERSION_NOT_FOUND("0502"),
    SERVICE_INDICATORS_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED("0503"),
    SERVICE_INDICATORS_SYSTEM_ALREADY_EXIST_URI_GOPESTAT_DUPLICATED("0504"),
    SERVICE_INDICATORS_SYSTEM_IN_PRODUCTION_NOT_FOUND("0505"),
    SERVICE_INDICATORS_SYSTEM_IN_DIFFUSION_NOT_FOUND("0506"),
    SERVICE_INDICATORS_SYSTEM_WRONG_STATE("0507"),
    SERVICE_INDICATORS_SYSTEM_VERSION_WRONG_STATE("0508"),
    
    SERVICE_DIMENSION_NOT_FOUND("0601"),
    SERVICE_DIMENSION_NOT_FOUND_IN_INDICATORS_SYSTEM("0602");


    private String errorCode;

    private static final Map<ServiceExceptionType, String> MESSAGE_MAP = new HashMap<ServiceExceptionType, String>();
    private static final Map<String, ServiceExceptionType> LOOKUP      = new HashMap<String, ServiceExceptionType>();

    static {
        // Invalid
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_INVALID_PARAMETER_COLLECTION_EMPTY, "exception.service.invalid.parameter.collection_empty");
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_INVALID_PARAMETER_UNEXPECTED, "exception.service.invalid.parameter.unexpected");
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_INVALID_PARAMETER_REQUIRED, "exception.service.invalid.parameter.required");
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_INVALID_NOT_FOUND, "exception.service.invalid.parameter.not_found");

        // Constraints
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_VALIDATION_CONSTRAINT_ENUMERATED, "exception.service.validation.constraint.enumerated");
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_VALIDATION_CONSTRAINT_CARDINALITY_MAX, "exception.service.validation.constraint.cardinality_max");
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_VALIDATION_COLLECTION_EMPTY, "exception.service.validation.collection_empty");
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_VALIDATION_METADATA_REQUIRED, "exception.service.validation.metadata.required");
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_VALIDATION_METADATA_MUST_BE_EMPTY, "exception.service.validation.metadata.must_be_empty");
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_VALIDATION_METADATA_UNMODIFIABLE, "exception.service.validation.metadata.unmodifiable");
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_VALIDATION_METADATA_INCORRECT, "exception.service.validation.metadata.incorrect_value");

        // Indicators systems
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_NOT_FOUND, "exception.service.validation.indicators_system.not_found");
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_VERSION_NOT_FOUND, "exception.service.validation.indicators_system.vesion_not_found");
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED, "exception.service.validation.indicators_system.already_exists.code_duplicated");
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_ALREADY_EXIST_URI_GOPESTAT_DUPLICATED, "exception.service.validation.indicators_system.already_exists.uri_gopestat_duplicated");
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_IN_PRODUCTION_NOT_FOUND, "exception.service.validation.indicators_system.production_not_found");
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_IN_DIFFUSION_NOT_FOUND, "exception.service.validation.indicators_system.diffusion_not_found");
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_WRONG_STATE, "exception.service.validation.indicators_system.wrong_state");
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_INDICATORS_SYSTEM_VERSION_WRONG_STATE, "exception.service.validation.indicators_system.version_wrong_state");
        
        // Dimensions
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_DIMENSION_NOT_FOUND, "exception.service.validation.dimension.not_found");
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_DIMENSION_NOT_FOUND_IN_INDICATORS_SYSTEM, "exception.service.validation.dimension.not_found_in_indicators_system");

        for (ServiceExceptionType s : EnumSet.allOf(ServiceExceptionType.class)) {
            LOOKUP.put(s.getErrorCode(), s);
        }
    }

    public String getErrorCode() {
        return errorCode;
    }

    /**
     * @param number
     */
    private ServiceExceptionType(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @param exception
     * @return
     */
    public static ServiceExceptionType get(String exception) {
        return LOOKUP.get(exception);
    }

    /**
     * Returns a localized message for this reason type and locale.
     * 
     * @param locale
     *            The locale.
     * @return A localized message given a reason type and locale.
     */
    public String getMessageForReasonType(Locale locale) {
        return LocaleUtil.getLocalizedMessageFromBundle("i18n/messages-service", MESSAGE_MAP.get(this), locale);
    }

    /**
     * Returns a message for this reason type in the default locale.
     * 
     * @return A message message for this reason type in the default locale.
     */
    public String getMessageForReasonType() {
        return getMessageForReasonType(null);
    }

    /**
     * Returns a lower case string of this enum.
     * 
     * @return a lower case string of this enum
     */
    public String lowerCaseString() {
        return this.toString().toLowerCase();
    }
}
