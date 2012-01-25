package es.gobcan.istac.indicadores.web.server.handlers;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicadores.web.shared.SaveIndicatorAction;
import es.gobcan.istac.indicadores.web.shared.SaveIndicatorResult;
import es.gobcan.istac.indicadores.web.shared.db.IndDatabase;
import es.gobcan.istac.indicadores.web.shared.db.Indicator;

public class SaveIndicatorHandler extends AbstractActionHandler<SaveIndicatorAction, SaveIndicatorResult>{
	
	public SaveIndicatorHandler() {
		super(SaveIndicatorAction.class);
	}

	@Override
	public SaveIndicatorResult execute(SaveIndicatorAction action, ExecutionContext context) throws ActionException {
		Indicator ind = action.getIndicator();
		long size = IndDatabase.getIndicators().size();
		ind.setId(size);
		IndDatabase.createIndicator(ind);
		return new SaveIndicatorResult();
	}
	
	@Override
	public void undo(SaveIndicatorAction action, SaveIndicatorResult result, ExecutionContext context) throws ActionException {
		
	}

}
