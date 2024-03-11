# UPGRADE - Proceso de actualización entre versiones

*Para actualizar de una versión a otra es suficiente con actualizar el WAR a la última versión. El siguiente listado presenta aquellos cambios de versión en los que no es suficiente con actualizar y que requieren por parte del instalador tener más cosas en cuenta. Si el cambio de versión engloba varios cambios de versión del listado, estos han de ejecutarse en orden de más antiguo a más reciente.*

*De esta forma, si tuviéramos una instalación en una versión **A.B.C** y quisieramos actualizar a una versión posterior **X.Y.Z** para la cual existan versiones anteriores que incluyan cambios listados en este documento, se deberá realizar la actualización pasando por todas estas versiones antes de poder llegar a la versión deseada.*

*EJEMPLO: Queremos actualizar desde la versión 1.0.0 a la 3.0.0 y existe un cambio en la base de datos en la actualización de la versión 1.0.0 a la 2.0.0.*

*Se deberá realizar primero la actualización de la versión 1.0.0 a la 2.0.0 y luego desde la 2.0.0 a la 3.0.0*

## 9.7.0 a 9.7.1-SNAPSHOT
* Se añade nueva propiedad de configuración en base de datos common-metadata. Ejecutar los scripts de esta carpeta en la base de datos de common-metadata:

```
etc/changes-from-release/9.7.0/db/common-metadata/postgresql/*.sql
```

* Hay cambios en base de datos de indicators por lo que es necesario ejecutar los scripts que se encuentran en la siguiente carpeta (excepto los scripts que se encuentran dentro de la carpeta "migrar-valores-geograficos" que se ejecutarán en el siguiente paso:

```
etc/changes-from-release/9.7.0/db/indicators/postgresql/*.sql
```

* Se ha realizado un proceso de migración de los valores geográficos cuya gestión desaparece de indicators y se usará, en su lugar los elementos de variable que proceden del srm. Por tanto, será necesario realizar un proceso de migración tanto de la tabla maestra de los valores geográficos como de los valores que se encuentran en las diferentes tablas que la referencian. Para dicha migración será necesario ejecutar los scripts que se encuentran en la carpeta siguiendo los pasos que se indican en cada fichero.

```
etc/changes-from-release/9.7.0/db/indicators/postgresql/migrar-valores-geograficos/*.sql
```


## 9.5.0 a 9.5.1-SNAPSHOT
* Es necesario en srm haber creado un tipo de anotación denominada "SYMBOL_POSITION" desde el menú "Administración" en el srm. Descripción "Posición del símbolo en las unidades de medida"
* Es necesario ejecutar el script "20231001_create_constant_properties_in_common_metadata.sql" que se encuentra en el srm y que crea el metadato "metamac.srm.codelist.annotation.type.position_unit" en common-metadata
* Es necesario ejecutar los scripts SQL contenidos en la carpeta.

```
etc/changes-from-release/9.5.0/db/*.sql

```
* Los scripts de la carpeta "migrar-areas-tematicas" y "migrar-unidades-medida" deben ser los últimos en ejecutarse.
    * Para el caso de la carpeta "migrar-areas-tematicas" ejecutar el siguiente script que indicará, para cada entorno el script que se debe ejecutar: "20231129_COMUN_script_1_convert_subject_code_to_category_element_external_item.sql"
        * Para esta migración, en la carpeta "/indicators/etc/helpers/migrar-areas-tematicas" se pueden encontrar, en diversas subcarpetas, los scripts necesarios por entorno para la migración de áreas temáticas a elementos de tema. Se debe ejecutar en cada entorno el script correspondiente.
    * Para el caso de la carpeta "migrar-unidades-medida" deben ejecutarse los scripts en el orden numerado de cada uno. El script 2, además, hace uso de un script de ayuda que precarga todos los datos necesarios para la migración en una tabla temporal temp_mig_units. Previamente se debe ejecutar este script que se encuentra en "/indicators/etc/helpers/migrar-unidades-medidas/20231005_2_precarga_demo_get_srm_codelist_units_data.sql". En este script se debe cambiar la consulta que se ejecutará en el srm en cada entorno como se indica en el propio script ya que la clasificación usada en cada entorno es diferente.
* Una terminado todo el despliegue para tener actualizada la caché de temas, ir a la aplicación de indicators y, en la vista principal, pulsar el botón "Actualizar caché de temas"*

## 9.4.0 a 9.5.0
* Es necesario ejecutar los scripts SQL contenidos en la carpeta

```
etc/changes-from-release/9.4.0/db/common-metadata/postgresql/*.sql
```

* Ejecutar los scripts contenidos en la carepeta

```
etc/changes-from-release/9.4.0/db/indicators/postgresql/*.sql
```

* En EDATOS-4185 se elimna la tabla tv_areas_tematicas y ahora usará un esquema de temas del srm. Es necesario una migración de valores. Esta migración es diferente por cada entorno. Hay que ejecutar en cada entorno los scripts indicados en cada una de las carpetas que cuelgan de la siguiente raíz:

```
etc/changes-from-release/9.4.0/db/migrar-datos
```


## 9.3.0 a 9.4.0
* Es necesario ejecutar los scripts SQL contenidos en la carpeta

```
etc/changes-from-release/9.3.0/db/common-metadata/postgresql/*.sql
```
* Importante: Esta versión depende de la versión complementos-apps - 5.1.0 para ajustar del todo ciertos estilos.

## 8.5.3 a 9.0.0
* A partir de esta versión de la aplicación se elimina el soporte para bases de datos Oracle o Sql Server, siendo PostgreSQL la única base de datos con soporte.
* Es necesario ejecutar los scripts SQL contenidos en la carpeta

```
etc/changes-from-release/8.5.3/db/indicators/$BD/20211105_add-column-stream-message-status-to-tb_indic_systems_versions.sql
etc/changes-from-release/8.5.3/db/indicators/$BD/20211105_add-column-stream-message-status-to-tb_indicators_versions.sql
```
donde $BD se corresponde con el sistema de gestión de base de datos que esté utilizando.

## 8.4.0 a 8.5.0
* Debido a que la funcionalidad del captcha se ha movido a external-users, dicha funcionalidad estará deshabilitada si edatos-external-users no está instalado. El modo de indicarlo es que la siguiente propiedad no esté inicializada:

```
    metamac.portal.rest.external.authentication.captcha.url
```
* Se han realizado cambios a la base de datos, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/8.4.0/db](etc/changes-from-release/8.4.0/db)
* Para el correcto funcionamiento del soporte de JSON-stat como fuente de datos es necesario añadir al almacén de certificados de la java del tomcat donde se despliega la aplicación el certificado del dominio del cual se obtendrán los ficheros JSON-stat
* A partir de esta versión indicadores se integra con el captcha de edatos-external-users. Para aprovechar estas funcionalidades asegúrese de que dicho proyecto esta instalado.
* Se han realizado cambios en el modo de cargar los indicadores relativos a las dimensiones temporales. Se han de recargar los indicadores para que no haya problemas, especialmente en las instancias de indicador.
* Para ello:
    * Configuramos el needsUpdate de todos los indicadores: 
        `update TB_INDICATORS_VERSIONS set NEEDS_UPDATE='1' WHERE IS_LAST_VERSION = '1';` 
    * Buscamos una consulta del statistical-resources y pulsamos "Reenviar mensaje de publicación"

