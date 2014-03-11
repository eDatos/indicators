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

import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.web.client.LoggedInGatekeeper;
import es.gobcan.istac.indicators.web.client.NameTokens;
import es.gobcan.istac.indicators.web.client.admin.view.handlers.AdminGeoValuesUiHandlers;
import es.gobcan.istac.indicators.web.shared.DeleteGeoValuesAction;
import es.gobcan.istac.indicators.web.shared.DeleteGeoValuesResult;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesPaginatedListResult;
import es.gobcan.istac.indicators.web.shared.SaveGeoValueAction;
import es.gobcan.istac.indicators.web.shared.SaveGeoValueResult;

public class AdminGeoValuesTabPresenter extends Presenter<AdminGeoValuesTabPresenter.AdminGeoValuesTabView, AdminGeoValuesTabPresenter.AdminGeoValuesTabProxy> implements AdminGeoValuesUiHandlers {

    public static final int MAX_RESULTS = 30;

    private DispatchAsync   dispatcher;

    public interface AdminGeoValuesTabView extends View, HasUiHandlers<AdminGeoValuesUiHandlers> {

        void onGeoValueCreated(GeographicalValueDto dto);
        void onGeoValueUpdated(GeographicalValueDto dto);
        void setGeoValues(int firstResult, List<GeographicalValueDto> dtos, int maxResults);
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.adminGeoValuesPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface AdminGeoValuesTabProxy extends Proxy<AdminGeoValuesTabPresenter>, Place {
    }

    @Inject
    public AdminGeoValuesTabPresenter(EventBus eventBus, AdminGeoValuesTabView view, AdminGeoValuesTabProxy proxy, DispatchAsync dispatcher) {
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

        reloadGeoValues();
    }

    // ACTIONS

    @Override
    public void deleteGeoValues(List<String> uuids) {
        dispatcher.execute(new DeleteGeoValuesAction(uuids), new WaitingAsyncCallbackHandlingError<DeleteGeoValuesResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                super.onWaitFailure(caught);
                reloadGeoValues();
            }

            @Override
            public void onWaitSuccess(DeleteGeoValuesResult result) {
                reloadGeoValues();
            }
        });
    }

    @Override
    public void retrieveGeoValues(final int firstResult) {
        retrieveGeoValuesWithAction(firstResult, null);
    }

    private void retrieveGeoValuesWithAction(final int firstResult, final Action action) {
        dispatcher.execute(new GetGeographicalValuesPaginatedListAction(MAX_RESULTS, firstResult), new WaitingAsyncCallbackHandlingError<GetGeographicalValuesPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetGeographicalValuesPaginatedListResult result) {
                getView().setGeoValues(firstResult, result.getDtos(), result.getTotalResults());
                if (action != null) {
                    action.run();
                }
            }
        });
    }

    @Override
    public void saveGeoValue(final int currentPage, GeographicalValueDto dto) {
        final boolean creation = dto.getUuid() == null;
        dispatcher.execute(new SaveGeoValueAction(dto), new WaitingAsyncCallbackHandlingError<SaveGeoValueResult>(this) {

            @Override
            public void onWaitSuccess(final SaveGeoValueResult result) {
                retrieveGeoValuesWithAction(currentPage, new Action() {

                    @Override
                    public void run() {
                        if (creation) {
                            getView().onGeoValueCreated(result.getOutputDto());
                        } else {
                            getView().onGeoValueUpdated(result.getOutputDto());
                        }
                    }
                });
            }
        });

    }

    private void reloadGeoValues() {
        retrieveGeoValues(0);
    }

    private static interface Action {

        void run();
    }
}