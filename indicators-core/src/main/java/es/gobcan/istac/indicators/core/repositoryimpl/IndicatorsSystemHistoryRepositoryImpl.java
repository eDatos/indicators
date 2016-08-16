package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.IndicatorsSystemHistory;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.INDICATORS_SYSTEM_UUID;

/**
 * Repository implementation for IndicatorsSystemHistory
 */
@Repository("indicatorsSystemHistoryRepository")
public class IndicatorsSystemHistoryRepositoryImpl extends IndicatorsSystemHistoryRepositoryBase {

    public IndicatorsSystemHistoryRepositoryImpl() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<IndicatorsSystemHistory> findIndicatorsSystemHistory(String uuid, int maxResults) {
        // @formatter:off
        String queryStr = "select h " +
        		          "from IndicatorsSystemHistory h " +
        		          "where h.indicatorsSystem.uuid = :indicatorsSystemUuid " +
        		          "order by h.publicationDate.datetime desc ";
        // @formatter:on
        Query query = getEntityManager().createQuery(queryStr);
        query.setParameter(INDICATORS_SYSTEM_UUID, uuid);
        query.setMaxResults(maxResults);

        return query.getResultList();
    }

}
