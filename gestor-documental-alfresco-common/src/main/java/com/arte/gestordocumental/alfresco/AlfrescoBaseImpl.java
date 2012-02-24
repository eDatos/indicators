package com.arte.gestordocumental.alfresco;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.alfresco.webservice.authoring.AuthoringServiceSoapBindingStub;
import org.alfresco.webservice.authoring.VersionResult;
import org.alfresco.webservice.classification.ClassificationServiceSoapBindingStub;
import org.alfresco.webservice.content.Content;
import org.alfresco.webservice.content.ContentServiceSoapBindingStub;
import org.alfresco.webservice.content.ContentServiceSoapPort;
import org.alfresco.webservice.repository.QueryResult;
import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.repository.RepositoryServiceSoapBindingStub;
import org.alfresco.webservice.repository.RepositoryServiceSoapPort;
import org.alfresco.webservice.repository.UpdateResult;
import org.alfresco.webservice.types.CML;
import org.alfresco.webservice.types.CMLCreate;
import org.alfresco.webservice.types.CMLDelete;
import org.alfresco.webservice.types.CMLMove;
import org.alfresco.webservice.types.CMLUpdate;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Node;
import org.alfresco.webservice.types.ParentReference;
import org.alfresco.webservice.types.Predicate;
import org.alfresco.webservice.types.Query;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.types.ResultSet;
import org.alfresco.webservice.types.ResultSetRow;
import org.alfresco.webservice.types.ResultSetRowNode;
import org.alfresco.webservice.types.Store;
import org.alfresco.webservice.types.Version;
import org.alfresco.webservice.types.VersionHistory;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.ISO9075;
import org.alfresco.webservice.util.WebServiceException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

import com.arte.gestordocumental.alfresco.exceptions.AlfrescoException;
import com.arte.gestordocumental.util.concurrencia.AuthenticationUtils;
import com.arte.gestordocumental.util.concurrencia.WebServiceFactory;
import com.arte.gestordocumental.util.concurrencia.WebServiceFactoryConfig;


public abstract class AlfrescoBaseImpl implements Alfresco, InitializingBean {
    
    private static final int BUFFER_SIZE = 4096;

	protected static Log log = LogFactory.getLog(Alfresco.class);
	
	// Atributos inicializados desde Spring
	private String alfrescoWebService;
	private String STORE_NOMBRE = "SpacesStore";
	private String STORE_VERSIONES_NOMBRE = "version2Store";
	private String pathRaiz;
	protected String usuario;
	protected String password;
	
	// Servicios de alfresco
	private RepositoryServiceSoapBindingStub repositoryService;
	private ContentServiceSoapBindingStub contentService;
	private AuthoringServiceSoapBindingStub authoringService;
	private ClassificationServiceSoapBindingStub classificationService;
	
	// Otros atributos
	private Store store;
	private Store storeVersion;
	private Reference referenceStore;
	
	private Map<String, String> namespacesModelo = new HashMap<String, String>();
	private WebServiceFactoryConfig webServiceFactoryConfig;
	
	/**
	 * Comprobaci�n de propiedades despu�s de la inicializaci�n del bean e inicializaciones
	 */
	public void afterPropertiesSet() throws Exception {
		
		log.info("Comprobacion de propiedades tras la construccion del bean Alfresco");

		checkRequired("alfrescoWebService", alfrescoWebService);		
		checkRequired("usuario", usuario);
		checkRequired("password", password);
		checkRequired("storeNombre", STORE_NOMBRE);
		checkRequired("storeVersionesNombre", STORE_VERSIONES_NOMBRE);	
		try {
			checkRequired("pathRaiz", pathRaiz);
		} catch (Exception e) {
			pathRaiz = "";
		}
		
		if (STORE_NOMBRE.equals(AlfrescoConstants.STORE_SPACESSTORE)) {
			// Se asume que si el store es "SpacesStore" se crea todo a partir de la carpeta 'company_home', para que los nodos creados
			// puedan verse desde el web client. Esta carpeta est� ya creada.
			setPathRaiz(AlfrescoConstants.CARPETA_COMPANY_HOME + AlfrescoConstants.SEPARADOR + pathRaiz); 	
		}

		log.info("   - alfrescoWebService = " + alfrescoWebService);
		log.info("   - usuario = " + usuario);
		log.info("   - password = *********");		
		log.info("   - storeNombre = " + STORE_NOMBRE);
		log.info("   - storeVersionesNombre = " + STORE_VERSIONES_NOMBRE);
		log.info("   - pathRaiz = " + pathRaiz);
		
//		// Se indica la direcci�n del web service de Alfresco
//		WebServiceFactory.setEndpointAddress(alfrescoWebService);
		// Configuracion de los servicios de Alfresco
		webServiceFactoryConfig = WebServiceFactory.crearWebServiceFactoryConfig(alfrescoWebService, null, null, usuario, password);
		
		try {			
			// Se abre la sesi�n de Alfresco
			abrirSesion(usuario, password);
			log.info("   - Iniciada sesion de Alfresco");
			// Inicializaci�n del workspace
			inicializarWorkspace();
			
			// Inicializar mapa con prefix y namespaces
			inicializarNamespacesModelo();
		} catch (Exception e) {
			// Se permite continuar...
		} finally {
			cerrarSesion();
		}
		log.info("Terminada la comprobacion de propiedades del bean Alfresco");
	}

	/* ********************************************************************************************************************************* */
	/* ***************************************** M�todos de la interfaz **************************************************************** */
	/* ********************************************************************************************************************************* */
	
	/**
	 * No est� permitido abrir la sesi�n con un usuario gen�rico, a no ser que la clase hija lo permita
	 */
	public void abrirSesion() throws RemoteException {
		throw new UnsupportedOperationException("De forma general, no est� permitido abrir la sesi�n con un usuario gen�rico");
	}
	
	/**
	 * Abre la sesi�n almacenando el ticket en el thread local
	 */
	public void abrirSesion(String usuario, String password) throws RemoteException {
		try {
			webServiceFactoryConfig.setUser(usuario);
			webServiceFactoryConfig.setPassword(password);
			AuthenticationUtils.startSession(webServiceFactoryConfig);
		} catch (Exception e) {
			log.error("No se ha podido iniciar la sesion de Alfresco", e);
			throw new RemoteException("No se ha podido iniciar la sesion de Alfresco", e);			
		}		
	}	
	
	/**
	 * Cierra la sesi�n de Alfresco
	 */
	public void cerrarSesion() {
		try {
			AuthenticationUtils.endSession(webServiceFactoryConfig);
		} catch (WebServiceException e) {
			log.error("No se ha podido cerrar la sesion de Alfresco", e);							
		}					
	}
	
	
    
    public CMLCreate crearCMLCreate(String rutaRelativa, String nombreNodo, String tipoNodo, NamedValue[] propiedades) throws Exception {
        String path = getPathAbsoluto(rutaRelativa);
        return crearCMLCreateAbsoluto(path, nombreNodo, tipoNodo, propiedades);
    }
    
    public CMLUpdate crearCMLUpdate(String ruta, NamedValue[] propiedades) throws Exception {
        Predicate predicado = getPredicado(ruta, null, null, null, Boolean.TRUE);
        return new CMLUpdate(propiedades, predicado, null);
    }
    
    public CMLMove crearCMLMove(String rutaOrigen, String nombreNodo, String rutaDestino) throws Exception {
        // Nombre del nodo en la forma "{http://...}nombreNodo"
        String nombreNodoConQname = AlfrescoConstants.createQNameString(getNamespaceModel(), nombreNodo);       
        
        // Nodo Padre ORIGEN
        Predicate predicateOrigen = getPredicado(rutaOrigen, nombreNodo, null, null, null);
        
        // Referencia al nodo Padre DESTINO
        String pathAbsolutoDestino = getPathAbsoluto(rutaDestino);
        Reference nodoPadre = getReferenciaAbsoluto(pathAbsolutoDestino, null);
        String tipoAsociacion = getTipoAsociacion(nodoPadre);
        ParentReference parentReferenceDestino = new ParentReference(getStore(), nodoPadre.getUuid(), null, tipoAsociacion, nombreNodoConQname);

        CMLMove cmlMove = new CMLMove();
        cmlMove.setWhere(predicateOrigen);        
        cmlMove.setTo(parentReferenceDestino);
        return cmlMove;
    }

    public CMLDelete crearCMLDelete(String ruta) throws Exception {
        Predicate predicado = getPredicado(ruta, null, null, null, Boolean.TRUE);
        return new CMLDelete(predicado);
    }
    
    public UpdateResult[] invocarCML(CML cml) throws Exception {
        return getRepositoryService().update(cml);
    }

	/**
	 * Existe ruta
	 */
	public boolean existeRuta(String ruta) throws Exception {
		return getReferenciaAbsoluto(AlfrescoPrivadoUtils.getPathAbsoluto(getPathRaiz(), ruta), null) != null; 
	}
	
	/**
	 * Obtiene un nodo
	 */
	public Node obtenerNodo(String ruta) throws Exception {
		return obtenerNodo(ruta, null);
	}

	/**
     * Obtiene un nodo si es del tipo indicado
     */
    public Node obtenerNodo(String ruta, String tipoNodo) throws Exception {
        Node[] nodos = getNodos(ruta, null, null, null, tipoNodo, true);
        return (nodos != null && nodos.length != 0) ? nodos[0] : null;
    }

	/**
	 * Buscar nodos en una determinada ruta, teniendo en cuenta ciertos criterios de b�squeda
	 */
	public Node[] buscarNodos(String ruta, String tipoNodo, String[] condicionesAdicionales) throws Exception {
		String pathAbsoluto = getPathAbsoluto(ruta);
		Query query = getQuery(pathAbsoluto, tipoNodo, true, condicionesAdicionales);
		Predicate predicado = new Predicate(null, getStore(), query);
		Node[] nodes = getRepositoryService().get(predicado);
		return nodes;
	}

