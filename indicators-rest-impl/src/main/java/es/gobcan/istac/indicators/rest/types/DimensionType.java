package es.gobcan.istac.indicators.rest.types;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"uuid", "parentUuid", "orderInLevel", "title"})
public class DimensionType {

    private String              uuid         = null;
    private String              parentUuid   = null;

    private Long                orderInLevel = null;
    private Map<String, String> title        = null;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getParentUuid() {
        return parentUuid;
    }

    public void setParentUuid(String parentUuid) {
        this.parentUuid = parentUuid;
    }

    public Long getOrderInLevel() {
        return orderInLevel;
    }

    public void setOrderInLevel(Long orderInLevel) {
        this.orderInLevel = orderInLevel;
    }

    public Map<String, String> getTitle() {
        return title;
    }

    public void setTitle(Map<String, String> title) {
        this.title = title;
    }

}
