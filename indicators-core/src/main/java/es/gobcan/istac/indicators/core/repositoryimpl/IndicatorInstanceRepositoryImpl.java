package es.gobcan.istac.indicators.core.repositoryimpl;

import org.springframework.stereotype.Repository;

import es.gobcan.istac.indicators.core.domain.IndicatorInstance;

/**
 * Repository implementation for IndicatorInstance
 */
@Repository("indicatorInstanceRepository")
public class IndicatorInstanceRepositoryImpl
    extends IndicatorInstanceRepositoryBase {
    public IndicatorInstanceRepositoryImpl() {
    }

    public IndicatorInstance findIndicatorInstance(String uuid) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "findIndicatorInstance not implemented");

    }
}
