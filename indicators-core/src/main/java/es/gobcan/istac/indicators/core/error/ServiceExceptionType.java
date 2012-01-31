package es.gobcan.istac.indicators.core.error;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.siemac.metamac.core.common.lang.LocaleUtil;

public enum ServiceExceptionType {

    SERVICE_INVALID_PARAMETER_COLLECTION_EMPTY("0201"),
    SERVICE_INVALID_PARAMETER_UNEXPECTED("0202"),
    SERVICE_INVALID_PARAMETER_NULL("0203"),
    SERVICE_INVALID_NOT_FOUND("0204"),

    SERVICE_VALIDATION_CONSTRAINT_ENUMERATED("0401"),
    SERVICE_VALIDATION_CONSTRAINT_CARDINALITY_MAX("0402"),
    SERVICE_VALIDATION_COLLECTION_EMPTY("0403"),
    SERVICE_VALIDATION_METADATA_REQUIRED("0404"),
    SERVICE_VALIDATION_METADATA_MUST_BE_EMPTY("0405"),

    SERVICE_INDICATORY_SYSTEM_NOT_FOUND("0501"),
    SERVICE_INDICATORY_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED("0502");

    private String errorCode;

    private static final Map<ServiceExceptionType, String> MESSAGE_MAP = new HashMap<ServiceExceptionType, String>();
    private static final Map<String, ServiceExceptionType> LOOKUP      = new HashMap<String, ServiceExceptionType>();

    static {
        // Invalid
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_INVALID_PARAMETER_COLLECTION_EMPTY, "exception.service.invalid.parameter.collection_empty");
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_INVALID_PARAMETER_UNEXPECTED, "exception.service.invalid.parameter.unexpected");
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_INVALID_PARAMETER_NULL, "exception.service.invalid.parameter.null");
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_INVALID_NOT_FOUND, "exception.service.invalid.parameter.not_found");

        // Constraints
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_VALIDATION_CONSTRAINT_ENUMERATED, "exception.service.validation.constraint.enumerated");
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_VALIDATION_CONSTRAINT_CARDINALITY_MAX, "exception.service.validation.constraint.cardinality_max");
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_VALIDATION_COLLECTION_EMPTY, "exception.service.validation.collection_empty");
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_VALIDATION_METADATA_REQUIRED, "exception.service.validation.metadata.required");
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_VALIDATION_METADATA_MUST_BE_EMPTY, "exception.service.validation.metadata.must_be_empty");

        // Indicators systems
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_NOT_FOUND, "exception.service.validation.indicator_system.not_found");
        MESSAGE_MAP.put(ServiceExceptionType.SERVICE_INDICATORY_SYSTEM_ALREADY_EXIST_CODE_DUPLICATED, "exception.service.validation.indicator_system.already_exists.code_duplicated");

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
