package es.gobcan.istac.indicators.web.server.handlers;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.web.server.model.IndDatabase;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorAction;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorResult;

public class UpdateIndicatorHandler extends AbstractActionHandler<UpdateIndicatorAction, UpdateIndicatorResult>{
	
	public UpdateIndicatorHandler() {
		super(UpdateIndicatorAction.class);
	}

	@Override
	public UpdateIndicatorResult execute(UpdateIndicatorAction action, ExecutionContext context) throws ActionException {
		IndicatorDto ind = action.getIndicator();
		IndDatabase.saveIndicator(ind);
		return new UpdateIndicatorResult();
	}
	
	@Override
	public void undo(UpdateIndicatorAction action, UpdateIndicatorResult result, ExecutionContext context) throws ActionException {
	}

}
