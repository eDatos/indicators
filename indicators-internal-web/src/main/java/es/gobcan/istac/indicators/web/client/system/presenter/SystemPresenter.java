package es.gobcan.istac.indicators.web.client.system.presenter;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

import java.util.List;
import java.util.logging.Logger;

import org.siemac.metamac.core.common.enume.domain.IstacTimeGranularityEnum;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
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
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

import es.gobcan.istac.indicators.core.dto.DimensionDto;
import es.gobcan.istac.indicators.core.dto.ElementLevelDto;
import es.gobcan.istac.indicators.core.dto.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.IndicatorSummaryDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemStructureDto;
import es.gobcan.istac.indicators.core.dto.TimeGranularityDto;
import es.gobcan.istac.indicators.core.dto.TimeValueDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemProcStatusEnum;
import es.gobcan.istac.indicators.core.navigation.shared.NameTokens;
import es.gobcan.istac.indicators.core.navigation.shared.PlaceRequestParams;
import es.gobcan.istac.indicators.web.client.LoggedInGatekeeper;
import es.gobcan.istac.indicators.web.client.main.presenter.MainPagePresenter;
import es.gobcan.istac.indicators.web.client.main.presenter.ToolStripPresenterWidget;
import es.gobcan.istac.indicators.web.client.utils.CommonUtils;
import es.gobcan.istac.indicators.web.shared.ArchiveIndicatorsSystemAction;
import es.gobcan.istac.indicators.web.shared.ArchiveIndicatorsSystemResult;
import es.gobcan.istac.indicators.web.shared.CreateDimensionAction;
import es.gobcan.istac.indicators.web.shared.CreateDimensionResult;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorInstanceResult;
import es.gobcan.istac.indicators.web.shared.DeleteDimensionAction;
import es.gobcan.istac.indicators.web.shared.DeleteDimensionResult;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorInstanceResult;
import es.gobcan.istac.indicators.web.shared.ExportSystemInDsplAction;
import es.gobcan.istac.indicators.web.shared.ExportSystemInDsplResult;
import es.gobcan.istac.indicators.web.shared.FindIndicatorsAction;
import es.gobcan.istac.indicators.web.shared.FindIndicatorsResult;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularitiesInIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularitiesInIndicatorResult;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularityAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularityResult;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValueAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValueResult;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesByGranularityInIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesByGranularityInIndicatorResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorInstancePreviewUrlAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorInstancePreviewUrlResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemByCodeAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemByCodeResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemStructureAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemStructureResult;
import es.gobcan.istac.indicators.web.shared.GetTimeGranularitiesInIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetTimeGranularitiesInIndicatorResult;
import es.gobcan.istac.indicators.web.shared.GetTimeValuesByGranularityInIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetTimeValuesByGranularityInIndicatorResult;
import es.gobcan.istac.indicators.web.shared.MoveSystemStructureContentAction;
import es.gobcan.istac.indicators.web.shared.MoveSystemStructureContentResult;
import es.gobcan.istac.indicators.web.shared.PublishIndicatorsSystemAction;
import es.gobcan.istac.indicators.web.shared.PublishIndicatorsSystemResult;
import es.gobcan.istac.indicators.web.shared.RejectIndicatorsSystemDiffusionValidationAction;
import es.gobcan.istac.indicators.web.shared.RejectIndicatorsSystemDiffusionValidationResult;
import es.gobcan.istac.indicators.web.shared.RejectIndicatorsSystemProductionValidationAction;
import es.gobcan.istac.indicators.web.shared.RejectIndicatorsSystemProductionValidationResult;
import es.gobcan.istac.indicators.web.shared.SendIndicatorsSystemToDiffusionValidationAction;
import es.gobcan.istac.indicators.web.shared.SendIndicatorsSystemToDiffusionValidationResult;
import es.gobcan.istac.indicators.web.shared.SendIndicatorsSystemToProductionValidationAction;
import es.gobcan.istac.indicators.web.shared.SendIndicatorsSystemToProductionValidationResult;
import es.gobcan.istac.indicators.web.shared.UpdateDimensionAction;
import es.gobcan.istac.indicators.web.shared.UpdateDimensionResult;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorInstanceResult;
import es.gobcan.istac.indicators.web.shared.VersioningIndicatorsSystemAction;
import es.gobcan.istac.indicators.web.shared.VersioningIndicatorsSystemResult;
import es.gobcan.istac.indicators.web.shared.criteria.IndicatorCriteria;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

