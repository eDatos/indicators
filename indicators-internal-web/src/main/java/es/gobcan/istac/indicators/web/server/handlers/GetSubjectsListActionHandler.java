package es.gobcan.istac.indicators.web.server.handlers;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.core.dto.SubjectDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.shared.GetSubjectsListAction;
import es.gobcan.istac.indicators.web.shared.GetSubjectsListResult;

@Component
public class GetSubjectsListActionHandler extends SecurityActionHandler<GetSubjectsListAction, GetSubjectsListResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetSubjectsListActionHandler() {
        super(GetSubjectsListAction.class);
    }

    @Override
    public GetSubjectsListResult executeSecurityAction(GetSubjectsListAction action) throws ActionException {
        try {
            List<SubjectDto> subjectDtos = indicatorsServiceFacade.retrieveSubjects(ServiceContextHolder.getCurrentServiceContext());
            return new GetSubjectsListResult(subjectDtos);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

    @Override
    public void undo(GetSubjectsListAction action, GetSubjectsListResult result, ExecutionContext context) throws ActionException {

    }

}
