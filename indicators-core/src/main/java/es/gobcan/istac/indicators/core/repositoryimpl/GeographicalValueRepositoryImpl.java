package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.gobcan.istac.indicators.core.serviceimpl.util.ServiceUtils;
import es.gobcan.istac.indicators.core.util.ListBlockIterator;
import es.gobcan.istac.indicators.core.util.ListBlockIteratorFn;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.GeographicalValue;

/**
 * Repository implementation for GeographicalValue
 */
@Repository("geographicalValueRepository")
public class GeographicalValueRepositoryImpl extends GeographicalValueRepositoryBase {

    public GeographicalValueRepositoryImpl() {
    }

    public GeographicalValue retrieveGeographicalValue(String uuid) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uuid", uuid);
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
        parameters.put("code", code);
        List<GeographicalValue> result = findByQuery("from GeographicalValue gv where gv.code = :code", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public List<GeographicalValue> findGeographicalValuesByCodes(List<String> codes) {
        List<GeographicalValue> geoValues = new ListBlockIterator<String, GeographicalValue>(codes, ServiceUtils.ORACLE_IN_MAX)
                .iterate(new ListBlockIteratorFn<String, GeographicalValue>() {
                    public List<GeographicalValue> apply(List<String> subcodes) {
                        Map<String, Object> parameters = new HashMap<String, Object>();
                        parameters.put("codes", subcodes);
                        return findByQuery("from GeographicalValue gv where gv.code in (:codes)", parameters);
                    }
                });
        return geoValues;
    }

    @Override
    public List<GeographicalValue> findGeographicalValuesByGranularity(String granularityCode) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("code", granularityCode);
        List<GeographicalValue> result = findByQuery("select gv from GeographicalValue gv inner join gv.granularity as gra where gra.code = :code", parameters);
        return result;
    }
}