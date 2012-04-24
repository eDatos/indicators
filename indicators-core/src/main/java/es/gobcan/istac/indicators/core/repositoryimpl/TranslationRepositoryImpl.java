package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.Translation;

/**
 * Repository implementation for Translation
 */
@Repository("translationRepository")
public class TranslationRepositoryImpl extends TranslationRepositoryBase {

    public TranslationRepositoryImpl() {
    }

    public Translation findTranslationByCode(String code) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("code", code);
        List<Translation> result = findByQuery("from Translation t where t.code = :code", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}
