package es.gobcan.istac.indicators.web.shared.criteria;

import org.siemac.metamac.web.common.shared.criteria.MetamacVersionableWebCriteria;

import es.gobcan.istac.indicators.web.shared.criteria.base.HasStatisticalOperationCriteria;

public class VersionableStatisticalResourceWebCriteria extends MetamacVersionableWebCriteria implements HasStatisticalOperationCriteria {

    private static final long   serialVersionUID = 1L;

    private String              statisticalOperationUrn;
    private String              code;
    private String              title;
    private String              urn;
    private String              description;

    public VersionableStatisticalResourceWebCriteria() {
        super();
    }

    public VersionableStatisticalResourceWebCriteria(String criteria) {
        super(criteria, true);
    }

    @Override
    public String getStatisticalOperationUrn() {
        return statisticalOperationUrn;
    }

    @Override
    public void setStatisticalOperationUrn(String statisticalOperationUrn) {
        this.statisticalOperationUrn = statisticalOperationUrn;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getUrn() {
        return urn;
    }

    public String getDescription() {
        return description;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}