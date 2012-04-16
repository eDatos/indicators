package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.GeographicalValue;

/**
 * Repository implementation for GeographicalValue
 */
@Repository("geographicalValueRepository")
public class GeographicalValueRepositoryImpl extends GeographicalValueRepositoryBase {

    public GeographicalValueRepositoryImpl() {
    }

    public GeographicalValue retrieveGeographicalValue(String uuid) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uuid", uuid);
        List<GeographicalValue> result = findByQuery("from GeographicalValue gv where gv.uuid = :uuid", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}