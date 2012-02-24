package com.arte.gestordocumental.util.concurrencia;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.xml.rpc.ServiceException;

import org.alfresco.webservice.accesscontrol.AccessControlServiceLocator;
import org.alfresco.webservice.accesscontrol.AccessControlServiceSoapBindingStub;
import org.alfresco.webservice.action.ActionServiceLocator;
import org.alfresco.webservice.action.ActionServiceSoapBindingStub;
import org.alfresco.webservice.administration.AdministrationServiceLocator;
import org.alfresco.webservice.administration.AdministrationServiceSoapBindingStub;
import org.alfresco.webservice.authentication.AuthenticationServiceLocator;
import org.alfresco.webservice.authentication.AuthenticationServiceSoapBindingStub;
import org.alfresco.webservice.authoring.AuthoringServiceLocator;
import org.alfresco.webservice.authoring.AuthoringServiceSoapBindingStub;
import org.alfresco.webservice.classification.ClassificationServiceLocator;
import org.alfresco.webservice.classification.ClassificationServiceSoapBindingStub;
import org.alfresco.webservice.content.ContentServiceLocator;
import org.alfresco.webservice.content.ContentServiceSoapBindingStub;
import org.alfresco.webservice.dictionary.DictionaryServiceLocator;
import org.alfresco.webservice.dictionary.DictionaryServiceSoapBindingStub;
import org.alfresco.webservice.repository.RepositoryServiceLocator;
import org.alfresco.webservice.repository.RepositoryServiceSoapBindingStub;
import org.alfresco.webservice.util.WebServiceException;
import org.apache.axis.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Arte Consultores
 *
 */
public final class WebServiceFactory
{
    /** Log */
    protected static Log logger = LogFactory.getLog(WebServiceFactory.class);
    
    /** Property file name */
    private static final String PROPERTY_FILE_NAME = "alfresco/webserviceclient.properties";
    private static final String REPO_LOCATION = "repository.location";
    private static final String REPO_WEBAPP = "repository.webapp";
    
    private static volatile boolean loadedProperties = false;
    /** Default endpoint address **/
    private static final String DEFAULT_ENDPOINT_ADDRESS = "http://localhost:8080/alfresco/api";
//    private static String endPointAddress = DEFAULT_ENDPOINT_ADDRESS;
    private static final String DEFAULT_REPO_WEBAPP = "alfresco";
//    private static String endPointWebapp = DEFAULT_REPO_WEBAPP;
    
    /** Default timeout **/
//    private static int timeoutMilliseconds = 60000;
    
    /** Service addresses */
    private static final String AUTHENTICATION_SERVICE_ADDRESS  = "/AuthenticationService";
    private static final String REPOSITORY_SERVICE_ADDRESS      = "/RepositoryService";
    private static final String CONTENT_SERVICE_ADDRESS         = "/ContentService";
    private static final String AUTHORING_SERVICE_ADDRESS       = "/AuthoringService";
    private static final String CLASSIFICATION_SERVICE_ADDRESS  = "/ClassificationService";
    private static final String ACTION_SERVICE_ADDRESS          = "/ActionService";
    private static final String ACCESS_CONTROL_ADDRESS          = "/AccessControlService";
    private static final String ADMINISTRATION_ADDRESS          = "/AdministrationService";
    private static final String DICTIONARY_SERVICE_ADDRESS      = "/DictionaryService";
    
    /**
     * Sets the endpoint address manually, overwrites the current value
     * 
     * @param endPointAddress	the end point address
     */
//    public static void setEndpointAddress(String endPointAddress)
//    {
//    	WebServiceFactory.endPointAddress = endPointAddress;
//    }
    
    /**
     * Sets the timeout
     * 
     * @param timeoutMilliseconds number of milliseconds to wait before timing out a web service call
     */
//    public static void setTimeoutMilliseconds(int timeoutMilliseconds)
//    {
//    	WebServiceFactory.timeoutMilliseconds = timeoutMilliseconds;
//    }
    
    /**
     * Get the current endpoints host
     * 
     * @return  the current endpoint host name
     */
    public static String getHost(WebServiceFactoryConfig webServiceFactoryConfig)
    {
        try
        {
            URL url = new URL(getEndpointAddress(webServiceFactoryConfig));
            return url.getHost();
        }
        catch (MalformedURLException exception)
        {
            throw new RuntimeException("Unable to get host string", exception);
        }
    }
    
    /**
     * Get the current endpoints port
     * 
     * @return  the current endpoint port number
     */
    public static int getPort(WebServiceFactoryConfig webServiceFactoryConfig)
    {
        try
        {
            URL url = new URL(getEndpointAddress(webServiceFactoryConfig));
            return url.getPort();
        }
        catch (MalformedURLException exception)
        {
            throw new RuntimeException("Unable to get host string", exception);
        }
    }
    
