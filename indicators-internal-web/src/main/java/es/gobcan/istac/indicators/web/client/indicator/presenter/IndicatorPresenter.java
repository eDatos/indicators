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
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import es.gobcan.istac.indicators.core.dto.serviceapi.DataDefinitionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataSourceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.DataStructureDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.QuantityUnitDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.SubjectDto;
import es.gobcan.istac.indicators.core.enume.domain.VersionTypeEnum;
import es.gobcan.istac.indicators.web.client.NameTokens;
import es.gobcan.istac.indicators.web.client.PlaceRequestParams;
import es.gobcan.istac.indicators.web.client.events.UpdateGeographicalGranularitiesEvent;
import es.gobcan.istac.indicators.web.client.events.UpdateGeographicalGranularitiesEvent.UpdateGeographicalGranularitiesHandler;
import es.gobcan.istac.indicators.web.client.events.UpdateQuantityUnitsEvent;
import es.gobcan.istac.indicators.web.client.events.UpdateQuantityUnitsEvent.UpdateQuantityUnitsHandler;
import es.gobcan.istac.indicators.web.client.main.presenter.MainPagePresenter;
import es.gobcan.istac.indicators.web.client.utils.ErrorUtils;
import es.gobcan.istac.indicators.web.shared.ArchiveIndicatorAction;
import es.gobcan.istac.indicators.web.shared.ArchiveIndicatorResult;
import es.gobcan.istac.indicators.web.shared.GetDataDefinitionAction;
import es.gobcan.istac.indicators.web.shared.GetDataDefinitionResult;
import es.gobcan.istac.indicators.web.shared.GetDataDefinitionsAction;
import es.gobcan.istac.indicators.web.shared.GetDataDefinitionsResult;
import es.gobcan.istac.indicators.web.shared.GetDataSourcesListAction;
import es.gobcan.istac.indicators.web.shared.GetDataSourcesListResult;
import es.gobcan.istac.indicators.web.shared.GetDataStructureAction;
import es.gobcan.istac.indicators.web.shared.GetDataStructureResult;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValueAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValueResult;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorByCodeAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorByCodeResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorListResult;
import es.gobcan.istac.indicators.web.shared.GetSubjectsListAction;
import es.gobcan.istac.indicators.web.shared.GetSubjectsListResult;
import es.gobcan.istac.indicators.web.shared.PublishIndicatorAction;
import es.gobcan.istac.indicators.web.shared.PublishIndicatorResult;
import es.gobcan.istac.indicators.web.shared.RejectIndicatorValidationAction;
import es.gobcan.istac.indicators.web.shared.RejectIndicatorValidationResult;
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

public class IndicatorPresenter extends Presenter<IndicatorPresenter.IndicatorView, IndicatorPresenter.IndicatorProxy> implements IndicatorUiHandler, UpdateQuantityUnitsHandler, UpdateGeographicalGranularitiesHandler {
	
    private Logger logger = Logger.getLogger(IndicatorPresenter.class.getName());
    
    private DispatchAsync dispatcher;
	private String indicatorCode;
	
	@ProxyCodeSplit
    @NameToken(NameTokens.indicatorPage)
    public interface IndicatorProxy extends Proxy<IndicatorPresenter>, Place {}
	
	public interface IndicatorView extends View, HasUiHandlers<IndicatorPresenter> {
		// Indicator
	    
	    void setIndicator(IndicatorDto indicator);
		void setIndicatorDataSources(List<DataSourceDto> dataSourceDtos);
		
		void setIndicatorList(List<IndicatorDto> indicators);
		
		void setSubjectsList(List<SubjectDto> subjectDtos);
		void setQuantityUnits(List<QuantityUnitDto> units);
		void setGeographicalGranularities(List<GeographicalGranularityDto> granularityDtos);
		void setGeographicalValues(List<GeographicalValueDto> geographicalValueDtos);
		void setGeographicalValue(GeographicalValueDto geographicalValueDto);
		
		// Data source
		
		void setDataDefinitions(List<DataDefinitionDto> dataDefinitionDtos);
		void setDataDefinition(DataDefinitionDto dataDefinitionDto);
		void setDataStructure(DataStructureDto dataStructureDto);
		
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
	}

	@Override
	protected void onReset() {
	    super.onReset();
	    SetTitleEvent.fire(IndicatorPresenter.this, getConstants().indicators());
	    retrieveIndicatorByCode();
	}
	
