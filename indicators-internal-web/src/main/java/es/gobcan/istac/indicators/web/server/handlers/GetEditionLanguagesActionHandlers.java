package es.gobcan.istac.indicators.web.server.handlers;

import java.util.List;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.AbstractActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import es.gobcan.istac.indicators.web.shared.GetEditionLanguagesAction;
import es.gobcan.istac.indicators.web.shared.GetEditionLanguagesResult;

@Component
public class GetEditionLanguagesActionHandlers extends AbstractActionHandler<GetEditionLanguagesAction, GetEditionLanguagesResult> {

    @Autowired
    private final ConfigurationService configurationService = null;

    public GetEditionLanguagesActionHandlers() {
        super(GetEditionLanguagesAction.class);
    }

    @Override
    public GetEditionLanguagesResult execute(GetEditionLanguagesAction action, ExecutionContext context) throws ActionException {
        try {
            List<String> languages = configurationService.retrieveLanguages();
            return new GetEditionLanguagesResult(languages);
        } catch (Exception e) {
            throw WebExceptionUtils.createMetamacWebException(new MetamacException(CommonServiceExceptionType.UNKNOWN));
        }
    }

    @Override
    public void undo(GetEditionLanguagesAction action, GetEditionLanguagesResult result, ExecutionContext context) throws ActionException {

    }

}
