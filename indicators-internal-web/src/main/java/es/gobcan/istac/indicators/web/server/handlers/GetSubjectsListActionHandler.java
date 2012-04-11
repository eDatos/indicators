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

import es.gobcan.istac.indicators.core.dto.SubjectDto;
import es.gobcan.istac.indicators.core.serviceapi.IndicatorsServiceFacade;
import es.gobcan.istac.indicators.web.server.ServiceContextHelper;
import es.gobcan.istac.indicators.web.shared.GetSubjectsListAction;
import es.gobcan.istac.indicators.web.shared.GetSubjectsListResult;

@Component
public class GetSubjectsListActionHandler extends AbstractActionHandler<GetSubjectsListAction, GetSubjectsListResult> {

    @Autowired
    private IndicatorsServiceFacade indicatorsServiceFacade;

    public GetSubjectsListActionHandler() {
        super(GetSubjectsListAction.class);
    }

    @Override
    public GetSubjectsListResult execute(GetSubjectsListAction action, ExecutionContext context) throws ActionException {
        try {
            List<SubjectDto> subjectDtos = indicatorsServiceFacade.retrieveSubjects(ServiceContextHelper.getServiceContext());
            return new GetSubjectsListResult(subjectDtos);
        } catch (MetamacException e) {
            throw new MetamacWebException(WebExceptionUtils.getMetamacWebExceptionItem(e.getExceptionItems()));
        }
    }

    @Override
    public void undo(GetSubjectsListAction action, GetSubjectsListResult result, ExecutionContext context) throws ActionException {

    }

}
