package es.gobcan.istac.indicadores.web.client.system.view;

import java.util.List;

import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.events.ShowContextMenuEvent;
import com.smartgwt.client.widgets.events.ShowContextMenuHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellContextClickEvent;
import com.smartgwt.client.widgets.grid.events.CellContextClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

import es.gobcan.istac.indicadores.web.client.system.view.menu.IndSystemContentNode;
import es.gobcan.istac.indicadores.web.client.system.view.menu.IndSystemContentNodeType;
import es.gobcan.istac.indicadores.web.shared.db.AnalysisDimension;
import es.gobcan.istac.indicadores.web.shared.db.IndicatorInstance;
import es.gobcan.istac.indicadores.web.shared.db.IndicatorSystem;
import es.gobcan.istac.indicadores.web.shared.db.IndicatorSystemContent;

import static es.gobcan.istac.indicadores.web.client.system.view.menu.IndSystemContentNodeType.*;

public class SystemStructurePanel extends VLayout {

	private static final Long FALSE_ROOT_NODE_ID = 0L;
	private static final Long ROOT_NODE_ID = 1L;
	
	/* TREE MANAGEMENT */
	private TreeNode falseRoot;
	private Tree tree;
	private final TreeGrid treeGrid;
	
	
	public SystemStructurePanel() {
		super();
		falseRoot = createTreeNode(FALSE_ROOT_NODE_ID.toString(), "", ROOT, ROOT_NODE_ID.toString(), null);
		
		tree = new Tree();
		tree.setModelType(TreeModelType.PARENT);
		tree.setNameProperty("Name");
		tree.setIdField("ID");
		tree.setParentIdField("ParentID");
	    tree.setRootValue(ROOT_NODE_ID.toString());  


		treeGrid = new TreeGrid();  
        treeGrid.setWidth100();  
        treeGrid.setHeight(400);  
        treeGrid.setClosedIconSuffix("");  
        treeGrid.setFields(new TreeGridField("Name"));  
        treeGrid.setData(tree);  
        
        treeGrid.addCellContextClickHandler(new CellContextClickHandler() {
			
			@Override
			public void onCellContextClick(CellContextClickEvent event) {
				ListGridRecord record = event.getRecord();
				Menu menu = buildContextMenu("Toma!");
				menu.showContextMenu();
			}
			
		});
        
        
        
        //treeGrid.draw();
        
		this.addMember(treeGrid);
	}
	
	
	
	private void buildNodes(List<IndicatorSystemContent> content, TreeNode parentNode) {
		String parentId = parentNode.getAttribute("ID");
		for (IndicatorSystemContent elem : content) {
			if (elem instanceof IndicatorInstance) {
				IndicatorInstance instance = (IndicatorInstance)elem;
				TreeNode node = createTreeNode(parentId+"-"+instance.getId(), instance.getName(), INDICATOR, parentId.toString(), instance);
				tree.add(node, parentNode);
			} else if (elem instanceof AnalysisDimension) {
				AnalysisDimension dim = (AnalysisDimension)elem;
				TreeNode node = createTreeNode(parentId+"-"+dim.getId(), dim.getName(), DIMENSION, parentId+"-"+dim.getId(), dim);
				tree.add(node, parentNode);
				buildNodes(dim.getContent(),node);
			} else {
				//TODO: Log low level error maybe WARN
			}
		}
	}
	
	private TreeNode createTreeNode(String id, String name, IndSystemContentNodeType type, String parent, Object source) {
		TreeNode node = new IndSystemContentNode(id, name, type, parent, source);
		return node;
	}
	
	private Menu buildContextMenu(IndSystemContentNode node) {
		if (node.isRoot() || node.isDimension()) {
			return buildContextMenuNode(node);
		} else if (node.isIndicator()) {
			return buildContextMenuLeaf(node);
		}
		
		Menu menu = new Menu();
		MenuItem item1 = new MenuItem("Crear Dimensión");
		item1.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(MenuItemClickEvent event) {
			}
		});
		MenuItem item2 = new MenuItem("Crear Instancia de Indicador");
		menu.addItem(item1);
		menu.addItem(item2);
		return menu;
	}
	
	private Menu buildContextMenuNode(IndSystemContentNode node) {
		Menu menu = new Menu();
		MenuItem item1 = new MenuItem("Crear Dimensión");
		item1.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(MenuItemClickEvent event) {
			}
		});
		MenuItem item2 = new MenuItem("Crear Instancia de Indicador");
		menu.addItem(item1);
		menu.addItem(item2);
		return menu;
	}
	
	
	
	private Menu buildContextMenuLeaf(IndSystemContentNode node) {
		Menu menu = new Menu();
		MenuItem item1 = new MenuItem(text);
		menu.addItem(item1);
		return menu;
	}
	

	public void setIndicatorSystem(IndicatorSystem indSys) {
		falseRoot.setAttribute("Name", indSys.getName());
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
