package es.gobcan.istac.indicadores.web.client.widgets.form;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.HasClickHandlers;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class MainFormLayout extends VLayout {

	private VLayout viewFormLayout;
	private VLayout editionFormLayout;
	
	private VLayout layout;
	
	private ToolStripButton editToolStripButton;
	private ToolStripButton saveToolStripButton;
	private ToolStripButton cancelToolStripButton;
	
	private ToolStripButton translateToolStripButton;
	

	public MainFormLayout() {
		super();
		setMembersMargin(10);
		setMargin(15);
		setAutoHeight();
		
		editToolStripButton = new ToolStripButton("Editar","edit_listgrid.png");
		editToolStripButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setEditionMode();
			}
		});
		
		saveToolStripButton = new ToolStripButton("Guardar","save_listgrid.png");
		saveToolStripButton.setVisibility(Visibility.HIDDEN);
		
		cancelToolStripButton = new ToolStripButton("Cancelar","cancel_listgrid.png");
		cancelToolStripButton.setVisibility(Visibility.HIDDEN);
		cancelToolStripButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setViewMode();
			}
		});
		
		translateToolStripButton = new ToolStripButton("Mostrar traducciones","locale_listgrid.png");
		translateToolStripButton.setActionType(SelectionType.CHECKBOX);
		translateToolStripButton.setAlign(Alignment.RIGHT);
		translateToolStripButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (translateToolStripButton.isSelected()) {
					translateToolStripButton.setTitle("Ocultar traducciones");
				} else {
					translateToolStripButton.setTitle("Mostrar traducciones");
				}
			}
		});
		
        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();
        toolStrip.addButton(editToolStripButton);
        toolStrip.addButton(saveToolStripButton);
        toolStrip.addButton(cancelToolStripButton);
        toolStrip.addSeparator();
        toolStrip.addButton(translateToolStripButton);

        viewFormLayout = new VLayout(15);
        viewFormLayout.setMargin(10);
        viewFormLayout.setAutoHeight();
		
        editionFormLayout = new VLayout(15);
        editionFormLayout.setMargin(10);
        editionFormLayout.setAutoHeight();
        editionFormLayout.setVisibility(Visibility.HIDDEN);
        
		layout = new VLayout();
		layout.setBorder("1px solid #d9d9d9");
		layout.setAutoHeight();
		layout.addMember(toolStrip);
		layout.addMember(viewFormLayout);
		layout.addMember(editionFormLayout);
		
		
		addMember(layout);
	}
	
	public void addViewForm(GroupDynamicForm form) {
		viewFormLayout.addMember(form);
	}
	
	public void addEditionForm(GroupDynamicForm form) {
		editionFormLayout.addMember(form);
	}
	
	public HasClickHandlers getSave() {
		return saveToolStripButton;
	}
	
	public ToolStripButton getTranslateToolStripButton() {
		return translateToolStripButton;
	}
	
	public void setViewMode() {
		viewFormLayout.show();
		editionFormLayout.hide();
		editToolStripButton.show();
		saveToolStripButton.hide();
		cancelToolStripButton.hide();
	}
	
	public void setEditionMode() {
		viewFormLayout.hide();
		editionFormLayout.show();
		editToolStripButton.hide();
		saveToolStripButton.show();
		cancelToolStripButton.show();
	}
	
}
