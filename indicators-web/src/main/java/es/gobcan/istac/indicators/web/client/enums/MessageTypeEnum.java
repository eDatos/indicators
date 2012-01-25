package es.gobcan.istac.indicators.web.client.enums;

import com.smartgwt.client.types.ValueEnum;

public enum MessageTypeEnum implements ValueEnum {

	ERROR("error"),
	SUCCESS("success"),
	WARNING("warning");
	
	private String value;

	MessageTypeEnum(String value) {
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return this.value;
	}
	
	public String getName() {
		return name();
	}

}