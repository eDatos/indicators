package es.gobcan.istac.indicators.web.client.system.presenter;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

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

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.web.client.NameTokens;
import es.gobcan.istac.indicators.web.client.PlaceRequestParams;
import es.gobcan.istac.indicators.web.client.enums.MessageTypeEnum;
import es.gobcan.istac.indicators.web.client.events.ShowMessageEvent;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorListPresenter;
import es.gobcan.istac.indicators.web.client.main.presenter.MainPagePresenter;
import es.gobcan.istac.indicators.web.client.utils.ErrorUtils;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemListResult;

public class SystemListPresenter extends Presenter<SystemListPresenter.SystemListView, SystemListPresenter.SystemListProxy> implements SystemListUiHandler {
	
	public interface SystemListView extends View, HasUiHandlers<SystemListUiHandler> {
		void setIndSystemList(List<IndicatorsSystemDto> indSysList);
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
		/*IndicatorSystemDto system = new IndicatorSystem();
		system.setName(name);
		dispatcher.execute(new SaveIndicatorSystemAction(system), new AsyncCallback<SaveIndicatorSystemResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowMessageEvent.fire(SystemListPresenter.this, ErrorUtils.getMessageList(caught, getMessages().systemErrorCreate()), MessageTypeEnum.ERROR);
			}
			@Override
			public void onSuccess(SaveIndicatorSystemResult result) {
				retrieveIndicatorSystemList();
				ShowMessageEvent.fire(SystemListPresenter.this, ErrorUtils.getMessageList(getMessages().systemCreated()), MessageTypeEnum.SUCCESS);
			}
		});*/
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
		dispatcher.execute(new GetIndicatorsSystemListAction(), new AsyncCallback<GetIndicatorsSystemListResult> () {
			@Override
			public void onFailure(Throwable caught) {
			    ShowMessageEvent.fire(SystemListPresenter.this, ErrorUtils.getMessageList(caught, getMessages().systemErrorRetrieveList()), MessageTypeEnum.ERROR);
			}

			@Override
			public void onSuccess(GetIndicatorsSystemListResult result) {
				getView().setIndSystemList(result.getIndicatorsSystemList());
			}
		});
	}
}
