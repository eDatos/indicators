package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.IndicatorsSystemHistory;

/**
 * Repository implementation for IndicatorsSystemHistory
 */
@Repository("indicatorsSystemHistoryRepository")
public class IndicatorsSystemHistoryRepositoryImpl
    extends IndicatorsSystemHistoryRepositoryBase {
    public IndicatorsSystemHistoryRepositoryImpl() {
    }

    @Override
    public List<IndicatorsSystemHistory> findIndicatorsSystemHistory(String uuid, int maxResults) {
        String queryStr = "select h " +
        		          "from IndicatorsSystemHistory h " +
        		          "where h.indicatorsSystem.uuid = :indicatorsSystemUuid " +
        		          "order by h.publicationDate.datetime desc ";
        Query query = getEntityManager().createQuery(queryStr);
        query.setParameter("indicatorsSystemUuid", uuid);
        query.setMaxResults(maxResults);
        
        return query.getResultList();
    }
    
}
