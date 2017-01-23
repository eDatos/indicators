package es.gobcan.istac.indicators.core.repositoryimpl;

import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.DATA_GPE_UUIDS;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.INDICATOR_CODE;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.PROC_STATUS;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.PUBLISHED_STATUS;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.SUBJECT_CODE;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.UUID;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.VERSION_NUMBER;

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
import es.gobcan.istac.indicators.core.serviceimpl.util.ServiceUtils;
import es.gobcan.istac.indicators.core.util.ListBlockIterator;
import es.gobcan.istac.indicators.core.util.ListBlockIteratorFn;

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
        parameters.put(UUID, uuid);
        parameters.put(VERSION_NUMBER, versionNumber);
        List<IndicatorVersion> result = findByQuery("from IndicatorVersion iv where iv.indicator.uuid = :uuid and iv.versionNumber = :versionNumber", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public IndicatorVersion findPublishedIndicatorVersionByCode(String indicatorCode) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(INDICATOR_CODE, indicatorCode);
        parameters.put(PUBLISHED_STATUS, IndicatorProcStatusEnum.PUBLISHED);

        // @formatter:off
        String query = "from IndicatorVersion iv " +
                       "where iv.indicator.code = :indicatorCode " +
                                "and iv.procStatus = :publishedStatus " +
                                "and iv.indicator.diffusionVersionNumber != null " +
                                "and iv.indicator.diffusionVersionNumber = iv.versionNumber";
        // @formatter:on

        List<IndicatorVersion> result = findByQuery(query, parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public List<IndicatorVersion> findPublishedIndicatorVersionWithSubjectCode(String subjectCode) throws MetamacException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(SUBJECT_CODE, subjectCode);
        parameters.put(PUBLISHED_STATUS, IndicatorProcStatusEnum.PUBLISHED);

        // @formatter:off
        String query = "from IndicatorVersion iv " +
                       "where iv.subjectCode = :subjectCode " +
                           "and iv.procStatus = :publishedStatus " +
                           "and iv.indicator.diffusionVersionNumber != null " +
                           "and iv.indicator.diffusionVersionNumber = iv.versionNumber";
        // @formatter:on

        return findByQuery(query, parameters, Integer.MAX_VALUE);
    }

    @Override
    public IndicatorVersion findOneIndicatorVersionLinkedToIndicator(String indicatorUuid) {

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(UUID, indicatorUuid);

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
        query.setParameter(PROC_STATUS, IndicatorProcStatusEnum.PUBLISHED);
        List<Object> results = query.getResultList();

        List<SubjectIndicatorResult> subjectsResults = new ArrayList<SubjectIndicatorResult>();
        if (results != null) {
            for (Object result : results) {
                String subjectCode = (String) ((Object[]) result)[0];
                InternationalString subjectTitle = (InternationalString) ((Object[]) result)[1];

                SubjectIndicatorResult subject = new SubjectIndicatorResult();
                subject.setId(subjectCode);
                subject.setTitle(subjectTitle);
                subjectsResults.add(subject);
            }
        }
        return subjectsResults;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SubjectIndicatorResult> findSubjectsInLastVersionIndicators() throws MetamacException {
        Query query = getEntityManager().createQuery("select iv.subjectCode, min(iv.subjectTitle) from IndicatorVersion iv where iv.isLastVersion = true group by iv.subjectCode");
        List<Object> results = query.getResultList();

        List<SubjectIndicatorResult> subjectsResults = new ArrayList<SubjectIndicatorResult>();
        if (results != null) {
            for (Object result : results) {
                String subjectCode = (String) ((Object[]) result)[0];
                InternationalString subjectTitle = (InternationalString) ((Object[]) result)[1];

                SubjectIndicatorResult subject = new SubjectIndicatorResult();
                subject.setId(subjectCode);
                subject.setTitle(subjectTitle);
                subjectsResults.add(subject);
            }
        }
        return subjectsResults;
    }

    @Override
    public List<IndicatorVersion> findIndicatorsVersionLinkedToAnyDataGpeUuids(List<String> dataGpeUuids) throws MetamacException {
        return new ListBlockIterator<String, IndicatorVersion>(dataGpeUuids, ServiceUtils.ORACLE_IN_MAX).iterate(new ListBlockIteratorFn<String, IndicatorVersion>() {

            @SuppressWarnings("unchecked")
            @Override
            public List<IndicatorVersion> apply(List<String> sublist) {
                StringBuilder querySql = new StringBuilder();
                if (sublist == null || sublist.size() == 0) {
                    return new ArrayList<IndicatorVersion>();
                }

                querySql.append("select indV ");
                querySql.append("from IndicatorVersion as indV ");
                querySql.append("inner join indV.dataSources as ds ");
                querySql.append("where ds.queryUuid in (:dataGpeUuids)");

                Query query = getEntityManager().createQuery(querySql.toString());
                query.setParameter(DATA_GPE_UUIDS, sublist);
                return query.getResultList();
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<IndicatorVersion> findIndicatorsVersionNeedsUpdate() throws MetamacException {
        Query query = getEntityManager().createQuery("from IndicatorVersion iv where iv.needsUpdate = true");
        return query.getResultList();
    }

}
