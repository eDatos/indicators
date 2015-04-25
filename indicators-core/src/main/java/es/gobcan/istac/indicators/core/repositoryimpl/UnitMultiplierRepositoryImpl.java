package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.UnitMultiplier;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.UNIT_MULTIPLIER;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.UUID;

/**
 * Repository implementation for UnitMultiplier
 */
@Repository("unitMultiplierRepository")
public class UnitMultiplierRepositoryImpl extends UnitMultiplierRepositoryBase {

    public UnitMultiplierRepositoryImpl() {
    }

    @Override
    public UnitMultiplier retrieveUnitMultiplier(Integer unitMultiplier) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(UNIT_MULTIPLIER, unitMultiplier);
        List<UnitMultiplier> result = findByQuery("from UnitMultiplier u where u.unitMultiplier = :unitMultiplier", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public UnitMultiplier retrieveUnitMultiplier(String unitMultiplierUuid) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(UUID, unitMultiplierUuid);
        List<UnitMultiplier> result = findByQuery("from UnitMultiplier u where u.uuid = :uuid", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public List<UnitMultiplier> findAllOrdered() {
        return findByQuery("from UnitMultiplier order by unitMultiplier", null);
    }
}
