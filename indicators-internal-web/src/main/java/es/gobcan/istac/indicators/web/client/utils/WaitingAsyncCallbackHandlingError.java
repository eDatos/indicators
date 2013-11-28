package es.gobcan.istac.indicators.web.client.utils;

import org.siemac.metamac.web.common.client.enums.MessageTypeEnum;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;

import com.google.gwt.event.shared.HasHandlers;

public abstract class WaitingAsyncCallbackHandlingError<T> extends WaitingAsyncCallback<T> {

    private HasHandlers source;

    private String      errorMesssage;

    public WaitingAsyncCallbackHandlingError(HasHandlers source, String alternativeErrorMessage) {
        this.source = source;
        this.errorMesssage = alternativeErrorMessage;
    }

    @Override
    public void onWaitFailure(Throwable caught) {
        ShowMessageEvent.fire(source, ErrorUtils.getErrorMessages(caught, errorMesssage), MessageTypeEnum.ERROR);
    }

}
