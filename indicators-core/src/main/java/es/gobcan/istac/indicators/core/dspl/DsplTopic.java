package es.gobcan.istac.indicators.core.dspl;

public class DsplTopic extends DsplNode {

    private DsplInfo info;
    private String   parent;

    public DsplTopic(String id, DsplInfo info) {
        super(id);
        this.info = info;
    }

    public void setParentTopic(DsplTopic topic) {
        parent = topic != null ? topic.getId() : null;
    }

    public String getParentTopic() {
        return parent;
    }

    public DsplInfo getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return "Topic " + getId();
    }
}
