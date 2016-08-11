package es.gobcan.istac.indicators.web.client.gin;

import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;

import es.gobcan.istac.indicators.core.navigation.shared.NameTokens;
import es.gobcan.istac.indicators.web.client.IndicatorsPlaceManager;
import es.gobcan.istac.indicators.web.client.LoggedInGatekeeper;
import es.gobcan.istac.indicators.web.client.admin.presenter.AdminGeoGranularitiesTabPresenter;
import es.gobcan.istac.indicators.web.client.admin.presenter.AdminGeoValuesTabPresenter;
import es.gobcan.istac.indicators.web.client.admin.presenter.AdminPresenter;
import es.gobcan.istac.indicators.web.client.admin.presenter.AdminQuantityUnitsTabPresenter;
import es.gobcan.istac.indicators.web.client.admin.presenter.AdminUnitMultipliersTabPresenter;
import es.gobcan.istac.indicators.web.client.admin.view.AdminGeoGranularitiesTabViewImpl;
import es.gobcan.istac.indicators.web.client.admin.view.AdminGeoValuesTabViewImpl;
import es.gobcan.istac.indicators.web.client.admin.view.AdminQuantityUnitsTabViewImpl;
import es.gobcan.istac.indicators.web.client.admin.view.AdminUnitMultipliersTabViewImpl;
import es.gobcan.istac.indicators.web.client.admin.view.AdminViewImpl;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorListPresenter;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorPresenter;
import es.gobcan.istac.indicators.web.client.indicator.view.IndicatorListViewImpl;
import es.gobcan.istac.indicators.web.client.indicator.view.IndicatorViewImpl;
import es.gobcan.istac.indicators.web.client.main.presenter.ErrorPagePresenter;
import es.gobcan.istac.indicators.web.client.main.presenter.MainPagePresenter;
import es.gobcan.istac.indicators.web.client.main.presenter.ToolStripPresenterWidget;
import es.gobcan.istac.indicators.web.client.main.presenter.UnauthorizedPagePresenter;
import es.gobcan.istac.indicators.web.client.main.view.ErrorPageViewImpl;
import es.gobcan.istac.indicators.web.client.main.view.MainPageViewImpl;
import es.gobcan.istac.indicators.web.client.main.view.ToolStripViewImpl;
import es.gobcan.istac.indicators.web.client.main.view.UnauthorizedPageViewImpl;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemListPresenter;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemPresenter;
import es.gobcan.istac.indicators.web.client.system.view.SystemListViewImpl;
import es.gobcan.istac.indicators.web.client.system.view.SystemViewImpl;

public class ClientModule extends AbstractPresenterModule {

    @Override
    protected void configure() {
        // Map EventBus, TokenFormatter, RootPresenter, PlaceManager y GoogleAnalytics
        install(new DefaultModule(IndicatorsPlaceManager.class));

        bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.indicatorListPage);

        // Main presenters
        bindPresenter(MainPagePresenter.class, MainPagePresenter.MainView.class, MainPageViewImpl.class, MainPagePresenter.MainProxy.class);

        bindPresenter(AdminPresenter.class, AdminPresenter.AdminView.class, AdminViewImpl.class, AdminPresenter.AdminProxy.class);
        bindPresenter(AdminQuantityUnitsTabPresenter.class, AdminQuantityUnitsTabPresenter.AdminQuantityUnitsTabView.class, AdminQuantityUnitsTabViewImpl.class,
                AdminQuantityUnitsTabPresenter.AdminQuantityUnitsTabProxy.class);
        bindPresenter(AdminGeoGranularitiesTabPresenter.class, AdminGeoGranularitiesTabPresenter.AdminGeoGranularitiesTabView.class, AdminGeoGranularitiesTabViewImpl.class,
                AdminGeoGranularitiesTabPresenter.AdminGeoGranularitiesTabProxy.class);
        bindPresenter(AdminUnitMultipliersTabPresenter.class, AdminUnitMultipliersTabPresenter.AdminUnitMultipliersTabView.class, AdminUnitMultipliersTabViewImpl.class,
                AdminUnitMultipliersTabPresenter.AdminUnitMultipliersTabProxy.class);
        bindPresenter(AdminGeoValuesTabPresenter.class, AdminGeoValuesTabPresenter.AdminGeoValuesTabView.class, AdminGeoValuesTabViewImpl.class,
                AdminGeoValuesTabPresenter.AdminGeoValuesTabProxy.class);

        bindPresenter(SystemListPresenter.class, SystemListPresenter.SystemListView.class, SystemListViewImpl.class, SystemListPresenter.SystemListProxy.class);
        bindPresenter(SystemPresenter.class, SystemPresenter.SystemView.class, SystemViewImpl.class, SystemPresenter.SystemProxy.class);
        bindPresenter(IndicatorListPresenter.class, IndicatorListPresenter.IndicatorListView.class, IndicatorListViewImpl.class, IndicatorListPresenter.IndicatorListProxy.class);
        bindPresenter(IndicatorPresenter.class, IndicatorPresenter.IndicatorView.class, IndicatorViewImpl.class, IndicatorPresenter.IndicatorProxy.class);

        // PresenterWidgets
        bindSingletonPresenterWidget(ToolStripPresenterWidget.class, ToolStripPresenterWidget.ToolStripView.class, ToolStripViewImpl.class);

        // Gate keeper
        bind(LoggedInGatekeeper.class).in(Singleton.class);

        // Error pages
        bindPresenter(ErrorPagePresenter.class, ErrorPagePresenter.ErrorPageView.class, ErrorPageViewImpl.class, ErrorPagePresenter.ErrorPageProxy.class);
        bindPresenter(UnauthorizedPagePresenter.class, UnauthorizedPagePresenter.UnauthorizedPageView.class, UnauthorizedPageViewImpl.class, UnauthorizedPagePresenter.UnauthorizedPageProxy.class);
    }

}
