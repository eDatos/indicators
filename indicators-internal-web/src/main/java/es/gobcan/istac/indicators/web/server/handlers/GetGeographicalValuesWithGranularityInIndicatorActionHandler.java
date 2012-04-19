package es.gobcan.istac.indicators.web.server.handlers;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.GeographicalValueDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesWithGranularityInIndicatorAction;
import es.gobcan.istac.indicators.web.shared.GetGeographicalValuesWithGranularityInIndicatorResult;

@Component
public class GetGeographicalValuesWithGranularityInIndicatorActionHandler
        extends
            AbstractActionHandler<GetGeographicalValuesWithGranularityInIndicatorAction, GetGeographicalValuesWithGranularityInIndicatorResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetGeographicalValuesWithGranularityInIndicatorActionHandler() {
        super(GetGeographicalValuesWithGranularityInIndicatorAction.class);
    }

    @Override
    public GetGeographicalValuesWithGranularityInIndicatorResult execute(GetGeographicalValuesWithGranularityInIndicatorAction action, ExecutionContext context) throws ActionException {
        try {
            List<GeographicalValueDto> geographicalValueDtos = indicatorsServiceFacade.retrieveGeographicalValuesWithGranularityInIndicator(ServiceContextHelper.getServiceContext(),
                    action.getIndicatorUuid(), action.getIndicatorVersion(), action.getGeographicalGranularityUuid());
            return new GetGeographicalValuesWithGranularityInIndicatorResult(geographicalValueDtos);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(GetGeographicalValuesWithGranularityInIndicatorAction action, GetGeographicalValuesWithGranularityInIndicatorResult result, ExecutionContext context) throws ActionException {

    }

}
