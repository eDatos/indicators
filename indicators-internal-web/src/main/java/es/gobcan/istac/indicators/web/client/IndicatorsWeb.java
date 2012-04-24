package es.gobcan.istac.indicators.web.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.sso.client.MetamacPrincipal;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.gwtplatform.mvp.client.DelayedBindRegistry;

import es.gobcan.istac.indicators.web.client.gin.IndicatorsWebGinjector;
import es.gobcan.istac.indicators.web.client.widgets.WaitingAsyncCallback;
import es.gobcan.istac.indicators.web.shared.GetUserPrincipalAction;
import es.gobcan.istac.indicators.web.shared.GetUserPrincipalResult;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class IndicatorsWeb implements EntryPoint {

    private Logger                           logger    = Logger.getLogger(IndicatorsWeb.class.getName());

    private static IndicatorsWebMessages     messages;
    private static IndicatorsWebCoreMessages coreMessages;
    private static IndicatorsWebConstants    constants;
    private IndicatorsWebGinjector           ginjector = GWT.create(IndicatorsWebGinjector.class);
    private static MetamacPrincipal          userPrincipal;

    @Override
    public void onModuleLoad() {
        ginjector.getDispatchAsync().execute(new GetUserPrincipalAction(), new WaitingAsyncCallback<GetUserPrincipalResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error getting logged user");
            }
            @Override
            public void onWaitSuccess(GetUserPrincipalResult result) {
                userPrincipal = result.getUserPrincipal();
                DelayedBindRegistry.bind(ginjector);
                ginjector.getPlaceManager().revealCurrentPlace();
            }
        });
    }

    public static IndicatorsWebCoreMessages getCoreMessages() {
        if (coreMessages == null) {
            coreMessages = GWT.create(IndicatorsWebCoreMessages.class);
        }
        return coreMessages;
    }

    public static IndicatorsWebMessages getMessages() {
        if (messages == null) {
            messages = GWT.create(IndicatorsWebMessages.class);
        }
        return messages;
    }

    public static IndicatorsWebConstants getConstants() {
        if (constants == null) {
            constants = GWT.create(IndicatorsWebConstants.class);
        }
        return constants;
    }

    public static MetamacPrincipal getUserPrincipal() {
        return userPrincipal;
    }
}
