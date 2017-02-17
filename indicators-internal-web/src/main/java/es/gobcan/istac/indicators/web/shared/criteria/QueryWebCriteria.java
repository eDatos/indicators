package es.gobcan.istac.indicators.web.shared.criteria;

public class QueryWebCriteria extends LifeCycleStatisticalResourceWebCriteria {

    private String            fixedQueryCode;
    
    private static final long serialVersionUID = -6034202563907012926L;

    public QueryWebCriteria() {
        super();
        fixedQueryCode = null;
    }
    
    public QueryWebCriteria(String criteria) {
        super(criteria);
    }

    public void setFixedQueryCode(String queryCode) {
        this.fixedQueryCode = queryCode;
    }

    public String getFixedQueryCode() {
        return fixedQueryCode;
    }
    
}
