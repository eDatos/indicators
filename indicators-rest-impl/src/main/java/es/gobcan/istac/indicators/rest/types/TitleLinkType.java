package es.gobcan.istac.indicators.rest.types;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"title"})
public class TitleLinkType extends LinkType {

    private Map<String, String> title = null;

    public Map<String, String> getTitle() {
        return title;
    }

    public void setTitle(Map<String, String> title) {
        this.title = title;
    }
}
