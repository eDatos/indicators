package es.gobcan.istac.indicators.core.domain;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;

/**
 * Generated interface for Repository for IndicatorVersion
 */
public interface SubjectRepository {

    String BEAN_ID = "subjectRepository";

    Subject retrieveSubject(String code) throws MetamacException;

    List<Subject> findSubjects() throws MetamacException;
}
