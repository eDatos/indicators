package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.Indicator;
import es.gobcan.istac.indicators.core.serviceimpl.util.ServiceUtils;
import es.gobcan.istac.indicators.core.util.ListBlockIterator;
import es.gobcan.istac.indicators.core.util.ListBlockIteratorFn;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.UUID;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.UUIDS;

/**
 * Repository implementation for Indicator
 */
@Repository("indicatorRepository")
public class IndicatorRepositoryImpl extends IndicatorRepositoryBase {

    public IndicatorRepositoryImpl() {
    }

    @Override
    public Indicator retrieveIndicator(String uuid) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(UUID, uuid);
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
        return new ListBlockIterator<String, String>(indicatorsUuid, ServiceUtils.ORACLE_IN_MAX).iterate(new ListBlockIteratorFn<String, String>() {

            @Override
            public List<String> apply(List<String> sublist) {
                String queryHql = "select distinct(i.uuid) from Indicator i where i.uuid in (:uuids) and i.isPublished = false";
                Query query = getEntityManager().createQuery(queryHql);
                query.setParameter(UUIDS, sublist);
                return query.getResultList();
            }
        });
    }

}
