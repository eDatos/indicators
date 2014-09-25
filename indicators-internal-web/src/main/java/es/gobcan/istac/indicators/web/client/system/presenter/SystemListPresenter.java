package es.gobcan.istac.indicators.web.client.system.presenter;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

import java.util.List;

import org.siemac.metamac.web.common.client.constants.CommonWebConstants;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

import es.gobcan.istac.indicators.web.client.LoggedInGatekeeper;
import es.gobcan.istac.indicators.web.client.NameTokens;
import es.gobcan.istac.indicators.web.client.PlaceRequestParams;
import es.gobcan.istac.indicators.web.client.main.presenter.MainPagePresenter;
import es.gobcan.istac.indicators.web.client.main.presenter.ToolStripPresenterWidget;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsSystemsAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsSystemsResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemPaginatedListResult;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemSummaryDtoWeb;

public class SystemListPresenter extends Presenter<SystemListPresenter.SystemListView, SystemListPresenter.SystemListProxy> implements SystemListUiHandler {

    private ToolStripPresenterWidget toolStripPresenterWidget;

    public interface SystemListView extends View, HasUiHandlers<SystemListUiHandler> {

        void setIndSystemList(List<IndicatorsSystemSummaryDtoWeb> indSysList, int firstResult, int totalResults);
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.systemListPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface SystemListProxy extends Proxy<SystemListPresenter>, Place {
    }

    private PlaceManager  placeManager;
    private DispatchAsync dispatcher;

    @Inject
    public SystemListPresenter(EventBus eventBus, SystemListView view, SystemListProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager, ToolStripPresenterWidget toolStripPresenterWidget) {
        super(eventBus, view, proxy);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
        this.toolStripPresenterWidget = toolStripPresenterWidget;
        getView().setUiHandlers(this);
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentToolBar = new Type<RevealContentHandler<?>>();

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.CONTENT_SLOT, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        SetTitleEvent.fire(SystemListPresenter.this, getConstants().indicatorSystems());
        retrieveSystems(0, CommonWebConstants.MAIN_LIST_MAX_RESULTS);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        setInSlot(TYPE_SetContextAreaContentToolBar, toolStripPresenterWidget);
    }

    @Override
    public void goToIndicatorsSystem(String indSysCode) {
        PlaceRequest systemDetailRequest = new PlaceRequest(NameTokens.systemPage).with(PlaceRequestParams.indSystemParam, indSysCode);
        placeManager.revealPlace(systemDetailRequest);
    }

    @Override
    public void retrieveSystems(int firstResult, int maxResults) {
        dispatcher.execute(new GetIndicatorsSystemPaginatedListAction(maxResults, firstResult), new WaitingAsyncCallbackHandlingError<GetIndicatorsSystemPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetIndicatorsSystemPaginatedListResult result) {
                getView().setIndSystemList(result.getIndicatorsSystemList(), result.getFirstResultOut(), result.getTotalResults());
            }
        });
    }

    @Override
    public void deleteIndicatorsSystems(List<String> uuids) {
        dispatcher.execute(new DeleteIndicatorsSystemsAction(uuids), new WaitingAsyncCallbackHandlingError<DeleteIndicatorsSystemsResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                super.onWaitFailure(caught);
                retrieveSystems(0, CommonWebConstants.MAIN_LIST_MAX_RESULTS);
            }
            @Override
            public void onWaitSuccess(DeleteIndicatorsSystemsResult result) {
                fireSuccessMessage(getMessages().systemDeleted());
                retrieveSystems(0, CommonWebConstants.MAIN_LIST_MAX_RESULTS);
            }
        });
    }
}
