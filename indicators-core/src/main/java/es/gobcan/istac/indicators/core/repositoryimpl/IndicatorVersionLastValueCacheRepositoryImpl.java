package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersionLastValueCache;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.GEO_CODE;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.INDICATOR_UUID;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.SUBJECT_CODE;

/**
 * Repository implementation for IndicatorVersionLastValueCache
 */
@Repository("indicatorVersionLastValueCacheRepository")
public class IndicatorVersionLastValueCacheRepositoryImpl extends IndicatorVersionLastValueCacheRepositoryBase {

    public IndicatorVersionLastValueCacheRepositoryImpl() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<IndicatorVersion> findLastNIndicatorsVersionsWithGeoCodeAndSubjectCodeOrderedByLastUpdate(String subjectCode, String geoCode, int n) {
        String queryHql = "select distinct(tuple.indicatorVersion), tuple.lastDataUpdated ";
        queryHql += "from IndicatorVersionLastValueCache tuple ";
        queryHql += "where tuple.indicatorVersion.subjectCode = :subjectCode ";
        queryHql += "and tuple.geographicalCode = :geoCode ";
        queryHql += "order by tuple.lastDataUpdated.datetime desc";

        Query query = getEntityManager().createQuery(queryHql);
        query.setParameter(SUBJECT_CODE, subjectCode);
        query.setParameter(GEO_CODE, geoCode);
        query.setMaxResults(n);

        List<IndicatorVersion> indicatorsVersions = new ArrayList<IndicatorVersion>();
        List<Object> results = query.getResultList();
        for (Object result : results) {
            Object[] fields = (Object[]) result;
            indicatorsVersions.add((IndicatorVersion) fields[0]);
        }
        return indicatorsVersions;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IndicatorVersionLastValueCache retrieveLastValueCacheForIndicatorVersionWithGeoCode(String indicatorUuid, String geoCode) {
        String queryHql = "select tuple ";
        queryHql += "from IndicatorVersionLastValueCache tuple ";
        queryHql += "where tuple.geographicalCode = :geoCode ";
        queryHql += "and tuple.indicatorVersion.indicator.uuid = :indicatorUuid ";

        Query query = getEntityManager().createQuery(queryHql);
        query.setParameter(INDICATOR_UUID, indicatorUuid);
        query.setParameter(GEO_CODE, geoCode);

        List<IndicatorVersionLastValueCache> latestValues = query.getResultList();
        if (latestValues != null && latestValues.size() > 0) {
            return latestValues.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void deleteWithIndicatorVersion(String indicatorVersionUuid) {
        String queryHql = "select tuple ";
        queryHql += "from IndicatorVersionLastValueCache tuple ";
        queryHql += "where tuple.indicatorVersion.uuid = :indicatorUuid ";

        Query query = getEntityManager().createQuery(queryHql);
        query.setParameter(INDICATOR_UUID, indicatorVersionUuid);

        List<IndicatorVersionLastValueCache> entities = query.getResultList();

        for (IndicatorVersionLastValueCache entity : entities) {
            delete(entity);
        }
        getEntityManager().flush();
    }
}
