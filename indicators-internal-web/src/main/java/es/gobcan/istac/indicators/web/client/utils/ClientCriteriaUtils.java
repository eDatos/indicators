package es.gobcan.istac.indicators.web.client.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder;
import org.siemac.metamac.core.common.criteria.shared.MetamacCriteriaOrder.OrderTypeEnum;

import com.smartgwt.client.data.SortSpecifier;
import com.smartgwt.client.types.SortDirection;

import es.gobcan.istac.indicators.core.criteria.IndicatorCriteriaOrderEnum;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorDS;

public class ClientCriteriaUtils {

    private static Map<String, IndicatorCriteriaOrderEnum> fieldCriteriaMapping = new HashMap<String, IndicatorCriteriaOrderEnum>();
    private static Map<SortDirection, OrderTypeEnum>       orderTypeMapping     = new HashMap<SortDirection, MetamacCriteriaOrder.OrderTypeEnum>();

    static {
        fieldCriteriaMapping.put(IndicatorDS.CODE, IndicatorCriteriaOrderEnum.CODE);
        fieldCriteriaMapping.put(IndicatorDS.TITLE, IndicatorCriteriaOrderEnum.TITLE);
        fieldCriteriaMapping.put(IndicatorDS.PROC_STATUS, IndicatorCriteriaOrderEnum.PRODUCTION_PROC_STATUS);
        fieldCriteriaMapping.put(IndicatorDS.VERSION_NUMBER, IndicatorCriteriaOrderEnum.PRODUCTION_VERSION);
        fieldCriteriaMapping.put(IndicatorDS.PROC_STATUS_DIFF, IndicatorCriteriaOrderEnum.DIFFUSION_PROC_STATUS);
        fieldCriteriaMapping.put(IndicatorDS.VERSION_NUMBER_DIFF, IndicatorCriteriaOrderEnum.DIFFUSION_VERSION);

        orderTypeMapping.put(SortDirection.ASCENDING, OrderTypeEnum.ASC);
        orderTypeMapping.put(SortDirection.DESCENDING, OrderTypeEnum.DESC);
    }

    public static List<MetamacCriteriaOrder> buildIndicatorCriteriaOrder(SortSpecifier[] sortSpecifiers) {
        List<MetamacCriteriaOrder> orders = new ArrayList<MetamacCriteriaOrder>();
        if (sortSpecifiers != null) {
            for (SortSpecifier sortSpecifier : sortSpecifiers) {
                IndicatorCriteriaOrderEnum field = getIndicatorCriteriaOrderEnum(sortSpecifier.getField());
                if (field != null) {
                    MetamacCriteriaOrder order = new MetamacCriteriaOrder();
                    order.setPropertyName(field.name());
                    order.setType(orderTypeMapping.get(sortSpecifier.getSortDirection()));
                    orders.add(order);
                }

            }
        }
        return orders;
    }

    private static IndicatorCriteriaOrderEnum getIndicatorCriteriaOrderEnum(String indicatorField) {
        return fieldCriteriaMapping.containsKey(indicatorField) ? fieldCriteriaMapping.get(indicatorField) : null;
    }

    public static List<MetamacCriteriaOrder> buildIndicatorCriteriaOrder(IndicatorCriteriaOrderEnum criteriaOrderEnum, OrderTypeEnum orderTypeEnum) {
        List<MetamacCriteriaOrder> orders = new ArrayList<MetamacCriteriaOrder>();
        if (criteriaOrderEnum != null) {
            MetamacCriteriaOrder order = new MetamacCriteriaOrder();
            order.setPropertyName(criteriaOrderEnum.name());
            order.setType(orderTypeEnum);
            orders.add(order);
        }
        return orders;
    }
}
