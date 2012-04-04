package es.gobcan.istac.indicators.web.client.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;


public abstract class WaitingAsyncCallback<T> implements AsyncCallback<T> {
    
    
    public WaitingAsyncCallback() {
        showWaitCursor();
    }

    @Override
    public void onFailure(Throwable caught) {
        onWaitFailure(caught);
        showDefaultCursor();
    }

    @Override
    public void onSuccess(T result) {
        onWaitSuccess(result);
        showDefaultCursor();
    }
    
    public abstract void onWaitFailure(Throwable caught);
    public abstract void onWaitSuccess(T result);
        
    private void showWaitCursor() {
        DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "progress");
    }
    
    private void showDefaultCursor() {
        DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
    }
}
