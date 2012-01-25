package es.gobcan.istac.indicators.web.client.system.presenter;

import java.util.ArrayList;
import java.util.List;

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

import es.gobcan.istac.indicators.web.client.NameTokens;
import es.gobcan.istac.indicators.web.client.PlaceRequestParams;
import es.gobcan.istac.indicators.web.client.enums.MessageTypeEnum;
import es.gobcan.istac.indicators.web.client.events.ShowMessageEvent;
import es.gobcan.istac.indicators.web.client.main.presenter.MainPagePresenter;
import es.gobcan.istac.indicators.web.client.utils.ErrorUtils;
import es.gobcan.istac.indicators.web.shared.GetIndicatorSystemListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorSystemListResult;
import es.gobcan.istac.indicators.web.shared.SaveIndicatorSystemAction;
import es.gobcan.istac.indicators.web.shared.SaveIndicatorSystemResult;
import es.gobcan.istac.indicators.web.shared.db.IndicatorSystem;

public class SystemListPresenter extends Presenter<SystemListPresenter.SystemListView, SystemListPresenter.SystemListProxy> implements SystemListUiHandler {
	
	public interface SystemListView extends View, HasUiHandlers<SystemListUiHandler> {
		void setIndSystemList(List<IndicatorSystem> indSysList);
	}
	
	@ProxyCodeSplit
	@NameToken(NameTokens.systemListPage)
	public interface SystemListProxy extends Proxy<SystemListPresenter>, Place {}
	
	
	private PlaceManager placeManager;
	private DispatchAsync dispatcher;
	
	@Inject
	public SystemListPresenter(EventBus eventBus, SystemListView view, SystemListProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager) {
		super(eventBus,view,proxy);
		this.dispatcher = dispatcher;
		this.placeManager = placeManager;
		view.setUiHandlers(this); //Este presenter será el manejador de eventos
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.CONTENT_SLOT, this);
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		retrieveIndicatorSystemList();
	}
	

	/**
	 * View Event Handlers 
	 */
	@Override
	public void createIndicatorSystem(String name) {
		IndicatorSystem system = new IndicatorSystem();
		system.setName(name);
		dispatcher.execute(new SaveIndicatorSystemAction(system), new AsyncCallback<SaveIndicatorSystemResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowMessageEvent.fire(SystemListPresenter.this, ErrorUtils.getMessageList(caught, "Error al crear Sistema de Indicadores"), MessageTypeEnum.ERROR);
			}
			@Override
			public void onSuccess(SaveIndicatorSystemResult result) {
				retrieveIndicatorSystemList();
				ShowMessageEvent.fire(SystemListPresenter.this, ErrorUtils.getMessageList("Se ha creado el Sistema de Indicadores"), MessageTypeEnum.SUCCESS);
			}
		});
	}
	
	@Override
	public void reloadIndicatorSystemList() {
		retrieveIndicatorSystemList();
	}
	
	@Override
	public void goToIndicatorSystem(String indSysId) {
		PlaceRequest systemDetailRequest = new PlaceRequest(NameTokens.systemPage).with(PlaceRequestParams.indSystemParam,indSysId);
		placeManager.revealPlace(systemDetailRequest);
	}
	
	/**
	 * Private methods
	 */

	private void retrieveIndicatorSystemList() {
		dispatcher.execute(new GetIndicatorSystemListAction(), new AsyncCallback<GetIndicatorSystemListResult> () {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(GetIndicatorSystemListResult result) {
				getView().setIndSystemList(result.getIndSysList());
			}
		});
	}
}
