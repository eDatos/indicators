package es.gobcan.istac.indicators.core.domain;

import java.util.List;

/**
 * Generated interface for Repository for IndicatorVersion
 */
public interface SubjectRepository {
    
    public static final String BEAN_ID = "subjectRepository";

    public Subject retrieveSubject(String code);

    public List<Subject> findSubjects();
}
