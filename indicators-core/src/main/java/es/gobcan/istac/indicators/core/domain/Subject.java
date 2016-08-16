package es.gobcan.istac.indicators.core.domain;

public class Subject {

    private String id;
    private String title;

    public Subject() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Subject) {
            return getKey().equals(((Subject) obj).getKey());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }

    public Object getKey() {
        return getId();
    }
}