	/**
	 * Obtiene la url para acceder al contenido
	 */
	public String obtenerUrlContenido(String uuid, String propiedadContenido) throws Exception {
	    return obtenerUrlContenido(uuid, propiedadContenido, getStore());
	}

//	/**
//	 * @deprecated Obtener el contenido de un nodo - NO se ha de usar porque no obtiene correctamente el contenido
//	 * cuando Alfresco no ha indexado. Utilizar en su lugar 'obtenerContenidoPorContentUrl':
//	 * 		String urlContenido = AlfrescoUtils.obtenerPropiedadValue(nodo.getProperties(), AlfrescoConstants.CONTENIDO_ALFRESCO_QNAME);
//	 * 		InputStream stream = obtenerContenidoPorContentUrl(urlContenido);
//	 */
//	public InputStream obtenerContenido(String uuid, String propiedadContenido) throws Exception {
//		return obtenerContenido(uuid, propiedadContenido, getStore());
//	}
	
//	/**
//	 * @deprecated Obtener el contenido de una versi�n de un nodo - NO se ha de usar porque no obtiene correctamente el contenido
//	 */
//	public InputStream obtenerContenidoVersion(String uuid, String propiedadContenido) throws Exception {
//		return obtenerContenido(uuid, propiedadContenido, getStoreVersion());
//	}
	
//	/**
//	 * @deprecated Obtener el contenido de un nodo - NO se ha de usar porque no obtiene correctamente el contenido
//	 */
//	private InputStream obtenerContenido(String uuid, String propiedadContenido, Store storeBusqueda) throws Exception {
//		try {
//		    String url = obtenerUrlContenido(uuid, propiedadContenido, storeBusqueda);
//			InputStream inputStream = getContentAsInputStream(url, propiedadContenido);
//			return inputStream;
//		} catch (Throwable e) {
//			throw new AlfrescoException("No es posible obtener el contenido de la propiedad " + propiedadContenido + " del uuid " + uuid, e);
//		}
//	}
	
//	/**
//	 * @deprecated Obtener el contenido de un nodo - NO se ha de usar porque si el usuario no es administrador obtiene el contenido a null
//	 */
//	public InputStream obtenerContenidoPorContentUrl(String contentUrl) throws Exception {	    
//	    
//        try {
//            contentUrl = contentUrl.replaceFirst("\\|.*", "");
//            String ticket = AuthenticationUtils.getTicket();
//            
//            StringBuilder sb = new StringBuilder();
//            sb.append(alfrescoWebService.replaceFirst("/api/?$", ""));
//            sb.append("/dr?");
//            sb.append(contentUrl);
//            sb.append("&ticket=");
//            sb.append(ticket);           
//
//            // Connect to donwload servlet
//            URL url = new URL(sb.toString());
//            URLConnection conn = url.openConnection();
//            return conn.getInputStream();
//        } catch (Exception e) {
//            //throw new AlfrescoException("No es posible obtener el contenido de la propiedad " + propiedad + " de la URL " + content.getUrl(), e);
//            return null;
//        }
//    }
	
	public InputStream obtenerContenidoPorNodeUuid(String nodeUuid) throws Exception {	   
		
		String ticket = AuthenticationUtils.getTicket();
		URL url = null;
		URLConnection conn = null;
		
		// Se intenta recuperar el nodo desde el SpacesStore       
        StringBuilder sb = new StringBuilder();
        sb.append(alfrescoWebService.replaceFirst("/api/?$", ""));
        sb.append("/d/d/workspace/SpacesStore/");
        sb.append(nodeUuid);
        sb.append("/dummy?ticket=");
        sb.append(ticket);  
	    
        try {
            // Connect to donwload servlet
            url = new URL(sb.toString());
            conn = url.openConnection();
            return conn.getInputStream();
        } catch (IOException e) {
        	 // Si no se encontro el nodo se intenta recuperarlo del Version2Store por si existiese ahi 
        }
        
        sb = new StringBuilder();
        sb.append(alfrescoWebService.replaceFirst("/api/?$", ""));
        sb.append("/d/d/workspace/Version2Store/");
        sb.append(nodeUuid);
        sb.append("/dummy?ticket=");
        sb.append(ticket);  
        
        try {
            // Connect to donwload servlet
            url = new URL(sb.toString());
            conn = url.openConnection();
            return conn.getInputStream();
        } catch (IOException e) {
        	throw new AlfrescoException("No es posible obtener el contenido de la URL " + url, e);
        }       
    }

	/**
	 * Obtener version de un nodo
	 * @param ruta
	 * @param versionSolicitada
	 * @return
	 * @throws Exception 
	 */
	public Node obtenerVersion(String ruta, String versionSolicitada) throws Exception {
		// Se obtiene la versi�n del historial de versiones
		Reference referenciaNodoActual = getReferencia(ruta, null);
		VersionHistory versionHistory = getAuthoringService().getVersionHistory(referenciaNodoActual);
		if (versionHistory.getVersions() != null) {
			for (Version version : versionHistory.getVersions()) {
				if (versionSolicitada.equals(version.getLabel())) {
					Reference referenciaNodoVersion = version.getId();
					Predicate predicado = getPredicadoVersion(referenciaNodoVersion);
					Node[] nodos = getNodo(predicado);
					return nodos[0];
				}
			}
		}
		return null;
	}

	/**
	 * Obtener un nodo consultando su version minor
	 * 
	 * La limitaci�n de solo minor se debe a que en versiones de alfresco < 2.1.2, alfresco no permitia crear una version
	 * major ver metodo deprecated crearVersion
	 */
	@Deprecated
	public Node obtenerVersion(String ruta, Integer versionSolicitada) throws Exception {
		// Se obtiene la versi�n del historial de versiones
		Reference referenciaNodoActual = getReferencia(ruta, null);
		VersionHistory versionHistory = getAuthoringService().getVersionHistory(referenciaNodoActual);
		if (versionHistory.getVersions() != null) {
			
			String versionFormatoAlfresco = "1." + Integer.toString(versionSolicitada - 1);
			for (Version version : versionHistory.getVersions()) {
				if (versionFormatoAlfresco.equals(version.getLabel())) {
					Reference referenciaNodoVersion = version.getId();
					Predicate predicado = getPredicadoVersion(referenciaNodoVersion);
					Node[] nodos = getNodo(predicado);
					return nodos[0];
				}
			}
		}
		return null;
	}
	
	
	public List<Node> obtenerVersiones(String ruta) throws Exception {
		
		List<Node> nodosVersiones = new ArrayList<Node>();
		
		Reference referencia = getReferencia(ruta, null);
		
		// Se obtiene la versi�n del historial de versiones
		VersionHistory versionHistory = getAuthoringService().getVersionHistory(referencia);
		Version[] versiones = versionHistory.getVersions();
		if (versiones != null) {
			for (int i = 0; i < versiones.length; i++) {
				Reference reference = versiones[i].getId();
				Predicate predicado = getPredicadoVersion(reference);
				Node[] nodos = getNodo(predicado);
				nodosVersiones.add(nodos[0]);
			}
		}
		
		return nodosVersiones;
	}

	
	public Reference crearNodo(String rutaRelativa, String nombreNodo, String tipoNodo, NamedValue[] propiedades) throws Exception {
		String path = getPathAbsoluto(rutaRelativa);
		return crearNodoAbsoluto(path, nombreNodo, tipoNodo, propiedades);
	}   

	public UpdateResult[] actualizarPropiedades(String ruta, NamedValue[] propiedades) throws Exception {
		Predicate predicado = getPredicado(ruta, null, null, null, Boolean.TRUE);
		CMLUpdate[] cmlUpdates = new CMLUpdate[] {new CMLUpdate(propiedades, predicado, null)};
		CML cml = new CML();
		cml.setUpdate(cmlUpdates);
		return invocarCML(cml);
	}
	
	public UpdateResult[] actualizarPropiedadesRecursivo(String ruta, NamedValue[] propiedades) throws Exception {
	    Predicate predicado = getPredicado(ruta, null, null, null, Boolean.FALSE);
	    CMLUpdate[] cmlUpdates = new CMLUpdate[] {new CMLUpdate(propiedades, predicado, null)};
	    CML cml = new CML();
	    cml.setUpdate(cmlUpdates);
	    return invocarCML(cml);
	}
	
	
	public Reference eliminarNodo(String pathElemento) throws Exception {
		Predicate predicate = getPredicado(pathElemento, null, null, null, Boolean.TRUE);
		return eliminarNodo(predicate);
	}
	

	/**
	 * Elimina un nodo
	 */
	public Reference eliminarNodo(Predicate predicate) throws Exception {
		CML cml = new CML();
		CMLDelete delete = new CMLDelete(predicate);
		cml.setDelete(new CMLDelete[]{delete});
		UpdateResult updateResult[] = invocarCML(cml);
		if (updateResult != null && updateResult.length != 0) {
			return updateResult[0].getSource();
		} else {
			return null;
		}
	}
	
	

