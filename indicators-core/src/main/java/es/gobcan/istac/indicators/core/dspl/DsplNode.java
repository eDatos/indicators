package es.gobcan.istac.indicators.core.dspl;

public abstract class DsplNode {

    protected String id;

    protected DsplNode(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DsplNode) {
            return id.equals(((DsplNode) (obj)).id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
