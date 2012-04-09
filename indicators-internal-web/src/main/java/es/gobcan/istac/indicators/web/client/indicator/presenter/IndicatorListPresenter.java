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

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.SubjectDto;
import es.gobcan.istac.indicators.web.client.NameTokens;
import es.gobcan.istac.indicators.web.client.PlaceRequestParams;
import es.gobcan.istac.indicators.web.client.main.presenter.MainPagePresenter;
import es.gobcan.istac.indicators.web.client.utils.ErrorUtils;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorAction;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorResult;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorListResult;
import es.gobcan.istac.indicators.web.shared.GetSubjectsListAction;
import es.gobcan.istac.indicators.web.shared.GetSubjectsListResult;

public class IndicatorListPresenter extends Presenter<IndicatorListPresenter.IndicatorListView, IndicatorListPresenter.IndicatorListProxy> implements IndicatorListUiHandler {

    private Logger        logger = Logger.getLogger(IndicatorListPresenter.class.getName());

    private DispatchAsync dispatcher;
    private PlaceManager  placeManager;

    public interface IndicatorListView extends View, HasUiHandlers<IndicatorListPresenter> {

        void setIndicatorList(List<IndicatorDto> indicatorList);
        void setSubjects(List<SubjectDto> subjectDtos);
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
        retrieveIndicatorList();
    }

    private void retrieveIndicatorList() {
        dispatcher.execute(new GetIndicatorListAction(), new AsyncCallback<GetIndicatorListResult>() {

            @Override
            public void onFailure(Throwable caught) {
                ShowMessageEvent.fire(IndicatorListPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().indicErrorRetrieveList()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(GetIndicatorListResult result) {
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

}
