package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.Dimension;

/**
 * Repository implementation for Dimension
 */
@Repository("dimensionRepository")
public class DimensionRepositoryImpl extends DimensionRepositoryBase {
    public DimensionRepositoryImpl() {
    }

    public Dimension findDimension(String uuid) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uuid", uuid);
        List<Dimension> result = findByQuery("from Dimension d where d.uuid = :uuid", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}
