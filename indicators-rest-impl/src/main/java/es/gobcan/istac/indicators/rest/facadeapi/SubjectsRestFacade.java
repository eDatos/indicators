package es.gobcan.istac.indicators.rest.facadeapi;

import es.gobcan.istac.indicators.rest.types.SubjectBaseType;

import java.util.List;

public interface SubjectsRestFacade {

    public List<SubjectBaseType> retrieveSubjects(final String baseUrl) throws Exception;

}
