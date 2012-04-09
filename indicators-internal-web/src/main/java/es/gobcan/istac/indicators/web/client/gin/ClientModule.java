package es.gobcan.istac.indicators.web.client.gin;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;

import es.gobcan.istac.indicators.web.client.IndicatorsPlaceManager;
import es.gobcan.istac.indicators.web.client.NameTokens;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorListPresenter;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorPresenter;
import es.gobcan.istac.indicators.web.client.indicator.view.IndicatorListViewImpl;
import es.gobcan.istac.indicators.web.client.indicator.view.IndicatorViewImpl;
import es.gobcan.istac.indicators.web.client.main.presenter.MainPagePresenter;
import es.gobcan.istac.indicators.web.client.main.view.MainPageViewImpl;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemListPresenter;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemPresenter;
import es.gobcan.istac.indicators.web.client.system.view.SystemListViewImpl;
import es.gobcan.istac.indicators.web.client.system.view.SystemViewImpl;

public class ClientModule extends AbstractPresenterModule {

    @Override
    protected void configure() {
        /* Mapeamos eventbus, tokenformatter, rootpresenter, placemanager y googleanalytics */
        install(new DefaultModule(IndicatorsPlaceManager.class));

        bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.systemListPage);

        // Main presenter
        bindPresenter(MainPagePresenter.class, MainPagePresenter.MainView.class, MainPageViewImpl.class, MainPagePresenter.MainProxy.class);

        bindPresenter(SystemListPresenter.class, SystemListPresenter.SystemListView.class, SystemListViewImpl.class, SystemListPresenter.SystemListProxy.class);

        bindPresenter(SystemPresenter.class, SystemPresenter.SystemView.class, SystemViewImpl.class, SystemPresenter.SystemProxy.class);

        bindPresenter(IndicatorListPresenter.class, IndicatorListPresenter.IndicatorListView.class, IndicatorListViewImpl.class, IndicatorListPresenter.IndicatorListProxy.class);

        bindPresenter(IndicatorPresenter.class, IndicatorPresenter.IndicatorView.class, IndicatorViewImpl.class, IndicatorPresenter.IndicatorProxy.class);

    }

}
