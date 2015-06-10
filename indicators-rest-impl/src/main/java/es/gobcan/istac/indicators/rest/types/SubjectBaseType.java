package es.gobcan.istac.indicators.rest.types;

import java.io.Serializable;
import java.util.Map;

public class SubjectBaseType implements Serializable {

    private static final long   serialVersionUID = -453903156267782786L;

    private String              id;
    private String              kind;
    private String              selfLink;
    private String              code;
    private Map<String, String> title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<String, String> getTitle() {
        return title;
    }

    public void setTitle(Map<String, String> title) {
        this.title = title;
    }

}
