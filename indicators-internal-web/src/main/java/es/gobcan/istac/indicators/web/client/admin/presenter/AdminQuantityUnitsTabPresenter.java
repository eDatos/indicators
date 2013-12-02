package es.gobcan.istac.indicators.web.client.admin.presenter;

import java.util.List;

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

import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.web.client.LoggedInGatekeeper;
import es.gobcan.istac.indicators.web.client.NameTokens;
import es.gobcan.istac.indicators.web.client.admin.view.handlers.AdminQuantityUnitsUiHandlers;
import es.gobcan.istac.indicators.web.client.events.UpdateQuantityUnitsEvent;
import es.gobcan.istac.indicators.web.client.utils.WaitingAsyncCallbackHandlingError;
import es.gobcan.istac.indicators.web.shared.DeleteQuantityUnitsAction;
import es.gobcan.istac.indicators.web.shared.DeleteQuantityUnitsResult;
import es.gobcan.istac.indicators.web.shared.GetQuantityUnitsListAction;
import es.gobcan.istac.indicators.web.shared.GetQuantityUnitsListResult;
import es.gobcan.istac.indicators.web.shared.GetQuantityUnitsPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetQuantityUnitsPaginatedListResult;
import es.gobcan.istac.indicators.web.shared.SaveQuantityUnitAction;
import es.gobcan.istac.indicators.web.shared.SaveQuantityUnitResult;

public class AdminQuantityUnitsTabPresenter extends Presenter<AdminQuantityUnitsTabPresenter.AdminQuantityUnitsTabView, AdminQuantityUnitsTabPresenter.AdminQuantityUnitsTabProxy>
        implements
            AdminQuantityUnitsUiHandlers {

    public static final int MAX_RESULTS = 30;

    private DispatchAsync   dispatcher;

    public interface AdminQuantityUnitsTabView extends View, HasUiHandlers<AdminQuantityUnitsUiHandlers> {

        void setQuantityUnits(int firstResult, List<QuantityUnitDto> quantityUnits, int totalResults);
        void onQuantityUnitCreated(QuantityUnitDto outputQuantityUnitDto);
        void onQuantityUnitUpdated(QuantityUnitDto outputQuantityUnitDto);
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.adminQuantityUnitsPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface AdminQuantityUnitsTabProxy extends Proxy<AdminQuantityUnitsTabPresenter>, Place {
    }

    @Inject
    public AdminQuantityUnitsTabPresenter(EventBus eventBus, AdminQuantityUnitsTabView view, AdminQuantityUnitsTabProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager) {
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

        reloadQuantityUnits();
    }

    // ACTIONS

    @Override
    public void deleteQuantityUnits(List<String> quantityUnitsUuids) {
        dispatcher.execute(new DeleteQuantityUnitsAction(quantityUnitsUuids), new WaitingAsyncCallbackHandlingError<DeleteQuantityUnitsResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                super.onWaitFailure(caught);
                reloadQuantityUnits();
            }

            @Override
            public void onWaitSuccess(DeleteQuantityUnitsResult result) {
                reloadQuantityUnits();
                reloadQuantityUnitsCache();
            }
        });
    }

    @Override
    public void retrieveQuantityUnits(final int firstResult, final int maxResults) {
        retrieveQuantityUnitsAndDoAction(firstResult, maxResults, null);
    }

    @Override
    public void saveQuantityUnit(final int currentPage, QuantityUnitDto quantityUnitDto) {
        final boolean creation = quantityUnitDto.getUuid() == null;
        dispatcher.execute(new SaveQuantityUnitAction(quantityUnitDto), new WaitingAsyncCallbackHandlingError<SaveQuantityUnitResult>(this) {

            @Override
            public void onWaitSuccess(final SaveQuantityUnitResult result) {
                retrieveQuantityUnitsAndDoAction(currentPage, MAX_RESULTS, new Action() {

                    @Override
                    public void run() {
                        if (creation) {
                            getView().onQuantityUnitCreated(result.getOutputQuantityUnitDto());
                        } else {
                            getView().onQuantityUnitUpdated(result.getOutputQuantityUnitDto());
                        }
                    }
                });

            }

        });

    }
    private void retrieveQuantityUnitsAndDoAction(final int firstResult, final int maxResults, final Action action) {
        dispatcher.execute(new GetQuantityUnitsPaginatedListAction(maxResults, firstResult), new WaitingAsyncCallbackHandlingError<GetQuantityUnitsPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetQuantityUnitsPaginatedListResult result) {
                getView().setQuantityUnits(firstResult, result.getDtos(), result.getTotalResults());
                if (action != null) {
                    action.run();
                }
            }
        });
    }

    private void reloadQuantityUnits() {
        retrieveQuantityUnits(0, MAX_RESULTS);
    }

    private void reloadQuantityUnitsCache() {
        dispatcher.execute(new GetQuantityUnitsListAction(), new WaitingAsyncCallbackHandlingError<GetQuantityUnitsListResult>(this) {

            @Override
            public void onWaitSuccess(GetQuantityUnitsListResult result) {
                UpdateQuantityUnitsEvent.fire(AdminQuantityUnitsTabPresenter.this, result.getQuantityUnits());
            }
        });
    }

    public static interface Action {

        void run();
    }
}