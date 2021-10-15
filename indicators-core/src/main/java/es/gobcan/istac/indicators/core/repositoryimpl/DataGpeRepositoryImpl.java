package es.gobcan.istac.indicators.core.repositoryimpl;

import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.ID_OPERACION;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.LAST_UPDATE_DATE;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.NOW;
import static es.gobcan.istac.indicators.core.repositoryimpl.util.SqlQueryParameters.UUIDS;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.DataDefinition;
import es.gobcan.istac.indicators.core.serviceimpl.util.ServiceUtils;
import es.gobcan.istac.indicators.core.util.ListBlockIterator;
import es.gobcan.istac.indicators.core.util.ListBlockIteratorFn;

/**
 * Repository implementation for DataGpe
 */
@Repository("dataGpeRepository")
public class DataGpeRepositoryImpl extends DataGpeRepositoryBase {

    public DataGpeRepositoryImpl() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> findCurrentDataDefinitionsOperationsCodes() {
        Date now = Calendar.getInstance().getTime();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(NOW, now);

        // @formatter:off
        String queryHql = "select distinct df.idOperacion "+
                           "from DataDefinition df "+
                           "where df.availableEndDate is null "+
                           "and df.availableStartDate = ("+
                                                      " select max(df2.availableStartDate) " +
                                                      " from DataDefinition df2 " +
                                                      // It's not a NOT visible query
                                                      " where df2.availableStartDate <= :now " +
                                                      // It's not archived
                                                      " and df2.availableEndDate is NULL " +
                                                      " and df.uuid = df2.uuid) " +
                          "order by df.idOperacion";
        // @formatter:on
        Query query = getEntityManager().createQuery(queryHql);
        query.setParameter(NOW, now);
        return query.getResultList();
    }

    @Override
    public List<DataDefinition> findCurrentDataDefinitionsByOperationCode(String operationCode) {
        Date now = Calendar.getInstance().getTime();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(NOW, now);
        parameters.put(ID_OPERACION, operationCode);

        // @formatter:off
        String query = "from DataDefinition df " +
                        "where df.idOperacion = :idOperacion " +
                        "and df.availableEndDate is null " +
                        "and df.availableStartDate = (" +
                                                      " select max(df2.availableStartDate) " +
                                                      " from DataDefinition df2 " +
                                                      // It's not a NOT visible query
                                                      " where df2.availableStartDate <= :now " +
                                                      // It's not archived
                                                      " and df2.availableEndDate is NULL " +
                                                      " and df.uuid = df2.uuid)";
        // @formatter:on
        return findByQuery(query, parameters);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> findDataDefinitionsWithDataUpdatedAfter(Date date) {
        Date now = Calendar.getInstance().getTime();
        // @formatter:off
        String queryHql = "select df.uuid " +
        		       "from DataDefinition df " +
                       "where df.availableEndDate is null " +
                       "and df.dataUpdateDate > :lastUpdateDate " +
                       "and df.availableStartDate = (" +
                                                      " select max(df2.availableStartDate) " +
                                                      " from DataDefinition df2 " +
                                                      // It's not a NOT visible query
                                                      " where df2.availableStartDate <= :now " +
                                                      // It's not archived
                                                      " and df2.availableEndDate is NULL " +
                                                      " and df.uuid = df2.uuid)";
        // @formatter:on
        Query query = getEntityManager().createQuery(queryHql);
        query.setParameter(NOW, now);
        query.setParameter(LAST_UPDATE_DATE, date);
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> filterDataDefinitionsWithDataUpdatedAfter(List<String> dataDefinitionsUuids, final Date date) {
        final Date now = Calendar.getInstance().getTime();
        return new ListBlockIterator<String, String>(dataDefinitionsUuids, ServiceUtils.ORACLE_IN_MAX).iterate(new ListBlockIteratorFn<String, String>() {

            @Override
            public List<String> apply(List<String> subcodes) {
                // @formatter:off
                        String queryHql = "select df.uuid " +
                                "from DataDefinition df " +
                                "where df.uuid in (:uuids) " +
                                "and df.availableEndDate is null " +
                                "and df.dataUpdateDate > :lastUpdateDate " +
                                "and df.availableStartDate = (" +
                                " select max(df2.availableStartDate) " +
                                " from DataDefinition df2 " +
                                // It's not a NOT visible query
                                " where df2.availableStartDate <= :now " +
                                // It's not archived
                                " and df2.availableEndDate is NULL " +
                                " and df.uuid = df2.uuid)";
                        // @formatter:on
                Query query = getEntityManager().createQuery(queryHql);
                query.setParameter(NOW, now);
                query.setParameter(LAST_UPDATE_DATE, date);
                query.setParameter(UUIDS, subcodes);
                return query.getResultList();
            }
        });
    }

}
