package es.gobcan.istac.indicators.web.server.handlers;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.web.server.model.IndDatabase;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorInstanceResult;

public class DeleteIndicatorInstanceHandler extends AbstractActionHandler<DeleteIndicatorInstanceAction, DeleteIndicatorInstanceResult>{
	
	public DeleteIndicatorInstanceHandler() {
		super(DeleteIndicatorInstanceAction.class);
	}
	
	@Override
	public DeleteIndicatorInstanceResult execute(DeleteIndicatorInstanceAction action, ExecutionContext context) throws ActionException {
		IndDatabase.deleteIndicatorInstance(action.getIndicatorInstanceUuid());
		return new DeleteIndicatorInstanceResult();
	}
	
	@Override
	public void undo(DeleteIndicatorInstanceAction action, DeleteIndicatorInstanceResult result, ExecutionContext context) throws ActionException {
		
	}

}
