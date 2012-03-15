package es.gobcan.istac.indicators.core.repositoryimpl.finders;

/**
 * This class can not be used out of service or repository. Do not use in web!
 */
public enum GeographicalValueCriteriaPropertyInternalEnum {
    
    GEOGRAPHICAL_GRANULARITY_UUID;
    
    public String value() {
        return name();
    }

    public static GeographicalValueCriteriaPropertyInternalEnum fromValue(String v) {
        return valueOf(v);
    }
}