package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorProcStatusEnum;
import es.gobcan.istac.indicators.core.repositoryimpl.finders.SubjectIndicatorResult;

/**
 * Repository implementation for IndicatorVersion
 */
@Repository("indicatorVersionRepository")
public class IndicatorVersionRepositoryImpl extends IndicatorVersionRepositoryBase {

    public IndicatorVersionRepositoryImpl() {
    }

    @Override
    public IndicatorVersion retrieveIndicatorVersion(String uuid, String versionNumber) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uuid", uuid);
        parameters.put("versionNumber", versionNumber);
        List<IndicatorVersion> result = findByQuery("from IndicatorVersion iv where iv.indicator.uuid = :uuid and iv.versionNumber = :versionNumber", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public IndicatorVersion findOneIndicatorVersionLinkedToIndicator(String indicatorUuid) {

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uuid", indicatorUuid);

        // Important! Queries must be executed separately because indicator can have different value of indicator in numerator, denominator..

        // Numerator
        {
            List<IndicatorVersion> result = findByQuery("from IndicatorVersion iv where iv.quantity.numerator.uuid = :uuid", parameters, 1);
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
        }
        // Denominator
        {
            List<IndicatorVersion> result = findByQuery("from IndicatorVersion iv where iv.quantity.denominator.uuid = :uuid", parameters, 1);
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
        }
        // Base quantity
        {
            List<IndicatorVersion> result = findByQuery("from IndicatorVersion iv where iv.quantity.baseQuantity.uuid = :uuid", parameters, 1);
            if (result != null && !result.isEmpty()) {
                return result.get(0);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SubjectIndicatorResult> findSubjectsInPublishedIndicators() throws MetamacException {
        Query query = getEntityManager().createQuery("select iv.subjectCode, min(iv.subjectTitle) from IndicatorVersion iv where iv.procStatus = :procStatus group by iv.subjectCode");
        query.setParameter("procStatus", IndicatorProcStatusEnum.PUBLISHED);
        List<Object> results = query.getResultList();
        
        List<SubjectIndicatorResult> subjectsResults = new ArrayList<SubjectIndicatorResult>();
        if (results != null) {
            for (Object result : results) {
                String subjectCode = (String)((Object[]) result)[0];
                InternationalString subjectTitle = (InternationalString)((Object[]) result)[1];
                
                SubjectIndicatorResult subjectIndicatorResult = new SubjectIndicatorResult();
                subjectIndicatorResult.setId(subjectCode);
                subjectIndicatorResult.setTitle(subjectTitle);
                subjectsResults.add(subjectIndicatorResult);
            }
        } 
        return subjectsResults;
    }
    
    @Override
    public List<IndicatorVersion> findIndicatorsVersionLinkedToAnyDataGpeUuids(List<String> dataGpeUuids) throws MetamacException {
        StringBuffer querySql = new StringBuffer();
        if (dataGpeUuids == null || dataGpeUuids.size() == 0) {
            return new ArrayList<IndicatorVersion>();
        }

        querySql.append("select indV ");
        querySql.append("from IndicatorVersion as indV ");
        querySql.append("inner join indV.dataSources as ds ");
        querySql.append("where ds.dataGpeUuid in (:dataGpeUuids)");
        
        Query query = getEntityManager().createQuery(querySql.toString());
        query.setParameter("dataGpeUuids", dataGpeUuids);
        List<IndicatorVersion> results = query.getResultList();
        return results;
    }
    
    @Override
    public List<IndicatorVersion> findIndicatorsVersionNeedsUpdate() throws MetamacException {
        Query query = getEntityManager().createQuery("from IndicatorVersion iv where iv.needsUpdate = true");
        List<IndicatorVersion> result = query.getResultList();
        return result;
    }

}
