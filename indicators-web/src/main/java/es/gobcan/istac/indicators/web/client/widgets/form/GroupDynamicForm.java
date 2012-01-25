package es.gobcan.istac.indicators.web.client.widgets.form;


public class GroupDynamicForm extends CustomDynamicForm {

	public GroupDynamicForm(String groupTitle) {
		super();
		setIsGroup(true);
		setGroupTitle(groupTitle);
		setNumCols(4);
		setTitleWidth(200);
	}

}
