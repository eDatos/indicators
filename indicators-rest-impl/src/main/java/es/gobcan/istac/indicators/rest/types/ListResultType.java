package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"kind", "total", "items"})
public class ListResultType<E extends Serializable> implements Serializable {

    private static final long serialVersionUID = 3350142395512943541L;

    private String            kind             = null;
    private Integer           total            = null;
    private List<E>           items            = null;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Integer getTotal() {
        return total;
    }

    private void setTotal(Integer total) {
        this.total = total;
    }

    public List<E> getItems() {
        return items;
    }

    public void setItems(List<E> items) {
        this.items = items;
        setTotal(items != null ? items.size() : 0);
    }

}
