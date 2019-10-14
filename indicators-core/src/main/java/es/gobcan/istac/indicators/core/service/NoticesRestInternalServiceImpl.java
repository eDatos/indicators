package es.gobcan.istac.indicators.core.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.utils.TranslateExceptions;
import org.siemac.metamac.core.common.lang.LocaleUtil;
import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.notices.v1_0.domain.Message;
import org.siemac.metamac.rest.notices.v1_0.domain.Notice;
import org.siemac.metamac.rest.notices.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.notices.v1_0.domain.enume.MetamacApplicationsEnum;
import org.siemac.metamac.rest.notices.v1_0.domain.enume.MetamacRolesEnum;
import org.siemac.metamac.rest.notices.v1_0.domain.utils.MessageBuilder;
import org.siemac.metamac.rest.notices.v1_0.domain.utils.NoticeBuilder;
import org.siemac.metamac.rest.utils.RestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.navigation.InternalWebApplicationNavigation;
import es.gobcan.istac.indicators.core.notices.ServiceNoticeAction;
import es.gobcan.istac.indicators.core.notices.ServiceNoticeMessage;

@Component(NoticesRestInternalService.BEAN_ID)
public class NoticesRestInternalServiceImpl implements NoticesRestInternalService {

    private static Logger                    logger = LoggerFactory.getLogger(NoticesRestInternalServiceImpl.class);

    @Autowired
    private RestApiLocator                   restApiLocator;

    @Autowired
    private IndicatorsConfigurationService   configurationService;

    @Autowired
    private TranslateExceptions              translateExceptions;

    private InternalWebApplicationNavigation internalWebApplicationNavigation;

    private String                           indicatorsInternalWebUrlBase;
    private String                           indicatorsApiInternalEndpointV10;

    @PostConstruct
    public void init() throws Exception {
        this.initEndpoints();
        this.internalWebApplicationNavigation = new InternalWebApplicationNavigation(this.indicatorsInternalWebUrlBase);
    }

    private void initEndpoints() throws MetamacException {
        this.indicatorsInternalWebUrlBase = this.configurationService.retrieveIndicatorsInternalWebApplicationUrlBase();
        this.indicatorsInternalWebUrlBase = StringUtils.removeEnd(this.indicatorsInternalWebUrlBase, "/");

        this.indicatorsApiInternalEndpointV10 = configurationService.retrieveIndicatorsInternalApiUrlBase();
        this.indicatorsApiInternalEndpointV10 = RestUtils.createLink(this.indicatorsApiInternalEndpointV10, IndicatorsConstants.API_VERSION_1_0);
    }

    @Override
    public void createCreateReplaceDatasetErrorBackgroundNotification(IndicatorVersion failedIndicator) {
        if (checkIfNotifyPopulationErrors(failedIndicator)) {
            createBackgroundNotification(ServiceNoticeAction.INDICATOR_CREATE_REPLACE_DATASET_ERROR, ServiceNoticeMessage.INDICATOR_CREATE_REPLACE_DATASET_ERROR, Arrays.asList(failedIndicator),
                    failedIndicator.getIndicator().getViewCode(), failedIndicator.getDataRepositoryTableName());
        }
    }

    @Override
    public void createAssignRolePermissionsDatasetErrorBackgroundNotification(String dataViewsRole, String viewCode) {
        createBackgroundNotification(ServiceNoticeAction.INDICATOR_ASSIGN_ROLE_PERMISSIONS_DATASET_ERROR, ServiceNoticeMessage.INDICATOR_ASSIGN_ROLE_PERMISSIONS_DATASET_ERROR,
                new ArrayList<IndicatorVersion>(), dataViewsRole, viewCode);
    }

    @Override
    public void createUpdateIndicatorsDataErrorBackgroundNotification(List<IndicatorVersion> failedPopulationIndicators) {
        List<IndicatorVersion> notifiableIndicators = getIndicatorsWithNotifyPopulationErrors(failedPopulationIndicators);
        if (!notifiableIndicators.isEmpty()) {
            createBackgroundNotification(ServiceNoticeAction.INDICATOR_POPULATION_ERROR, ServiceNoticeMessage.INDICATOR_POPULATION_ERROR, notifiableIndicators);
        }
    }

    @Override
    public void createDeleteDatasetErrorBackgroundNotification(IndicatorVersion failedIndicator, String oldDatasetId) {
        if (checkIfNotifyPopulationErrors(failedIndicator)) {
            createBackgroundNotification(ServiceNoticeAction.INDICATOR_DELETE_DATASET_ERROR, ServiceNoticeMessage.INDICATOR_DELETE_DATASET_ERROR, Arrays.asList(failedIndicator), oldDatasetId);
        }
    }

