package es.gobcan.istac.indicators.web.server.handlers;

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

    private static String        PROP_EDITION_LANGUAGES = "indicators.internal.edition.languages";

    @Autowired
    private ConfigurationService configurationService   = null;

    public GetEditionLanguagesActionHandlers() {
        super(GetEditionLanguagesAction.class);
    }

    @Override
    public GetEditionLanguagesResult execute(GetEditionLanguagesAction action, ExecutionContext context) throws ActionException {
        try {
            String[] languages = configurationService.getConfig().getStringArray(PROP_EDITION_LANGUAGES);
            return new GetEditionLanguagesResult(languages);
        } catch (Exception e) {
            throw WebExceptionUtils.createMetamacWebException(new MetamacException(CommonServiceExceptionType.UNKNOWN));
        }
    }

    @Override
    public void undo(GetEditionLanguagesAction action, GetEditionLanguagesResult result, ExecutionContext context) throws ActionException {

    }

}
