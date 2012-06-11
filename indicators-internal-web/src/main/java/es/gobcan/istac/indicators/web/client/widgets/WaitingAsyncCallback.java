package es.gobcan.istac.indicators.web.client.widgets;

import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class WaitingAsyncCallback<T> implements AsyncCallback<T> {

    private WaitPopup waitPopup;

    public WaitingAsyncCallback() {
        startWaiting();
    }

    @Override
    public void onFailure(Throwable caught) {
        onWaitFailure(caught);
        stopWaiting();
    }

    @Override
    public void onSuccess(T result) {
        onWaitSuccess(result);
        stopWaiting();
    }

    public abstract void onWaitFailure(Throwable caught);
    public abstract void onWaitSuccess(T result);

    private void startWaiting() {
        waitPopup = new WaitPopup();
        waitPopup.show("");
    }

    private void stopWaiting() {
        if (waitPopup != null) {
            waitPopup.hideFinal();
        }
    }
}
