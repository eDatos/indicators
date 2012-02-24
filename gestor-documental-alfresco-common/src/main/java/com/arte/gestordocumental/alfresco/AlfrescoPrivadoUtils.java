package com.arte.gestordocumental.alfresco;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.alfresco.webservice.util.ISO9075;
import org.apache.commons.lang.StringUtils;

class AlfrescoPrivadoUtils {
	
	
	public static String normalizar(String path, boolean paraConsulta, boolean iso9075) {
		if (!StringUtils.isBlank(path)) {
			// Normalizar el carácter "/" (AlfrescoConstants.SEPARADOR)
			String pathNormalizado = path.replaceAll(AlfrescoConstants.SEPARADOR + "{2,}", AlfrescoConstants.SEPARADOR);
			pathNormalizado = StringUtils.removeStart(pathNormalizado, AlfrescoConstants.SEPARADOR);
			pathNormalizado = StringUtils.removeEnd(pathNormalizado, AlfrescoConstants.SEPARADOR);
			
			if (paraConsulta) {
				if (iso9075) {
					String[] carpetas = pathNormalizado.split(AlfrescoConstants.SEPARADOR);
					String pathFinal = "";
					if (carpetas.length != 0) {
						for (int i = 0; i < carpetas.length; i++) {
							String carpeta = carpetas[i];
							if (!carpeta.equals(AlfrescoConstants.SEPARADOR_BUSQUEDA_IGNORAR) && !carpeta.equals(AlfrescoConstants.SEPARADOR_BUSQUEDA_RECURSIVA) &&
							    !carpeta.equals(AlfrescoConstants.SEPARADOR_BUSQUEDA_RECURSIVA_NODE_INCLUSIVE)) {
								// Normalizar para números, caracteres, espacios...
								String sinEncoding = ISO9075.decode(carpeta); // por si acaso que ya estuviese codificada
								carpeta = ISO9075.encode(sinEncoding);
							} 
							pathFinal += carpeta + AlfrescoConstants.SEPARADOR;

						}
						pathFinal = StringUtils.removeEnd(pathFinal, AlfrescoConstants.SEPARADOR);
						pathNormalizado = pathFinal;
					}					
				} else {
					// Normalizar los espacios
					pathNormalizado = pathNormalizado.replaceAll(" ", AlfrescoConstants.ESPACIO_ALFRESCO);
				}
			}
			return pathNormalizado;
		}
		return path;
	}
	
	public static String normalizarBusquedaRecursiva(String path, boolean paraConsulta, boolean iso9075) {
	    String consultaNormalizada = normalizar(path, paraConsulta, iso9075);
	    consultaNormalizada = consultaNormalizada.replaceAll(AlfrescoConstants.SEPARADOR + "*" + AlfrescoConstants.SEPARADOR_BUSQUEDA_RECURSIVA + AlfrescoConstants.SEPARADOR + "*", AlfrescoConstants.SEPARADOR + AlfrescoConstants.SEPARADOR);
	    consultaNormalizada = consultaNormalizada.replaceAll(AlfrescoConstants.SEPARADOR + "*" + "\\"+ AlfrescoConstants.SEPARADOR_BUSQUEDA_RECURSIVA_NODE_INCLUSIVE + AlfrescoConstants.SEPARADOR + "*", AlfrescoConstants.SEPARADOR + AlfrescoConstants.SEPARADOR);
		return consultaNormalizada;
	}	
	
	/**
	 * Devuelve la lista de carpetas
	 * Nota: el path DEBE estar normalizado.
	 * Nota: No se usa el split porque dos '/' seguidos tienen un significado o acabar en '/'
	 */
	public static List<String> separarPath(String path) {
		List<String> carpetas = new ArrayList<String>();
		StringBuffer carpeta = new StringBuffer();
		for (int i = 0; i < path.length(); i++) {
			char caracterPath = path.charAt(i);
			if (caracterPath == AlfrescoConstants.SEPARADOR.charAt(0) || (i == path.length() - 1)) {
				if (i == path.length() - 1) {
					carpeta.append(caracterPath);	
				}
				carpetas.add(carpeta.toString());
				carpeta = new StringBuffer();
			} else {
				carpeta.append(caracterPath);
			}			
		}
		return carpetas;		 
	}
	
	/**
	 * El path es absoluto
	 * OJO!! Se asume que el path está bien construido, NORMALIZADO, sin símbolos "/" de más
	 */
	public static String completarPathNormalizadoConQnamePrefix(String path, String prefijo){
			
			StringBuffer pathConQname = new StringBuffer();
	//        int sizePathRaiz = getPathRaiz().split("/").length;
			List<String> carpetas = separarPath(path);
						
			for (int i = 0; i < carpetas.size(); i++) {
				String carpeta = carpetas.get(i);
				if (!StringUtils.isBlank(carpeta)) {
					if (carpeta.contains(":")) {
						// Ya tiene el prefix añadido
						pathConQname.append(carpetas.get(i));
	//				} else if (i < sizePathRaiz) {
	//	                // Es una carpeta raiz, no hay que añadir la raiz
	//			        pathConQname.append(carpetas.get(i));
	            } else if (carpeta.equals(AlfrescoConstants.SEPARADOR) || carpeta.equals(AlfrescoConstants.SEPARADOR_BUSQUEDA_RECURSIVA)  || 
				    carpeta.equals(AlfrescoConstants.SEPARADOR_BUSQUEDA_RECURSIVA_NODE_INCLUSIVE) || carpeta.equals(AlfrescoConstants.SEPARADOR_BUSQUEDA_IGNORAR)) {
					pathConQname.append(carpeta);
				} else {
					pathConQname.append(prefijo + ":" + carpeta);
				} 
				if (i != carpetas.size() - 1) {
					pathConQname.append(AlfrescoConstants.SEPARADOR);					
				}
			} else {
				pathConQname.append(AlfrescoConstants.SEPARADOR); // es una búsqueda recursiva "//"
			}
		}			
		return pathConQname.toString();
	}
	
	public static String getNombreNodoConQname(String nombreNodo, Map<String, String> namespacesModelo, String namespaceModel) {
		// Nombre del nodo en la forma "{http://...}nombreNodo"
		String nombreNodoConQname = null;
		if (nombreNodo.contains(AlfrescoConstants.SEPARADOR_PREFIX_QNAME)) {
			// El nombre ya contiene el prefix. Hay que cambiar el prefix por el namespace
			String[] nombreNodoConPrefix = nombreNodo.split(":");
			String prefix = nombreNodoConPrefix[0];
			String nombreNodoSinPrefix = nombreNodoConPrefix[1];			
			nombreNodoConQname = AlfrescoConstants.createQNameString(namespacesModelo.get(prefix), nombreNodoSinPrefix);
		} else {
			nombreNodoConQname = AlfrescoConstants.createQNameString(namespaceModel, nombreNodo);	
		}
		return nombreNodoConQname;
	}
	

	public static String getPathAbsoluto(String pathRaiz, String pathRelativo) {
		if (!pathRelativo.startsWith(AlfrescoConstants.BUSQUEDA_SIN_PATH_RAIZ)) {
			return pathRaiz + AlfrescoConstants.SEPARADOR + pathRelativo;
		} else {
			return AlfrescoConstants.CARPETA_COMPANY_HOME + AlfrescoConstants.SEPARADOR + pathRelativo.replace(AlfrescoConstants.BUSQUEDA_SIN_PATH_RAIZ, "");
		}
	}
}
