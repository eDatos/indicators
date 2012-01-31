package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.IndicatorSystemVersion;

/**
 * Repository implementation for IndicatorSystemVersion
 */
@Repository("indicatorSystemVersionRepository")
public class IndicatorSystemVersionRepositoryImpl extends IndicatorSystemVersionRepositoryBase {

    public IndicatorSystemVersionRepositoryImpl() {
    }

    @Override
    public IndicatorSystemVersion retrieveIndicatorSystemVersion(String uuid, Long versionNumber) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uuid", uuid);
        parameters.put("versionNumber", versionNumber);
        List<IndicatorSystemVersion> result = findByQuery("from IndicatorSystemVersion isv where isv.indicatorSystem.uuid = :uuid and isv.versionNumber = :versionNumber", parameters);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public List<IndicatorSystemVersion> findIndicatorSystemVersions(String uri) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uri", uri);
        List<IndicatorSystemVersion> result = findByQuery("from IndicatorSystemVersion isv where isv.uri = :uri", parameters);
        return result;
    }
}
