package com.arte.gestordocumental.alfresco;

import java.io.InputStream;
import java.util.List;

import org.alfresco.webservice.repository.UpdateResult;
import org.alfresco.webservice.types.CML;
import org.alfresco.webservice.types.CMLCreate;
import org.alfresco.webservice.types.CMLDelete;
import org.alfresco.webservice.types.CMLMove;
import org.alfresco.webservice.types.CMLUpdate;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Node;
import org.alfresco.webservice.types.Reference;

public interface Alfresco {
	
	public void abrirSesion() throws Exception;
	public void abrirSesion(String usuario, String password) throws Exception;
	public void cerrarSesion();	
	
	// OPERACIONES CON CML
    public CMLCreate crearCMLCreate(String ruta, String nombreNodo, String tipoNodo, NamedValue[] propiedades) throws Exception;
    public CMLUpdate crearCMLUpdate(String ruta, NamedValue[] propiedades) throws Exception;
    public CMLMove crearCMLMove(String rutaOrigen, String nombreNodo, String rutaDestino) throws Exception;
    public CMLDelete crearCMLDelete(String ruta) throws Exception;
    public UpdateResult[] invocarCML(CML cml) throws Exception;
    
	// Consultas
	public boolean existeRuta(String ruta) throws Exception;
	public Node obtenerNodo(String ruta) throws Exception;
	public Node obtenerNodo(String ruta, String tipoNodo) throws Exception;
	public Node[] buscarNodos(String ruta, String tipoNodo, String[] condiciones) throws Exception;
	public InputStream obtenerContenidoPorNodeUuid(String nodeUuid) throws Exception;
	public Node obtenerVersion(String ruta, String versionSolicitada) throws Exception;
	@Deprecated
	public Node obtenerVersion(String ruta, Integer versionSolicitada) throws Exception;
	public List<Node> obtenerVersiones(String ruta) throws Exception;
	public String obtenerUrlContenido(String uuid, String propiedadContenido) throws Exception;
	
	// Deprecated
//	public InputStream obtenerContenido(String uuid, String propiedadContenido) throws Exception;
//	public InputStream obtenerContenidoPorContentUrl(String contentUrl) throws Exception;
//	public InputStream obtenerContenidoVersion(String uuid, String propiedadContenido) throws Exception;
		
	// Actualizaciones
	public Reference crearNodo(String ruta, String nombreNodo, String tipoNodo, NamedValue[] propiedades) throws Exception;
	public UpdateResult[] actualizarPropiedades(String ruta, NamedValue[] propiedades) throws Exception;
	public UpdateResult[] actualizarPropiedadesRecursivo(String ruta, NamedValue[] propiedades) throws Exception;
	public Reference eliminarNodo(String ruta) throws Exception;
	public String guardarContenido(InputStream stream, String fileName, String mimetype, String encoding) throws Exception;
	public String guardarContenido(InputStream inputStream, String fileName, String mimetype, String encoding, int timeout) throws Exception;
	public String crearVersion(String ruta, String comentario, AlfrescoVersionType version) throws Exception;
	@Deprecated
	public String crearVersion(String ruta, String comentario) throws Exception;
	public void restaurarVersion(String ruta, String versionSolicitada) throws Exception;
	    
	// Utilidades
	public String getQuery(String prefijoModel, String atributo, String valor, Boolean nullable, boolean anywhere);
	public String getQuery(String prefijoModel, String atributo, List<String> valores, Boolean nullable, boolean anywhere);
	
