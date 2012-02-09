package es.gobcan.istac.indicators.core.repositoryimpl;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.Dimension;

/**
 * Repository implementation for Dimension
 */
@Repository("dimensionRepository")
public class DimensionRepositoryImpl extends DimensionRepositoryBase {
    public DimensionRepositoryImpl() {
    }

    public Dimension findDimensionByUuid(String uuid) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "findDimensionByUuid not implemented");

    }
}
