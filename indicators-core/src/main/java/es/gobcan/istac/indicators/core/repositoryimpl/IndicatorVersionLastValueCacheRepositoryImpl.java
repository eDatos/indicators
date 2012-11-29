package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionLastValueCache;

/**
 * Repository implementation for IndicatorVersionLastValueCache
 */
@Repository("indicatorVersionLastValueCacheRepository")
public class IndicatorVersionLastValueCacheRepositoryImpl extends IndicatorVersionLastValueCacheRepositoryBase {
    
    public IndicatorVersionLastValueCacheRepositoryImpl() {
    }
    
    @Override
    public List<IndicatorVersion> findLastNIndicatorsVersionsWithGeoCodeAndSubjectCodeOrderedByLastUpdate(String subjectCode, String geoCode, int n) {
        String queryHql = "select distinct(tuple.indicatorVersion), tuple.lastDataUpdated ";
        queryHql += "from IndicatorVersionLastValueCache tuple ";
        queryHql += "where tuple.indicatorVersion.subjectCode = :subjectCode ";
        queryHql += "and tuple.geographicalValue.code = :geoCode ";
        queryHql += "order by tuple.lastDataUpdated.datetime desc";
        
        Query query = getEntityManager().createQuery(queryHql);
        query.setParameter("subjectCode", subjectCode);
        query.setParameter("geoCode", geoCode);
        query.setMaxResults(n);
        
        List<IndicatorVersion> indicatorsVersions = new ArrayList<IndicatorVersion>(); 
        List<Object> results = query.getResultList();
        for (Object result : results) {
            Object[] fields = (Object[])result;
            indicatorsVersions.add((IndicatorVersion)fields[0]);
        }
        return indicatorsVersions;
    }
    
    
    
    @Override
    public IndicatorVersionLastValueCache retrieveLastValueCacheForIndicatorVersionWithGeoCode(String indicatorUuid, String geoCode) {
        String queryHql = "select tuple ";
        queryHql += "from IndicatorVersionLastValueCache tuple ";
        queryHql += "where tuple.geographicalValue.code = :geoCode ";
        queryHql += "and tuple.indicatorVersion.indicator.uuid = :indicatorUuid ";
        
        Query query = getEntityManager().createQuery(queryHql);
        query.setParameter("indicatorUuid", indicatorUuid);
        query.setParameter("geoCode", geoCode);
        
        List<IndicatorVersionLastValueCache> latestValues = query.getResultList();
        if (latestValues != null && latestValues.size() > 0) {
            return latestValues.get(0);
        }
        return null;
    }
    
    @Override
    public void deleteWithIndicatorVersion(String indicatorUuid) {
        String queryHql = "select tuple ";
        queryHql += "from IndicatorVersionLastValueCache tuple ";
        queryHql += "where tuple.indicatorVersion.indicator.uuid = :indicatorUuid ";
        
        Query query = getEntityManager().createQuery(queryHql);
        query.setParameter("indicatorUuid", indicatorUuid);
        
        List<IndicatorVersionLastValueCache> entities = query.getResultList();
        
        for (IndicatorVersionLastValueCache entity : entities) {
           delete(entity);
        }
        getEntityManager().flush();
    }
}
