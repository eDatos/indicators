package es.gobcan.istac.indicators.core.repositoryimpl.finders;

import org.siemac.metamac.core.common.ent.domain.InternationalString;

/**
 * Dto for results
 */
public class SubjectIndicatorResult {

    private String              id; // code
    private InternationalString title;

    public SubjectIndicatorResult() {
    }

    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public InternationalString getTitle() {
        return title;
    }

    public void setTitle(InternationalString title) {
        this.title = title;
    }
}
