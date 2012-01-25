package es.gobcan.istac.indicators.web.server;

import com.gwtplatform.dispatch.server.guice.HandlerModule;

import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorListHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorSystemHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorSystemListHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorSystemStructureHandler;
import es.gobcan.istac.indicators.web.server.handlers.SaveIndicatorHandler;
import es.gobcan.istac.indicators.web.server.handlers.SaveIndicatorSystemHandler;
import es.gobcan.istac.indicators.web.shared.GetIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorSystemAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorSystemListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorSystemStructureAction;
import es.gobcan.istac.indicators.web.shared.SaveIndicatorAction;
import es.gobcan.istac.indicators.web.shared.SaveIndicatorSystemAction;

public class ServerModule extends HandlerModule {

	@Override
	protected void configureHandlers() {
		bindHandler(SaveIndicatorSystemAction.class,SaveIndicatorSystemHandler.class);
		bindHandler(GetIndicatorSystemListAction.class,GetIndicatorSystemListHandler.class);
		bindHandler(GetIndicatorSystemAction.class,GetIndicatorSystemHandler.class);
		bindHandler(GetIndicatorSystemStructureAction.class,GetIndicatorSystemStructureHandler.class);
		
		bindHandler(SaveIndicatorAction.class,SaveIndicatorHandler.class);
		bindHandler(GetIndicatorListAction.class,GetIndicatorListHandler.class);
		bindHandler(GetIndicatorAction.class,GetIndicatorHandler.class);
	}
	
}
