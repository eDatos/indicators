package es.gobcan.istac.indicators.web.shared.criteria;

import org.siemac.metamac.web.common.shared.criteria.PaginationWebCriteria;

import es.gobcan.istac.indicators.web.client.utils.IndicatorsWebConstants;

public class IndicatorsSystemCriteria extends PaginationWebCriteria {

    private static final long serialVersionUID = -6655051147299387214L;

    private String            code;
    private Boolean           isOperational;

    public IndicatorsSystemCriteria() {
        setFirstResult(0);
        setMaxResults(IndicatorsWebConstants.SYSTEMS_LISTGRID_MAX_RESULTS);
    }

    public IndicatorsSystemCriteria(IndicatorsSystemCriteria criteria, boolean isOperational) {
        setFirstResult(0);
        setMaxResults(IndicatorsWebConstants.SYSTEMS_LISTGRID_MAX_RESULTS);
        setCriteria(criteria.getCriteria());
        setCode(criteria.getCode());
        setIsOperational(isOperational);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getIsOperational() {
        return isOperational;
    }

    public void setIsOperational(Boolean isOperational) {
        this.isOperational = isOperational;
    }

}
