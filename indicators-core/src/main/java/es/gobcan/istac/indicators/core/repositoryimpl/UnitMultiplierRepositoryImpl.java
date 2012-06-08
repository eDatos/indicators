package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.UnitMultiplier;

/**
 * Repository implementation for UnitMultiplier
 */
@Repository("unitMultiplierRepository")
public class UnitMultiplierRepositoryImpl extends UnitMultiplierRepositoryBase {

    public UnitMultiplierRepositoryImpl() {
    }

    public UnitMultiplier retrieveUnitMultiplier(Integer unitMultiplier) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("unitMultiplier", unitMultiplier);
        List<UnitMultiplier> result = findByQuery("from UnitMultiplier u where u.unitMultiplier = :unitMultiplier", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}
