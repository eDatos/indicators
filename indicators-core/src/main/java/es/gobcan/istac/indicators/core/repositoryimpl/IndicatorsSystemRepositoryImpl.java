package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.IndicatorsSystem;

/**
 * Repository implementation for IndicatorsSystem
 */
@Repository("indicatorsSystemRepository")
public class IndicatorsSystemRepositoryImpl extends IndicatorsSystemRepositoryBase {
    public IndicatorsSystemRepositoryImpl() {
    }

    public IndicatorsSystem retrieveIndicatorsSystem(String uuid) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uuid", uuid);
        List<IndicatorsSystem> result = findByQuery("from IndicatorsSystem i where i.uuid = :uuid", parameters);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    // TODO paginación
    @Override
    public List<IndicatorsSystem> findIndicatorsSystems(String code) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        if (code != null) {
            parameters.put("code", code);
        }
        StringBuilder query = new StringBuilder("from IndicatorsSystem i ");
        StringBuilder queryWhere = new StringBuilder(" where ");
        if (code != null) {
            queryWhere.append(" upper(i.code) = upper(:code) ");
        }
        if (!queryWhere.toString().equals(" where ")) {
            query.append(queryWhere);
        }
        query.append("order by i.id asc");
        List<IndicatorsSystem> result = findByQuery(query.toString(), parameters);
        return result;        
    }
}
