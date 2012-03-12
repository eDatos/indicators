package es.gobcan.istac.indicators.web.client.system.presenter;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

import java.util.List;

import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.ElementLevelDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalGranularityDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalValueDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemStructureDto;
import es.gobcan.istac.indicators.web.client.NameTokens;
import es.gobcan.istac.indicators.web.client.PlaceRequestParams;
import es.gobcan.istac.indicators.web.client.events.UpdateGeographicalGranularitiesEvent;
import es.gobcan.istac.indicators.web.client.events.UpdateGeographicalGranularitiesEvent.UpdateGeographicalGranularitiesHandler;
import es.gobcan.istac.indicators.web.client.main.presenter.MainPagePresenter;
import es.gobcan.istac.indicators.web.client.utils.ErrorUtils;
import es.gobcan.istac.indicators.web.shared.CreateDimensionAction;
import es.gobcan.istac.indicators.web.shared.CreateDimensionResult;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.CreateIndicatorInstanceResult;
import es.gobcan.istac.indicators.web.shared.DeleteDimensionAction;
import es.gobcan.istac.indicators.web.shared.DeleteDimensionResult;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorInstanceResult;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorListResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemStructureAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemStructureResult;
import es.gobcan.istac.indicators.web.shared.MoveSystemStructureContentAction;
import es.gobcan.istac.indicators.web.shared.MoveSystemStructureContentResult;
import es.gobcan.istac.indicators.web.shared.UpdateDimensionAction;
import es.gobcan.istac.indicators.web.shared.UpdateDimensionResult;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorInstanceAction;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorInstanceResult;

public class SystemPresenter extends Presenter<SystemPresenter.SystemView, SystemPresenter.SystemProxy> implements SystemUiHandler, UpdateGeographicalGranularitiesHandler {
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> GENERAL_SLOT = new Type<RevealContentHandler<?>>();
	@ContentSlot
	public static final Type<RevealContentHandler<?>> STRUCTURE_SLOT = new Type<RevealContentHandler<?>>();
	
	private PlaceManager placeManager;
	private DispatchAsync dispatcher;
	
	/* Models*/
	private IndicatorsSystemDto indSystem;
	private String codeLastStructure;

	public interface SystemView extends View, HasUiHandlers<SystemUiHandler> {
	    void init();
	    
	    void setIndicatorFromIndicatorInstance(IndicatorDto indicator);
	    void setIndicators(List<IndicatorDto> indicators);
		void setIndicatorsSystem(IndicatorsSystemDto indicatorSystem);
		void setIndicatorsSystemStructure(IndicatorsSystemDto indicatorsSystem, IndicatorsSystemStructureDto structure);
		void onDimensionSaved(DimensionDto dimension);
		void onIndicatorInstanceSaved(IndicatorInstanceDto instance);
		
		void setGeographicalGranularities(List<GeographicalGranularityDto> geographicalGranularityDtos);
		void setGeographicalValues(List<GeographicalValueDto> geographicalValueDtos);
	}
	
	@ProxyCodeSplit
	@NameToken(NameTokens.systemPage)
	public interface SystemProxy extends Proxy<SystemPresenter>, Place {
	}
	
