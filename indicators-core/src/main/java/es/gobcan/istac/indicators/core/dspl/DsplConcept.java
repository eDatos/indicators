package es.gobcan.istac.indicators.core.dspl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DsplConcept extends DsplNode {

    private DsplInfo            info;
    private List<String>        topics;
    private List<DsplAttribute> attributes;
    private DsplDataType        type;
    private String              extend;

    private DsplTable           table;

    public DsplConcept(String id, DsplInfo info) {
        super(id);
        this.info = info;
        this.topics = new ArrayList<String>();
        this.attributes = new ArrayList<DsplAttribute>();
    }

    public void setType(DsplDataType type) {
        this.type = type;
    }

    public DsplDataType getType() {
        return type;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public void addTopic(String idTopic) {
        topics.add(idTopic);
    }

    public DsplInfo getInfo() {
        return info;
    }

    public String getTableRef() {
        return getTable().getId();
    }

    public Map<String, Set<String>> getTableMapProperties() {
        DsplData data = getTable().getData();
        return data.getLocalisedColumns();
    }

    public void setTable(DsplTable table) {
        this.table = table;
    }

    public DsplTable getTable() {
        return table;
    }

    public void addAttribute(DsplAttribute attribute) {
        attributes.add(attribute);
    }

    public List<String> getTopics() {
        return topics;
    }

    public List<DsplAttribute> getAttributes() {
        return attributes;
    }

    public List<DsplConceptAttribute> getConceptAttributes() {
        List<DsplConceptAttribute> conceptAttributes = new ArrayList<DsplConceptAttribute>();
        for (DsplAttribute attribute : attributes) {
            if (attribute instanceof DsplConceptAttribute) {
                conceptAttributes.add((DsplConceptAttribute) attribute);
            }
        }
        return conceptAttributes;
    }

    public List<DsplSimpleAttribute> getSimpleAttributes() {
        List<DsplSimpleAttribute> simpleAttributes = new ArrayList<DsplSimpleAttribute>();
        for (DsplAttribute attribute : attributes) {
            if (attribute instanceof DsplSimpleAttribute) {
                simpleAttributes.add((DsplSimpleAttribute) attribute);
            }
        }
        return simpleAttributes;
    }
}
