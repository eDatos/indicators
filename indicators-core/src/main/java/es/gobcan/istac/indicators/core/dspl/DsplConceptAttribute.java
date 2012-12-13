package es.gobcan.istac.indicators.core.dspl;

public class DsplConceptAttribute extends DsplAttribute {

    private String concept;

    public DsplConceptAttribute(String id, String conceptId, String value) {
        super(id, new DsplLocalisedValue(value));
        this.concept = conceptId;
    }

    public String getConceptRef() {
        return concept;
    }
}
