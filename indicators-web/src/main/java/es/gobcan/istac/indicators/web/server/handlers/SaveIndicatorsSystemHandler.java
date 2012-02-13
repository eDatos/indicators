package es.gobcan.istac.indicators.web.server.handlers;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.web.server.model.IndDatabase;
import es.gobcan.istac.indicators.web.shared.SaveIndicatorsSystemAction;
import es.gobcan.istac.indicators.web.shared.SaveIndicatorsSystemResult;

public class SaveIndicatorsSystemHandler extends AbstractActionHandler<SaveIndicatorsSystemAction, SaveIndicatorsSystemResult>{
	
	public SaveIndicatorsSystemHandler() {
		super(SaveIndicatorsSystemAction.class);
	}

	@Override
	public SaveIndicatorsSystemResult execute(SaveIndicatorsSystemAction action, ExecutionContext context) throws ActionException {
		IndicatorsSystemDto indSys = action.getIndicatorsSystem();
		long size = IndDatabase.getIndicatorsSystems().size();
		indSys.setId(size);
		IndDatabase.createIndicatorsSystem(indSys);
		return new SaveIndicatorsSystemResult();
	}
	
	@Override
	public void undo(SaveIndicatorsSystemAction action, SaveIndicatorsSystemResult result, ExecutionContext context) throws ActionException {
		
	}

}
