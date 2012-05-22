package es.gobcan.istac.indicators.web.client.widgets;

import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;

import es.gobcan.istac.indicators.web.client.IndicatorsWeb;

public class VersionFooter extends HLayout {

    private static final int MASTHEAD_HEIGHT = 20;

    public VersionFooter() {
        super();

        // Head layout container
        this.setStyleName("footerPanel");
        this.setHeight(MASTHEAD_HEIGHT);

        // Name label
        Label footerLabel = new Label();
        footerLabel.setStyleName("footerText");
        footerLabel.setContents(IndicatorsWeb.getConstants().appVersion());
        footerLabel.setWidth100();

        this.addMember(footerLabel);
    }

}
