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
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtplatform.mvp.client.DelayedBindRegistry;

import es.gobcan.istac.indicators.web.client.gin.IndicatorsWebGinjector;
import es.gobcan.istac.indicators.web.shared.GetEditionLanguagesAction;
import es.gobcan.istac.indicators.web.shared.GetEditionLanguagesResult;
import es.gobcan.istac.indicators.web.shared.GetLoginPageUrlAction;
import es.gobcan.istac.indicators.web.shared.GetLoginPageUrlResult;
import es.gobcan.istac.indicators.web.shared.ValidateTicketAction;
import es.gobcan.istac.indicators.web.shared.ValidateTicketResult;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class IndicatorsWeb extends MetamacEntryPoint {

    private static final boolean             SECURITY_ENABLED = false;
    private static Logger                    logger           = Logger.getLogger(IndicatorsWeb.class.getName());

    private static MetamacPrincipal          principal;
    private static IndicatorsWebMessages     messages;
    private static IndicatorsWebCoreMessages coreMessages;
    private static IndicatorsWebConstants    constants;

    private IndicatorsWebGinjector           ginjector        = GWT.create(IndicatorsWebGinjector.class);

    @Override
    public void onModuleLoad() {
        setUncaughtExceptionHandler();

        prepareApplication(SECURITY_ENABLED);
    }

    public void prepareApplication(boolean securityEnabled) {
        if (securityEnabled) {
            loadSecuredApplication();
        } else {
            loadNonSecuredApplication();
        }
    }

    private void loadSecuredApplication() {
        String ticketParam = Window.Location.getParameter(TICKET);
        if (ticketParam != null) {
            UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
            urlBuilder.removeParameter(TICKET);
            urlBuilder.setHash(Window.Location.getHash() + TICKET_HASH + ticketParam);
            String url = urlBuilder.buildString();
            Window.Location.replace(url);
            return;
        }

        String hash = Window.Location.getHash();

        String ticketHash = null;
        if (hash.contains(TICKET_HASH)) {
            ticketHash = hash.substring(hash.indexOf(TICKET_HASH) + TICKET_HASH.length(), hash.length());
        }

        if (ticketHash == null || ticketHash.length() == 0) {
            displayLoginView();
        } else {
            final String ticketInValidation = ticketHash;
            String serviceUrl = Window.Location.createUrlBuilder().buildString();
            ginjector.getDispatcher().execute(new ValidateTicketAction(ticketHash, serviceUrl), new AsyncCallback<ValidateTicketResult>() {

                @Override
                public void onFailure(Throwable arg0) {
                    logger.log(Level.SEVERE, "Error validating ticket");
                }
                @Override
                public void onSuccess(ValidateTicketResult result) {
                    setPrincipal(result.getMetamacPrincipal());

                    String userHash = Window.Location.getHash();
                    userHash = userHash.replace(TICKET_HASH + ticketInValidation, "");

                    String url = Window.Location.createUrlBuilder().setHash(userHash).buildString();
                    Window.Location.assign(url);

                    // Load edition languages
                    loadConfiguration();
                }
            });
        }
    }

    private void loadNonSecuredApplication() {
        ginjector.getDispatcher().execute(new MockCASUserAction("GESTOR_ACCESOS"), new AsyncCallback<MockCASUserResult>() {

            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error mocking CAS user");
            }
            @Override
            public void onSuccess(MockCASUserResult result) {
                setPrincipal(result.getMetamacPrincipal());

                // Load edition languages
                loadConfiguration();
            }
        });
    }

    private void loadConfiguration() {
        // Load edition languages
        ginjector.getDispatcher().execute(new GetEditionLanguagesAction(), new AsyncCallback<GetEditionLanguagesResult>() {

            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error loading edition languages");
                // If an error occurs while loading edition languages, enable SPANISH, ENGLISH and PORTUGUESE by default
                ApplicationEditionLanguages.setEditionLanguages(new String[]{ApplicationEditionLanguages.SPANISH, ApplicationEditionLanguages.ENGLISH, ApplicationEditionLanguages.PORTUGUESE});
                loadNavbar();
            }
            @Override
            public void onSuccess(GetEditionLanguagesResult result) {
                ApplicationEditionLanguages.setEditionLanguages(result.getLanguages());
                ApplicationOrganisation.setCurrentOrganisation(ApplicationOrganisationEnum.ISTAC.name());
                loadNavbar();
            }
        });
    }

    private void loadNavbar() {
        ginjector.getDispatcher().execute(new GetNavigationBarUrlAction(), new AsyncCallback<GetNavigationBarUrlResult>() {

            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error loading toolbar");
                loadApplication();
            }

            @Override
            public void onSuccess(GetNavigationBarUrlResult result) {
                // Load scripts for navigation bar
                if (result.getNavigationBarUrl() != null) {
                    MetamacNavBar.loadScripts(result.getNavigationBarUrl());
                } else {
                    logger.log(Level.SEVERE, "Error loading toolbar");
                }
                loadApplication();
            };
        });
    }

    private void loadApplication() {
        LoginAuthenticatedEvent.fire(ginjector.getEventBus(), IndicatorsWeb.principal);
        // This is required for GWT-Platform proxy's generator.
        DelayedBindRegistry.bind(ginjector);
        ginjector.getPlaceManager().revealCurrentPlace();
    }

    protected void setPrincipal(MetamacPrincipal principal) {
        IndicatorsWeb.principal = principal;
    }

    @Override
    public void setUncaughtExceptionHandler() {
        GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

            @Override
            public void onUncaughtException(Throwable e) {
                Throwable unwrapped = unwrap(e);
                logger.log(Level.SEVERE, unwrapped.toString(), e);
            }
        });
    }

    @Override
    public Throwable unwrap(Throwable e) {
        if (e instanceof UmbrellaException) {
            UmbrellaException ue = (UmbrellaException) e;
            if (ue.getCauses().size() == 1) {
                return unwrap(ue.getCauses().iterator().next());
            }
        }
        return e;
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
