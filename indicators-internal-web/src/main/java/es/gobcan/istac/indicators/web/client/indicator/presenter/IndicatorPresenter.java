package es.gobcan.istac.indicators.web.client.indicator.presenter;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.Window;
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
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

import es.gobcan.istac.indicators.core.dto.DataDefinitionDto;
import es.gobcan.istac.indicators.core.dto.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.DataStructureDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.core.dto.SubjectDto;
import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorProcStatusEnum;
import es.gobcan.istac.indicators.core.navigation.shared.NameTokens;
import es.gobcan.istac.indicators.core.navigation.shared.PlaceRequestParams;
import es.gobcan.istac.indicators.web.client.LoggedInGatekeeper;
import es.gobcan.istac.indicators.web.client.enums.EnvironmentTypeEnum;
import es.gobcan.istac.indicators.web.client.enums.IndicatorCalculationTypeEnum;
import es.gobcan.istac.indicators.web.client.enums.RateDerivationTypeEnum;
import es.gobcan.istac.indicators.web.client.main.presenter.MainPagePresenter;
import es.gobcan.istac.indicators.web.client.main.presenter.ToolStripPresenterWidget;
import es.gobcan.istac.indicators.web.shared.ArchiveIndicatorAction;
import es.gobcan.istac.indicators.web.shared.ArchiveIndicatorResult;
import es.gobcan.istac.indicators.web.shared.DeleteDataSourcesAction;
import es.gobcan.istac.indicators.web.shared.DeleteDataSourcesResult;
import es.gobcan.istac.indicators.web.shared.DisableNotifyPopulationErrorsAction;
import es.gobcan.istac.indicators.web.shared.DisableNotifyPopulationErrorsResult;
import es.gobcan.istac.indicators.web.shared.EnableNotifyPopulationErrorsAction;
import es.gobcan.istac.indicators.web.shared.EnableNotifyPopulationErrorsResult;
import es.gobcan.istac.indicators.web.shared.FindDataDefinitionsByOperationCodeAction;
import es.gobcan.istac.indicators.web.shared.FindDataDefinitionsByOperationCodeResult;
import es.gobcan.istac.indicators.web.shared.FindIndicatorsAction;
import es.gobcan.istac.indicators.web.shared.FindIndicatorsResult;
import es.gobcan.istac.indicators.web.shared.GetDataDefinitionsOperationsCodesAction;
import es.gobcan.istac.indicators.web.shared.GetDataDefinitionsOperationsCodesResult;
import es.gobcan.istac.indicators.web.shared.GetDataSourcesListAction;
import es.gobcan.istac.indicators.web.shared.GetDataSourcesListResult;
import es.gobcan.istac.indicators.web.shared.GetDataStructureAction;
import es.gobcan.istac.indicators.web.shared.GetDataStructureResult;
import es.gobcan.istac.indicators.web.shared.GetEditionLanguagesAction;
import es.gobcan.istac.indicators.web.shared.GetEditionLanguagesResult;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValueAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValueResult;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesAction.Builder;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorByCodeAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorByCodeResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorPreviewDiffusionUrlAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorPreviewDiffusionUrlResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorPreviewProductionUrlAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorPreviewProductionUrlResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorResult;
import es.gobcan.istac.indicators.web.shared.GetQueriesPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetQueriesPaginatedListResult;
import es.gobcan.istac.indicators.web.shared.GetStatisticalOperationsPaginatedListAction;
import es.gobcan.istac.indicators.web.shared.GetStatisticalOperationsPaginatedListResult;
import es.gobcan.istac.indicators.web.shared.GetSubjectsListAction;
import es.gobcan.istac.indicators.web.shared.GetSubjectsListResult;
import es.gobcan.istac.indicators.web.shared.GetUnitMultipliersAction;
import es.gobcan.istac.indicators.web.shared.GetUnitMultipliersResult;
import es.gobcan.istac.indicators.web.shared.PlanifyPopulateIndicatorDataAction;
import es.gobcan.istac.indicators.web.shared.PlanifyPopulateIndicatorDataResult;
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
import es.gobcan.istac.indicators.web.shared.criteria.QueryWebCriteria;

