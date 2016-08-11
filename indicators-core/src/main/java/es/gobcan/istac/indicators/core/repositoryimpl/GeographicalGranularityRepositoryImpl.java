package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.GeographicalGranularity;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.CODE;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.UUID;

/**
 * Repository implementation for GeographicalGranularity
 */
@Repository("geographicalGranularityRepository")
public class GeographicalGranularityRepositoryImpl extends GeographicalGranularityRepositoryBase {

    public GeographicalGranularityRepositoryImpl() {
    }

    @Override
    public GeographicalGranularity retrieveGeographicalGranularity(String uuid) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(UUID, uuid);
        List<GeographicalGranularity> result = findByQuery("from GeographicalGranularity gg where gg.uuid = :uuid", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public GeographicalGranularity findGeographicalGranularityByCode(String code) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(CODE, code);
        List<GeographicalGranularity> result = findByQuery("from GeographicalGranularity gg where gg.code = :code", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}