public class SystemPresenter extends Presenter<SystemPresenter.SystemView, SystemPresenter.SystemProxy> implements SystemUiHandler {

    private Logger                                    logger         = Logger.getLogger(SystemPresenter.class.getName());

    @ContentSlot
    public static final Type<RevealContentHandler<?>> GENERAL_SLOT   = new Type<RevealContentHandler<?>>();
    @ContentSlot
    public static final Type<RevealContentHandler<?>> STRUCTURE_SLOT = new Type<RevealContentHandler<?>>();

    private PlaceManager                              placeManager;
    private DispatchAsync                             dispatcher;

    private IndicatorsSystemDtoWeb                    indSystem;                                                         // To be able to cache structure

    private ToolStripPresenterWidget                  toolStripPresenterWidget;

    public interface SystemView extends View, HasUiHandlers<SystemUiHandler> {

        void init();

        void setIndicatorFromIndicatorInstance(IndicatorDto indicator);

        void setIndicators(List<IndicatorSummaryDto> indicators);

        void setIndicatorsSystem(IndicatorsSystemDtoWeb indicatorSystem);

        void setDiffusionIndicatorsSystem(IndicatorsSystemDtoWeb indicatorSystem);

        void setIndicatorsSystemStructure(IndicatorsSystemDtoWeb indicatorsSystem, IndicatorsSystemStructureDto structure);

        void onDimensionSaved(DimensionDto dimension);

        void onIndicatorInstanceSaved(IndicatorInstanceDto instance);

        // void onIndicatorDataPopulated(IndicatorDto indicatorDto);

        // Instance
        void setTemporalGranularitiesForIndicator(List<TimeGranularityDto> timeGranularityEnums);

        void setTemporalValuesFormIndicator(List<TimeValueDto> timeValues);

        void setGeographicalGranularitiesForIndicator(List<GeographicalGranularityDto> geographicalGranularityDtos);

        void setGeographicalGranularity(GeographicalGranularityDto geographicalGranularityDto);

        void setGeographicalValuesForIndicator(List<GeographicalValueDto> geographicalValueDtos);

        void setGeographicalValue(GeographicalValueDto geographicalValueDto);

    }

