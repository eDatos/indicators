package es.gobcan.istac.indicators.web.server.handlers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.ElementLevelDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
import es.gobcan.istac.indicators.web.shared.MoveSystemStructureContentAction;
import es.gobcan.istac.indicators.web.shared.MoveSystemStructureContentResult;

@Component
public class MoveSystemStructureContentActionHandler extends AbstractActionHandler<MoveSystemStructureContentAction, MoveSystemStructureContentResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public MoveSystemStructureContentActionHandler() {
        super(MoveSystemStructureContentAction.class);
    }

    @Override
    public MoveSystemStructureContentResult execute(MoveSystemStructureContentAction action, ExecutionContext context) throws ActionException {
        try {
            ElementLevelDto level = action.getLevel();
            Long order = action.getTargetOrderInLevel();
            if (level.isElementTypeDimension()) {
                indicatorsServiceFacade.updateDimensionLocation(ServiceContextHelper.getServiceContext(), level.getDimension().getUuid(), action.getTargetDimensionUuid(), order++);
            } else if (level.isElementTypeIndicatorInstance()) {
                indicatorsServiceFacade.updateIndicatorInstanceLocation(ServiceContextHelper.getServiceContext(), level.getIndicatorInstance().getUuid(), action.getTargetDimensionUuid(), order++);
            }
            return new MoveSystemStructureContentResult();
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(MoveSystemStructureContentAction action, MoveSystemStructureContentResult result, ExecutionContext context) throws ActionException {

    }

}
