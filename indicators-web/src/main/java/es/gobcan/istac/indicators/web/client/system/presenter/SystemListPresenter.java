package es.gobcan.istac.indicators.web.client.system.presenter;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

import java.util.List;

import org.siemac.metamac.web.common.client.utils.ErrorUtils;

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
import es.gobcan.istac.indicators.web.client.main.presenter.MainPagePresenter;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsSystemsAction;
import es.gobcan.istac.indicators.web.shared.DeleteIndicatorsSystemsResult;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorsSystemListResult;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorsSystemAction;
import es.gobcan.istac.indicators.web.shared.UpdateIndicatorsSystemResult;

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
	public void createIndicatorsSystem(IndicatorsSystemDto system) {
		dispatcher.execute(new UpdateIndicatorsSystemAction(system), new AsyncCallback<UpdateIndicatorsSystemResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowMessageEvent.fire(SystemListPresenter.this, caught, getMessages().systemErrorCreate());
			}
			@Override
			public void onSuccess(UpdateIndicatorsSystemResult result) {
				retrieveIndicatorSystemList();
				ShowMessageEvent.fire(SystemListPresenter.this, ErrorUtils.getMessageList(getMessages().systemCreated()), MessageTypeEnum.SUCCESS);
			}
		});
	}
	
	@Override
	public void deleteIndicatorsSystems(List<String> codes) {
		dispatcher.execute(new DeleteIndicatorsSystemsAction(codes), new AsyncCallback<DeleteIndicatorsSystemsResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowMessageEvent.fire(SystemListPresenter.this, caught, getMessages().systemErrorDelete());
			}

			@Override
			public void onSuccess(DeleteIndicatorsSystemsResult result) {
				retrieveIndicatorSystemList();
				ShowMessageEvent.fire(SystemListPresenter.this, ErrorUtils.getMessageList(getMessages().systemDeleted()), MessageTypeEnum.SUCCESS);
			}
		});
	}
	
	@Override
	public void reloadIndicatorsSystemList() {
		retrieveIndicatorSystemList();
	}
	
	@Override
	public void goToIndicatorsSystem(String indSysCode) {
		PlaceRequest systemDetailRequest = new PlaceRequest(NameTokens.systemPage).with(PlaceRequestParams.indSystemParam,indSysCode);
		placeManager.revealPlace(systemDetailRequest);
	}
	
	/**
	 * Private methods
	 */

	private void retrieveIndicatorSystemList() {
		dispatcher.execute(new GetIndicatorsSystemListAction(), new AsyncCallback<GetIndicatorsSystemListResult> () {
			@Override
			public void onFailure(Throwable caught) {
			    ShowMessageEvent.fire(SystemListPresenter.this, caught, getMessages().systemErrorRetrieveList());
			}

			@Override
			public void onSuccess(GetIndicatorsSystemListResult result) {
				getView().setIndSystemList(result.getIndicatorsSystemList());
			}
		});
	}
}
