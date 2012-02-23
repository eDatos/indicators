package es.gobcan.istac.indicators.web.server.handlers;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.web.server.model.IndDatabase;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorInstanceResult;

public class UpdateIndicatorInstanceHandler
		extends
		AbstractActionHandler<UpdateIndicatorInstanceAction, UpdateIndicatorInstanceResult> {

	public UpdateIndicatorInstanceHandler() {
		super(UpdateIndicatorInstanceAction.class);
	}

	@Override
	public UpdateIndicatorInstanceResult execute(UpdateIndicatorInstanceAction action, ExecutionContext context) throws ActionException {
		IndDatabase.saveIndicatorInstance(action.getIndicatorInstance());
		return new UpdateIndicatorInstanceResult();
	}

	@Override
	public void undo(UpdateIndicatorInstanceAction action, UpdateIndicatorInstanceResult result, ExecutionContext context) throws ActionException {

	}

}
