package es.gobcan.istac.indicators.web.server;

import com.gwtplatform.dispatch.server.guice.HandlerModule;

import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorListHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorsSystemHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorsSystemListHandler;
import es.gobcan.istac.indicators.web.server.handlers.GetIndicatorsSystemStructureHandler;
import es.gobcan.istac.indicators.web.server.handlers.SaveIndicatorHandler;
import es.gobcan.istac.indicators.web.server.handlers.SaveIndicatorsSystemHandler;
import es.gobcan.istac.indicators.web.shared.GetIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemStructureAction;
import es.gobcan.istac.indicators.web.shared.SaveIndicatorAction;
import es.gobcan.istac.indicators.web.shared.SaveIndicatorsSystemAction;

public class ServerModule extends HandlerModule {

	@Override
	protected void configureHandlers() {
		bindHandler(SaveIndicatorsSystemAction.class,SaveIndicatorsSystemHandler.class);
		bindHandler(GetIndicatorsSystemListAction.class,GetIndicatorsSystemListHandler.class);
		bindHandler(GetIndicatorsSystemAction.class,GetIndicatorsSystemHandler.class);
		bindHandler(GetIndicatorsSystemStructureAction.class,GetIndicatorsSystemStructureHandler.class);
		
		bindHandler(SaveIndicatorAction.class,SaveIndicatorHandler.class);
		bindHandler(GetIndicatorListAction.class,GetIndicatorListHandler.class);
		bindHandler(GetIndicatorAction.class,GetIndicatorHandler.class);
	}
	
}