    /**
     * Get the authentication service
     * 
     * @return
     */
    public static AuthenticationServiceSoapBindingStub getAuthenticationService(WebServiceFactoryConfig webServiceFactoryConfig)
    {
    	AuthenticationServiceSoapBindingStub authenticationService = null;
        try 
        {
            // Get the authentication service
            AuthenticationServiceLocator locator = new AuthenticationServiceLocator();
            locator.setAuthenticationServiceEndpointAddress(getEndpointAddress(webServiceFactoryConfig) + AUTHENTICATION_SERVICE_ADDRESS);                
            authenticationService = (AuthenticationServiceSoapBindingStub)locator.getAuthenticationService();
        }
        catch (ServiceException jre) 
        {
        	if (logger.isDebugEnabled() == true)
            {
        		if (jre.getLinkedCause() != null)
                {
        			jre.getLinkedCause().printStackTrace();
                }
            }
   
            throw new WebServiceException("Error creating authentication service: " + jre.getMessage(), jre);
        }        
        
        // Time out after a minute
        authenticationService.setTimeout(webServiceFactoryConfig.getTimeoutMilliseconds());
        
        return authenticationService;
    }
    
    /**
     * Get the repository service
     * 
     * @return
     */
    public static RepositoryServiceSoapBindingStub getRepositoryService(WebServiceFactoryConfig webServiceFactoryConfig)
    {
    	RepositoryServiceSoapBindingStub repositoryService = null;           
		try 
		{
		    // Get the repository service
		    RepositoryServiceLocator locator = new RepositoryServiceLocator(AuthenticationUtils.getEngineConfiguration());
		    locator.setRepositoryServiceEndpointAddress(getEndpointAddress(webServiceFactoryConfig) + REPOSITORY_SERVICE_ADDRESS);                
		    repositoryService = (RepositoryServiceSoapBindingStub)locator.getRepositoryService();
            repositoryService.setMaintainSession(true);
		 }
		 catch (ServiceException jre) 
		 {
			 if (logger.isDebugEnabled() == true)
		     {
                if (jre.getLinkedCause() != null)
                {
                    jre.getLinkedCause().printStackTrace();
                }
		     }
		   
			 throw new WebServiceException("Error creating repositoryService service: " + jre.getMessage(), jre);
		}        
	
		// Time out after a minute
		repositoryService.setTimeout(webServiceFactoryConfig.getTimeoutMilliseconds());      
        
        return repositoryService;
    }
    
    /**
     * Get the authoring service
     * 
     * @return
     */
    public static AuthoringServiceSoapBindingStub getAuthoringService(WebServiceFactoryConfig webServiceFactoryConfig)
    {
    	AuthoringServiceSoapBindingStub authoringService = null;
                  
        try 
        {
            // Get the authoring service
            AuthoringServiceLocator locator = new AuthoringServiceLocator(AuthenticationUtils.getEngineConfiguration());
            locator.setAuthoringServiceEndpointAddress(getEndpointAddress(webServiceFactoryConfig) + AUTHORING_SERVICE_ADDRESS);                
            authoringService = (AuthoringServiceSoapBindingStub)locator.getAuthoringService();
            authoringService.setMaintainSession(true);
        }
        catch (ServiceException jre) 
        {
            if (logger.isDebugEnabled() == true)
            {
                if (jre.getLinkedCause() != null)
                {
                    jre.getLinkedCause().printStackTrace();
                }
            }
   
            throw new WebServiceException("Error creating authoring service: " + jre.getMessage(), jre);
        }        
        
        // Time out after a minute
        authoringService.setTimeout(webServiceFactoryConfig.getTimeoutMilliseconds());       
        
        return authoringService;
    }
    
    /**
     * Get the classification service
     * 
     * @return
     */
    public static ClassificationServiceSoapBindingStub getClassificationService(WebServiceFactoryConfig webServiceFactoryConfig)
    {
    	ClassificationServiceSoapBindingStub classificationService = null;
            
        try 
        {
            // Get the classification service
            ClassificationServiceLocator locator = new ClassificationServiceLocator(AuthenticationUtils.getEngineConfiguration());
            locator.setClassificationServiceEndpointAddress(getEndpointAddress(webServiceFactoryConfig) + CLASSIFICATION_SERVICE_ADDRESS);                
            classificationService = (ClassificationServiceSoapBindingStub)locator.getClassificationService();
            classificationService.setMaintainSession(true);
        }
        catch (ServiceException jre) 
        {
            if (logger.isDebugEnabled() == true)
            {
                if (jre.getLinkedCause() != null)
                {
                    jre.getLinkedCause().printStackTrace();
                }
            }
   
            throw new WebServiceException("Error creating classification service: " + jre.getMessage(), jre);
        }        
        
        // Time out after a minute
        classificationService.setTimeout(webServiceFactoryConfig.getTimeoutMilliseconds());        
        
        return classificationService;
    }

