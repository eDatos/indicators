package es.gobcan.istac.indicadores.web.client.widgets.form;


public class GroupDynamicForm extends CustomDynamicForm {

	public GroupDynamicForm(String groupTitle) {
		super();
		setIsGroup(true);
		setGroupTitle(groupTitle);
		setNumCols(4);
		setTitleWidth(200);
	}

}
