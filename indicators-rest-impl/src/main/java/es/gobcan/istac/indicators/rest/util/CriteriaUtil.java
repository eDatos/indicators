package es.gobcan.istac.indicators.rest.util;

import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;

import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;

public final class CriteriaUtil {

    private CriteriaUtil() {
    }

    private static final Integer MAXIMUM_RESULT_SIZE_DEFAULT = Integer.valueOf(25);
    private static final Integer MAXIMUM_RESULT_SIZE_ALLOWED = Integer.valueOf(1000);

    public static PagingParameter createPagingParameter(final RestCriteriaPaginator paginator) {
        if (paginator.getOffset() == null || paginator.getOffset() < 0) {
            paginator.setOffset(0);
        }

        if (paginator.getLimit() == null || paginator.getLimit() < 0) {
            paginator.setLimit(MAXIMUM_RESULT_SIZE_DEFAULT);
        }

        if (paginator.getLimit() > MAXIMUM_RESULT_SIZE_ALLOWED) {
            paginator.setLimit(MAXIMUM_RESULT_SIZE_ALLOWED);
        }
        return PagingParameter.rowAccess(paginator.getOffset(), paginator.getOffset() + paginator.getLimit(), Boolean.TRUE);
    }

}
