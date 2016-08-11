package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.serviceimpl.util.ServiceUtils;
import es.gobcan.istac.indicators.core.util.ListBlockIterator;
import es.gobcan.istac.indicators.core.util.ListBlockIteratorFn;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.CODE;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.CODES;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.UUID;

/**
 * Repository implementation for GeographicalValue
 */
@Repository("geographicalValueRepository")
public class GeographicalValueRepositoryImpl extends GeographicalValueRepositoryBase {

    public GeographicalValueRepositoryImpl() {
    }

    @Override
    public GeographicalValue retrieveGeographicalValue(String uuid) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(UUID, uuid);
        List<GeographicalValue> result = findByQuery("from GeographicalValue gv where gv.uuid = :uuid", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public GeographicalValue findGeographicalValueByCode(String code) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(CODE, code);
        List<GeographicalValue> result = findByQuery("from GeographicalValue gv where gv.code = :code", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public List<GeographicalValue> findGeographicalValuesByCodes(List<String> codes) {
        return new ListBlockIterator<String, GeographicalValue>(codes, ServiceUtils.ORACLE_IN_MAX).iterate(new ListBlockIteratorFn<String, GeographicalValue>() {

            @Override
            public List<GeographicalValue> apply(List<String> subcodes) {
                Map<String, Object> parameters = new HashMap<String, Object>();
                parameters.put(CODES, subcodes);
                return findByQuery("from GeographicalValue gv where gv.code in (:codes)", parameters);
            }
        });
    }

    @Override
    public List<GeographicalValue> findGeographicalValuesByGranularity(String granularityCode) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(CODE, granularityCode);
        return findByQuery("select gv from GeographicalValue gv inner join gv.granularity as gra where gra.code = :code", parameters);
    }
}