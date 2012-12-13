package es.gobcan.istac.indicators.core.dspl;

import java.util.ArrayList;
import java.util.List;


public class DsplSlice extends DsplNode {

    private List<String> dimensions;
    private List<String> metrics;
    private DsplTable table;
    
    public DsplSlice(String id) {
        super(id);
        dimensions = new ArrayList<String>();
        metrics = new ArrayList<String>();
    }
    
    public void addDimension(String dimRef) {
        dimensions.add(dimRef);
    }
    
    public void addMetric(String metricRef) {
        metrics.add(metricRef);
    }

    public List<String> getDimensions() {
        return dimensions;
    }
    
    public List<String> getMetrics() {
        return metrics;
    }
    
    
    public void setTable(DsplTable table) {
        this.table = table;
    }
    
    
    public DsplTable getTable() {
        return table;
    }
}
