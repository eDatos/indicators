package es.gobcan.istac.indicators.web.client.system.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderClickHandler;
import com.smartgwt.client.widgets.tree.events.FolderClosedEvent;
import com.smartgwt.client.widgets.tree.events.FolderClosedHandler;
import com.smartgwt.client.widgets.tree.events.FolderContextClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderContextClickHandler;
import com.smartgwt.client.widgets.tree.events.FolderDropEvent;
import com.smartgwt.client.widgets.tree.events.FolderDropHandler;
import com.smartgwt.client.widgets.tree.events.FolderOpenedEvent;
import com.smartgwt.client.widgets.tree.events.FolderOpenedHandler;
import com.smartgwt.client.widgets.tree.events.LeafClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafClickHandler;
import com.smartgwt.client.widgets.tree.events.LeafContextClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafContextClickHandler;

import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.ElementLevelDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemStructureDto;
import es.gobcan.istac.indicators.web.client.model.ds.DimensionDS;
import es.gobcan.istac.indicators.web.client.model.ds.IndicatorInstanceDS;
import es.gobcan.istac.indicators.web.client.system.presenter.SystemUiHandler;
import es.gobcan.istac.indicators.web.client.system.view.tree.IndSystemContentNode;

public class SystemStructurePanel extends VLayout {
	
	private SystemUiHandler uiHandlers;
	private IndicatorsSystemDto system;
	
	private EditableTreePanel treePanelEdit;
	private DimensionPanel dimensionPanel;
	private IndicatorInstancePanel indicatorInstPanel;
	private DeleteConfirmationWindow dimensionDeleteConfirm;
	private DeleteConfirmationWindow indInstanceDeleteConfirm;
	
	private DimensionDto selectDimForCreate;
	private DimensionDto selectedDimension;
	
	private IndicatorInstanceDto selectedIndInstance;
	
	public SystemStructurePanel() {
		super();
		
		this.treePanelEdit = new EditableTreePanel();
		
		this.dimensionPanel = new DimensionPanel();
		this.dimensionPanel.hide();
		this.indicatorInstPanel = new IndicatorInstancePanel(); 
		this.indicatorInstPanel.hide();
		
		dimensionDeleteConfirm = new DeleteConfirmationWindow(getConstants().appConfirmDeleteTitle(), getConstants().systemStrucDimDeleteConfirm());
		indInstanceDeleteConfirm = new DeleteConfirmationWindow(getConstants().appConfirmDeleteTitle(), getConstants().systemStrucIndInstanceDeleteConfirm());
		
		this.addMember(treePanelEdit);
		this.addMember(dimensionPanel);
		this.addMember(indicatorInstPanel);
		bindEvents();
	}
	
