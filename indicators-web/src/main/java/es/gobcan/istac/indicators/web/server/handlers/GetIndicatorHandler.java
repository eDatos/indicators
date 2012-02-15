package es.gobcan.istac.indicators.web.server.handlers;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.web.server.model.IndDatabase;
import es.gobcan.istac.indicators.web.shared.GetIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorResult;

public class GetIndicatorHandler extends AbstractActionHandler<GetIndicatorAction, GetIndicatorResult> {

	public GetIndicatorHandler() {
		super(GetIndicatorAction.class);
	}

	@Override
	public GetIndicatorResult execute(GetIndicatorAction action, ExecutionContext context) throws ActionException {
		String code = action.getCode();
		return new GetIndicatorResult(IndDatabase.getIndicatorByCode(code));
	}
	
	@Override
	public void undo(GetIndicatorAction action, GetIndicatorResult result, ExecutionContext context) throws ActionException {
		
	}
	
}
