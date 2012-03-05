package es.gobcan.istac.indicators.web.client.main.presenter;

import java.util.List;

import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.HideMessageEvent;
import org.siemac.metamac.web.common.client.events.HideMessageEvent.HideMessageHandler;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent.ShowMessageHandler;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;

import es.gobcan.istac.indicators.web.client.NameTokens;

public class MainPagePresenter extends Presenter<MainPagePresenter.MainView, MainPagePresenter.MainProxy> implements ShowMessageHandler, HideMessageHandler {
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> CONTENT_SLOT = new Type<RevealContentHandler<?>>();
	
	public interface MainView extends View{
		void showMessage(List<String> messages, MessageTypeEnum type);
		void hideMessages();
	}
	
	@ProxyStandard
	@NameToken(NameTokens.mainPage)
	public interface MainProxy extends Proxy<MainPagePresenter>, Place {}
	
	@Inject
	public MainPagePresenter(EventBus eventBus, MainView view, MainProxy proxy) {
		super(eventBus,view,proxy);
	}

	@Override
	protected void revealInParent() {
		RevealRootContentEvent.fire(this,this);
	}
	
	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(ShowMessageEvent.getType(), this);
	}
	
	@Override
	public void onShowMessage(ShowMessageEvent event) {
		getView().showMessage(event.getMessages(), event.getMessageType());
	}
	
	@Override
	public void onHideMessage(HideMessageEvent event) {
		getView().hideMessages();
	}

}
