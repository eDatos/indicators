package es.gobcan.istac.indicators.web.client.gin;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.client.gin.DispatchAsyncModule;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorListPresenter;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorPresenter;
import es.gobcan.istac.indicators.web.client.main.presenter.MainPagePresenter;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemListPresenter;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemPresenter;

@GinModules({DispatchAsyncModule.class, ClientModule.class})
public interface IndicatorsWebGinjector extends Ginjector {

    PlaceManager getPlaceManager();
    EventBus getEventBus();
    DispatchAsync getDispatcher();

    Provider<MainPagePresenter> getMainPagePresenter();
    AsyncProvider<SystemListPresenter> getSystemListPresenter();
    AsyncProvider<SystemPresenter> getSystemPresenter();
    AsyncProvider<IndicatorListPresenter> getIndicatorListPresenter();
    AsyncProvider<IndicatorPresenter> getIndicatorPresenter();
}
