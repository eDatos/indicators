package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.IndicatorInstance;

/**
 * Repository implementation for IndicatorInstance
 */
@Repository("indicatorInstanceRepository")
public class IndicatorInstanceRepositoryImpl extends IndicatorInstanceRepositoryBase {

    public IndicatorInstanceRepositoryImpl() {
    }

    public IndicatorInstance findIndicatorInstance(String uuid) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uuid", uuid);
        List<IndicatorInstance> result = findByQuery("from IndicatorInstance ii where ii.uuid = :uuid", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public Boolean existAnyIndicatorInstance(String indicatorsSystemUuid, String indicatorsSystemVersionNumber) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("indicatorsSystemUuid", indicatorsSystemUuid);
        parameters.put("indicatorsSystemVersionNumber", indicatorsSystemVersionNumber);
        List<IndicatorInstance> result = findByQuery("from IndicatorInstance ii where ii.elementLevel.indicatorsSystemVersion.indicatorsSystem.uuid = :indicatorsSystemUuid and ii.elementLevel.indicatorsSystemVersion.versionNumber = :indicatorsSystemVersionNumber", parameters, 1);
        return result != null && !result.isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> findIndicatorsLinkedWithIndicatorsSystemVersion(Long indicatorsSystemVersionId) {
        Query query = getEntityManager().createQuery("select distinct(ii.indicator.uuid) from IndicatorInstance ii where ii.elementLevel.indicatorsSystemVersion.id = :id");
        query.setParameter("id", indicatorsSystemVersionId);
        List<String> result = query.getResultList();
        return result;
    }
}
