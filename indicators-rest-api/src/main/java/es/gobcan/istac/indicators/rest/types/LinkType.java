package es.gobcan.istac.indicators.rest.types;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"kind", "href"})
public class LinkType {

    private String kind = null;
    private String href = null;

    public LinkType() {
        // dummy constructor necessary for Jackson
    }

    public LinkType(String kind, String href) {
        super();
        this.kind = kind;
        this.href = href;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

}
