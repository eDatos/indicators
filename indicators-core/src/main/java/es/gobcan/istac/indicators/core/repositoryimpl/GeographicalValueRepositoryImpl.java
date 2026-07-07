package es.gobcan.istac.indicators.core.repositoryimpl;

import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.CODE;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.CODES;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.UUID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.GeographicalValue;
import es.gobcan.istac.indicators.core.serviceimpl.util.ServiceUtils;
import es.gobcan.istac.indicators.core.util.ListBlockIterator;
import es.gobcan.istac.indicators.core.util.ListBlockIteratorFn;

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
        return new ListBlockIterator<String, GeographicalValue>(codes, ServiceUtils.SIZE_IN_MAX).iterate(new ListBlockIteratorFn<String, GeographicalValue>() {

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

    // Native SQL is intentional: TB_LIS_GEOGR_VALUES can contain hundreds of thousands of rows.
    // A JPA entity loop would load all of them into Hibernate's first-level cache and issue N individual UPDATEs.
    // JPQL bulk UPDATE cannot compute per-row values (prefix || CODE) in the SET clause on Hibernate 3.x.
    // This method is called only from the CodelistAvro Kafka consumer for granularity codelist (administrative, infrequent event).
    @Override
    public void updateGlobalOrderByGranularity(Long granularityId, Integer granularityOrder) {
        String globalOrderPrefix = String.format("%05d_", granularityOrder);
        String sql = "UPDATE TB_LIS_GEOGR_VALUES ";
        sql += "SET GLOBAL_ORDER = :prefix || CODE, ";
        sql += "VERSION = VERSION + 1 ";
        sql += "WHERE GRANULARITY_FK = :granularityId";

        Query query = getEntityManager().createNativeQuery(sql);
        query.setParameter("prefix", globalOrderPrefix);
        query.setParameter("granularityId", granularityId);
        query.executeUpdate();
    }
}