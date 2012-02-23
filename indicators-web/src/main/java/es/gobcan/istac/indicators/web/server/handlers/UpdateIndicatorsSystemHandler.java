package es.gobcan.istac.indicators.web.server.handlers;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.web.server.model.IndDatabase;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorsSystemAction;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorsSystemResult;

public class UpdateIndicatorsSystemHandler extends AbstractActionHandler<UpdateIndicatorsSystemAction, UpdateIndicatorsSystemResult>{
	
	public UpdateIndicatorsSystemHandler() {
		super(UpdateIndicatorsSystemAction.class);
	}

	@Override
	public UpdateIndicatorsSystemResult execute(UpdateIndicatorsSystemAction action, ExecutionContext context) throws ActionException {
		IndicatorsSystemDto indSys = action.getIndicatorsSystem();
		IndDatabase.saveIndicatorsSystem(indSys);
		return new UpdateIndicatorsSystemResult();
	}
	
	@Override
	public void undo(UpdateIndicatorsSystemAction action, UpdateIndicatorsSystemResult result, ExecutionContext context) throws ActionException {
		
	}

}
