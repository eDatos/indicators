package es.gobcan.istac.indicators.web.client.widgets;

import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;

public class LoadingWindow extends Window {

    public LoadingWindow() {
        Label label = new Label(" Waiting for server...");
        this.setShowCloseButton(false);
        this.setShowMinimizeButton(false);
        this.setShowBody(true);
        this.setShowHeader(true);
        this.setShowMaximizeButton(false);
        this.setTitle("");
        this.setShowTitle(false);
        this.setAutoCenter(true);
        this.setCanDragReposition(false);
        this.setCanDragResize(false);
        this.setWidth(300);
        this.setHeight(130);
        this.setShowModalMask(true);
        this.setIsModal(true);
        this.addItem(label);
        this.setOverflow(Overflow.HIDDEN);
    }
}
