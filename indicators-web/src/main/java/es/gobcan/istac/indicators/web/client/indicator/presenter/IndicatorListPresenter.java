package es.gobcan.istac.indicadores.web.client.indicator.presenter;

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

import es.gobcan.istac.indicadores.web.client.NameTokens;
import es.gobcan.istac.indicadores.web.client.enums.MessageTypeEnum;
import es.gobcan.istac.indicadores.web.client.events.ShowMessageEvent;
import es.gobcan.istac.indicadores.web.client.main.presenter.MainPagePresenter;
import es.gobcan.istac.indicadores.web.client.utils.ErrorUtils;
import es.gobcan.istac.indicadores.web.shared.GetIndicatorListAction;
import es.gobcan.istac.indicadores.web.shared.GetIndicatorListResult;
import es.gobcan.istac.indicadores.web.shared.SaveIndicatorAction;
import es.gobcan.istac.indicadores.web.shared.SaveIndicatorResult;
import es.gobcan.istac.indicadores.web.shared.db.Indicator;

public class IndicatorListPresenter extends Presenter<IndicatorListPresenter.IndicatorListView, IndicatorListPresenter.IndicatorListProxy> implements IndicatorListUiHandler {
	
	private DispatchAsync dispatcher;
	
	public interface IndicatorListView extends View, HasUiHandlers<IndicatorListPresenter> {
		void setIndicatorList(List<Indicator> indicatorList);
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
				ShowMessageEvent.fire(IndicatorListPresenter.this, ErrorUtils.getMessageList(caught, "Error al obtener el listado de Indicadores"), MessageTypeEnum.ERROR);
			}
			@Override
			public void onSuccess(GetIndicatorListResult result) {
				getView().setIndicatorList(result.getIndicatorList());
			}
		});
	}
	
	//Manejo de eventos
	@Override
	public void createIndicator(String name) {
		Indicator ind = new Indicator();
		ind.setName(name);
		dispatcher.execute(new SaveIndicatorAction(ind), new AsyncCallback<SaveIndicatorResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowMessageEvent.fire(IndicatorListPresenter.this, ErrorUtils.getMessageList(caught, "Error al obtener el listado de Indicadores"), MessageTypeEnum.ERROR);
			}
			@Override
			public void onSuccess(SaveIndicatorResult result) {
				retrieveIndicatorList(); 
				ShowMessageEvent.fire(IndicatorListPresenter.this, ErrorUtils.getMessageList("Se ha creado el Indicador"), MessageTypeEnum.SUCCESS);
			}
		});
	}
	
	@Override
	public void reloadIndicatorList() {
		retrieveIndicatorList();
	}
}
