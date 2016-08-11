package es.gobcan.istac.indicators.web.server.handlers;

import org.apache.commons.collections.CollectionUtils;
import org.siemac.metamac.core.common.criteria.MetamacCriteria;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPaginator;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaPropertyRestriction.OperationType;
import org.siemac.metamac.core.common.criteria.MetamacCriteriaResult;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.criteria.IndicatorsSystemCriteriaPropertyEnum;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemDto;
import es.gobcan.istac.indicators.core.dto.IndicatorsSystemSummaryDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.utils.DtoUtils;
import es.gobcan.istac.indicators.web.shared.SendIndicatorsSystemToProductionValidationAction;
import es.gobcan.istac.indicators.web.shared.SendIndicatorsSystemToProductionValidationResult;
import es.gobcan.istac.indicators.web.shared.dto.IndicatorsSystemDtoWeb;

@Component
public class SendIndicatorsSystemToProductionValidationActionHandler extends SecurityActionHandler<SendIndicatorsSystemToProductionValidationAction, SendIndicatorsSystemToProductionValidationResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public SendIndicatorsSystemToProductionValidationActionHandler() {
        super(SendIndicatorsSystemToProductionValidationAction.class);
    }

    @Override
    public SendIndicatorsSystemToProductionValidationResult executeSecurityAction(SendIndicatorsSystemToProductionValidationAction action) throws ActionException {
        IndicatorsSystemDtoWeb indicatorsSystemDtoWeb = action.getSystemToSend();

        MetamacCriteria criteria = new MetamacCriteria();
        criteria.setPaginator(new MetamacCriteriaPaginator());
        criteria.getPaginator().setMaximumResultSize(1);
        MetamacCriteriaPropertyRestriction restriction = new MetamacCriteriaPropertyRestriction(IndicatorsSystemCriteriaPropertyEnum.CODE.name(), indicatorsSystemDtoWeb.getCode(), OperationType.EQ);
        criteria.setRestriction(restriction);
        try {
            MetamacCriteriaResult<IndicatorsSystemSummaryDto> result = indicatorsServiceFacade.findIndicatorsSystems(ServiceContextHolder.getCurrentServiceContext(), criteria);
            if (!CollectionUtils.isEmpty(result.getResults())) {
                // If exists, send to production validation
                IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.sendIndicatorsSystemToProductionValidation(ServiceContextHolder.getCurrentServiceContext(), result.getResults()
                        .get(0).getUuid());
                return new SendIndicatorsSystemToProductionValidationResult(DtoUtils.updateIndicatorsSystemDtoWeb(indicatorsSystemDtoWeb, indicatorsSystemDto));
            } else {
                // If system does not exist, create and send to production validation
                IndicatorsSystemDto indicatorsSystemDto = indicatorsServiceFacade.createIndicatorsSystem(ServiceContextHolder.getCurrentServiceContext(), indicatorsSystemDtoWeb);
                indicatorsSystemDto = indicatorsServiceFacade.sendIndicatorsSystemToProductionValidation(ServiceContextHolder.getCurrentServiceContext(), indicatorsSystemDto.getUuid());
                return new SendIndicatorsSystemToProductionValidationResult(DtoUtils.updateIndicatorsSystemDtoWeb(indicatorsSystemDtoWeb, indicatorsSystemDto));
            }
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }

    }

    @Override
    public void undo(SendIndicatorsSystemToProductionValidationAction action, SendIndicatorsSystemToProductionValidationResult result, ExecutionContext context) throws ActionException {

    }

}
