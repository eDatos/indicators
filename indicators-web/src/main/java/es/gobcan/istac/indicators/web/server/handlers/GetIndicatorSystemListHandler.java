package es.gobcan.istac.indicadores.web.server.handlers;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicadores.web.shared.GetIndicatorSystemListAction;
import es.gobcan.istac.indicadores.web.shared.GetIndicatorSystemListResult;
import es.gobcan.istac.indicadores.web.shared.db.IndDatabase;

public class GetIndicatorSystemListHandler extends AbstractActionHandler<GetIndicatorSystemListAction, GetIndicatorSystemListResult>{

	public GetIndicatorSystemListHandler() {
		super(GetIndicatorSystemListAction.class);
	}
	
	@Override
	public GetIndicatorSystemListResult execute(GetIndicatorSystemListAction action, ExecutionContext context) throws ActionException {
		return new GetIndicatorSystemListResult(IndDatabase.getIndicatorSystems());
	}

	@Override
	public void undo(GetIndicatorSystemListAction action, GetIndicatorSystemListResult result, ExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub

	}

}
