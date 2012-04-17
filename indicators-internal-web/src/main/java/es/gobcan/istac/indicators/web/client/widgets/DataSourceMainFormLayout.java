package es.gobcan.istac.indicators.web.client.widgets;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import org.siemac.metamac.web.common.client.resources.GlobalResources;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;

import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

import es.gobcan.istac.indicators.web.client.resources.IndicatorsResources;

public class DataSourceMainFormLayout extends InternationalMainFormLayout {

    private ToolStripButton editQueryButton;

    private Label           label;

    public DataSourceMainFormLayout() {
        super();

        editQueryButton = new ToolStripButton(getConstants().editQuery(), IndicatorsResources.RESOURCE.reset().getURL());
        editQueryButton.setVisibility(Visibility.HIDDEN);
        editQueryButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                editQueryButton.hide();
                label.hide();
                markForRedraw();
            }
        });

        label = new Label(getConstants().infoDataSourceQueryEdition());
        label.setIcon(GlobalResources.RESOURCE.info().getURL());
        label.setAutoHeight();

        addEditionCanvas(label);

        toolStrip.addSeparator();
        toolStrip.addButton(editQueryButton);
    }

    public ToolStripButton getEditQueryToolStripButton() {
        return editQueryButton;
    }

    @Override
    public void setViewMode() {
        super.setViewMode();
        editQueryButton.hide();
    }

    @Override
    public void setEditionMode() {
        super.setEditionMode();
        editQueryButton.show();
        label.show();
        markForRedraw();
    }

    public Label getLabel() {
        return label;
    }

}
