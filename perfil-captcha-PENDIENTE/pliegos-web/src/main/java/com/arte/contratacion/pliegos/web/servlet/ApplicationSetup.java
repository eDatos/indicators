package com.arte.contratacion.pliegos.web.servlet;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.security.Security;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import jfactory.jbean.PropApplication;
import jfactory.tx.Transaction;
import jfactory.util.Propiedades;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import asf_securityagent.a;

import com.arte.contratacion.ServiceLocator;
import com.arte.contratacion.pliegos.conf.AppConfiguration;
import com.arte.contratacion.pliegos.conf.EnvironmentConstant;
import com.arte.contratacion.pliegos.organigrama.OrganigramaLoaderCore;
import com.arte.contratacion.pliegos.util.PropertiesConstants;


public class ApplicationSetup extends HttpServlet {
	private static final Log LOG = LogFactory.getLog(ApplicationSetup.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public void init() throws ServletException {
		super.init();
		try {
			// Provider Bouncy Castle para poder indexar pdfs protegidos
			Security.addProvider(new BouncyCastleProvider());
			
			String nombreCluster = System.getProperty("servidor");
			if (nombreCluster == null) {
				throw new IllegalArgumentException("Propiedad \"servidor\" es requerida");
			}
			LOG.info("Nombre del Cluster = \"" + nombreCluster + "\"");
			
			InputStream inputStream = this.getClass().getResourceAsStream("/conf/repositorio/jackrabbit.properties");
			Properties properties = new Properties();

			properties.load(inputStream);
		
			String repositorioDirectorio = properties.getProperty("repositorio.directorio");
			if (repositorioDirectorio == null) {
				throw new IllegalArgumentException("Propiedad \"repositorioDirectorio\" es requerida");
			}
			

			String url = (new URL(repositorioDirectorio + nombreCluster)).getFile();
			LOG.info("URL Jackrabbit = \"" + url + "\"");
			File jackrabbitCluster = new File(url);
			if (!jackrabbitCluster.exists()) {
				jackrabbitCluster.mkdir();
			}
			
			url = (new URL(repositorioDirectorio + "cluster-journal")).getFile();
			LOG.info("URL Jackrabbit Cluster Journal = \"" + url + "\"");
			File clusterJournal = new File(url);
			if (!clusterJournal.exists()) {
				clusterJournal.mkdir();
			}
			
			// Platino
            configurarPlatino();
			
			// Inicializamos Spring (se llama a un servicio cualquiera para que se inicialice todo el spring)
			ServiceLocator.instance().getAdministracionServicio();

			// Imprimir propiedades
            loguearConfiguracion();
			
			// Cargar los organismos al levantar el tomcat
			OrganigramaLoaderCore.instance().actualizarOrganismos();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void configurarPlatino() throws Exception {

        // Workaround (CUIDADO CON ESTO): PLATINO FIRMA (Mirado el código de las librerías: asf_securityagent.jar y jFactory.jar)
		
		// OJO!!! jFactory.jar cambia el TimeZone al leer las propiedades para Platino
		TimeZone timeZone = TimeZone.getDefault(); // jfactory.locale.timeZone
		Locale locale = Locale.getDefault();
		
		try {
			sustituirPropiedades(a.b());
			sustituirPropiedades(PropApplication.getPropiedades());
			PropApplication.getLog().closeStreams();
			
			// Reiniciamos el logFactory
			jfactory.util.LogFactory localLogFactory = new jfactory.util.LogFactory(PropApplication.getPropiedades());
			PropApplication.getPropiedades().remove("jfactory.util.Log.objetoLog");
			
			// Ponemos el nuevo log
			jfactory.util.Log log = localLogFactory.createLog();
			Field field = PropApplication.class.getDeclaredField("c");
			field.setAccessible(true);
			field.set(null, log);
			field.setAccessible(false);			
	        Transaction.setProperties(PropApplication.getPropiedades());
	        Transaction.setLog(log);
		} finally {
			TimeZone.setDefault(timeZone);
			Locale.setDefault(locale);
		}
	}	
	
	private void sustituirPropiedades(Propiedades propiedades) {
		String securityAgentUrl = AppConfiguration.instance().getConfiguration().getString("platino.ws.firma.securityagentURL");
		String basePath = EnvironmentConstant.DATA_URL;
		
		for (Object key : propiedades.keySet()) {
			String value = propiedades.getString((String)key);
			if (value.contains("${platino.ws.firma.securityagentURL}")) {
				value = value.replace("${platino.ws.firma.securityagentURL}", securityAgentUrl);
				propiedades.setProperty((String)key, value);
			} else if (value.contains("${data.perfildelcontratante.base.path}")){
				value = value.replace("${data.perfildelcontratante.base.path}", basePath);
				propiedades.setProperty((String)key, value);
			}
		}
	}
	
	private void loguearConfiguracion() {
    	LOG.info("**********************************************************");
    	LOG.info("CONFIGURACIÓN EN FICHEROS DE PROPIEDADES EN /data");
    	
    	// Firma
    	loguearConfiguracion(PropertiesConstants.PLATINO_WS_FIRMA_ENDPOINT);
    	loguearConfiguracion(PropertiesConstants.PLATINO_WS_FIRMA_INVOKING_APP);
    	loguearConfiguracion(PropertiesConstants.PLATINO_WS_FIRMA_ALIAS);
    	loguearConfiguracion(PropertiesConstants.PLATINO_WS_FIRMA_SECURITY_AGENT);
    	loguearConfiguracion(PropertiesConstants.PLATINO_WS_FIRMA_CERTIFICATE_SEPARATOR);
    	loguearConfiguracion(PropertiesConstants.PLATINO_WS_FIRMA_ASF_URL);
    	loguearConfiguracion(PropertiesConstants.PLATINO_WS_FIRMA_JS_URL);
    	loguearConfiguracion(PropertiesConstants.PLATINO_BACKOFFICE_URI);
    	loguearConfiguracion(PropertiesConstants.PLATINO_CERTIFICADO_ALIAS);
    	
    	// Mensajes
    	loguearConfiguracion(PropertiesConstants.PLATINO_WS_MENSAJES_ENDPOINT);
    	
    	// platino-security
    	loguearConfiguracion(PropertiesConstants.PLATINO_SECURITY_CRYPTO_PROVIDER);
    	loguearConfiguracion(PropertiesConstants.PLATINO_SECURITY_CRYPTO_KEYSTORE);
    	loguearConfiguracion(PropertiesConstants.PLATINO_SECURITY_CRYPTO_FILE);
    	
    	// Quartz
    	loguearConfiguracion(PropertiesConstants.QUARTZ_DIRECTORY_TEMPUPLOAD);
    	
    	// BIRT
    	loguearConfiguracion(PropertiesConstants.BIRT_ENGINEHOME);
    	loguearConfiguracion(PropertiesConstants.BIRT_LOGDIRECTORY);
    	loguearConfiguracion(PropertiesConstants.BIRT_RESOURCEPATH);
    	loguearConfiguracion(PropertiesConstants.BIRT_LOGLEVEL);
    	loguearConfiguracion(PropertiesConstants.BIRT_REPORTS);
    	
    	// LDAP
    	loguearConfiguracion(PropertiesConstants.LDAP_URL);
    	loguearConfiguracion(PropertiesConstants.LDAP_USUARIO);
//    	loguearConfiguracion(PropertiesConstants.LDAP_PASSWORD);
    	loguearConfiguracion(PropertiesConstants.LDAP_DNUSER);
    	loguearConfiguracion(PropertiesConstants.LDAP_RAIZ);

    	// Generales
    	loguearConfiguracion(PropertiesConstants.PROCEDIMIENTO_BASEURL);
    	loguearConfiguracion(PropertiesConstants.INFORMEANUNCIO_BASEURL);
    	loguearConfiguracion(PropertiesConstants.SELLOANUNCIO_BASEURL);
    	loguearConfiguracion(PropertiesConstants.ANUNCIOPREVIO_BASEURL);

    	// Otros
    	loguearConfiguracion(PropertiesConstants.ORGANIGRAMA_WS_ENDPOINT);
    	loguearConfiguracionOpcional(PropertiesConstants.PLATINO_ENABLED);
    	loguearConfiguracionOpcional(PropertiesConstants.PLATINO_LOCAL_TEST);
    	loguearConfiguracionOpcional(PropertiesConstants.PLATINO_LOCAL_TEST_URL);
    	loguearConfiguracionOpcional(PropertiesConstants.SERVER_URL);
    	loguearConfiguracionOpcional(PropertiesConstants.CODICEWS_ENABLED);
    	loguearConfiguracionOpcional(PropertiesConstants.LOGIN_MAXIMOS_INTENTOS);
    	loguearConfiguracionOpcional("login.numeroIntentosAumentarSeguridad"); // TODO en PropertiesConstants
    	

    	LOG.info("**********************************************************");	
	}    	
	
	private String loguearConfiguracion(String propiedad) {
    	return loguearConfiguracion(propiedad, Boolean.TRUE);
    }
    
    private String loguearConfiguracionOpcional(String propiedad) {
    	return loguearConfiguracion(propiedad, Boolean.FALSE);
    }
    
	private String loguearConfiguracion(String propiedad, Boolean required) {
		Object valorObject = AppConfiguration.instance().getProperties().get(propiedad);
		String valor = valorObject != null ? valorObject.toString() : null;
		if (required && valor == null) {
			throw new IllegalArgumentException("No se ha indicado en el fichero de propiedades la propiedad " + propiedad);
		}
		LOG.info("[" + propiedad + "] = " + valor);
		return valor;
	}
	

	@Override
	public void destroy() {
		// Destruimos Spring
		ServiceLocator.instance().shutdown();
		super.destroy();
	}

}
