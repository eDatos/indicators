package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.GeographicValue;

/**
 * Repository implementation for GeographicValue
 */
@Repository("geographicValueRepository")
public class GeographicValueRepositoryImpl extends GeographicValueRepositoryBase {

    public GeographicValueRepositoryImpl() {
    }

    public GeographicValue retrieveGeographicValue(String uuid) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uuid", uuid);
        List<GeographicValue> result = findByQuery("from GeographicValue gv where gv.uuid = :uuid", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}
