package es.gobcan.istac.indicators.web.server.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.schema.common.v1_0.domain.MetamacExceptionItemList;

public class WSExceptionUtils {

    /**
     * Returns {@link MetamacExceptionItem} list from a {@link MetamacExceptionItemList}
     * 
     * @param exceptionItemList
     * @return
     */
    public static List<MetamacExceptionItem> getMetamacExceptionItems(MetamacExceptionItemList exceptionItemList) {
        List<MetamacExceptionItem> metamacExceptionItems = new ArrayList<MetamacExceptionItem>();
        if (exceptionItemList != null) {
            for (org.siemac.metamac.schema.common.v1_0.domain.MetamacExceptionItem item : exceptionItemList.getMetamacExceptionItem()) {
                MetamacExceptionItem metamacExceptionItem = new MetamacExceptionItem();
                metamacExceptionItem.setCode(item.getCode());
                metamacExceptionItem.setMessage(item.getMessage());
                if (item.getMessageParameters() != null) {
                    Serializable[] messageParameters = item.getMessageParameters().toArray(new Serializable[item.getMessageParameters().size()]);
                    metamacExceptionItem.setMessageParameters(messageParameters);
                }
            }
        }
        return metamacExceptionItems;
    }

}
