package es.gobcan.istac.indicators.web.shared.criteria;

import org.siemac.metamac.web.common.shared.criteria.PaginationWebCriteria;

public class GeoValueCriteria extends PaginationWebCriteria {

    private static final long serialVersionUID = 5888787900805195271L;

    private String            granularityCode;

    public String getGranularityCode() {
        return granularityCode;
    }

    public void setGranularityCode(String granularityCode) {
        this.granularityCode = granularityCode;
    }
}
