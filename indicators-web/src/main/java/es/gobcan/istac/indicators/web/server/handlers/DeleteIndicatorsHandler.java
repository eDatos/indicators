package es.gobcan.istac.indicators.web.server.handlers;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.web.server.model.IndDatabase;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsResult;

public class DeleteIndicatorsHandler extends AbstractActionHandler<DeleteIndicatorsAction, DeleteIndicatorsResult> {

	public DeleteIndicatorsHandler() {
		super(DeleteIndicatorsAction.class);
	}

	@Override
	public DeleteIndicatorsResult execute(DeleteIndicatorsAction action, ExecutionContext context) throws ActionException {
		for (String code : action.getCodes()) {
			IndDatabase.deleteIndicator(code);
		}
		return new DeleteIndicatorsResult();
	}

	@Override
	public void undo(DeleteIndicatorsAction action, DeleteIndicatorsResult result, ExecutionContext context) throws ActionException {
	}
	

}
