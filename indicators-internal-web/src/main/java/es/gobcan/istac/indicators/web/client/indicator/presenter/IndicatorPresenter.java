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
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import es.gobcan.istac.indicators.core.dto.DataDefinitionDto;
import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.DataStructureDto;
import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.core.dto.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.SubjectDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorProcStatusEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;
import es.gobcan.istac.indicators.web.client.NameTokens;
import es.gobcan.istac.indicators.web.client.PlaceRequestParams;
import es.gobcan.istac.indicators.web.client.events.UpdateGeographicalGranularitiesEvent;
import es.gobcan.istac.indicators.web.client.events.UpdateGeographicalGranularitiesEvent.UpdateGeographicalGranularitiesHandler;
import es.gobcan.istac.indicators.web.client.events.UpdateQuantityUnitsEvent;
import es.gobcan.istac.indicators.web.client.events.UpdateQuantityUnitsEvent.UpdateQuantityUnitsHandler;
import es.gobcan.istac.indicators.web.client.main.presenter.MainPagePresenter;
import es.gobcan.istac.indicators.web.client.utils.ErrorUtils;
import es.gobcan.istac.indicators.web.client.widgets.WaitingAsyncCallback;
import es.gobcan.istac.indicators.web.shared.ArchiveIndicatorAction;
import es.gobcan.istac.indicators.web.shared.ArchiveIndicatorResult;
import es.gobcan.istac.indicators.web.shared.DeleteDataSourcesAction;
import es.gobcan.istac.indicators.web.shared.DeleteDataSourcesResult;
import es.gobcan.istac.indicators.web.shared.FindDataDefinitionsByOperationCodeAction;
import es.gobcan.istac.indicators.web.shared.FindDataDefinitionsByOperationCodeResult;
import es.gobcan.istac.indicators.web.shared.FindIndicatorsAction;
import es.gobcan.istac.indicators.web.shared.FindIndicatorsResult;
import es.gobcan.istac.indicators.web.shared.GetDataDefinitionAction;
import es.gobcan.istac.indicators.web.shared.GetDataDefinitionResult;
import es.gobcan.istac.indicators.web.shared.GetDataDefinitionsOperationsCodesAction;
import es.gobcan.istac.indicators.web.shared.GetDataDefinitionsOperationsCodesResult;
import es.gobcan.istac.indicators.web.shared.GetDataSourcesListAction;
import es.gobcan.istac.indicators.web.shared.GetDataSourcesListResult;
import es.gobcan.istac.indicators.web.shared.GetDataStructureAction;
import es.gobcan.istac.indicators.web.shared.GetDataStructureResult;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValueAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValueResult;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorByCodeAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorByCodeResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorResult;
import es.gobcan.istac.indicators.web.shared.GetSubjectsListAction;
import es.gobcan.istac.indicators.web.shared.GetSubjectsListResult;
import es.gobcan.istac.indicators.web.shared.PopulateIndicatorDataAction;
import es.gobcan.istac.indicators.web.shared.PopulateIndicatorDataResult;
import es.gobcan.istac.indicators.web.shared.PublishIndicatorAction;
import es.gobcan.istac.indicators.web.shared.PublishIndicatorResult;
import es.gobcan.istac.indicators.web.shared.RejectIndicatorDiffusionValidationAction;
import es.gobcan.istac.indicators.web.shared.RejectIndicatorDiffusionValidationResult;
import es.gobcan.istac.indicators.web.shared.RejectIndicatorProductionValidationAction;
import es.gobcan.istac.indicators.web.shared.RejectIndicatorProductionValidationResult;
import es.gobcan.istac.indicators.web.shared.SaveDataSourceAction;
import es.gobcan.istac.indicators.web.shared.SaveDataSourceResult;
import es.gobcan.istac.indicators.web.shared.SendIndicatorToDiffusionValidationAction;
import es.gobcan.istac.indicators.web.shared.SendIndicatorToDiffusionValidationResult;
import es.gobcan.istac.indicators.web.shared.SendIndicatorToProductionValidationAction;
import es.gobcan.istac.indicators.web.shared.SendIndicatorToProductionValidationResult;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorAction;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorResult;
import es.gobcan.istac.indicators.web.shared.VersioningIndicatorAction;
import es.gobcan.istac.indicators.web.shared.VersioningIndicatorResult;
import es.gobcan.istac.indicators.web.shared.criteria.IndicatorCriteria;

