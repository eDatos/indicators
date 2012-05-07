package es.gobcan.istac.indicators.web.client;

import org.siemac.metamac.sso.client.MetamacPrincipal;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtplatform.mvp.client.DelayedBindRegistry;

import es.gobcan.istac.indicators.web.client.gin.IndicatorsWebGinjector;
import es.gobcan.istac.indicators.web.shared.GetLoginPageUrlAction;
import es.gobcan.istac.indicators.web.shared.GetLoginPageUrlResult;
import es.gobcan.istac.indicators.web.shared.ValidateTicketAction;
import es.gobcan.istac.indicators.web.shared.ValidateTicketResult;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class IndicatorsWeb implements EntryPoint {

    private static MetamacPrincipal          principal;
    private static IndicatorsWebMessages     messages;
    private static IndicatorsWebCoreMessages coreMessages;
    private static IndicatorsWebConstants    constants;
    private IndicatorsWebGinjector           ginjector = GWT.create(IndicatorsWebGinjector.class);

    /*
     * public void onModuleLoad() {
     * String ticket = Window.Location.getParameter("ticket");
     * if (ticket != null) {
     * String url = Window.Location.createUrlBuilder().removeParameter("ticket").setHash(";ticket=" + ticket).buildString();
     * Window.Location.replace(url);
     * return;
     * }
     * ticket = Window.Location.getHash().replace("#;ticket=", "");
     * if (ticket == null || ticket.length() == 0) {
     * displayLoginView();
     * } else {
     * String serviceUrl = Window.Location.createUrlBuilder().buildString();
     * ginjector.getDispatcher().execute(new ValidateTicketAction(ticket, serviceUrl), new AsyncCallback<ValidateTicketResult>() {
     * @Override
     * public void onFailure(Throwable arg0) {
     * // TODO log
     * }
     * @Override
     * public void onSuccess(ValidateTicketResult result) {
     * IndicatorsWeb.principal = result.getMetamacPrincipal();
     * String url = Window.Location.createUrlBuilder().setHash("").buildString();
     * Window.Location.assign(url);
     * // This is required for GWT-Platform proxy's generator.
     * DelayedBindRegistry.bind(ginjector);
     * ginjector.getPlaceManager().revealCurrentPlace();
     * }
     * });
     * }
     * }
     */

    public void onModuleLoad() {
        // TODO: CHANGE WHEN ISTAC HAS A LDAP USER IN CIBER

        ginjector.getDispatcher().execute(new ValidateTicketAction(null, null), new AsyncCallback<ValidateTicketResult>() {

            @Override
            public void onFailure(Throwable arg0) {
                // TODO log
            }
            @Override
            public void onSuccess(ValidateTicketResult result) {
                IndicatorsWeb.principal = result.getMetamacPrincipal();

                // This is required for GWT-Platform proxy's generator.
                DelayedBindRegistry.bind(ginjector);
                ginjector.getPlaceManager().revealCurrentPlace();

            }
        });
    }

    public void displayLoginView() {
        String serviceUrl = Window.Location.createUrlBuilder().buildString();
        ginjector.getDispatcher().execute(new GetLoginPageUrlAction(serviceUrl), new AsyncCallback<GetLoginPageUrlResult>() {

            @Override
            public void onFailure(Throwable caught) {

            }
            @Override
            public void onSuccess(GetLoginPageUrlResult result) {
                Window.Location.replace(result.getLoginPageUrl());
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
        return principal;
    }
}