	public String normalizarRuta(String rutaNoNormalizada);

	
//	
//	public Predicate getPredicado(String[] ruta, String nombreNodoPath, String tipoNodo, String[] condicionesAdicionales, Boolean rutaCompleta) throws Exception;
//	public Predicate getPredicado(String ruta, String nombreNodoPath, String tipoNodo, String[] condicionesAdicionales, Boolean rutaCompleta) throws Exception;	
//	public Predicate getPredicado(final Reference referencia) throws Exception;
//	public Predicate getPredicadoVersion(final Reference referencia) throws Exception;
//	
//	public Reference getReferencia(final String pathRelativo, final String tipoNodo) throws Exception;
//	public Reference getReferencia(String uuid) throws Exception;
//	
//	public boolean existeNodo(String pathPadreRelativo, String nombreNodoPath, String tipoNodo) throws Exception;
//	
//	
//	
//	public Reference crearNodo(final String ruta, String nombreNodo, String tipoNodo, NamedValue[] propiedades, List<ContenidoAlfresco> contenidos) throws Exception;
//	public Reference crearEnlace(Reference nodoOrigen, String nombreNodo, String pathRelativoDestino, String propiedadDestination, String propiedadEnlace) throws Exception;
//	public Reference crearCarpeta(String pathPadre, String nombreCarpeta) throws Exception;
//	public Reference crearCarpetas(String pathPadre, String rutaCarpetasCrear) throws Exception;
//	
//	public Reference eliminarNodo(final Predicate predicate) throws Exception;
//	
//	
//	public Node[] getNodos(String[] ruta, String nombreNodoPath, String tipoNodo, Boolean rutaCompleta) throws Exception;	
//	public Node[] getNodosHijos(String pathRelativo, String tipoNodo) throws Exception;
//	
//	
//	public Node[] getNodos(List<String> nodeUuids, String[] condiciones) throws Exception;
//	public Node[] getNodos(List<String> nodeUuids, String[] condiciones, Store store) throws Exception;
//	public Node getNodo(String nodeUuid, String consultaCondicion) throws Exception;
//	public Node getNodo(String nodeUuid, String consultaCondicion, Store store) throws Exception;
//	public Node[] getNodo(Predicate predicado) throws Exception;
//	public Reference getNodoVersion(Reference referencia, String versionSolicitada) throws Exception;
//	public Version getVersion(Reference nodo, Calendar dia)  throws Exception;
//	public Version[] getVersiones(Reference nodo) throws Exception;
//	
//	public UpdateResult[] actualizar(CML cml) throws Exception;
//	public CMLUpdate crearActualizacionPropiedades(Predicate predicado, NamedValue[] propiedades) throws Exception;	
//	public CMLWriteContent crearActualizacionContenido(Predicate predicado, ContenidoAlfresco contenido) throws Exception;
//
//	public UpdateResult[] actualizarNodo(Predicate predicado, NamedValue[] propiedades, List<ContenidoAlfresco> contenidos) throws Exception;
//	public UpdateResult[] actualizarPropiedades(Predicate predicado, NamedValue[] propiedades) throws Exception;	
//	public UpdateResult[] actualizarContenido(Predicate predicado, ContenidoAlfresco contenido) throws Exception;
//	
//	public void eliminarPropiedad(Predicate predicado, String propiedad) throws Exception;
//	
//	public CMLMove[] crearMoverNodos(List<DocumentoCML> documentosCML) throws Exception;	
//	public UpdateResult[] moverNodos(List<DocumentoCML> documentosCML) throws Exception;
//	public UpdateResult moverNodo(DocumentoCML documentoCML) throws Exception;
//	public CMLDelete crearEliminarNodo(Reference referencia) throws Exception;
//	
//	public NamedValue[] obtenerPropiedadesVersion(Reference referencia) throws Exception;
// 	
// 	public ContenidoAlfresco getContenido(Reference referencia, String propiedadContenido) throws Exception;
// 	
// 	public String crearVersion(Predicate predicado, String comentario) throws Exception;
// 	
//	public void categorizar(Predicate predicado, List<String> categorias) throws Exception;
//	
//	public String getQuery(String prefijoModel, String atributo, String valor);
//	public String getQuery(String prefijoModel, String atributo, String valor, boolean anywhere);
//	public String getQuery(String prefijoModel, String atributo, List<String> valores, boolean anywhere);
//
//	public Reference getNodoPadre(Reference nodo) throws Exception;
//	public Reference getNodoPadre(Reference nodo, int profundidadPadre) throws Exception;
//
//	public String getPathNodoPadre(Reference nodo) throws Exception;
//	public String getPathNodoPadre(Reference nodo, int profundidadPadre) throws Exception;
//	
//	/* ********************************************************************************************************************************* */
//	/* ************************************************************ Versión 1.1.1  ***************************************************** */
//	/* ********************************************************************************************************************************* */
//	public String getPathNodoEnWorkspace(String uuid);
//	public List<String> getCategorias() throws Exception;
//	public String getCategoriaPath(String categoria);
//	
//	/* ********************************************************************************************************************************* */
//    /* ************************************************************ Versión 1.1.4  ***************************************************** */
//    /* ********************************************************************************************************************************* */
//	
//	public InputStream obtenerContenido(Reference referencia, String propiedadContenido) throws Exception;
//	
//    /* ********************************************************************************************************************************* */
//    /* ************************************************************ Versión 1.1.7  ***************************************************** */
//    /* ********************************************************************************************************************************* */
//    public Content obtenerContenidoDescripcion(Reference referencia, String propiedadContenido) throws Exception;    
//
//
//    /* ********************************************************************************************************************************* */
//    /* ************************************************************ Versión 1.1.11  ***************************************************** */
//    /* ********************************************************************************************************************************* */
//	
//	public String[] obtenerPropiedadMultivaluada(Predicate predicado, String nombrePropiedad) throws Exception;
//	public String obtenerPropiedad(String ruta, String nombrePropiedad) throws Exception;
//	public String obtenerPropiedad(Predicate predicado, String nombrePropiedad) throws Exception;
//	public String crearVersion(String ruta, String comentario) throws Exception;
}