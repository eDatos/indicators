package es.gobcan.istac.indicators.web.client.main.presenter;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.HideMessageEvent;
import org.siemac.metamac.web.common.client.events.HideMessageEvent.HideMessageHandler;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.SetTitleEvent.SetTitleHandler;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent.ShowMessageHandler;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;

import es.gobcan.istac.indicators.web.client.NameTokens;
import es.gobcan.istac.indicators.web.client.events.UpdateGeographicalGranularitiesEvent;
import es.gobcan.istac.indicators.web.client.events.UpdateQuantityUnitsEvent;
import es.gobcan.istac.indicators.web.client.main.view.handlers.MainPageUiHandlers;
import es.gobcan.istac.indicators.web.client.utils.ErrorUtils;
import es.gobcan.istac.indicators.web.client.widgets.WaitingAsyncCallback;
import es.gobcan.istac.indicators.web.shared.CloseSessionAction;
import es.gobcan.istac.indicators.web.shared.CloseSessionResult;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularitiesAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalGranularitiesResult;
import es.gobcan.istac.indicators.web.shared.GetQuantityUnitsListAction;
import es.gobcan.istac.indicators.web.shared.GetQuantityUnitsListResult;

public class MainPagePresenter extends Presenter<MainPagePresenter.MainView, MainPagePresenter.MainProxy> implements ShowMessageHandler, HideMessageHandler, MainPageUiHandlers, SetTitleHandler {

    private static Logger                             logger       = Logger.getLogger(MainPagePresenter.class.getName());

    private final DispatchAsync                       dispatcher;

    @ContentSlot
    public static final Type<RevealContentHandler<?>> CONTENT_SLOT = new Type<RevealContentHandler<?>>();

    public interface MainView extends View, HasUiHandlers<MainPageUiHandlers> {

        void showMessage(List<String> messages, MessageTypeEnum type);
        void hideMessages();
        void setTitle(String title);
    }

    @ProxyStandard
    @NameToken(NameTokens.mainPage)
    public interface MainProxy extends Proxy<MainPagePresenter>, Place {
    }

    @Inject
    public MainPagePresenter(EventBus eventBus, MainView view, MainProxy proxy, DispatchAsync dispatcher) {
        super(eventBus, view, proxy);
        getView().setUiHandlers(this);
        this.dispatcher = dispatcher;
    }

    @Override
    protected void revealInParent() {
        RevealRootContentEvent.fire(this, this);
    }

    @Override
    protected void onBind() {
        super.onBind();
        addRegisteredHandler(ShowMessageEvent.getType(), this);
        // TODO Is this the proper place to load value lists?
        loadQuantityUnits();
        loadGeographicalGranularities();
    }

    @Override
    protected void onReset() {
        super.onReset();
        hideMessages();
    }

    @Override
    public void onShowMessage(ShowMessageEvent event) {
        getView().showMessage(event.getMessages(), event.getMessageType());
    }

    @ProxyEvent
    @Override
    public void onHideMessage(HideMessageEvent event) {
        hideMessages();
    }

    private void hideMessages() {
        getView().hideMessages();
    }

    private void loadQuantityUnits() {
        dispatcher.execute(new GetQuantityUnitsListAction(), new WaitingAsyncCallback<GetQuantityUnitsListResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(MainPagePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRetrievingQuantityUnits()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetQuantityUnitsListResult result) {
                UpdateQuantityUnitsEvent.fire(MainPagePresenter.this, result.getQuantityUnits());
            }
        });
    }

    private void loadGeographicalGranularities() {
        dispatcher.execute(new GetGeographicalGranularitiesAction(), new WaitingAsyncCallback<GetGeographicalGranularitiesResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fire(MainPagePresenter.this, ErrorUtils.getErrorMessages(caught, getMessages().errorRetrievingGeographicalGranularities()), MessageTypeEnum.ERROR);
            }
            @Override
            public void onWaitSuccess(GetGeographicalGranularitiesResult result) {
                UpdateGeographicalGranularitiesEvent.fire(MainPagePresenter.this, result.getGeographicalGranularityDtos());
            }
        });
    }

    @ProxyEvent
    @Override
    public void onSetTitle(SetTitleEvent event) {
        getView().setTitle(event.getTitle());
    }

    @Override
    public void closeSession() {
        dispatcher.execute(new CloseSessionAction(), new AsyncCallback<CloseSessionResult>() {

            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error closing session");
            }
            @Override
            public void onSuccess(CloseSessionResult result) {
                Window.Location.assign(result.getLogoutPageUrl());
            }
        });
    }

}
