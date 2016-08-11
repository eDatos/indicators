package es.gobcan.istac.indicators.core.dspl;

public class DsplTable extends DsplNode {

    private DsplData data;

    public DsplTable(String id) {
        super(id);
    }

    public void setData(DsplData data) {
        this.data = data;
        this.data.setDataFileName(id + ".csv");
    }

    public DsplData getData() {
        return data;
    }
}
