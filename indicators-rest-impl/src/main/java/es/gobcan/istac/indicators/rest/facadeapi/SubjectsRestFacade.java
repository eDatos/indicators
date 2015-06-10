package es.gobcan.istac.indicators.rest.facadeapi;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;

import es.gobcan.istac.indicators.rest.types.SubjectBaseType;

public interface SubjectsRestFacade {

    List<SubjectBaseType> retrieveSubjects() throws MetamacException;

}
