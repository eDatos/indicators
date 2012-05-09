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
        if (caught != null && caught instanceof MetamacWebException) {
            List<MetamacWebExceptionItem> metamacExceptionItems = ((MetamacWebException) caught).getWebExceptionItems();
            if (metamacExceptionItems.isEmpty()) {
                list.add(alternativeMessage);
            } else {
                for (MetamacWebExceptionItem item : metamacExceptionItems) {
                    if (EXCEPTION_UNKNOWN.equals(item.getCode())) {
                        // If exception code is EXCEPTION_UNKNOWN, show alternative message
                        list.add(alternativeMessage);
                    } else {
                        // If there is any problem getting message from code, show alternative message
                        try {
                            list.add(getMessage(item));
                        } catch (MetamacWebException e) {
                            list.add(alternativeMessage);
                        }
                    }
                }
            }
        } else {
            list.add(alternativeMessage);
        }
        return list;
    }

    private static String getMessage(MetamacWebExceptionItem item) throws MetamacWebException {
        if (item != null && item.getCode() != null) {
            // GWT generate a "_" separated method when the key is separated by "."
            String code = transformMessageCode(item.getCode());
            try {
                String message = getCoreMessages().getString(code);
                return getMessageWithParameters(message, getTranslatedParameters(item.getMessageParameters()));
            } catch (Exception e) {
                throw new MetamacWebException("MESSAGE_NOT_FOUND", "Message with code = " + code + " not found");
            }
        }
        throw new MetamacWebException("NULL_MESSAGE", "Message with null code");
    }

    private static String[] getTranslatedParameters(String[] messageParameters) {
        if (messageParameters != null) {
            String[] translatedParameters = new String[messageParameters.length];
            for (int i = 0; i < messageParameters.length; i++) {
                // if (messageParameters[i] instanceof String) {
                String code = transformMessageCode(String.valueOf(messageParameters[i]));
                try {
                    // If parameter is translated, return the translation
                    translatedParameters[i] = getCoreMessages().getString(code);
                } catch (Exception e) {
                    translatedParameters[i] = String.valueOf(messageParameters[i]);
                }
                // }
                // } else if (messageParameters[i] instanceof String[]) {
                // StringBuilder builder = new StringBuilder();
                // for (int j = 0; j < ((String[])messageParameters[i]).length; j++) {
                // String parameter = ((String[])messageParameters[i])[j];
                // String code = transformMessageCode(parameter);
                // try {
                // builder.append(getCoreMessages().getString(code));
                // } catch (Exception e) {
                // builder.append(parameter);
                // }
                // builder.append((j != ((String[])messageParameters[i]).length - 1) ? "," : new String());
                // }
                // translatedParameters[i] = builder.toString();
                // }
            }
            return translatedParameters;
        }
        return null;
    }

}
