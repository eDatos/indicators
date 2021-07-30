package es.gobcan.istac.indicators.core.job;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.MetamacPrincipalAccess;
import org.siemac.metamac.sso.client.SsoClientConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.enume.domain.RoleEnum;
import es.gobcan.istac.indicators.core.service.NoticesRestInternalService;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class IndicatorsUpdateJob implements Job {

    private static Logger           LOG = LoggerFactory.getLogger(IndicatorsUpdateJob.class);

    private IndicatorsServiceFacade indicatorsServiceFacade;

    public IndicatorsUpdateJob() {
    }

    private IndicatorsServiceFacade getIndicatorsServiceFacade() {
        if (indicatorsServiceFacade == null) {
            indicatorsServiceFacade = ApplicationContextProvider.getApplicationContext().getBean(IndicatorsServiceFacade.class);
        }
        return indicatorsServiceFacade;
    }

    @Override
    public void execute(JobExecutionContext context) {

        ServiceContext serviceContext = new ServiceContext("updateJob", context.getFireInstanceId(), "metamac-core");
        MetamacPrincipal metamacPrincipal = new MetamacPrincipal();
        metamacPrincipal.setUserId(serviceContext.getUserId());
        metamacPrincipal.getAccesses().add(new MetamacPrincipalAccess(RoleEnum.ADMINISTRADOR.getName(), IndicatorsConstants.SECURITY_APPLICATION_ID, null));
        serviceContext.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);

        List<IndicatorVersion> failedPopulationIndicators = new ArrayList<>();

        updateIndicatorsDataFromGpe(serviceContext, failedPopulationIndicators);
        updateIndicatorsDataFromJsonStat(serviceContext, failedPopulationIndicators);

        createUpdateIndicatorsDataErrorBackgroundNotification(failedPopulationIndicators);
    }

    private void createUpdateIndicatorsDataErrorBackgroundNotification(List<IndicatorVersion> failedPopulationIndicators) {
        if (!failedPopulationIndicators.isEmpty()) {
            getNoticesRestInternalService().createUpdateIndicatorsDataErrorBackgroundNotification(failedPopulationIndicators);
        }
    }

    private void updateIndicatorsDataFromGpe(ServiceContext serviceContext, List<IndicatorVersion> failedPopulationIndicators) {
        try {
            LOG.info("Updating indicators Data from GPE...");
            failedPopulationIndicators.addAll(getIndicatorsServiceFacade().updateIndicatorsDataFromGpe(serviceContext));
        } catch (MetamacException e) {
            LOG.error("Updating indicators Data from GPE", e);
        }
    }

    private void updateIndicatorsDataFromJsonStat(ServiceContext serviceContext, List<IndicatorVersion> failedPopulationIndicators) {
        try {
            LOG.info("Updating indicators Data from JSON-stat...");
            failedPopulationIndicators.addAll(getIndicatorsServiceFacade().updateIndicatorsDataFromJsonStat(serviceContext));
        } catch (MetamacException e) {
            LOG.error("Updating indicators Data from JSON-stat", e);
        }
    }

    private NoticesRestInternalService getNoticesRestInternalService() {
        return (NoticesRestInternalService) ApplicationContextProvider.getApplicationContext().getBean(NoticesRestInternalService.BEAN_ID);
    }
}
