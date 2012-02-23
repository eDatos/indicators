package es.gobcan.istac.indicators.web.server.handlers;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.web.server.model.IndDatabase;
import es.gobcan.istac.indicators.web.shared.DeleteDimensionAction;
import es.gobcan.istac.indicators.web.shared.DeleteDimensionResult;

public class DeleteDimensionHandler extends AbstractActionHandler<DeleteDimensionAction, DeleteDimensionResult> {
	
	public DeleteDimensionHandler() {
		super(DeleteDimensionAction.class);
	}
	
	@Override
	public DeleteDimensionResult execute(DeleteDimensionAction action, ExecutionContext context) throws ActionException {
		IndDatabase.deleteDimension(action.getDimensionUuid());
		return new DeleteDimensionResult();
	}
	
	@Override
	public void undo(DeleteDimensionAction action, DeleteDimensionResult result, ExecutionContext context) throws ActionException {
		
	}

}
