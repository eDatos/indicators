package es.gobcan.istac.indicators.web.client.utils;


import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getCoreMessages;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.web.common.client.utils.CommonErrorUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.siemac.metamac.web.common.shared.exception.MetamacWebExceptionItem;


public class ErrorUtils extends CommonErrorUtils {

    public static List<String> getErrorMessages(Throwable caught, String alternativeMessage) {
        List<String> list = new ArrayList<String>();
        if (caught instanceof MetamacWebException) {
            List<MetamacWebExceptionItem> metamacExceptionItems = ((MetamacWebException) caught).getWebExceptionItems();
            for (MetamacWebExceptionItem item : metamacExceptionItems) {
                if (EXCEPTION_UNKNOWN.equals(item.getCode())) {
                    list.add(alternativeMessage);
                } else {
                    list.add(getMessage(item));
                }
            }
        } else {
            list.add(alternativeMessage);
        }
        return list;
    }

    private static String getMessage(MetamacWebExceptionItem item) {
        if (item != null && item.getCode() != null) {
            // GWT generate a "_" separated method when the key is separated by "."
            String code = item.getCode().replace(".", "_");
            String message = getCoreMessages().getString(code);
            return getMessageWithParameters(message, item.getMessageParameters());
        }
        return new String();
    }
    
}
