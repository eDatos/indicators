package es.gobcan.istac.indicators.web.client.system.view.tree;

import com.smartgwt.client.widgets.tree.TreeNode;

import es.gobcan.istac.indicators.core.dto.serviceapi.ElementLevelDto;

public class IndSystemContentNode extends TreeNode {

    public static final String ATTR_ID     = "ID";
    public static final String ATTR_NAME   = "Name";
    public static final String ATTR_PARENT = "ParentID";
    public static final String ATTR_TYPE   = "Type";
    public static final String ATTR_SOURCE = "Source";

    public IndSystemContentNode(String id, String name, String parent, ElementLevelDto source) {
        super();
        this.setAttribute(ATTR_ID, id);
        this.setAttribute(ATTR_NAME, name);
        this.setAttribute(ATTR_PARENT, parent);
        this.setAttribute(ATTR_SOURCE, source);
        IndSystemContentNodeType type = null;
        if (source == null) {
            type = IndSystemContentNodeType.ROOT;
        } else if (source.isDimension()) {
            type = IndSystemContentNodeType.DIMENSION;
        } else if (source.isIndicatorInstance()) {
            type = IndSystemContentNodeType.INDICATOR;
        }
        this.setAttribute(ATTR_TYPE, type);

        if (IndSystemContentNodeType.INDICATOR.equals(type)) {
            this.setIsFolder(false);
            this.setCanDrag(true);
            this.setCanAcceptDrop(true);
        } else {
            this.setIsFolder(true);
            if (IndSystemContentNodeType.ROOT.equals(type)) {
                this.setCanDrag(false);
                this.setCanAcceptDrop(true);
            } else {
                this.setCanDrag(true);
                this.setCanAcceptDrop(true);
            }
        }

    }

    public String getId() {
        return this.getAttribute(ATTR_ID);
    }

    public void setName(String name) {
        this.setAttribute(ATTR_NAME, name);
    }

    public String getName() {
        return this.getAttribute(ATTR_NAME);
    }

    public IndSystemContentNodeType getType() {
        return (IndSystemContentNodeType) this.getAttributeAsObject(ATTR_TYPE);
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

    public ElementLevelDto getSource() {
        return this.getAttributeAsObject(ATTR_SOURCE) == null ? null : (ElementLevelDto) this.getAttributeAsObject(ATTR_SOURCE);
    }
}
