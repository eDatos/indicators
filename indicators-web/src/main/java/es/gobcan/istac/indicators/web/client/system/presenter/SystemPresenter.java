package es.gobcan.istac.indicators.web.client.system.presenter;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

import java.util.List;

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
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.web.client.NameTokens;
import es.gobcan.istac.indicators.web.client.PlaceRequestParams;
import es.gobcan.istac.indicators.web.client.enums.MessageTypeEnum;
import es.gobcan.istac.indicators.web.client.events.ShowMessageEvent;
import es.gobcan.istac.indicators.web.client.main.presenter.MainPagePresenter;
import es.gobcan.istac.indicators.web.client.utils.ErrorUtils;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemStructureAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemStructureResult;
import es.gobcan.istac.indicators.web.shared.db.IndicatorSystemContent;

public class SystemPresenter extends Presenter<SystemPresenter.SystemView, SystemPresenter.SystemProxy> implements SystemUiHandler {
	
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
		void setIndicatorsSystem(IndicatorsSystemDto indicatorSystem);
		void setIndicatorsSystemStructure(List<IndicatorSystemContent> content);
		void init();
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
	
	private void retrieveIndSystem(String indSystemCode) {
		dispatcher.execute(new GetIndicatorsSystemAction(indSystemCode), new AsyncCallback<GetIndicatorsSystemResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getMessageList(caught, getMessages().systemErrorRetrieve()), MessageTypeEnum.ERROR);
			}

			@Override
			public void onSuccess(GetIndicatorsSystemResult result) {
				SystemPresenter.this.indSystem = result.getIndicatorsSystem();
				getView().setIndicatorsSystem(result.getIndicatorsSystem());
			}
		});
	}
	
	
	/* UiHandler */
	@Override
	public void goToGeneral() {
		if (NameTokens.systemPage.equals(placeManager.getCurrentPlaceRequest().getNameToken())) { //If we are in SystemPage
			placeManager.revealRelativePlace(new PlaceRequest(NameTokens.systemGeneralPage));
		} else if (!NameTokens.systemGeneralPage.equals(placeManager.getCurrentPlaceRequest().getNameToken())){ //If we are in other tab
			placeManager.revealRelativePlace(new PlaceRequest(NameTokens.systemGeneralPage),-1);
		}
	}
	
	/*public void goToStructure() {
		if (NameTokens.systemPage.equals(placeManager.getCurrentPlaceRequest().getNameToken())) { //If we are in SystemPage
			placeManager.revealRelativePlace(new PlaceRequest(NameTokens.systemStructurePage));
		} else if (!NameTokens.systemStructurePage.equals(placeManager.getCurrentPlaceRequest().getNameToken())){ //If we are in other tab
			placeManager.revealRelativePlace(new PlaceRequest(NameTokens.systemStructurePage),-1); 
		}		
	}*/
	
	@Override
	public void retrieveSystemStructure() {
		if (indSystem != null && !indSystem.getId().equals(codeLastStructure)) {
			codeLastStructure = indSystem.getCode();
			dispatcher.execute(new GetIndicatorsSystemStructureAction(indSystem.getId()), new AsyncCallback<GetIndicatorsSystemStructureResult>() {
				@Override
				public void onFailure(Throwable caught) {
					//TODO: mensaje error
				}
	
				@Override
				public void onSuccess(GetIndicatorsSystemStructureResult result) {
					getView().setIndicatorsSystemStructure(result.getListContent());
				}
				
			});
		}
	}
	
}
