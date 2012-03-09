package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.GeographicGranularity;

/**
 * Repository implementation for GeographicGranularity
 */
@Repository("geographicGranularityRepository")
public class GeographicGranularityRepositoryImpl
    extends GeographicGranularityRepositoryBase {
    public GeographicGranularityRepositoryImpl() {
    }

    public GeographicGranularity retrieveGeographicGranularity(String uuid) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uuid", uuid);
        List<GeographicGranularity> result = findByQuery("from GeographicGranularity gg where gg.uuid = :uuid", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}