    /**
     * Get the action service
     * 
     * @return
     */
    public static ActionServiceSoapBindingStub getActionService(WebServiceFactoryConfig webServiceFactoryConfig)
    {
    	ActionServiceSoapBindingStub actionService = null;
            
        try 
        {
            // Get the action service
            ActionServiceLocator locator = new ActionServiceLocator(AuthenticationUtils.getEngineConfiguration());
            locator.setActionServiceEndpointAddress(getEndpointAddress(webServiceFactoryConfig) + ACTION_SERVICE_ADDRESS);                
            actionService = (ActionServiceSoapBindingStub)locator.getActionService();
            actionService.setMaintainSession(true);
        }
        catch (ServiceException jre) 
        {
            if (logger.isDebugEnabled() == true)
            {
                if (jre.getLinkedCause() != null)
                {
                    jre.getLinkedCause().printStackTrace();
                }
            }
   
            throw new WebServiceException("Error creating action service: " + jre.getMessage(), jre);
        }        
            
        // Time out after a minute
        actionService.setTimeout(webServiceFactoryConfig.getTimeoutMilliseconds());      
        
        return actionService;
    }

    /**
     * Get the content service
     * 
     * @return  the content service
     */
    public static ContentServiceSoapBindingStub getContentService(WebServiceFactoryConfig webServiceFactoryConfig)
    {
    	ContentServiceSoapBindingStub contentService = null;           
        try 
        {
            // Get the content service
            ContentServiceLocator locator = new ContentServiceLocator(AuthenticationUtils.getEngineConfiguration());
            locator.setContentServiceEndpointAddress(getEndpointAddress(webServiceFactoryConfig) + CONTENT_SERVICE_ADDRESS);                
            contentService = (ContentServiceSoapBindingStub)locator.getContentService();
            contentService.setMaintainSession(true);
        }
        catch (ServiceException jre) 
        {
            if (logger.isDebugEnabled() == true)
            {
                if (jre.getLinkedCause() != null)
                {
                    jre.getLinkedCause().printStackTrace();
                }
            }
   
            throw new WebServiceException("Error creating content service: " + jre.getMessage(), jre);
        }        
        
        // Time out after a minute
        contentService.setTimeout(webServiceFactoryConfig.getTimeoutMilliseconds());       
        
        return contentService;
    }
    
    /**
     * Get the access control service
     * 
     * @return  the access control service
     */
    public static AccessControlServiceSoapBindingStub getAccessControlService(WebServiceFactoryConfig webServiceFactoryConfig)
    {
    	AccessControlServiceSoapBindingStub accessControlService = null;           
        try 
        {
            // Get the access control service
            AccessControlServiceLocator locator = new AccessControlServiceLocator(AuthenticationUtils.getEngineConfiguration());
            locator.setAccessControlServiceEndpointAddress(getEndpointAddress(webServiceFactoryConfig) + ACCESS_CONTROL_ADDRESS);                
            accessControlService = (AccessControlServiceSoapBindingStub)locator.getAccessControlService();
            accessControlService.setMaintainSession(true);
        }
        catch (ServiceException jre) 
        {
            if (logger.isDebugEnabled() == true)
            {
                if (jre.getLinkedCause() != null)
                {
                    jre.getLinkedCause().printStackTrace();
                }
            }
   
            throw new WebServiceException("Error creating access control service: " + jre.getMessage(), jre);
        }        
            
        // Time out after a minute
        accessControlService.setTimeout(webServiceFactoryConfig.getTimeoutMilliseconds());
        
        return accessControlService;
    }
    
    /**
     * Get the administration service
     * 
     * @return  the administration service
     */
    public static AdministrationServiceSoapBindingStub getAdministrationService(WebServiceFactoryConfig webServiceFactoryConfig)
    {
    	AdministrationServiceSoapBindingStub administrationService = null;
            
        try 
        {
            // Get the adminstration service
            AdministrationServiceLocator locator = new AdministrationServiceLocator(AuthenticationUtils.getEngineConfiguration());
            locator.setAdministrationServiceEndpointAddress(getEndpointAddress(webServiceFactoryConfig) + ADMINISTRATION_ADDRESS);                
            administrationService = (AdministrationServiceSoapBindingStub)locator.getAdministrationService();
            administrationService.setMaintainSession(true);
        }
        catch (ServiceException jre) 
        {
            if (logger.isDebugEnabled() == true)
            {
                if (jre.getLinkedCause() != null)
                {
                    jre.getLinkedCause().printStackTrace();
                }
            }
   
            throw new WebServiceException("Error creating administration service: " + jre.getMessage(), jre);
        }        
        
        // Time out after a minute
        administrationService.setTimeout(webServiceFactoryConfig.getTimeoutMilliseconds());       
        
        return administrationService;
    }

