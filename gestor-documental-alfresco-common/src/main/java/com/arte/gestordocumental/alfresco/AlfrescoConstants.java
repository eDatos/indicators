package com.arte.gestordocumental.alfresco;

import org.alfresco.webservice.util.Constants;


public class AlfrescoConstants {	
	
    // Namespaces
    public static final String NAMESPACE_CONTENT_MODEL_PREFIX  = "cm";
    public static final String NAMESPACE_CONTENT_MODEL = Constants.NAMESPACE_CONTENT_MODEL; // http://www.alfresco.org/model/content/1.0
    
    public static final String NAMESPACE_APPLICATION_MODEL = "http://www.alfresco.org/model/application/1.0";
    public static final String NAMESPACE_APPLICATION_MODEL_PREFIX  = "app";

    public static final String NAMESPACE_SYSTEM_MODEL = Constants.NAMESPACE_SYSTEM_MODEL;  // {http://www.alfresco.org/model/system/1.0}
    public static final String NAMESPACE_SYSTEM_MODEL_PREFIX  = "sys";

    public static final String NAMESPACE_DICTIONARY_MODEL = "http://www.alfresco.org/model/dictionary/1.0";
    public static final String NAMESPACE_DICTIONARY_MODEL_PREFIX  = "d";

    public static final String NAMESPACE_VERSION_MODEL = "http://www.alfresco.org/model/versionstore/1.0";

    public static final String STORE_SPACESSTORE = "SpacesStore";
    
    // Tipos de datos
    public static final String CARPETA				 =  Constants.TYPE_FOLDER;
    
    // Propiedades del modelo de Alfresco    
    public static final String UUID_ALFRESCO		 = "node-uuid";
    
    public static final String NOMBRE_ALFRESCO		 = "name";
    public static final String CREATED_ALFRESCO 	 = "created";
    public static final String DESTINATION_ALFRESCO  = "destination";
    public static final String FILELINK_ALFRESCO 	 = "filelink";
    
    public static final String AUTOVERSIONABLE = "autoVersion";
    public static final String INITIAL_VERSION = "initialVersion";
    public static final String VERSION_TYPE = "versionType";
    public static final String VERSION_LABEL = "versionLabel";
    
    public static final String VERSION_DESCRIPCION 	 = "description";    
    public static final String VERSION_ATTRIBUTES_NAME = "qname";
    public static final String VERSION_ATTRIBUTES_VALUE = "value";
    public static final String VERSION_ATTRIBUTES_VALUES = "multiValue";
    public static final String VERSION_ATTRIBUTES_ISMULTIVALUE = "isMultiValue";
    public static final String VERSION_PROPIEDAD = "versionedProperty";
    
    public static final String CATEGORIES = "categories";        
    
    public static String CARPETA_COMPANY_HOME = "app:company_home";
    public static String CATEGORIAS_RAIZ = "/app:company_home/cm:categoryRoot/cm:generalclassifiable";
	


    // Propiedades del modelo de Alfresco con QName
    public static final String NOMBRE_ALFRESCO_QNAME		 =  createQNameString(Constants.NAMESPACE_CONTENT_MODEL, NOMBRE_ALFRESCO);
    
    public static final String CONTENIDO_ALFRESCO_QNAME 	 = Constants.PROP_CONTENT;
    public static final String CREATED_ALFRESCO_QNAME 		 = createQNameString(Constants.NAMESPACE_CONTENT_MODEL, CREATED_ALFRESCO);
    
    public static final String DESTINATION_ALFRESCO_QNAME = createQNameString(Constants.NAMESPACE_CONTENT_MODEL, DESTINATION_ALFRESCO);
    public static final String FILELINK_ALFRESCO_QNAME = createQNameString(NAMESPACE_APPLICATION_MODEL, FILELINK_ALFRESCO);
    
    public static final String AUTOVERSIONABLE_QNAME = createQNameString(Constants.NAMESPACE_CONTENT_MODEL, AUTOVERSIONABLE);    
    public static final String INITIAL_VERSION_QNAME = createQNameString(Constants.NAMESPACE_CONTENT_MODEL, INITIAL_VERSION);
    public static final String VERSION_TYPE_QNAME = createQNameString(Constants.NAMESPACE_CONTENT_MODEL, VERSION_TYPE);
    public static final String VERSION_LABEL_QNAME = createQNameString(Constants.NAMESPACE_CONTENT_MODEL, VERSION_LABEL);
    public static final String VERSION_PROPIEDAD_QNAME = createQNameString(NAMESPACE_VERSION_MODEL, VERSION_PROPIEDAD);
    public static final String VERSION_ATTRIBUTES_NAME_QNAME = createQNameString(NAMESPACE_VERSION_MODEL, VERSION_ATTRIBUTES_NAME);
    public static final String VERSION_ATTRIBUTES_VALUE_QNAME = createQNameString(NAMESPACE_VERSION_MODEL, VERSION_ATTRIBUTES_VALUE);
    public static final String VERSION_ATTRIBUTES_VALUES_QNAME = createQNameString(NAMESPACE_VERSION_MODEL, VERSION_ATTRIBUTES_VALUES);
    public static final String VERSION_ATTRIBUTES_ISMULTIVALUE_QNAME = createQNameString(NAMESPACE_VERSION_MODEL, VERSION_ATTRIBUTES_ISMULTIVALUE);
    
    public static final String CATEGORIES_QNAME = createQNameString(Constants.NAMESPACE_CONTENT_MODEL, CATEGORIES);
    
    // Otras propiedades
    public static final String SEPARADOR 	 = "/";
    public static final String SEPARADOR_BUSQUEDA_RECURSIVA = "#";
    public static final String SEPARADOR_BUSQUEDA_RECURSIVA_NODE_INCLUSIVE = "@";
    public static final String SEPARADOR_BUSQUEDA_IGNORAR = "*";
    
    public static final String BUSQUEDA_SIN_PATH_RAIZ = "|>";
    public static final String SEPARADOR_PREFIX_QNAME = ":";
    
    public static final String ENCODING_UTF8  = "UTF-8";
    public static final String ENCODING_ISO  = "ISO-8859-1";
    public static final String ENCODING_CONTENIDO = ENCODING_ISO;    
    
    public static final String ESPACIO_ALFRESCO = "_x0020_";
    
    public static final String CLASIFICACION_GENERAL_CLASSIFIABLE = "{http://www.alfresco.org/model/content/1.0}generalclassifiable";
    
    /**
     * 
     */
    public static String createQNameString(String namespace, String name) {
        return "{" + namespace + "}" + name;
    }
    
}



