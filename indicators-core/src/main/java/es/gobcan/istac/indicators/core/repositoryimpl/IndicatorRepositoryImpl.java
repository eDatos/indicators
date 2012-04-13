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
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Indicator> findIndicatorsNeedsUpdate() {
        Query query = getEntityManager().createQuery("select i from Indicator i where i.needsUpdate = true");
        List<Indicator> result = query.getResultList();
        return result;
    }
    
    @Override
    public List<Indicator> findIndicatorsWithPublishedVersionLinkedToAnyDataGpeUuids(List<String> dataGpeUuids) throws MetamacException {
        StringBuffer querySql = new StringBuffer();
        if (dataGpeUuids == null || dataGpeUuids.size() == 0) {
            return new ArrayList<Indicator>();
        }

        querySql.append("select ind ");
        querySql.append("from Indicator as ind, ");
        querySql.append("IndicatorVersion as indV ");
        querySql.append("inner join indV.dataSources as ds ");
        querySql.append("where indV.id = ind.diffusionVersion.idIndicatorVersion ");
        querySql.append("and ds.dataGpeUuid in (:dataGpeUuids)");
        
        Query query = getEntityManager().createQuery(querySql.toString());
        query.setParameter("dataGpeUuids", dataGpeUuids);
        List<Indicator> results = query.getResultList();
        return results;
    }
}
