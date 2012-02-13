package es.gobcan.istac.indicators.web.server.handlers;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.web.server.model.IndDatabase;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemResult;

public class GetIndicatorsSystemHandler extends AbstractActionHandler<GetIndicatorsSystemAction, GetIndicatorsSystemResult>{

	public GetIndicatorsSystemHandler() {
		super(GetIndicatorsSystemAction.class);
	}

	@Override
	public GetIndicatorsSystemResult execute(GetIndicatorsSystemAction action, ExecutionContext context) throws ActionException {
		Long id = action.getIndSysId();
		IndicatorsSystemDto indSys = IndDatabase.getIndicatorsSystemById(id);
		return new GetIndicatorsSystemResult(indSys);
	}

	@Override
	public void undo(GetIndicatorsSystemAction action, GetIndicatorsSystemResult result, ExecutionContext context) throws ActionException {
	}

}
