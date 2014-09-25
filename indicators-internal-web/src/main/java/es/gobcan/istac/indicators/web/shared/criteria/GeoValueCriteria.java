package es.gobcan.istac.indicators.web.shared.criteria;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder;
import org.siemac.metamac.web.common.shared.criteria.PaginationWebCriteria;

import es.gobcan.istac.indicators.web.client.utils.IndicatorsWebConstants;

public class GeoValueCriteria extends PaginationWebCriteria {

    private static final long          serialVersionUID = 5888787900805195271L;

    private String                     granularityCode;

    private List<MetamacCriteriaOrder> orders           = new ArrayList<MetamacCriteriaOrder>();

    public GeoValueCriteria() {
        setFirstResult(0);
        setMaxResults(IndicatorsWebConstants.LISTGRID_MAX_RESULTS);
    }

    public String getGranularityCode() {
        return granularityCode;
    }

    public void setGranularityCode(String granularityCode) {
        this.granularityCode = granularityCode;
    }

    public List<MetamacCriteriaOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<MetamacCriteriaOrder> orders) {
        this.orders = orders;
    }
}
