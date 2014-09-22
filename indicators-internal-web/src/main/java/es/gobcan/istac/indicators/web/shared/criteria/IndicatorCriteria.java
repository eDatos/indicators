package es.gobcan.istac.indicators.web.shared.criteria;

import org.siemac.metamac.web.common.shared.criteria.PaginationWebCriteria;

import es.gobcan.istac.indicators.core.enume.domain.IndicatorProcStatusEnum;

public class IndicatorCriteria extends PaginationWebCriteria {

    private static final long       serialVersionUID = -6655051147299387214L;

    private String                  code;
    private String                  title;
    private IndicatorProcStatusEnum productionVersionProcStatus;
    private IndicatorProcStatusEnum diffusionVersionProcStatus;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public IndicatorProcStatusEnum getProductionVersionProcStatus() {
        return productionVersionProcStatus;
    }

    public void setProductionVersionProcStatus(IndicatorProcStatusEnum productionVersionProcStatus) {
        this.productionVersionProcStatus = productionVersionProcStatus;
    }

    public IndicatorProcStatusEnum getDiffusionVersionProcStatus() {
        return diffusionVersionProcStatus;
    }

    public void setDiffusionVersionProcStatus(IndicatorProcStatusEnum diffusionVersionProcStatus) {
        this.diffusionVersionProcStatus = diffusionVersionProcStatus;
    }
}
