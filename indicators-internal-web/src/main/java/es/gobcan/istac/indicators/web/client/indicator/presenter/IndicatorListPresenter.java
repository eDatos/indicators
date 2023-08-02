package es.gobcan.istac.indicators.web.client.indicator.presenter;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

import java.util.List;

import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;
import org.siemac.metamac.web.common.shared.criteria.SrmExternalResourceRestCriteria;
import org.siemac.metamac.web.common.shared.criteria.SrmItemRestCriteria;
import org.siemac.metamac.web.common.shared.domain.ExternalItemsResult;

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

import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.core.dto.SubjectDto;
import es.gobcan.istac.indicators.core.navigation.shared.NameTokens;
import es.gobcan.istac.indicators.core.navigation.shared.PlaceRequestParams;
import es.gobcan.istac.indicators.web.client.LoggedInGatekeeper;
import es.gobcan.istac.indicators.web.client.main.presenter.MainPagePresenter;
import es.gobcan.istac.indicators.web.client.main.presenter.ToolStripPresenterWidget;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorAction;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorResult;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsResult;
import es.gobcan.istac.indicators.web.shared.DisableNotifyPopulationErrorsAction;
import es.gobcan.istac.indicators.web.shared.DisableNotifyPopulationErrorsResult;
import es.gobcan.istac.indicators.web.shared.EnableNotifyPopulationErrorsAction;
import es.gobcan.istac.indicators.web.shared.EnableNotifyPopulationErrorsResult;
import es.gobcan.istac.indicators.web.shared.ExportIndicatorsAction;
import es.gobcan.istac.indicators.web.shared.ExportIndicatorsResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorPaginatedListResult;
import es.gobcan.istac.indicators.web.shared.GetSubjectsListAction;
import es.gobcan.istac.indicators.web.shared.GetSubjectsListResult;
import es.gobcan.istac.indicators.web.shared.criteria.IndicatorCriteria;
import es.gobcan.istac.indicators.web.shared.external.GetExternalResourcesAction;
import es.gobcan.istac.indicators.web.shared.external.GetExternalResourcesResult;
import es.gobcan.istac.indicators.web.shared.external.RestWebCriteriaUtils;

public class IndicatorListPresenter extends Presenter<IndicatorListPresenter.IndicatorListView, IndicatorListPresenter.IndicatorListProxy> implements IndicatorListUiHandler {

    private DispatchAsync            dispatcher;
    private PlaceManager             placeManager;

    private ToolStripPresenterWidget toolStripPresenterWidget;

    public interface IndicatorListView extends View, HasUiHandlers<IndicatorListUiHandler> {

        void setIndicatorList(List<IndicatorSummaryDto> indicatorList, int firstResult, int totalResults);

        void setSubjectsForCreateIndicator(List<SubjectDto> subjectDtos);

        void setSubjectsForSearchIndicator(List<SubjectDto> subjectDtos);

        // Search
        void clearSearchSection();

        IndicatorCriteria getIndicatorCriteria();

        // external items
        void setItems(String formItemName, ExternalItemsResult result);
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
        super(eventBus, view, proxy);
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
        IndicatorCriteria criteria = getView().getIndicatorCriteria();
        retrieveIndicators(criteria);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        setInSlot(TYPE_SetContextAreaContentToolBar, toolStripPresenterWidget);
    }

