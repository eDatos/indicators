package es.gobcan.istac.indicadores.web.client.indicator.presenter;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import es.gobcan.istac.indicadores.web.client.NameTokens;
import es.gobcan.istac.indicadores.web.client.PlaceRequestParams;
import es.gobcan.istac.indicadores.web.client.enums.MessageTypeEnum;
import es.gobcan.istac.indicadores.web.client.events.ShowMessageEvent;
import es.gobcan.istac.indicadores.web.client.main.presenter.MainPagePresenter;
import es.gobcan.istac.indicadores.web.client.utils.ErrorUtils;
import es.gobcan.istac.indicadores.web.shared.GetIndicatorAction;
import es.gobcan.istac.indicadores.web.shared.GetIndicatorResult;
import es.gobcan.istac.indicadores.web.shared.db.Indicator;

public class IndicatorPresenter extends Presenter<IndicatorPresenter.IndicatorView, IndicatorPresenter.IndicatorProxy>{
	private DispatchAsync dispatcher;
	private Long indicatorId;
	
	public interface IndicatorView extends View {
		void setIndicator(Indicator indicator);
	}
	
	@ProxyCodeSplit
	@NameToken(NameTokens.indicatorPage)
	public interface IndicatorProxy extends Proxy<IndicatorPresenter>, Place {}
	
	@Inject
	public IndicatorPresenter(EventBus eventBus, IndicatorView view, IndicatorProxy proxy, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		this.dispatcher = dispatcher;
	}
	
	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPagePresenter.CONTENT_SLOT, this);
	}
	
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		indicatorId = Long.parseLong(request.getParameter(PlaceRequestParams.indicatorParam, null));
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		retrieveIndicator();
	}
	
	private void retrieveIndicator() {
		dispatcher.execute(new GetIndicatorAction(this.indicatorId), new AsyncCallback<GetIndicatorResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowMessageEvent.fire(IndicatorPresenter.this, ErrorUtils.getMessageList(caught, "Error al cargar el Indicador"), MessageTypeEnum.ERROR);
			}

			@Override
			public void onSuccess(GetIndicatorResult result) {
				getView().setIndicator(result.getIndicator());
			}
			
		});
	}

}
