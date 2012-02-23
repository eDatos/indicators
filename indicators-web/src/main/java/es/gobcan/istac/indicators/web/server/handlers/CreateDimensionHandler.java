package es.gobcan.istac.indicators.web.server.handlers;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.web.server.model.IndDatabase;
import es.gobcan.istac.indicators.web.shared.CreateDimensionAction;
import es.gobcan.istac.indicators.web.shared.CreateDimensionResult;

public class CreateDimensionHandler extends AbstractActionHandler<CreateDimensionAction, CreateDimensionResult>{

	public CreateDimensionHandler() {
		super(CreateDimensionAction.class);
	}

	@Override
	public CreateDimensionResult execute(CreateDimensionAction action, ExecutionContext context) throws ActionException {
		IndicatorsSystemDto system = action.getIndicatorsSystem();
		//TODO: llamar metodo wrapper que crea si no existe
		DimensionDto createdDim = IndDatabase.createDimension(system.getUuid(), action.getDimension());
		return new CreateDimensionResult(createdDim);
	}

	@Override
	public void undo(CreateDimensionAction action, CreateDimensionResult result, ExecutionContext context) throws ActionException {
		
	}

}
