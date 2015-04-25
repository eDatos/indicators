package es.gobcan.istac.indicators.core.dspl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DsplDataset {

    private String            id;
    private DsplInfo          datasetInfo;
    private DsplInfo          providerInfo;

    private List<DsplTopic>   topics;
    private List<DsplConcept> concepts;
    private List<DsplSlice>   slices;
    private List<DsplTable>   tables;

    public DsplDataset(String id, DsplInfo dsInfo, DsplInfo providerInfo) {
        this.id = id;
        this.datasetInfo = dsInfo;
        this.providerInfo = providerInfo;

        this.topics = new ArrayList<DsplTopic>();
        this.concepts = new ArrayList<DsplConcept>();
        this.slices = new ArrayList<DsplSlice>();
        this.tables = new ArrayList<DsplTable>();
    }

    public String getId() {
        return id;
    }

    public void addTopics(Collection<DsplTopic> topics) {
        for (DsplTopic topic : topics) {
            this.topics.add(topic);
        }
    }

    public void addConcepts(Collection<DsplConcept> concepts) {
        for (DsplConcept dsplConcept : concepts) {
            this.concepts.add(dsplConcept);
            if (dsplConcept.getTable() != null) {
                this.tables.add(dsplConcept.getTable());
            }
        }
    }

    public void addSlices(Collection<DsplSlice> slices) {
        for (DsplSlice slice : slices) {
            this.slices.add(slice);
            if (slice.getTable() != null) {
                this.tables.add(slice.getTable());
            }
        }
    }

    public DsplInfo getDatasetInfo() {
        return datasetInfo;
    }

    public DsplInfo getProviderInfo() {
        return providerInfo;
    }

    public List<DsplTopic> getTopics() {
        return topics;
    }

    public List<DsplConcept> getConcepts() {
        return concepts;
    }

    public List<DsplSlice> getSlices() {
        return slices;
    }

    public List<DsplTable> getTables() {
        return tables;
    }
}
