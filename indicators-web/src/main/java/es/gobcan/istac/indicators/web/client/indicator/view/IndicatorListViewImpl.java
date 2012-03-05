package es.gobcan.istac.indicators.web.client.indicator.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;
import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;
import org.siemac.metamac.core.common.dto.serviceapi.LocalisedStringDto;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.QuantityDto;
import es.gobcan.istac.indicators.core.enume.domain.QuantityTypeEnum;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorListPresenter;
import es.gobcan.istac.indicators.web.client.indicator.presenter.IndicatorListUiHandler;
import es.gobcan.istac.indicators.web.client.model.IndicatorRecord;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorsDS;

public class IndicatorListViewImpl extends ViewImpl implements IndicatorListPresenter.IndicatorListView {

	private IndicatorListUiHandler uiHandlers;
	
	private VLayout panel;
	
	private ToolStripButton newIndicatorActor;
	private ToolStripButton deleteIndicatorActor;
	
	private ListGrid indicatorList;
	
	//New indicator
	private CreateForm createPanel;
	
	//Delete
	private DeleteConfirmationWindow deleteConfirmationWindow;
	
	@Inject
	public IndicatorListViewImpl() {
		super();
	
		//Toolstrip
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		
		newIndicatorActor = new ToolStripButton(getConstants().indicNew(), RESOURCE.newListGrid().getURL());
		deleteIndicatorActor = new ToolStripButton(getConstants().indicDelete(), RESOURCE.deleteListGrid().getURL());
		deleteIndicatorActor.hide();
		toolStrip.addButton(newIndicatorActor);
		toolStrip.addButton(deleteIndicatorActor);
		
		indicatorList = new ListGrid();
		IndicatorsDS indicatorsDS = new IndicatorsDS();
		indicatorList.setDataSource(indicatorsDS);
		indicatorList.setLeaveScrollbarGap(false);
		indicatorList.setUseAllDataSourceFields(false);
		indicatorList.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		
		ListGridField fieldCode = new ListGridField(IndicatorsDS.FIELD_CODE, getConstants().indicListHeaderIdentifier());
		fieldCode.setAlign(Alignment.LEFT);
		ListGridField fieldName = new ListGridField(IndicatorsDS.FIELD_INTERNATIONAL_NAME, getConstants().indicListHeaderName());
		indicatorList.setFields(fieldCode, fieldName);
		
		createPanel = new CreateForm();
		createPanel.hide();
		
		panel = new VLayout();
		panel.addMember(toolStrip);
		panel.addMember(indicatorList);
		panel.addMember(createPanel);
		
		//Delete Confirmation Modal
		deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().appConfirmDeleteTitle(), getConstants().indicDeleteConfirm());
		deleteConfirmationWindow.setVisibility(Visibility.HIDDEN);
		
		bindEvents();
	}
	
	private void bindEvents() {
		newIndicatorActor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				createPanel.init();
				createPanel.show();
			}
		});
		
		indicatorList.addSelectionChangedHandler(new SelectionChangedHandler() {
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				if (indicatorList.getSelectedRecords().length > 0) {
					deleteIndicatorActor.show();
				} else {
					deleteIndicatorActor.hide();
				}
			}
		});

		indicatorList.addRecordClickHandler(new RecordClickHandler() {
			@Override
			public void onRecordClick(RecordClickEvent event) {
				if (event.getFieldNum() != 0) { //Clicking checkbox will be ignored
				    String code = event.getRecord().getAttribute(IndicatorsDS.FIELD_CODE);
					uiHandlers.goToIndicator(code);
				}
			}
		});
		
		deleteIndicatorActor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				deleteConfirmationWindow.show();
			}
		});
		
		deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				uiHandlers.deleteIndicators(getUuidsFromSelected());
				deleteConfirmationWindow.hide();
			}
		});
	}
	
	@Override
	public Widget asWidget() {
		return panel;
	}
	
	@Override
	public void setIndicatorList(List<IndicatorDto> indicators) {
		createPanel.hide();
		IndicatorRecord[] records = new IndicatorRecord[indicators.size()];
		int index = 0;
		for (IndicatorDto ind : indicators) {
			records[index++] = new IndicatorRecord(ind.getUuid(), ind.getCode(), getLocalisedString(ind.getName()));
		}
		indicatorList.setData(records);
	}
	
	@Override
	public void setUiHandlers(IndicatorListPresenter uiHandlers) {
		this.uiHandlers = uiHandlers;
	}
	
	/* Util */
	public List<String> getUuidsFromSelected() {
		List<String> codes = new ArrayList<String>();
		for (ListGridRecord record : indicatorList.getSelectedRecords()) {
			codes.add(record.getAttribute(IndicatorsDS.FIELD_UUID));
		}
		return codes;
	}
	

	private class CreateForm extends VLayout {
		private GeneralPanel generalPanel;
		
		public CreateForm() {
			
			TitleLabel indicatorLabel = new TitleLabel();
			indicatorLabel.setContents(getConstants().indicNewTitle());
			
			generalPanel = new GeneralPanel();
			
			this.addMember(indicatorLabel);
			this.addMember(generalPanel);
			init();
		}
		
		public void init() {
			generalPanel.setIndicator(new IndicatorDto());
		}
		
		private class GeneralPanel extends VLayout {
			
		    /* Data */
		    private IndicatorDto indicator;
		    
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
		                saveIndicator();
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
			    RequiredTextItem code = new RequiredTextItem(IndicatorsDS.FIELD_CODE, getConstants().indicDetailIdentifier());
				
				// Identifiers Form
				identifiersEditionForm = new GroupDynamicForm(getConstants().indicDetailIdentifiers());
				identifiersEditionForm.setFields(code);
				
				
				MultiLanguageTextItem internationalName = new MultiLanguageTextItem(IndicatorsDS.FIELD_INTERNATIONAL_NAME, getConstants().indicDetailName());
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
			
			public void setIndicator(IndicatorDto indicator) {
			    this.indicator = indicator;
			    generalEditionForm.clearValues();
			    identifiersEditionForm.clearValues();
				mainFormLayout.setEditionMode();
				
				// Clear errors
				/*identifiersEditionForm.clearErrors(true);
				generalEditionForm.clearErrors(true);*/
			}

			
		    private void saveIndicator() {
		    	boolean validated = identifiersEditionForm.validate();
		    	validated = generalEditionForm.validate(false) && validated;
		        if (validated) {
		        	indicator.setCode((String)identifiersEditionForm.getValue(IndicatorsDS.FIELD_CODE));
		            indicator.setName((InternationalStringDto)generalEditionForm.getValue(IndicatorsDS.FIELD_INTERNATIONAL_NAME));
		            //TODO: Change this when new fields are added
		            indicator.setSubjectCode("SUBJECT_CODE_CHANGE_ME");
		            indicator.setSubjectTitle(createIntString("Subject titulo es"));
		            QuantityDto quantity = new QuantityDto();
		            quantity.setType(QuantityTypeEnum.QUANTITY);
		            quantity.setUnitUuid("1");
		            indicator.setQuantity(quantity);
		            IndicatorListViewImpl.this.uiHandlers.createIndicator(indicator);
		        }
		    }
		    
 		    //TODO: remove, just mocking
		    private InternationalStringDto createIntString(String textEs) {
		        InternationalStringDto intStr = new InternationalStringDto();
		        LocalisedStringDto localisedString = new LocalisedStringDto();
		        localisedString.setLocale("es");
		        localisedString.setLabel(textEs);
		        intStr.addText(localisedString);

		        return intStr;
		    }
		}
	}
}
