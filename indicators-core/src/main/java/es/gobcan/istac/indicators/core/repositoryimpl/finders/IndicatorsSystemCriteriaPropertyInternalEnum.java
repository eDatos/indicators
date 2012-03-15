package es.gobcan.istac.indicators.core.repositoryimpl.finders;

/**
 * This class can not be used out of service or repository. Do not use in web!
 */
public enum IndicatorsSystemCriteriaPropertyInternalEnum {
    
    CODE,
    URI_GOPESTAT,
    PROC_STATUS,
    IS_LAST_VERSION,
    VERSION_NUMBER;
    
    public String value() {
        return name();
    }

    public static IndicatorsSystemCriteriaPropertyInternalEnum fromValue(String v) {
        return valueOf(v);
    }
}