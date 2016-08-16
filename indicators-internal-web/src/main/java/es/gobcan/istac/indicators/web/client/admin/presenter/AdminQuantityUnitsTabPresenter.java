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
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.navigation.shared.NameTokens;
import es.gobcan.istac.indicators.web.client.IndicatorsValues;
import es.gobcan.istac.indicators.web.client.LoggedInGatekeeper;
import es.gobcan.istac.indicators.web.client.admin.view.handlers.AdminQuantityUnitsUiHandlers;
import es.gobcan.istac.indicators.web.shared.DeleteQuantityUnitsAction;
import es.gobcan.istac.indicators.web.shared.DeleteQuantityUnitsResult;
import es.gobcan.istac.indicators.web.shared.GetQuantityUnitsListAction;
import es.gobcan.istac.indicators.web.shared.GetQuantityUnitsListResult;
import es.gobcan.istac.indicators.web.shared.GetQuantityUnitsPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetQuantityUnitsPaginatedListResult;
import es.gobcan.istac.indicators.web.shared.SaveQuantityUnitAction;
import es.gobcan.istac.indicators.web.shared.SaveQuantityUnitResult;
import es.gobcan.istac.indicators.web.shared.criteria.QuantityUnitCriteria;

public class AdminQuantityUnitsTabPresenter extends Presenter<AdminQuantityUnitsTabPresenter.AdminQuantityUnitsTabView, AdminQuantityUnitsTabPresenter.AdminQuantityUnitsTabProxy>
        implements
            AdminQuantityUnitsUiHandlers {

    private DispatchAsync dispatcher;

    public interface AdminQuantityUnitsTabView extends View, HasUiHandlers<AdminQuantityUnitsUiHandlers> {

        void setQuantityUnits(int firstResult, List<QuantityUnitDto> quantityUnits, int totalResults);
        void onQuantityUnitCreated(QuantityUnitDto outputQuantityUnitDto);
        void onQuantityUnitUpdated(QuantityUnitDto outputQuantityUnitDto);

        // Search
        void clearSearchSection();
        QuantityUnitCriteria getQuantityUnitCriteria();
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

        getView().clearSearchSection();
        reloadQuantityUnits(0);
    }

    // ACTIONS

    @Override
    public void deleteQuantityUnits(List<String> quantityUnitsUuids, final int firstResult) {
        dispatcher.execute(new DeleteQuantityUnitsAction(quantityUnitsUuids), new WaitingAsyncCallbackHandlingError<DeleteQuantityUnitsResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                super.onWaitFailure(caught);
                reloadQuantityUnits(firstResult);
            }

            @Override
            public void onWaitSuccess(DeleteQuantityUnitsResult result) {
                reloadQuantityUnits(firstResult);
                reloadQuantityUnitsCache();
            }
        });
    }

    @Override
    public void retrieveQuantityUnits(QuantityUnitCriteria criteria) {
        retrieveQuantityUnitsAndDoAction(criteria, null);
    }

    @Override
    public void saveQuantityUnit(final int firstResult, QuantityUnitDto quantityUnitDto) {
        final boolean creation = quantityUnitDto.getUuid() == null;
        dispatcher.execute(new SaveQuantityUnitAction(quantityUnitDto), new WaitingAsyncCallbackHandlingError<SaveQuantityUnitResult>(this) {

            @Override
            public void onWaitSuccess(final SaveQuantityUnitResult result) {

                QuantityUnitCriteria criteria = getView().getQuantityUnitCriteria();
                criteria.setFirstResult(firstResult);

                retrieveQuantityUnitsAndDoAction(criteria, new Action() {

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

    private void retrieveQuantityUnitsAndDoAction(final QuantityUnitCriteria criteria, final Action action) {
        dispatcher.execute(new GetQuantityUnitsPaginatedListAction(criteria), new WaitingAsyncCallbackHandlingError<GetQuantityUnitsPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetQuantityUnitsPaginatedListResult result) {
                getView().setQuantityUnits(criteria.getFirstResult(), result.getDtos(), result.getTotalResults());
                if (action != null) {
                    action.run();
                }
            }
        });
    }

    private void reloadQuantityUnits(int firstResult) {
        QuantityUnitCriteria criteria = getView().getQuantityUnitCriteria();
        criteria.setFirstResult(firstResult);
        retrieveQuantityUnits(criteria);
    }

    private void reloadQuantityUnitsCache() {
        dispatcher.execute(new GetQuantityUnitsListAction(), new WaitingAsyncCallbackHandlingError<GetQuantityUnitsListResult>(this) {

            @Override
            public void onWaitSuccess(GetQuantityUnitsListResult result) {
                IndicatorsValues.setQuantityUnits(result.getQuantityUnits());
            }
        });
    }

    public static interface Action {

        void run();
    }
}