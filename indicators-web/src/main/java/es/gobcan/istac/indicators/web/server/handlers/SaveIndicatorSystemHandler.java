package es.gobcan.istac.indicators.web.server.handlers;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.web.shared.SaveIndicatorSystemAction;
import es.gobcan.istac.indicators.web.shared.SaveIndicatorSystemResult;
import es.gobcan.istac.indicators.web.shared.db.IndDatabase;
import es.gobcan.istac.indicators.web.shared.db.IndicatorSystem;

public class SaveIndicatorSystemHandler extends AbstractActionHandler<SaveIndicatorSystemAction, SaveIndicatorSystemResult>{
	
	public SaveIndicatorSystemHandler() {
		super(SaveIndicatorSystemAction.class);
	}

	@Override
	public SaveIndicatorSystemResult execute(SaveIndicatorSystemAction action, ExecutionContext context) throws ActionException {
		IndicatorSystem indSys = action.getIndSystem();
		long size = IndDatabase.getIndicatorSystems().size();
		indSys.setId(size);
		IndDatabase.createIndicatorSystem(indSys);
		return new SaveIndicatorSystemResult();
	}
	
	@Override
	public void undo(SaveIndicatorSystemAction action, SaveIndicatorSystemResult result, ExecutionContext context) throws ActionException {
		
	}

}
