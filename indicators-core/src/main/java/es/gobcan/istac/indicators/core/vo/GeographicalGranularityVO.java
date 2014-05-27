package es.gobcan.istac.indicators.core.vo;

import org.siemac.metamac.core.common.ent.domain.InternationalString;

public class GeographicalGranularityVO {

    private String              code;
    private InternationalString title;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public InternationalString getTitle() {
        return title;
    }

    public void setTitle(InternationalString title) {
        this.title = title;
    }

}
