package es.gobcan.istac.indicators.web.client.admin.presenter;

import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
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
import es.gobcan.istac.indicators.web.client.IndicatorsWeb;
import es.gobcan.istac.indicators.web.client.LoggedInGatekeeper;
import es.gobcan.istac.indicators.web.client.NameTokens;
import es.gobcan.istac.indicators.web.client.admin.view.handlers.AdminQuantityUnitsUiHandlers;
import es.gobcan.istac.indicators.web.client.utils.WaitingAsyncCallbackHandlingError;
import es.gobcan.istac.indicators.web.shared.DeleteQuantityUnitsAction;
import es.gobcan.istac.indicators.web.shared.DeleteQuantityUnitsResult;
import es.gobcan.istac.indicators.web.shared.GetQuantityUnitsListAction;
import es.gobcan.istac.indicators.web.shared.GetQuantityUnitsListResult;
import es.gobcan.istac.indicators.web.shared.SaveQuantityUnitAction;
import es.gobcan.istac.indicators.web.shared.SaveQuantityUnitResult;

public class AdminQuantityUnitsTabPresenter extends Presenter<AdminQuantityUnitsTabPresenter.AdminQuantityUnitsTabView, AdminQuantityUnitsTabPresenter.AdminQuantityUnitsTabProxy>
        implements
            AdminQuantityUnitsUiHandlers {

    private DispatchAsync dispatcher;

    private PlaceManager  placeManager;

    public interface AdminQuantityUnitsTabView extends View, HasUiHandlers<AdminQuantityUnitsUiHandlers> {

        void setQuantityUnits(List<QuantityUnitDto> quantityUnits);
        void onQuantityUnitSaved(QuantityUnitDto quantityUnit);
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
        this.placeManager = placeManager;
        getView().setUiHandlers(this);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, AdminPresenter.TYPE_SetContextAreaAdmin, this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        // FIXME: select tab in parent
        // SelectAdminTabEvent.fire(this, AdminTabTypeEnum.QUANTITY_UNITS);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);

        retrieveQuantityUnits();
    }

    // ACTIONS

    @Override
    public void deleteQuantityUnits(List<String> quantityUnitsUuids) {
        dispatcher.execute(new DeleteQuantityUnitsAction(quantityUnitsUuids), new WaitingAsyncCallbackHandlingError<DeleteQuantityUnitsResult>(this, IndicatorsWeb.getMessages()
                .errorDeletingQuantityUnits()) {

            @Override
            public void onWaitFailure(Throwable caught) {
                super.onWaitFailure(caught);
                retrieveQuantityUnits();
            }

            @Override
            public void onWaitSuccess(DeleteQuantityUnitsResult result) {
                retrieveQuantityUnits();
            }
        });
    }

    public void retrieveQuantityUnits() {
        dispatcher.execute(new GetQuantityUnitsListAction(), new WaitingAsyncCallbackHandlingError<GetQuantityUnitsListResult>(this, IndicatorsWeb.getMessages().errorRetrievingQuantityUnits()) {

            @Override
            public void onWaitSuccess(GetQuantityUnitsListResult result) {
                getView().setQuantityUnits(result.getQuantityUnits());
            }
        });
    }

    @Override
    public void saveQuantityUnit(QuantityUnitDto quantityUnitDto) {
        dispatcher.execute(new SaveQuantityUnitAction(quantityUnitDto), new WaitingAsyncCallbackHandlingError<SaveQuantityUnitResult>(this, IndicatorsWeb.getMessages().errorSavingQuantityUnit()) {

            @Override
            public void onWaitSuccess(SaveQuantityUnitResult result) {
                getView().onQuantityUnitSaved(result.getOutputQuantityUnitDto());
            }
        });

    }
}