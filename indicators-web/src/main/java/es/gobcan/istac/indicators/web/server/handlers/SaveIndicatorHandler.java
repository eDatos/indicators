package es.gobcan.istac.indicators.web.server.handlers;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.web.server.model.IndDatabase;
import es.gobcan.istac.indicators.web.shared.SaveIndicatorAction;
import es.gobcan.istac.indicators.web.shared.SaveIndicatorResult;

public class SaveIndicatorHandler extends AbstractActionHandler<SaveIndicatorAction, SaveIndicatorResult>{
	
	public SaveIndicatorHandler() {
		super(SaveIndicatorAction.class);
	}

	@Override
	public SaveIndicatorResult execute(SaveIndicatorAction action, ExecutionContext context) throws ActionException {
		IndicatorDto ind = action.getIndicator();
		IndDatabase.saveIndicator(ind);
		return new SaveIndicatorResult();
	}
	
	@Override
	public void undo(SaveIndicatorAction action, SaveIndicatorResult result, ExecutionContext context) throws ActionException {
	}

}
