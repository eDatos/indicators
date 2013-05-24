package es.gobcan.istac.indicators.core.job;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.MetamacPrincipalAccess;
import org.siemac.metamac.sso.client.SsoClientConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.enume.domain.RoleEnum;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class IndicatorsUpdateJob implements Job {

    private static Logger         logger = LoggerFactory.getLogger(IndicatorsUpdateJob.class);

    
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
    public void execute(JobExecutionContext context) throws JobExecutionException {

        ServiceContext serviceContext = new ServiceContext("updateJob", context.getFireInstanceId(), "metamac-core");
        MetamacPrincipal metamacPrincipal = new MetamacPrincipal();
        metamacPrincipal.setUserId(serviceContext.getUserId());
        metamacPrincipal.getAccesses().add(new MetamacPrincipalAccess(RoleEnum.ADMINISTRADOR.getName(), IndicatorsConstants.SECURITY_APPLICATION_ID, null));
        serviceContext.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);
        
        try {
            getIndicatorsServiceFacade().updateIndicatorsData(serviceContext);
        } catch (MetamacException e) {
            logger.error("Error updating indicators Data");
        }
    }
}
