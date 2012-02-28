package es.gobcan.istac.indicators.web.client.utils;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.siemac.metamac.web.common.shared.exception.MetamacWebExceptionItem;

import es.gobcan.istac.indicators.web.client.IndicatorsWeb;


public class ErrorUtils {

    public static List<String> getErrorMessages(Throwable caught, String alternativeMessage) {
        List<String> list = new ArrayList<String>();
        if (caught instanceof MetamacWebException) {
            List<MetamacWebExceptionItem> metamacExceptionItems = ((MetamacWebException) caught).getWebExceptionItems();
            for (MetamacWebExceptionItem item : metamacExceptionItems) {
                list.add(getMessage(item));
            }
        } else {
            list.add(alternativeMessage);
        }
        return list;
    }

    public static List<String> getMessageList(String ...messages) {
        List<String> messageList = new ArrayList<String>();
        for (String message : messages) {
            messageList.add(message);
        }
        return messageList;
    }
    
    private static String getMessage(MetamacWebExceptionItem item) {
        if (item != null && item.getCode() != null) {
            // GWT generate a "_" separated method when the key is separated by "."
            String code = item.getCode().replace(".", "_");
            String message =IndicatorsWeb.getCoreMessages().getString(code);
            return getMessageWithParameters(message, item.getMessageParameters());
        }
        return new String();
    }
    
    private static String getMessageWithParameters(String message, Serializable[] parameters) {
        if (parameters != null) {
            int i = 0;
            while (i < parameters.length) {
                String delimiter = "{" + i + "}";
                while(message.contains(delimiter)) {
                    message = message.replace( delimiter, String.valueOf(parameters[i]));
                }
                i++;
            }
        }
        return message;
    }
    
}