	private void bindEvents() {
		dimensionDeleteConfirm.getYesButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hidePanels();
				uiHandlers.deleteDimension(selectedDimension);
			}
		});
		
		indInstanceDeleteConfirm.getYesButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hidePanels();
				uiHandlers.deleteIndicatorInstance(selectedIndInstance);
			}
		});
	}
	

	public void setIndicatorsSystem(IndicatorsSystemDto indSys) {
		treePanelEdit.setIndicatorsSystem(indSys);
	}
	
	public void setIndicators(List<IndicatorDto> indicators) {
	    indicatorInstPanel.setIndicators(indicators);
	}
	
	public void setIndicatorFromIndicatorInstance(IndicatorDto indicator) {
	    indicatorInstPanel.setIndicator(indicator);
	}
	
	public void setIndicatorSystemStructure(IndicatorsSystemDto indicatorsSystem, IndicatorsSystemStructureDto structure) {
	    if (this.system != null && indicatorsSystem != null) {
	        if (this.system.getCode().equals(indicatorsSystem.getCode())) { //reloading same structure
	            //check if we have just persisted the system, in that case reload
	            if (this.system.getUuid() == null && structure.getUuid() != null) {
	                uiHandlers.retrieveIndSystem(this.system.getCode());
	            }
	        } else { //loading new structure, we must hide panels with old information
	            hidePanels();
	        }
	    }
		this.system = indicatorsSystem;
		treePanelEdit.setIndicatorSystemStructure(structure);
	}
	
	public void selectDimension(DimensionDto dim) {
	    if (indicatorInstPanel.isVisible()) {
	        indicatorInstPanel.hide();
	    }
	    selectedDimension = dim;
	    dimensionPanel.setDimension(dim);
	    dimensionPanel.show();
	}
	
	public void selectIndicatorInstance(IndicatorInstanceDto instance) {
		if (dimensionPanel.isVisible()) {
			dimensionPanel.hide();
		}
		selectedIndInstance = instance;
		uiHandlers.retrieveIndicatorFromIndicatorInstance(instance.getIndicatorUuid());
		indicatorInstPanel.setIndicatorInstance(instance);
		indicatorInstPanel.show();
	}
	
	private void showDimCreatePanel() {
	    hidePanels();
	    selectedDimension = null;
	    selectedIndInstance = null;
	    dimensionPanel.clearForms();
	    dimensionPanel.setEditionMode();
	    dimensionPanel.resetTitle();
	    dimensionPanel.show();
	}
	
	private void showIndicInstanceCreatePanel() {
		hidePanels();
		//reload indicators
		uiHandlers.retrieveIndicators();
		selectedDimension = null;
		selectedIndInstance = null;
		indicatorInstPanel.clearForms();
		indicatorInstPanel.setEditionMode();
		indicatorInstPanel.resetTitle();
		indicatorInstPanel.show();
	}
	
	private void saveDimension(DimensionDto dim) {
		boolean creating = (selectedDimension == null);
		selectedDimension = dim;
		if (creating) {
			dim.setParentUuid(selectDimForCreate == null ? null : selectDimForCreate.getUuid()); //whether is root's child or dimension's
			dim.setOrderInLevel(1L); //First child in parent
			uiHandlers.createDimension(system, dim);
		} else {
			uiHandlers.updateDimension(dim);
		}
	}
	
	private void saveIndicatorInstance(IndicatorInstanceDto inst) {
		boolean creating = (selectedIndInstance == null || selectedIndInstance.getUuid() == null);
		selectedIndInstance = inst;
		if (creating) {
			inst.setParentUuid(selectDimForCreate == null ? null : selectDimForCreate.getUuid()); //whether is root's child or dimension's
			inst.setOrderInLevel(1L); //First child in parent
			uiHandlers.createIndicatorInstance(system, inst);
		} else {
			uiHandlers.updateIndicatorInstance(inst);
		}
	}
	
	public void onDimensionSaved(DimensionDto dimension) {
		selectDimension(dimension);
		dimensionPanel.onDimensionSaved();
	}
	
	public void onIndicatorInstanceSaved(IndicatorInstanceDto instance) {
		selectIndicatorInstance(instance);
		indicatorInstPanel.onIndicatorInstanceSaved();
	}
	
	private void hidePanels() {
        if (dimensionPanel.isVisible()) {
            dimensionPanel.hide();
        }
        if (indicatorInstPanel.isVisible()) {
            indicatorInstPanel.hide();
        }
    }
	
	public void setUiHandlers(SystemUiHandler uiHandlers) {
		this.uiHandlers = uiHandlers;
	}
	
	private class TreePanel extends VLayout {
		protected final Long FALSE_ROOT_NODE_ID = 0L;
		protected final Long ROOT_NODE_ID = 1L;
		
		/* TREE MANAGEMENT */
		protected IndSystemContentNode falseRoot;
		protected Tree tree;
		protected final TreeGrid treeGrid;
		protected Map<ElementLevelDto,IndSystemContentNode> sourceMapping;
		protected String treeOpenState;					//Internal representation that helps us to recover opened nodes after reloading tree
		
		public TreePanel() {
			super();
			this.setHeight(400);
			falseRoot = new IndSystemContentNode(FALSE_ROOT_NODE_ID.toString(), "", ROOT_NODE_ID.toString(), null);
			
			tree = new Tree();
			tree.setModelType(TreeModelType.PARENT);
			tree.setNameProperty(IndSystemContentNode.ATTR_NAME);
			tree.setIdField(IndSystemContentNode.ATTR_ID);
			tree.setParentIdField(IndSystemContentNode.ATTR_PARENT);
		    tree.setRootValue(ROOT_NODE_ID.toString());  


			treeGrid = new TreeGrid();  
			treeGrid.setShowHeader(false);
	        treeGrid.setFields(new TreeGridField("Name"));
	        treeGrid.setData(tree);
	        treeGrid.setShowCellContextMenus(true);
	        treeGrid.setSelectionAppearance(SelectionAppearance.ROW_STYLE);
	        sourceMapping = new HashMap<ElementLevelDto, IndSystemContentNode>();
			this.addMember(treeGrid);
		}
		
		/**
		 * Builds TreeNodes based on 
		 * @param parentNode
		 * @param dimensions
		 * @param totalInstances
		 */
		private void buildNodes(TreeNode parentNode, List<ElementLevelDto> elementsLevel) {
			String parentId = parentNode.getAttribute("ID");
			for (ElementLevelDto elemLevel : elementsLevel) {
			    if (elemLevel.isDimension()) {
			        DimensionDto dim = elemLevel.getDimension();
			        IndSystemContentNode node = new IndSystemContentNode(dim.getUuid(), InternationalStringUtils.getLocalisedString(dim.getTitle()), parentId, elemLevel);
			        tree.add(node, parentNode);
			        sourceMapping.put(elemLevel, node);
			        List<ElementLevelDto> children = buildChildrenList(elemLevel.getSubelements());
			        buildNodes(node, children);
			    } else if (elemLevel.isIndicatorInstance()) {
			        IndicatorInstanceDto indInst = elemLevel.getIndicatorInstance();
			        IndSystemContentNode node = new IndSystemContentNode(indInst.getUuid(), InternationalStringUtils.getLocalisedString(indInst.getTitle()), parentId, elemLevel);
			        tree.add(node, parentNode);
			        sourceMapping.put(elemLevel, node);
			    }
			}
		}
		
		public void setIndicatorsSystem(IndicatorsSystemDto indSys) {
			falseRoot.setAttribute("Name", getLocalisedString(indSys.getTitle()));
			falseRoot.setAttribute("Source", indSys);
			treeGrid.redraw();
		}
		
		public void setIndicatorSystemStructure(IndicatorsSystemStructureDto structure) {
			//Clear the tree
			tree.removeList(tree.getAllNodes());
			sourceMapping.clear();

			//Build nodes recursevely
			tree.add(falseRoot,"/");
			if (structure != null) {
			    List<ElementLevelDto> children = buildChildrenList(structure.getElements());
			    buildNodes(falseRoot, children);
			    recoverOpenState();
			}
			treeGrid.markForRedraw();
		}
		
		private void recoverOpenState() {
			//recover opened nodes
			if (treeOpenState != null){
				treeGrid.setOpenState(treeOpenState);
			}
			TreeNode node = null;
			if (selectedDimension != null) {
				node = sourceMapping.get(selectedDimension);
			}
			if (selectedIndInstance != null) {
				node = sourceMapping.get(selectedIndInstance);
			}
			if (node != null) {
				TreeNode[] ascendantNodes = treeGrid.getData().getParents(node);
				treeGrid.getData().openFolders(ascendantNodes);
			}
			
		}
		
		/*
		 * Given subdimensions and ALL indicatorInstances, the method must create a new List containing all parentUuid direct children
		 * and it must be sorted by orderInLevel field.
		 */
		private List<ElementLevelDto> buildChildrenList(List<ElementLevelDto> elementsLevel) {
			Collections.sort(elementsLevel, new Comparator<ElementLevelDto>() {
				@Override
				public int compare(ElementLevelDto o1, ElementLevelDto o2) {
					return o1.getOrderInLevel().compareTo(o2.getOrderInLevel());
				}
			});
			return elementsLevel;
		}
	}

	private class EditableTreePanel extends TreePanel {
	    
        public EditableTreePanel() {
            super();
            treeGrid.setCanReorderRecords(true);  
            treeGrid.setCanAcceptDroppedRecords(true);  
            treeGrid.setCanDragRecordsOut(false);  
            treeGrid.setDragDataAction(DragDataAction.MOVE);
            bindEvents();
        }
        
        private void bindEvents() {
            treeGrid.addFolderContextClickHandler(new FolderContextClickHandler() {
                @Override
                public void onFolderContextClick(FolderContextClickEvent event) {
                    IndSystemContentNode node =(IndSystemContentNode)event.getFolder();
                    Menu menu = buildContextMenuNode(node);
                    menu.setAutoDraw(true);
                    menu.showContextMenu();
                    menu.setShowCellContextMenus(true);
                }
            });
            
            treeGrid.addLeafContextClickHandler(new LeafContextClickHandler() {
                @Override
                public void onLeafContextClick(LeafContextClickEvent event) {
                    IndSystemContentNode node =(IndSystemContentNode)event.getLeaf();
                    Menu menu = buildContextMenuLeaf(node);
                    menu.showContextMenu();
                }
            });
            
            
            treeGrid.addFolderClickHandler(new FolderClickHandler() {
                @Override
                public void onFolderClick(FolderClickEvent event) {
                    IndSystemContentNode node = (IndSystemContentNode)event.getFolder();
                    if (node.isDimension()) {
                        SystemStructurePanel.this.selectDimension(node.getSource().getDimension());
                    }
                }
            });
            treeGrid.addLeafClickHandler(new LeafClickHandler() {
                @Override
                public void onLeafClick(LeafClickEvent event) {
                    IndSystemContentNode node = (IndSystemContentNode)event.getLeaf();
                    if (node.isIndicator()) {
                        SystemStructurePanel.this.selectIndicatorInstance(node.getSource().getIndicatorInstance());
                    }
                }
            });
            
            treeGrid.addFolderDropHandler(new FolderDropHandler() {
				@Override
				public void onFolderDrop(FolderDropEvent event) {
					TreeNode dropFolder = event.getFolder();
					TreeNode droppedNode = event.getNodes().length > 0 ? event.getNodes()[0] : null;
					int position = event.getIndex(); //absolute position
					if (isDroppable(dropFolder)) {
	                    TreeNode[] siblings = treeGrid.getData().getChildren(dropFolder);
					    
					    //We find out position of node under dropFolder
					    int relPosition = position;        //use to update position
					    int pos = -1;
				        for (int i = 0; i < siblings.length; i++) {
				            if (siblings[i] == droppedNode) {
				                pos = i;
				            }
				        }
				        if (pos >= 0 && pos < position) { //if moved node is before final position, the position must be updated
				            relPosition--;
				        }
					    
						ElementLevelDto level = ((IndSystemContentNode)droppedNode).getSource();
						//Get drop folder, finding out target dimension/root 
						IndSystemContentNode nodeParent = (IndSystemContentNode)(dropFolder);
						String targetUuid = nodeParent.isRoot() ? null : nodeParent.getId();
						
						Long order = relPosition + 1L; //relative position starts at 0, order at 1

						uiHandlers.moveSystemStructureNodes(system.getUuid(), targetUuid, level, order);
					}
					event.cancel();
				}
			});
            
            treeGrid.addFolderOpenedHandler(new FolderOpenedHandler() {
                //This method is used to save the open state
				@Override
				public void onFolderOpened(FolderOpenedEvent event) {
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						@Override
						public void execute() {
							treeOpenState = treeGrid.getOpenState();
						}
					});
				}
			});
            
            treeGrid.addFolderClosedHandler(new FolderClosedHandler() {
				@Override
				public void onFolderClosed(FolderClosedEvent event) {
					Scheduler.get().scheduleDeferred(new ScheduledCommand() {
						
						@Override
						public void execute() {
							treeOpenState = treeGrid.getOpenState();
						}
					});
				}
			});
        }
       
        private boolean isDroppable(TreeNode dropFolder) {
        	if (treeGrid.getDropFolder().getAttribute(IndSystemContentNode.ATTR_NAME).equals("/")) {	//Replacing false root is not allowed
				return false;
			}
        	return true;
        }
        
        private Menu buildContextMenuNode(IndSystemContentNode node) {
            final IndSystemContentNode selNode = node;
            Menu menu = new Menu();
            MenuItem item3 = new MenuItem(getConstants().systemStrucNewDim());
            item3.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(MenuItemClickEvent event) {
                    if (selNode == falseRoot)
                        SystemStructurePanel.this.selectDimForCreate = null;
                    else {
                        SystemStructurePanel.this.selectDimForCreate = selNode.getSource().getDimension();
                    }
                    SystemStructurePanel.this.showDimCreatePanel();
                }
            });
            MenuItem item4 = new MenuItem(getConstants().systemStrucNewIndInstance());
            item4.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(MenuItemClickEvent event) {
                    if (selNode == falseRoot)
                        SystemStructurePanel.this.selectDimForCreate = null;
                    else {
                        SystemStructurePanel.this.selectDimForCreate = selNode.getSource().getDimension();
                    }
                    SystemStructurePanel.this.showIndicInstanceCreatePanel();
				}
			});
            
            if (node.isDimension()) {
                MenuItem item1 = new MenuItem(getConstants().systemStrucDimDelete());
                item1.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(MenuItemClickEvent event) {
						DimensionDto dim = selNode.getSource().getDimension();
						selectedDimension = dim;
						dimensionDeleteConfirm.show();
					}
				});
                menu.addItem(item1);
            }
            
            menu.addItem(item3);
            menu.addItem(item4);
            return menu;
        }
        
        private Menu buildContextMenuLeaf(IndSystemContentNode node) {
            final IndSystemContentNode selNode = node;
            Menu menu = new Menu();
            MenuItem item1 = new MenuItem(getConstants().systemStrucIndInstanceDelete());
            item1.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(MenuItemClickEvent event) {
					IndicatorInstanceDto instance = selNode.getSource().getIndicatorInstance();
					selectedIndInstance = instance;
					indInstanceDeleteConfirm.show();
				}
			});
            menu.addItem(item1);
            return menu;
        }
        
        
	}
	
	private class DimensionPanel extends VLayout {
		private InternationalMainFormLayout mainFormLayout;
		
		private Label titleLabel;
		private GroupDynamicForm form;
		private GroupDynamicForm editForm;
		
		private boolean createMode;
		
        public DimensionPanel() {
            mainFormLayout = new InternationalMainFormLayout();
            mainFormLayout.setMargin(0);
            
            titleLabel = new Label();
            titleLabel.setAlign(Alignment.LEFT);
            titleLabel.setOverflow(Overflow.HIDDEN);
            titleLabel.setHeight(40);
            titleLabel.setStyleName("sectionTitle");
            titleLabel.setContents(getConstants().systemStrucDimTitle());
            
            createViewForm();
            createEditForm();
            
            this.addMember(titleLabel);
            this.addMember(mainFormLayout);
            //init
            createMode = false;
            bindEvents();
        }
        
        public void onDimensionSaved() {
			mainFormLayout.setViewMode();
		}

		private void bindEvents() {
            mainFormLayout.getSave().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (editForm.validate()) {
                        DimensionDto dim = null;
                        if (selectedDimension != null) {
                            dim = fillDimension(selectedDimension);
                        } else {
                            dim = fillDimension(new DimensionDto());
                        }
                        SystemStructurePanel.this.saveDimension(dim);
                        setDimension(dim);
                    }
                }
            });
           	mainFormLayout.getTranslateToolStripButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					boolean translationsShowed =  mainFormLayout.getTranslateToolStripButton().isSelected();
					form.setTranslationsShowed(translationsShowed);
					editForm.setTranslationsShowed(translationsShowed);
				}
			});
           	mainFormLayout.getCancelToolStripButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
           	    @Override
           	    public void onClick(ClickEvent event) {
           	        if (createMode) {
           	            DimensionPanel.this.hide();
           	        }
           	    }
           	});
        }
        
        public void setDimension(DimensionDto dim) {
            createMode = dim.getUuid() == null;
            titleLabel.setContents(getConstants().systemStrucDimTitle()+": "+InternationalStringUtils.getLocalisedString(dim.getTitle()));
            setDimensionView(dim);
            setDimensionEdit(dim);
            this.markForRedraw();
        }
        
        public void resetTitle() {
            titleLabel.setContents(getConstants().systemStrucDimTitle()+":");
        }
        
        public void setEditionMode() {
            mainFormLayout.setEditionMode();
        }
        
        public void clearForms() {
            createMode = true;
        	form.clearValues();
        	editForm.clearValues();
        }
        
        private DimensionDto fillDimension(DimensionDto dim) {
            dim.setTitle((InternationalStringDto)(editForm.getValue(DimensionDS.FIELD_INTERNATIONAL_TITLE)));
            return dim;
        }
        
        private void setDimensionView(DimensionDto dim) {
        	form.setValue(DimensionDS.FIELD_INTERNATIONAL_TITLE, RecordUtils.getInternationalStringRecord(dim.getTitle()));
        }
        
        private void setDimensionEdit(DimensionDto dim) {
        	editForm.setValue(DimensionDS.FIELD_INTERNATIONAL_TITLE, RecordUtils.getInternationalStringRecord(dim.getTitle()));
        }
        
        private void createViewForm() {
        	form = new GroupDynamicForm(getConstants().systemStrucDimTitle());
            ViewMultiLanguageTextItem staticName = new ViewMultiLanguageTextItem(DimensionDS.FIELD_INTERNATIONAL_TITLE, getConstants().systemStrucDimName());
            form.setFields(staticName);
            mainFormLayout.addViewCanvas(form);
        }
        
        private void createEditForm() {
            editForm = new GroupDynamicForm(getConstants().systemStrucDimTitle());
            MultiLanguageTextItem editName = new MultiLanguageTextItem(DimensionDS.FIELD_INTERNATIONAL_TITLE, getConstants().systemStrucDimName());
            editName.setRequired(true);
            editForm.setFields(editName);
            mainFormLayout.addEditionCanvas(editForm);
        }
	}
	
	private class IndicatorInstancePanel extends VLayout {
	    private static final String FIELD_INDICATOR_NAME = "ind_name-indinst";
	    
        private InternationalMainFormLayout mainFormLayout;
        private Label titleLabel;
        private GroupDynamicForm form; 
        private GroupDynamicForm editForm;
        private boolean createMode;
        
        private SelectItem indicatorsListItem;
        
        public IndicatorInstancePanel() {
            mainFormLayout = new InternationalMainFormLayout();
            mainFormLayout.setMargin(0);
            
            titleLabel = new Label();
            titleLabel.setAlign(Alignment.LEFT);
            titleLabel.setOverflow(Overflow.HIDDEN);
            titleLabel.setHeight(40);
            titleLabel.setStyleName("sectionTitle");
            titleLabel.setTitle(getConstants().systemStrucIndInstanceTitle());
            
            createViewForm();
            createEditForm();
            
            this.addMember(titleLabel);
            this.addMember(mainFormLayout);
            //init
            createMode = false;
            bindEvents();
        }
        
        public void onIndicatorInstanceSaved() {
			mainFormLayout.setViewMode();
		}
        
        private void bindEvents() {
            mainFormLayout.getSave().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (editForm.validate()) {
                        IndicatorInstanceDto inst = null;
                        if (selectedIndInstance != null) {
                            inst = fillIndicatorInstance(selectedIndInstance);
                        } else {
                            inst = fillIndicatorInstance(new IndicatorInstanceDto());
                        }
                        SystemStructurePanel.this.saveIndicatorInstance(inst);
                        setIndicatorInstance(inst);
                    }
                }
            });
            
           
        	mainFormLayout.getTranslateToolStripButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					boolean translationsShowed =  mainFormLayout.getTranslateToolStripButton().isSelected();
					form.setTranslationsShowed(translationsShowed);
					editForm.setTranslationsShowed(translationsShowed);
				}
			});
            mainFormLayout.getCancelToolStripButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (createMode) {
                        IndicatorInstancePanel.this.hide();
                    }
                }
            });
        }
        
        public void resetTitle() {
            titleLabel.setContents(getConstants().systemStrucIndInstanceTitle()+":");
        }
        
        public void setEditionMode() {
            mainFormLayout.setEditionMode();
        }
        
        public void clearForms() {
            createMode = true;
        	form.clearValues();
        	editForm.clearValues();
        }
        
        private IndicatorInstanceDto fillIndicatorInstance(IndicatorInstanceDto inst) {
        	inst.setTitle((InternationalStringDto)(editForm.getValue(IndicatorInstanceDS.FIELD_INTERNATIONAL_TITLE)));
        	inst.setIndicatorUuid(editForm.getValueAsString(IndicatorInstanceDS.FIELD_INDICATOR_UUID));
        	//TODO: just until temporary values table is ready to use
        	inst.setTemporaryGranularityId("TEMP_GRAN_ID_CHANGEME");
        	return inst;
        }
        
        public void setIndicatorInstance(IndicatorInstanceDto indInst) {
            createMode = indInst.getUuid() == null;
            titleLabel.setContents(getConstants().systemStrucIndInstanceTitle()+": "+InternationalStringUtils.getLocalisedString(indInst.getTitle()));
            //We need indicators in order to fill select item
            uiHandlers.retrieveIndicators();
            setIndicatorInstanceView(indInst);
            setIndicatorInstanceEdit(indInst);
        }
        
        
        public void setIndicators(List<IndicatorDto> indicators) {
            LinkedHashMap<String,String> indicatorsMap = new LinkedHashMap<String, String>();
            for (IndicatorDto indicator : indicators) {
                indicatorsMap.put(indicator.getUuid(), getLocalisedString(indicator.getName()));
            }
            indicatorsListItem.setValueMap(indicatorsMap);
        }
        
        
        private void setIndicator(IndicatorDto indicator) {
            form.setValue(FIELD_INDICATOR_NAME, getLocalisedString(indicator.getName()));
        }
        
        private void setIndicatorInstanceView(IndicatorInstanceDto indInst) {
        	form.setValue(IndicatorInstanceDS.FIELD_INTERNATIONAL_TITLE, RecordUtils.getInternationalStringRecord(indInst.getTitle()));
        	form.setValue(FIELD_INDICATOR_NAME, "");
        }
        
        private void setIndicatorInstanceEdit(IndicatorInstanceDto indInst) {
        	editForm.setValue(IndicatorInstanceDS.FIELD_INTERNATIONAL_TITLE, RecordUtils.getInternationalStringRecord(indInst.getTitle()));
        	editForm.setValue(IndicatorInstanceDS.FIELD_INDICATOR_UUID, indInst.getIndicatorUuid());
        }
        
        private void createViewForm() {
            form = new GroupDynamicForm(getConstants().systemStrucIndInstanceTitle());
            ViewMultiLanguageTextItem staticName = new ViewMultiLanguageTextItem(IndicatorInstanceDS.FIELD_INTERNATIONAL_TITLE, getConstants().systemStrucIndInstanceTitleField());
            StaticTextItem indicatorNameItem = new StaticTextItem(FIELD_INDICATOR_NAME, getConstants().systemStrucIndInstanceIndicator());
            form.setFields(staticName,indicatorNameItem);
            mainFormLayout.addViewCanvas(form);
        }
        
        private void createEditForm() {
            editForm = new GroupDynamicForm(getConstants().systemStrucIndInstanceTitle());
            MultiLanguageTextItem editName = new MultiLanguageTextItem(IndicatorInstanceDS.FIELD_INTERNATIONAL_TITLE, getConstants().systemStrucIndInstanceTitleField());
            indicatorsListItem = new SelectItem(IndicatorInstanceDS.FIELD_INDICATOR_UUID, getConstants().systemStrucIndInstanceIndicator());
            editForm.setFields(editName,indicatorsListItem);
            mainFormLayout.addEditionCanvas(editForm);
        }

    }
}
