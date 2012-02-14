package es.gobcan.istac.indicators.core.repositoryimpl;

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

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "findDataSource not implemented");

    }
}
