package es.gobcan.istac.indicators.web.server.handlers;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.web.server.model.IndDatabase;
import es.gobcan.istac.indicators.web.shared.UpdateDimensionAction;
import es.gobcan.istac.indicators.web.shared.UpdateDimensionResult;

public class UpdateDimensionHandler  extends AbstractActionHandler<UpdateDimensionAction, UpdateDimensionResult>{

	public UpdateDimensionHandler() {
		super(UpdateDimensionAction.class);
	}

	@Override
	public UpdateDimensionResult execute(UpdateDimensionAction action, ExecutionContext context) throws ActionException {
		IndDatabase.saveDimension(action.getDimension());
		return new UpdateDimensionResult();
	}

	@Override
	public void undo(UpdateDimensionAction action, UpdateDimensionResult result, ExecutionContext context) throws ActionException {
		
	}
	

}
