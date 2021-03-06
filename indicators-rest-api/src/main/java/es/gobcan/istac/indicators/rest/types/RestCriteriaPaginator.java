package es.gobcan.istac.indicators.rest.types;

public class RestCriteriaPaginator {

    private Integer limit  = null;
    private Integer offset = null;

    public RestCriteriaPaginator() {
        // dummy constructor necessary for Jackson
    }

    public RestCriteriaPaginator(Integer limit, Integer offset) {
        super();
        this.limit = limit;
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

}
