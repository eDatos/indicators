package es.gobcan.istac.indicators.core.dspl;

import java.util.ArrayList;
import java.util.List;

public class DsplTopic extends DsplNode {

    public DsplInfo         info;
    private List<DsplTopic> topics;

    public DsplTopic(String id, DsplInfo info) {
        super(id);
        this.info = info;
        topics = new ArrayList<DsplTopic>();
    }

    public void addChildTopic(DsplTopic topic) {
        topics.add(topic);
    }

    public List<DsplTopic> getChildrenTopics() {
        return new ArrayList<DsplTopic>(topics);
    }

    public DsplInfo getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return "Topic " + getId();
    }
}
