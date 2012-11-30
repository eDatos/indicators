package es.gobcan.istac.indicators.rest.facadeimpl;

import es.gobcan.istac.indicators.core.domain.IndicatorInstance;
import es.gobcan.istac.indicators.core.domain.IndicatorsSystemVersion;
import es.gobcan.istac.indicators.rest.RestConstants;
import es.gobcan.istac.indicators.rest.exception.RestRuntimeException;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.rest.search.criteria.SculptorCriteria;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

public class PublishedIndicatorSystemRestFacadeImpl extends IndicatorSystemRestFacadeImpl {

    protected PagedResult<IndicatorsSystemVersion> findIndicatorsSystems(PagingParameter pagingParameter) throws MetamacException {

        return indicatorsSystemsService.findIndicatorsSystemsPublished(RestConstants.SERVICE_CONTEXT, null, pagingParameter);
    }


    protected IndicatorsSystemVersion retrieveIndicatorsSystemByCode(String idIndicatorSystem) throws MetamacException {
        return indicatorsSystemsService.retrieveIndicatorsSystemPublishedByCode(RestConstants.SERVICE_CONTEXT, idIndicatorSystem);
    }

    protected PagedResult<IndicatorInstance> findIndicatorsInstancesInIndicatorsSystems(SculptorCriteria sculptorCriteria) throws MetamacException {
        return indicatorsSystemsService.findIndicatorsInstancesInPublishedIndicatorsSystems(
                RestConstants.SERVICE_CONTEXT,
                sculptorCriteria.getConditions(),
                sculptorCriteria.getPagingParameter());
    }

    protected IndicatorInstance retrieveIndicatorInstanceByCode(final String idIndicatorSystem, final String idIndicatorInstance) throws MetamacException {
        IndicatorInstance indicatorInstance = indicatorsSystemsService.retrieveIndicatorInstancePublishedByCode(RestConstants.SERVICE_CONTEXT, idIndicatorInstance);
        IndicatorsSystemVersion indicatorsSystemVersion = retrieveIndicatorsSystemByCode(idIndicatorSystem);
        if (!indicatorInstance.getElementLevel().getIndicatorsSystemVersion().getIndicatorsSystem().getCode().equals(indicatorsSystemVersion.getIndicatorsSystem().getCode())) {
            String text = MessageFormat.format("IndicatorInstance: {0}, not in indicatorSystem: {1}", idIndicatorSystem, idIndicatorInstance);

            throw new RestRuntimeException(HttpStatus.NOT_FOUND, text);
        }
        return indicatorInstance;
    }



}
