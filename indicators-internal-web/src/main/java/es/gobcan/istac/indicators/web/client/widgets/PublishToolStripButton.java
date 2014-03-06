package es.gobcan.istac.indicators.web.client.widgets;

import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class PublishToolStripButton extends ToolStripButton {

    public PublishToolStripButton(String title, String icon) {
        super(title, icon);
        setVisibility(Visibility.HIDDEN);
        setWidth(150);
        setShowRollOver(true);
        setShowDisabled(true);
        setShowDown(true);
        setTitleStyle("mainFormLayoutButton");
    }

}
