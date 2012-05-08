package es.gobcan.istac.indicators.web.client.system.presenter;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

import java.util.List;

import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import es.gobcan.istac.indicators.web.client.NameTokens;
import es.gobcan.istac.indicators.web.client.PlaceRequestParams;
import es.gobcan.istac.indicators.web.client.main.presenter.MainPagePresenter;
import es.gobcan.istac.indicators.web.client.presenter.PaginationPresenter;
import es.gobcan.istac.indicators.web.client.utils.ErrorUtils;
import es.gobcan.istac.indicators.web.client.widgets.StatusBar;
import es.gobcan.istac.indicators.web.client.widgets.WaitingAsyncCallback;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsSystemsAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsSystemsResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemPaginatedListResult;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

public class SystemListPresenter extends PaginationPresenter<SystemListPresenter.SystemListView, SystemListPresenter.SystemListProxy> implements SystemListUiHandler {

    public static final int DEFAULT_MAX_RESULTS = 30;

    public interface SystemListView extends View, HasUiHandlers<SystemListPresenter> {

        void setIndSystemList(List<IndicatorsSystemDtoWeb> indSysList);
        void onIndicatorsSystemsDeleted();

        StatusBar getStatusBar();
        void refreshStatusBar();
        void setNumberOfElements(int numberOfElements);
        void setPageNumber(int pageNumber);
        void removeSelectedData();
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.systemListPage)
    public interface SystemListProxy extends Proxy<SystemListPresenter>, Place {
    }

    private PlaceManager  placeManager;
    private DispatchAsync dispatcher;

    @Inject
    public SystemListPresenter(EventBus eventBus, SystemListView view, SystemListProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, view, proxy, dispatcher, DEFAULT_MAX_RESULTS);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
        getView().setUiHandlers(this);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.CONTENT_SLOT, this);
    }

    @Override
    protected void onReset() {
        super.onReset();
        SetTitleEvent.fire(SystemListPresenter.this, getConstants().indicatorSystems());

        initializePaginationSettings();

        retrieveResultSet();
    }

    @Override
    public void reloadIndicatorsSystemList() {
        retrieveResultSet();
    }

    @Override
    public void goToIndicatorsSystem(String indSysCode) {
        PlaceRequest systemDetailRequest = new PlaceRequest(NameTokens.systemPage).with(PlaceRequestParams.indSystemParam, indSysCode);
        placeManager.revealPlace(systemDetailRequest);
    }

    @Override
    public void retrieveResultSet() {
        dispatcher.execute(new GetIndicatorsSystemPaginatedListAction(getMaxResults(), getFirstResult()), new WaitingAsyncCallback<GetIndicatorsSystemPaginatedListResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(SystemListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().systemErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetIndicatorsSystemPaginatedListResult result) {
                SystemListPresenter.this.totalResults = result.getTotalResults();

                setNumberOfElements(result.getIndicatorsSystemList().size());

                // update Selected label e.g "0 of 50 selected"
                getView().setNumberOfElements(getNumberOfElements());
                getView().setPageNumber(getPageNumber());
                getView().refreshStatusBar();

                // Log.debug("onSuccess() - firstResult: " + firstResult +
                // " pageNumber: " + pageNumber + " numberOfElements: " +
                // numberOfElements);

                // enable/disable the pagination widgets
                if (getPageNumber() == 1) {
                    getView().getStatusBar().getResultSetFirstButton().disable();
                    getView().getStatusBar().getResultSetPreviousButton().disable();
                } else {
                    getView().getStatusBar().getResultSetFirstButton().enable();
                    getView().getStatusBar().getResultSetPreviousButton().enable();
                }

                // enable/disable the pagination widgets
                if ((result.getTotalResults() - (getPageNumber() - 1) * DEFAULT_MAX_RESULTS) > getNumberOfElements()) {
                    getView().getStatusBar().getResultSetNextButton().enable();
                    getView().getStatusBar().getResultSetLastButton().enable();
                } else {
                    getView().getStatusBar().getResultSetNextButton().disable();
                    getView().getStatusBar().getResultSetLastButton().disable();
                }

                // pass the result set to the View
                getView().setIndSystemList(result.getIndicatorsSystemList());
            }
        });
    }

    @Override
    public void deleteIndicatorsSystems(List<String> uuids) {
        dispatcher.execute(new DeleteIndicatorsSystemsAction(uuids), new WaitingAsyncCallback<DeleteIndicatorsSystemsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(SystemListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().systemErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteIndicatorsSystemsResult result) {
                ShowMessageEvent.fire(SystemListPresenter.this, ErrorUtils.getMessageList(getMessages().systemDeleted()), MessageTypeEnum.SUCCESS);
                getView().onIndicatorsSystemsDeleted();
            }
        });
    }

}
