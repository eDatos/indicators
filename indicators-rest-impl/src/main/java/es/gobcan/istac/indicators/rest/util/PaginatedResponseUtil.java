package es.gobcan.istac.indicators.rest.util;

public abstract class PaginatedResponseUtil {

    protected final static int NO_OFFSET = -1;

    protected PaginatedResponseUtil() {
    }

    protected static int getOffsetLastPage(int limit, int offset, int total) {
        int lastPage = (int) Math.ceil(((double) total) / limit);
        int shift = offset % limit;
        int offsetLastPage = (lastPage - 1) * limit + shift;
        while (offsetLastPage > total && offsetLastPage >= 0) {
            offsetLastPage = --lastPage * limit + shift;
        }
        if (offsetLastPage < 0) {
            return NO_OFFSET;
        }
        return offsetLastPage;
    }

    protected static int getOffsetPreviousPage(int limit, int offset) {
        int offsetPreviousPage = offset - limit;
        if (offsetPreviousPage < 0) {
            return NO_OFFSET;
        }
        return offsetPreviousPage;
    }

    protected static int getOffsetNextPage(int limit, int offset, int total) {
        int offsetNextPage = offset + limit;
        if (offsetNextPage > total - 1) {
            return NO_OFFSET;
        }
        return offsetNextPage;
    }
}
