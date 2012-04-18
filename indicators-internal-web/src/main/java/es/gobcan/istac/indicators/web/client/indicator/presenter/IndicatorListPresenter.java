package es.gobcan.istac.indicators.web.client.indicator.presenter;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.SubjectDto;
import es.gobcan.istac.indicators.web.client.NameTokens;
import es.gobcan.istac.indicators.web.client.PlaceRequestParams;
import es.gobcan.istac.indicators.web.client.main.presenter.MainPagePresenter;
import es.gobcan.istac.indicators.web.client.utils.ErrorUtils;
import es.gobcan.istac.indicators.web.client.widgets.StatusBar;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorAction;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorResult;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorPaginatedListResult;
import es.gobcan.istac.indicators.web.shared.GetSubjectsListAction;
import es.gobcan.istac.indicators.web.shared.GetSubjectsListResult;

public class IndicatorListPresenter extends Presenter<IndicatorListPresenter.IndicatorListView, IndicatorListPresenter.IndicatorListProxy> implements IndicatorListUiHandler {

    public static final int DEFAULT_MAX_RESULTS = 30;

    private Logger          logger              = Logger.getLogger(IndicatorListPresenter.class.getName());

    private DispatchAsync   dispatcher;
    private PlaceManager    placeManager;

    private int             maxResults;
    private int             firstResult;
    private int             pageNumber;
    private int             numberOfElements;

    public interface IndicatorListView extends View, HasUiHandlers<IndicatorListPresenter> {

        void setIndicatorList(List<IndicatorDto> indicatorList);
        void setSubjects(List<SubjectDto> subjectDtos);

        StatusBar getStatusBar();
        void refreshStatusBar();
        void setNumberOfElements(int numberOfElements);
        void setPageNumber(int pageNumber);
        void removeSelectedData();
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.indicatorListPage)
    public interface IndicatorListProxy extends Proxy<IndicatorListPresenter>, Place {
    }

    @Inject
    public IndicatorListPresenter(EventBus eventBus, IndicatorListView view, IndicatorListProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, view, proxy);
        this.dispatcher = dispatcher;
        getView().setUiHandlers(this);
        this.placeManager = placeManager;
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.CONTENT_SLOT, this);
    }

    @Override
    protected void onReset() {
        super.onReset();
        SetTitleEvent.fire(IndicatorListPresenter.this, getConstants().indicators());

        maxResults = DEFAULT_MAX_RESULTS;
        firstResult = 0;
        pageNumber = 1;
        numberOfElements = maxResults;

        retrieveIndicatorList();
    }

    private void retrieveIndicatorList() {
        dispatcher.execute(new GetIndicatorPaginatedListAction(getMaxResults(), getFirstResult()), new AsyncCallback<GetIndicatorPaginatedListResult>() {

            @Override
            public void onFailure(Throwable caught) {
                ShowMessageEvent.fire(IndicatorListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().indicErrorRetrieveList()), MessageTypeEnum.ERROR);
            }

            @Override
            public void onSuccess(GetIndicatorPaginatedListResult result) {
                setNumberOfElements(result.getIndicatorList().size());

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
                }

                // enable/disable the pagination widgets
                if ((result.getTotalResults() - (getPageNumber() - 1) * DEFAULT_MAX_RESULTS) > getNumberOfElements()) {
                    getView().getStatusBar().getResultSetNextButton().enable();
                } else {
                    getView().getStatusBar().getResultSetNextButton().disable();
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
        dispatcher.execute(new CreateIndicatorAction(indicator), new AsyncCallback<CreateIndicatorResult>() {

            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error creating indicator");
                ShowMessageEvent.fire(IndicatorListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().indicErrorCreate()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(CreateIndicatorResult result) {
                logger.log(Level.INFO, "Indicator created successfully");
                retrieveIndicatorList();
                ShowMessageEvent.fire(IndicatorListPresenter.this, ErrorUtils.getMessageList(getMessages().indicCreated()), MessageTypeEnum.SUCCESS);
            }
        });
    }

    @Override
    public void reloadIndicatorList() {
        retrieveIndicatorList();
    }

    @Override
    public void deleteIndicators(List<String> uuids) {
        dispatcher.execute(new DeleteIndicatorsAction(uuids), new AsyncCallback<DeleteIndicatorsResult>() {

            @Override
            public void onFailure(Throwable caught) {
                ShowMessageEvent.fire(IndicatorListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().indicErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(DeleteIndicatorsResult result) {
                retrieveIndicatorList();
                ShowMessageEvent.fire(IndicatorListPresenter.this, ErrorUtils.getMessageList(getMessages().indicDeleted()), MessageTypeEnum.SUCCESS);
            }
        });
    }

    @Override
    public void retrieveSubjectsList() {
        dispatcher.execute(new GetSubjectsListAction(), new AsyncCallback<GetSubjectsListResult>() {

            @Override
            public void onFailure(Throwable caught) {
                ShowMessageEvent.fire(IndicatorListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRetrievingSubjects()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(GetSubjectsListResult result) {
                getView().setSubjects(result.getSubjectDtos());
            }
        });
    }

    // PAGINATION

    protected void resultSetFirstButtonClicked() {
        firstResult = 0;
        pageNumber = 1;

        retrieveIndicatorList();
    }

    protected void resultSetPreviousButtonClicked() {
        firstResult -= maxResults;
        pageNumber--;

        retrieveIndicatorList();
    }

    protected void resultSetNextButtonClicked() {
        firstResult += numberOfElements;
        pageNumber++;

        retrieveIndicatorList();
    }

    protected int getMaxResults() {
        return maxResults;
    }

    protected void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    protected int getFirstResult() {
        return firstResult;
    }

    protected void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }

    protected int getPageNumber() {
        return pageNumber;
    }

    protected void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    protected int getNumberOfElements() {
        return numberOfElements;
    }

    protected void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    @Override
    public void onResultSetFirstButtonClicked() {
        resultSetFirstButtonClicked();

        getView().getStatusBar().getResultSetFirstButton().disable();
    }

    @Override
    public void onResultSetPreviousButtonClicked() {
        resultSetPreviousButtonClicked();
    }

    @Override
    public void onResultSetNextButtonClicked() {
        resultSetNextButtonClicked();

        getView().getStatusBar().getResultSetFirstButton().enable();
        getView().getStatusBar().getResultSetPreviousButton().enable();
    }

}
