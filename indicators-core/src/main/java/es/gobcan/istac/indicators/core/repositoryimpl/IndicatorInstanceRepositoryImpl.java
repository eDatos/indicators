package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.IndicatorInstance;

/**
 * Repository implementation for IndicatorInstance
 */
@Repository("indicatorInstanceRepository")
public class IndicatorInstanceRepositoryImpl extends IndicatorInstanceRepositoryBase {

    public IndicatorInstanceRepositoryImpl() {
    }

    public IndicatorInstance findIndicatorInstance(String uuid) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uuid", uuid);
        List<IndicatorInstance> result = findByQuery("from IndicatorInstance ii where ii.uuid = :uuid", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }
}
