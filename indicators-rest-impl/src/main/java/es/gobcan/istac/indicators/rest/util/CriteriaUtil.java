package es.gobcan.istac.indicators.rest.util;

import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;

import es.gobcan.istac.indicators.rest.types.RestCriteriaPaginator;

public final class CriteriaUtil {

    private CriteriaUtil() {
    }

    protected static final Integer MAXIMUM_RESULT_SIZE_DEFAULT = Integer.valueOf(25);
    protected static final Integer MAXIMUM_RESULT_SIZE_ALLOWED = Integer.valueOf(1000);

    public static PagingParameter createPagingParameter(final RestCriteriaPaginator paginator) {
        Integer offset = calculateOffset(paginator.getOffset());
        Integer limit = calculateLimit(paginator.getLimit());
        return PagingParameter.rowAccess(offset, offset + limit, Boolean.TRUE);
    }

    public static Integer calculateOffset(Integer offset) {
        if (offset == null || offset < 0) {
            return 0;
        } else {
            return offset;
        }
    }

    public static Integer calculateLimit(Integer limit) {
        if (limit == null || limit < 0) {
            return MAXIMUM_RESULT_SIZE_DEFAULT;
        } else if (limit > MAXIMUM_RESULT_SIZE_ALLOWED) {
            return MAXIMUM_RESULT_SIZE_ALLOWED;
        } else {
            return limit;
        }
    }

}
