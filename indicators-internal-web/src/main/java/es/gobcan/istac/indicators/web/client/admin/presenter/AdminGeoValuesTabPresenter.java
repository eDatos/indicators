package es.gobcan.istac.indicators.web.client.admin.presenter;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.navigation.shared.NameTokens;
import es.gobcan.istac.indicators.core.navigation.shared.PlaceRequestParams;
import es.gobcan.istac.indicators.web.client.LoggedInGatekeeper;
import es.gobcan.istac.indicators.web.client.admin.view.handlers.AdminGeoValuesUiHandlers;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesPaginatedListResult;
import es.gobcan.istac.indicators.web.shared.criteria.GeoValueCriteria;

public class AdminGeoValuesTabPresenter extends Presenter<AdminGeoValuesTabPresenter.AdminGeoValuesTabView, AdminGeoValuesTabPresenter.AdminGeoValuesTabProxy> implements AdminGeoValuesUiHandlers {

    private DispatchAsync dispatcher;
    private PlaceManager  placeManager;

    public interface AdminGeoValuesTabView extends View, HasUiHandlers<AdminGeoValuesUiHandlers> {

        void setGeoValues(int firstResult, List<GeographicalValueDto> dtos, int maxResults, String defaultGeoValue);
        void selectGeoValues(int indexDefaultGeoValue);

        // Search
        void clearSearchSection();
        GeoValueCriteria getGeoValueCriteria();
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.adminGeoValuesPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface AdminGeoValuesTabProxy extends Proxy<AdminGeoValuesTabPresenter>, Place {
    }

    @Inject
    public AdminGeoValuesTabPresenter(EventBus eventBus, AdminGeoValuesTabView view, AdminGeoValuesTabProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, view, proxy);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        getView().setUiHandlers(this);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, AdminPresenter.TYPE_SetContextAreaAdmin, this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);

        String geoValueParam = request.getParameter(PlaceRequestParams.adminGeoValueParam, null);

        if (StringUtils.isEmpty(geoValueParam)) {
            getView().clearSearchSection();
            reloadGeoValues(0);
        } else {
            GeoValueCriteria criteria = new GeoValueCriteria();
            criteria.setGeographicalValueCode(geoValueParam);
            retrieveGeoValues(criteria, geoValueParam);

        }

    }

    private void reloadGeoValues(int firstResult) {
        GeoValueCriteria criteria = getView().getGeoValueCriteria();
        criteria.setFirstResult(firstResult);
        retrieveGeoValues(criteria);
    }

    // ACTIONS

    public void retrieveGeoValues(GeoValueCriteria criteria, String defaultGeoValue) {
        retrieveGeoValuesWithAction(criteria, null, defaultGeoValue);

    }

    @Override
    public void retrieveGeoValues(GeoValueCriteria criteria) {
        retrieveGeoValuesWithAction(criteria, null, null);

    }

    private void retrieveGeoValuesWithAction(final GeoValueCriteria criteria, final Action action, final String defaultGeoValue) {
        dispatcher.execute(new GetGeographicalValuesPaginatedListAction(criteria), new WaitingAsyncCallbackHandlingError<GetGeographicalValuesPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetGeographicalValuesPaginatedListResult result) {
                getView().setGeoValues(criteria.getFirstResult(), result.getDtos(), result.getTotalResults(), defaultGeoValue);
                if (action != null) {
                    action.run();
                }
            }
        });
    }

    // Handler events
    @Override
    public void goToGeoValue(String code) {
        PlaceRequest geoValueDetailRequest = new PlaceRequest(NameTokens.adminGeoValuesPage).with(PlaceRequestParams.adminGeoValueParam, code);
        placeManager.revealPlace(geoValueDetailRequest);
    }

    private static interface Action {

        void run();
    }

    @Override
    public void goTo(List<PlaceRequest> location) {
        if (location != null && !location.isEmpty()) {
            placeManager.revealPlaceHierarchy(location);
        }
    }
}