    @Override
    public void createConsumerFromKafkaErrorBackgroundNotification(String keyMessage) {
        createBackgroundNotification(ServiceNoticeAction.INDICATOR_RECEIVED_FROM_KAFKA_ERROR, ServiceNoticeMessage.INDICATOR_RECEIVED_FROM_KAFKA_ERROR, new ArrayList<IndicatorVersion>(), keyMessage);
    }

    @Override
    public void createMaximumVersionReachedBackgroundNotification(IndicatorVersion indicatorVersion, VersionTypeEnum versionTypeEnum) {
        if (checkIfNotifyPopulationErrors(indicatorVersion)) {
            createBackgroundNotification(ServiceNoticeAction.MAX_VERSION_REACHED_ERROR, ServiceNoticeMessage.MAX_VERSION_REACHED_ERROR, Arrays.asList(indicatorVersion), versionTypeEnum.getName(),
                    indicatorVersion.getCode(), indicatorVersion.getVersionNumber());
        }
    }

    @Override
    public void createPopulateIndicatorDataSuccessBackgroundNotification(String user, Indicator indicator) {
        try {
            if (checkIfNotifyPopulationErrors(indicator)) {
                Locale locale = configurationService.retrieveLanguageDefaultLocale();
                createPopulateIndicatorDataBackgroundNotication(locale, ServiceNoticeAction.INDICATOR_POPULATION_DATA_SUCCESS, ServiceNoticeMessage.INDICATOR_POPULATION_DATA_SUCCESS, user, indicator);
            }
        } catch (MetamacException e) {
            logger.error("Error creating createPopulateIndicatorDataSuccessBackgroundNotification:", e);
        }
    }

    @Override
    public void createPopulateIndicatorDataErrorBackgroundNotification(String user, Indicator indicator, MetamacException metamacException) {
        try {
            if (checkIfNotifyPopulationErrors(indicator)) {
                Locale locale = configurationService.retrieveLanguageDefaultLocale();
                Throwable localisedException = translateExceptions.translateException(locale, metamacException);

                createPopulateIndicatorDataBackgroundNotication(locale, ServiceNoticeAction.INDICATOR_POPULATION_DATA_ERROR, ServiceNoticeMessage.INDICATOR_POPULATION_DATA_ERROR, user, indicator,
                        localisedException.getMessage());
            }
        } catch (MetamacException e) {
            logger.error("Error creating createPopulateIndicatorDataErrorBackgroundNotification:", e);
        }
    }

    private List<IndicatorVersion> getIndicatorsWithNotifyPopulationErrors(List<IndicatorVersion> failedPopulationIndicators) {
        List<IndicatorVersion> notifiableIndicators = new ArrayList<IndicatorVersion>();
        for (IndicatorVersion failedIndicatorVersion : failedPopulationIndicators) {
            if (checkIfNotifyPopulationErrors(failedIndicatorVersion)) {
                notifiableIndicators.add(failedIndicatorVersion);
            }
        }
        return notifiableIndicators;
    }

    private boolean checkIfNotifyPopulationErrors(IndicatorVersion failedIndicator) {
        return checkIfNotifyPopulationErrors(failedIndicator.getIndicator());
    }

    private boolean checkIfNotifyPopulationErrors(Indicator indicator) {
        return indicator.getNotifyPopulationErrors();
    }

    private void createBackgroundNotification(String actionCode, String messageCode, List<IndicatorVersion> failedIndicators, Object... messageParams) {
        try {

            Notice notification = createNotice(actionCode, messageCode, failedIndicators, messageParams);
            restApiLocator.getNoticesRestInternalFacadeV10().createNotice(notification);

        } catch (MetamacException e) {
            logger.error("Error creating createErrorBackgroundNotification:", e);
        }

    }

    private void createPopulateIndicatorDataBackgroundNotication(Locale locale, String actionCode, String messageCode, String user, Indicator indicator, Object... messageParams) {
        ResourceInternal resourceInternal = indicatorToResourceInternal(indicator);

        Message message = createMessage(locale, Arrays.asList(resourceInternal), messageCode, messageParams);

        Notice notice = createPopulateIndicatorDataNotice(locale, actionCode, indicator.getCode(), message, user);

        restApiLocator.getNoticesRestInternalFacadeV10().createNotice(notice);
    }

    private Message createMessage(Locale locale, List<ResourceInternal> resources, String messageCode, Object... messageParams) {
        String localisedMessage = (messageParams == null) ? LocaleUtil.getMessageForCode(messageCode, locale) : getMessageForCodeWithParams(messageCode, locale, messageParams);

        // @formatter:off
        return  MessageBuilder.message()
                .withText(localisedMessage)
                .withResources(resources)
                .build();
        // @formatter:off
    }
    
