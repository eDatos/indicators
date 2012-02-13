package es.gobcan.istac.indicators.web.client.indicator.presenter;

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
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.web.client.NameTokens;
import es.gobcan.istac.indicators.web.client.enums.MessageTypeEnum;
import es.gobcan.istac.indicators.web.client.events.ShowMessageEvent;
import es.gobcan.istac.indicators.web.client.main.presenter.MainPagePresenter;
import es.gobcan.istac.indicators.web.client.utils.ErrorUtils;
import es.gobcan.istac.indicators.web.shared.GetIndicatorListAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorListResult;
import es.gobcan.istac.indicators.web.shared.SaveIndicatorAction;
import es.gobcan.istac.indicators.web.shared.SaveIndicatorResult;

public class IndicatorListPresenter extends Presenter<IndicatorListPresenter.IndicatorListView, IndicatorListPresenter.IndicatorListProxy> implements IndicatorListUiHandler {
	
	private DispatchAsync dispatcher;
	
	public interface IndicatorListView extends View, HasUiHandlers<IndicatorListPresenter> {
		void setIndicatorList(List<IndicatorDto> indicatorList);
	}
	
	@ProxyCodeSplit
	@NameToken(NameTokens.indicatorListPage)
	public interface IndicatorListProxy extends Proxy<IndicatorListPresenter>, Place {}

	@Inject
	public IndicatorListPresenter(EventBus eventBus, IndicatorListView view, IndicatorListProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		this.dispatcher = dispatcher;
		getView().setUiHandlers(this);
	}
	
	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.CONTENT_SLOT, this);
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		retrieveIndicatorList();
	}
	
	private void retrieveIndicatorList() {
		dispatcher.execute(new GetIndicatorListAction(), new AsyncCallback<GetIndicatorListResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowMessageEvent.fire(IndicatorListPresenter.this, ErrorUtils.getMessageList(caught, getMessages().indicErrorRetrieveList()), MessageTypeEnum.ERROR);
			}
			@Override
			public void onSuccess(GetIndicatorListResult result) {
				getView().setIndicatorList(result.getIndicatorList());
			}
		});
	}
	
	//Manejo de eventos
	@Override
	public void createIndicator(IndicatorDto indicator) {
		dispatcher.execute(new SaveIndicatorAction(indicator), new AsyncCallback<SaveIndicatorResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowMessageEvent.fire(IndicatorListPresenter.this, ErrorUtils.getMessageList(caught, getMessages().indicErrorCreate()), MessageTypeEnum.ERROR);
			}
			@Override
			public void onSuccess(SaveIndicatorResult result) {
				retrieveIndicatorList(); 
				ShowMessageEvent.fire(IndicatorListPresenter.this, ErrorUtils.getMessageList(getMessages().indicCreated()), MessageTypeEnum.SUCCESS);
			}
		});
	}
	
	@Override
	public void reloadIndicatorList() {
		retrieveIndicatorList();
	}
}
