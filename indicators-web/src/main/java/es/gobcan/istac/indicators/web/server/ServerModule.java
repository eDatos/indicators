package es.gobcan.istac.indicators.web.server;

import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.spring.HandlerModule;

import es.gobcan.istac.indicators.web.server.handlers.CreateDimensionHandler;
import es.gobcan.istac.indicators.web.server.handlers.CreateIndicatorHandler;
import es.gobcan.istac.indicators.web.server.handlers.CreateIndicatorInstanceHandler;
import es.gobcan.istac.indicators.web.server.handlers.DeleteDimensionHandler;
import es.gobcan.istac.indicators.web.server.handlers.DeleteIndicatorInstanceHandler;
import es.gobcan.istac.indicators.web.server.handlers.DeleteIndicatorsHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorListHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorsSystemHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorsSystemListHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorsSystemStructureHandler;
import es.gobcan.istac.indicators.web.server.handlers.MoveSystemStructureContentHandler;
import es.gobcan.istac.indicators.web.server.handlers.UpdateDimensionHandler;
import es.gobcan.istac.indicators.web.server.handlers.UpdateIndicatorHandler;
import es.gobcan.istac.indicators.web.server.handlers.UpdateIndicatorInstanceHandler;
import es.gobcan.istac.indicators.web.server.handlers.UpdateIndicatorsSystemHandler;
import es.gobcan.istac.indicators.web.shared.CreateDimensionAction;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorAction;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.DeleteDimensionAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemStructureAction;
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
		bindHandler(UpdateIndicatorsSystemAction.class, UpdateIndicatorsSystemHandler.class);
		bindHandler(GetIndicatorsSystemListAction.class, GetIndicatorsSystemListHandler.class);
		bindHandler(GetIndicatorsSystemAction.class, GetIndicatorsSystemHandler.class);
		bindHandler(GetIndicatorsSystemStructureAction.class, GetIndicatorsSystemStructureHandler.class);
		
		/* System structure related actions */
		bindHandler(CreateDimensionAction.class, CreateDimensionHandler.class);
		bindHandler(UpdateDimensionAction.class, UpdateDimensionHandler.class);
		bindHandler(DeleteDimensionAction.class, DeleteDimensionHandler.class);
		bindHandler(CreateIndicatorInstanceAction.class, CreateIndicatorInstanceHandler.class);
		bindHandler(UpdateIndicatorInstanceAction.class, UpdateIndicatorInstanceHandler.class);
		bindHandler(DeleteIndicatorInstanceAction.class, DeleteIndicatorInstanceHandler.class);
		bindHandler(MoveSystemStructureContentAction.class, MoveSystemStructureContentHandler.class);
		
		/* Indicators actions */
		bindHandler(CreateIndicatorAction.class, CreateIndicatorHandler.class);
		bindHandler(UpdateIndicatorAction.class, UpdateIndicatorHandler.class);
		bindHandler(GetIndicatorListAction.class, GetIndicatorListHandler.class);
		bindHandler(GetIndicatorAction.class, GetIndicatorHandler.class);
		bindHandler(DeleteIndicatorsAction.class, DeleteIndicatorsHandler.class);
	}
	
}
