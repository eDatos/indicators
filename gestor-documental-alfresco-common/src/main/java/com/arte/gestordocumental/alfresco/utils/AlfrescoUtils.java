package com.arte.gestordocumental.alfresco.utils;

import java.security.MessageDigest;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.alfresco.webservice.types.NamedValue;
import org.apache.commons.lang.StringUtils;

import com.arte.gestordocumental.alfresco.AlfrescoConstants;




public class AlfrescoUtils {
	
	public final static String SEPARADOR = AlfrescoConstants.SEPARADOR;
	public final static String SEPARADOR_BUSQUEDA_RECURSIVA = SEPARADOR + AlfrescoConstants.SEPARADOR_BUSQUEDA_RECURSIVA + SEPARADOR;
	public final static String SEPARADOR_BUSQUEDA_IGNORAR = SEPARADOR + AlfrescoConstants.SEPARADOR_BUSQUEDA_IGNORAR + SEPARADOR;
    private static final DatatypeFactory dataTypeFactory;
    
    static {
        try {
            dataTypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
	
	private final static String ALGORITMO_HASH = "MD5";
	
	/**
	 * Genera el hash de un conjunto de bytes
	 */
	public static byte[] generarHash(byte[] contenido) throws Exception {		
		MessageDigest digest = MessageDigest.getInstance(ALGORITMO_HASH);		
		digest.update(contenido);
		byte[] hash = digest.digest();	
		return hash;
	}
	
	
	public static NamedValue[] nameValueTOArray(List<NamedValue> propiedades) {
		NamedValue[] propiedadesArray = new NamedValue[propiedades.size()];
		propiedades.toArray(propiedadesArray);
		return propiedadesArray;
	}
	
	public static NamedValue valoresToNamedValue(String propiedad, List<String> valores) {
		String [] array = null;
		if (valores != null) {
//			Set<String> set = new HashSet<String>(valores); // Se transforma a Set para evitar que se pongan datos repetidas
//			array = new String[set.size()];
//			set.toArray(array);		
			array = new String[valores.size()];
			valores.toArray(array);  // Ya no se transforma a Set porque sí que puede ocurrir que haya elementos repetidos
		} 
		return new NamedValue(propiedad, true, null, array);
	}
	
    public static String formatFechaFormatoAlfresco(XMLGregorianCalendar fecha) {
        return fecha != null ? ISO8601DateFormat.format(fecha.toGregorianCalendar().getTime()) : null;
    }

    public static String formatFechaFormatoAlfresco(Date fecha) {
        return fecha != null ? ISO8601DateFormat.format(fecha) : null;
    }
    
    public static Date parseFechaFormatoAlfresco(String fecha) {
        return fecha != null ? ISO8601DateFormat.parse(fecha) : null;
    }
	
	public static XMLGregorianCalendar getFechaActual() {
	    return dataTypeFactory.newXMLGregorianCalendar();
	}
	
	/**
	 * La versión del Alfresco siempre será "1._" (por un bug que no permite indicar si la versión es MAJOR o MINOR).
	 * En el gestor Documental, se tomará como versión la parte decimal de la versión del Alfresco. 
	 *  v00 corresponde con 1.0
	 *  v01 corresponde con 1.1
	 *  v02 corresponde con 1.2
	 */
	public static String versionGestorToVersionAlfresco(String versionGestorDocumental) {
		return "1." + Integer.toString(Integer.parseInt(versionGestorDocumental));		
	}
	
	public static final String obtenerUuidNodo(String path) {
		return StringUtils.substringAfterLast(path, SEPARADOR);
	}
	
	public static String obtenerPropiedadValue(NamedValue[] propiedades, String propiedad) {
		String[] propiedadesValor = obtenerPropiedadValueMultivaluada(propiedades, propiedad);
		return propiedadesValor != null && propiedadesValor.length != 0 ? propiedadesValor[0] : null;
	}
	
	public static String[] obtenerPropiedadValueMultivaluada(NamedValue[] propiedades, String propiedad) {
		for (NamedValue propiedadNamedValue : propiedades) {
			if (propiedadNamedValue.getName().equals(propiedad)) {
				if (propiedadNamedValue.getIsMultiValue() == null) {   // si no se indica el valor, este atributo aparece como null.
					return null;
				}
				if (propiedadNamedValue.getIsMultiValue()) {
					return propiedadNamedValue.getValues();
				} else {
					return new String[] {propiedadNamedValue.getValue()};					
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param version Versión actual
	 * @param versionSolicitada  Versión solicitada, que ha de ser menor o igual que la versión actual
	 */
	public static boolean esCorrectaVersionSolicitada(String version, String versionSolicitada) {
//		  11/2007: La versión ya no tiene "."
//	   	  int indice = version.indexOf(VERSION_DELIMITADOR);
//        int majorVersion = Integer.parseInt(version.substring(0, indice));
//        int minorVersion = Integer.parseInt(version.substring(indice + 1));
//		
//        indice = versionSolicitada.indexOf(VERSION_DELIMITADOR);
//        int majorVersionSolicitada = Integer.parseInt(versionSolicitada.substring(0, indice));
//        int minorVersionSolicitada = Integer.parseInt(versionSolicitada.substring(indice + 1));
//        
//        return (majorVersionSolicitada < majorVersion) || (majorVersionSolicitada == majorVersion && minorVersionSolicitada <= minorVersion);
	      int versionSolicitadaInteger = Integer.parseInt(versionSolicitada);
	      int versionInteger = Integer.parseInt(version);
	      return (versionSolicitadaInteger <= versionInteger);		
	}
}
