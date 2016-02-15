package es.gobcan.istac.indicators.core.service;

import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.lang.LocaleUtil;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.notices.v1_0.domain.Message;
import org.siemac.metamac.rest.notices.v1_0.domain.Notice;
import org.siemac.metamac.rest.notices.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.notices.v1_0.domain.enume.MetamacApplicationsEnum;
import org.siemac.metamac.rest.notices.v1_0.domain.enume.MetamacRolesEnum;
import org.siemac.metamac.rest.notices.v1_0.domain.utils.MessageBuilder;
import org.siemac.metamac.rest.notices.v1_0.domain.utils.NoticeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.navigation.InternalWebApplicationNavigation;

@Component(NoticesRestInternalService.BEAN_ID)
public class NoticesRestInternalServiceImpl implements NoticesRestInternalService {

    private static Logger                  logger = LoggerFactory.getLogger(NoticesRestInternalServiceImpl.class);

    @Autowired
    private RestApiLocator               restApiLocator;

    @Autowired
    private IndicatorsConfigurationService configurationService;
    
    private InternalWebApplicationNavigation internalWebApplicationNavigation;

    private String indicatorsExternalWebUrlBase;

    @PostConstruct
    public void init() throws Exception {
        this.initEndpoints();
        this.internalWebApplicationNavigation = new InternalWebApplicationNavigation(this.indicatorsExternalWebUrlBase);
    }
    
    private void initEndpoints() throws MetamacException {
        this.indicatorsExternalWebUrlBase = this.configurationService.retrieveIndicatorsExternalWebUrlBase();
        this.indicatorsExternalWebUrlBase = StringUtils.removeEnd(this.indicatorsExternalWebUrlBase, "/");
    }

    @Override
    public void createErrorBackgroundNotification(String user, String actionCode, String messageCode, List<IndicatorVersion> failedPopulationIndicators) {
        try {
            if (failedPopulationIndicators.size() > 0) {
                Locale locale = configurationService.retrieveLanguageDefaultLocale();
                String subject = LocaleUtil.getMessageForCode(actionCode, locale);
                String localisedMessage = LocaleUtil.getMessageForCode(messageCode, locale);
                String sendingApp = MetamacApplicationsEnum.GESTOR_INDICADORES.getName();
                
                ResourceInternal[] resources = this.indicatorVersionsToResourceInternal(failedPopulationIndicators);
                
                // @formatter:off
                Message message = MessageBuilder.message().withText(localisedMessage)
                    .withResources(resources).build();
                
                Notice notification = NoticeBuilder.notification()
                        .withMessages(message)                                
                        .withSendingApplication(sendingApp)
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
    
    private ResourceInternal[] indicatorVersionsToResourceInternal(List<IndicatorVersion> indicatorsVersions) {
        ResourceInternal[] resources = new ResourceInternal[indicatorsVersions.size()];

        for(int i = 0; i < indicatorsVersions.size(); i++) {
            resources[i] = indicatorVersionToResource(indicatorsVersions.get(i));
        }
        return resources;
    }

    private ResourceInternal indicatorVersionToResource(IndicatorVersion indicatorVersion) {
        ResourceInternal resource = new ResourceInternal();
        if (indicatorVersion != null) {
            resource.setId(indicatorVersion.getIndicator().getCode());
            resource.setUrn(indicatorVersion.getUuid());
            resource.setName(this.toInternationalString(indicatorVersion.getTitle()));
            resource.setManagementAppLink(this.toIndicatorManagementApplicationLink(indicatorVersion.getUuid()));
        }
        return resource;
    }

    public String toIndicatorManagementApplicationLink(String indicatorUuid) {
        return this.internalWebApplicationNavigation.buildIndicatorUrl(indicatorUuid);
    }

    private InternationalString toInternationalString(org.siemac.metamac.core.common.ent.domain.InternationalString sources) {
        if (sources == null) {
            return null;
        }
        InternationalString targets = new InternationalString();
        for (org.siemac.metamac.core.common.ent.domain.LocalisedString source : sources.getTexts()) {
            LocalisedString target = new LocalisedString();
            target.setValue(source.getLabel());
            target.setLang(source.getLocale());
            targets.getTexts().add(target);
        }
        return targets;
    }
}
