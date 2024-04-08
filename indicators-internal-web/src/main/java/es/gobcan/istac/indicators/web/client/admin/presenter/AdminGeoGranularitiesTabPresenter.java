package es.gobcan.istac.indicators.web.client.admin.presenter;

import java.util.List;

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
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.navigation.shared.NameTokens;
import es.gobcan.istac.indicators.web.client.LoggedInGatekeeper;
import es.gobcan.istac.indicators.web.client.admin.view.handlers.AdminGeoGranularitiesUiHandlers;
import es.gobcan.istac.indicators.web.client.utils.IndicatorsWebConstants;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularitiesPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularitiesPaginatedListResult;

public class AdminGeoGranularitiesTabPresenter extends Presenter<AdminGeoGranularitiesTabPresenter.AdminGeoGranularitiesTabView, AdminGeoGranularitiesTabPresenter.AdminGeoGranularitiesTabProxy>
        implements
            AdminGeoGranularitiesUiHandlers {

    private DispatchAsync dispatcher;

    public interface AdminGeoGranularitiesTabView extends View, HasUiHandlers<AdminGeoGranularitiesUiHandlers> {

        void setGeoGranularities(int firstResult, List<GeographicalGranularityDto> geographicalGranularityDtos, int maxResults);
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.adminGeoGranularitiesPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface AdminGeoGranularitiesTabProxy extends Proxy<AdminGeoGranularitiesTabPresenter>, Place {
    }

    @Inject
    public AdminGeoGranularitiesTabPresenter(EventBus eventBus, AdminGeoGranularitiesTabView view, AdminGeoGranularitiesTabProxy proxy, DispatchAsync dispatcher) {
        super(eventBus, view, proxy);
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

        reloadGeoGranularities(0);
    }

    // ACTIONS

    @Override
    public void retrieveGeoGranularities(final int firstResult) {
        retrieveGeoGranularitiesWithAction(firstResult, null);
    }

    private void retrieveGeoGranularitiesWithAction(final int firstResult, final Action action) {
        dispatcher.execute(new GetGeographicalGranularitiesPaginatedListAction(IndicatorsWebConstants.LISTGRID_MAX_RESULTS, firstResult),
                new WaitingAsyncCallbackHandlingError<GetGeographicalGranularitiesPaginatedListResult>(this) {

                    @Override
                    public void onWaitSuccess(GetGeographicalGranularitiesPaginatedListResult result) {
                        getView().setGeoGranularities(firstResult, result.getDtos(), result.getTotalResults());
                        if (action != null) {
                            action.run();
                        }
                    }
                });
    }

    private void reloadGeoGranularities(int firstResult) {
        retrieveGeoGranularities(firstResult);
    }

    private static interface Action {

        void run();
    }
}