## 8.3.0 a 8.4.0
* Se han realizado cambios que implican que, previo al despliegue de esta versión en cualquier entorno, se debe realizar la migración de Kafka a la versión 6.1.1.
* Se debe modificar el fichero logback-indicators-internal-web.xml para añadir la siguiente entrada justo después del inicio del tag configuration. La siguiente entrada configura un filtro a nivel de logs que evita que se emitan mensajes de logs duplicados de forma indefinida a los que la nueva versión de Kafka es propenso.

~~~
  <turboFilter class="org.siemac.edatos.core.common.util.ExpiringDuplicateMessageFilter">
	<allowedRepetitions>5</allowedRepetitions>
	<cacheSize>500</cacheSize>
	<expireAfterWriteSeconds>900</expireAfterWriteSeconds>
  </turboFilter>
~~~

## 8.2.3 a 8.3.0
* Se han realizado cambios a la base de datos, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/8.2.3/db](etc/changes-from-release/8.2.3/db)
* Se han realizado cambios a la base de datos PostgreSQL, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha situados dentro del proyecto edatos-dataset-repository: etc/changes-from-release/1.1.0/db/edatos-dataset-repository/postgresql
* Actualizar el WAR

## 8.2.2 a 8.2.3
* Se han realizado cambios a la base de datos, por ello se proveen una serie de scripts SQL para adaptarse a la nueva versión. Ejecutar los scripts de la siguiente ruta en el esquema correspondiente por orden de fecha: [etc/changes-from-release/8.2.2/db](etc/changes-from-release/8.2.2/db)
* Actualizar el WAR

## 0.0.0 a 8.1.1
* El proceso de actualizaciones entre versiones para versiones anteriores a la 8.1.1 está definido en "Metamac - Manual de instalación.doc"
