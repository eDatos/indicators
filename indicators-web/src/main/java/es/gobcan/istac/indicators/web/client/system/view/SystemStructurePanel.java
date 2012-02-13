package es.gobcan.istac.indicators.web.client.system.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.system.view.tree.IndSystemContentNodeType.DIMENSION;
import static es.gobcan.istac.indicators.web.client.system.view.tree.IndSystemContentNodeType.INDICATOR;
import static es.gobcan.istac.indicators.web.client.system.view.tree.IndSystemContentNodeType.ROOT;
import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.MainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.google.gwt.user.client.Random;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderClickHandler;
import com.smartgwt.client.widgets.tree.events.FolderContextClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderContextClickHandler;
import com.smartgwt.client.widgets.tree.events.LeafClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafClickHandler;
import com.smartgwt.client.widgets.tree.events.LeafContextClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafContextClickHandler;

import es.gobcan.istac.indicators.core.dto.serviceapi.IndicatorsSystemDto;
import es.gobcan.istac.indicators.web.client.resources.IndicatorsResources;
import es.gobcan.istac.indicators.web.client.system.view.tree.IndSystemContentNode;
import es.gobcan.istac.indicators.web.client.system.view.tree.IndSystemContentNodeType;
import es.gobcan.istac.indicators.web.shared.db.AnalysisDimension;
import es.gobcan.istac.indicators.web.shared.db.IndicatorInstance;
import es.gobcan.istac.indicators.web.shared.db.IndicatorSystemContent;

public class SystemStructurePanel extends VLayout {

    private MainFormLayout mainFormLayout;
	private TreePanel treePanel;
	private EditableTreePanel treePanelEdit;
	private DimensionPanel dimPanel;
	private IndicatorInstancePanel indicatorInstPanel;
	
	private IndicatorSystemContent selectContentForCreate;
	private AnalysisDimension selectedDimension;
	private boolean createMode;
	
	public SystemStructurePanel() {
		super();
		this.mainFormLayout = new MainFormLayout();
		
		this.treePanel = new TreePanel();
		this.treePanelEdit = new EditableTreePanel();
		
		this.dimPanel = new DimensionPanel();
		this.dimPanel.hide();
		this.indicatorInstPanel = new IndicatorInstancePanel();
		this.indicatorInstPanel.hide();
		
		this.mainFormLayout.addViewCanvas(treePanel);
		
		this.mainFormLayout.addEditionCanvas(treePanelEdit);
		this.mainFormLayout.addEditionCanvas(dimPanel);
		this.mainFormLayout.addEditionCanvas(indicatorInstPanel);
		
		this.addMember(mainFormLayout);
	}
	

	public void setIndicatorsSystem(IndicatorsSystemDto indSys) {
		treePanel.setIndicatorsSystem(indSys);
		treePanelEdit.setIndicatorsSystem(indSys);
	}
	
	public void setIndicatorSystemStructure(List<IndicatorSystemContent> structure) {
		treePanel.setIndicatorSystemStructure(structure);
		treePanelEdit.setIndicatorSystemStructure(structure);
	}
	
	public void selectDimension(AnalysisDimension dim) {
	    if (indicatorInstPanel.isVisible()) {
	        indicatorInstPanel.hide();
	    }
	    selectedDimension = dim;
	    dimPanel.setDimension(dim);
	    dimPanel.show();
	}
	
	public void selectIndicatorInst(IndicatorInstance indInst) {
	    if (dimPanel.isVisible()) {
	        dimPanel.hide();
	    }
	    indicatorInstPanel.setIndicatorInstance(indInst);
	    indicatorInstPanel.show();
	} 
	
	private void showDimCreatePanel() {
	    hidePanels();
	    selectedDimension = null;
	    dimPanel.clearForms();
	    dimPanel.setEditionMode();
	    dimPanel.resetTitle();
	    dimPanel.show();
	}
	
	private void saveDimension(AnalysisDimension dim) {
	    selectedDimension = dim;
	    treePanelEdit.addNode(selectedDimension, selectContentForCreate);
	    treePanelEdit.markForRedraw();
	}
	
	private void hidePanels() {
        if (dimPanel.isVisible()) {
            dimPanel.hide();
        }
        if (indicatorInstPanel.isVisible()) {
            indicatorInstPanel.hide();
        }
    }
	
	
	private class TreePanel extends VLayout {
		protected final Long FALSE_ROOT_NODE_ID = 0L;
		protected final Long ROOT_NODE_ID = 1L;
		
