package es.gobcan.istac.indicators.rest.util;

import org.siemac.metamac.rest.utils.RestUtils;

import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.types.IndicatorBaseType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;

public class IndicatorsPaginatedResponseUtil extends PaginatedResponseUtil {

    public static void createPaginationLinks(PagedResultType<IndicatorBaseType> target, String baseURL, String query, String order, Integer limit, Integer offset, String fields, String representation) {

        Integer total = target.getTotal();

        target.setSelfLink(toPaginatedLink(baseURL, query, order, limit, offset, fields, representation));

        // first page: only if it is not first page
        if (offset >= limit) {
            int firstOffset = 0;
            target.setFirstLink(toPaginatedLink(baseURL, query, order, limit, firstOffset, fields, representation));
        }
        // last page: only if it is not last page
        if (offset + limit < total) {
            int lastOffset = getOffsetLastPage(limit, offset, total);
            target.setLastLink(toPaginatedLink(baseURL, query, order, limit, lastOffset, fields, representation));
        }
        // previous and next page
        if (offset > 0) {
            int previousOffset = getOffsetPreviousPage(limit, offset);
            if (NO_OFFSET != previousOffset) {
                target.setPreviousLink(toPaginatedLink(baseURL, query, order, limit, previousOffset, fields, representation));
            }
            int nextOffset = getOffsetNextPage(limit, offset, total);
            if (NO_OFFSET != nextOffset) {
                target.setNextLink(toPaginatedLink(baseURL, query, order, limit, nextOffset, fields, representation));
            }
        }
    }

    private static String toPaginatedLink(String baseURL, String query, String order, Integer limit, Integer offset, String fields, String representation) {
        if (offset < 0) {
            return null;
        }
        String link = baseURL;
        link = RestUtils.createLinkWithQueryParam(link, RestConstants.PARAMETER_Q, query);
        link = RestUtils.createLinkWithQueryParam(link, RestConstants.PARAMETER_ORDER, order);
        link = RestUtils.createLinkWithQueryParam(link, RestConstants.PARAMETER_OFFSET, fields);
        link = RestUtils.createLinkWithQueryParam(link, RestConstants.PARAMETER_OFFSET, representation);
        link = RestUtils.createLinkWithQueryParam(link, RestConstants.PARAMETER_LIMIT, String.valueOf(limit));
        link = RestUtils.createLinkWithQueryParam(link, RestConstants.PARAMETER_OFFSET, String.valueOf(offset));
        return link;
    }
}
