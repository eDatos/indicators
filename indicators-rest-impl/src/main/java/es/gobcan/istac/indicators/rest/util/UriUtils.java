package es.gobcan.istac.indicators.rest.util;

import org.springframework.web.util.UriComponentsBuilder;

import es.gobcan.istac.indicators.rest.RestConstants;

public class UriUtils {

    private UriUtils() {
    }

    public static String getSubjectSelfLink(UriComponentsBuilder uriComponentsBuilder) {
        return uriComponentsBuilder.pathSegment(RestConstants.API_INDICATORS_VERSION, RestConstants.API_INDICATORS_SUBJECTS).build().toString();

    }
}
