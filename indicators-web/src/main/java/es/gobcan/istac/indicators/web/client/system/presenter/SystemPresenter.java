package es.gobcan.istac.indicators.web.client.system.presenter;

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

import es.gobcan.istac.indicators.web.client.NameTokens;
import es.gobcan.istac.indicators.web.client.PlaceRequestParams;
import es.gobcan.istac.indicators.web.client.enums.MessageTypeEnum;
import es.gobcan.istac.indicators.web.client.events.ShowMessageEvent;
import es.gobcan.istac.indicators.web.client.main.presenter.MainPagePresenter;
import es.gobcan.istac.indicators.web.client.utils.ErrorUtils;
import es.gobcan.istac.indicators.web.shared.GetIndicatorSystemAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorSystemResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorSystemStructureAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorSystemStructureResult;
import es.gobcan.istac.indicators.web.shared.db.IndicatorSystem;
import es.gobcan.istac.indicators.web.shared.db.IndicatorSystemContent;

public class SystemPresenter extends Presenter<SystemPresenter.SystemView, SystemPresenter.SystemProxy> implements SystemUiHandler {
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> GENERAL_SLOT = new Type<RevealContentHandler<?>>();
	@ContentSlot
	public static final Type<RevealContentHandler<?>> STRUCTURE_SLOT = new Type<RevealContentHandler<?>>();
	
	private PlaceManager placeManager;
	private DispatchAsync dispatcher;
	
	/* Models*/
	private IndicatorSystem indSystem;
	private Long lastStructureID;

	public interface SystemView extends View, HasUiHandlers<SystemUiHandler> {
		void setIndicatorSystem(IndicatorSystem indicatorSystem);
		void setIndicatorSystemStructure(List<IndicatorSystemContent> content);
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
		Long id = Long.parseLong(getIdFromRequest(placeManager.getCurrentPlaceHierarchy()));
		retrieveIndSystem(id); 
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
	
	private void retrieveIndSystem(Long indSystemId) {
		dispatcher.execute(new GetIndicatorSystemAction(indSystemId), new AsyncCallback<GetIndicatorSystemResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowMessageEvent.fire(SystemPresenter.this, ErrorUtils.getMessageList(caught, "Error al obtener el Sistema de Indicadores"), MessageTypeEnum.ERROR);
			}

			@Override
			public void onSuccess(GetIndicatorSystemResult result) {
				SystemPresenter.this.indSystem = result.getIndicatorSystem();
				getView().setIndicatorSystem(result.getIndicatorSystem());
				//SelectIndicatorSystemEvent.fire(SystemPresenter.this, result.getIndicatorSystem());
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
		if (indSystem != null && !indSystem.getId().equals(lastStructureID)) {
			lastStructureID = indSystem.getId();
			dispatcher.execute(new GetIndicatorSystemStructureAction(indSystem.getId()), new AsyncCallback<GetIndicatorSystemStructureResult>() {
				@Override
				public void onFailure(Throwable caught) {
					//TODO: mensaje error
				}
	
				@Override
				public void onSuccess(GetIndicatorSystemStructureResult result) {
					getView().setIndicatorSystemStructure(result.getListContent());
				}
				
			});
		}
	}
	
}