public class IndicatorPresenter extends Presenter<IndicatorPresenter.IndicatorView, IndicatorPresenter.IndicatorProxy> implements IndicatorUiHandler {

    private Logger                   logger = Logger.getLogger(IndicatorPresenter.class.getName());

    private DispatchAsync            dispatcher;
    private String                   indicatorCode;
    private IndicatorDto             indicatorDto;
    private List<DataSourceDto>      datasourcesDtos;

    private List<UnitMultiplierDto>  unitMultiplierDtos;

    private ToolStripPresenterWidget toolStripPresenterWidget;

    @ProxyCodeSplit
    @NameToken(NameTokens.indicatorPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface IndicatorProxy extends Proxy<IndicatorPresenter>, Place {
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentToolBar = new Type<RevealContentHandler<?>>();

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

        void setGeographicalValues(List<GeographicalValueDto> geographicalValueDtos);

        void setGeographicalValue(GeographicalValueDto geographicalValueDto);

        // Data source

        void setDataDefinitionsOperationCodes(List<String> operationCodes);

        void setDataDefinitions(List<DataDefinitionDto> dataDefinitionDtos);

        void setDataStructure(DataStructureDto dataStructureDto);

        void setDataStructureForEdition(DataStructureDto dataStructureDto);

        void setGeographicalValuesDS(List<GeographicalValueDto> geographicalValueDtos);

        void setGeographicalValueDS(GeographicalValueDto geographicalValueDto);

        void onDataSourceSaved(DataSourceDto dataSourceDto);

        void setRateIndicators(List<IndicatorSummaryDto> indicatorDtos, RateDerivationTypeEnum rateDerivationTypeEnum, IndicatorCalculationTypeEnum indicatorCalculationTypeEnum);

        void setRateIndicator(IndicatorDto indicatorDto, RateDerivationTypeEnum rateDerivationTypeEnum, IndicatorCalculationTypeEnum indicatorCalculationTypeEnum);

        void setUnitMultipliers(List<UnitMultiplierDto> unitMultiplierDtos);

        void updateVisibilityNotifyPopulateErrors(Boolean notifyPopulationErrors);

        void setHasDiffusionIndicatorDatasources(boolean hasDatasources);

        void setQueriesForRelatedQuery(GetQueriesPaginatedListResult result);

        void setStatisticalOperationsForQuerySelection(List<ExternalItemDto> operationsList);

        void showInformationMessage(String title, String message);

        void setEditionLanguages(List<String> languages);
    }

    @Inject
    public IndicatorPresenter(EventBus eventBus, IndicatorView view, IndicatorProxy proxy, DispatchAsync dispatcher, ToolStripPresenterWidget toolStripPresenterWidget) {
        super(eventBus, view, proxy);
        this.dispatcher = dispatcher;
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
        indicatorCode = request.getParameter(PlaceRequestParams.indicatorParam, null);
        indicatorDto = null;
        retrieveEditionLanguages();
    }

    @Override
    protected void onReset() {
        super.onReset();
        SetTitleEvent.fire(IndicatorPresenter.this, getConstants().indicators());
        retrieveIndicatorByCode();
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        setInSlot(TYPE_SetContextAreaContentToolBar, toolStripPresenterWidget);
    }

    private void retrieveIndicatorByCode() {
        dispatcher.execute(new GetIndicatorByCodeAction(indicatorCode, null), new WaitingAsyncCallbackHandlingError<GetIndicatorByCodeResult>(this) {

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
        dispatcher.execute(new GetIndicatorByCodeAction(indicatorCode, versionNumber), new WaitingAsyncCallbackHandlingError<GetIndicatorByCodeResult>(this) {

            @Override
            public void onWaitSuccess(GetIndicatorByCodeResult result) {
                getView().setDiffusionIndicator(result.getIndicator());
            }
        });
    }

    @Override
    public void saveIndicator(IndicatorDto indicator) {
        dispatcher.execute(new UpdateIndicatorAction(indicator), new WaitingAsyncCallbackHandlingError<UpdateIndicatorResult>(this) {

            @Override
            public void onWaitSuccess(UpdateIndicatorResult result) {
                indicatorDto = result.getIndicatorDto();
                getView().setIndicator(indicatorDto);
                fireSuccessMessage(getMessages().indicatorSaved());
            }
        });
    }

    @Override
    public void retrieveSubjects() {
        dispatcher.execute(new GetSubjectsListAction(), new WaitingAsyncCallbackHandlingError<GetSubjectsListResult>(this) {

            @Override
            public void onWaitSuccess(GetSubjectsListResult result) {
                getView().setSubjectsList(result.getSubjectDtos());
            }
        });
    }

    @Override
    public void retrieveGeographicalValuesByGranularity(final String geographicalGranularityUuid) {
        Builder builder = new Builder();
        GetGeographicalValuesAction action = builder.geographicalGranularityUuid(geographicalGranularityUuid).build();
        dispatcher.execute(action, new WaitingAsyncCallbackHandlingError<GetGeographicalValuesResult>(this) {

            @Override
            public void onWaitSuccess(GetGeographicalValuesResult result) {
                getView().setGeographicalValues(result.getGeographicalValueDtos());
            }
        });
    }

    @Override
    public void retrieveGeographicalValue(final String geographicalValueUuid) {
        dispatcher.execute(new GetGeographicalValueAction(geographicalValueUuid), new WaitingAsyncCallbackHandlingError<GetGeographicalValueResult>(this) {

            @Override
            public void onWaitSuccess(GetGeographicalValueResult result) {
                getView().setGeographicalValue(result.getGeographicalValueDto());
            }
        });
    }

    @Override
    public void sendToProductionValidation(final String uuid) {
        dispatcher.execute(new SendIndicatorToProductionValidationAction(uuid), new WaitingAsyncCallbackHandlingError<SendIndicatorToProductionValidationResult>(this) {

            @Override
            public void onWaitSuccess(SendIndicatorToProductionValidationResult result) {
                fireSuccessMessage(getMessages().indicatorSentToProductionValidation());
                indicatorDto = result.getIndicatorDto();
                getView().setIndicator(indicatorDto);
            }
        });
    }

    @Override
    public void sendToDiffusionValidation(final String uuid) {
        dispatcher.execute(new SendIndicatorToDiffusionValidationAction(uuid), new WaitingAsyncCallbackHandlingError<SendIndicatorToDiffusionValidationResult>(this) {

            @Override
            public void onWaitSuccess(SendIndicatorToDiffusionValidationResult result) {
                fireSuccessMessage(getMessages().indicatorSentToDiffusionValidation());
                indicatorDto = result.getIndicatorDto();
                getView().setIndicator(indicatorDto);
            }
        });
    }

    @Override
    public void rejectValidation(final IndicatorDto indicatorDto) {
        if (IndicatorProcStatusEnum.PRODUCTION_VALIDATION.equals(indicatorDto.getProcStatus())) {
            dispatcher.execute(new RejectIndicatorProductionValidationAction(indicatorDto.getUuid()), new WaitingAsyncCallbackHandlingError<RejectIndicatorProductionValidationResult>(this) {

                @Override
                public void onWaitSuccess(RejectIndicatorProductionValidationResult result) {
                    fireSuccessMessage(getMessages().indicatorValidationRejected());
                    IndicatorPresenter.this.indicatorDto = result.getIndicatorDto();
                    getView().setIndicator(result.getIndicatorDto());
                }
            });
        } else if (IndicatorProcStatusEnum.DIFFUSION_VALIDATION.equals(indicatorDto.getProcStatus())) {
            dispatcher.execute(new RejectIndicatorDiffusionValidationAction(indicatorDto.getUuid()), new WaitingAsyncCallbackHandlingError<RejectIndicatorDiffusionValidationResult>(this) {

                @Override
                public void onWaitSuccess(RejectIndicatorDiffusionValidationResult result) {
                    fireSuccessMessage(getMessages().indicatorValidationRejected());
                    IndicatorPresenter.this.indicatorDto = result.getIndicatorDto();
                    getView().setIndicator(result.getIndicatorDto());
                }
            });
        }
    }

    @Override
    public void publish(final String uuid) {
        dispatcher.execute(new PublishIndicatorAction(uuid), new WaitingAsyncCallbackHandlingError<PublishIndicatorResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                super.onWaitFailure(caught);
                logger.log(Level.SEVERE, "Error publishing indicator with uuid  = " + uuid);
                retrieveIndicatorByCode();
            }

            @Override
            public void onWaitSuccess(PublishIndicatorResult result) {
                fireSuccessMessage(getMessages().indicatorPublished());
                indicatorDto = result.getIndicatorDto();
                getView().setIndicator(result.getIndicatorDto());
            }
        });
    }

    @Override
    public void archive(final String uuid) {
        dispatcher.execute(new ArchiveIndicatorAction(uuid), new WaitingAsyncCallbackHandlingError<ArchiveIndicatorResult>(this) {

            @Override
            public void onWaitSuccess(ArchiveIndicatorResult result) {
                fireSuccessMessage(getMessages().indicatorArchived());
                indicatorDto = result.getIndicatorDto();
                getView().setIndicator(result.getIndicatorDto());
            }
        });
    }

    @Override
    public void versioningIndicator(String uuid, VersionTypeEnum versionType) {
        dispatcher.execute(new VersioningIndicatorAction(uuid, versionType), new WaitingAsyncCallbackHandlingError<VersioningIndicatorResult>(this) {

            @Override
            public void onWaitSuccess(VersioningIndicatorResult result) {
                fireSuccessMessage(getMessages().indicatorVersioned());
                indicatorDto = result.getIndicatorDto();
                getView().setIndicator(indicatorDto);
                retrieveDataSources(indicatorDto.getUuid(), indicatorDto.getVersionNumber()); // Reload data sources
            }
        });
    }

    private void retrieveDataSources(final String indicatorUuid, final String indicatorVersion) {
        dispatcher.execute(new GetDataSourcesListAction(indicatorUuid, indicatorVersion), new WaitingAsyncCallbackHandlingError<GetDataSourcesListResult>(this) {

            @Override
            public void onWaitSuccess(GetDataSourcesListResult result) {
                datasourcesDtos = result.getDataSourceDtos();
                getView().setIndicatorDataSources(result.getDataSourceDtos());
            }
        });
    }

    @Override
    public boolean hasDatasources() {
        return datasourcesDtos != null && !datasourcesDtos.isEmpty();
    }

    @Override
    public void hasDiffusionIndicatorDatasources(String indicatorUuid, String indicatorVersion) {
        dispatcher.execute(new GetDataSourcesListAction(indicatorUuid, indicatorVersion), new WaitingAsyncCallbackHandlingError<GetDataSourcesListResult>(this) {

            @Override
            public void onWaitSuccess(GetDataSourcesListResult result) {
                List<DataSourceDto> datasourcesDtos = result.getDataSourceDtos();
                getView().setHasDiffusionIndicatorDatasources(datasourcesDtos != null && !datasourcesDtos.isEmpty());
            }
        });
    }

    @Override
    public void retrieveDataDefinitionsByOperationCode(final String operationCode) {
        dispatcher.execute(new FindDataDefinitionsByOperationCodeAction(operationCode), new WaitingAsyncCallbackHandlingError<FindDataDefinitionsByOperationCodeResult>(this) {

            @Override
            public void onWaitSuccess(FindDataDefinitionsByOperationCodeResult result) {
                getView().setDataDefinitions(result.getDataDefinitionDtos());
            }
        });
    }

    @Override
    public void retrieveDataStructure(String uuid) {
        dispatcher.execute(new GetDataStructureAction(uuid), new WaitingAsyncCallbackHandlingError<GetDataStructureResult>(this) {

            @Override
            public void onWaitSuccess(GetDataStructureResult result) {
                getView().setDataStructure(result.getDataStructureDto());
            }
        });
    }

    @Override
    public void retrieveDataStructureEdition(String uuid) {
        dispatcher.execute(new GetDataStructureAction(uuid), new WaitingAsyncCallbackHandlingError<GetDataStructureResult>(this) {

            @Override
            public void onWaitSuccess(GetDataStructureResult result) {
                getView().setDataStructureForEdition(result.getDataStructureDto());
            }
        });
    }

    @Override
    public void retrieveStatisticalOperationsForQuerySelection() {
        dispatcher.execute(new GetStatisticalOperationsPaginatedListAction(0, Integer.MAX_VALUE, null), new WaitingAsyncCallbackHandlingError<GetStatisticalOperationsPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetStatisticalOperationsPaginatedListResult result) {
                getView().setStatisticalOperationsForQuerySelection(result.getOperationsList());
            }
        });
    }

    @Override
    public void retrieveQueriesForRelatedQuery(int firstResult, int maxResults, QueryWebCriteria criteria) {
        dispatcher.execute(new GetQueriesPaginatedListAction(firstResult, maxResults, criteria), new WaitingAsyncCallbackHandlingError<GetQueriesPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetQueriesPaginatedListResult result) {
                getView().setQueriesForRelatedQuery(result);
            }
        });
    }

    @Override
    public void retrieveGeographicalValueDS(final String uuid) {
        dispatcher.execute(new GetGeographicalValueAction(uuid), new WaitingAsyncCallbackHandlingError<GetGeographicalValueResult>(this) {

            @Override
            public void onWaitSuccess(GetGeographicalValueResult result) {
                getView().setGeographicalValueDS(result.getGeographicalValueDto());
            }
        });
    }

    @Override
    public void saveDataSource(final String indicatorUuid, final DataSourceDto dataSourceDto) {
        dispatcher.execute(new SaveDataSourceAction(indicatorUuid, dataSourceDto), new WaitingAsyncCallbackHandlingError<SaveDataSourceResult>(this) {

            @Override
            public void onWaitSuccess(SaveDataSourceResult result) {
                fireSuccessMessage(getMessages().dataSourceSaved());
                getView().onDataSourceSaved(result.getDataSourceDto());

                // Update indicator every time data sources are modified
                retrieveUpdatedIndicador();
            }
        });
    }

    @Override
    public void deleteDataSource(List<String> uuids) {
        dispatcher.execute(new DeleteDataSourcesAction(uuids), new WaitingAsyncCallbackHandlingError<DeleteDataSourcesResult>(this) {

            @Override
            public void onWaitSuccess(DeleteDataSourcesResult result) {
                retrieveDataSources(indicatorDto.getUuid(), indicatorDto.getVersionNumber());
                fireSuccessMessage(getMessages().dataSourcesDeleted());

                // Update indicator every time data sources are modified
                retrieveUpdatedIndicador();
            }
        });
    }

    private void retrieveUpdatedIndicador() {
        dispatcher.execute(new GetIndicatorByCodeAction(indicatorCode, null), new WaitingAsyncCallbackHandlingError<GetIndicatorByCodeResult>(this) {

            @Override
            public void onWaitSuccess(GetIndicatorByCodeResult result) {
                indicatorDto = result.getIndicator();
                getView().setIndicator(indicatorDto);
            }
        });
    }

    @Override
    public void populateData(String uuid) {
        dispatcher.execute(new PlanifyPopulateIndicatorDataAction(uuid), new WaitingAsyncCallbackHandlingError<PlanifyPopulateIndicatorDataResult>(this) {

            @Override
            public void onWaitSuccess(PlanifyPopulateIndicatorDataResult result) {
                // Indicator must be reloaded to show 'task on background' message
                indicatorDto = result.getIndicatorDto();
                getView().setIndicator(indicatorDto);
                retrieveDataSources(indicatorDto.getUuid(), indicatorDto.getVersionNumber()); // Reload data sources

                getView().showInformationMessage(getMessages().indicatorPopulateData(), getMessages().indicatorPopulateDataInProgress());
            }
        });
    }

    @Override
    public void searchIndicatorsQuantityDenominator(IndicatorCriteria criteria) {
        dispatcher.execute(new FindIndicatorsAction(criteria), new WaitingAsyncCallbackHandlingError<FindIndicatorsResult>(this) {

            @Override
            public void onWaitSuccess(FindIndicatorsResult result) {
                getView().setIndicatorListQuantityDenominator(result.getIndicatorDtos());
            }
        });
    }

    @Override
    public void searchIndicatorsQuantityNumerator(IndicatorCriteria criteria) {
        dispatcher.execute(new FindIndicatorsAction(criteria), new WaitingAsyncCallbackHandlingError<FindIndicatorsResult>(this) {

            @Override
            public void onWaitSuccess(FindIndicatorsResult result) {
                getView().setIndicatorListQuantityNumerator(result.getIndicatorDtos());
            }
        });
    }

    @Override
    public void searchIndicatorsQuantityIndicatorBase(IndicatorCriteria criteria) {
        dispatcher.execute(new FindIndicatorsAction(criteria), new WaitingAsyncCallbackHandlingError<FindIndicatorsResult>(this) {

            @Override
            public void onWaitSuccess(FindIndicatorsResult result) {
                getView().setIndicatorListQuantityIndicatorBase(result.getIndicatorDtos());
            }
        });
    }

    @Override
    public void retrieveQuantityDenominatorIndicator(String indicatorUuid) {
        dispatcher.execute(new GetIndicatorAction(indicatorUuid), new WaitingAsyncCallbackHandlingError<GetIndicatorResult>(this) {

            @Override
            public void onWaitSuccess(GetIndicatorResult result) {
                getView().setIndicatorQuantityDenominator(result.getIndicator());
            }
        });
    }

    @Override
    public void retrieveQuantityNumeratorIndicator(String indicatorUuid) {
        dispatcher.execute(new GetIndicatorAction(indicatorUuid), new WaitingAsyncCallbackHandlingError<GetIndicatorResult>(this) {

            @Override
            public void onWaitSuccess(GetIndicatorResult result) {
                getView().setIndicatorQuantityNumerator(result.getIndicator());
            }
        });
    }

    @Override
    public void retrieveQuantityIndicatorBase(String indicatorUuid) {
        dispatcher.execute(new GetIndicatorAction(indicatorUuid), new WaitingAsyncCallbackHandlingError<GetIndicatorResult>(this) {

            @Override
            public void onWaitSuccess(GetIndicatorResult result) {
                getView().setIndicatorQuantityIndicatorBase(result.getIndicator());
            }
        });
    }

    @Override
    public void retrieveDataDefinitionsOperationsCodes() {
        dispatcher.execute(new GetDataDefinitionsOperationsCodesAction(), new WaitingAsyncCallbackHandlingError<GetDataDefinitionsOperationsCodesResult>(this) {

            @Override
            public void onWaitSuccess(GetDataDefinitionsOperationsCodesResult result) {
                getView().setDataDefinitionsOperationCodes(result.getOperationCodes());
            }
        });
    }

    @Override
    public void searchRateIndicators(IndicatorCriteria criteria, final RateDerivationTypeEnum rateDerivationTypeEnum, final IndicatorCalculationTypeEnum indicatorCalculationTypeEnum) {
        dispatcher.execute(new FindIndicatorsAction(criteria), new WaitingAsyncCallbackHandlingError<FindIndicatorsResult>(this) {

            @Override
            public void onWaitSuccess(FindIndicatorsResult result) {
                getView().setRateIndicators(result.getIndicatorDtos(), rateDerivationTypeEnum, indicatorCalculationTypeEnum);
            }
        });
    }

    @Override
    public void retrieveRateIndicator(String indicatorUuid, final RateDerivationTypeEnum rateDerivationTypeEnum, final IndicatorCalculationTypeEnum indicatorCalculationTypeEnum) {
        dispatcher.execute(new GetIndicatorAction(indicatorUuid), new WaitingAsyncCallbackHandlingError<GetIndicatorResult>(this) {

            @Override
            public void onWaitSuccess(GetIndicatorResult result) {
                getView().setRateIndicator(result.getIndicator(), rateDerivationTypeEnum, indicatorCalculationTypeEnum);
            }
        });
    }

    @Override
    public void retrieveUnitMultipliers() {
        if (unitMultiplierDtos == null) {
            dispatcher.execute(new GetUnitMultipliersAction(), new WaitingAsyncCallbackHandlingError<GetUnitMultipliersResult>(this) {

                @Override
                public void onWaitSuccess(GetUnitMultipliersResult result) {
                    unitMultiplierDtos = result.getUnitMultiplierDtos();
                    setUnitMultipliers();
                }
            });
        } else {
            setUnitMultipliers();
        }
    }

    @Override
    public void previewData(String code, EnvironmentTypeEnum environmentType) {
        if (EnvironmentTypeEnum.PRODUCTION.equals(environmentType)) {
            dispatcher.execute(new GetIndicatorPreviewProductionUrlAction(code), new WaitingAsyncCallbackHandlingError<GetIndicatorPreviewProductionUrlResult>(this) {

                @Override
                public void onWaitSuccess(GetIndicatorPreviewProductionUrlResult result) {
                    Window.open(result.getUrl(), "_blank", "");
                }
            });
        } else if (EnvironmentTypeEnum.DIFFUSION.equals(environmentType)) {
            dispatcher.execute(new GetIndicatorPreviewDiffusionUrlAction(code), new WaitingAsyncCallbackHandlingError<GetIndicatorPreviewDiffusionUrlResult>(this) {

                @Override
                public void onWaitSuccess(GetIndicatorPreviewDiffusionUrlResult result) {
                    Window.open(result.getUrl(), "_blank", "");
                }
            });
        }
    }

    private void setUnitMultipliers() {
        getView().setUnitMultipliers(unitMultiplierDtos);
    }

    @Override
    public void enableNotifyPopulationErrors(final String uuid) {
        dispatcher.execute(new EnableNotifyPopulationErrorsAction(Arrays.asList(uuid)), new WaitingAsyncCallbackHandlingError<EnableNotifyPopulationErrorsResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                super.onWaitFailure(caught);
                logger.log(Level.SEVERE, "Error enabling notify population errors for indicator with uuid  = " + uuid);
                retrieveIndicatorByCode();
            }

            @Override
            public void onWaitSuccess(EnableNotifyPopulationErrorsResult result) {
                fireSuccessMessage(getMessages().indicatorEnabledNotifyPopulationErrors());
                getView().updateVisibilityNotifyPopulateErrors(true);
            }
        });
    }

    @Override
    public void disableNotifyPopulationErrors(final String uuid) {
        dispatcher.execute(new DisableNotifyPopulationErrorsAction(Arrays.asList(uuid)), new WaitingAsyncCallbackHandlingError<DisableNotifyPopulationErrorsResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                super.onWaitFailure(caught);
                logger.log(Level.SEVERE, "Error disabling notify population errors for indicator with uuid  = " + uuid);
                retrieveIndicatorByCode();
            }

            @Override
            public void onWaitSuccess(DisableNotifyPopulationErrorsResult result) {
                fireSuccessMessage(getMessages().indicatorDisabledNotifyPopulationErrors());
                getView().updateVisibilityNotifyPopulateErrors(false);
            }
        });
    }

    @Override
    public void retrieveEditionLanguages() {
        dispatcher.execute(new GetEditionLanguagesAction(), new WaitingAsyncCallbackHandlingError<GetEditionLanguagesResult>(this) {

            @Override
            public void onWaitSuccess(GetEditionLanguagesResult result) {
                getView().setEditionLanguages(result.getLanguages());
            }
        });
    }

}
