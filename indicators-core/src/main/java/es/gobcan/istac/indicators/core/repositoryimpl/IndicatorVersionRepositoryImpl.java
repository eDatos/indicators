package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.IndicatorVersion;
import es.gobcan.istac.indicators.core.domain.IndicatorVersion;

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
}
