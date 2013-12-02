package es.gobcan.istac.indicators.web.client.admin.presenter;

import java.util.List;

import com.google.web.bindery.event.shared.EventBus;
import com.google.inject.Inject;
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

import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;
import es.gobcan.istac.indicators.web.client.LoggedInGatekeeper;
import es.gobcan.istac.indicators.web.client.NameTokens;
import es.gobcan.istac.indicators.web.client.admin.view.handlers.AdminUnitMultipliersUiHandlers;
import es.gobcan.istac.indicators.web.client.utils.WaitingAsyncCallbackHandlingError;
import es.gobcan.istac.indicators.web.shared.DeleteUnitMultipliersAction;
import es.gobcan.istac.indicators.web.shared.DeleteUnitMultipliersResult;
import es.gobcan.istac.indicators.web.shared.GetUnitMultipliersAction;
import es.gobcan.istac.indicators.web.shared.GetUnitMultipliersPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetUnitMultipliersPaginatedListResult;
import es.gobcan.istac.indicators.web.shared.GetUnitMultipliersResult;
import es.gobcan.istac.indicators.web.shared.SaveUnitMultiplierAction;
import es.gobcan.istac.indicators.web.shared.SaveUnitMultiplierResult;

public class AdminUnitMultipliersTabPresenter extends Presenter<AdminUnitMultipliersTabPresenter.AdminUnitMultipliersTabView, AdminUnitMultipliersTabPresenter.AdminUnitMultipliersTabProxy>
        implements
            AdminUnitMultipliersUiHandlers {

    public static final int MAX_RESULTS = 30;
    private DispatchAsync   dispatcher;

    public interface AdminUnitMultipliersTabView extends View, HasUiHandlers<AdminUnitMultipliersUiHandlers> {

        void onUnitMultiplierCreated(UnitMultiplierDto dto);
        void onUnitMultiplierUpdated(UnitMultiplierDto dto);
        void setUnitMultipliers(int firstResult, List<UnitMultiplierDto> dtos, int totalResults);
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.adminUnitMultipliersPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface AdminUnitMultipliersTabProxy extends Proxy<AdminUnitMultipliersTabPresenter>, Place {
    }

    @Inject
    public AdminUnitMultipliersTabPresenter(EventBus eventBus, AdminUnitMultipliersTabView view, AdminUnitMultipliersTabProxy proxy, DispatchAsync dispatcher) {
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

        reloadUnitMultipliers();
    }

    // ACTIONS

    @Override
    public void deleteUnitMultipliers(List<String> uuids) {
        dispatcher.execute(new DeleteUnitMultipliersAction(uuids), new WaitingAsyncCallbackHandlingError<DeleteUnitMultipliersResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                super.onWaitFailure(caught);
                reloadUnitMultipliers();
            }

            @Override
            public void onWaitSuccess(DeleteUnitMultipliersResult result) {
                reloadUnitMultipliers();
            }
        });
    }

    @Override
    public void retrieveUnitMultipliers(final int firstResult) {
        retrieveUnitMultipliersAndDoAction(firstResult, null);
    }

    private void retrieveUnitMultipliersAndDoAction(final int firstResult, final Action action) {
        dispatcher.execute(new GetUnitMultipliersPaginatedListAction(MAX_RESULTS, firstResult), new WaitingAsyncCallbackHandlingError<GetUnitMultipliersPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetUnitMultipliersPaginatedListResult result) {
                getView().setUnitMultipliers(firstResult, result.getDtos(), result.getTotalResults());
                if (action != null) {
                    action.run();
                }
            }
        });
    }

    private void reloadUnitMultipliers() {
        retrieveUnitMultipliers(0);
    }

    @Override
    public void saveUnitMultiplier(final int currentPage, UnitMultiplierDto dto) {
        final boolean creation = dto.getUuid() == null;

        dispatcher.execute(new SaveUnitMultiplierAction(dto), new WaitingAsyncCallbackHandlingError<SaveUnitMultiplierResult>(this) {

            @Override
            public void onWaitSuccess(final SaveUnitMultiplierResult result) {
                retrieveUnitMultipliersAndDoAction(currentPage, new Action() {

                    @Override
                    public void run() {
                        if (creation) {
                            getView().onUnitMultiplierCreated(result.getOutputDto());
                        } else {
                            getView().onUnitMultiplierUpdated(result.getOutputDto());
                        }
                    }
                });
            }
        });

    }

    private static interface Action {

        void run();
    }
}