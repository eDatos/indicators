package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.MetamacPrincipalAccess;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.core.enume.domain.RoleEnum;
import es.gobcan.istac.indicators.web.shared.GetUserPrincipalAction;
import es.gobcan.istac.indicators.web.shared.GetUserPrincipalResult;

@Component
public class GetUserPrincipalActionHandler extends AbstractActionHandler<GetUserPrincipalAction, GetUserPrincipalResult> {

    public GetUserPrincipalActionHandler() {
        super(GetUserPrincipalAction.class);
    }

    @Override
    public GetUserPrincipalResult execute(GetUserPrincipalAction action, ExecutionContext context) throws ActionException {
        MetamacPrincipal metamacPrincipal = new MetamacPrincipal();
        // metamacPrincipal.setUserId(serviceContext.getUserId());
        metamacPrincipal.getAccesses().add(new MetamacPrincipalAccess(RoleEnum.ADMINISTRADOR.getName(), IndicatorsConstants.SECURITY_APPLICATION_ID, null));
        return new GetUserPrincipalResult(metamacPrincipal);
    }

    @Override
    public void undo(GetUserPrincipalAction action, GetUserPrincipalResult result, ExecutionContext context) throws ActionException {

    }

}