  /**
   * Streams content into the repository.  Once done a content details string is returned and this can be used to update 
   * a content property in a CML statement.
   * 
   * @param stream  the file to stream into the repository
   * @param host  the host name of the destination repository
   * @param port  the port name of the destination repository
   * @param mimetype the mimetype of the file, ignored if null
   * @param encoding the encoding of the file, ignored if null
   * @return      the content data that can be used to set the content property in a CML statement  
   */
  @Deprecated
  public String guardarContenido(InputStream stream, String fileName, String mimetype, String encoding) {
		try {
	        String result = null;
	        String host = WebServiceFactory.getHost(webServiceFactoryConfig);
	        int port = WebServiceFactory.getPort(webServiceFactoryConfig);
	        if (port == -1) {
	            if (alfrescoWebService.startsWith("http://")) {
	                port = 80; 
	            } else {
	                port = 443;
	            }
	        }
	        
            StringBuilder requestSB = new  StringBuilder();
            requestSB.append("PUT ");
            requestSB.append(new URL(alfrescoWebService).getPath().replaceFirst("/api/?$", ""));
            requestSB.append("/upload/");
            requestSB.append(URLEncoder.encode(fileName, "UTF-8"));
            requestSB.append("?ticket=");
            requestSB.append(AuthenticationUtils.getTicket());
            if (mimetype != null) {
                requestSB.append("&mimetype=").append(mimetype);
            }
            if (encoding != null) {
                requestSB.append("&encoding=").append(encoding);
            }            
            requestSB.append(" HTTP/1.1\n");
            
            requestSB.append("Cookie: JSESSIONID=").append(AuthenticationUtils.getAuthenticationDetails().getSessionId()).append(";\n");
            requestSB.append("Content-Length: ").append(stream.available()).append("\n");
            requestSB.append("Host: ").append(host).append(":").append(port).append("\n");
            requestSB.append("Connection: Keep-Alive\n\n");
            String request = requestSB.toString();
            
            // Open sockets and streams
            Socket socket = new Socket(host, port);
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            DataInputStream is = new DataInputStream(socket.getInputStream());

            try {
                if (socket != null && os != null && is != null) {
                    // Write the request header
                    os.writeBytes(request);

                    // Stream the content onto the server
                    int byteCount = 0;
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int bytesRead = -1;
                    while ((bytesRead = stream.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                        byteCount += bytesRead;
                    }
                    os.flush();
                    stream.close();

                    // Read the response and deal with any errors that might occur
                    boolean firstLine = true;
                    String responseLine;
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    while ((responseLine = br.readLine()) != null) {
                        if (firstLine == true) {
                            if (responseLine.contains("200") == true) {
                                firstLine = false;
                            } else if (responseLine.contains("401") == true) {
                                throw new RuntimeException("Content could not be uploaded because invalid credentials have been supplied.");
                            } else if (responseLine.contains("403") == true) {
                                throw new RuntimeException("Content could not be uploaded because user does not have sufficient priveledges.");
                            } else {
                                throw new RuntimeException("Error returned from upload servlet (" + responseLine + ")");
                            }
                        } else if (responseLine.contains("contentUrl") == true) {
                            result = responseLine;
                            break;
                        }
                    }
                }
            } finally {
                try {
                    // Close the streams and socket
                    if (os != null) {
                        os.close();
                    }
                    if (is != null) {
                        is.close();
                    }
                    if (socket != null) {
                        socket.close();
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error closing sockets and streams", e);
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Error writing content to repository server", e);
        }
    }
  
  
  /**
   * Metodo REST para subir archivos (funciona para archivos grandes).
   * <p>
   * Usar este metodo par subir el contenido de un archivo a un documento.
   *
   * @param   inputStream Contenido del archivo a subir.
   * @param   fileName Nombre del archivo de contenido a subir
   * @param   mimetype Tipo mime del contenido. Si no se especifica ninguno
   * 		  alfresco lo tratar� como un stream (application/octet-stream) por defecto.
   * @param   timeout Establecer un timeout especifico (en milisegundos)        
   * @return  contentUrl Url del contenido creado.
   * @since   2.1.2
   */
  public String guardarContenido(InputStream inputStream, String fileName, String mimetype, String encoding, int timeout) {
	  String result = null;
     
      StringBuilder requestSB = new  StringBuilder();
      try {     
		  requestSB.append(alfrescoWebService.replaceFirst("/api/?$", ""));
	      requestSB.append("/upload/");
	      requestSB.append(URLEncoder.encode(fileName, "UTF-8"));
	      requestSB.append("?ticket=");
	      requestSB.append(AuthenticationUtils.getTicket());
	  } catch (UnsupportedEncodingException e) {
		  throw new RuntimeException("Unsupported Encoding", e);
	  }
      
      if (mimetype != null) {
          requestSB.append("&mimetype=").append(mimetype);
      }
      if (encoding != null) {
          requestSB.append("&encoding=").append(encoding);
      }            

      URL url;
      HttpURLConnection con = null;
//      OutputStreamWriter outStreamWriter = null;
      try {
          url = new URL(requestSB.toString());
          con = (HttpURLConnection) url.openConnection();
          con.setRequestMethod("PUT");
          con.setDoOutput(true);
          con.setUseCaches(false);
          con.setAllowUserInteraction(false);
          con.setConnectTimeout(timeout);
//          con.setFixedLengthStreamingMode(2);
          
      }
      catch (MalformedURLException ex) {
          throw new RuntimeException("Error url invalida", ex);
      }
      catch (ProtocolException ex) {
          throw new RuntimeException("Error conectado a la url de upload", ex);
      }
      catch (IOException ex) {
          throw new RuntimeException("Error conectado a la url de upload", ex);
      }

      try {
//    	  outStreamWriter = new OutputStreamWriter(con.getOutputStream());
//    	  IOUtils.copy(inputStream, outStreamWriter);
//		  outStreamWriter.flush();
    	  IOUtils.copy(inputStream, con.getOutputStream());
		  

  		// RESPUESTA
  		if (con instanceof HttpURLConnection) {
  		   HttpURLConnection httpConnection = (HttpURLConnection) con;
  		   if (httpConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
  			 throw new RuntimeException("Error el servidor ha respondido: [HTTP response code: " + httpConnection.getResponseCode() + ", HTTP response message: " + httpConnection.getResponseMessage() + "]");
  		   }
  		   else {
	  	        String responseLine;
	  	        BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(con.getInputStream())));
	  	        while ((responseLine = br.readLine()) != null) {
	  	            if (responseLine.contains("contentUrl") == true) {
	  	                result = responseLine;
	  	                break;
	  	            }
	  	        }
  		   }
  		}
  		else {
  			 throw new RuntimeException("Error enviando archivo");
  		}
  		
  		return result;
      }
      catch (IOException ex) {
          throw new RuntimeException("Error enviando archivo", ex);
      }
      finally {
//    	  IOUtils.closeQuietly(outStreamWriter);
    	  IOUtils.closeQuietly(inputStream);
      }

  }
  
  /**
   * Crea una version nueva del nodo, indicando si es major o minor
   * @param ubicacion
   * @param comentario
   * @param version
   * @return
   * @throws Exception
   */
	public String crearVersion(String ubicacion, String comentario, AlfrescoVersionType version) throws Exception {
		Predicate predicado = getPredicado(ubicacion, null, null, null, true);
		
		VersionResult versionRes = getAuthoringService().createVersion(predicado, new NamedValue[]{
				  new NamedValue(AlfrescoConstants.VERSION_TYPE, false, version.name(), null),			
				  new NamedValue(AlfrescoConstants.VERSION_DESCRIPCION, false, comentario, null)},											
				false);
		
		return versionRes.getVersions()[0].getLabel(); // N�mero de versi�n
	}
  
	
	/**
	 * Crea una nueva versi�n del nodo
	 * 
	 * Este metodo solo creaba una version minor ya que en versiones anteriores de alfresco a la 2.1.2 no eprmitia
	 * crear versiones major ver bug corregido http://wiki.alfresco.com/wiki/Release_2.1.2  AR-1863
	 */
  	@Deprecated
	public String crearVersion(String ubicacion, String comentario) throws Exception {
		Predicate predicado = getPredicado(ubicacion, null, null, null, true);
		VersionResult version = getAuthoringService().createVersion(predicado, new NamedValue[]{
			  	//new NamedValue("versionType", false, VersionType.MAJOR.name(), null),	// no funciona! Mirar 'http://issues.alfresco.com/browse/AR-1863'				
				  new NamedValue(AlfrescoConstants.VERSION_DESCRIPCION, false, comentario, null)},											
				false);
		
		return version.getVersions()[0].getLabel(); // N�mero de versi�n
	}

  	/**
  	 * Restaurar la versi�n de un nodo y ponerla como copia actual de trabajo
  	 * Se indica la ruta del nodo y el Label de la version de alfresco
  	 */
  	public void restaurarVersion(String ruta, String versionSolicitada) throws Exception {
  		Reference ref = getReferenciaAbsoluto(getPathAbsoluto(ruta),null);
  		getAuthoringService().revertVersion(ref,versionSolicitada);
  	}
  	
//	/**
//	 * Crea el enlace de un nodo
//	 */
//	public Reference crearEnlace(Reference nodoOrigen, String nombreNodo, String pathRelativoDestino, String propiedadDestination, String propiedadEnlace) throws Exception {
//		NamedValue[] propiedades = new NamedValue[2]; 
//		propiedades[0] = new NamedValue(Constants.PROP_NAME, false, nombreNodo, null);
//		// Hay que poner la propiedad destination como referencia al UUID del nodo
//		String destination = getPathNodoEnWorkspace(nodoOrigen.getUuid());
//		propiedades[1] = new NamedValue(propiedadDestination, false, destination, null); 
//		
//		return crearNodo(pathRelativoDestino, nombreNodo, propiedadEnlace, propiedades);
//	}	

//	/**
//	 * Crea un nodo de tipo 'carpeta'.
//	 * No se comprueba que no exista la carpeta
//	 * El path es relativo
//	 */
//	public Reference crearCarpeta(String pathRelativoPadre, String nombreCarpeta) throws Exception {
//		NamedValue[] propiedades = new NamedValue[]{new NamedValue(Constants.PROP_NAME, false, nombreCarpeta, null)};
//		return crearNodo(pathRelativoPadre, nombreCarpeta, Constants.TYPE_FOLDER, propiedades);
//	}
//	
//	/**
//	 * Crea nodos de tipo 'carpeta' que se indican en 'rutaCarpetasCrear', en caso de que no existan. Se crean bajo el directorio
//	 * 'pathPadre'
//	 * El path es relativo
//	 */
//	public Reference crearCarpetas(String pathPadre, String rutaCarpetasCrear) throws Exception {
//		String pathAbsolutoPadre = getPathAbsoluto(pathPadre);
//		if (!pathAbsolutoPadre.endsWith(SEPARADOR)) {
//			pathAbsolutoPadre += SEPARADOR;
//		}
//		return obtenerYCrearReferenciaPath(pathAbsolutoPadre, rutaCarpetasCrear, true, false);
//	}		
	
	/**
	 * Path relativo
	 * @throws AlfrescoException 
	 * @throws RemoteException 
	 */
	private Predicate getPredicado(String[] pathRelativoPadre, String nombreNodoPath, String nombreNodoPropiedad,
								   String[] propiedadNombre, String tipoNodo, String[] condicionesAdicionales,
								   Boolean pathYaCompleto) throws Exception {
		String[] pathAbsolutoPadre = new String[pathRelativoPadre.length];
		for (int i = 0; i < pathRelativoPadre.length; i++) {
			pathAbsolutoPadre[i] = getPathAbsoluto(pathRelativoPadre[i]);
		}
		return getPredicadoAbsoluto(pathAbsolutoPadre, nombreNodoPath, nombreNodoPropiedad, propiedadNombre, tipoNodo, condicionesAdicionales, pathYaCompleto);
	}
	
//	private Predicate getPredicado(String pathRelativoPadre, String nombreNodoPath, String nombreNodoPropiedad, String[] propiedadNombre, String tipoNodo, String[] condicionesAdicionales, Boolean pathYaCompleto) throws Exception {
//		return getPredicado(new String[]{pathRelativoPadre}, nombreNodoPath, nombreNodoPropiedad, propiedadNombre, tipoNodo, condicionesAdicionales, pathYaCompleto);
//		
//	}
//

//	public Predicate getPredicado(String[] pathRelativoPadre, String nombreNodoPath, String tipoNodo, String[] condicionesAdicionales, Boolean pathYaCompleto) throws Exception {
//		return getPredicado(pathRelativoPadre, nombreNodoPath, null, null, tipoNodo, condicionesAdicionales, pathYaCompleto);
//	}
	
	public Predicate getPredicado(String pathRelativoPadre, String nombreNodoPath, String tipoNodo,
								  String[] condicionesAdicionales, 
								  Boolean pathYaCompleto) throws Exception {
		return getPredicado(new String[]{pathRelativoPadre}, nombreNodoPath, null, null, tipoNodo, condicionesAdicionales, pathYaCompleto);
		
	}

	
//	private Predicate getPredicadoAbsoluto(String pathAbsolutoPadre, String nombreNodoPath, String nombreNodoPropiedad, String[] propiedadNombre, String tipoNodo, String[] condicionesAdicionales, Boolean pathYaCompleto) throws Exception {
//		return getPredicadoAbsoluto(new String[] {pathAbsolutoPadre}, nombreNodoPath, nombreNodoPropiedad, propiedadNombre, tipoNodo, condicionesAdicionales, pathYaCompleto);
//	}
	
	private Predicate getPredicadoAbsoluto(String[] pathAbsolutoPadre, String nombreNodoPath, String nombreNodoPropiedad,
										   String[] propiedadNombre, String tipoNodo, String[] condicionesAdicionales,
										   Boolean pathYaCompleto) throws Exception {
		if (!StringUtils.isBlank(nombreNodoPath) && !StringUtils.isBlank(nombreNodoPropiedad)) {
			throw new AlfrescoException("No pueden ser tanto 'nombreNodoPath' como 'nombreNodoPropiedad' distintos de null");
		}
		
		Query query = null;
		if (pathYaCompleto != null && pathYaCompleto) {
			query = getQuery(pathAbsolutoPadre, tipoNodo, false, condicionesAdicionales);
		} else if (!StringUtils.isBlank(nombreNodoPath)) {
			String[] paths = new String[pathAbsolutoPadre.length];
			for (int i = 0; i < pathAbsolutoPadre.length; i++) {
				paths[i] = pathAbsolutoPadre[i] + AlfrescoConstants.SEPARADOR + nombreNodoPath;
			}
			query = getQuery(paths, tipoNodo, false, condicionesAdicionales);
		} else if (!StringUtils.isBlank(nombreNodoPropiedad)) {
			query = getQuery(pathAbsolutoPadre, nombreNodoPropiedad, propiedadNombre, tipoNodo, false, condicionesAdicionales);
		} else {
			// B�squeda recursiva
			query = getQuery(pathAbsolutoPadre, tipoNodo, true, condicionesAdicionales);
		}
		
		Predicate predicate = new Predicate(null, getStore(), query);
		return predicate;
	}
	
//	public Predicate getPredicado(Reference referencia) throws Exception {
//		Predicate predicado = new Predicate(new Reference[]{referencia}, getStore(), null);
//		return predicado;
//	}
//	
	public Predicate getPredicadoVersion(final Reference referencia) throws Exception {
	    Predicate predicado = new Predicate(new Reference[]{referencia}, getStoreVersion(), null);
	    return predicado;
    }

	/**
	 * Obtiene los nodos de un path dado, teniendo en cuenta el nombre del nodo
	 */
	private Node[] getNodos(String ubicacion, String nombreNodoPath, String nombreNodoPropiedad, String[] propiedadNombre, String tipoNodo, Boolean ubicacionCompleta) throws Exception {
		return getNodos(new String[]{ubicacion}, nombreNodoPath, nombreNodoPropiedad, propiedadNombre, tipoNodo, ubicacionCompleta);
	}

	/**
	 * Obtiene los nodos de un path dado, teniendo en cuenta el path del nodo
	 */
	public Node[] getNodos(String ubicacion, String nombreNodoPath, String tipoNodo, Boolean ubicacionCompleta) throws Exception {
		return getNodos(ubicacion, nombreNodoPath, null, null, tipoNodo, ubicacionCompleta);
	}

	
	/**
	 * Obtiene los nodos de un path dado, teniendo en cuenta el nombre del nodo
	 */
	private Node[] getNodos(String[] ubicacion, String nombreNodoPath, String nombreNodoPropiedad,
							String[] propiedadNombre, String tipoNodo,
							Boolean ubicacionCompleta) throws Exception {
		if (!StringUtils.isBlank(nombreNodoPath) && !StringUtils.isBlank(nombreNodoPropiedad)) {
			throw new AlfrescoException("No pueden ser tanto 'nombreNodoPath' como 'nombreNodoPropiedad' distintos de null");
		}
		Predicate predicado = getPredicado(ubicacion, nombreNodoPath, nombreNodoPropiedad, propiedadNombre, tipoNodo, null, ubicacionCompleta);
		Node[] nodes = getRepositoryService().get(predicado);
		return nodes;
	}	
	
//	/**
//	 * Obtiene los nodos de un path dado, teniendo en cuenta el path del nodo
//	 */
//	public Node[] getNodos(String[] ubicacion, String nombreNodoPath, String tipoNodo, Boolean ubicacionCompleta) throws Exception {
//		return getNodos(ubicacion, nombreNodoPath, null, null, tipoNodo, ubicacionCompleta);
//	}
//	/**
//	 * Obtiene todos los nodos hijos de un path dado
//	 */
//	public Node[] getNodosHijos(String pathRelativo, String tipoNodo) throws Exception {
//		return getNodosHijos(pathRelativo, tipoNodo, null);
//	}
//
//	/**
//	 * Obtiene todos los nodos hijos de un path dado, teniendo en cuenta una condici�n dada
//	 */
//	public Node[] getNodosHijos(String pathRelativo, String tipoNodo, String[] condicionesAdicionales) throws Exception {
//		String pathAbsoluto = getPathAbsoluto(pathRelativo);
//		Query query = getQuery(pathAbsoluto, tipoNodo, true, condicionesAdicionales);
//		Predicate predicado = new Predicate(null, getStore(), query);
//		Node[] nodes = getRepositoryService().get(predicado);
//		return nodes;
//	}
//	/**
//	 * Obtiene el nodo con el UUIDs indicado, busc�ndolos en el store por defecto.
//	 * Adem�s, si se indica, debe cumplir la condici�n dada
//	 */
//	public Node getNodo(String nodeUuid, String condicion) throws Exception {
//		return getNodo(nodeUuid, condicion, getStore());		
//	}
//	
//	public Node getNodo(String nodeUuid, String condicion, Store store) throws Exception {
//        List<String> nodeUuids = new ArrayList<String>();
//        nodeUuids.add(nodeUuid);
//        return getNodos(nodeUuids, new String[]{condicion}, store)[0];	    
//	}
//	
//	/**
//	 * Obtiene los nodos con los UUIDs indicados, busc�ndolos en el store por defecto.
//	 * Adem�s, si se indican, deben cumplir las condiciones dadas
//	 */
//	public Node[] getNodos(List<String> nodeUuids, String[] condiciones) throws Exception {
//	    return getNodos(nodeUuids, condiciones, getStore());
//	}
//	
//	public Node[] getNodos(List<String> nodeUuids, String[] condiciones, Store store) throws Exception {
//        StringBuffer queryUUID = new StringBuffer("(");
//        int i = 0;      
//        for (String nodeUuid : nodeUuids) {
//            if (i != 0) {
//                queryUUID.append(" OR ");
//            } 
//            queryUUID.append(getQuery(AlfrescoConstants.NAMESPACE_SYSTEM_MODEL_PREFIX, AlfrescoConstants.UUID_ALFRESCO, nodeUuid));
//            i++;
//        }       
//        queryUUID.append(")");
//        
//        StringBuffer consultaCondicion = getConsultaCondicion(condiciones);
//        Query query = new Query(Constants.QUERY_LANG_LUCENE, queryUUID + consultaCondicion.toString());
//        Predicate predicado = new Predicate(null, store, query);
//        Node[] nodes = getRepositoryService().get(predicado);
//        
//        return nodes;	    
//	}
//	
////	/**
////	 * Obtiene los nodos con los UUIDs indicados, busc�ndolos en el store por defecto
////	 */
////	public Node[] getNodos(List<String> nodeUuids) throws Exception {
////		int i = 0;
////		Reference[] referencias = new Reference[nodeUuids.size()];
////		for (String nodeUuid : nodeUuids) {
////			referencias[i] = new Reference(getStore(), nodeUuid, null);
////			i++;
////		}		
////		Predicate predicado = new Predicate(referencias, getStore(), null);
////		Node[] nodes = getRepositoryService().get(predicado);
////		
////		return nodes;
////	}
//
//	/**
//	 * Comprueba si existe un nodo en un path dado
//	 * El path es absoluto
//	 */
//	private boolean existeNodoPath(String pathPadreAbsoluto, String nombreNodoPath, String tipoNodo) throws Exception {
//		String pathAbsoluto = pathPadreAbsoluto + AlfrescoConstants.SEPARADOR + nombreNodoPath;
//		Reference reference = getReferenciaAbsoluto(pathAbsoluto, tipoNodo);
//		return reference != null;
//	}
//	
//	/**
//	 * Comprueba si existe un nodo con un nombre dado bajo un path dado
//	 */
//	private boolean existeNodoName(String pathPadreAbsoluto, String nombreNodoPropiedad, String[] propiedadNombre, String tipoNodo) throws Exception {
//		Reference reference = getReferenciaAbsoluto(pathPadreAbsoluto, tipoNodo, nombreNodoPropiedad, propiedadNombre);
//		return reference != null;
//	}
//	
//	/**
//	 * Path relativo
//	 */
//	private boolean existeNodo(String pathPadreRelativo, String nombreNodoPath, String nombreNodoPropiedad, String[] propiedadNombre, String tipoNodo) throws Exception {
//		if (StringUtils.isBlank(pathPadreRelativo)) {
//			throw new IllegalArgumentException("El path del padre no puede ser nulo");
//		}
//		
//		if (!StringUtils.isBlank(nombreNodoPropiedad) && (propiedadNombre == null || propiedadNombre.length != 2)) {
//			throw new IllegalArgumentException("Si se busca por el nombre del nodo se ha de indicar cu�l es la propiedad que contiene el nombre");
//		}
//		
//		if (!StringUtils.isBlank(nombreNodoPath)) {
//			String pathAbsolutoNodo = getPathAbsoluto(pathPadreRelativo);
//			if (existeNodoPath(pathAbsolutoNodo, nombreNodoPath, tipoNodo)) {
//				return true;
//			}
//		}
//		if (!StringUtils.isBlank(nombreNodoPropiedad)) {
//			String pathAbsolutoPadre = getPathAbsoluto(pathPadreRelativo);
//			if (existeNodoName(pathAbsolutoPadre, nombreNodoPropiedad, propiedadNombre, tipoNodo)) {
//				return true;
//			}
//		}
//		
//		return  false;
//	}
//	
//	/**
//	 * Path relativo
//	 */
//	public boolean existeNodo(String pathPadreRelativo, String nombreNodoPath, String tipoNodo) throws Exception {
//		return existeNodo(pathPadreRelativo, nombreNodoPath, null, null, tipoNodo);
//	}

//	
//	public UpdateResult[] actualizarNodo(Predicate predicado, NamedValue[] propiedades, List<ContenidoAlfresco> contenidos) throws Exception {
//        CML cml = new CML();
//        CMLUpdate[] cmlUpdates = new CMLUpdate[] {crearActualizacionPropiedades(predicado, propiedades)};
//        cml.setUpdate(cmlUpdates);
//        
//        // Se escribe el contenido del nodo en el caso de que se pase por par�metros
//        if (contenidos != null && contenidos.size() != 0) {
//            int i = 0;
//            cml.setWriteContent(new CMLWriteContent[contenidos.size()]);
//            for (ContenidoAlfresco contenido : contenidos) {
//                ContentFormat cf = new ContentFormat(contenido.getTipoMime(), AlfrescoConstants.ENCODING_CONTENIDO);
//                CMLWriteContent write = new CMLWriteContent(contenido.getPropiedadNodo(), contenido.getContenido(), cf, predicado, null);
//                cml.setWriteContent(i, write);
//                i++;
//            }
//        }
//	    return actualizar(cml);
//	}

//	public UpdateResult[] actualizarContenido(Predicate predicado, ContenidoAlfresco contenido) throws Exception {
//		CMLWriteContent[] cmlWriteContents = new CMLWriteContent[] {crearActualizacionContenido(predicado, contenido)};
//		CML cml = new CML();
//		cml.setWriteContent(cmlWriteContents);
//		return actualizar(cml);
//	}	
//

//	/**
//	 */
//	public CMLWriteContent crearActualizacionContenido(Predicate predicado, ContenidoAlfresco contenido) throws Exception {
//		ContentFormat cf = new ContentFormat(contenido.getTipoMime(), AlfrescoConstants.ENCODING_CONTENIDO);
//        CMLWriteContent write = new CMLWriteContent(contenido.getPropiedadNodo(), contenido.getContenido(), cf, predicado, null);
//		return write;
//	}		
//	
//	
//	/**
//	 */
//	public void eliminarPropiedad(Predicate predicado, String propiedad) throws ContentFault, RemoteException {
//		getContentService().clear(predicado, propiedad);
//	}	
//	
//	
//	/**
//	 * 
//	 */
//	public CMLDelete crearEliminarNodo(Reference referencia) throws Exception {
//		Predicate predicate = getPredicado(referencia);
//		CMLDelete delete = new CMLDelete(predicate);
//		return delete;		
//	}
//	
//
//	
//	public CMLMove[] crearMoverNodos(List<DocumentoCML> documentosCML) throws Exception {
//		CMLMove[] cmlMoves = new CMLMove[documentosCML.size()];
//		if (documentosCML != null && documentosCML.size() != 0) {
//			int i = 0;
//			for (DocumentoCML documento : documentosCML) {
//				// Nombre del nodo en la forma "{http://...}nombreNodo"
//				String nombreNodoConQname = AlfrescoConstants.createQNameString(getNamespaceModel(), documento.getIdDocumento());
//				
//				// Referencia al nodo padre destino
//				String pathAbsolutoDestino = getPathAbsoluto(documento.getDestino());
//				Reference nodoPadre = getReferenciaAbsoluto(pathAbsolutoDestino, null);
//				String tipoAsociacion = getTipoAsociacion(nodoPadre);
//				ParentReference parentReferenceDestino = new ParentReference(getStore(), nodoPadre.getUuid(), null, tipoAsociacion, nombreNodoConQname);
//				
//			    CMLMove cmlMove = new CMLMove(); 
//			    cmlMove.setTo(parentReferenceDestino); 
//			    // Se indica d�nde est� ubicado el documento actualmente
//			    if (documento.getOrigen() != null) {
//			    	cmlMove.setWhere(getPredicado(documento.getOrigen(), documento.getIdDocumento(), null, null, null, null, null));
//			    } else if (documento.getOrigenReference() != null) {
//			    	cmlMove.setWhere(getPredicado(documento.getOrigenReference()));
//			    } else {
//			    	throw new IllegalArgumentException("No pueden ser 'origen' y 'origenReference' ambos null");
//			    }
//				cmlMoves[i] = cmlMove;
//				i++;
//				
//			}   
//		}
//		return cmlMoves;
//	}
//	
//	public UpdateResult[] moverNodos(List<DocumentoCML> documentosCML) throws Exception {
//		CMLMove[] cmlMoves = crearMoverNodos(documentosCML);
//		CML cml = new CML();
//		cml.setMove(cmlMoves);
//		return actualizar(cml);
//	}
//	
//	
//	public UpdateResult moverNodo(DocumentoCML documentoCML) throws Exception {
//		List<DocumentoCML> documentosCML = new ArrayList<DocumentoCML>();
//		documentosCML.add(documentoCML);
//		UpdateResult[] resultados = moverNodos(documentosCML);
//		return resultados != null && resultados.length != 0 ? resultados[0] : null;
//	}
//	

//	public ContenidoAlfresco getContenido(Reference referencia, String propiedadContenido) throws Exception {
//		
//		Content[] contenido = getContentService().read(new Predicate(new Reference[]{referencia}, getStore(), null), propiedadContenido);
//		ContenidoAlfresco alfrescoContenido = null; 
//		if (contenido[0].getLength() != 0) {
//			try {
//				InputStream inputStream = getContentAsInputStream(contenido[0], propiedadContenido);
//				
//				byte[] contenidoBytes = null;
//				if (inputStream != null) {
//					contenidoBytes = StreamUtil.toByteArray(inputStream);
//				}
//				alfrescoContenido = new ContenidoAlfresco(contenido[0].getFormat().getMimetype(), contenidoBytes, propiedadContenido);
//			} catch (Exception e) {
//				throw new AlfrescoException("No es posible obtener el contenido de la propiedad " + propiedadContenido + " de la URL " + contenido[0].getUrl(), e);
//			}
//		} 
//		return alfrescoContenido;
//	}
//
//	private String formatearCategoria(String categoria) {
//		return categoria.replaceAll(" ", "_x0020_");
//	}
//	
//	/**
//	 * Para consultar los miembros:
//	 * 	Query query = new Query(Constants.QUERY_LANG_LUCENE, "PATH:\"/app:company_home/cm:categoryRoot/cm:generalclassifiable/cm:Tipos_x0020_de_x0020_documentos//member\"");		
//	 *  getRepositoryService().query(getStore(), query, false);
//	 */
//	public void categorizar(Predicate predicado, List<String> categorias) throws ClassificationFault, RemoteException {
//		if (categorias.size() == 0) {
//			return;
//		}
//		int i = 0;
//		Store spacesStore = new Store(Constants.WORKSPACE_STORE, AlfrescoConstants.STORE_SPACESSTORE);
//		AppliedCategory appliedCategory = new AppliedCategory();
//        appliedCategory.setClassification(AlfrescoConstants.CLASIFICACION_GENERAL_CLASSIFIABLE);
//        appliedCategory.setCategories(new Reference[categorias.size()]);        
//		for (String categoria : categorias) {
//			Reference referencia = new Reference(spacesStore, null, CATEGORIAS_RAIZ + "/cm:" + getCategoriaTiposDocumentos() + "/cm:" + formatearCategoria(categoria));        
//	        appliedCategory.setCategories(i, referencia);
//			i++;
//		}
//        
//        // Se aplican las categor�as
//        getClassificationService().setCategories(predicado, new AppliedCategory[] {appliedCategory});
//	}
//	
//    
//    public Content obtenerContenidoDescripcion(Reference referencia, String propiedadContenido) throws Exception {
//        Content[] contenido = getContentService().read(new Predicate(new Reference[]{referencia}, getStore(), null), propiedadContenido);
//        if (contenido[0].getLength() != 0) {
//            return contenido[0];
//        }
//        return null;
//    }
//	
//	/* ********************************************************************************************************************************* */
//	/* ******************************************** M�todos privados ******************************************************************* */
//	/* ********************************************************************************************************************************* */	
	   
    private String obtenerUrlContenido(String uuid, String propiedadContenido, Store storeBusqueda) throws Exception {
        // Predicado del nodo versi�n (en el store de versiones)
        String queryUuid = getQuery(AlfrescoConstants.NAMESPACE_SYSTEM_MODEL_PREFIX, AlfrescoConstants.UUID_ALFRESCO, uuid, null, false);
        Predicate predicado = new Predicate(null, storeBusqueda, new Query(Constants.QUERY_LANG_LUCENE, queryUuid));
        // Contenido
        Content[] contenido = getContentService().read(predicado, propiedadContenido);
        if (contenido != null && contenido[0].getLength() != 0) {
            try {
               return contenido[0].getUrl();
            } catch (Throwable e) {
                throw new AlfrescoException("No es posible obtener el contenido de la propiedad " + propiedadContenido + " de la URL " + contenido[0].getUrl(), e);
            }
        } 
        return null;
    }
    
	
//	/**
//	 * By default, the servlet assumes that the content is on the {http://www.alfresco.org/model/content/1.0}content content property. If you require the content of a different specific model property, provide the fully qualified name of the property as an additional URL argument called property
//	 */
//	private InputStream getContentAsInputStream(String urlContenido, String propiedad) throws AlfrescoException {
//
//		String ticket = AuthenticationUtils.getTicket();
//		String strUrl = urlContenido + "?ticket=" + ticket;
//		
//		if (!StringUtils.isBlank(propiedad)) {
//			strUrl += "&property=" + propiedad;   //ojo! si no se pusiese el ticket en la URL ser�a ?property=... en vez de &property=...
//		}
//		
//		try {
//			// Connect to donwload servlet
//			URL url = new URL(strUrl);
//			URLConnection conn = url.openConnection();
//			return conn.getInputStream();
//		} catch (Exception e) {
//			//throw new AlfrescoException("No es posible obtener el contenido de la propiedad " + propiedad + " de la URL " + content.getUrl(), e);
//			return null;
//		}
//	}

	
//	public String[] obtenerPropiedadMultivaluada(Predicate predicado, String nombrePropiedad) throws Exception {
//		Node[] nodes = getRepositoryService().get(predicado);
//		if (nodes.length == 1) {
//			NamedValue[] propiedades =  nodes[0].getProperties();
//			String[] valores = AlfrescoUtils.obtenerPropiedadValueMultivaluada(propiedades, nombrePropiedad);
//			return valores;
//		} else if (nodes.length > 1) {
//			throw new AlfrescoException("Al leer una propiedad, no es posible obtener m�s de un nodo como resultado de la consulta");
//		}
//		return null;
//	}
//	
//	public String obtenerPropiedad(Predicate predicado, String nombrePropiedad) throws Exception {
//		String[] valores = obtenerPropiedadMultivaluada(predicado, nombrePropiedad);
//		return (valores != null && valores.length != 0) ? valores[0] : null;
//	}
//	
//	public String obtenerPropiedad(String ubicacion, String nombrePropiedad) throws Exception {
//		Predicate predicado = getPredicado(ubicacion, null, null, null, true);
//		return obtenerPropiedad(predicado, nombrePropiedad);
//	}
//
//	
//	private NamedValue[] obtenerPropiedadesVersion(String uuid) throws Exception {
//		
//		// Nota: Es necesario crear esta referencia, porque en la que se pasa por par�metros tiene como scheme del store 'versionStore', 
//		// y en realidad es 'workspace',
//		Reference referenciaVersion = new Reference(getStoreVersion(), uuid, null); 
//			
//		QueryResult r = getRepositoryService().queryChildren(referenciaVersion);
//		if (r.getResultSet().getRows() == null || r.getResultSet().getRows().length == 0) {
//			return null;
//		}
//		
//		List<NamedValue> propiedadesVersionList = new ArrayList<NamedValue>();
//		
//		for (ResultSetRow resultSetRow : r.getResultSet().getRows()) {
//			if (resultSetRow.getNode().getType().equals(AlfrescoConstants.VERSION_PROPIEDAD_QNAME)) {
//				String nombre = null;
//				String valor = null;
//				String[] valores = null;
//				boolean isMultivalue = false;
//			
//				for (NamedValue propiedadNamedValue : resultSetRow.getColumns()) {
//					if (propiedadNamedValue.getName().equals(AlfrescoConstants.VERSION_ATTRIBUTES_NAME_QNAME)) {						
//						nombre = propiedadNamedValue.getValue();			
//					} else if (propiedadNamedValue.getName().equals(AlfrescoConstants.VERSION_ATTRIBUTES_VALUE_QNAME)) {
//						valor = propiedadNamedValue.getValue();
//					} else if (propiedadNamedValue.getName().equals(AlfrescoConstants.VERSION_ATTRIBUTES_VALUES_QNAME)) {
//						valores = propiedadNamedValue.getValues();
//					} else if (propiedadNamedValue.getName().equals(AlfrescoConstants.VERSION_ATTRIBUTES_ISMULTIVALUE_QNAME)) {
//						isMultivalue = Boolean.parseBoolean(propiedadNamedValue.getValue());
//					} 
//				}
//				propiedadesVersionList.add(new NamedValue(nombre, isMultivalue, valor, valores)); 
//			}
//		}
//	
//		NamedValue[] propiedadesVersion = new NamedValue[propiedadesVersionList.size()];
//		propiedadesVersionList.toArray(propiedadesVersion);
//		
//		return propiedadesVersion;
//	}
	
	
	
    private CMLCreate crearCMLCreateAbsoluto(String pathAbsoluto, String nombreNodo, String tipoNodo, NamedValue[] propiedades) throws Exception {
        
        // Nombre del nodo en la forma "{http://...}nombreNodo"
        String nombreNodoConQname = AlfrescoPrivadoUtils.getNombreNodoConQname(nombreNodo, namespacesModelo, getNamespaceModel());
        
        // Se obtiene la referencia del nodo padre      
        //Reference nodoPadre = getReferenciaAbsoluto(pathAbsoluto, null);
        Reference nodoPadre = getReferenciaAbsoluto(pathAbsoluto, null);
        
        String tipoAsociacion = getTipoAsociacion(nodoPadre);
        ParentReference parentReference = new ParentReference(getStore(), nodoPadre.getUuid(), null, tipoAsociacion, nombreNodoConQname); // por aqu� se busca el directorio al utilizar XPath
        
        // Para crear el nodo       
        return new CMLCreate(nombreNodoConQname, parentReference, null, null, null, tipoNodo, propiedades);
    }

	
	/**
	 * Path absoluto
	 * Crea un nodo bajo un nodo padre.
	 * En este m�todo no se comprueba que el nodo no exista
	 */
	private Reference crearNodoAbsoluto(String pathAbsoluto, String nombreNodo, String tipoNodo, NamedValue[] propiedades) throws Exception {
	    CMLCreate create = crearCMLCreateAbsoluto(pathAbsoluto, nombreNodo, tipoNodo, propiedades);
		CML cml = new CML();
		cml.setCreate(new CMLCreate[]{create});
		
		// Se crea el nodo 
		UpdateResult[] updateResult = invocarCML(cml);
		if (updateResult != null && updateResult.length != 0) {
			return updateResult[0].getDestination();
		} else {
			return null;
		}
	}
	
	/**
	 * El tipo de asociaci�n del nodo root del store es de tipo ASSOC_CHILDREN, mientras que para el resto de carpetas de tipo nt:folder
	 * es ASSOC_CONTAINS
	 */
	private String getTipoAsociacion(Reference nodo) throws Exception {
		if (nodo.getUuid().equals(getReferenceStore().getUuid())) {
			return Constants.ASSOC_CHILDREN;
		} else {
			return Constants.ASSOC_CONTAINS;
		} 
	}

	
	/**
	 * Comprobaci�n de propiedades del bean 
	 */
	protected void checkRequired(String propertyName, String propertyValue) throws Exception {
		if (StringUtils.isBlank(propertyValue)) {
			throw new Exception("   Par�metro \"" + propertyName + "\" requerido en el bean Alfresco");
		}
	}
	
	private Reference getReferencia(Query query) throws Exception {
		ResultSet resultSet = executeQuery(query);
		if (resultSet.getTotalRowCount() == 0) {
			return null;
		} else if (resultSet.getTotalRowCount() == 1) {
			// El nodo existe
			ResultSetRow[] rows = resultSet.getRows();
			ResultSetRowNode node = rows[0].getNode();
			if (node != null) {
				return new Reference(getStore(), node.getId(), null);
			} else {
				throw new AlfrescoException ("El nodo debe existir");
			}
		} else {
			throw new AlfrescoException ("En esta b�squeda no se espera obtener m�s de un resultado en la consulta " + query.getStatement());
		}
	}
	
	private Query getQuery(String[] posiblesPathsAbsolutosPadres, String nombreNodo,
						   String[] propiedadNombre, String tipoNodo, boolean busquedaHijos, String[] condiciones) {

		// 1) Consulta por el path
		String[] consultasPath = new String[posiblesPathsAbsolutosPadres.length];
		for (int i = 0; i < posiblesPathsAbsolutosPadres.length; i++) {
			// Se prepara la consulta para hacer una b�squeda recursiva, en caso de que aparezca el car�cter de BUSQUEDA RECURSIVA
			boolean busquedaInclusive = posiblesPathsAbsolutosPadres[i].contains(AlfrescoConstants.SEPARADOR_BUSQUEDA_RECURSIVA_NODE_INCLUSIVE);
            
			String consulta = null;
            
			consulta = AlfrescoPrivadoUtils.normalizarBusquedaRecursiva(posiblesPathsAbsolutosPadres[i], true, normalizarISO9075());

			consulta = AlfrescoPrivadoUtils.completarPathNormalizadoConQnamePrefix(consulta, getNamespaceModelPrefix());

			if (nombreNodo != null || busquedaHijos) {     
				if (!StringUtils.isBlank(consulta) && !consulta.endsWith(AlfrescoConstants.SEPARADOR)) {
					consulta += AlfrescoConstants.SEPARADOR;
				}
				if(busquedaInclusive) {
				    consulta = "PATH:\"" + consulta + ".\"";
				} else {
				    consulta = "PATH:\"" + consulta + "*\"";
				}
			} else {
				consulta = "PATH:\"" + consulta + "\"";
			}
			
			consultasPath[i] = consulta;
		}
		StringBuffer consultaPathBuffer = new StringBuffer("(");
		for (int i = 0; i < consultasPath.length; i++) {
			consultaPathBuffer.append(consultasPath[i]);
			if (i != consultasPath.length - 1) {
				consultaPathBuffer.append(" OR ");
			}			
		}
		consultaPathBuffer.append(")");
		String consultaPath = consultaPathBuffer.toString();
		
		// 2) Consulta por el nombre del nodo, en caso de que se indique
		String consultaNombreNodo;
		if (nombreNodo != null) {
			if (propiedadNombre == null) { 
				consultaNombreNodo = " AND " + getQuery("cm", "name", nombreNodo, null, false);
			} else {				
				consultaNombreNodo = " AND " + getQuery(propiedadNombre[0], propiedadNombre[1], nombreNodo, null, false);
			}
		} else {
			consultaNombreNodo = "";
		}
		
		// 3) Consulta por el tipo de nodo, en caso de que se indique		
		String consultaTipoNodo;
		if (tipoNodo != null) {
			consultaTipoNodo = " AND TYPE:\"" + tipoNodo + "\"";
		} else {
			consultaTipoNodo = "";
		}
		
		StringBuffer consultaCondicion = getConsultaCondicion(condiciones);
		
		return new Query(Constants.QUERY_LANG_LUCENE, consultaPath + consultaNombreNodo + consultaTipoNodo + consultaCondicion);		
	}
	
	private StringBuffer getConsultaCondicion(String[] condiciones) {
		StringBuffer consultaCondicion = new StringBuffer();
		if (condiciones != null) {
			for (int i = 0; i < condiciones.length; i++) {
				String condicionAdicional = condiciones[i];
				if (!StringUtils.isBlank(condicionAdicional)) {
					consultaCondicion.append(" AND " + condicionAdicional);
				}
			}
		} 
		return consultaCondicion;
	}
	
	/**
	 */
	private Query getQuery(String pathAbsolutoPadre, String tipoNodo, boolean busquedaHijos, String[] condicionesAdicionales) {
		return getQuery(new String[]{pathAbsolutoPadre}, tipoNodo, busquedaHijos, condicionesAdicionales);
	}
	
	/**
	 */
	private Query getQuery(String[] pathAbsolutoPadre, String tipoNodo, boolean busquedaHijos, String[] condicionesAdicionales) {
		return getQuery(pathAbsolutoPadre, null, null, tipoNodo, busquedaHijos, condicionesAdicionales);
	}	
	
//	/**
//	 * Busca s�lo en los nodos que est�n inmediatamente por debajo (no entra en subdirectorios, si no se indica)
//	 * To find all nodes directly below "/sys:user"     ---> PATH:"/sys:user/*"             ===> esta es la que se esta usando
//	 * To find all nodes at any depth below "/sys:user" ---> PATH:"/sys:user//*"
//	 */
//	private Reference getReferenciaAbsoluto(String[] pathPadreAbsoluto, String tipoNodo, String nombreNodo, String[] propiedadNombre) throws Exception {
//		Query query = getQuery(pathPadreAbsoluto, nombreNodo, propiedadNombre, tipoNodo, false, null);
//		return getReferencia(query);
//	}
	
//	private Reference getReferenciaAbsoluto(String pathPadreAbsoluto, String tipoNodo, String nombreNodo, String[] propiedadNombre) throws Exception {
//		return getReferenciaAbsoluto(new String[]{pathPadreAbsoluto}, tipoNodo, nombreNodo, propiedadNombre);
//	}
//	
	/**
	 * El path es absoluto  
	 */
	private Reference getReferenciaAbsoluto(String pathAbsoluto, String tipoNodo) throws Exception {
		String pathAbsolutoNormalizado = AlfrescoPrivadoUtils.normalizar(pathAbsoluto, true, normalizarISO9075());
		if (StringUtils.isBlank(pathAbsolutoNormalizado)) {
			return referenceStore;
		}
		Query query = getQuery(pathAbsolutoNormalizado, tipoNodo, false, null);
		return getReferencia(query);
	}
	
	public Reference getReferencia(String pathRelativo, String tipoNodo) throws Exception {
		String pathAbsoluto = getPathAbsoluto(pathRelativo);
		return getReferenciaAbsoluto(pathAbsoluto, tipoNodo);
	}
	
	/**
	 * @throws AlfrescoException 
	 */
	private ResultSet executeQuery(Query query) throws Exception {
		QueryResult queryResult = getRepositoryService().query(getStore(), query, false);
		return queryResult.getResultSet();
	}
	
	/**
	 * No se comprueba que no exista la carpeta
	 * El path es absoluto
	 */
	private Reference crearCarpetaAbsoluto(String pathAbsoluto, String nombreCarpeta) throws Exception {
		NamedValue[] propiedades = new NamedValue[]{new NamedValue(Constants.PROP_NAME, false, nombreCarpeta, null)};
		return crearNodoAbsoluto(pathAbsoluto,  nombreCarpeta, Constants.TYPE_FOLDER, propiedades);
	}		
	
	/**
	 * El path es absoluto  
	 * 
	 * @param createFolders Indica si se han de crear las subcarpetas en el caso de que no existan
	 * @return
	 * @throws AlfrescoException 
	 * @throws RemoteException 
	 * @throws RepositoryFault 
	 */
	private Reference obtenerYCrearReferenciaPath(String rutaPadreAbsoluta, String pathAbsolutoToCreate, boolean createFolders, boolean debug) throws Exception {
		Reference referenceLastParent = null;
		if (rutaPadreAbsoluta == AlfrescoConstants.SEPARADOR) {
			referenceLastParent = getReferenceStore();
		} else {
			referenceLastParent = getReferenciaAbsoluto(rutaPadreAbsoluta, null);
		}

		String[] foldersName = pathAbsolutoToCreate.split(AlfrescoConstants.SEPARADOR);
		if (foldersName.length != 0) {
			String pathAbsolutoPadre = rutaPadreAbsoluta;		
			for (int i = 0; i < foldersName.length; i++) {
				if (!StringUtils.isBlank(foldersName[i])) {					 
					// Se comprueba si la carpeta est� ya creada					
					Reference subFolderReference = getReferenciaAbsoluto(pathAbsolutoPadre + AlfrescoConstants.SEPARADOR + foldersName[i], null);
					if (subFolderReference == null) {
						// La carpeta no est� creada
						if (createFolders) {
							if (debug) {
								log.info("	- La carpeta '" + foldersName[i] + "' no esta creada. Va a crearse.");
							}
							// Se crea la carpeta
							subFolderReference = crearCarpetaAbsoluto(pathAbsolutoPadre, foldersName[i]);
							if (debug) {
								log.info("	- Carpeta '" + foldersName[i] + "' creada.");
							}
						} else {
							throw new AlfrescoException("No se ha encontrado la carpeta en el repositorio, y no est� permitido crearla bajo demanda: " + foldersName[i]);
						}
					}
					pathAbsolutoPadre += foldersName[i] + AlfrescoConstants.SEPARADOR;
					referenceLastParent = subFolderReference;
				}
			}
		}
		return referenceLastParent;
	}
	
//	private Version[] getNodoVersion(Reference referencia) throws Exception {
//		// Se obtiene la versi�n del historial de versiones
//		VersionHistory versionHistory = getAuthoringService().getVersionHistory(referencia);
//		return versionHistory.getVersions();
//	}
//	
//	/**
//	 * Obtiene el nodo padre de un nodo dado
//	 */
//	public Reference getNodoPadre(Reference nodo) throws Exception {
//	    return getNodoPadre(nodo, 0);
//	}
//	
//    /** 
//     * Obtiene el nodo padre de un nodo dado pada una determinada profundidad
//     * ej: profundidadPadre = 0 => devuelve el nodo padre
//     *     profundidadOadre = 1 => deveulve el nodo abuelo    
//     * @param nodo
//     * @param profundidadPadre
//     * @return devuelve null en caso de que no exista un padre para la profundidad indicada
//     * @throws RemoteException
//     * @throws AlfrescoException
//     */
//    public Reference getNodoPadre(Reference nodo, int profundidadPadre) throws Exception {
//        if (profundidadPadre < 0) {
//            throw new IllegalArgumentException("El par�metro \"profundidadPadre\" no puede ser negativo");
//        }
//        QueryResult queryResult = getRepositoryService().queryParents(nodo);
//        ResultSet resultSet = queryResult.getResultSet();
//        
//        if (resultSet.getRows().length < profundidadPadre) {
//            return null;
//        }
//        String uuidPadre = queryResult.getResultSet().getRows(profundidadPadre).getNode().getId();
//        return getNodo(uuidPadre, null, nodo.getStore()).getReference();
//    }
//
//
//	/**
//	 * Obtiene el path del nodo padre de un nodo dado, a partir del path por defecto
//	 */
//	public String getPathNodoPadre(Reference nodo) throws Exception {
//		return  getPathNodoPadre(nodo, 0);
//	}
//	
//    /**
//     * Obtiene el path del nodo padre de un nodo dado, a partir del path por defecto
//     */
//    public String getPathNodoPadre(Reference nodo, int profundidadPadre) throws Exception {
//        Reference reference = getNodoPadre(nodo, profundidadPadre);
//        String path = reference.getPath();
//        path = ISO9075.decode(path);
//        // OJO!! Mantener el orden de las sustituciones
//        path = path.replaceAll(getPathRaiz(), "");
//        path = path.replaceAll("/.*?:", "/");
//        return path;
//    }   
//	
//	/**
//	 * Referencia al UUID del nodo en el Workspace
//	 */
//	public String getPathNodoEnWorkspace(String uuid) {
//		return Constants.WORKSPACE_STORE + "://" + normalizar(getStoreNombre() + AlfrescoConstants.SEPARADOR + uuid, false);
//	}
//	
//	public List<String> getCategorias() throws ClassificationFault, RemoteException {
//		Store spacesStore = new Store(Constants.WORKSPACE_STORE, AlfrescoConstants.STORE_SPACESSTORE);
//		Reference parentCategory = new Reference(spacesStore, null, CATEGORIAS_RAIZ + "/cm:" + getCategoriaTiposDocumentos());
//		Category[] categories = getClassificationService().getChildCategories(parentCategory);
//		List<String> categoriasString = new ArrayList<String>();
//		if (categories != null) {
//			for (int i = 0; i < categories.length; i++) {
//				Category category = categories[i];
//				categoriasString.add(category.getTitle());
//			}
//		} 
//		return categoriasString; 
//	}
//	
//	public String getCategoriaPath(String categoria) {
//		return "PATH:\"" + CATEGORIAS_RAIZ + "/cm:" + getCategoriaTiposDocumentos() + "/cm:" + categoria + "/*\"";
//	}
//
//	
//    /**
//     * Obtiene el path del nodo padre de un nodo dado, a partir del path por defecto
//     */
//    public String getPathNodoPadre(Reference nodo, int profundidadPadre) throws Exception {
//        Reference reference = getNodoPadre(nodo, profundidadPadre);
//        return obtenerPath(reference);
//    }   
//	
//	/**
//	 * Referencia al UUID del nodo en el Workspace
//	 */
//	public String getPathNodoEnWorkspace(String uuid) {
//		return Constants.WORKSPACE_STORE + "://" + normalizar(getStoreNombre() + SEPARADOR + uuid, false);
//	}
//	
//	public List<String> getCategorias() throws ClassificationFault, RemoteException {
//		Store spacesStore = new Store(Constants.WORKSPACE_STORE, AlfrescoConstants.STORE_SPACESSTORE);
//		Reference parentCategory = new Reference(spacesStore, null, CATEGORIAS_RAIZ + "/cm:" + getCategoriaTiposDocumentos());
//		Category[] categories = getClassificationService().getChildCategories(parentCategory);
//		List<String> categoriasString = new ArrayList<String>();
//		if (categories != null) {
//			for (int i = 0; i < categories.length; i++) {
//				Category category = categories[i];
//				categoriasString.add(category.getTitle());
//			}
//		} 
//		return categoriasString; 
//	}
//	
//	public String getCategoriaPath(String categoria) {
//		return "PATH:\"" + CATEGORIAS_RAIZ + "/cm:" + getCategoriaTiposDocumentos() + "/cm:" + categoria + "/*\"";
//	}

	
	/* ********************************************************************************************************************************* */
	/* *************************************** A implementar por las clases hijas  ***************************************************** */
	/* ********************************************************************************************************************************* */
	protected abstract String getNamespaceModel();			 // Namespace propio del modelo "Gestor Documental"
	protected abstract String getNamespaceModelPrefix();	 // Prefix del Namespace propio del modelo "Gestor Documental"
	protected abstract String getCategoriaTiposDocumentos(); // Nombre de la categor�a de los tipos de documento
	protected abstract String[] getSubcarpetasPathRaiz();	 // Subcarpetas a crear inicialmente por debajo del path ra�z
	protected abstract boolean normalizarISO9075();	
	/* ********************************************************************************************************************************* */
	/* ******************************************** Getters y setters ****************************************************************** */
	/* ********************************************************************************************************************************* */
	
	private Reference getReferenceStore() throws Exception {
		if (referenceStore == null) {
			inicializacion();
		}
		return referenceStore;
	}

	private RepositoryServiceSoapPort getRepositoryService() {
		if (repositoryService == null) {
			repositoryService = WebServiceFactory.getRepositoryService(webServiceFactoryConfig);
		}
		return repositoryService;
	}

	private ContentServiceSoapPort getContentService() {
		if (contentService == null) {
			contentService = WebServiceFactory.getContentService(webServiceFactoryConfig);
		}
		return contentService;
	}

	
	private AuthoringServiceSoapBindingStub getAuthoringService() {
		if (authoringService == null) {
			authoringService = WebServiceFactory.getAuthoringService(webServiceFactoryConfig);
		}
		return authoringService;
	}


	@SuppressWarnings("unused")
    private ClassificationServiceSoapBindingStub getClassificationService() {
		if (classificationService == null) {
			classificationService = WebServiceFactory.getClassificationService(webServiceFactoryConfig);
		}
		return classificationService;
	}

	public String getPathRaiz() {
		return pathRaiz;
	}

	public void setPathRaiz(String pathRaiz) {
		this.pathRaiz = AlfrescoPrivadoUtils.normalizar(pathRaiz, false, normalizarISO9075()); 
	}
	
	public Store getStore() throws Exception {
		if (store == null) {
			inicializacion();
		}		
		return store;
	}

	private void inicializacion() throws Exception {
		log.info("Inicializando stores y workspace");
		try {
			inicializarWorkspace();					
		} catch (RemoteException e) {
			log.error("No se ha podido inicializar el workspace", e);
			throw e;
		} catch (AlfrescoException e) {
			log.error("No se ha podido inicializar el workspace", e);
			throw e;
		}
		log.info("Inicializacion del stores y workspace completado");			
	}
	
	public Store getStoreVersion() throws Exception {
		if (storeVersion == null) {
			inicializacion();			
		}
		return storeVersion;
	}

	public void setStoreVersion(Store storeVersion) {
		this.storeVersion = storeVersion;
	}

	public void setAlfrescoWebService(String alfrescoWebService) {
		this.alfrescoWebService = alfrescoWebService;
	}

	/**
	 * @param anywhere Indica si se va a realizar una b�squedas por cadenas: Ej: *zu* --> puede devolver 'azul'
	 * Nota: Wild cards (* and ?) are supported in phrases after V2.1  
	 */
	public String getQuery(String prefijoModel, String atributo, String valor, Boolean nullable, boolean anywhere) {
	    
		String atributoIso = ISO9075.encode(atributo); // debe codificarse porque puede comenzar por número
		
		if (nullable != null) {
			if (nullable) {
		        return "ISNULL:\"" + prefijoModel + ":" + atributoIso +"\"";
		    } else {
		    	return "ISNOTNULL:\"" + prefijoModel + ":" + atributoIso +"\"";
		    }			
		} else { 
		    String comillasInicio = anywhere ? "\"*" : "\"";
		    String comillasFinal = anywhere ? "*\"" : "\"";
		     
			return "@" + prefijoModel + "\\:" + atributoIso + ":" + comillasInicio + valor + comillasFinal;
		}
	}
	
	public String normalizarRuta(String rutaNoNormalizada) {
		String path = ISO9075.decode(rutaNoNormalizada);
        // OJO!! Mantener el orden de las sustituciones
        path = path.replaceAll(getPathRaiz(), "");
        path = path.replaceAll("/.*?:", "/");
        return path;
	}
	
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
//	private Reference getReferencia(String uuid) throws Exception {
//		return new Reference(getStore(), uuid, null);		
//	}

//	public Version[] getVersiones(Reference nodo) throws Exception {
//		return this.getNodoVersion(nodo);
//	}
//	
	public Node[] getNodo(Predicate predicado) throws Exception {
		return getRepositoryService().get(predicado);		
	}
	
//	@SuppressWarnings("unchecked")
//	public Version getVersion(Reference nodo, Calendar dia)  throws Exception {
//		
//		// Se obtienen las versiones del nodo
//		Version[] versiones = getNodoVersion(nodo);
//		if (versiones == null) {
//			throw new IllegalArgumentException("El nodo no tiene versiones");
//		}
//		
//		// Se ordenan las versiones por fecha
//		Comparator comparator = new BeanComparator(com.arte.gestordocumental.alfresco.AlfrescoConstants.CREATED_ALFRESCO, BeanComparator.SORT_DESCENDING);
//		Collections.sort(Arrays.asList(versiones), comparator);
//	
//		// Se obtiene la versi�n vigente en la fecha dada
//		for (int i = 0; i < versiones.length; i++) {
//			org.alfresco.webservice.types.Version version = versiones[i];
//			Calendar diaVersion = DateUtils.truncate(version.getCreated(), Calendar.DATE);
//			diaVersion.setTimeZone(dia.getTimeZone());
//			if (!CalendarUtil.after(diaVersion, dia, CalendarUtil.COMPARE_DAY)) {
//				return version;
//			}				
//		}
//		return null;
//	}
//	
	/**
	 * @param anywhere Indica si se va a realizar una b�squedas por cadenas: Ej: *cade* --> puede devolver cadena 
	 */
	@SuppressWarnings("unchecked")
	public String getQuery(String prefijoModel, String atributo, List<String> valores, Boolean nullable, boolean anywhere) {
		StringBuffer queryTotal = new StringBuffer();
		queryTotal.append("(");
		for (Iterator iter = valores.iterator(); iter.hasNext();) {
			String valor = (String) iter.next();			
			String query = getQuery(prefijoModel, atributo, valor, nullable, anywhere);
			queryTotal.append(query);
			if (iter.hasNext()) {
				queryTotal.append(" OR ");	
			}
		}
		queryTotal.append(")");
		return queryTotal.toString();
	}
	
	
	
	
	/** ********************************************************************************************************************************* */
	/** ************************************************************ Inicializaciones  ************************************************** */
	/** ********************************************************************************************************************************* */

	/**
	 * Devuelve el Store
	 */
	private Store inicializarStore(String scheme, String address) throws Exception {

		log.info("Inicializando store: " + scheme + "://" + address);
		
		Store store = new Store(scheme, address);
		
		// Referencia al nodo root del "store"
		Predicate predicate = new Predicate(null, store, null);
		try {
			Node[] nodes = getRepositoryService().get(predicate);
			// Ya est� creado el store			
			if (nodes.length == 1) { 
				referenceStore = nodes[0].getReference();
			} else {
				throw new AlfrescoException("   - No se ha podido obtener el nodo root del store: " + scheme + "://" + address);
			}
		} catch (RepositoryFault e) {
			// El store no est� creado -> error!!!
			log.error("Error al inicializar el store: " + scheme + "://" + address + " [Excepcion: " + e + "]");
			throw new AlfrescoException("Error al inicializar el store: " + scheme + "://" + address + " [Excepcion: " + e + "]");
		}
		log.info("Store inicializado: " + scheme + "://" + address);

		return store;
	}		
	
	private void inicializarPathDefecto() throws Exception {
		log.info("Inicializando el path por defecto: " + getPathRaiz());
		try {
			obtenerYCrearReferenciaPath(AlfrescoConstants.SEPARADOR, getPathRaiz(), false, true);
			
		} catch (RemoteException e) {
			log.error("No se ha podido inicializar el path por defecto", e);
			throw e;
		} catch (AlfrescoException e) {
			log.error("No se ha podido inicializar el path por defecto", e);
			throw e;			
		}
		log.info("Inicializacion del path por defecto completada");			
		
		// Posibilidad de inicializaci�n de m�s subcarpetas
		String[] subcarpetas = getSubcarpetasPathRaiz();
		if (subcarpetas != null && subcarpetas.length != 0) {
			log.info("Inicializando creacion de subcarpetas bajo el path: " + Arrays.toString(subcarpetas));
			try {
				for (int i = 0; i < subcarpetas.length; i++) {
					String subcarpeta = subcarpetas[i];
					log.info("   - Subcarpeta: " + subcarpeta);
					obtenerYCrearReferenciaPath(getPathRaiz(), subcarpeta, true, true);
				}				
			} catch (RemoteException e) {
				log.error("No se ha podido completar la creacion de subcarpetas bajo el path", e);
				throw e;
			} catch (AlfrescoException e) {
				log.error("No se ha podido completar la creacion de subcarpetas bajo el path", e);
				throw e;			
			}
			log.info("Creacion de subcarpetas bajo el path completada");
		}
	}
	
	/**
	 * Devuelve el Store, cre�ndolo si no existe
	 * @throws AlfrescoException 
	 * @throws RemoteException 
	 * @throws Exception 
	 */
	protected synchronized void inicializarWorkspace() throws Exception {
		if (store != null) { // Si es != null es que todo se ha inicializado ya correctamente
			return;
		}
		
		Store store = inicializarStore(Constants.WORKSPACE_STORE, STORE_NOMBRE);
		Store storeVersion = inicializarStore(Constants.WORKSPACE_STORE, STORE_VERSIONES_NOMBRE);
		
		this.storeVersion = storeVersion;
		this.store = store;
		try {
			inicializarPathDefecto();
		} catch (RemoteException e) {
			this.store = null;
			this.storeVersion = null;
			this.referenceStore = null;
			throw e;			
		} catch (AlfrescoException e) {
			this.store = null;
			this.storeVersion = null;
			this.referenceStore = null;
			throw e;
		}
	}
	
	private void inicializarNamespacesModelo() {
		// Content
		namespacesModelo.put(AlfrescoConstants.NAMESPACE_CONTENT_MODEL_PREFIX, AlfrescoConstants.NAMESPACE_CONTENT_MODEL);
		// Application
		namespacesModelo.put(AlfrescoConstants.NAMESPACE_APPLICATION_MODEL_PREFIX, AlfrescoConstants.NAMESPACE_APPLICATION_MODEL);
		// Dictionary
		namespacesModelo.put(AlfrescoConstants.NAMESPACE_DICTIONARY_MODEL_PREFIX, AlfrescoConstants.NAMESPACE_DICTIONARY_MODEL);
		// System
		namespacesModelo.put(AlfrescoConstants.NAMESPACE_SYSTEM_MODEL_PREFIX, AlfrescoConstants.NAMESPACE_SYSTEM_MODEL);
	}
	
//	private String obtenerPath(Reference reference) {
//        String rutaNoNormalizada = reference.getPath();
//        return normalizarRuta(rutaNoNormalizada);
//	}
	
	private String getPathAbsoluto(String ruta) {
		return AlfrescoPrivadoUtils.getPathAbsoluto(getPathRaiz(), ruta);
	}
}