    private Notice createPopulateIndicatorDataNotice(Locale locale, String actionCode, String actionParams, Message message, String user) {
        String subject = getMessageForCodeWithParams(actionCode, locale, actionParams);
    
        // @formatter:off
        return NoticeBuilder.notification()
                .withMessages(message)
                .withSendingApplication(MetamacApplicationsEnum.GESTOR_INDICADORES.getName())
                .withReceivers(user)
                .withSendingUser(user)
                .withSubject(subject)
                .build();
        // @formatter:on
    }

    private Notice createNotice(String actionCode, String messageCode, List<IndicatorVersion> failedIndicators, Object... messageParams) throws MetamacException {
        Locale locale = configurationService.retrieveLanguageDefaultLocale();
        String subject = LocaleUtil.getMessageForCode(actionCode, locale);
        String sendingApp = MetamacApplicationsEnum.GESTOR_INDICADORES.getName();

        List<ResourceInternal> resources = indicatorVersionsToResourceInternal(failedIndicators);

        Message message = createMessage(locale, resources, messageCode, messageParams);

        // @formatter:off
        return NoticeBuilder.notification()
            .withMessages(message)
            .withSendingApplication(sendingApp)
            .withSubject(subject)
            .withRoles(MetamacRolesEnum.ADMINISTRADOR)
            .build();
        // @formatter:on
    }

    private String getMessageForCodeWithParams(String actionCode, Locale locale, Object... messageParams) {
        String localisedMessage = LocaleUtil.getMessageForCode(actionCode, locale);
        return MessageFormat.format(localisedMessage, messageParams);
    }

    private List<ResourceInternal> indicatorVersionsToResourceInternal(List<IndicatorVersion> indicatorsVersions) {
        List<ResourceInternal> resources = new ArrayList<ResourceInternal>();

        for (IndicatorVersion indicatorVersion : indicatorsVersions) {
            resources.add(indicatorVersionToResource(indicatorVersion));
        }

        return resources;
    }

    private ResourceInternal indicatorVersionToResource(IndicatorVersion indicatorVersion) {
        ResourceInternal resource = new ResourceInternal();
        if (indicatorVersion != null) {
            resource.setId(composeCodeWithVersion(indicatorVersion));
            resource.setUrn(indicatorVersion.getUuid());
            resource.setSelfLink(toIndicatorSelfLink(indicatorVersion.getIndicator()));
            resource.setKind(TypeExternalArtefactsEnum.INDICATOR.getValue());
            resource.setName(this.toInternationalString(indicatorVersion.getTitle()));
            resource.setManagementAppLink(this.toIndicatorManagementApplicationLink(indicatorVersion.getIndicator()));
        }
        return resource;
    }

    private ResourceInternal indicatorToResourceInternal(Indicator indicator) {
        ResourceInternal resource = new ResourceInternal();
        if (indicator != null) {
            resource.setId(indicator.getCode());
            resource.setUrn(indicator.getUuid());
            resource.setSelfLink(toIndicatorSelfLink(indicator));
            resource.setKind(TypeExternalArtefactsEnum.INDICATOR.getValue());
            resource.setName(this.toInternationalString(
                    indicator.getProductionIndicatorVersion() != null ? indicator.getProductionIndicatorVersion().getTitle() : indicator.getDiffusionIndicatorVersion().getTitle()));
            resource.setManagementAppLink(this.toIndicatorManagementApplicationLink(indicator));
        }
        return resource;
    }

    private String composeCodeWithVersion(IndicatorVersion indicatorVersion) {
        return indicatorVersion.getIndicator().getCode().concat(" - v").concat(indicatorVersion.getVersionNumber());
    }

    // Atención: Este método replica funcionalidad de es.gobcan.istac.indicators.rest.component.UriLinks.getIndicatorsLink()
    private ResourceLink toIndicatorSelfLink(Indicator indicator) {
        return toResourceLink(TypeExternalArtefactsEnum.INDICATOR.getValue(), toIndicatorLink(indicator.getCode()));
    }

    private String toIndicatorLink(String code) {
        return RestUtils.createLink(getIndicatorsLink(), code);
    }

    private String getIndicatorsLink() {
        return RestUtils.createLink(this.indicatorsApiInternalEndpointV10, IndicatorsConstants.API_INDICATORS_INDICATORS);
    }

    private ResourceLink toResourceLink(String kind, String href) {
        ResourceLink target = new ResourceLink();
        target.setKind(kind);
        target.setHref(href);
        return target;
    }

    public String toIndicatorManagementApplicationLink(Indicator indicator) {
        return this.internalWebApplicationNavigation.buildIndicatorUrl(indicator.getCode());
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
