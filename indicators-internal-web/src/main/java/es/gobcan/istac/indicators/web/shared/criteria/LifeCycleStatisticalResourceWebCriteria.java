package es.gobcan.istac.indicators.web.shared.criteria;

public class LifeCycleStatisticalResourceWebCriteria extends VersionableStatisticalResourceWebCriteria {

    private static final long serialVersionUID = 1L;

    private String   procStatus;

    public LifeCycleStatisticalResourceWebCriteria() {
        super();
    }

    public LifeCycleStatisticalResourceWebCriteria(String criteria) {
        super(criteria);
    }

    public String getProcStatus() {
        return procStatus;
    }

    public void setProcStatus(String procStatus) {
        this.procStatus = procStatus;
    }
}
