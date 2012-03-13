package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.Subject;

/**
 * Repository implementation for Subject
 */
@Repository("subjectRepository")
public class SubjectRepositoryImpl extends SubjectRepositoryBase {
    public SubjectRepositoryImpl() {
    }

    public Subject retrieveSubject(String code) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id", code);
        List<Subject> result = findByQuery("from Subject s where s.id = :id", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public List<Subject> findSubjects() {
        List<Subject> result = findByQuery("from Subject s order by s.id", null);
        return result;
    }
}
