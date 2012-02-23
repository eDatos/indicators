package es.gobcan.istac.indicators.web.client.system.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;
import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;
import org.siemac.metamac.web.common.client.widgets.CustomListGrid;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.web.client.model.IndicatorSystemRecord;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorsSystemsDS;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemListPresenter.SystemListView;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemListUiHandler;

public class SystemListViewImpl extends ViewImpl implements SystemListView {

	private SystemListUiHandler uiHandlers;
	
	
	private final CustomListGrid indSystemListGrid;
	private VLayout vLayout;
	
	private ToolStripButton newActor;
	private ToolStripButton deleteActor;
	
	private CreateForm createPanel;
	private DeleteConfirmationWindow deleteConfirmationWindow;
	
	@Inject
	public SystemListViewImpl() {
		//ToolStrip
		newActor = new ToolStripButton(getConstants().systemNew(),RESOURCE.newListGrid().getURL());
		deleteActor = new ToolStripButton(getConstants().systemDelete(),RESOURCE.deleteListGrid().getURL());
		deleteActor.hide();
		
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		toolStrip.addButton(newActor);
		toolStrip.addButton(deleteActor);
		
		indSystemListGrid = new CustomListGrid();
		IndicatorsSystemsDS datasource = new IndicatorsSystemsDS();
		indSystemListGrid.setDataSource(datasource);
		indSystemListGrid.setUseAllDataSourceFields(false);
		indSystemListGrid.setLeaveScrollbarGap(false);
		
		//List
		ListGridField field1 = new ListGridField(IndicatorsSystemsDS.FIELD_CODE,getConstants().systemListHeaderIdentifier());
		field1.setAlign(Alignment.LEFT);
		ListGridField field2 = new ListGridField(IndicatorsSystemsDS.FIELD_INTERNATIONAL_TITLE,getConstants().systemListHeaderTitle());
		indSystemListGrid.setFields(field1,field2);
		
		IndicatorSystemRecord[] records = new IndicatorSystemRecord[0];

		indSystemListGrid.setData(records);
		
		createPanel = new CreateForm();
		createPanel.hide();
		
		vLayout = new VLayout();
		vLayout.addMember(toolStrip);
		vLayout.addMember(indSystemListGrid);
		vLayout.addMember(createPanel);
		
		deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().appConfirmDeleteTitle(), getConstants().systemDeleteConfirm());
		deleteConfirmationWindow.hide();
		
