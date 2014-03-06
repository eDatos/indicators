package es.gobcan.istac.indicators.web.client;

import java.util.logging.Logger;

import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.web.common.client.MetamacSecurityEntryPoint;
import org.siemac.metamac.web.common.client.gin.MetamacWebGinjector;

import com.google.gwt.core.client.GWT;

import es.gobcan.istac.indicators.core.constants.IndicatorsConstants;
import es.gobcan.istac.indicators.web.client.gin.IndicatorsWebGinjector;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class IndicatorsWeb extends MetamacSecurityEntryPoint {

    private static final boolean             SECURITY_ENABLED = true;
    private static Logger                    logger           = Logger.getLogger(IndicatorsWeb.class.getName());

    private static MetamacPrincipal          principal;
    private static IndicatorsWebMessages     messages;
    private static IndicatorsWebCoreMessages coreMessages;
    private static IndicatorsWebConstants    constants;

    private final IndicatorsWebGinjector     ginjector        = GWT.create(IndicatorsWebGinjector.class);

    @Override
    public void onModuleLoad() {
        setUncaughtExceptionHandler();

        prepareApplication(SECURITY_ENABLED);
    }

    @Override
    protected void setPrincipal(MetamacPrincipal principal) {
        IndicatorsWeb.principal = principal;
    }

    public static MetamacPrincipal getCurrentUser() {
        return IndicatorsWeb.principal;
    }

    @Override
    protected MetamacPrincipal getPrincipal() {
        return IndicatorsWeb.principal;
    }

    @Override
    protected String getApplicationTitle() {
        return getConstants().appTitle();
    }

    @Override
    protected String getBundleName() {
        return "messages-indicators-internal-web";
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

    @Override
    protected MetamacWebGinjector getWebGinjector() {
        return ginjector;
    }

    @Override
    protected String getSecurityApplicationId() {
        return IndicatorsConstants.SECURITY_APPLICATION_ID;
    }

}
