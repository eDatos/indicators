package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.QuantityUnit;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.UUID;

/**
 * Repository implementation for QuantityUnit
 */
@Repository("quantityUnitRepository")
public class QuantityUnitRepositoryImpl extends QuantityUnitRepositoryBase {

    public QuantityUnitRepositoryImpl() {
    }

    @Override
    public QuantityUnit retrieveQuantityUnit(String uuid) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(UUID, uuid);
        List<QuantityUnit> result = findByQuery("from QuantityUnit qu where qu.uuid = :uuid", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}
