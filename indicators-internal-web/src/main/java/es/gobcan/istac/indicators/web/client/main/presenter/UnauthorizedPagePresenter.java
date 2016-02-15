package es.gobcan.istac.indicators.web.client.main.presenter;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import es.gobcan.istac.indicators.core.navigation.shared.NameTokens;
import es.gobcan.istac.indicators.web.client.main.view.handlers.UnauthorizedPageUiHandlers;

public class UnauthorizedPagePresenter extends Presenter<UnauthorizedPagePresenter.UnauthorizedPageView, UnauthorizedPagePresenter.UnauthorizedPageProxy> implements UnauthorizedPageUiHandlers {

    @ProxyCodeSplit
    @NameToken(NameTokens.unauthorizedAccessPage)
    @NoGatekeeper
    public interface UnauthorizedPageProxy extends Proxy<UnauthorizedPagePresenter>, Place {

    }

    public interface UnauthorizedPageView extends View, HasUiHandlers<UnauthorizedPageUiHandlers> {

    }

    @Inject
    public UnauthorizedPagePresenter(EventBus eventBus, UnauthorizedPageView view, UnauthorizedPageProxy proxy) {
        super(eventBus, view, proxy);
        getView().setUiHandlers(this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.CONTENT_SLOT, this);
    }

}
