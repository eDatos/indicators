package es.gobcan.istac.indicators.web.server.handlers;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.web.server.model.IndDatabase;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemListResult;

public class GetIndicatorsSystemListHandler extends AbstractActionHandler<GetIndicatorsSystemListAction, GetIndicatorsSystemListResult>{

	public GetIndicatorsSystemListHandler() {
		super(GetIndicatorsSystemListAction.class);
	}
	
	@Override
	public GetIndicatorsSystemListResult execute(GetIndicatorsSystemListAction action, ExecutionContext context) throws ActionException {
		return new GetIndicatorsSystemListResult(IndDatabase.getIndicatorsSystems());
	}

	@Override
	public void undo(GetIndicatorsSystemListAction action, GetIndicatorsSystemListResult result, ExecutionContext context) throws ActionException {
		// TODO Auto-generated method stub

	}

}
