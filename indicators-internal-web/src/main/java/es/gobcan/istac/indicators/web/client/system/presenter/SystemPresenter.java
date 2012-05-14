package es.gobcan.istac.indicators.web.client.system.presenter;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
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
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemStructureDto;
import es.gobcan.istac.indicators.core.dto.TimeGranularityDto;
import es.gobcan.istac.indicators.core.dto.TimeValueDto;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemProcStatusEnum;
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;
import es.gobcan.istac.indicators.web.client.NameTokens;
import es.gobcan.istac.indicators.web.client.PlaceRequestParams;
import es.gobcan.istac.indicators.web.client.main.presenter.MainPagePresenter;
import es.gobcan.istac.indicators.web.client.utils.ErrorUtils;
import es.gobcan.istac.indicators.web.client.widgets.WaitingAsyncCallback;
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
import es.gobcan.istac.indicators.web.shared.FindIndicatorsAction;
import es.gobcan.istac.indicators.web.shared.FindIndicatorsResult;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularitiesInIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularitiesInIndicatorResult;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValueAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValueResult;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesByGranularityInIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesByGranularityInIndicatorResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemByCodeAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemByCodeResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemStructureAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemStructureResult;
import es.gobcan.istac.indicators.web.shared.GetTimeGranularitiesInIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetTimeGranularitiesInIndicatorResult;
import es.gobcan.istac.indicators.web.shared.GetTimeValuesInIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetTimeValuesInIndicatorResult;
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

    /* Models */
    private IndicatorsSystemDtoWeb                    indSystem;                                                         // To be able to cache structure

    public interface SystemView extends View, HasUiHandlers<SystemUiHandler> {

        void init();

        void setIndicatorFromIndicatorInstance(IndicatorDto indicator);
        void setIndicators(List<IndicatorDto> indicators);
        void setIndicatorsSystem(IndicatorsSystemDtoWeb indicatorSystem);
        void setIndicatorsSystemStructure(IndicatorsSystemDtoWeb indicatorsSystem, IndicatorsSystemStructureDto structure);
        void onDimensionSaved(DimensionDto dimension);
        void onIndicatorInstanceSaved(IndicatorInstanceDto instance);

        // void onIndicatorDataPopulated(IndicatorDto indicatorDto);

        // Instance
        void setTemporalGranularitiesForIndicator(List<TimeGranularityDto> timeGranularityEnums);
        void setTemporalValuesFormIndicator(List<TimeValueDto> timeValues);
        void setGeographicalGranularitiesForIndicator(List<GeographicalGranularityDto> geographicalGranularityDtos);
        void setGeographicalValuesForIndicator(List<GeographicalValueDto> geographicalValueDtos);
        void setGeographicalValue(GeographicalValueDto geographicalValueDto);

    }

    @ProxyCodeSplit
    @NameToken(NameTokens.systemPage)
    public interface SystemProxy extends Proxy<SystemPresenter>, Place {
    }

    @Inject
    public SystemPresenter(EventBus eventBus, SystemView view, SystemProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, view, proxy);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
        view.setUiHandlers(this);
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
        dispatcher.execute(new GetIndicatorsSystemByCodeAction(indSystemCode), new WaitingAsyncCallback<GetIndicatorsSystemByCodeResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error loading indicator system with code = " + indSystemCode);
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().systemErrorRetrieve()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetIndicatorsSystemByCodeResult result) {
                setIndicatorsSystem(result.getIndicatorsSystem());
                // Load system structure
                retrieveSystemStructure();
            }
        });
    }

    @Override
    public void retrieveSystemStructure() {
        dispatcher.execute(new GetIndicatorsSystemStructureAction(indSystem.getCode()), new WaitingAsyncCallback<GetIndicatorsSystemStructureResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error loading system structure of indicator system  with code = " + indSystem.getCode());
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().systemStrucErrorRetrieve()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetIndicatorsSystemStructureResult result) {
                getView().setIndicatorsSystemStructure(indSystem, result.getStructure());
            }
        });
    }

    @Override
    public void retrieveIndicator(final String uuid) {
        dispatcher.execute(new GetIndicatorAction(uuid), new WaitingAsyncCallback<GetIndicatorResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error retrieving indicador with uuid = " + uuid);
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRetrievingIndicatorFromInstance()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetIndicatorResult result) {
                getView().setIndicatorFromIndicatorInstance(result.getIndicator());
            }
        });
    }

    @Override
    public void createDimension(IndicatorsSystemDtoWeb system, DimensionDto dimension) {
        dispatcher.execute(new CreateDimensionAction(system, dimension), new WaitingAsyncCallback<CreateDimensionResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().systemStrucDimErrorCreate()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(CreateDimensionResult result) {
                retrieveSystemStructure(); // Reload system structure
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getMessageList(getMessages().systemStrucDimCreated()), MessageTypeEnum.SUCCESS);
                getView().onDimensionSaved(result.getCreatedDimension());
            }
        });
    }

    @Override
    public void updateDimension(DimensionDto dimension) {
        dispatcher.execute(new UpdateDimensionAction(dimension), new WaitingAsyncCallback<UpdateDimensionResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().systemStrucDimErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateDimensionResult result) {
                retrieveSystemStructure(); // Reload system structure
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getMessageList(getMessages().systemStrucDimSaved()), MessageTypeEnum.SUCCESS);
                getView().onDimensionSaved(result.getDimension());
            }
        });
    }

    @Override
    public void deleteDimension(DimensionDto dimension) {
        dispatcher.execute(new DeleteDimensionAction(dimension.getUuid()), new WaitingAsyncCallback<DeleteDimensionResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().systemStrucDimErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteDimensionResult result) {
                retrieveSystemStructure(); // Reload system structure
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getMessageList(getMessages().systemStrucDimDeleted()), MessageTypeEnum.SUCCESS);
            }
        });
    }

    @Override
    public void createIndicatorInstance(IndicatorsSystemDtoWeb system, IndicatorInstanceDto instance) {
        dispatcher.execute(new CreateIndicatorInstanceAction(system, instance), new WaitingAsyncCallback<CreateIndicatorInstanceResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().systemStrucIndInstanceErrorCreate()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(CreateIndicatorInstanceResult result) {
                retrieveSystemStructure(); // Reload system structure
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getMessageList(getMessages().systemStrucIndInstanceCreated()), MessageTypeEnum.SUCCESS);
                getView().onIndicatorInstanceSaved(result.getCreatedIndicatorInstance());
            }
        });
    }

    @Override
    public void updateIndicatorInstance(IndicatorInstanceDto indicatorInstance) {
        final IndicatorInstanceDto instance = indicatorInstance;
        dispatcher.execute(new UpdateIndicatorInstanceAction(instance), new WaitingAsyncCallback<UpdateIndicatorInstanceResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().systemStrucIndInstanceErrorSave()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(UpdateIndicatorInstanceResult result) {
                retrieveSystemStructure(); // Reload system structure
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getMessageList(getMessages().systemStrucIndInstanceSaved()), MessageTypeEnum.SUCCESS);
                getView().onIndicatorInstanceSaved(result.getIndicatorInstance());
            }
        });
    }

    @Override
    public void moveSystemStructureNodes(String systemUuid, String targetUuid, ElementLevelDto level, Long newOrder) {
        dispatcher.execute(new MoveSystemStructureContentAction(systemUuid, targetUuid, newOrder, level), new WaitingAsyncCallback<MoveSystemStructureContentResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                retrieveSystemStructure(); // Reload system structure
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().systemStrucNodesErrorMove()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(MoveSystemStructureContentResult result) {
                retrieveSystemStructure();
            }
        });
    }

    @Override
    public void deleteIndicatorInstance(IndicatorInstanceDto instance) {
        dispatcher.execute(new DeleteIndicatorInstanceAction(instance.getUuid()), new WaitingAsyncCallback<DeleteIndicatorInstanceResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().systemStrucIndInstanceErrorDelete()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(DeleteIndicatorInstanceResult result) {
                retrieveSystemStructure(); // Reload system structure
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getMessageList(getMessages().systemStrucIndInstanceDeleted()), MessageTypeEnum.SUCCESS);
            }
        });
    }

    @Override
    public void retrieveGeographicalValue(String geographicalValueUuid) {
        dispatcher.execute(new GetGeographicalValueAction(geographicalValueUuid), new WaitingAsyncCallback<GetGeographicalValueResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorGeographicalValueNotFound()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetGeographicalValueResult result) {
                getView().setGeographicalValue(result.getGeographicalValueDto());
            }
        });
    }

    @Override
    public void sendToProductionValidation(final IndicatorsSystemDtoWeb indicatorsSystemDto) {
        dispatcher.execute(new SendIndicatorsSystemToProductionValidationAction(indicatorsSystemDto), new WaitingAsyncCallback<SendIndicatorsSystemToProductionValidationResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error sendind to production validation indicators system with uuid = " + indicatorsSystemDto.getUuid());
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorSendingSystemToProductionValidation()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SendIndicatorsSystemToProductionValidationResult result) {
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getMessageList(getMessages().systemSentToProductionValidation()), MessageTypeEnum.SUCCESS);
                setIndicatorsSystem(result.getIndicatorsSystemDtoWeb());
            }
        });
    }

    @Override
    public void sendToDiffusionValidation(final IndicatorsSystemDtoWeb indicatorsSystemDto) {
        dispatcher.execute(new SendIndicatorsSystemToDiffusionValidationAction(indicatorsSystemDto), new WaitingAsyncCallback<SendIndicatorsSystemToDiffusionValidationResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error sendind to diffusion validation indicators system with uuid = " + indicatorsSystemDto.getUuid());
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorSendingSystemToDiffusionValidation()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(SendIndicatorsSystemToDiffusionValidationResult result) {
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getMessageList(getMessages().systemSentToDiffusionValidation()), MessageTypeEnum.SUCCESS);
                setIndicatorsSystem(result.getIndicatorsSystemDtoWeb());
            }
        });
    }

    @Override
    public void rejectValidation(final IndicatorsSystemDtoWeb indicatorsSystemDto) {
        if (IndicatorsSystemProcStatusEnum.PRODUCTION_VALIDATION.equals(indicatorsSystemDto.getProcStatus())) {
            dispatcher.execute(new RejectIndicatorsSystemProductionValidationAction(indicatorsSystemDto), new WaitingAsyncCallback<RejectIndicatorsSystemProductionValidationResult>() {

                @Override
                public void onWaitFailure(Throwable caught) {
                    logger.log(Level.SEVERE, "Error rejecting validation of indicators system with uuid = " + indicatorsSystemDto.getUuid());
                    ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRejectingSystemValidation()), MessageTypeEnum.ERROR);
                }
                @Override
                public void onWaitSuccess(RejectIndicatorsSystemProductionValidationResult result) {
                    ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getMessageList(getMessages().systemValidationRejected()), MessageTypeEnum.SUCCESS);
                    setIndicatorsSystem(result.getIndicatorsSystemDto());
                }
            });
        } else if (IndicatorsSystemProcStatusEnum.DIFFUSION_VALIDATION.equals(indicatorsSystemDto.getProcStatus())) {
            dispatcher.execute(new RejectIndicatorsSystemDiffusionValidationAction(indicatorsSystemDto), new WaitingAsyncCallback<RejectIndicatorsSystemDiffusionValidationResult>() {

                @Override
                public void onWaitFailure(Throwable caught) {
                    logger.log(Level.SEVERE, "Error rejecting validation of indicators system with uuid = " + indicatorsSystemDto.getUuid());
                    ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRejectingSystemValidation()), MessageTypeEnum.ERROR);
                }
                @Override
                public void onWaitSuccess(RejectIndicatorsSystemDiffusionValidationResult result) {
                    ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getMessageList(getMessages().systemValidationRejected()), MessageTypeEnum.SUCCESS);
                    setIndicatorsSystem(result.getIndicatorsSystemDto());
                }
            });
        }
    }

    @Override
    public void publish(final IndicatorsSystemDtoWeb indicatorsSystemDto) {
        if (indicatorsSystemDto.isOperationExternallyPublished()) {
            dispatcher.execute(new PublishIndicatorsSystemAction(indicatorsSystemDto), new WaitingAsyncCallback<PublishIndicatorsSystemResult>() {

                @Override
                public void onWaitFailure(Throwable caught) {
                    logger.log(Level.SEVERE, "Error publishing indicators system with uuid = " + indicatorsSystemDto.getUuid());
                    ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorPublishingSystem()), MessageTypeEnum.ERROR);
                }
                @Override
                public void onWaitSuccess(PublishIndicatorsSystemResult result) {
                    ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getMessageList(getMessages().systemPublished()), MessageTypeEnum.SUCCESS);
                    setIndicatorsSystem(result.getIndicatorsSystemDto());
                }
            });
        } else {
            ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(null, getMessages().errorPublishingSystemOperationNotPublished()), MessageTypeEnum.ERROR);
        }
    }

    @Override
    public void archive(final IndicatorsSystemDtoWeb indicatorsSystemDto) {
        dispatcher.execute(new ArchiveIndicatorsSystemAction(indicatorsSystemDto), new WaitingAsyncCallback<ArchiveIndicatorsSystemResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error archiving indicators system with uuid = " + indicatorsSystemDto.getUuid());
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorArchivingSystem()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(ArchiveIndicatorsSystemResult result) {
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getMessageList(getMessages().systemArchived()), MessageTypeEnum.SUCCESS);
                setIndicatorsSystem(result.getIndicatorsSystemDtoWeb());
            }
        });
    }

    @Override
    public void versioningIndicatorsSystem(IndicatorsSystemDtoWeb indicatorsSystemDto, VersionTypeEnum versionType) {
        dispatcher.execute(new VersioningIndicatorsSystemAction(indicatorsSystemDto, versionType), new WaitingAsyncCallback<VersioningIndicatorsSystemResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorVersioningIndicatorsSystem()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(VersioningIndicatorsSystemResult result) {
                retrieveSystemStructure(); // Reload system structure
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getMessageList(getMessages().systemVersioned()), MessageTypeEnum.SUCCESS);
                setIndicatorsSystem(result.getIndicatorsSystemDtoWeb());
            }
        });
    }

    // @Override
    // public void populateIndicatorData(String uuid, String version) {
    // dispatcher.execute(new PopulateIndicatorDataAction(uuid, version), new WaitingAsyncCallback<PopulateIndicatorDataResult>() {
    //
    // @Override
    // public void onWaitFailure(Throwable caught) {
    // ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorPopulatingIndicatorData()), MessageTypeEnum.ERROR);
    // }
    // @Override
    // public void onWaitSuccess(PopulateIndicatorDataResult result) {
    // getView().onIndicatorDataPopulated(result.getIndicatorDto());
    // ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getMessageList(getMessages().indicatorDataPopulated()), MessageTypeEnum.SUCCESS);
    // }
    // });
    // }

    @Override
    public void retrieveTimeGranularitiesInIndicator(String indicatorUuid, String indicatorVersion) {
        dispatcher.execute(new GetTimeGranularitiesInIndicatorAction(indicatorUuid, indicatorVersion), new WaitingAsyncCallback<GetTimeGranularitiesInIndicatorResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRetrievingIndicatorTimeGranularities()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetTimeGranularitiesInIndicatorResult result) {
                getView().setTemporalGranularitiesForIndicator(result.getTimeGranularityDtos());
            }
        });

    }

    @Override
    public void retrieveTimeValuesInIndicator(String indicatorUuid, String indicatorVersion) {
        dispatcher.execute(new GetTimeValuesInIndicatorAction(indicatorUuid, indicatorVersion), new WaitingAsyncCallback<GetTimeValuesInIndicatorResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRetrievingIndicatorTimeValues()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetTimeValuesInIndicatorResult result) {
                getView().setTemporalValuesFormIndicator(result.getTimeValueDtos());
            }
        });
    }

    @Override
    public void retrieveGeographicalGranularitiesInIndicator(String indicatorUuid, String indicatorVersion) {
        dispatcher.execute(new GetGeographicalGranularitiesInIndicatorAction(indicatorUuid, indicatorVersion), new WaitingAsyncCallback<GetGeographicalGranularitiesInIndicatorResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRetrievingIndicatorGeographicalGranularities()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetGeographicalGranularitiesInIndicatorResult result) {
                getView().setGeographicalGranularitiesForIndicator(result.getGeographicalGranularities());
            }
        });
    }

    @Override
    public void retrieveGeographicalValuesWithGranularityInIndicator(String indicatorUuid, String indicatorVersion, String geographicalGranularityUuid) {
        dispatcher.execute(new GetGeographicalValuesByGranularityInIndicatorAction(indicatorUuid, indicatorVersion, geographicalGranularityUuid),
                new WaitingAsyncCallback<GetGeographicalValuesByGranularityInIndicatorResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRetrievingIndicatorGeographicalValues()), MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onWaitSuccess(GetGeographicalValuesByGranularityInIndicatorResult result) {
                        getView().setGeographicalValuesForIndicator(result.getGeographicalValueDtos());
                    }
                });
    }

    @Override
    public void searchIndicator(IndicatorCriteria criteria) {
        dispatcher.execute(new FindIndicatorsAction(criteria), new WaitingAsyncCallback<FindIndicatorsResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorSearchingIndicators()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(FindIndicatorsResult result) {
                getView().setIndicators(result.getIndicatorDtos());
            }
        });
    }

}
