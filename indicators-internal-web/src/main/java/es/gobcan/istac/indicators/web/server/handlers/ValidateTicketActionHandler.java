package es.gobcan.istac.indicators.web.server.handlers;

import java.util.UUID;

import javax.annotation.PostConstruct;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.MetamacPrincipalAccess;
import org.siemac.metamac.sso.client.SsoClientConstants;
import org.siemac.metamac.sso.validation.ValidateTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.enume.domain.RoleEnum;
import es.gobcan.istac.indicators.web.server.ServiceContextHolder;
import es.gobcan.istac.indicators.web.shared.ValidateTicketAction;
import es.gobcan.istac.indicators.web.shared.ValidateTicketResult;

@Component
public class ValidateTicketActionHandler extends AbstractActionHandler<ValidateTicketAction, ValidateTicketResult> {

    protected static Logger      log                        = LoggerFactory.getLogger(ValidateTicketActionHandler.class);

    private static String        PROP_CAS_SERVER_URL_PREFIX = "indicators.security.casServerUrlPrefix";
    private static String        PROP_TOLERANCE             = "indicators.security.tolerance";

    @Autowired
    private ConfigurationService configurationService       = null;

    private ValidateTicket       validateTicket             = null;

    public ValidateTicketActionHandler() {
        super(ValidateTicketAction.class);
    }

    @PostConstruct
    public void initActionHandler() {
        String casServerUrlPrefix = configurationService.getConfig().getString(PROP_CAS_SERVER_URL_PREFIX);
        String tolerance = configurationService.getConfig().getString(PROP_TOLERANCE); // ms

        validateTicket = new ValidateTicket(casServerUrlPrefix);
        validateTicket.setTolerance(Long.valueOf(tolerance));
    }

    // TODO: THIS IS A MOCK FOR LDAP
    @Override
    public ValidateTicketResult execute(ValidateTicketAction action, ExecutionContext context) throws ActionException {
        MetamacPrincipal metamacPrincipal = new MetamacPrincipal();
        metamacPrincipal.setUserId("DUMMY");
        metamacPrincipal.getAccesses().add(new MetamacPrincipalAccess(RoleEnum.ADMINISTRADOR.name(), IndicatorsConstants.SECURITY_APPLICATION_ID, "DUMMY"));

        ServiceContext serviceContext = new ServiceContext(metamacPrincipal.getUserId(), UUID.randomUUID().toString(), IndicatorsConstants.SECURITY_APPLICATION_ID);
        serviceContext.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);
        ServiceContextHolder.putCurrentServiceContext(serviceContext);
        return new ValidateTicketResult(metamacPrincipal);
    }

    // @Override
    // public ValidateTicketResult execute(ValidateTicketAction action, ExecutionContext context) throws ActionException {
    // String ticket = action.getTicket();
    // String service = action.getServiceUrl();
    // try {
    // MetamacPrincipal metamacPrincipal = validateTicket.validateTicket(ticket, service);
    // ServiceContext serviceContext = new ServiceContext(metamacPrincipal.getUserId(), ticket, IndicatorsConstants.SECURITY_APPLICATION_ID);
    // serviceContext.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);
    // ServiceContextHolder.putCurrentServiceContext(serviceContext);
    // return new ValidateTicketResult(metamacPrincipal);
    // } catch (final org.siemac.metamac.sso.exception.TicketValidationException e) {
    // throw new ActionException(e);
    // }
    // }

    @Override
    public void undo(ValidateTicketAction arg0, ValidateTicketResult arg1, ExecutionContext arg2) throws ActionException {

    }

    protected final boolean parseBoolean(final String value) {
        return ((value != null) && value.equalsIgnoreCase("true"));
    }

}
