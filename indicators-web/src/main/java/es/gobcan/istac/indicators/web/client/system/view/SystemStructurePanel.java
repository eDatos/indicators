package es.gobcan.istac.indicators.web.client.system.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.system.view.tree.IndSystemContentNodeType.DIMENSION;
import static es.gobcan.istac.indicators.web.client.system.view.tree.IndSystemContentNodeType.INDICATOR;
import static es.gobcan.istac.indicators.web.client.system.view.tree.IndSystemContentNodeType.ROOT;
import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.dto.serviceapi.InternationalStringDto;
import org.siemac.metamac.web.common.client.utils.ErrorUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.DragStopEvent;
import com.smartgwt.client.widgets.events.DragStopHandler;
import com.smartgwt.client.widgets.events.DropEvent;
import com.smartgwt.client.widgets.events.DropHandler;
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
import com.smartgwt.client.widgets.tree.events.FolderOpenedEvent;
import com.smartgwt.client.widgets.tree.events.FolderOpenedHandler;
import com.smartgwt.client.widgets.tree.events.LeafClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafClickHandler;
import com.smartgwt.client.widgets.tree.events.LeafContextClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafContextClickHandler;

import es.gobcan.istac.indicators.core.dto.serviceapi.DimensionDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorInstanceDto;
import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.web.client.enums.MessageTypeEnum;
import es.gobcan.istac.indicators.web.client.events.ShowMessageEvent;
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
	
	public void setIndicatorSystemStructure(IndicatorsSystemDto indicatorsSystem, List<DimensionDto> dimensions, List<IndicatorInstanceDto> indicatorInstances) {
		this.system = indicatorsSystem;
		treePanelEdit.setIndicatorSystemStructure(dimensions, indicatorInstances);
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
		boolean creating = (selectedIndInstance == null);
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
		protected Map<Object,IndSystemContentNode> sourceMapping;
		protected String treeOpenState;					//Internal representation that helps us to recover opened nodes after reloading tree
		
		public TreePanel() {
			super();
			this.setHeight(400);
			falseRoot = new IndSystemContentNode(FALSE_ROOT_NODE_ID.toString(), "", ROOT, ROOT_NODE_ID.toString(), null);
			
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
	        sourceMapping = new HashMap<Object, IndSystemContentNode>();
			this.addMember(treeGrid);
		}
		
		/**
		 * Builds TreeNodes based on 
		 * @param parentNode
		 * @param dimensions
		 * @param totalInstances
		 */
		private void buildNodes(TreeNode parentNode, List<Object>children, List<IndicatorInstanceDto> totalInstances) {
			String parentId = parentNode.getAttribute("ID");
			for (Object child : children) {
				if (child instanceof DimensionDto) {
					DimensionDto dim = (DimensionDto)child;
					IndSystemContentNode node = new IndSystemContentNode(dim.getUuid(), InternationalStringUtils.getLocalisedString(dim.getTitle()), DIMENSION, parentId, dim);
					tree.add(node, parentNode);
					sourceMapping.put(dim, node);
					List<Object> subChildren = buildChildrenList(dim.getUuid(), dim.getSubdimensions(),totalInstances);
					buildNodes(node, subChildren,totalInstances);
				} else if (child instanceof IndicatorInstanceDto) {
					IndicatorInstanceDto indInst = (IndicatorInstanceDto)child;
					IndSystemContentNode node = new IndSystemContentNode(indInst.getUuid(), InternationalStringUtils.getLocalisedString(indInst.getTitle()), INDICATOR, parentId, indInst);
					tree.add(node, parentNode);
					sourceMapping.put(indInst, node);
				}
			}
		}
		
		public void setIndicatorsSystem(IndicatorsSystemDto indSys) {
			falseRoot.setAttribute("Name", getLocalisedString(indSys.getTitle()));
			falseRoot.setAttribute("Source", indSys);
			treeGrid.redraw();
		}
		
		public void setIndicatorSystemStructure(List<DimensionDto> dimensions, List<IndicatorInstanceDto> indicatorInstances) {
			//Clear the tree
			tree.removeList(tree.getAllNodes());
			sourceMapping.clear();

			//Build nodes recursevely
			tree.add(falseRoot,"/");
			List<Object> children = buildChildrenList(null, dimensions, indicatorInstances);
			
			buildNodes(falseRoot, children, indicatorInstances);

			recoverOpenState();
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
		private List<Object> buildChildrenList(String parentUuid, List<DimensionDto> dimensions, List<IndicatorInstanceDto> indicatorInstances) {
			List<Object> children = new ArrayList<Object>(dimensions);
			
			for (IndicatorInstanceDto inst : indicatorInstances) {
				if (parentUuid == null && inst.getParentUuid() == null) {
					children.add(inst);
				} else if (parentUuid != null && parentUuid.equals(inst.getParentUuid())) {
					children.add(inst);
				}
			}
			
			Collections.sort(children, new Comparator<Object>() {
				@Override
				public int compare(Object o1, Object o2) {
					Long leftSide = -1L;
					Long rightSide = -1L;
					if (o1 instanceof DimensionDto) {
						leftSide = ((DimensionDto)o1).getOrderInLevel();
					} else {
						leftSide = ((IndicatorInstanceDto)o1).getOrderInLevel();
					}
					
					if (o2 instanceof DimensionDto) {
						rightSide = ((DimensionDto)o2).getOrderInLevel();
					} else {
						rightSide = ((IndicatorInstanceDto)o2).getOrderInLevel();
					}
					
					return leftSide.compareTo(rightSide);
				}
			});
			return children;
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
                        SystemStructurePanel.this.selectDimension((DimensionDto)node.getSource());
                    }
                }
            });
            treeGrid.addLeafClickHandler(new LeafClickHandler() {
                @Override
                public void onLeafClick(LeafClickEvent event) {
                    IndSystemContentNode node = (IndSystemContentNode)event.getLeaf();
                    if (node.isIndicator()) {
                        SystemStructurePanel.this.selectIndicatorInstance((IndicatorInstanceDto)node.getSource());
                    }
                }
            });
            treeGrid.addDropHandler(new DropHandler() {
				
				@Override
				public void onDrop(DropEvent event) {
					if (!isDroppable(treeGrid.getDropFolder(), treeGrid.getDragData())) {
						event.cancel();
					}
				}
			});
            
            treeGrid.addDragStopHandler(new DragStopHandler() {
				@Override
				public void onDragStop(DragStopEvent event) {
					/* Workaround: when a node is dropped into a closed folder, the getDropFolder method returns 
					 * the dropped node instead of dropped folder, in order to solve this the dropped folder is obtained getting
					 * the first dropped node and finding out who its parent is.
					 */
					TreeNode firstDragged = ((TreeNode)(treeGrid.getDragData()[0]));
					TreeNode dropFolder = treeGrid.getData().getParent(firstDragged);
					if (isDroppable(dropFolder, treeGrid.getDragData())) {
						Record[] records = treeGrid.getDragData();
						List<Object> content = new ArrayList<Object>();
						for (Record rec : records) {
							IndSystemContentNode node = (IndSystemContentNode)rec;
							content.add(node.getSource());
						}
						//Get drop folder, finding out target dimension/root 
						IndSystemContentNode nodeParent = (IndSystemContentNode)(dropFolder);
						String targetUuid = nodeParent.isRoot() ? null : nodeParent.getId();
						
						//We need to find out new order, we do this observing updated tree structure
						TreeNode[] siblings = treeGrid.getData().getChildren(nodeParent);
						Long order = 1L;
						
						//We look for the index of first inserting element in updated tree
						String firstInsertElementUuid = ((IndSystemContentNode)(records[0])).getId();
						int index = -1;
						for (index = 0; index < siblings.length; index++) {
							IndSystemContentNode contNode = (IndSystemContentNode)(siblings[index]);
							if (contNode.getId().equals(firstInsertElementUuid)) {
								break;
							}
						}
						//We want the node right before
						if (index == 0) {
							order = 1L;
						} else {
							IndSystemContentNode contNode = (IndSystemContentNode)(siblings[index-1]);
							if (contNode.getSource() instanceof DimensionDto) {
								order = ((DimensionDto)contNode.getSource()).getOrderInLevel() + 1;
							} else if (contNode.getSource() instanceof IndicatorInstanceDto) {
								order = ((IndicatorInstanceDto)contNode.getSource()).getOrderInLevel() + 1;
							}
						}
						
						uiHandlers.moveSystemStructureNodes(system.getUuid(), targetUuid, content, order);
					}
				}
			});
            
            treeGrid.addFolderOpenedHandler(new FolderOpenedHandler() {
				
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
       
        private boolean isDroppable(TreeNode dropFolder, Record[] dragData) {
        	if (treeGrid.getDropFolder().getAttribute(IndSystemContentNode.ATTR_NAME).equals("/")) {	//Replacing false root is not allowed
				ShowMessageEvent.fire(SystemStructurePanel.this, ErrorUtils.getMessageList("No puede existir más de una raiz"), MessageTypeEnum.ERROR);
				return false;
			} else { 
				//can´t move a node into any of its descendents
				List<String> messages = new ArrayList<String>();
				treeGrid.getDropFolder();
				for (Record rec: treeGrid.getDragData()) {
					if (rec instanceof TreeNode) {
						TreeNode dragNode = (TreeNode)rec;
						if (treeGrid.getData().isDescendantOf(treeGrid.getDropFolder(), dragNode)) {
							messages.add("Nodo X no se puede mover al destino al ser descendiente");
						}
						
					}
				}
				if (!messages.isEmpty()) {
					ShowMessageEvent.fire(SystemStructurePanel.this, messages, MessageTypeEnum.ERROR);
					return false;
				}
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
                        SystemStructurePanel.this.selectDimForCreate = (DimensionDto)selNode.getSource();
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
                        SystemStructurePanel.this.selectDimForCreate = (DimensionDto)selNode.getSource();
                    }
                    SystemStructurePanel.this.showIndicInstanceCreatePanel();
				}
			});
            
            if (node.isDimension()) {
                MenuItem item1 = new MenuItem(getConstants().systemStrucDimDelete());
                item1.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(MenuItemClickEvent event) {
						DimensionDto dim = (DimensionDto)selNode.getSource();
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
					IndicatorInstanceDto instance = (IndicatorInstanceDto)selNode.getSource();
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
        }
        
        public void setDimension(DimensionDto dim) {
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
        private InternationalMainFormLayout mainFormLayout;
        
        private Label titleLabel;
        
        private GroupDynamicForm form; 
        private GroupDynamicForm editForm; 
        
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
        }
        
        public void resetTitle() {
            titleLabel.setContents(getConstants().systemStrucIndInstanceTitle()+":");
        }
        
        public void setEditionMode() {
            mainFormLayout.setEditionMode();
        }
        
        public void clearForms() {
        	form.clearValues();
        	editForm.clearValues();
        }
        
        private IndicatorInstanceDto fillIndicatorInstance(IndicatorInstanceDto inst) {
        	inst.setTitle((InternationalStringDto)(editForm.getValue(IndicatorInstanceDS.FIELD_INTERNATIONAL_TITLE)));
        	return inst;
        }
        
        public void setIndicatorInstance(IndicatorInstanceDto indInst) {
            titleLabel.setContents(getConstants().systemStrucIndInstanceTitle()+": "+InternationalStringUtils.getLocalisedString(indInst.getTitle()));
            setIndicatorInstanceView(indInst);
            setIndicatorInstanceEdit(indInst);
        }
        
        private void setIndicatorInstanceView(IndicatorInstanceDto indInst) {
        	form.setValue(IndicatorInstanceDS.FIELD_INTERNATIONAL_TITLE, RecordUtils.getInternationalStringRecord(indInst.getTitle()));
        }
        
        private void setIndicatorInstanceEdit(IndicatorInstanceDto indInst) {
        	editForm.setValue(IndicatorInstanceDS.FIELD_INTERNATIONAL_TITLE, RecordUtils.getInternationalStringRecord(indInst.getTitle()));
        }
        
        private void createViewForm() {
            form = new GroupDynamicForm(getConstants().systemStrucIndInstanceTitle());
            ViewMultiLanguageTextItem staticName = new ViewMultiLanguageTextItem(IndicatorInstanceDS.FIELD_INTERNATIONAL_TITLE, getConstants().systemStrucIndInstanceTitleField());
            form.setFields(staticName);
            mainFormLayout.addViewCanvas(form);
        }
        
        private void createEditForm() {
            editForm = new GroupDynamicForm(getConstants().systemStrucIndInstanceTitle());
            MultiLanguageTextItem editName = new MultiLanguageTextItem(IndicatorInstanceDS.FIELD_INTERNATIONAL_TITLE, getConstants().systemStrucIndInstanceTitleField());
            editForm.setFields(editName);
            mainFormLayout.addEditionCanvas(editForm);
        }

    }
}
