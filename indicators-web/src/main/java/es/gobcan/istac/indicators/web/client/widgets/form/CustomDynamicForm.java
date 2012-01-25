package es.gobcan.istac.indicators.web.client.widgets.form;

import com.smartgwt.client.types.FormErrorOrientation;
import com.smartgwt.client.widgets.form.DynamicForm;

public class CustomDynamicForm extends DynamicForm {

	public CustomDynamicForm() {
		super();
		setErrorOrientation(FormErrorOrientation.RIGHT);
		setValidateOnChange(true);
		setStyleName("form");
	}
	
}
