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
    public List<String> findDatasourceDataGpeUuidLinkedToIndicatorVersion(Long indicatorVersionId) {
        String querySql = "select distinct(ds.dataGpeUuid) " +
        		          "from DataSource ds " +
        		          "where ds.dataGpeUuid is not null " +
        		          "and ds.indicatorVersion.id = :id";
        Query query = getEntityManager().createQuery(querySql);
        query.setParameter("id", indicatorVersionId);
        List<String> results = query.getResultList();
        return results;
    }

    @Override
    public List<String> findIndicatorsLinkedWithIndicatorVersion(Long indicatorVersionId) {

        List<String> indicatorsUuidLinked = new ArrayList<String>();

        // Important! Queries must be executed separated (Following is executed incorrectly: 1) Join of annualRate and interperiodRate. 2) quantity with null values in numerator, denominator...)
        List<String> queriesSql = buildQueriesToFindIndicatorsLinkedWithIndicatorVersion();
        for (String querySql : queriesSql) {
            indicatorsUuidLinked.addAll(findIndicatorsLinkedCommon(indicatorVersionId, querySql));
        }
        return indicatorsUuidLinked;
    }
    
    private List<String> buildQueriesToFindIndicatorsLinkedWithIndicatorVersion() {
        String[] rateAttributes = new String[] {"annualPercentageRate", "interperiodPercentageRate"}; // Note: *puntualRate has not attributes with indicators
        String[] quantityIndicatorsAttributes = new String[] {"numerator", "denominator", "baseQuantity"};
        List<String> queriesSql = new ArrayList<String>();
        for (int i = 0; i < rateAttributes.length; i++) {
            String rateAttribute = rateAttributes[i];
            for (int j = 0; j < quantityIndicatorsAttributes.length; j++) {
                String quantityIndicatorsAttribute = quantityIndicatorsAttributes[j];
                
                StringBuffer querySql = new StringBuffer();
                querySql.append("select d.");
                querySql.append(rateAttribute);
                querySql.append(".quantity.");
                querySql.append(quantityIndicatorsAttribute);
                querySql.append(".uuid from DataSource d where d.indicatorVersion.id = :id");
                
                queriesSql.add(querySql.toString());
            }
        }        
        return queriesSql;
    }

    @SuppressWarnings("unchecked")
    private List<String> findIndicatorsLinkedCommon(Long indicatorVersionId, String querySql) {
        Query query = getEntityManager().createQuery(querySql);
        query.setParameter("id", indicatorVersionId);
        List<String> results = query.getResultList();
        return results;
    }
}
