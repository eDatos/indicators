package es.gobcan.istac.indicators.web.client.indicator.widgets;

import es.gobcan.istac.indicators.web.client.widgets.filter.base.VersionableStatisticalResourceFilterBaseForm;
import es.gobcan.istac.indicators.web.shared.criteria.QueryWebCriteria;

public class QueryFilterForm extends VersionableStatisticalResourceFilterBaseForm<QueryWebCriteria> {

    private String fixedQueryCode;

    public QueryFilterForm() {
        this.fixedQueryCode = null;
        super.onlyLastVersionFacet.getFormItem().setVisible(false); // No needed for queries
    }

    public void setFixedQueryCode(String fixedQueryCode) {
        this.fixedQueryCode = fixedQueryCode;
    }

    @Override
    public QueryWebCriteria getSearchCriteria() {
        QueryWebCriteria criteria = super.getSearchCriteria();
        criteria.setFixedQueryCode(fixedQueryCode);
        return criteria;
    }

    @Override
    protected QueryWebCriteria buildEmptySearchCriteria() {
        return new QueryWebCriteria();
    }

}
