package es.gobcan.istac.indicators.web.server;

import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.spring.HandlerModule;

import es.gobcan.istac.indicators.web.server.handlers.CreateDimensionActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.CreateIndicatorActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.CreateIndicatorInstanceActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.DeleteDimensionActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.DeleteIndicatorInstanceActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.DeleteIndicatorsActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetGeographicalGranularitiesActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetGeographicalValuesActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorByCodeActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorListActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorsSystemByCodeActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorsSystemListActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorsSystemStructureActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetQuantityUnitsListActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.MoveSystemStructureContentActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.UpdateDimensionActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.UpdateIndicatorActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.UpdateIndicatorInstanceActionHandler;
import es.gobcan.istac.indicators.web.server.handlers.UpdateIndicatorsSystemActionHandler;
import es.gobcan.istac.indicators.web.shared.CreateDimensionAction;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorAction;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.DeleteDimensionAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularitiesAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorByCodeAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemStructureAction;
import es.gobcan.istac.indicators.web.shared.GetQuantityUnitsListAction;
import es.gobcan.istac.indicators.web.shared.MoveSystemStructureContentAction;
import es.gobcan.istac.indicators.web.shared.UpdateDimensionAction;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorAction;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorsSystemAction;

@Component
public class ServerModule extends HandlerModule {

    public ServerModule() {
    }
    
	@Override
	protected void configureHandlers() {
		/* System actions */
		bindHandler(UpdateIndicatorsSystemAction.class, UpdateIndicatorsSystemActionHandler.class);
		bindHandler(GetIndicatorsSystemListAction.class, GetIndicatorsSystemListActionHandler.class);
		bindHandler(GetIndicatorsSystemAction.class, GetIndicatorsSystemByCodeActionHandler.class);
		bindHandler(GetIndicatorsSystemStructureAction.class, GetIndicatorsSystemStructureActionHandler.class);
		
		/* System structure related actions */
		bindHandler(CreateDimensionAction.class, CreateDimensionActionHandler.class);
		bindHandler(UpdateDimensionAction.class, UpdateDimensionActionHandler.class);
		bindHandler(DeleteDimensionAction.class, DeleteDimensionActionHandler.class);
		bindHandler(CreateIndicatorInstanceAction.class, CreateIndicatorInstanceActionHandler.class);
		bindHandler(UpdateIndicatorInstanceAction.class, UpdateIndicatorInstanceActionHandler.class);
		bindHandler(DeleteIndicatorInstanceAction.class, DeleteIndicatorInstanceActionHandler.class);
		bindHandler(MoveSystemStructureContentAction.class, MoveSystemStructureContentActionHandler.class);
		
		/* Indicators actions */
		bindHandler(CreateIndicatorAction.class, CreateIndicatorActionHandler.class);
		bindHandler(UpdateIndicatorAction.class, UpdateIndicatorActionHandler.class);
		bindHandler(GetIndicatorListAction.class, GetIndicatorListActionHandler.class);
		bindHandler(GetIndicatorByCodeAction.class, GetIndicatorByCodeActionHandler.class);
		bindHandler(GetIndicatorAction.class, GetIndicatorActionHandler.class);
		bindHandler(DeleteIndicatorsAction.class, DeleteIndicatorsActionHandler.class);
		bindHandler(GetQuantityUnitsListAction.class, GetQuantityUnitsListActionHandler.class);
		bindHandler(GetGeographicalGranularitiesAction.class, GetGeographicalGranularitiesActionHandler.class);
		bindHandler(GetGeographicalValuesAction.class, GetGeographicalValuesActionHandler.class);
	}
	
}
