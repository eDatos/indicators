package es.gobcan.istac.indicators.rest.util;

import org.siemac.metamac.rest.utils.RestUtils;

import es.gobcan.istac.indicators.rest.IndicatorsRestConstants;
import es.gobcan.istac.indicators.rest.types.IndicatorInstanceBaseType;
import es.gobcan.istac.indicators.rest.types.LinkType;
import es.gobcan.istac.indicators.rest.types.PagedResultType;

public class IndicatorInstancesPaginatedResponseUtil extends PaginatedResponseUtil {

    public static void createPaginationLinks(PagedResultType<IndicatorInstanceBaseType> target, String baseURL, String parentUrl, String query, String order, Integer queryLimit, Integer queryOffset,
            String fields, String representation, String granularity) {

        Integer total = target.getTotal();
        Integer offset = CriteriaUtil.calculateOffset(queryOffset);
        Integer limit = CriteriaUtil.calculateLimit(queryLimit);

        target.setSelfLink(toPaginatedLink(baseURL, query, order, limit, offset, fields, representation, granularity));
        target.setParentLink(new LinkType(IndicatorsRestConstants.KIND_INDICATOR_SYSTEM, parentUrl));

        // first page: only if it is not first page
        if (offset >= limit) {
            int firstOffset = 0;
            target.setFirstLink(toPaginatedLink(baseURL, query, order, limit, firstOffset, fields, representation, granularity));
        }
        // last page: only if it is not last page
        if (offset + limit < total) {
            int lastOffset = getOffsetLastPage(limit, offset, total);
            target.setLastLink(toPaginatedLink(baseURL, query, order, limit, lastOffset, fields, representation, granularity));
        }
        // previous and next page
        if (offset > 0) {
            int previousOffset = getOffsetPreviousPage(limit, offset);
            if (NO_OFFSET != previousOffset) {
                target.setPreviousLink(toPaginatedLink(baseURL, query, order, limit, previousOffset, fields, representation, granularity));
            }
            int nextOffset = getOffsetNextPage(limit, offset, total);
            if (NO_OFFSET != nextOffset) {
                target.setNextLink(toPaginatedLink(baseURL, query, order, limit, nextOffset, fields, representation, granularity));
            }
        }
    }
    private static String toPaginatedLink(String baseURL, String query, String order, Integer limit, Integer offset, String fields, String representation, String granularity) {
        if (offset < 0) {
            return null;
        }
        String link = baseURL;
        link = RestUtils.createLinkWithQueryParam(link, IndicatorsRestConstants.PARAMETER_Q, query);
        link = RestUtils.createLinkWithQueryParam(link, IndicatorsRestConstants.PARAMETER_ORDER, order);
        link = RestUtils.createLinkWithQueryParam(link, IndicatorsRestConstants.PARAMETER_FIELDS, fields);
        link = RestUtils.createLinkWithQueryParam(link, IndicatorsRestConstants.PARAMETER_REPRESENTATION, representation);
        link = RestUtils.createLinkWithQueryParam(link, IndicatorsRestConstants.PARAMETER_GRANULARITY, granularity);
        link = RestUtils.createLinkWithQueryParam(link, IndicatorsRestConstants.PARAMETER_LIMIT, String.valueOf(limit));
        link = RestUtils.createLinkWithQueryParam(link, IndicatorsRestConstants.PARAMETER_OFFSET, String.valueOf(offset));
        return link;
    }
}