	private void retrieveIndicatorByCode() {
		dispatcher.execute(new GetIndicatorByCodeAction(this.indicatorCode), new AsyncCallback<GetIndicatorByCodeResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().indicErrorRetrieve()), MessageTypeEnum.ERROR);
			}
			@Override
			public void onSuccess(GetIndicatorByCodeResult result) {
			    final IndicatorDto indicatorDto = result.getIndicator();
				dispatcher.execute(new GetIndicatorListAction(), new AsyncCallback<GetIndicatorListResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().indicErrorRetrieveList()), MessageTypeEnum.ERROR);
                    }
                    @Override
                    public void onSuccess(GetIndicatorListResult result) {
                        getView().setIndicatorList(result.getIndicatorList());
                        getView().setIndicator(indicatorDto);
                    }
                });
				retrieveDataSources(indicatorDto.getUuid(), indicatorDto.getVersionNumber());
			}
		});
	}
	
	public void saveIndicator(IndicatorDto indicator) {
	    dispatcher.execute(new UpdateIndicatorAction(indicator), new AsyncCallback<UpdateIndicatorResult>() {
	        @Override
	        public void onFailure(Throwable caught) {
	            ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().indicErrorSave()), MessageTypeEnum.ERROR);
	        }
	        @Override
	        public void onSuccess(UpdateIndicatorResult result) {
	           getView().setIndicator(result.getIndicatorDto());
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
        dispatcher.execute(new GetSubjectsListAction(), new AsyncCallback<GetSubjectsListResult>() {
            @Override
            public void onFailure(Throwable caught) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRetrievingSubjects()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(GetSubjectsListResult result) {
                getView().setSubjectsList(result.getSubjectDtos());
            }}
        );
    }
    
    @ProxyEvent
    @Override
    public void onUpdateGeographicalGranularities(UpdateGeographicalGranularitiesEvent event) {
        getView().setGeographicalGranularities(event.getGeographicalGranularities());
    }

    @Override
    public void retrieveGeographicalValues(final String geographicalGranularityUuid) {
        dispatcher.execute(new GetGeographicalValuesAction(geographicalGranularityUuid), new AsyncCallback<GetGeographicalValuesResult>() {
            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error loading geographical values with geographical granularity UUID = " + geographicalGranularityUuid);
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRetrievingGeographicalValues()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(GetGeographicalValuesResult result) {
                getView().setGeographicalValues(result.getGeographicalValueDtos());
            }}
        );
    }

    @Override
    public void retrieveGeographicalValue(final String geographicalValueUuid) {
        dispatcher.execute(new GetGeographicalValueAction(geographicalValueUuid), new AsyncCallback<GetGeographicalValueResult>() {
            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error loading geographical value with UUID = " + geographicalValueUuid);
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorGeographicalValueNotFound()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(GetGeographicalValueResult result) {
                getView().setGeographicalValue(result.getGeographicalValueDto());
            }}
        );
    }

    @Override
    public void sendToProductionValidation(final String uuid) {
        dispatcher.execute(new SendIndicatorToProductionValidationAction(uuid), new AsyncCallback<SendIndicatorToProductionValidationResult>() {
            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error sending to production validation indicator with uuid  = " + uuid);
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorSendingIndicatorToProductionValidation()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(SendIndicatorToProductionValidationResult result) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getMessageList(getMessages().indicatorSentToProductionValidation()), MessageTypeEnum.SUCCESS);
                getView().setIndicator(result.getIndicatorDto());
            }}
        );
    }

    @Override
    public void sendToDiffusionValidation(final String uuid) {
        dispatcher.execute(new SendIndicatorToDiffusionValidationAction(uuid), new AsyncCallback<SendIndicatorToDiffusionValidationResult>() {
            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error sending to diffusion validation indicator with uuid  = " + uuid);
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorSendingIndicatorToDiffusionValidation()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(SendIndicatorToDiffusionValidationResult result) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getMessageList(getMessages().indicatorSentToDiffusionValidation()), MessageTypeEnum.SUCCESS);
                getView().setIndicator(result.getIndicatorDto());
            }}
        );
    }

    @Override
    public void rejectValidation(final String uuid) {
        dispatcher.execute(new RejectIndicatorValidationAction(uuid), new AsyncCallback<RejectIndicatorValidationResult>() {
            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error rejecting validation of indicator with uuid  = " + uuid);
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRejectingIndicatorValidation()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(RejectIndicatorValidationResult result) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getMessageList(getMessages().indicatorValidationRejected()), MessageTypeEnum.SUCCESS);
                getView().setIndicator(result.getIndicatorDto());
            }}
        );
    }

    @Override
    public void publish(final String uuid) {
        dispatcher.execute(new PublishIndicatorAction(uuid), new AsyncCallback<PublishIndicatorResult>() {
            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error publishing indicator with uuid  = " + uuid);
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorPublishingIndicator()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(PublishIndicatorResult result) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getMessageList(getMessages().indicatorPublished()), MessageTypeEnum.SUCCESS);
                getView().setIndicator(result.getIndicatorDto());
            }}
        );
    }

    @Override
    public void archive(final String uuid) {
        dispatcher.execute(new ArchiveIndicatorAction(uuid), new AsyncCallback<ArchiveIndicatorResult>() {
            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error arhiving indicator with uuid  = " + uuid);
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorArchivingIndicator()), MessageTypeEnum.ERROR);  
            }
            @Override
            public void onSuccess(ArchiveIndicatorResult result) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getMessageList(getMessages().indicatorArchived()), MessageTypeEnum.SUCCESS);
                getView().setIndicator(result.getIndicatorDto());
            }}
        );
    }

    @Override
    public void versioningIndicator(String uuid, VersionTypeEnum versionType) {
        dispatcher.execute(new VersioningIndicatorAction(uuid, versionType), new AsyncCallback<VersioningIndicatorResult>() {
            @Override
            public void onFailure(Throwable caught) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorVersioningIndicator()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(VersioningIndicatorResult result) {
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getMessageList(getMessages().indicatorVersioned()), MessageTypeEnum.SUCCESS);
                getView().setIndicator(result.getIndicatorDto());
            }}
        );
    }
    
    private void retrieveDataSources(final String indicatorUuid, final String indicatorVersion) {
        dispatcher.execute(new GetDataSourcesListAction(indicatorUuid, indicatorVersion), new AsyncCallback<GetDataSourcesListResult>() {
            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error retrieving data sources of indicator with uuid = " + indicatorUuid +  " and version =" + indicatorVersion);
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRetrievingDataSources()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(GetDataSourcesListResult result) {
                getView().setIndicatorDataSources(result.getDataSourceDtos());
            }}
        );
    }

    @Override
    public void retrieveDataDefinitions() {
        dispatcher.execute(new GetDataDefinitionsAction(), new AsyncCallback<GetDataDefinitionsResult>() {
            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error retrieving data definitions");
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRetrievingDataDefinitions()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(GetDataDefinitionsResult result) {
                getView().setDataDefinitions(result.getDataDefinitionDtos());
            }}
        );
    }

    @Override
    public void retrieveDataStructure(String uuid) {
        dispatcher.execute(new GetDataStructureAction(uuid), new AsyncCallback<GetDataStructureResult>() {
            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error retrieving data structure");
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRetrievingDataStructure()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(GetDataStructureResult result) {
                getView().setDataStructure(result.getDataStructureDto());
            }}
        );
        
    }

    @Override
    public void retrieveDataDefinition(final String uuid) {
        dispatcher.execute(new GetDataDefinitionAction(uuid), new AsyncCallback<GetDataDefinitionResult>() {
            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error retrieving data definition with uuid = " + uuid);
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRetrievingDataDefinition()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(GetDataDefinitionResult result) {
                getView().setDataDefinition(result.getDataDefinitionDto());
            }}
        );
    }

    @Override
    public void retrieveGeographicalValuesDS() {
        dispatcher.execute(new GetGeographicalValuesAction(null), new AsyncCallback<GetGeographicalValuesResult>() {
            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error retrieving geographical values");
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRetrievingGeographicalValues()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(GetGeographicalValuesResult result) {
                getView().setGeographicalValuesDS(result.getGeographicalValueDtos());
            }}
        );
    }

    @Override
    public void retrieveGeographicalValueDS(final String uuid) {
        dispatcher.execute(new GetGeographicalValueAction(uuid), new AsyncCallback<GetGeographicalValueResult>() {
            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error loading geographical value with UUID = " + uuid);
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorGeographicalValueNotFound()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(GetGeographicalValueResult result) {
                getView().setGeographicalValueDS(result.getGeographicalValueDto());
            }}
        );
    }

    @Override
    public void saveDataSource(final String indicatorUuid, final DataSourceDto dataSourceDto) {
        dispatcher.execute(new SaveDataSourceAction(indicatorUuid, dataSourceDto), new AsyncCallback<SaveDataSourceResult>() {
            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error saving datasource with uuid = " + dataSourceDto.getUuid() + " in indicator with uuid = " + indicatorUuid);
                ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorSavingDataSource()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onSuccess(SaveDataSourceResult result) {
                getView().onDataSourceSaved(result.getDataSourceDto());
            }}
        );
    }

}