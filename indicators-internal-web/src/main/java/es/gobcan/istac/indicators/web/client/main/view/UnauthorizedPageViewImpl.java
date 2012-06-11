package es.gobcan.istac.indicators.web.client.main.view;

import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.web.client.main.presenter.UnauthorizedPagePresenter;
import es.gobcan.istac.indicators.web.client.main.view.handlers.UnauthorizedPageUiHandlers;

public class UnauthorizedPageViewImpl extends ViewWithUiHandlers<UnauthorizedPageUiHandlers> implements UnauthorizedPagePresenter.UnauthorizedPageView {

    private VLayout panel;

    public UnauthorizedPageViewImpl() {
        super();
        panel = new VLayout();
        panel.addMember(new Label("Error"));
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

}
