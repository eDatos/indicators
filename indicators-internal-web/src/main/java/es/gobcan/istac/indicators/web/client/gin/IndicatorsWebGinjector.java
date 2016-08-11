package es.gobcan.istac.indicators.web.client.gin;

import org.siemac.metamac.web.common.client.gin.MetamacWebGinjector;

import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.inject.client.GinModules;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.client.gin.DispatchAsyncModule;

import es.gobcan.istac.indicators.web.client.LoggedInGatekeeper;
import es.gobcan.istac.indicators.web.client.admin.presenter.AdminGeoGranularitiesTabPresenter;
import es.gobcan.istac.indicators.web.client.admin.presenter.AdminGeoValuesTabPresenter;
import es.gobcan.istac.indicators.web.client.admin.presenter.AdminPresenter;
import es.gobcan.istac.indicators.web.client.admin.presenter.AdminQuantityUnitsTabPresenter;
import es.gobcan.istac.indicators.web.client.admin.presenter.AdminUnitMultipliersTabPresenter;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorListPresenter;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorPresenter;
import es.gobcan.istac.indicators.web.client.main.presenter.ErrorPagePresenter;
import es.gobcan.istac.indicators.web.client.main.presenter.MainPagePresenter;
import es.gobcan.istac.indicators.web.client.main.presenter.UnauthorizedPagePresenter;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemListPresenter;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemPresenter;

@GinModules({DispatchAsyncModule.class, ClientModule.class})
public interface IndicatorsWebGinjector extends MetamacWebGinjector {

    LoggedInGatekeeper getLoggedInGatekeeper();

    Provider<MainPagePresenter> getMainPagePresenter();

    AsyncProvider<AdminPresenter> getAdminPresenter();
    AsyncProvider<AdminQuantityUnitsTabPresenter> getAdminQuantityUnitsTabPresenter();
    AsyncProvider<AdminGeoGranularitiesTabPresenter> getAdminGeoGranularitiesTabPresenter();
    AsyncProvider<AdminUnitMultipliersTabPresenter> getAdminUnitMultipliersTabPresenter();
    AsyncProvider<AdminGeoValuesTabPresenter> getAdminGeoValuesTabPresenter();

    AsyncProvider<SystemListPresenter> getSystemListPresenter();
    AsyncProvider<SystemPresenter> getSystemPresenter();
    AsyncProvider<IndicatorListPresenter> getIndicatorListPresenter();
    AsyncProvider<IndicatorPresenter> getIndicatorPresenter();

    AsyncProvider<ErrorPagePresenter> getErrorPagePresenter();
    AsyncProvider<UnauthorizedPagePresenter> getUnauthorizedPagePresenter();
}
