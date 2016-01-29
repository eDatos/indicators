package es.gobcan.istac.indicators.core.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.lang.LocaleUtil;
import org.siemac.metamac.rest.notices.v1_0.domain.Notice;
import org.siemac.metamac.rest.notices.v1_0.domain.enume.MetamacApplicationsEnum;
import org.siemac.metamac.rest.notices.v1_0.domain.enume.MetamacRolesEnum;
import org.siemac.metamac.rest.notices.v1_0.domain.utils.NoticeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;

@Component(NoticesRestInternalService.BEAN_ID)
public class NoticesRestInternalServiceImpl implements NoticesRestInternalService {

    private static Logger                  logger = LoggerFactory.getLogger(NoticesRestInternalServiceImpl.class);

    @Autowired
    private NoticeApiLocator               restApiLocator;

    @Autowired
    private IndicatorsConfigurationService configurationService;

    @Override
    public void createErrorBackgroundNotification(String user, String actionCode, String message, List<IndicatorVersion> failedPopulationIndicators) {
        try {
            if (failedPopulationIndicators.size() > 0) {
                Locale locale = configurationService.retrieveLanguageDefaultLocale();
                String subject = LocaleUtil.getMessageForCode(actionCode, locale);
                String sendingApp = MetamacApplicationsEnum.GESTOR_INDICADORES.getName();

                List<String> translatedMessages = processMessages(message, failedPopulationIndicators);
                String[] messages = new String[translatedMessages.size()];
                messages = translatedMessages.toArray(messages);

                // @formatter:off
                Notice notification = NoticeBuilder.notification()
                        .withMessagesWithoutResources(messages)
                        .withSendingApplication(sendingApp)
                        .withSendingUser(user)
                        .withSubject(subject)
                        .withRoles(MetamacRolesEnum.ADMINISTRADOR)
                        .build();
                // @formatter:on

                restApiLocator.getNoticesRestInternalFacadeV10().createNotice(notification);
            }
        } catch (MetamacException e) {
            logger.error("Error creating createErrorBackgroundNotification:", e);
        }

    }

    private List<String> processMessages(String messageCode, List<IndicatorVersion> failedPopulationIndicators) throws MetamacException {
        List<String> messages = new ArrayList<String>();
        for (IndicatorVersion failedPopulationIndicator : failedPopulationIndicators) {
            messages.add(createTranslatedMessage(messageCode, failedPopulationIndicator));
        }
        return messages;
    }

    private String createTranslatedMessage(String messageCode, IndicatorVersion failedPopulationIndicator) throws MetamacException {
        Locale locale = configurationService.retrieveLanguageDefaultLocale();
        String localisedMessage = LocaleUtil.getMessageForCode(messageCode, locale);
        // UUID = {0}
        localisedMessage = MessageFormat.format(localisedMessage, failedPopulationIndicator.getUuid());
        return localisedMessage;
    }

}
