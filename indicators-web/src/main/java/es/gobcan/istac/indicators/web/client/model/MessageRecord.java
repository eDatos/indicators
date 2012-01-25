package es.gobcan.istac.indicadores.web.client.model;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class MessageRecord  extends ListGridRecord {
	
	public static final String TEXT = "text";
	
	public MessageRecord() { }
	
	public MessageRecord(String text) {
		setText(text);
	}

	public void setText(String value) {
		setAttribute(TEXT, value);
	}
	
	public String getText() {
		return getAttributeAsString(TEXT);
	}
	
}