    @Override
    public void retrieveIndicators(IndicatorCriteria criteria) {
        dispatcher.execute(new GetIndicatorPaginatedListAction(criteria), new WaitingAsyncCallbackHandlingError<GetIndicatorPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetIndicatorPaginatedListResult result) {
                getView().setIndicatorList(result.getIndicatorList(), result.getFirstResult(), result.getTotalResults());
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
                fireSuccessMessage(getMessages().indicCreated());

                // Go to the last page to view the indicator created
                IndicatorCriteria indicatorCriteria = getView().getIndicatorCriteria();
                retrieveIndicators(indicatorCriteria);
            }
        });
    }

    @Override
    public void deleteIndicators(List<String> uuids) {
        dispatcher.execute(new DeleteIndicatorsAction(uuids), new WaitingAsyncCallbackHandlingError<DeleteIndicatorsResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                super.onWaitFailure(caught);
                IndicatorCriteria criteria = getView().getIndicatorCriteria();
                retrieveIndicators(criteria);
            }

            @Override
            public void onWaitSuccess(DeleteIndicatorsResult result) {
                fireSuccessMessage(getMessages().indicDeleted());
                IndicatorCriteria criteria = getView().getIndicatorCriteria();
                retrieveIndicators(criteria);
            }
        });
    }

    @Override
    public void retrieveSubjectsListForCreateIndicator() {
        dispatcher.execute(new GetSubjectsListAction(), new WaitingAsyncCallbackHandlingError<GetSubjectsListResult>(this) {

            @Override
            public void onWaitSuccess(GetSubjectsListResult result) {
                getView().setSubjectsForCreateIndicator(result.getSubjectDtos());
            }
        });
    }

    @Override
    public void retrieveSubjectsListForSearchIndicator() {
        dispatcher.execute(new GetSubjectsListAction(), new WaitingAsyncCallbackHandlingError<GetSubjectsListResult>(this) {

            @Override
            public void onWaitSuccess(GetSubjectsListResult result) {
                getView().setSubjectsForSearchIndicator(result.getSubjectDtos());
            }
        });
    }

    @Override
    public void exportIndicators(IndicatorCriteria criteria) {
        dispatcher.execute(new ExportIndicatorsAction(criteria), new WaitingAsyncCallbackHandlingError<ExportIndicatorsResult>(this) {

            @Override
            public void onWaitSuccess(ExportIndicatorsResult result) {
                CommonUtils.downloadFile(result.getFileName());
            }

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(IndicatorListPresenter.this, caught);
            }

        });
    }

    @Override
    public void enableNotifyPopulationErrors(List<String> uuids) {
        dispatcher.execute(new EnableNotifyPopulationErrorsAction(uuids), new WaitingAsyncCallbackHandlingError<EnableNotifyPopulationErrorsResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                super.onWaitFailure(caught);
                IndicatorCriteria criteria = getView().getIndicatorCriteria();
                retrieveIndicators(criteria);
            }

            @Override
            public void onWaitSuccess(EnableNotifyPopulationErrorsResult result) {
                fireSuccessMessage(getMessages().indicatorEnabledNotifyPopulationErrors());
                IndicatorCriteria criteria = getView().getIndicatorCriteria();
                retrieveIndicators(criteria);
            }
        });
    }

    @Override
    public void disableNotifyPopulationErrors(List<String> uuids) {
        dispatcher.execute(new DisableNotifyPopulationErrorsAction(uuids), new WaitingAsyncCallbackHandlingError<DisableNotifyPopulationErrorsResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                super.onWaitFailure(caught);
                IndicatorCriteria criteria = getView().getIndicatorCriteria();
                retrieveIndicators(criteria);
            }

            @Override
            public void onWaitSuccess(DisableNotifyPopulationErrorsResult result) {
                fireSuccessMessage(getMessages().indicatorDisabledNotifyPopulationErrors());
                IndicatorCriteria criteria = getView().getIndicatorCriteria();
                retrieveIndicators(criteria);
            }
        });
    }

    //
    // EXTERNAL RESOURCES
    //

    @Override
    public void retrieveItemSchemes(final String formItemName, SrmExternalResourceRestCriteria srmItemSchemeRestCriteria, TypeExternalArtefactsEnum[] types, int firstResult, int maxResults) {
        // without implement
    }

    @Override
    public void retrieveItemSchemes(final String formItemName, SrmExternalResourceRestCriteria srmItemSchemeRestCriteria, int firstResult, int maxResults) {
        // without implement
    }

    @Override
    public void retrieveItems(final String formItemName, SrmItemRestCriteria itemWebCriteria, TypeExternalArtefactsEnum[] types, int firstResult, int maxResults) {
        itemWebCriteria = RestWebCriteriaUtils.buildItemWebCriteria(itemWebCriteria, types);
        retrieveItems(formItemName, itemWebCriteria, firstResult, maxResults);
    }

    @Override
    public void retrieveItems(final String formItemName, SrmItemRestCriteria itemWebCriteria, int firstResult, int maxResults) {
        dispatcher.execute(new GetExternalResourcesAction(itemWebCriteria, firstResult, maxResults), new WaitingAsyncCallbackHandlingError<GetExternalResourcesResult>(this) {

            @Override
            public void onWaitSuccess(GetExternalResourcesResult result) {
                getView().setItems(formItemName, result.getExternalItemsResult());
            }
        });
    }

    //
    // NAVIGATION
    //

    @Override
    public void goTo(List<PlaceRequest> location) {
        // without implement
    }
}