		bindEvents();
	}
	
	private void bindEvents() {
		//New Indicator System
		newActor.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				createPanel.init();
				createPanel.show();
			}
		});
		
		indSystemListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				if (indSystemListGrid.getSelectedRecords().length > 0) {
					deleteActor.show();
				} else {
					deleteActor.hide();
				}
			}
		});
		
		indSystemListGrid.addRecordClickHandler(new RecordClickHandler() {
			@Override
			public void onRecordClick(RecordClickEvent event) {
				if (event.getFieldNum() != 0) { //Clicking checkbox will be ignored
					String code = event.getRecord().getAttribute(IndicatorsSystemsDS.FIELD_CODE);
					uiHandlers.goToIndicatorsSystem(code);
				}
			}
		});
		
		deleteActor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				deleteConfirmationWindow.show();
			}
		});
		
		deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				uiHandlers.deleteIndicatorsSystems(getCodesFromSelected());
				deleteConfirmationWindow.hide();
			}
		});
	}
	
	@Override
	public void setIndSystemList(List<IndicatorsSystemDto> indicatorsSystemList) {
		createPanel.hide();
		IndicatorSystemRecord[] records = new IndicatorSystemRecord[indicatorsSystemList.size()];
		int index = 0;
		for (IndicatorsSystemDto indSys : indicatorsSystemList) {
			records[index++] = new IndicatorSystemRecord(indSys.getUuid(), indSys.getCode(), getLocalisedString(indSys.getTitle()));
		}
		indSystemListGrid.setData(records);
	}
	
	@Override
	public Widget asWidget() {
		return vLayout;
	}

	@Override
	public void setUiHandlers(SystemListUiHandler uiHandlers) {
		this.uiHandlers = uiHandlers;
	}
	
	/* Util */
	public List<String> getCodesFromSelected() {
		List<String> codes = new ArrayList<String>();
		for (ListGridRecord record : indSystemListGrid.getSelectedRecords()) {
			codes.add(record.getAttribute(IndicatorsSystemsDS.FIELD_CODE));
		}
		return codes;
	}

	private class CreateForm extends VLayout {
		private GeneralPanel generalPanel;
		
		public CreateForm() {
		
			Label title = new Label();
			title.setAlign(Alignment.LEFT);
			title.setOverflow(Overflow.HIDDEN);
			title.setHeight(40);
			title.setStyleName("sectionTitle");
			title.setContents(getConstants().systemNewTitle());
			
			this.generalPanel = new GeneralPanel();
			
			this.addMember(title);
			this.addMember(generalPanel);
			init();
		}
		
		private void init() {
			generalPanel.setIndicatorsSystem(new IndicatorsSystemDto());
		}
		
		private class GeneralPanel extends VLayout {
		    /* Data */
		    private IndicatorsSystemDto system;
		    
			private InternationalMainFormLayout mainFormLayout;
			
			/* Edit Form*/
			private GroupDynamicForm identifiersEditionForm;
			private GroupDynamicForm generalEditionForm;
			
			public GeneralPanel() {
				super();
				mainFormLayout = new InternationalMainFormLayout();
				mainFormLayout.setEditionMode();
	
				createForm();
				
				this.addMember(mainFormLayout);
				bindEvents();
			}
			
			private void bindEvents() {
			    mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {
			        @Override
		            public void onClick(ClickEvent event) {
			        	boolean translationsShowed =  mainFormLayout.getTranslateToolStripButton().isSelected();
		                generalEditionForm.setTranslationsShowed(translationsShowed);
		                generalEditionForm.markForRedraw();
		            }
		        });
		        
		        mainFormLayout.getSave().addClickHandler(new ClickHandler() {
		            @Override
		            public void onClick(ClickEvent event) {
		                saveIndicatorsSystem();
		            }
		        });
		        
		        mainFormLayout.getCancelToolStripButton().addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						createPanel.hide();
					}
				});
	
			}

			
			private void createForm() {
			    RequiredTextItem code = new RequiredTextItem(IndicatorsSystemsDS.FIELD_CODE, getConstants().systemDetailIdentifier());
				
				// Identifiers Form
				identifiersEditionForm = new GroupDynamicForm(getConstants().indicDetailIdentifiers());
				identifiersEditionForm.setFields(code);
				
				
				MultiLanguageTextItem internationalName = new MultiLanguageTextItem(IndicatorsSystemsDS.FIELD_INTERNATIONAL_TITLE, getConstants().systemDetailTitle());
				internationalName.setRequired(true);
				// General Form
				generalEditionForm = new GroupDynamicForm(getConstants().indicDetailDetails());
				generalEditionForm.setFields(internationalName);
						
				// Status Form
				GroupDynamicForm statusForm = new GroupDynamicForm(getConstants().indicDetailStatus());
				//statusForm.setFields(staticFinalItemEdit, staticStartDateItemEdit, staticEndDateItemEdit);
				
				mainFormLayout.addEditionCanvas(identifiersEditionForm);
				mainFormLayout.addEditionCanvas(generalEditionForm);
				mainFormLayout.addEditionCanvas(statusForm);
			}
			
			public void setIndicatorsSystem(IndicatorsSystemDto system) {
			    this.system = system;
			    generalEditionForm.clearValues();
			    identifiersEditionForm.clearValues();
				mainFormLayout.setEditionMode();
			}

			
		    private void saveIndicatorsSystem() {
		    	boolean validated = identifiersEditionForm.validate();
		    	validated = generalEditionForm.validate(false) && validated;
		        if (validated) {
		        	system.setCode((String)identifiersEditionForm.getValue(IndicatorsSystemsDS.FIELD_CODE));
		            system.setTitle((InternationalStringDto)generalEditionForm.getValue(IndicatorsSystemsDS.FIELD_INTERNATIONAL_TITLE));
		            SystemListViewImpl.this.uiHandlers.createIndicatorsSystem(system);
		        }
		    }
		}
	}
}
