package es.gobcan.istac.indicators.web.client.widgets.form.fields;

import com.smartgwt.client.widgets.form.fields.StaticTextItem;

public class ViewTextItem extends StaticTextItem {

	public ViewTextItem() {
		super();
		common();
	}
	
	public ViewTextItem(String name, String title) {
		super(name, title);
		common();
	}
	
	private void common() {
		setTitleStyle("staticFormItemTitle");
		setCellStyle("staticFormItemTextBox");
	}
	
}
