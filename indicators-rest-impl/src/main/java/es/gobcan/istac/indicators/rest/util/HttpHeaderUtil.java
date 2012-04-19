package es.gobcan.istac.indicators.rest.util;

import static es.gobcan.istac.indicators.rest.util.RESTURIUtil.REL_FIRST;
import static es.gobcan.istac.indicators.rest.util.RESTURIUtil.REL_LAST;
import static es.gobcan.istac.indicators.rest.util.RESTURIUtil.REL_NEXT;
import static es.gobcan.istac.indicators.rest.util.RESTURIUtil.REL_PREV;
import static es.gobcan.istac.indicators.rest.util.RESTURIUtil.createLinkHeader;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

import es.gobcan.istac.indicators.rest.types.PagedResultType;

public class HttpHeaderUtil {

    //
    public static <T extends Serializable> HttpHeaders createPagedHeaders(final String httpUrl, final PagedResultType<T> pagedResultType) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(httpUrl);

        final StringBuilder linkHeader = new StringBuilder();
        if (hasNext(pagedResultType)) {
            final String uriForNextPage = constructNextUri(uriBuilder, pagedResultType);
            linkHeader.append(createLinkHeader(uriForNextPage, REL_NEXT));
        }
        if (hasPrevious(pagedResultType)) {
            final String uriForPrevPage = constructPrevPageUri(uriBuilder, pagedResultType);
            appendCommaIfNecessary(linkHeader);
            linkHeader.append(createLinkHeader(uriForPrevPage, REL_PREV));
        }
        if (hasFirst(pagedResultType)) {
            final String uriForFirstPage = constructFirstUri(uriBuilder, pagedResultType);
            appendCommaIfNecessary(linkHeader);
            linkHeader.append(createLinkHeader(uriForFirstPage, REL_FIRST));
        }
        if (hasLast(pagedResultType)) {
            final String uriForLastPage = constructLastUri(uriBuilder, pagedResultType);
            appendCommaIfNecessary(linkHeader);
            linkHeader.append(createLinkHeader(uriForLastPage, REL_LAST));
        }

        String link = linkHeader.toString();
        HttpHeaders headers = new HttpHeaders();
        if (StringUtils.isNotBlank(link)) {
            headers.add(RESTURIUtil.LINK, link);
        }
        return headers;
    }

    private static <T extends Serializable> String constructNextUri(final UriComponentsBuilder uriBuilder, final PagedResultType<T> pagedResultType) {
        return uriBuilder.replaceQueryParam("offset", pagedResultType.getOffset() + pagedResultType.getLimit()).replaceQueryParam("limit", pagedResultType.getLimit()).build().encode().toUriString();
    }

    private static <T extends Serializable> String constructPrevPageUri(final UriComponentsBuilder uriBuilder, final PagedResultType<T> pagedResultType) {
        return uriBuilder.replaceQueryParam("offset", pagedResultType.getOffset() + pagedResultType.getLimit()).replaceQueryParam("limit", pagedResultType.getLimit()).build().encode().toUriString();
    }

    private static <T extends Serializable> String constructFirstUri(final UriComponentsBuilder uriBuilder, final PagedResultType<T> pagedResultType) {
        return uriBuilder.replaceQueryParam("offset", 0).replaceQueryParam("limit", pagedResultType.getLimit()).build().encode().toUriString();
    }

    private static <T extends Serializable> String constructLastUri(final UriComponentsBuilder uriBuilder, final PagedResultType<T> pagedResultType) {
        return uriBuilder.replaceQueryParam("offset", pagedResultType.getTotal() - pagedResultType.getLimit()).replaceQueryParam("limit", pagedResultType.getLimit()).build().encode().toUriString();
    }

    private static <T extends Serializable> boolean hasNext(final PagedResultType<T> pagedResultType) {
        return pagedResultType.getOffset() + pagedResultType.getLimit() < pagedResultType.getTotal();
    }

    private static <T extends Serializable> boolean hasPrevious(final PagedResultType<T> pagedResultType) {
        return pagedResultType.getOffset() >= pagedResultType.getLimit();
    }

    private static <T extends Serializable> boolean hasFirst(final PagedResultType<T> pagedResultType) {
        return pagedResultType.getOffset() > 0;
    }

    private static <T extends Serializable> boolean hasLast(final PagedResultType<T> pagedResultType) {
        return hasNext(pagedResultType);
    }

    private static void appendCommaIfNecessary(final StringBuilder linkHeader) {
        if (linkHeader.length() > 0) {
            linkHeader.append(", ");
        }
    }

}
