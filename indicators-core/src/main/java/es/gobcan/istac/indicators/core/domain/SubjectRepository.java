package es.gobcan.istac.indicators.core.domain;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;

/**
 * Generated interface for Repository for IndicatorVersion
 */
public interface SubjectRepository {

    public static final String BEAN_ID = "subjectRepository";

    public Subject retrieveSubject(String code) throws MetamacException;

    public List<Subject> findSubjects() throws MetamacException;
}
