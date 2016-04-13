package es.gobcan.istac.indicators.core.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
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
            createBackgroundNotification(ServiceNoticeAction.INDICATOR_POPULATION_ERROR, ServiceNoticeMessage.INDICATOR_POPULATION_ERROR, failedPopulationIndicators);
        }
    }

    @Override
    public void createDeleteDatasetErrorBackgroundNotification(IndicatorVersion failedIndicator, String oldDatasetId) {
        if (checkIfNotifyPopulationErrors(failedIndicator)) {
            createBackgroundNotification(ServiceNoticeAction.INDICATOR_DELETE_DATASET_ERROR, ServiceNoticeMessage.INDICATOR_DELETE_DATASET_ERROR, Arrays.asList(failedIndicator), oldDatasetId);
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
        return failedIndicator.getIndicator().getNotifyPopulationErrors();
    }

    private void createBackgroundNotification(String actionCode, String messageCode, List<IndicatorVersion> failedIndicators, Object... messageParams) {
        try {

            Notice notification = createNotice(actionCode, messageCode, failedIndicators, messageParams);
            restApiLocator.getNoticesRestInternalFacadeV10().createNotice(notification);

        } catch (MetamacException e) {
            logger.error("Error creating createErrorBackgroundNotification:", e);
        }

    }

    private Notice createNotice(String actionCode, String messageCode, List<IndicatorVersion> failedIndicators, Object... messageParams) throws MetamacException {
        Locale locale = configurationService.retrieveLanguageDefaultLocale();
        String subject = LocaleUtil.getMessageForCode(actionCode, locale);
        String localisedMessage = getMessageForCodeWithParams(messageCode, locale, messageParams);
        String sendingApp = MetamacApplicationsEnum.GESTOR_INDICADORES.getName();

        List<ResourceInternal> resources = indicatorVersionsToResourceInternal(failedIndicators);

        // @formatter:off
        Message message = MessageBuilder.message()
            .withText(localisedMessage)
            .withResources(resources)
            .build();

        Notice notification = NoticeBuilder.notification()
            .withMessages(message)
            .withSendingApplication(sendingApp)
            .withSubject(subject)
            .withRoles(MetamacRolesEnum.ADMINISTRADOR)
            .build();
        // @formatter:on
        return notification;
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