public class IndicatorPresenter extends Presenter<IndicatorPresenter.IndicatorView, IndicatorPresenter.IndicatorProxy>
        implements
            IndicatorUiHandler,
            UpdateQuantityUnitsHandler,
            UpdateGeographicalGranularitiesHandler {

    private Logger        logger = Logger.getLogger(IndicatorPresenter.class.getName());

    private DispatchAsync dispatcher;
    private String        indicatorCode;
    private IndicatorDto  indicatorDto;

    @ProxyCodeSplit
    @NameToken(NameTokens.indicatorPage)
    public interface IndicatorProxy extends Proxy<IndicatorPresenter>, Place {
    }

    public interface IndicatorView extends View, HasUiHandlers<IndicatorPresenter> {

        // Indicator

        void setIndicator(IndicatorDto indicator);
        void setIndicatorDataSources(List<DataSourceDto> dataSourceDtos);
        void setDiffusionIndicator(IndicatorDto indicator);

        void setIndicatorListQuantityDenominator(List<IndicatorSummaryDto> indicators);
        void setIndicatorListQuantityNumerator(List<IndicatorSummaryDto> indicators);
        void setIndicatorListQuantityIndicatorBase(List<IndicatorSummaryDto> indicators);

        void setIndicatorQuantityDenominator(IndicatorDto indicator);
        void setIndicatorQuantityNumerator(IndicatorDto indicator);
        void setIndicatorQuantityIndicatorBase(IndicatorDto indicator);

        void setSubjectsList(List<SubjectDto> subjectDtos);
        void setQuantityUnits(List<QuantityUnitDto> units);
        void setGeographicalGranularities(List<GeographicalGranularityDto> granularityDtos);
        void setGeographicalValues(List<GeographicalValueDto> geographicalValueDtos);
        void setGeographicalValue(GeographicalValueDto geographicalValueDto);

        // Data source

        void setDataDefinitionsOperationCodes(List<String> operationCodes);
        void setDataDefinitions(List<DataDefinitionDto> dataDefinitionDtos);
        void setDataDefinition(DataDefinitionDto dataDefinitionDto);
        void setDataStructureView(DataStructureDto dataStructureDto);
        void setDataStructureEdition(DataStructureDto dataStructureDto);

        void setGeographicalValuesDS(List<GeographicalValueDto> geographicalValueDtos);
        void setGeographicalValueDS(GeographicalValueDto geographicalValueDto);

        void onDataSourceSaved(DataSourceDto dataSourceDto);
    }

    @Inject
    public IndicatorPresenter(EventBus eventBus, IndicatorView view, IndicatorProxy proxy, DispatchAsync dispatcher) {
        super(eventBus, view, proxy);
        this.dispatcher = dispatcher;
        getView().setUiHandlers(this);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.CONTENT_SLOT, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        indicatorCode = request.getParameter(PlaceRequestParams.indicatorParam, null);
        indicatorDto = null;
    }

    @Override
    protected void onReset() {
        super.onReset();
        SetTitleEvent.fire(IndicatorPresenter.this, getConstants().indicators());
        retrieveIndicatorByCode();
    }

    private void retrieveIndicatorByCode() {
        dispatcher.execute(new GetIndicatorByCodeAction(this.indicatorCode, null), new WaitingAsyncCallback<GetIndicatorByCodeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().indicErrorRetrieve()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetIndicatorByCodeResult result) {
                indicatorDto = result.getIndicator();
                getView().setIndicator(indicatorDto);
                retrieveDataSources(indicatorDto.getUuid(), indicatorDto.getVersionNumber());
            }
        });
    }

    @Override
    public void retrieveDiffusionIndicator(String code, String versionNumber) {
        dispatcher.execute(new GetIndicatorByCodeAction(this.indicatorCode, null), new WaitingAsyncCallback<GetIndicatorByCodeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().indicErrorRetrieve()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetIndicatorByCodeResult result) {
                getView().setDiffusionIndicator(indicatorDto);
            }
        });
    }

    public void saveIndicator(IndicatorDto indicator) {
        dispatcher.execute(new UpdateIndicatorAction(indicator), new WaitingAsyncCallback<UpdateIndicatorResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().indicErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateIndicatorResult result) {
                getView().setIndicator(result.getIndicatorDto());
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getMessageList(getMessages().indicatorSaved()), MessageTypeEnum.SUCCESS);
            }
        });
    }

    @ProxyEvent
    @Override
    public void onUpdateQuantityUnits(UpdateQuantityUnitsEvent event) {
        getView().setQuantityUnits(event.getQuantityUnits());
    }

    @Override
    public void retrieveSubjects() {
        dispatcher.execute(new GetSubjectsListAction(), new WaitingAsyncCallback<GetSubjectsListResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRetrievingSubjects()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetSubjectsListResult result) {
                getView().setSubjectsList(result.getSubjectDtos());
            }
        });
    }

    @ProxyEvent
    @Override
    public void onUpdateGeographicalGranularities(UpdateGeographicalGranularitiesEvent event) {
        getView().setGeographicalGranularities(event.getGeographicalGranularities());
    }

    @Override
    public void retrieveGeographicalValues(final String geographicalGranularityUuid) {
        dispatcher.execute(new GetGeographicalValuesAction(geographicalGranularityUuid), new WaitingAsyncCallback<GetGeographicalValuesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error loading geographical values with geographical granularity UUID = " + geographicalGranularityUuid);
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRetrievingGeographicalValues()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetGeographicalValuesResult result) {
                getView().setGeographicalValues(result.getGeographicalValueDtos());
            }
        });
    }

    @Override
    public void retrieveGeographicalValue(final String geographicalValueUuid) {
        dispatcher.execute(new GetGeographicalValueAction(geographicalValueUuid), new WaitingAsyncCallback<GetGeographicalValueResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error loading geographical value with UUID = " + geographicalValueUuid);
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorGeographicalValueNotFound()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetGeographicalValueResult result) {
                getView().setGeographicalValue(result.getGeographicalValueDto());
            }
        });
    }

    @Override
    public void sendToProductionValidation(final String uuid) {
        dispatcher.execute(new SendIndicatorToProductionValidationAction(uuid), new WaitingAsyncCallback<SendIndicatorToProductionValidationResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error sending to production validation indicator with uuid  = " + uuid);
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorSendingIndicatorToProductionValidation()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SendIndicatorToProductionValidationResult result) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getMessageList(getMessages().indicatorSentToProductionValidation()), MessageTypeEnum.SUCCESS);
                getView().setIndicator(result.getIndicatorDto());
            }
        });
    }

    @Override
    public void sendToDiffusionValidation(final String uuid) {
        dispatcher.execute(new SendIndicatorToDiffusionValidationAction(uuid), new WaitingAsyncCallback<SendIndicatorToDiffusionValidationResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error sending to diffusion validation indicator with uuid  = " + uuid);
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorSendingIndicatorToDiffusionValidation()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SendIndicatorToDiffusionValidationResult result) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getMessageList(getMessages().indicatorSentToDiffusionValidation()), MessageTypeEnum.SUCCESS);
                getView().setIndicator(result.getIndicatorDto());
            }
        });
    }

    @Override
    public void rejectValidation(final IndicatorDto indicatorDto) {
        if (IndicatorProcStatusEnum.PRODUCTION_VALIDATION.equals(indicatorDto.getProcStatus())) {
            dispatcher.execute(new RejectIndicatorProductionValidationAction(indicatorDto.getUuid()), new WaitingAsyncCallback<RejectIndicatorProductionValidationResult>() {

                @Override
                public void onWaitFailure(Throwable caught) {
                    logger.log(Level.SEVERE, "Error rejecting validation of indicator with uuid  = " + indicatorDto.getUuid());
                    ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRejectingIndicatorValidation()), MessageTypeEnum.ERROR);
                }
                @Override
                public void onWaitSuccess(RejectIndicatorProductionValidationResult result) {
                    ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getMessageList(getMessages().indicatorValidationRejected()), MessageTypeEnum.SUCCESS);
                    getView().setIndicator(result.getIndicatorDto());
                }
            });
        } else if (IndicatorProcStatusEnum.DIFFUSION_VALIDATION.equals(indicatorDto.getProcStatus())) {
            dispatcher.execute(new RejectIndicatorDiffusionValidationAction(indicatorDto.getUuid()), new WaitingAsyncCallback<RejectIndicatorDiffusionValidationResult>() {

                @Override
                public void onWaitFailure(Throwable caught) {
                    logger.log(Level.SEVERE, "Error rejecting validation of indicator with uuid  = " + indicatorDto.getUuid());
                    ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRejectingIndicatorValidation()), MessageTypeEnum.ERROR);
                }
                @Override
                public void onWaitSuccess(RejectIndicatorDiffusionValidationResult result) {
                    ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getMessageList(getMessages().indicatorValidationRejected()), MessageTypeEnum.SUCCESS);
                    getView().setIndicator(result.getIndicatorDto());
                }
            });
        }
    }

    @Override
    public void publish(final String uuid) {
        dispatcher.execute(new PublishIndicatorAction(uuid), new WaitingAsyncCallback<PublishIndicatorResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error publishing indicator with uuid  = " + uuid);
                retrieveIndicatorByCode();
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorPublishingIndicator()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(PublishIndicatorResult result) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getMessageList(getMessages().indicatorPublished()), MessageTypeEnum.SUCCESS);
                getView().setIndicator(result.getIndicatorDto());
            }
        });
    }

    @Override
    public void archive(final String uuid) {
        dispatcher.execute(new ArchiveIndicatorAction(uuid), new WaitingAsyncCallback<ArchiveIndicatorResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error arhiving indicator with uuid  = " + uuid);
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorArchivingIndicator()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(ArchiveIndicatorResult result) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getMessageList(getMessages().indicatorArchived()), MessageTypeEnum.SUCCESS);
                getView().setIndicator(result.getIndicatorDto());
            }
        });
    }

    @Override
    public void versioningIndicator(String uuid, VersionTypeEnum versionType) {
        dispatcher.execute(new VersioningIndicatorAction(uuid, versionType), new WaitingAsyncCallback<VersioningIndicatorResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorVersioningIndicator()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(VersioningIndicatorResult result) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getMessageList(getMessages().indicatorVersioned()), MessageTypeEnum.SUCCESS);
                getView().setIndicator(result.getIndicatorDto());
            }
        });
    }

    private void retrieveDataSources(final String indicatorUuid, final String indicatorVersion) {
        dispatcher.execute(new GetDataSourcesListAction(indicatorUuid, indicatorVersion), new WaitingAsyncCallback<GetDataSourcesListResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error retrieving data sources of indicator with uuid = " + indicatorUuid + " and version =" + indicatorVersion);
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRetrievingDataSources()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetDataSourcesListResult result) {
                getView().setIndicatorDataSources(result.getDataSourceDtos());
            }
        });
    }

    @Override
    public void retrieveDataDefinitionsByOperationCode(final String operationCode) {
        dispatcher.execute(new FindDataDefinitionsByOperationCodeAction(operationCode), new WaitingAsyncCallback<FindDataDefinitionsByOperationCodeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error retrieving data definitions");
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorFindingDataDefinitions(operationCode)), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(FindDataDefinitionsByOperationCodeResult result) {
                getView().setDataDefinitions(result.getDataDefinitionDtos());
            }
        });
    }

    @Override
    public void retrieveDataStructureView(String uuid) {
        dispatcher.execute(new GetDataStructureAction(uuid), new WaitingAsyncCallback<GetDataStructureResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error retrieving data structure");
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRetrievingDataStructure()), MessageTypeEnum.ERROR);

            }
            @Override
            public void onWaitSuccess(GetDataStructureResult result) {
                getView().setDataStructureView(result.getDataStructureDto());
            }
        });
    }

    @Override
    public void retrieveDataStructureEdition(String uuid) {
        dispatcher.execute(new GetDataStructureAction(uuid), new WaitingAsyncCallback<GetDataStructureResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error retrieving data structure");
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRetrievingDataStructure()), MessageTypeEnum.ERROR);

            }
            @Override
            public void onWaitSuccess(GetDataStructureResult result) {
                getView().setDataStructureEdition(result.getDataStructureDto());
            }
        });
    }

    @Override
    public void retrieveDataDefinition(final String uuid) {
        dispatcher.execute(new GetDataDefinitionAction(uuid), new WaitingAsyncCallback<GetDataDefinitionResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error retrieving data definition with uuid = " + uuid);
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRetrievingDataDefinition()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetDataDefinitionResult result) {
                getView().setDataDefinition(result.getDataDefinitionDto());
            }
        });
    }

    @Override
    public void retrieveGeographicalValueDS(final String uuid) {
        dispatcher.execute(new GetGeographicalValueAction(uuid), new WaitingAsyncCallback<GetGeographicalValueResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error loading geographical value with UUID = " + uuid);
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorGeographicalValueNotFound()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetGeographicalValueResult result) {
                getView().setGeographicalValueDS(result.getGeographicalValueDto());
            }
        });
    }

    @Override
    public void saveDataSource(final String indicatorUuid, final DataSourceDto dataSourceDto) {
        dispatcher.execute(new SaveDataSourceAction(indicatorUuid, dataSourceDto), new WaitingAsyncCallback<SaveDataSourceResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error saving datasource with uuid = " + dataSourceDto.getUuid() + " in indicator with uuid = " + indicatorUuid);
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorSavingDataSource()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SaveDataSourceResult result) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getMessageList(getMessages().dataSourceSaved()), MessageTypeEnum.SUCCESS);
                getView().onDataSourceSaved(result.getDataSourceDto());

                // Update indicator every time data sources are modified
                retrieveUpdatedIndicador();
            }
        });
    }

    @Override
    public void deleteDataSource(List<String> uuids) {
        dispatcher.execute(new DeleteDataSourcesAction(uuids), new WaitingAsyncCallback<DeleteDataSourcesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error deleting datasources");
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().dataSourcesErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteDataSourcesResult result) {
                retrieveDataSources(indicatorDto.getUuid(), indicatorDto.getVersionNumber());
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getMessageList(getMessages().dataSourcesDeleted()), MessageTypeEnum.SUCCESS);

                // Update indicator every time data sources are modified
                retrieveUpdatedIndicador();
            }
        });
    }

    private void retrieveUpdatedIndicador() {
        dispatcher.execute(new GetIndicatorByCodeAction(indicatorCode, null), new WaitingAsyncCallback<GetIndicatorByCodeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().indicErrorRetrieve()), MessageTypeEnum.ERROR);
            }

            @Override
            public void onWaitSuccess(GetIndicatorByCodeResult result) {
                indicatorDto = result.getIndicator();
                getView().setIndicator(indicatorDto);
            }
        });
    }

    @Override
    public void populateData(String uuid, String version) {
        dispatcher.execute(new PopulateIndicatorDataAction(uuid, version), new WaitingAsyncCallback<PopulateIndicatorDataResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorPopulatingIndicatorData()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(PopulateIndicatorDataResult result) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getMessageList(getMessages().indicatorDataPopulated()), MessageTypeEnum.SUCCESS);
            }
        });
    }

    @Override
    public void searchIndicatorsQuantityDenominator(IndicatorCriteria criteria) {
        dispatcher.execute(new FindIndicatorsAction(criteria), new WaitingAsyncCallback<FindIndicatorsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorSearchingIndicators()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(FindIndicatorsResult result) {
                getView().setIndicatorListQuantityDenominator(result.getIndicatorDtos());
            }
        });
    }

    @Override
    public void searchIndicatorsQuantityNumerator(IndicatorCriteria criteria) {
        dispatcher.execute(new FindIndicatorsAction(criteria), new WaitingAsyncCallback<FindIndicatorsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorSearchingIndicators()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(FindIndicatorsResult result) {
                getView().setIndicatorListQuantityNumerator(result.getIndicatorDtos());
            }
        });
    }

    @Override
    public void searchIndicatorsQuantityIndicatorBase(IndicatorCriteria criteria) {
        dispatcher.execute(new FindIndicatorsAction(criteria), new WaitingAsyncCallback<FindIndicatorsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorSearchingIndicators()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(FindIndicatorsResult result) {
                getView().setIndicatorListQuantityIndicatorBase(result.getIndicatorDtos());
            }
        });
    }

    @Override
    public void retrieveQuantityDenominatorIndicator(String indicatorUuid) {
        dispatcher.execute(new GetIndicatorAction(indicatorUuid), new WaitingAsyncCallback<GetIndicatorResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRetrievingIndicator()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetIndicatorResult result) {
                getView().setIndicatorQuantityDenominator(result.getIndicator());
            }
        });
    }

    @Override
    public void retrieveQuantityNumeratorIndicator(String indicatorUuid) {
        dispatcher.execute(new GetIndicatorAction(indicatorUuid), new WaitingAsyncCallback<GetIndicatorResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRetrievingIndicator()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetIndicatorResult result) {
                getView().setIndicatorQuantityNumerator(result.getIndicator());
            }
        });
    }

    @Override
    public void retrieveQuantityIndicatorBase(String indicatorUuid) {
        dispatcher.execute(new GetIndicatorAction(indicatorUuid), new WaitingAsyncCallback<GetIndicatorResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRetrievingIndicator()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetIndicatorResult result) {
                getView().setIndicatorQuantityIndicatorBase(result.getIndicator());
            }
        });
    }

    @Override
    public void retrieveDataDefinitionsOperationsCodes() {
        dispatcher.execute(new GetDataDefinitionsOperationsCodesAction(), new WaitingAsyncCallback<GetDataDefinitionsOperationsCodesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRetrievingDataDefinitionsOperationCodes()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetDataDefinitionsOperationsCodesResult result) {
                getView().setDataDefinitionsOperationCodes(result.getOperationCodes());
            }
        });
    }

}
