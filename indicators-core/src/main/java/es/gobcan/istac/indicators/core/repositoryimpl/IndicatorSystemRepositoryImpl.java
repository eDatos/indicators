package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.IndicatorSystem;

/**
 * Repository implementation for IndicatorSystem
 */
@Repository("indicatorSystemRepository")
public class IndicatorSystemRepositoryImpl extends IndicatorSystemRepositoryBase {
    public IndicatorSystemRepositoryImpl() {
    }

    public IndicatorSystem retrieveIndicatorSystem(String uuid) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uuid", uuid);
        List<IndicatorSystem> result = findByQuery("from IndicatorSystem is where is.uuid = :uuid", parameters);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}
