package es.gobcan.istac.indicators.web.server.handlers;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.DataStructureDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.rest.StatisticalResoucesRestExternalFacade;
import es.gobcan.istac.indicators.web.shared.GetDataStructureAction;
import es.gobcan.istac.indicators.web.shared.GetDataStructureResult;

@Component
public class GetDataStructureActionHandler extends SecurityActionHandler<GetDataStructureAction, GetDataStructureResult> {

    @Autowired
    private IndicatorsServiceFacade               indicatorsServiceFacade;

    @Autowired
    private StatisticalResoucesRestExternalFacade statisticalResoucesRestExternalFacade;

    public GetDataStructureActionHandler() {
        super(GetDataStructureAction.class);
    }

    @Override
    public GetDataStructureResult executeSecurityAction(GetDataStructureAction action) throws ActionException {
        try {
            DataStructureDto dataStructureDto = null;
            if (StringUtils.startsWithIgnoreCase(action.getUuid(), UrnUtils.URN_SIEMAC_CLASS_QUERY_PREFIX)) {
                dataStructureDto = statisticalResoucesRestExternalFacade.retrieveDataDefinitionFromQuery(ServiceContextHolder.getCurrentServiceContext(), action.getUuid());
            } else if (CommonWebUtils.isValidUrl(action.getUuid())) {
                dataStructureDto = indicatorsServiceFacade.retrieveJsonStatDataStructure(ServiceContextHolder.getCurrentServiceContext(), action.getUuid());
            } else {
                dataStructureDto = indicatorsServiceFacade.retrieveDataStructure(ServiceContextHolder.getCurrentServiceContext(), action.getUuid());
            }
            return new GetDataStructureResult(dataStructureDto);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(GetDataStructureAction action, GetDataStructureResult result, ExecutionContext context) throws ActionException {

    }

}
