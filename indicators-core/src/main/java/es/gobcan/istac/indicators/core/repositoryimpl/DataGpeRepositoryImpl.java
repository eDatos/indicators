package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import es.gobcan.istac.indicators.core.domain.Translation;
import es.gobcan.istac.indicators.core.serviceimpl.util.ServiceUtils;
import es.gobcan.istac.indicators.core.util.ListBlockIterator;
import es.gobcan.istac.indicators.core.util.ListBlockIteratorFn;
import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.DataDefinition;

/**
 * Repository implementation for DataGpe
 */
@Repository("dataGpeRepository")
public class DataGpeRepositoryImpl extends DataGpeRepositoryBase {
    public DataGpeRepositoryImpl() {
    }

    @Override
    public List<String> findCurrentDataDefinitionsOperationsCodes() {
        Date now = Calendar.getInstance().getTime();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("now", now);
        String queryHql = "select distinct df.idOperacion "+
                           "from DataDefinition df "+
                           "where df.availableEndDate is null "+
                           "and df.availableStartDate = ("+
                                                      " select max(df2.availableStartDate) " +
                                                      " from DataDefinition df2 "+
                                                      " where df2.availableStartDate <= :now "+ //Its not a NOT visible query
                                                      " and df2.availableEndDate is NULL "+ //It's not archived
                                                      " and df.uuid = df2.uuid) " +
                          "order by df.idOperacion";
        Query query = getEntityManager().createQuery(queryHql);
        query.setParameter("now", now);
        List<String> result = query.getResultList();
        return result;
    }

    @Override
    public List<DataDefinition> findCurrentDataDefinitions() {
        Date now = Calendar.getInstance().getTime();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("now", now);
        String query = "from DataDefinition df "+
                       "where df.availableEndDate is null "+
                       "and df.availableStartDate = ("+
                                                      " select max(df2.availableStartDate) " +
                                                      " from DataDefinition df2 "+
                                                      " where df2.availableStartDate <= :now "+ //Its not a NOT visible query
                                                      " and df2.availableEndDate is NULL "+ //It's not archived
                                                      " and df.uuid = df2.uuid)";
        List<DataDefinition> result = findByQuery(query,parameters);
        return result;
    }

    @Override
    public List<DataDefinition> findCurrentDataDefinitionsByOperationCode(String operationCode) {
        Date now = Calendar.getInstance().getTime();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("now", now);
        parameters.put("idOperacion", operationCode);
        String query = "from DataDefinition df "+
                        "where df.idOperacion = :idOperacion "+
                        "and df.availableEndDate is null "+
                        "and df.availableStartDate = ("+
                                                      " select max(df2.availableStartDate) " +
                                                      " from DataDefinition df2 "+
                                                      " where df2.availableStartDate <= :now "+ //Its not a NOT visible query
                                                      " and df2.availableEndDate is NULL "+ //It's not archived
                                                      " and df.uuid = df2.uuid)";
        List<DataDefinition> result = findByQuery(query,parameters);
        return result;
    }


    @Override
    public DataDefinition findCurrentDataDefinition(String uuid) {
        Date now = Calendar.getInstance().getTime();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("now", now);
        parameters.put("uuid", uuid);
        String query = "from DataDefinition df "+
                       "where df.uuid = :uuid "+
                       "and df.availableEndDate is null "+
                       "and df.availableStartDate = ("+
                                                      " select max(df2.availableStartDate) " +
                                                      " from DataDefinition df2 "+
                                                      " where df2.availableStartDate <= :now "+ //Its not a NOT visible query
                                                      " and df2.availableEndDate is NULL "+ //It's not archived
                                                      " and df.uuid = df2.uuid)";
        List<DataDefinition> result = findByQuery(query,parameters);
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<String> findDataDefinitionsWithDataUpdatedAfter(Date date) {
        Date now = Calendar.getInstance().getTime();
        String queryHql = "select df.uuid " +
        		       "from DataDefinition df "+
                       "where df.availableEndDate is null "+
                       "and df.dataUpdateDate > :lastUpdateDate "+
                       "and df.availableStartDate = ("+
                                                      " select max(df2.availableStartDate) " +
                                                      " from DataDefinition df2 "+
                                                      " where df2.availableStartDate <= :now "+ //Its not a NOT visible query
                                                      " and df2.availableEndDate is NULL "+ //It's not archived
                                                      " and df.uuid = df2.uuid)";
        Query query = getEntityManager().createQuery(queryHql);
        query.setParameter("now", now);
        query.setParameter("lastUpdateDate", date);
        List<String> result = query.getResultList();
        return result;
    }

    @Override
    public List<String> filterDataDefinitionsWithDataUpdatedAfter(List<String> dataDefinitionsUuids, final Date date) {
        final Date now = Calendar.getInstance().getTime();
        List<String> result = new ListBlockIterator<String, String>(dataDefinitionsUuids, ServiceUtils.ORACLE_IN_MAX)
                .iterate(new ListBlockIteratorFn<String, String>() {
                    public List<String> apply(List<String> subcodes) {
                        String queryHql = "select df.uuid " +
                                "from DataDefinition df "+
                                "where df.uuid in (:uuids) " +
                                "and df.availableEndDate is null "+
                                "and df.dataUpdateDate > :lastUpdateDate "+
                                "and df.availableStartDate = ("+
                                " select max(df2.availableStartDate) " +
                                " from DataDefinition df2 "+
                                " where df2.availableStartDate <= :now "+ //Its not a NOT visible query
                                " and df2.availableEndDate is NULL "+ //It's not archived
                                " and df.uuid = df2.uuid)";
                        Query query = getEntityManager().createQuery(queryHql);
                        query.setParameter("now", now);
                        query.setParameter("lastUpdateDate", date);
                        query.setParameter("uuids", subcodes);
                        return query.getResultList();
                    }
                });
        return result;
    }

}
