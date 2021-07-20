package es.gobcan.istac.indicators.core.domain.jsonstat;

import org.codehaus.jackson.annotate.JsonProperty;

public class JsonStatUnit {

    @JsonProperty("base")
    private String  base;

    @JsonProperty("decimals")
    private Integer decimals;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }
}
