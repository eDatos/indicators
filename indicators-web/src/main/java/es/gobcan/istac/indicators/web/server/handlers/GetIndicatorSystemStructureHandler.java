package es.gobcan.istac.indicadores.web.server.handlers;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicadores.web.shared.GetIndicatorSystemStructureAction;
import es.gobcan.istac.indicadores.web.shared.GetIndicatorSystemStructureResult;
import es.gobcan.istac.indicadores.web.shared.db.IndDatabase;

public class GetIndicatorSystemStructureHandler extends AbstractActionHandler<GetIndicatorSystemStructureAction, GetIndicatorSystemStructureResult>{

	public GetIndicatorSystemStructureHandler() {
		super(GetIndicatorSystemStructureAction.class);
	}

	@Override
	public GetIndicatorSystemStructureResult execute(GetIndicatorSystemStructureAction action, ExecutionContext context) throws ActionException {
		return new GetIndicatorSystemStructureResult(IndDatabase.getIndicatorSystemStructure(action.getIndSysId()));
	}

	@Override
	public void undo(GetIndicatorSystemStructureAction action, GetIndicatorSystemStructureResult result, ExecutionContext context) throws ActionException {
		
	}
	

}
