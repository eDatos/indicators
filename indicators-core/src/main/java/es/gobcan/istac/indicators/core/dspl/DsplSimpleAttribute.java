package es.gobcan.istac.indicators.core.dspl;

import org.siemac.metamac.core.common.ent.domain.InternationalString;

public class DsplSimpleAttribute extends DsplAttribute {

    private String type;

    public DsplSimpleAttribute(String id, String type, String value) {
        super(id, new DsplLocalisedValue(value));
        this.type = type;
    }

    public DsplSimpleAttribute(String id, String type, InternationalString value) {
        super(id, new DsplLocalisedValue(value));
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
