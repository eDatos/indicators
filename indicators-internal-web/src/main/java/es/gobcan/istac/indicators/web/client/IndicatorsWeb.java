package es.gobcan.istac.indicators.web.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.web.common.client.MetamacEntryPoint;
import org.siemac.metamac.web.common.client.enums.ApplicationOrganisationEnum;
import org.siemac.metamac.web.common.client.events.LoginAuthenticatedEvent;
import org.siemac.metamac.web.common.client.utils.ApplicationEditionLanguages;
import org.siemac.metamac.web.common.client.utils.ApplicationOrganisation;
import org.siemac.metamac.web.common.client.widgets.MetamacNavBar;
import org.siemac.metamac.web.common.client.widgets.WaitingAsyncCallback;
import org.siemac.metamac.web.common.shared.GetNavigationBarUrlAction;
import org.siemac.metamac.web.common.shared.GetNavigationBarUrlResult;
import org.siemac.metamac.web.common.shared.MockCASUserAction;
import org.siemac.metamac.web.common.shared.MockCASUserResult;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.gwtplatform.mvp.client.DelayedBindRegistry;

import es.gobcan.istac.indicators.web.client.gin.IndicatorsWebGinjector;
import es.gobcan.istac.indicators.web.shared.GetEditionLanguagesAction;
import es.gobcan.istac.indicators.web.shared.GetEditionLanguagesResult;
import es.gobcan.istac.indicators.web.shared.GetLoginPageUrlAction;
import es.gobcan.istac.indicators.web.shared.GetLoginPageUrlResult;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class IndicatorsWeb extends MetamacEntryPoint {

    private static Logger                    logger    = Logger.getLogger(IndicatorsWeb.class.getName());

    private static MetamacPrincipal          principal;
    private static IndicatorsWebMessages     messages;
    private static IndicatorsWebCoreMessages coreMessages;
    private static IndicatorsWebConstants    constants;

    private IndicatorsWebGinjector           ginjector = GWT.create(IndicatorsWebGinjector.class);

    public void onModuleLoad() {
        ginjector.getDispatcher().execute(new GetNavigationBarUrlAction(), new WaitingAsyncCallback<GetNavigationBarUrlResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error loading toolbar");
                loadNonSecuredApplication();
            }
            public void onWaitSuccess(GetNavigationBarUrlResult result) {
                // Load scripts for navigation bar
                MetamacNavBar.loadScripts(result.getNavigationBarUrl());
                loadNonSecuredApplication();
            };
        });
    }

    // TODO This method should be removed to use CAS authentication
    // Application id should be the same than the one defined in org.siemac.metamac.access.control.constants.AccessControlConstants.SECURITY_APPLICATION_ID
    private void loadNonSecuredApplication() {
        ginjector.getDispatcher().execute(new MockCASUserAction("GESTOR_ACCESOS"), new WaitingAsyncCallback<MockCASUserResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error mocking CAS user");
            }
            @Override
            public void onWaitSuccess(MockCASUserResult result) {
                IndicatorsWeb.principal = result.getMetamacPrincipal();
                // Load edition languages
                ginjector.getDispatcher().execute(new GetEditionLanguagesAction(), new WaitingAsyncCallback<GetEditionLanguagesResult>() {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        logger.log(Level.SEVERE, "Error loading edition languages");
                        // If an error occurs while loading edition languages, enable SPANISH, ENGLISH and PORTUGUESE by default
                        ApplicationEditionLanguages.setEditionLanguages(new String[]{ApplicationEditionLanguages.SPANISH, ApplicationEditionLanguages.ENGLISH, ApplicationEditionLanguages.PORTUGUESE});
                        loadApplication();
                    }
                    @Override
                    public void onWaitSuccess(GetEditionLanguagesResult result) {
                        ApplicationEditionLanguages.setEditionLanguages(result.getLanguages());
                        ApplicationOrganisation.setCurrentOrganisation(ApplicationOrganisationEnum.ISTAC.name());
                        loadApplication();
                    }
                });
            }
        });
    }

    // private void loadSecuredApplication() {
    // String ticketParam = Window.Location.getParameter(TICKET);
    // if (ticketParam != null) {
    // UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
    // urlBuilder.removeParameter(TICKET);
    // urlBuilder.setHash(Window.Location.getHash() + TICKET_HASH + ticketParam);
    // String url = urlBuilder.buildString();
    // Window.Location.replace(url);
    // return;
    // }
    // String hash = Window.Location.getHash();
    // String ticketHash = null;
    // if (hash.contains(TICKET_HASH)) {
    // ticketHash = hash.substring(hash.indexOf(TICKET_HASH) + TICKET_HASH.length(), hash.length());
    // }
    //
    // if (ticketHash == null || ticketHash.length() == 0) {
    // displayLoginView();
    // } else {
    // String serviceUrl = Window.Location.createUrlBuilder().buildString();
    // ginjector.getDispatcher().execute(new ValidateTicketAction(ticketHash, serviceUrl), new WaitingAsyncCallback<ValidateTicketResult>() {
    //
    // @Override
    // public void onWaitFailure(Throwable arg0) {
    // logger.log(Level.SEVERE, "Error validating ticket");
    // }
    // @Override
    // public void onWaitSuccess(ValidateTicketResult result) {
    // IndicatorsWeb.principal = result.getMetamacPrincipal();
    //
    // String url = Window.Location.createUrlBuilder().setHash("").buildString();
    // Window.Location.assign(url);
    //
    // // Load edition languages
    // ginjector.getDispatcher().execute(new GetEditionLanguagesAction(), new WaitingAsyncCallback<GetEditionLanguagesResult>() {
    //
    // @Override
    // public void onWaitFailure(Throwable caught) {
    // logger.log(Level.SEVERE, "Error loading edition languages");
    // // If an error occurs while loading edition languages, enable SPANISH, ENGLISH and PORTUGUESE by default
    // ApplicationEditionLanguages.setEditionLanguages(new String[]{ApplicationEditionLanguages.SPANISH, ApplicationEditionLanguages.ENGLISH,
    // ApplicationEditionLanguages.PORTUGUESE});
    // loadApplication();
    // }
    // @Override
    // public void onWaitSuccess(GetEditionLanguagesResult result) {
    // ApplicationEditionLanguages.setEditionLanguages(result.getLanguages());
    // ApplicationOrganisation.setCurrentOrganisation(ApplicationOrganisationEnum.ISTAC.name());
    // loadApplication();
    // }
    // });
    // }
    // });
    // }
    // }

    private void loadApplication() {
        LoginAuthenticatedEvent.fire(ginjector.getEventBus(), IndicatorsWeb.principal);
        // This is required for GWT-Platform proxy's generator.
        DelayedBindRegistry.bind(ginjector);
        ginjector.getPlaceManager().revealCurrentPlace();
    }

    public void displayLoginView() {
        String serviceUrl = Window.Location.createUrlBuilder().buildString();
        ginjector.getDispatcher().execute(new GetLoginPageUrlAction(serviceUrl), new WaitingAsyncCallback<GetLoginPageUrlResult>() {

            @Override
            public void onWaitFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error getting login page URL");
            }
            @Override
            public void onWaitSuccess(GetLoginPageUrlResult result) {
                Window.Location.replace(result.getLoginPageUrl());
            }
        });
    }

    public static MetamacPrincipal getCurrentUser() {
        return IndicatorsWeb.principal;
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

    public static final String LOCAL_HOST  = GWT.getHostPageBaseURL();
    public static final String REMOTE_HOST = GWT.getHostPageBaseURL();

    public static String getRelativeURL(String url) {
        String realModuleBase;

        if (GWT.isScript()) {
            String moduleBase = GWT.getModuleBaseURL();

            // Use for deployment to PRODUCTION server
            realModuleBase = REMOTE_HOST;

            // Use to test compiled browser locally
            if (moduleBase.indexOf("localhost") != -1) {
                realModuleBase = LOCAL_HOST;
            }
        } else {
            // This is the URL for GWT Hosted mode
            realModuleBase = LOCAL_HOST;

        }

        return realModuleBase + url;
    }

}
