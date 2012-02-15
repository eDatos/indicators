package es.gobcan.istac.indicators.web.server.handlers;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.web.server.model.IndDatabase;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsSystemsAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsSystemsResult;

public class DeleteIndicatorsSystemsHandler extends AbstractActionHandler<DeleteIndicatorsSystemsAction, DeleteIndicatorsSystemsResult> {

	public DeleteIndicatorsSystemsHandler() {
		super(DeleteIndicatorsSystemsAction.class);
	}

	@Override
	public DeleteIndicatorsSystemsResult execute(DeleteIndicatorsSystemsAction action, ExecutionContext context) throws ActionException {
		for (String code : action.getCodes()) {
			IndDatabase.deleteIndicatorsSystem(code);
		}
		return new DeleteIndicatorsSystemsResult();
	}

	@Override
	public void undo(DeleteIndicatorsSystemsAction action, DeleteIndicatorsSystemsResult result, ExecutionContext context) throws ActionException {
		
	}

	
}
