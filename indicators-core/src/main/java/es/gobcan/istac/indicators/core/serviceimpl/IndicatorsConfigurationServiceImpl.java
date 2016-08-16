package es.gobcan.istac.indicators.core.serviceimpl;

import java.util.Date;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.springframework.stereotype.Service;

import es.gobcan.istac.indicators.core.domain.Configuration;
import es.gobcan.istac.indicators.core.error.ServiceExceptionType;

/**
 * Implementation of IndicatorsConfigurationService.
 */
@Service("indicatorsConfigurationService")
public class IndicatorsConfigurationServiceImpl extends IndicatorsConfigurationServiceImplBase {

    public IndicatorsConfigurationServiceImpl() {
    }

    public Date retrieveLastSuccessfulGpeQueryDate(ServiceContext ctx) throws MetamacException {
        List<Configuration> configurations = getConfigurationRepository().findAll();
        if (configurations != null && configurations.size() > 0) {
            return configurations.get(0).getLastSuccessfulGpeQueryDate();
        } else {
            throw new MetamacException(ServiceExceptionType.CONFIGURATION_NOT_FOUND);
        }
    }
    
    @Override
    public void setLastSuccessfulGpeQueryDate(ServiceContext ctx, Date date) throws MetamacException {
        List<Configuration> configurations = getConfigurationRepository().findAll();
        if (configurations != null && configurations.size() > 0) {
            Configuration configuration = configurations.get(0);
            configuration.setLastSuccessfulGpeQueryDate(date);
            getConfigurationRepository().save(configuration);
        } else {
            throw new MetamacException(ServiceExceptionType.CONFIGURATION_NOT_FOUND);
        }
    }
}
