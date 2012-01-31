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
        List<IndicatorSystem> result = findByQuery("from IndicatorSystem i where i.uuid = :uuid", parameters);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    // TODO paginación
    @Override
    public List<IndicatorSystem> findIndicatorsSystems(String code) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("code", code);
        List<IndicatorSystem> result = findByQuery("from IndicatorSystem i where i.code = :code", parameters);
        return result;
    }
}