		/* TREE MANAGEMENT */
		protected IndSystemContentNode falseRoot;
		protected Tree tree;
		protected final TreeGrid treeGrid;
		protected Map<Object,IndSystemContentNode> sourceMapping;
		
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
		
		private void buildNodes(List<IndicatorSystemContent> content, TreeNode parentNode) {
			String parentId = parentNode.getAttribute("ID");
			for (IndicatorSystemContent elem : content) {
				if (elem instanceof IndicatorInstance) {
					IndicatorInstance instance = (IndicatorInstance)elem;
					IndSystemContentNode node = new IndSystemContentNode(parentId+"-"+instance.getId(), instance.getName(), INDICATOR, parentId.toString(), instance);
					tree.add(node, parentNode);
					sourceMapping.put(instance, node);
				} else if (elem instanceof AnalysisDimension) {
					AnalysisDimension dim = (AnalysisDimension)elem;
					IndSystemContentNode node = new IndSystemContentNode(parentId+"-"+dim.getId(), dim.getName(), DIMENSION, parentId+"-"+dim.getId(), dim);
					tree.add(node, parentNode);
					sourceMapping.put(dim, node);
					buildNodes(dim.getContent(),node);
				} else {
					//TODO: Log low level error maybe WARN
				}
			}
		}
		
		public void setIndicatorsSystem(IndicatorsSystemDto indSys) {
			falseRoot.setAttribute("Name", getLocalisedString(indSys.getTitle()));
			falseRoot.setAttribute("Source", indSys);
			treeGrid.redraw();
		}
		
		public void setIndicatorSystemStructure(List<IndicatorSystemContent> structure) {
			//Clear the tree
			tree.removeList(tree.getAllNodes());

			//Build nodes recursevely
			tree.add(falseRoot,"/");
			buildNodes(structure, falseRoot);

			treeGrid.redraw();
		}
	}

	private class EditableTreePanel extends TreePanel {
	    
        public EditableTreePanel() {
            super();
            bindEvents();
        }
        
