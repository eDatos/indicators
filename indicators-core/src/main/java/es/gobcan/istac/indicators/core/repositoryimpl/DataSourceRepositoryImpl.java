package es.gobcan.istac.indicators.core.repositoryimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

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

    @Override
    public List<String> findIndicatorsLinkedWithIndicatorVersion(Long indicatorVersionId) {

        List<String> indicatorsUuidLinked = new ArrayList<String>();

        // Important! Queries must be executed separated (Following is executed incorrectly: 1) Join of annualRate and interperiodRate. 2) quantity with null values in numerator, denominator...)
        indicatorsUuidLinked.addAll(findIndicatorsLinkedCommon(indicatorVersionId, "select d.annualRate.quantity.numerator.uuid from DataSource d where d.indicatorVersion.id = :id"));
        indicatorsUuidLinked.addAll(findIndicatorsLinkedCommon(indicatorVersionId, "select d.annualRate.quantity.denominator.uuid from DataSource d where d.indicatorVersion.id = :id"));
        indicatorsUuidLinked.addAll(findIndicatorsLinkedCommon(indicatorVersionId, "select d.annualRate.quantity.baseQuantity.uuid from DataSource d where d.indicatorVersion.id = :id"));
        indicatorsUuidLinked.addAll(findIndicatorsLinkedCommon(indicatorVersionId, "select d.interperiodRate.quantity.numerator.uuid from DataSource d where d.indicatorVersion.id = :id"));
        indicatorsUuidLinked.addAll(findIndicatorsLinkedCommon(indicatorVersionId, "select d.interperiodRate.quantity.denominator.uuid from DataSource d where d.indicatorVersion.id = :id"));
        indicatorsUuidLinked.addAll(findIndicatorsLinkedCommon(indicatorVersionId, "select d.interperiodRate.quantity.baseQuantity.uuid from DataSource d where d.indicatorVersion.id = :id"));
        return indicatorsUuidLinked;
    }

    @SuppressWarnings("unchecked")
    private List<String> findIndicatorsLinkedCommon(Long indicatorVersionId, String querySql) {
        Query query = getEntityManager().createQuery(querySql);
        query.setParameter("id", indicatorVersionId);
        List<String> results = query.getResultList();
        return results;
    }
}
