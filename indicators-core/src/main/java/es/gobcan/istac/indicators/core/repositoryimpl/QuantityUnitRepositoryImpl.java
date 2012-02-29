package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.QuantityUnit;

/**
 * Repository implementation for QuantityUnit
 */
@Repository("quantityUnitRepository")
public class QuantityUnitRepositoryImpl extends QuantityUnitRepositoryBase {

    public QuantityUnitRepositoryImpl() {
    }

    public QuantityUnit retrieveQuantityUnit(String uuid) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uuid", uuid);
        List<QuantityUnit> result = findByQuery("from QuantityUnit qu where qu.uuid = :uuid", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}
