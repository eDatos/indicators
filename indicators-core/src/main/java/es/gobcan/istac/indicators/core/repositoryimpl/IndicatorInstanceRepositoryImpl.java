package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.enume.domain.IndicatorsSystemProcStatusEnum;

/**
 * Repository implementation for IndicatorInstance
 */
@Repository("indicatorInstanceRepository")
public class IndicatorInstanceRepositoryImpl extends IndicatorInstanceRepositoryBase {

    public IndicatorInstanceRepositoryImpl() {
    }

    public IndicatorInstance findIndicatorInstance(String uuid) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uuid", uuid);
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
        parameters.put("code", code);
        parameters.put("procStatus", IndicatorsSystemProcStatusEnum.PUBLISHED);
        List<IndicatorInstance> result = findByQuery("from IndicatorInstance ii where ii.code = :code and ii.elementLevel.indicatorsSystemVersion.procStatus = :procStatus", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public Boolean existAnyIndicatorInstance(String indicatorsSystemUuid, String indicatorsSystemVersionNumber) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("indicatorsSystemUuid", indicatorsSystemUuid);
        parameters.put("indicatorsSystemVersionNumber", indicatorsSystemVersionNumber);
        List<IndicatorInstance> result = findByQuery(
                "from IndicatorInstance ii where ii.elementLevel.indicatorsSystemVersion.indicatorsSystem.uuid = :indicatorsSystemUuid and ii.elementLevel.indicatorsSystemVersion.versionNumber = :indicatorsSystemVersionNumber",
                parameters, 1);
        return result != null && !result.isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> findIndicatorsLinkedWithIndicatorsSystemVersion(Long indicatorsSystemVersionId) {
        Query query = getEntityManager().createQuery("select distinct(ii.indicator.uuid) from IndicatorInstance ii where ii.elementLevel.indicatorsSystemVersion.id = :id");
        query.setParameter("id", indicatorsSystemVersionId);
        List<String> result = query.getResultList();
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> findIndicatorsSystemsWithDiffusionVersionWithIndicator(String indicatorUuid) {
        Query query = getEntityManager().createQuery(
                "select distinct(ii.elementLevel.indicatorsSystemVersion.indicatorsSystem.uuid) " + "from IndicatorInstance ii " + "where ii.indicator.uuid = :uuid "
                        + "and ii.elementLevel.indicatorsSystemVersion.indicatorsSystem.diffusionVersion is not null "
                        + "and ii.elementLevel.indicatorsSystemVersion.versionNumber = ii.elementLevel.indicatorsSystemVersion.indicatorsSystem.diffusionVersion.versionNumber");
        query.setParameter("uuid", indicatorUuid);
        List<String> result = query.getResultList();
        return result;
    }
}
