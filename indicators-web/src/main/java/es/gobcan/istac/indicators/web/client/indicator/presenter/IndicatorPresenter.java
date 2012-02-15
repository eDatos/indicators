package es.gobcan.istac.indicators.web.client.indicator.presenter;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

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
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.web.client.NameTokens;
import es.gobcan.istac.indicators.web.client.PlaceRequestParams;
import es.gobcan.istac.indicators.web.client.enums.MessageTypeEnum;
import es.gobcan.istac.indicators.web.client.events.ShowMessageEvent;
import es.gobcan.istac.indicators.web.client.main.presenter.MainPagePresenter;
import es.gobcan.istac.indicators.web.client.utils.ErrorUtils;
import es.gobcan.istac.indicators.web.shared.GetIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetIndicatorResult;
import es.gobcan.istac.indicators.web.shared.SaveIndicatorAction;
import es.gobcan.istac.indicators.web.shared.SaveIndicatorResult;

public class IndicatorPresenter extends Presenter<IndicatorPresenter.IndicatorView, IndicatorPresenter.IndicatorProxy> implements IndicatorUiHandler {
	private DispatchAsync dispatcher;
	private String indicatorCode;
	
	public interface IndicatorView extends View, HasUiHandlers<IndicatorPresenter> {
		void setIndicator(IndicatorDto indicator);
	}
	
	@ProxyCodeSplit
	@NameToken(NameTokens.indicatorPage)
	public interface IndicatorProxy extends Proxy<IndicatorPresenter>, Place {}
	
	@Inject
	public IndicatorPresenter(EventBus eventBus, IndicatorView view, IndicatorProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		this.dispatcher = dispatcher;
		view.setUiHandlers(this);
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
		retrieveIndicator();
	}
	
	private void retrieveIndicator() {
		dispatcher.execute(new GetIndicatorAction(this.indicatorCode), new AsyncCallback<GetIndicatorResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getMessageList(caught, getMessages().indicErrorRetrieve()), MessageTypeEnum.ERROR);
			}

			@Override
			public void onSuccess(GetIndicatorResult result) {
				getView().setIndicator(result.getIndicator());
			}
			
		});
	}
	
	/* UiHandlers */
	public void saveIndicator(IndicatorDto indicator) {
	    dispatcher.execute(new SaveIndicatorAction(indicator), new AsyncCallback<SaveIndicatorResult>() {
	       @Override
	        public void onFailure(Throwable caught) {
	           ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getMessageList(caught, getMessages().indicErrorSave()), MessageTypeEnum.ERROR);
	        }
	       
	       @Override
	        public void onSuccess(SaveIndicatorResult result) {
	           retrieveIndicator();
	        }
	    });
	}

}
