package es.gobcan.istac.indicators.web.server.handlers;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.web.server.model.IndDatabase;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorInstanceResult;

public class CreateIndicatorInstanceHandler extends AbstractActionHandler<CreateIndicatorInstanceAction, CreateIndicatorInstanceResult>{

	public CreateIndicatorInstanceHandler() {
		super(CreateIndicatorInstanceAction.class);
	}

	@Override
	public CreateIndicatorInstanceResult execute(CreateIndicatorInstanceAction action, ExecutionContext context) throws ActionException {
		IndicatorsSystemDto system = action.getIndicatorsSystem();
		//TODO: llamar metodo wrapper que crea si no existe
		IndicatorInstanceDto createdInstance = IndDatabase.createIndicatorInstance(system.getUuid(), action.getIndicatorInstance());
		return new CreateIndicatorInstanceResult(createdInstance);
	}

	@Override
	public void undo(CreateIndicatorInstanceAction action,CreateIndicatorInstanceResult result, ExecutionContext context) throws ActionException {
	}

}
