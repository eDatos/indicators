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
}
