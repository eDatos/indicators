package com.arte.gestordocumental.uri;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.arte.gestordocumental.GestorDocumentalConstants;

public class UriDocumentoUtils {
	
	// Comienzo de identificadores de contenido
	public static final String ID_INICIAL_DOCUMENTO = "D";
	
	public static final String ID_VERSION = "v";
	
	private static final String SEPARADOR_URI = "/";
	
	private static final String OPCIONAL = "?";
	
	protected static final String INICIO = "^";
	protected static final String FIN = "$";
	
	protected static final String SEPARADOR_GROUP_PATTERN_LEFT = "(";
	protected static final String SEPARADOR_GROUP_PATTERN_RIGHT = ")";
	
	protected static final String ID_DOCUMENTO_PATTERN = ID_INICIAL_DOCUMENTO + "\\d+";
	
	private static final String VERSION_PATTERN = ID_VERSION + SEPARADOR_GROUP_PATTERN_LEFT + "\\d+" + SEPARADOR_GROUP_PATTERN_RIGHT;
	protected static final String VERSION_PATTERN_OPCIONAL = SEPARADOR_GROUP_PATTERN_LEFT + SEPARADOR_URI + VERSION_PATTERN + SEPARADOR_GROUP_PATTERN_RIGHT + OPCIONAL;
	
	protected static final String SEPARADOR_OPCIONAL = SEPARADOR_URI + OPCIONAL;
	
	
	/**
	 * Parsea la URI de un documento
	 * Resultado:
	 *  - Objeto UriDocumento con los atributos:
	 * 	    1) IdDocumento
	 *      2) UriDocumento con versión
	 *      3) Versión del documento
	 */
	public static UriDocumento parsearUri(Pattern patternUri, String uriDocumento, boolean versionRequerida) throws IllegalArgumentException {
		Matcher matcher = patternUri.matcher(uriDocumento);
		if (!matcher.find()) {
			throw new IllegalArgumentException("URI de documento incorrecta: " + uriDocumento + ". No sigue el patron " + patternUri.pattern());
		} 

		String uri = matcher.group(1); // http://www.gobiernodecanarias.org/empleo/documentos/D000001/v1
		String idDocumento = matcher.group(2); // D000001
		String version = matcher.group(4); // v1
		
		// Se comprueba si se ha indicado la versión, cuando ésta es requerida 
		if (version == null && versionRequerida) {
			throw new IllegalArgumentException("URI de documento incorrecta: " + uriDocumento + ". No se ha especificado la versión");
		}
		
		// Resultado
		return new UriDocumento(uriDocumento, uri, idDocumento, version);
		
	}
	
	/**
	 * Genera la URI de un documento (incluyendo la versión)
	 */
	public static String construirUriDocumento(String uriDocumentos, String idDocumento, String numeroVersion) {
		String version = StringUtils.leftPad(numeroVersion, 2, "0");
		return uriDocumentos + idDocumento + SEPARADOR_URI + GestorDocumentalConstants.ID_VERSION +  version;
	}
}