        public void addNode(IndicatorSystemContent content, IndicatorSystemContent parent) {
            //We are adding temporal node, id will be generated
            if (sourceMapping.get(content) == null) {
                String nodeId = "T"+Random.nextInt();
                IndSystemContentNode parentNode = parent == null ? falseRoot : sourceMapping.get(parent);
                String parentId = parentNode.getAttribute("ID");
                IndSystemContentNodeType type = null;
                if (content instanceof AnalysisDimension) {
                    type = DIMENSION;
                } else if (content instanceof IndicatorInstance) {
                    type = INDICATOR;
                }
                IndSystemContentNode node = new IndSystemContentNode(parentId+"-"+nodeId, content.getName(), type, parentId.toString(), content);
                tree.add(node, parentNode);
                sourceMapping.put(content, node);
            } else {
                //Node already existed, we update stuff
                sourceMapping.get(content).setName(content.getName());
            }
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
                        SystemStructurePanel.this.selectDimension((AnalysisDimension)node.getSource());
                    }
                }
            });
            treeGrid.addLeafClickHandler(new LeafClickHandler() {
                @Override
                public void onLeafClick(LeafClickEvent event) {
                    IndSystemContentNode node = (IndSystemContentNode)event.getLeaf();
                    if (node.isIndicator()) {
                        SystemStructurePanel.this.selectIndicatorInst((IndicatorInstance)node.getSource());
                    }
                }
            });
        }
        
        private Menu buildContextMenuNode(IndSystemContentNode node) {
            final IndSystemContentNode selNode = node;
            Menu menu = new Menu();
            MenuItem item3 = new MenuItem(getConstants().systemStrucNewDim());
            item3.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(MenuItemClickEvent event) {
                    if (selNode == falseRoot)
                        SystemStructurePanel.this.selectContentForCreate = null;
                    else {
                        SystemStructurePanel.this.selectContentForCreate = (AnalysisDimension)selNode.getSource();
                    }
                    SystemStructurePanel.this.createMode = true;
                    SystemStructurePanel.this.showDimCreatePanel();
                }
            });
            MenuItem item4 = new MenuItem(getConstants().systemStrucNewIndInstance());
            
            if (node.isDimension()) {
                MenuItem item1 = new MenuItem(getConstants().systemStrucDimDelete());
                menu.addItem(item1);
            }
            
            menu.addItem(item3);
            menu.addItem(item4);
            return menu;
        }
        
        private Menu buildContextMenuLeaf(IndSystemContentNode node) {
            Menu menu = new Menu();
            MenuItem item1 = new MenuItem(getConstants().systemStrucIndInstanceDelete());
            menu.addItem(item1);
            return menu;
        }
        
        
	}
	
	private class DimensionPanel extends VLayout {
		private InternationalMainFormLayout mainFormLayout;
		
		private ViewTextItem staticName;
		private RequiredTextItem editName;
		private Label titleLabel;
		private GroupDynamicForm editionForm;
		
        public DimensionPanel() {
            mainFormLayout = new InternationalMainFormLayout();
            mainFormLayout.setMargin(0);
            createMode = false;
            ToolStripButton saveButton = mainFormLayout.getSave();
            saveButton.setIcon(IndicatorsResources.RESOURCE.okApply().getURL());
            saveButton.setTitle(getConstants().systemStrucDimOk());
            
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
        
        private void bindEvents() {
            mainFormLayout.getSave().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (editionForm.validate()) {
                        AnalysisDimension dim = null;
                        if (selectedDimension != null) {
                            dim = fillDimension(selectedDimension);
                        } else {
                            dim = fillDimension(new AnalysisDimension());
                        }
                        SystemStructurePanel.this.saveDimension(dim);
                        SystemStructurePanel.this.createMode = false;
                        DimensionPanel.this.setDimension(dim);
                        DimensionPanel.this.mainFormLayout.setViewMode();
                    }
                }
            });
        }
        
        public void setDimension(AnalysisDimension dim) {
            titleLabel.setContents(getConstants().systemStrucDimTitle()+": "+dim.getName());
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
            editName.clearValue();
            staticName.clearValue();
        }
        
        private AnalysisDimension fillDimension(AnalysisDimension dim) {
            dim.setName(editName.getValueAsString());
            return dim;
        }
        
        private void setDimensionView(AnalysisDimension dim) {
            staticName.setValue(dim.getName());
        }
        
        private void setDimensionEdit(AnalysisDimension dim) {
            editName.setValue(dim.getName());
        }
        
        private void createViewForm() {
            GroupDynamicForm form = new GroupDynamicForm(getConstants().systemStrucDimTitle());
            staticName = new ViewTextItem("dim-name-view", getConstants().systemStrucDimName());
            form.setFields(staticName);
            mainFormLayout.addViewCanvas(form);
        }
        
        private void createEditForm() {
            editionForm = new GroupDynamicForm(getConstants().systemStrucDimTitle());
            editName = new RequiredTextItem("dim-name-edit", getConstants().systemStrucDimName());
            editionForm.setFields(editName);
            mainFormLayout.addEditionCanvas(editionForm);
        }
        
        
        
	}
	
	private class IndicatorInstancePanel extends VLayout {
        private InternationalMainFormLayout mainFormLayout;
        
        private ViewTextItem staticName;
        private RequiredTextItem editName;
        private Label titleLabel;
        
        public IndicatorInstancePanel() {
            mainFormLayout = new InternationalMainFormLayout();
            mainFormLayout.setMargin(0);
            ToolStripButton saveButton = mainFormLayout.getSave();
            saveButton.setIcon(IndicatorsResources.RESOURCE.okApply().getURL());
            saveButton.setTitle(getConstants().systemStrucIndInstanceOk());
            
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
        }
        
        public void setIndicatorInstance(IndicatorInstance indInst) {
            titleLabel.setContents(getConstants().systemStrucIndInstanceTitle()+": "+indInst.getName());
            setDimensionView(indInst);
            setDimensionEdit(indInst);
        }
        
        private void setDimensionView(IndicatorInstance indInst) {
            staticName.setValue(indInst.getName());
        }
        
        private void setDimensionEdit(IndicatorInstance indInst) {
            editName.setValue(indInst.getName());
        }
        
        private void createViewForm() {
            GroupDynamicForm form = new GroupDynamicForm(getConstants().systemStrucIndInstanceTitle());
            staticName = new ViewTextItem("indinst-name-view", getConstants().systemStrucDimName());
            form.setFields(staticName);
            mainFormLayout.addViewCanvas(form);
        }
        
        private void createEditForm() {
            GroupDynamicForm form = new GroupDynamicForm(getConstants().systemStrucIndInstanceTitle());
            editName = new RequiredTextItem("indinst-name-edit", getConstants().systemStrucDimName());
            form.setFields(editName);
            mainFormLayout.addEditionCanvas(form);
        }
        
    }
}
