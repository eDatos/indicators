package es.gobcan.istac.indicadores.web.server.handlers;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicadores.web.shared.GetIndicatorSystemAction;
import es.gobcan.istac.indicadores.web.shared.GetIndicatorSystemResult;
import es.gobcan.istac.indicadores.web.shared.db.IndDatabase;
import es.gobcan.istac.indicadores.web.shared.db.IndicatorSystem;

public class GetIndicatorSystemHandler extends AbstractActionHandler<GetIndicatorSystemAction, GetIndicatorSystemResult>{

	public GetIndicatorSystemHandler() {
		super(GetIndicatorSystemAction.class);
	}

	@Override
	public GetIndicatorSystemResult execute(GetIndicatorSystemAction action, ExecutionContext context) throws ActionException {
		Long id = action.getIndSysId();
		IndicatorSystem indSys = IndDatabase.getIndicatorSystemById(id);
		return new GetIndicatorSystemResult(indSys);
	}

	@Override
	public void undo(GetIndicatorSystemAction action, GetIndicatorSystemResult result, ExecutionContext context) throws ActionException {
	}

}
