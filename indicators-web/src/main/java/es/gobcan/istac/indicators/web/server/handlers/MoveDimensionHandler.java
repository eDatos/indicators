package es.gobcan.istac.indicators.web.server.handlers;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.web.server.model.IndDatabase;
import es.gobcan.istac.indicators.web.shared.MoveDimensionAction;
import es.gobcan.istac.indicators.web.shared.MoveDimensionResult;

public class MoveDimensionHandler extends AbstractActionHandler<MoveDimensionAction, MoveDimensionResult>{

	public MoveDimensionHandler() {
		super(MoveDimensionAction.class);
	}

	@Override
	public MoveDimensionResult execute(MoveDimensionAction action, ExecutionContext context) throws ActionException {
		IndDatabase.moveDimension(action.getUuid(), action.getSystemUuid(), action.getNewParentUuid(), action.getNewOrderInLevel());
		return new MoveDimensionResult();
	}

	@Override
	public void undo(MoveDimensionAction action, MoveDimensionResult result, ExecutionContext context) throws ActionException {
		
	}

}
