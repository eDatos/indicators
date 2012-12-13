package es.gobcan.istac.indicators.core.dspl;

public abstract class DsplAttribute extends DsplNode {

    protected DsplLocalisedValue value;

    protected DsplAttribute(String id) {
        super(id);
    }

    protected DsplAttribute(String id, DsplLocalisedValue value) {
        super(id);
        this.value = value;
    }

    public DsplLocalisedValue getValue() {
        return value;
    }

}
