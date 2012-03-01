package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.DataSource;

/**
 * Repository implementation for DataSource
 */
@Repository("dataSourceRepository")
public class DataSourceRepositoryImpl extends DataSourceRepositoryBase {

    public DataSourceRepositoryImpl() {
    }

    public DataSource findDataSource(String uuid) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("uuid", uuid);
        List<DataSource> result = findByQuery("from DataSource d where d.uuid = :uuid", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

//    @Override
//    public Set<String> findIndicatorsLinkedWithDatasourcesOfAnIndicatorVersion(Long indicatorVersionId) {
//        
//        Set<String> indicatorsUuidLinked = new HashSet<String>();
//        
//        // Important! Queries must be executed separated (we can execute an unique query with numerator, denominator... at time)
//        findIndicatorsLinkedInColumn(indicatorVersionId, "select d.annualRate.quantity.numerator.uuid from DataSource d where d.indicatorVersion.id = :id", indicatorsUuidLinked);
//        findIndicatorsLinkedInColumn(indicatorVersionId, "select d.annualRate.quantity.denominator.uuid from DataSource d where d.indicatorVersion.id = :id", indicatorsUuidLinked);
//        findIndicatorsLinkedInColumn(indicatorVersionId, "select d.annualRate.quantity.baseQuantity.uuid from DataSource d where d.indicatorVersion.id = :id", indicatorsUuidLinked);
//        findIndicatorsLinkedInColumn(indicatorVersionId, "select d.interperiodRate.quantity.numerator.uuid from DataSource d where d.indicatorVersion.id = :id", indicatorsUuidLinked);
//        findIndicatorsLinkedInColumn(indicatorVersionId, "select d.interperiodRate.quantity.denominator.uuid from DataSource d where d.indicatorVersion.id = :id", indicatorsUuidLinked);
//        findIndicatorsLinkedInColumn(indicatorVersionId, "select d.interperiodRate.quantity.baseQuantity.uuid from DataSource d where d.indicatorVersion.id = :id", indicatorsUuidLinked);
//        
//        return indicatorsUuidLinked;
//    }
//
//    @SuppressWarnings("unchecked")
//    private void findIndicatorsLinkedInColumn(Long indicatorVersionId, String querySql, Set<String> indicatorsUuidLinked) {
//        Query query = getEntityManager().createQuery(querySql);
//        query.setParameter("id", indicatorVersionId);
//        List<String> result = query.getResultList();
//        for (String indicatorUuidLinked : result) {
//            indicatorsUuidLinked.add(indicatorUuidLinked);
//        }
//    }
}
