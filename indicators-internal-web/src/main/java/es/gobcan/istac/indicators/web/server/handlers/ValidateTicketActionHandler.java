package es.gobcan.istac.indicators.web.server.handlers;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequestWrapper;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.SsoClientConstants;
import org.siemac.metamac.sso.validation.ValidateTicket;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.session.SingleSignOutFilter;
import org.siemac.metamac.web.common.shared.ValidateTicketAction;
import org.siemac.metamac.web.common.shared.ValidateTicketResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.conf.IndicatorsConfigurationService;
import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;

@Component
public class ValidateTicketActionHandler extends AbstractActionHandler<ValidateTicketAction, ValidateTicketResult> {

    protected static Logger                log                  = LoggerFactory.getLogger(ValidateTicketActionHandler.class);

    protected static final String          TICKET_PARAMETER     = "ticket";
    protected static final String          TICKET_QUERY_STRING  = "&ticket=";

    @Autowired
    private IndicatorsConfigurationService configurationService = null;

    protected ValidateTicket               validateTicket       = null;

    protected HttpServletRequestWrapper    requestWrapper       = null;

    public ValidateTicketActionHandler() {
        super(ValidateTicketAction.class);
    }

    @PostConstruct
    public void initActionHandler() throws MetamacException {
        String casServerUrlPrefix = configurationService.retrieveSecurityCasServerUrlPrefix();
        String tolerance = configurationService.retrieveSecurityTolerance();

        validateTicket = new ValidateTicket(casServerUrlPrefix);
        validateTicket.setTolerance(Long.valueOf(tolerance));
    }

    @Override
    public ValidateTicketResult execute(ValidateTicketAction action, ExecutionContext context) throws ActionException {

        final String ticket = action.getTicket();
        final String service = action.getServiceUrl();

        try {
            MetamacPrincipal metamacPrincipal = validateTicket.validateTicket(ticket, service);
            ServiceContext serviceContext = new ServiceContext(metamacPrincipal.getUserId(), ticket, IndicatorsConstants.SECURITY_APPLICATION_ID);
            serviceContext.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);
            ServiceContextHolder.putCurrentServiceContext(serviceContext);

            HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(ServiceContextHolder.getCurrentRequest()) {

                @Override
                public String getParameter(String name) {
                    if (TICKET_PARAMETER.equals(name)) {
                        return ticket;
                    }
                    return super.getParameter(name);
                }
                @Override
                public String getQueryString() {
                    return super.getQueryString() + TICKET_QUERY_STRING + ticket;
                }
            };
            SingleSignOutFilter.getSingleSignOutHandler().recordSession(requestWrapper);

            return new ValidateTicketResult(metamacPrincipal);
        } catch (final org.siemac.metamac.sso.exception.TicketValidationException e) {
            throw new ActionException(e);
        }
    }

    @Override
    public void undo(ValidateTicketAction arg0, ValidateTicketResult arg1, ExecutionContext arg2) throws ActionException {

    }

    protected final boolean parseBoolean(final String value) {
        return ((value != null) && value.equalsIgnoreCase("true"));
    }

}
