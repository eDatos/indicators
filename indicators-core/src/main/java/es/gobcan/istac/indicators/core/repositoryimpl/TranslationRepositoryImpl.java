package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.serviceimpl.util.ServiceUtils;
import es.gobcan.istac.indicators.core.util.ListBlockIterator;
import es.gobcan.istac.indicators.core.util.ListBlockIteratorFn;
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

    @Override
    public Map<String, Translation> findTranslationsByCodes(List<String> translationCodes) {
        List<Translation> translations = new ListBlockIterator<String, Translation>(translationCodes, ServiceUtils.ORACLE_IN_MAX)
                .iterate(new ListBlockIteratorFn<String, Translation>() {
                    public List<Translation> apply(List<String> subcodes) {
                        Map<String, Object> parameters = new HashMap<String, Object>();
                        parameters.put("codes", subcodes);
                        return findByQuery("from Translation t where t.code in (:codes)", parameters, 1);
                    }
                });

        Map<String,Translation> translationsMap = new HashMap<String, Translation>();
        for (Translation translation : translations) {
            translationsMap.put(translation.getCode(), translation);
        }
        return translationsMap;
    }


}
