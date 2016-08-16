package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemProcStatusEnum;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.CODE;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.ID;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.INDICATORS_SYSTEM_UUID;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.INDICATORS_SYSTEM_VERSION_NUMBER;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.PROC_STATUS;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.PUBLISHED_STATUS;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.SYSTEM_CODE;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.UUID;

/**
 * Repository implementation for IndicatorInstance
 */
@Repository("indicatorInstanceRepository")
public class IndicatorInstanceRepositoryImpl extends IndicatorInstanceRepositoryBase {

    public IndicatorInstanceRepositoryImpl() {
    }

    @Override
    public IndicatorInstance findIndicatorInstance(String uuid) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(UUID, uuid);
        List<IndicatorInstance> result = findByQuery("from IndicatorInstance ii where ii.uuid = :uuid", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public IndicatorInstance findIndicatorInstancePublishedByCode(String code) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(CODE, code);
        parameters.put(PROC_STATUS, IndicatorsSystemProcStatusEnum.PUBLISHED);
        List<IndicatorInstance> result = findByQuery("from IndicatorInstance ii where ii.code = :code and ii.elementLevel.indicatorsSystemVersion.procStatus = :procStatus", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public IndicatorInstance findIndicatorInstanceInPublishedIndicatorSystem(String systemCode, String indicatorInstanceCode) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(CODE, indicatorInstanceCode);
        parameters.put(SYSTEM_CODE, systemCode);
        parameters.put(PROC_STATUS, IndicatorsSystemProcStatusEnum.PUBLISHED);

        // @formatter:off
        String queryStr = "from IndicatorInstance ii " +
                          "where ii.code = :code " +
                              "and ii.elementLevel.indicatorsSystemVersion.indicatorsSystem.code = :systemCode " +
                              "and ii.elementLevel.indicatorsSystemVersion.procStatus = :procStatus";
        // @formatter:on

        List<IndicatorInstance> result = findByQuery(queryStr, parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public List<IndicatorInstance> findIndicatorsInstancesInPublishedIndicatorSystem(String systemCode) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(SYSTEM_CODE, systemCode);
        parameters.put(PROC_STATUS, IndicatorsSystemProcStatusEnum.PUBLISHED);

        // @formatter:off
        String queryStr = "from IndicatorInstance ii " +
                          "where ii.elementLevel.indicatorsSystemVersion.indicatorsSystem.code = :systemCode " +
                              "and ii.elementLevel.indicatorsSystemVersion.procStatus = :procStatus";
        // @formatter:on

        List<IndicatorInstance> results = findByQuery(queryStr, parameters, Integer.MAX_VALUE);
        return results;
    }

    @Override
    public Boolean existAnyIndicatorInstance(String indicatorsSystemUuid, String indicatorsSystemVersionNumber) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(INDICATORS_SYSTEM_UUID, indicatorsSystemUuid);
        parameters.put(INDICATORS_SYSTEM_VERSION_NUMBER, indicatorsSystemVersionNumber);
        // @formatter:off
        List<IndicatorInstance> result = findByQuery("from IndicatorInstance ii " +
                                                     "where ii.elementLevel.indicatorsSystemVersion.indicatorsSystem.uuid = :indicatorsSystemUuid " +
                                                         "and ii.elementLevel.indicatorsSystemVersion.versionNumber = :indicatorsSystemVersionNumber",
                                                      parameters, 1);
        // @formatter:on
        return result != null && !result.isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> findIndicatorsLinkedWithIndicatorsSystemVersion(Long indicatorsSystemVersionId) {
        Query query = getEntityManager().createQuery("select distinct(ii.indicator.uuid) from IndicatorInstance ii where ii.elementLevel.indicatorsSystemVersion.id = :id");
        query.setParameter(ID, indicatorsSystemVersionId);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> findIndicatorsSystemsPublishedWithIndicator(String indicatorUuid) {

        // @formatter:off
        Query query = getEntityManager().createQuery("select distinct(ii.elementLevel.indicatorsSystemVersion.indicatorsSystem.uuid) " +
                                                     "from IndicatorInstance ii " +
                                                     "where ii.indicator.uuid = :uuid " +
                                                         "and ii.elementLevel.indicatorsSystemVersion.indicatorsSystem.diffusionVersion is not null " +
                                                         "and ii.elementLevel.indicatorsSystemVersion.versionNumber = ii.elementLevel.indicatorsSystemVersion.indicatorsSystem.diffusionVersion.versionNumber " +
                                                         "and ii.elementLevel.indicatorsSystemVersion.procStatus = :publishedStatus ");
        // @formatter:on
        query.setParameter(UUID, indicatorUuid);
        query.setParameter(PUBLISHED_STATUS, IndicatorsSystemProcStatusEnum.PUBLISHED);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> findIndicatorsInstancesInPublishedIndicatorSystemWithIndicator(String indicatorUuid) {
        // @formatter:off
        Query query = getEntityManager().createQuery("select distinct(ii.uuid) " +
                                                     "from IndicatorInstance ii " +
                                                     "where ii.indicator.uuid = :uuid " +
                                                         "and ii.elementLevel.indicatorsSystemVersion.indicatorsSystem.diffusionVersion is not null " +
                                                         "and ii.elementLevel.indicatorsSystemVersion.versionNumber = ii.elementLevel.indicatorsSystemVersion.indicatorsSystem.diffusionVersion.versionNumber " +
                                                         "and ii.elementLevel.indicatorsSystemVersion.procStatus = :publishedStatus ");
        // @formatter:on
        query.setParameter(UUID, indicatorUuid);
        query.setParameter(PUBLISHED_STATUS, IndicatorsSystemProcStatusEnum.PUBLISHED);
        return query.getResultList();
    }

}
