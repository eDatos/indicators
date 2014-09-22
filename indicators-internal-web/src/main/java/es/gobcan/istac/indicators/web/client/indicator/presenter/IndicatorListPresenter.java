package es.gobcan.istac.indicators.web.client.indicator.presenter;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
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

import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.core.dto.SubjectDto;
import es.gobcan.istac.indicators.web.client.LoggedInGatekeeper;
import es.gobcan.istac.indicators.web.client.NameTokens;
import es.gobcan.istac.indicators.web.client.PlaceRequestParams;
import es.gobcan.istac.indicators.web.client.main.presenter.MainPagePresenter;
import es.gobcan.istac.indicators.web.client.main.presenter.ToolStripPresenterWidget;
import es.gobcan.istac.indicators.web.client.presenter.PaginationPresenter;
import es.gobcan.istac.indicators.web.client.widgets.StatusBar;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorAction;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorResult;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorPaginatedListResult;
import es.gobcan.istac.indicators.web.shared.GetSubjectsListAction;
import es.gobcan.istac.indicators.web.shared.GetSubjectsListResult;
import es.gobcan.istac.indicators.web.shared.criteria.IndicatorCriteria;

public class IndicatorListPresenter extends PaginationPresenter<IndicatorListPresenter.IndicatorListView, IndicatorListPresenter.IndicatorListProxy> implements IndicatorListUiHandler {

    public static final int          DEFAULT_MAX_RESULTS = 30;

    private Logger                   logger              = Logger.getLogger(IndicatorListPresenter.class.getName());

    private DispatchAsync            dispatcher;
    private PlaceManager             placeManager;

    private ToolStripPresenterWidget toolStripPresenterWidget;

    public interface IndicatorListView extends View, HasUiHandlers<IndicatorListPresenter> {

        void setIndicatorList(List<IndicatorSummaryDto> indicatorList);
        void setSubjects(List<SubjectDto> subjectDtos);

        StatusBar getStatusBar();
        void refreshStatusBar();
        void setNumberOfElements(int numberOfElements);
        void setPageNumber(int pageNumber);
        void removeSelectedData();

        // Search
        void clearSearchSection();
        IndicatorCriteria getIndicatorCriteria();
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.indicatorListPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface IndicatorListProxy extends Proxy<IndicatorListPresenter>, Place {
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentToolBar = new Type<RevealContentHandler<?>>();

    @Inject
    public IndicatorListPresenter(EventBus eventBus, IndicatorListView view, IndicatorListProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager,
            ToolStripPresenterWidget toolStripPresenterWidget) {
        super(eventBus, view, proxy, dispatcher, DEFAULT_MAX_RESULTS);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
        getView().setUiHandlers(this);
        this.toolStripPresenterWidget = toolStripPresenterWidget;
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.CONTENT_SLOT, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        // Clear search section
        getView().clearSearchSection();

        SetTitleEvent.fire(IndicatorListPresenter.this, getConstants().indicators());
        initializePaginationSettings();
        retrieveResultSet();
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        setInSlot(TYPE_SetContextAreaContentToolBar, toolStripPresenterWidget);
    }

    @Override
    public void retrieveIndicators() {
        retrieveResultSet();
    }

    @Override
    public void retrieveResultSet() {
        IndicatorCriteria criteria = getView().getIndicatorCriteria();
        criteria.setFirstResult(getFirstResult());
        criteria.setMaxResults(getMaxResults());
        dispatcher.execute(new GetIndicatorPaginatedListAction(criteria), new WaitingAsyncCallbackHandlingError<GetIndicatorPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetIndicatorPaginatedListResult result) {
                IndicatorListPresenter.this.totalResults = result.getTotalResults();

                setNumberOfElements(result.getIndicatorList().size());

                // update Selected label e.g "0 of 50 selected"
                getView().setNumberOfElements(getNumberOfElements());
                getView().setPageNumber(getPageNumber());
                getView().refreshStatusBar();

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
                getView().setIndicatorList(result.getIndicatorList());
            }
        });
    }

    // Handler events
    @Override
    public void goToIndicator(String code) {
        PlaceRequest indicatorDetailRequest = new PlaceRequest(NameTokens.indicatorPage).with(PlaceRequestParams.indicatorParam, code);
        placeManager.revealPlace(indicatorDetailRequest);
    }

    @Override
    public void createIndicator(IndicatorDto indicator) {
        dispatcher.execute(new CreateIndicatorAction(indicator), new WaitingAsyncCallbackHandlingError<CreateIndicatorResult>(this) {

            @Override
            public void onWaitSuccess(CreateIndicatorResult result) {
                logger.log(Level.INFO, "Indicator created successfully");
                fireSuccessMessage(getMessages().indicCreated());
                // Go to the last page to view the indicator created
                totalResults++;
                resultSetLastButtonClicked();
            }
        });
    }

    @Override
    public void reloadIndicatorList() {
        retrieveResultSet();
    }

    @Override
    public void deleteIndicators(List<String> uuids) {
        dispatcher.execute(new DeleteIndicatorsAction(uuids), new WaitingAsyncCallbackHandlingError<DeleteIndicatorsResult>(this) {

            @Override
            public void onWaitSuccess(DeleteIndicatorsResult result) {
                retrieveResultSet();
                fireSuccessMessage(getMessages().indicDeleted());
            }
        });
    }

    @Override
    public void retrieveSubjectsList() {
        dispatcher.execute(new GetSubjectsListAction(), new WaitingAsyncCallbackHandlingError<GetSubjectsListResult>(this) {

            @Override
            public void onWaitSuccess(GetSubjectsListResult result) {
                getView().setSubjects(result.getSubjectDtos());
            }
        });
    }
}
