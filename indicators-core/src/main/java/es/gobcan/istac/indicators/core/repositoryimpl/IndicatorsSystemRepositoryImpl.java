package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.UUID;

/**
 * Repository implementation for IndicatorsSystem
 */
@Repository("indicatorsSystemRepository")
public class IndicatorsSystemRepositoryImpl extends IndicatorsSystemRepositoryBase {

    public IndicatorsSystemRepositoryImpl() {
    }

    @Override
    public IndicatorsSystem retrieveIndicatorsSystem(String uuid) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(UUID, uuid);
        List<IndicatorsSystem> result = findByQuery("from IndicatorsSystem i where i.uuid = :uuid", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}