    /**
     * Get the dictionary service
     * 
     * @return  the dictionary service
     */
    public static DictionaryServiceSoapBindingStub getDictionaryService(WebServiceFactoryConfig webServiceFactoryConfig)
    {
    	DictionaryServiceSoapBindingStub dictionaryService = null;
           
        try 
        {
            // Get the dictionary service
            DictionaryServiceLocator locator = new DictionaryServiceLocator(AuthenticationUtils.getEngineConfiguration());
            locator.setDictionaryServiceEndpointAddress(getEndpointAddress(webServiceFactoryConfig) + DICTIONARY_SERVICE_ADDRESS);                
            dictionaryService = (DictionaryServiceSoapBindingStub)locator.getDictionaryService();
            dictionaryService.setMaintainSession(true);
        }
        catch (ServiceException jre) 
        {
            if (logger.isDebugEnabled() == true)
            {
                if (jre.getLinkedCause() != null)
                {
                    jre.getLinkedCause().printStackTrace();
                }
            }
   
            throw new WebServiceException("Error creating dictionary service: " + jre.getMessage(), jre);
        }        
        
        // Time out after a minute
        dictionaryService.setTimeout(webServiceFactoryConfig.getTimeoutMilliseconds());

        return dictionaryService;
    }

    private static synchronized void loadProperties(String propertyFileName, WebServiceFactoryConfig webServiceFactoryConfig)
    {
        if (loadedProperties)
        {
            return;
        }
        Properties props = new Properties();
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTY_FILE_NAME);
        if (is != null)
        {
            // Load from the file
            try
            {
                props.load(is);            
            }
            catch (Exception e)
            {
                // Do nothing, just use the default endpoint
                logger.debug("Unable to load webservice client properties from " + propertyFileName + ": " + e.getMessage());
            }
        }
        // Add defaults for any properties not set
        
        if (props.getProperty(REPO_LOCATION) != null)
        {
            webServiceFactoryConfig.setEndPointAddress(props.getProperty(REPO_LOCATION));
        }
        if (props.getProperty(REPO_WEBAPP) != null)
        {
        	webServiceFactoryConfig.setEndPointWebapp(props.getProperty(REPO_WEBAPP));
        }
        loadedProperties = true;
    }
    
    /**
     * Gets the configured end-point address
     */
    public static String getEndpointAddress(WebServiceFactoryConfig webServiceFactoryConfig)
    {
        if (!loadedProperties)
        {
            loadProperties(PROPERTY_FILE_NAME, webServiceFactoryConfig);
        }
        return webServiceFactoryConfig.getEndPointAddress();
    }
    
    /**
     * Gets the configured end-point webapp name
     */
    public static String getEndpointWebapp(WebServiceFactoryConfig webServiceFactoryConfig)
    {
        if (!loadedProperties)
        {
            loadProperties(PROPERTY_FILE_NAME, webServiceFactoryConfig);
        }
        return webServiceFactoryConfig.getEndPointWebapp();
    }
    
    /**
     * Crea una configuracion para ser usada por el WebServiceFactory
     * @param endPointAddress
     * @param endPointWebapp
     * @param timeoutMilliseconds
     * @return la configuracion creada
     */
    public static WebServiceFactoryConfig crearWebServiceFactoryConfig(String endPointAddress, String endPointWebapp, Integer timeoutMilliseconds, String user, String password) {
    	if (StringUtils.isEmpty(endPointAddress)) {
    		endPointAddress = DEFAULT_ENDPOINT_ADDRESS;
    	}
    	
    	if (StringUtils.isEmpty(endPointWebapp)) {
    		endPointWebapp = DEFAULT_REPO_WEBAPP;
    	}
    	
    	if (timeoutMilliseconds == null) {
    		timeoutMilliseconds = 60000;
    	}
    	
    	if (StringUtils.isEmpty(user) || StringUtils.isEmpty(password)) {
    	  throw new WebServiceException("Error inicializadon datos de configuracion para WebServiceFactory: usuario o password nulo/s");
    	}
    	  
    	return new WebServiceFactoryConfig(endPointAddress, endPointWebapp, timeoutMilliseconds, user, password);
    }
}
