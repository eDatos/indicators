package es.gobcan.istac.indicators.web.client.widgets;

import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCanvasItem;

import com.smartgwt.client.widgets.layout.VLayout;


public class ViewVariableListItem extends CustomCanvasItem {
    
    public ViewVariableListItem(String name, String title) {
        super(name, title);
        setTitleStyle("staticFormItemTitle");
        
        VLayout vlayout = new VLayout();
        vlayout.setHeight(10);
        
        setCanvas(vlayout);
    }

}
