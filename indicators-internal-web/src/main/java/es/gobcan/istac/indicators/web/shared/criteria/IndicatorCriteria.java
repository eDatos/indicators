package es.gobcan.istac.indicators.web.shared.criteria;

import com.google.gwt.user.client.rpc.IsSerializable;

public class IndicatorCriteria implements IsSerializable {

    private String code;
    private String title;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
