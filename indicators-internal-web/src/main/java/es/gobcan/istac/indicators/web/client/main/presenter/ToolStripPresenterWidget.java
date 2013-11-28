package es.gobcan.istac.indicators.web.client.main.presenter;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.HasClickHandlers;

import es.gobcan.istac.indicators.web.client.NameTokens;

public class ToolStripPresenterWidget extends PresenterWidget<ToolStripPresenterWidget.ToolStripView> {

    private final PlaceManager placeManager;

    public interface ToolStripView extends View {

        HasClickHandlers getIndicatorsSystemsButton();
        HasClickHandlers getIndicatorsButton();
        HasClickHandlers getAdminButton();
    }

    @Inject
    public ToolStripPresenterWidget(EventBus eventBus, ToolStripView toolStripView, PlaceManager placeManager) {
        super(eventBus, toolStripView);
        this.placeManager = placeManager;
    }

    @Override
    protected void onBind() {
        super.onBind();

        registerHandler(getView().getIndicatorsSystemsButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                PlaceRequest request = new PlaceRequest(NameTokens.systemListPage);
                List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
                placeRequestHierarchy.add(request);
                placeManager.revealPlaceHierarchy(placeRequestHierarchy);
            }
        }));

        registerHandler(getView().getIndicatorsButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                PlaceRequest request = new PlaceRequest(NameTokens.indicatorListPage);
                List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
                placeRequestHierarchy.add(request);
                placeManager.revealPlaceHierarchy(placeRequestHierarchy);
            }
        }));

        registerHandler(getView().getAdminButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                PlaceRequest request = new PlaceRequest(NameTokens.adminPage);
                List<PlaceRequest> placeRequestHierarchy = new ArrayList<PlaceRequest>();
                placeRequestHierarchy.add(request);
                placeManager.revealPlaceHierarchy(placeRequestHierarchy);
            }
        }));
    }

    @Override
    protected void onReveal() {
        super.onReveal();
    }

    @Override
    protected void onHide() {
        super.onHide();
    }

    @Override
    protected void onUnbind() {
        super.onUnbind();
    }

    @Override
    protected void onReset() {
        super.onReset();
    }

}