    @ProxyCodeSplit
    @NameToken(NameTokens.systemPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface SystemProxy extends Proxy<SystemPresenter>, Place {
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentToolBar = new Type<RevealContentHandler<?>>();

    @Inject
    public SystemPresenter(EventBus eventBus, SystemView view, SystemProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager, ToolStripPresenterWidget toolStripPresenterWidget) {
        super(eventBus, view, proxy);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
        view.setUiHandlers(this);
        this.toolStripPresenterWidget = toolStripPresenterWidget;
    }

    @Override
    protected void onBind() {
        super.onBind();
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.CONTENT_SLOT, this);
    }

    @Override
    protected void onReset() {
        super.onReset();
        SetTitleEvent.fire(SystemPresenter.this, getConstants().indicatorSystems());
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        setInSlot(TYPE_SetContextAreaContentToolBar, toolStripPresenterWidget);
        String code = getIdFromRequest(placeManager.getCurrentPlaceHierarchy());
        retrieveIndSystem(code);
        getView().init();
    }

    private String getIdFromRequest(List<PlaceRequest> hierarchy) {
        for (PlaceRequest request : hierarchy) {
            if (this.getProxy().getNameToken().equals(request.getNameToken())) {
                return request.getParameter(PlaceRequestParams.indSystemParam, null);
            }
        }
        return null;
    }

    private void setIndicatorsSystem(IndicatorsSystemDtoWeb indicatorsSystem) {
        this.indSystem = indicatorsSystem;
        getView().setIndicatorsSystem(indicatorsSystem);
    }

    @Override
    public void retrieveIndSystem(final String indSystemCode) {
        dispatcher.execute(new GetIndicatorsSystemByCodeAction(indSystemCode, null), new WaitingAsyncCallbackHandlingError<GetIndicatorsSystemByCodeResult>(this) {

            @Override
            public void onWaitSuccess(GetIndicatorsSystemByCodeResult result) {
                indSystem = result.getIndicatorsSystem();
                setIndicatorsSystem(result.getIndicatorsSystem());
                // Load system structure
                retrieveSystemStructure();
            }
        });
    }

    @Override
    public void retrieveDiffusionIndSystem(final String indSystemCode, final String versionNumber) {
        dispatcher.execute(new GetIndicatorsSystemByCodeAction(indSystemCode, versionNumber), new WaitingAsyncCallbackHandlingError<GetIndicatorsSystemByCodeResult>(this) {

            @Override
            public void onWaitSuccess(GetIndicatorsSystemByCodeResult result) {
                getView().setDiffusionIndicatorsSystem(result.getIndicatorsSystem());
            }
        });
    }

    @Override
    public void retrieveSystemStructure() {
        dispatcher.execute(new GetIndicatorsSystemStructureAction(indSystem.getCode()), new WaitingAsyncCallbackHandlingError<GetIndicatorsSystemStructureResult>(this) {

            @Override
            public void onWaitSuccess(GetIndicatorsSystemStructureResult result) {
                getView().setIndicatorsSystemStructure(indSystem, result.getStructure());
            }
        });
    }

    @Override
    public void retrieveIndicator(final String uuid) {
        dispatcher.execute(new GetIndicatorAction(uuid), new WaitingAsyncCallbackHandlingError<GetIndicatorResult>(this) {

            @Override
            public void onWaitSuccess(GetIndicatorResult result) {
                getView().setIndicatorFromIndicatorInstance(result.getIndicator());
            }
        });
    }

    @Override
    public void createDimension(IndicatorsSystemDtoWeb system, DimensionDto dimension) {
        dispatcher.execute(new CreateDimensionAction(system, dimension), new WaitingAsyncCallbackHandlingError<CreateDimensionResult>(this) {

            @Override
            public void onWaitSuccess(CreateDimensionResult result) {
                retrieveSystemStructure(); // Reload system structure
                fireSuccessMessage(getMessages().systemStrucDimCreated());
                getView().onDimensionSaved(result.getCreatedDimension());
            }
        });
    }

    @Override
    public void updateDimension(DimensionDto dimension) {
        dispatcher.execute(new UpdateDimensionAction(dimension), new WaitingAsyncCallbackHandlingError<UpdateDimensionResult>(this) {

            @Override
            public void onWaitSuccess(UpdateDimensionResult result) {
                retrieveSystemStructure(); // Reload system structure
                fireSuccessMessage(getMessages().systemStrucDimSaved());
                getView().onDimensionSaved(result.getDimension());
            }
        });
    }

    @Override
    public void deleteDimension(DimensionDto dimension) {
        dispatcher.execute(new DeleteDimensionAction(dimension.getUuid()), new WaitingAsyncCallbackHandlingError<DeleteDimensionResult>(this) {

            @Override
            public void onWaitSuccess(DeleteDimensionResult result) {
                retrieveSystemStructure(); // Reload system structure
                fireSuccessMessage(getMessages().systemStrucDimDeleted());
            }
        });
    }

    @Override
    public void createIndicatorInstance(IndicatorsSystemDtoWeb system, IndicatorInstanceDto instance) {
        dispatcher.execute(new CreateIndicatorInstanceAction(system, instance), new WaitingAsyncCallbackHandlingError<CreateIndicatorInstanceResult>(this) {

            @Override
            public void onWaitSuccess(CreateIndicatorInstanceResult result) {
                retrieveSystemStructure(); // Reload system structure
                fireSuccessMessage(getMessages().systemStrucIndInstanceCreated());
                getView().onIndicatorInstanceSaved(result.getCreatedIndicatorInstance());
            }
        });
    }

    @Override
    public void updateIndicatorInstance(IndicatorInstanceDto indicatorInstance) {
        final IndicatorInstanceDto instance = indicatorInstance;
        dispatcher.execute(new UpdateIndicatorInstanceAction(instance), new WaitingAsyncCallbackHandlingError<UpdateIndicatorInstanceResult>(this) {

            @Override
            public void onWaitSuccess(UpdateIndicatorInstanceResult result) {
                retrieveSystemStructure(); // Reload system structure
                fireSuccessMessage(getMessages().systemStrucIndInstanceSaved());
                getView().onIndicatorInstanceSaved(result.getIndicatorInstance());
            }
        });
    }

    @Override
    public void moveSystemStructureNodes(String systemUuid, String targetUuid, ElementLevelDto level, Long newOrder) {
        dispatcher.execute(new MoveSystemStructureContentAction(systemUuid, targetUuid, newOrder, level), new WaitingAsyncCallbackHandlingError<MoveSystemStructureContentResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                super.onWaitFailure(caught);
                retrieveSystemStructure(); // Reload system structure
            }

            @Override
            public void onWaitSuccess(MoveSystemStructureContentResult result) {
                retrieveSystemStructure();
            }
        });
    }

    @Override
    public void deleteIndicatorInstance(IndicatorInstanceDto instance) {
        dispatcher.execute(new DeleteIndicatorInstanceAction(instance.getUuid()), new WaitingAsyncCallbackHandlingError<DeleteIndicatorInstanceResult>(this) {

            @Override
            public void onWaitSuccess(DeleteIndicatorInstanceResult result) {
                retrieveSystemStructure(); // Reload system structure
                fireSuccessMessage(getMessages().systemStrucIndInstanceDeleted());
            }
        });
    }

    @Override
    public void retrieveGeographicalValue(String geographicalValueUuid) {
        dispatcher.execute(new GetGeographicalValueAction(geographicalValueUuid), new WaitingAsyncCallbackHandlingError<GetGeographicalValueResult>(this) {

            @Override
            public void onWaitSuccess(GetGeographicalValueResult result) {
                getView().setGeographicalValue(result.getGeographicalValueDto());
            }
        });
    }

    @Override
    public void sendToProductionValidation(final IndicatorsSystemDtoWeb indicatorsSystemDto) {
        dispatcher.execute(new SendIndicatorsSystemToProductionValidationAction(indicatorsSystemDto), new WaitingAsyncCallbackHandlingError<SendIndicatorsSystemToProductionValidationResult>(this) {

            @Override
            public void onWaitSuccess(SendIndicatorsSystemToProductionValidationResult result) {
                fireSuccessMessage(getMessages().systemSentToProductionValidation());
                indSystem = result.getIndicatorsSystemDtoWeb();
                setIndicatorsSystem(result.getIndicatorsSystemDtoWeb());
            }
        });
    }

    @Override
    public void sendToDiffusionValidation(final IndicatorsSystemDtoWeb indicatorsSystemDto) {
        dispatcher.execute(new SendIndicatorsSystemToDiffusionValidationAction(indicatorsSystemDto), new WaitingAsyncCallbackHandlingError<SendIndicatorsSystemToDiffusionValidationResult>(this) {

            @Override
            public void onWaitSuccess(SendIndicatorsSystemToDiffusionValidationResult result) {
                fireSuccessMessage(getMessages().systemSentToDiffusionValidation());
                indSystem = result.getIndicatorsSystemDtoWeb();
                setIndicatorsSystem(result.getIndicatorsSystemDtoWeb());
            }
        });
    }

    @Override
    public void rejectValidation(final IndicatorsSystemDtoWeb indicatorsSystemDto) {
        if (IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION.equals(indicatorsSystemDto.getProcStatus())) {
            dispatcher.execute(new RejectIndicatorsSystemProductionValidationAction(indicatorsSystemDto),
                    new WaitingAsyncCallbackHandlingError<RejectIndicatorsSystemProductionValidationResult>(this) {

                        @Override
                        public void onWaitSuccess(RejectIndicatorsSystemProductionValidationResult result) {
                            fireSuccessMessage(getMessages().systemValidationRejected());
                            indSystem = result.getIndicatorsSystemDto();
                            setIndicatorsSystem(result.getIndicatorsSystemDto());
                        }
                    });
        } else if (IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION.equals(indicatorsSystemDto.getProcStatus())) {
            dispatcher.execute(new RejectIndicatorsSystemDiffusionValidationAction(indicatorsSystemDto), new WaitingAsyncCallbackHandlingError<RejectIndicatorsSystemDiffusionValidationResult>(this) {

                @Override
                public void onWaitSuccess(RejectIndicatorsSystemDiffusionValidationResult result) {
                    fireSuccessMessage(getMessages().systemValidationRejected());
                    indSystem = result.getIndicatorsSystemDto();
                    setIndicatorsSystem(result.getIndicatorsSystemDto());
                }
            });
        }
    }

    @Override
    public void publish(final IndicatorsSystemDtoWeb indicatorsSystemDto) {
        if (indicatorsSystemDto.isOperationExternallyPublished()) {
            dispatcher.execute(new PublishIndicatorsSystemAction(indicatorsSystemDto), new WaitingAsyncCallbackHandlingError<PublishIndicatorsSystemResult>(this) {

                @Override
                public void onWaitSuccess(PublishIndicatorsSystemResult result) {
                    fireSuccessMessage(getMessages().systemPublished());
                    indSystem = result.getIndicatorsSystemDto();
                    setIndicatorsSystem(result.getIndicatorsSystemDto());
                }
            });
        } else {
            ShowMessageEvent.fireErrorMessage(SystemPresenter.this, getMessages().errorPublishingSystemOperationNotPublished());
        }
    }

    @Override
    public void archive(final IndicatorsSystemDtoWeb indicatorsSystemDto) {
        dispatcher.execute(new ArchiveIndicatorsSystemAction(indicatorsSystemDto), new WaitingAsyncCallbackHandlingError<ArchiveIndicatorsSystemResult>(this) {

            @Override
            public void onWaitSuccess(ArchiveIndicatorsSystemResult result) {
                fireSuccessMessage(getMessages().systemArchived());
                indSystem = result.getIndicatorsSystemDtoWeb();
                setIndicatorsSystem(result.getIndicatorsSystemDtoWeb());
            }
        });
    }

    @Override
    public void versioningIndicatorsSystem(IndicatorsSystemDtoWeb indicatorsSystemDto, VersionTypeEnum versionType) {
        dispatcher.execute(new VersioningIndicatorsSystemAction(indicatorsSystemDto, versionType), new WaitingAsyncCallbackHandlingError<VersioningIndicatorsSystemResult>(this) {

            @Override
            public void onWaitSuccess(VersioningIndicatorsSystemResult result) {
                retrieveSystemStructure(); // Reload system structure
                fireSuccessMessage(getMessages().systemVersioned());
                indSystem = result.getIndicatorsSystemDtoWeb();
                setIndicatorsSystem(result.getIndicatorsSystemDtoWeb());
            }
        });
    }

    @Override
    public void retrieveTimeGranularitiesInIndicator(String indicatorUuid, String indicatorVersion) {
        dispatcher.execute(new GetTimeGranularitiesInIndicatorAction(indicatorUuid, indicatorVersion), new WaitingAsyncCallbackHandlingError<GetTimeGranularitiesInIndicatorResult>(this) {

            @Override
            public void onWaitSuccess(GetTimeGranularitiesInIndicatorResult result) {
                getView().setTemporalGranularitiesForIndicator(result.getTimeGranularityDtos());
            }
        });

    }

    @Override
    public void retrieveTimeValuesWithGranularityInIndicator(String indicatorUuid, String indicatorVersion, IstacTimeGranularityEnum timeGranularity) {
        dispatcher.execute(new GetTimeValuesByGranularityInIndicatorAction(indicatorUuid, indicatorVersion, timeGranularity),
                new WaitingAsyncCallbackHandlingError<GetTimeValuesByGranularityInIndicatorResult>(this) {

                    @Override
                    public void onWaitSuccess(GetTimeValuesByGranularityInIndicatorResult result) {
                        getView().setTemporalValuesFormIndicator(result.getTimeValueDtos());
                    }
                });
    }

    @Override
    public void retrieveGeographicalGranularitiesInIndicator(String indicatorUuid, String indicatorVersion) {
        dispatcher.execute(new GetGeographicalGranularitiesInIndicatorAction(indicatorUuid, indicatorVersion),
                new WaitingAsyncCallbackHandlingError<GetGeographicalGranularitiesInIndicatorResult>(this) {

                    @Override
                    public void onWaitSuccess(GetGeographicalGranularitiesInIndicatorResult result) {
                        getView().setGeographicalGranularitiesForIndicator(result.getGeographicalGranularities());
                    }
                });
    }

    @Override
    public void retrieveGeographicalValuesWithGranularityInIndicator(String indicatorUuid, String indicatorVersion, String geographicalGranularityUuid) {
        dispatcher.execute(new GetGeographicalValuesByGranularityInIndicatorAction(indicatorUuid, indicatorVersion, geographicalGranularityUuid),
                new WaitingAsyncCallbackHandlingError<GetGeographicalValuesByGranularityInIndicatorResult>(this) {

                    @Override
                    public void onWaitSuccess(GetGeographicalValuesByGranularityInIndicatorResult result) {
                        getView().setGeographicalValuesForIndicator(result.getGeographicalValueDtos());
                    }
                });
    }

    @Override
    public void searchIndicator(IndicatorCriteria criteria) {
        dispatcher.execute(new FindIndicatorsAction(criteria), new WaitingAsyncCallbackHandlingError<FindIndicatorsResult>(this) {

            @Override
            public void onWaitSuccess(FindIndicatorsResult result) {
                getView().setIndicators(result.getIndicatorDtos());
            }
        });
    }

    @Override
    public void retrieveGeographicalGranularity(String geographicalGranularityUuid) {
        dispatcher.execute(new GetGeographicalGranularityAction(geographicalGranularityUuid), new WaitingAsyncCallbackHandlingError<GetGeographicalGranularityResult>(this) {

            @Override
            public void onWaitSuccess(GetGeographicalGranularityResult result) {
                getView().setGeographicalGranularity(result.getGeographicalGranularityDto());
            }
        });
    }

    @Override
    public void previewData(String instanceCode, String systemCode) {
        dispatcher.execute(new GetIndicatorInstancePreviewUrlAction(instanceCode, systemCode), new WaitingAsyncCallbackHandlingError<GetIndicatorInstancePreviewUrlResult>(this) {

            @Override
            public void onWaitSuccess(GetIndicatorInstancePreviewUrlResult result) {
                Window.open(result.getUrl(), "_blank", "");
            }
        });
    }

    @Override
    public void exportIndicatorsSystemInDspl(IndicatorsSystemDtoWeb indicatorsSystemDto, boolean mergingTimeGranularities) {
        dispatcher.execute(new ExportSystemInDsplAction(indicatorsSystemDto.getUuid(), indicatorsSystemDto.getTitle(), indicatorsSystemDto.getDescription(), mergingTimeGranularities),
                new WaitingAsyncCallbackHandlingError<ExportSystemInDsplResult>(this) {

                    @Override
                    public void onWaitSuccess(ExportSystemInDsplResult result) {
                        if (result.getFiles() != null) {
                            for (String file : result.getFiles()) {
                                CommonUtils.downloadFile(file);
                            }
                        }
                    }
                });
    }
}
