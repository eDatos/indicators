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

import es.gobcan.istac.indicators.core.dto.serviceapi.ElementLevelDto;
import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
import es.gobcan.istac.indicators.web.server.services.IndicatorsServiceWrapper;
import es.gobcan.istac.indicators.web.shared.MoveSystemStructureContentAction;
import es.gobcan.istac.indicators.web.shared.MoveSystemStructureContentResult;

@Component
public class MoveSystemStructureContentHandler extends AbstractActionHandler<MoveSystemStructureContentAction, MoveSystemStructureContentResult> {

    @Autowired
    private IndicatorsServiceWrapper service;
    
    public MoveSystemStructureContentHandler() {
        super(MoveSystemStructureContentAction.class);
    }

    @Override
    public MoveSystemStructureContentResult execute(MoveSystemStructureContentAction action, ExecutionContext context) throws ActionException {
        try {
            List<ElementLevelDto> levels = action.getLevels();
            Long order = action.getTargetOrderInLevel();
            for (ElementLevelDto level : levels) {
                if (level.isDimension()) {
                    service.updateDimensionLocation(ServiceContextHelper.getServiceContext(), level.getDimension().getUuid(), action.getSystemUuid() , action.getTargetDimensionUuid(), order++);
                } else if (level.isIndicatorInstance()) {
                    service.updateIndicatorInstanceLocation(ServiceContextHelper.getServiceContext(), level.getIndicatorInstance().getUuid(), action.getSystemUuid() , action.getTargetDimensionUuid(), order++);
                }
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
