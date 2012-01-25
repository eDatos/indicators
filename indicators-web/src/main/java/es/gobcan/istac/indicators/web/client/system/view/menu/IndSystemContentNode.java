package es.gobcan.istac.indicadores.web.client.system.view.menu;

import com.smartgwt.client.widgets.tree.TreeNode;

public class IndSystemContentNode extends TreeNode {

	public static final String ATTR_ID = "ID";
	public static final String ATTR_NAME = "Name";
	public static final String ATTR_PARENT = "ParentID";
	public static final String ATTR_TYPE = "Type";
	public static final String ATTR_SOURCE = "Source";
	
	public IndSystemContentNode(String id, String name, IndSystemContentNodeType type, String parent, Object source) {
		super();
		this.setAttribute(ATTR_ID,id);
		this.setAttribute(ATTR_NAME, name);
		this.setAttribute(ATTR_PARENT,parent);
		this.setAttribute(ATTR_TYPE,type);
		this.setAttribute(ATTR_SOURCE,source);
	}
	
	public String getId() {
		return this.getAttribute(ATTR_ID);
	}
	
	public String getName() {
		return this.getAttribute(ATTR_NAME);
	}
	
	public IndSystemContentNodeType getType() {
		return (IndSystemContentNodeType)this.getAttributeAsObject(ATTR_TYPE);
	}
	
	/* Utils for type */
	public boolean isRoot() {
		return IndSystemContentNodeType.ROOT.equals(getType());
	}
	
	public boolean isDimension() {
		return IndSystemContentNodeType.DIMENSION.equals(getType());
	}
	
	public boolean isIndicator() {
		return IndSystemContentNodeType.INDICATOR.equals(getType());
	}
	
	
	
	public String getParentID() {
		return this.getAttribute(ATTR_PARENT);
	}
	
	public Object getSource() {
		return this.getAttributeAsObject(ATTR_SOURCE);
	}
}
