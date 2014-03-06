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
import es.gobcan.istac.indicators.web.client.LoggedInGatekeeper;
import es.gobcan.istac.indicators.web.client.NameTokens;
import es.gobcan.istac.indicators.web.client.admin.view.handlers.AdminGeoGranularitiesUiHandlers;
import es.gobcan.istac.indicators.web.client.events.UpdateGeographicalGranularitiesEvent;
import es.gobcan.istac.indicators.web.shared.DeleteGeoGranularitiesAction;
import es.gobcan.istac.indicators.web.shared.DeleteGeoGranularitiesResult;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularitiesAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularitiesPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularitiesPaginatedListResult;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularitiesResult;
import es.gobcan.istac.indicators.web.shared.SaveGeoGranularityAction;
import es.gobcan.istac.indicators.web.shared.SaveGeoGranularityResult;

public class AdminGeoGranularitiesTabPresenter extends Presenter<AdminGeoGranularitiesTabPresenter.AdminGeoGranularitiesTabView, AdminGeoGranularitiesTabPresenter.AdminGeoGranularitiesTabProxy>
        implements
            AdminGeoGranularitiesUiHandlers {

    public static final int MAX_RESULTS = 30;

    private DispatchAsync   dispatcher;

    public interface AdminGeoGranularitiesTabView extends View, HasUiHandlers<AdminGeoGranularitiesUiHandlers> {

        void onGeoGranularityCreated(GeographicalGranularityDto dto);
        void onGeoGranularityUpdated(GeographicalGranularityDto dto);
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

        reloadGeoGranularities();
    }

    // ACTIONS

    @Override
    public void deleteGeoGranularities(List<String> uuids) {
        dispatcher.execute(new DeleteGeoGranularitiesAction(uuids), new WaitingAsyncCallbackHandlingError<DeleteGeoGranularitiesResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                super.onWaitFailure(caught);
                reloadGeoGranularities();
            }

            @Override
            public void onWaitSuccess(DeleteGeoGranularitiesResult result) {
                reloadGeoGranularities();
                reloadGeoGranularitiesCache();
            }
        });
    }

    @Override
    public void retrieveGeoGranularities(final int firstResult) {
        retrieveGeoGranularitiesWithAction(firstResult, null);
    }

    private void retrieveGeoGranularitiesWithAction(final int firstResult, final Action action) {
        dispatcher.execute(new GetGeographicalGranularitiesPaginatedListAction(MAX_RESULTS, firstResult), new WaitingAsyncCallbackHandlingError<GetGeographicalGranularitiesPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetGeographicalGranularitiesPaginatedListResult result) {
                getView().setGeoGranularities(firstResult, result.getDtos(), result.getTotalResults());
                if (action != null) {
                    action.run();
                }
            }
        });
    }

    @Override
    public void saveGeoGranularity(final int currentPage, GeographicalGranularityDto dto) {
        final boolean creation = dto.getUuid() == null;
        dispatcher.execute(new SaveGeoGranularityAction(dto), new WaitingAsyncCallbackHandlingError<SaveGeoGranularityResult>(this) {

            @Override
            public void onWaitSuccess(final SaveGeoGranularityResult result) {
                retrieveGeoGranularitiesWithAction(currentPage, new Action() {

                    @Override
                    public void run() {
                        if (creation) {
                            getView().onGeoGranularityCreated(result.getOutputDto());
                        } else {
                            getView().onGeoGranularityUpdated(result.getOutputDto());
                        }
                    }
                });
                reloadGeoGranularitiesCache();
            }
        });

    }

    private void reloadGeoGranularities() {
        retrieveGeoGranularities(0);
    }

    private void reloadGeoGranularitiesCache() {
        dispatcher.execute(new GetGeographicalGranularitiesAction(), new WaitingAsyncCallbackHandlingError<GetGeographicalGranularitiesResult>(this) {

            @Override
            public void onWaitSuccess(GetGeographicalGranularitiesResult result) {
                UpdateGeographicalGranularitiesEvent.fire(AdminGeoGranularitiesTabPresenter.this, result.getGeographicalGranularityDtos());
            }
        });
    }

    private static interface Action {

        void run();
    }
}