package es.gobcan.istac.indicadores.web.server.handlers;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicadores.web.shared.GetIndicatorListAction;
import es.gobcan.istac.indicadores.web.shared.GetIndicatorListResult;
import es.gobcan.istac.indicadores.web.shared.db.IndDatabase;

public class GetIndicatorListHandler extends AbstractActionHandler<GetIndicatorListAction, GetIndicatorListResult>{

	public GetIndicatorListHandler() {
		super(GetIndicatorListAction.class);
	}
	
	@Override
	public GetIndicatorListResult execute(GetIndicatorListAction action, ExecutionContext context) throws ActionException {
		return new GetIndicatorListResult(IndDatabase.getIndicators());
	}

	@Override
	public void undo(GetIndicatorListAction action, GetIndicatorListResult result, ExecutionContext context) throws ActionException {
		
	}

}
