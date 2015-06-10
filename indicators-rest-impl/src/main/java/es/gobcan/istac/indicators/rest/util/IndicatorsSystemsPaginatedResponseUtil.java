package es.gobcan.istac.indicators.rest.util;

import org.siemac.metamac.rest.utils.RestUtils;

import es.gobcan.istac.indicators.rest.IndicatorsRestConstants;
import es.gobcan.istac.indicators.rest.types.IndicatorsSystemBaseType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;

public class IndicatorsSystemsPaginatedResponseUtil extends PaginatedResponseUtil {

    public static void createPaginationLinks(PagedResultType<IndicatorsSystemBaseType> target, String baseURL, Integer queryLimit, Integer queryOffset) {

        Integer total = target.getTotal();
        Integer offset = CriteriaUtil.calculateOffset(queryOffset);
        Integer limit = CriteriaUtil.calculateLimit(queryLimit);

        target.setSelfLink(toPaginatedLink(baseURL, limit, offset));

        // first page: only if it is not first page
        if (offset >= limit) {
            int firstOffset = 0;
            target.setFirstLink(toPaginatedLink(baseURL, limit, firstOffset));
        }
        // last page: only if it is not last page
        if (offset + limit < total) {
            int lastOffset = getOffsetLastPage(limit, offset, total);
            target.setLastLink(toPaginatedLink(baseURL, limit, lastOffset));
        }
        // previous and next page
        if (offset > 0) {
            int previousOffset = getOffsetPreviousPage(limit, offset);
            if (NO_OFFSET != previousOffset) {
                target.setPreviousLink(toPaginatedLink(baseURL, limit, previousOffset));
            }
            int nextOffset = getOffsetNextPage(limit, offset, total);
            if (NO_OFFSET != nextOffset) {
                target.setNextLink(toPaginatedLink(baseURL, limit, nextOffset));
            }
        }
    }

    private static String toPaginatedLink(String baseURL, Integer limit, Integer offset) {
        if (offset < 0) {
            return null;
        }
        String link = baseURL;
        link = RestUtils.createLinkWithQueryParam(link, IndicatorsRestConstants.PARAMETER_LIMIT, String.valueOf(limit));
        link = RestUtils.createLinkWithQueryParam(link, IndicatorsRestConstants.PARAMETER_OFFSET, String.valueOf(offset));
        return link;
    }
}
