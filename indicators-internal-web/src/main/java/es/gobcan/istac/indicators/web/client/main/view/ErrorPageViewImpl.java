package es.gobcan.istac.indicators.web.client.main.view;

import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.VLayout;

import es.gobcan.istac.indicators.web.client.main.presenter.ErrorPagePresenter;
import es.gobcan.istac.indicators.web.client.main.view.handlers.ErrorPageUiHandlers;

public class ErrorPageViewImpl extends ViewWithUiHandlers<ErrorPageUiHandlers> implements ErrorPagePresenter.ErrorPageView {

    private VLayout panel;

    public ErrorPageViewImpl() {
        super();
        panel = new VLayout();
        panel.addMember(new Label("Error"));
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

}
