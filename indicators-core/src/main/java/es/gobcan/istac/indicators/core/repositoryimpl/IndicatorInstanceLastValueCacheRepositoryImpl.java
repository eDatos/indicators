package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorInstanceLastValueCache;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorProcStatusEnum;

/**
 * Repository implementation for IndicatorInstanceLastValueCache
 */
@Repository("indicatorInstanceLastValueCacheRepository")
public class IndicatorInstanceLastValueCacheRepositoryImpl extends IndicatorInstanceLastValueCacheRepositoryBase {

    public IndicatorInstanceLastValueCacheRepositoryImpl() {
    }

    @Override
    public List<IndicatorInstance> findLastNIndicatorsInstancesWithGeoCodeInIndicatorsSystemOrderedByLastUpdate(String systemCode, String geoCode, int n) {
        String queryHql = "select tuple.indicatorInstance, tuple.lastDataUpdated ";
        queryHql += "from IndicatorInstanceLastValueCache tuple, IndicatorVersion indV ";
        queryHql += "where tuple.geographicalCode = :geoCode ";
        queryHql += "and tuple.indicatorInstance.elementLevel.indicatorsSystemVersion.indicatorsSystem.code = :systemCode ";
        queryHql += "and tuple.indicatorInstance.indicator.diffusionVersion.idIndicatorVersion = indV.id ";
        queryHql += "and indV.procStatus = :publishedStatus ";
        queryHql += "order by tuple.lastDataUpdated.datetime desc";

        Query query = getEntityManager().createQuery(queryHql);
        query.setParameter("publishedStatus", IndicatorProcStatusEnum.PUBLISHED);
        query.setParameter("systemCode", systemCode);
        query.setParameter("geoCode", geoCode);
        query.setMaxResults(n);

        List<IndicatorInstance> indicatorsInstances = new ArrayList<IndicatorInstance>();
        List<Object> results = query.getResultList();
        for (Object result : results) {
            Object[] fields = (Object[]) result;
            indicatorsInstances.add((IndicatorInstance) fields[0]);
        }
        return indicatorsInstances;
    }

    @Override
    public IndicatorInstanceLastValueCache retrieveLastValueCacheForIndicatorInstanceWithGeoCode(String indicatorInstanceUuid, String geoCode) {
        String queryHql = "select tuple ";
        queryHql += "from IndicatorInstanceLastValueCache tuple ";
        queryHql += "where tuple.indicatorInstance.uuid = :uuidInstance ";
        queryHql += "and tuple.geographicalCode = :geoCode ";

        Query query = getEntityManager().createQuery(queryHql);
        query.setParameter("uuidInstance", indicatorInstanceUuid);
        query.setParameter("geoCode", geoCode);
        List<IndicatorInstanceLastValueCache> latestValues = query.getResultList();

        if (latestValues != null && latestValues.size() > 0) {
            return latestValues.get(0);
        }
        return null;
    }

    @Override
    public void deleteWithIndicatorInstance(String indicatorInstanceUuid) {
        String queryHql = "select tuple ";
        queryHql += "from IndicatorInstanceLastValueCache tuple ";
        queryHql += "where tuple.indicatorInstance.uuid = :uuidInstance ";

        Query query = getEntityManager().createQuery(queryHql);
        query.setParameter("uuidInstance", indicatorInstanceUuid);

        List<IndicatorInstanceLastValueCache> entities = query.getResultList();
        for (IndicatorInstanceLastValueCache entity : entities) {
            delete(entity);
        }
        getEntityManager().flush();
    }
}