	@Inject
	public SystemPresenter(EventBus eventBus, SystemView view, SystemProxy proxy, DispatchAsync dispatcher,PlaceManager placeManager) {
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
	
	@Override
	public void retrieveIndSystem(String indSystemCode) {
		dispatcher.execute(new GetIndicatorsSystemAction(indSystemCode), new AsyncCallback<GetIndicatorsSystemResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().systemErrorRetrieve()), MessageTypeEnum.ERROR);
			}
			@Override
			public void onSuccess(GetIndicatorsSystemResult result) {
				SystemPresenter.this.indSystem = result.getIndicatorsSystem();
				getView().setIndicatorsSystem(result.getIndicatorsSystem());
			}
		});
	}
	
	
	@Override
	public void retrieveSystemStructure() {
		if (indSystem != null && !indSystem.getCode().equals(codeLastStructure)) {
			codeLastStructure = indSystem.getCode();
			dispatcher.execute(new GetIndicatorsSystemStructureAction(indSystem.getCode()), new AsyncCallback<GetIndicatorsSystemStructureResult>() {
				@Override
				public void onFailure(Throwable caught) {
					ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().systemStrucErrorRetrieve()), MessageTypeEnum.ERROR);
				}
				@Override
				public void onSuccess(GetIndicatorsSystemStructureResult result) {
					getView().setIndicatorsSystemStructure(indSystem, result.getStructure());
				}
			});
		}
	}
	
	@Override
	public void retrieveIndicators() {
	    dispatcher.execute(new GetIndicatorListAction(), new AsyncCallback<GetIndicatorListResult>() {
	        @Override
	        public void onFailure(Throwable caught) {
	            ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().systemStrucDimErrorCreate()), MessageTypeEnum.ERROR);
	        }
	        @Override
	        public void onSuccess(GetIndicatorListResult result) {
	            getView().setIndicators(result.getIndicatorList());
	        }
	    });
	}
	
	@Override
	public void retrieveIndicatorFromIndicatorInstance(String uuid) {
	    dispatcher.execute(new GetIndicatorAction(uuid), new AsyncCallback<GetIndicatorResult>() {
	        @Override
	        public void onFailure(Throwable caught) {
	            ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().systemStrucDimErrorCreate()), MessageTypeEnum.ERROR);
	        }
	        @Override
	        public void onSuccess(GetIndicatorResult result) {
	            getView().setIndicatorFromIndicatorInstance(result.getIndicator());
	        }
	    });
	}
	
	@Override
	public void createDimension(IndicatorsSystemDto system, DimensionDto dimension) {
		dispatcher.execute(new CreateDimensionAction(system, dimension), new AsyncCallback<CreateDimensionResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().systemStrucDimErrorCreate()), MessageTypeEnum.ERROR);
			}
			@Override
			public void onSuccess(CreateDimensionResult result) {
				retrieveSystemStructureNoCache();
				ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getMessageList(getMessages().systemStrucDimCreated()), MessageTypeEnum.SUCCESS);
				getView().onDimensionSaved(result.getCreatedDimension());
			}
		});
	}
	
	@Override
	public void updateDimension(DimensionDto dimension) {
		dispatcher.execute(new UpdateDimensionAction(dimension), new AsyncCallback<UpdateDimensionResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().systemStrucDimErrorSave()), MessageTypeEnum.ERROR);
			}
			@Override
			public void onSuccess(UpdateDimensionResult result) {
				retrieveSystemStructureNoCache();
				ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getMessageList(getMessages().systemStrucDimSaved()), MessageTypeEnum.SUCCESS);
				getView().onDimensionSaved(result.getDimension());
			}
		});
	}
	
	@Override
	public void deleteDimension(DimensionDto dimension) {
		dispatcher.execute(new DeleteDimensionAction(dimension.getUuid()), new AsyncCallback<DeleteDimensionResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().systemStrucDimErrorDelete()), MessageTypeEnum.ERROR);
			}
			@Override
			public void onSuccess(DeleteDimensionResult result) {
				retrieveSystemStructureNoCache();
				ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getMessageList(getMessages().systemStrucDimDeleted()), MessageTypeEnum.SUCCESS);
			}
		});
	}
	
	@Override
	public void createIndicatorInstance(IndicatorsSystemDto system, IndicatorInstanceDto instance) {
		dispatcher.execute(new CreateIndicatorInstanceAction(system, instance), new AsyncCallback<CreateIndicatorInstanceResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().systemStrucIndInstanceErrorCreate()), MessageTypeEnum.ERROR);
			}
			@Override
			public void onSuccess(CreateIndicatorInstanceResult result) {
				retrieveSystemStructureNoCache();
				ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getMessageList(getMessages().systemStrucIndInstanceCreated()), MessageTypeEnum.SUCCESS);
				getView().onIndicatorInstanceSaved(result.getCreatedIndicatorInstance());
			}
		});
	}
	
	@Override
	public void updateIndicatorInstance(IndicatorInstanceDto indicatorInstance) {
		final IndicatorInstanceDto instance = indicatorInstance;
		dispatcher.execute(new UpdateIndicatorInstanceAction(instance), new AsyncCallback<UpdateIndicatorInstanceResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().systemStrucIndInstanceErrorSave()), MessageTypeEnum.ERROR);
			}
			@Override
			public void onSuccess(UpdateIndicatorInstanceResult result) {
				retrieveSystemStructureNoCache();
				ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getMessageList(getMessages().systemStrucIndInstanceSaved()), MessageTypeEnum.SUCCESS);
				getView().onIndicatorInstanceSaved(result.getIndicatorInstance());
			}
		});
	}
	
	@Override
	public void moveSystemStructureNodes(String systemUuid, String targetUuid, ElementLevelDto level, Long newOrder) {
		dispatcher.execute(new MoveSystemStructureContentAction(systemUuid, targetUuid, newOrder, level), new AsyncCallback<MoveSystemStructureContentResult>() {
			@Override
			public void onFailure(Throwable caught) {
				retrieveSystemStructureNoCache();
				ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().systemStrucNodesErrorMove()), MessageTypeEnum.ERROR);
			}
			@Override
			public void onSuccess(MoveSystemStructureContentResult result) {
				retrieveSystemStructureNoCache();
			}
		});
	}
	
	@Override
	public void deleteIndicatorInstance(IndicatorInstanceDto instance) {
		dispatcher.execute(new DeleteIndicatorInstanceAction(instance.getUuid()), new AsyncCallback<DeleteIndicatorInstanceResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().systemStrucIndInstanceErrorDelete()), MessageTypeEnum.ERROR);
			}
			@Override
			public void onSuccess(DeleteIndicatorInstanceResult result) {
				retrieveSystemStructureNoCache();
				ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getMessageList(getMessages().systemStrucIndInstanceDeleted()), MessageTypeEnum.SUCCESS);
			}
		});
	}
	
	private void retrieveSystemStructureNoCache() {
		codeLastStructure = indSystem.getCode();
		dispatcher.execute(new GetIndicatorsSystemStructureAction(indSystem.getCode()), new AsyncCallback<GetIndicatorsSystemStructureResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().systemStrucErrorRetrieve()), MessageTypeEnum.ERROR);
			}
			@Override
			public void onSuccess(GetIndicatorsSystemStructureResult result) {
				getView().setIndicatorsSystemStructure(indSystem, result.getStructure());
			}
		});
	}
	
	@Override
	public void retrieveGeographicalValues(String geographicalGranularityUuid) {
	    dispatcher.execute(new GetGeographicalValuesAction(geographicalGranularityUuid), new AsyncCallback<GetGeographicalValuesResult>() {
	        @Override
	        public void onFailure(Throwable caught) {
	            ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorLoadingGeographicalValues()), MessageTypeEnum.ERROR);
	        }
	        @Override
	        public void onSuccess(GetGeographicalValuesResult result) {
	            getView().setGeographicalValues(result.getGeographicalValueDtos());
	        }
	    });
	}

	@ProxyEvent
    @Override
    public void onUpdateGeographicalGranularities(UpdateGeographicalGranularitiesEvent event) {
        getView().setGeographicalGranularities(event.getGeographicalGranularities());
    }
   
}
