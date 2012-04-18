package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.Indicator;

/**
 * Repository implementation for Indicator
 */
@Repository("indicatorRepository")
public class IndicatorRepositoryImpl extends IndicatorRepositoryBase {

    public IndicatorRepositoryImpl() {
    }

    public Indicator retrieveIndicator(String uuid) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uuid", uuid);
        List<Indicator> result = findByQuery("from Indicator i where i.uuid = :uuid", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> filterIndicatorsNotPublished(List<String> indicatorsUuid) {
        Query query = getEntityManager().createQuery("select distinct(i.uuid) from Indicator i where i.uuid in (:uuids) and i.isPublished = false");
        query.setParameter("uuids", indicatorsUuid);
        List<String> result = query.getResultList();
        return result;
    }
    
}
