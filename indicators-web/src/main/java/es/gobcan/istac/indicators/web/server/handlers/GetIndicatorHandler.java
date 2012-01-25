package es.gobcan.istac.indicadores.web.server.handlers;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicadores.web.shared.GetIndicatorAction;
import es.gobcan.istac.indicadores.web.shared.GetIndicatorResult;
import es.gobcan.istac.indicadores.web.shared.db.IndDatabase;

public class GetIndicatorHandler extends AbstractActionHandler<GetIndicatorAction, GetIndicatorResult> {

	public GetIndicatorHandler() {
		super(GetIndicatorAction.class);
	}

	@Override
	public GetIndicatorResult execute(GetIndicatorAction action, ExecutionContext context) throws ActionException {
		Long id = action.getIndicatorId();
		return new GetIndicatorResult(IndDatabase.getIndicatorById(id));
	}
	
	@Override
	public void undo(GetIndicatorAction action, GetIndicatorResult result, ExecutionContext context) throws ActionException {
		
	}
	
}
