package es.gobcan.istac.indicators.rest.util;

import org.siemac.metamac.rest.utils.RestUtils;
import org.springframework.web.util.UriComponentsBuilder;

import es.gobcan.istac.indicators.rest.RestConstants;

public final class UriUtils {

    private UriUtils() {
    }

    // /vX/subjects
    public static String getSubjectsLink(UriComponentsBuilder uriComponentsBuilder) {
        return uriComponentsBuilder.pathSegment(RestConstants.API_INDICATORS_VERSION, RestConstants.API_INDICATORS_SUBJECTS).build().toString();

    }

    // /vX/geographicalValues
    public static String getGeographicalValuesLink(UriComponentsBuilder uriComponentsBuilder) {
        return uriComponentsBuilder.pathSegment(RestConstants.API_INDICATORS_VERSION, RestConstants.API_INDICATORS_GEOGRAPHICAL_VALUES).build().toString();

    }

    // /vX/geographicalValues + parameters
    public static String getGeographicalValuesSelfLink(UriComponentsBuilder uriComponentsBuilder, String subjectCode, String systemCode, String geographicalGranularityCode) {
        String link = getGeographicalValuesLink(uriComponentsBuilder);
        link = RestUtils.createLinkWithQueryParam(link, RestConstants.PARAMETER_SUBJECT_CODE, subjectCode);
        link = RestUtils.createLinkWithQueryParam(link, RestConstants.PARAMETER_SYSTEM_CODE, systemCode);
        link = RestUtils.createLinkWithQueryParam(link, RestConstants.GEOGRAPHICAL_GRANULARITY_CODE, geographicalGranularityCode);
        return link;
    }

    // /vX/geographicGranularities
    public static String getGeographicalGranularitiesLink(UriComponentsBuilder uriComponentsBuilder) {
        return uriComponentsBuilder.pathSegment(RestConstants.API_INDICATORS_VERSION, RestConstants.API_INDICATORS_GEOGRAPHIC_GRANULARITIES).build().toString();

    }

    // /vX/geographicGranularities + parameters
    public static String getGeographicalGranularitiesSelfLink(UriComponentsBuilder uriComponentsBuilder, String subjectCode, String systemCode) {
        String link = getGeographicalGranularitiesLink(uriComponentsBuilder);
        link = RestUtils.createLinkWithQueryParam(link, RestConstants.PARAMETER_SUBJECT_CODE, subjectCode);
        link = RestUtils.createLinkWithQueryParam(link, RestConstants.PARAMETER_SYSTEM_CODE, systemCode);
        return link;
    }

    // /vX/timeGranularities
    public static String getTimeGranularitiesLink(UriComponentsBuilder uriComponentsBuilder) {
        return uriComponentsBuilder.pathSegment(RestConstants.API_INDICATORS_VERSION, RestConstants.API_INDICATORS_TIME_GRANULARITIES).build().toString();
    }
}
