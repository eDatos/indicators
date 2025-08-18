-- --------------------------------------------------------------------------------------------------
-- EDATOS-5154 - Añadir descripciones a las vistas de datos
-- 
-- Script para rellenar los datos de las nuevas tablas para almacenar información de las clasificaciones de la variable VR_TERRITORIO así como la claificación ficticia para la dimensión de medida
-- del srm.

-- --------------------------------------------------------------------------------------------------

--0 Añadir extensión si no existe en el esquema extensions de la bd statistical-resources-data
--0.1) Ver si existe en esquema extensions
SELECT *
FROM pg_extension e
JOIN pg_namespace n ON e.extnamespace = n.oid
WHERE e.extname = 'uuid-ossp'
  AND n.nspname = 'extensions';
-- 0.2)  Si no existe crear en ese esquema
/*
CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA extensions;

OJO! PUEDE QUE EXISE EN ESQUEMA de la bd. Comprobar 
SELECT *
FROM pg_extension e
JOIN pg_namespace n ON e.extnamespace = n.oid
WHERE e.extname = 'uuid-ossp';

Y si se quiere pasar a esquema extensions:
--CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
--ALTER EXTENSION "uuid-ossp" SET SCHEMA extensions;
*/


--1) 
--1.1) Crear la tabla temporal en la bd del srm y en la base de datos de indicators_data
 create table temp_srm_codes 
 (code varchar(255),
  urn varchar(4000),
  type varchar(255),
  code_title_es varchar(4000),
  code_title_ca varchar(4000),
  code_title_en varchar(4000),
  element_code varchar(255)
  );
 
 
 ---------A ejecutar en la bd del SRM-------------------
--1.2) Rellenar la tabla anterior con todas las clasificaciones y esquemas de conceptos existentes
--1.2.1) Clasificaciones
c   insert into temp_srm_codes(code, urn, type, code_title_es, code_title_ca, code_title_en, element_code) 
  select  n.code, n1.urn, 'structuralResources#codelist',
(select label from tb_localised_strings l where l.international_string_fk = n.name_fk and l.locale = 'es') as code_title_es,
(select label from tb_localised_strings l where l.international_string_fk = n.name_fk and l.locale = 'ca') as code_title_ca,
(select label from tb_localised_strings l where l.international_string_fk = n.name_fk and l.locale = 'en') as code_title_en,
vea.code
 from TB_M_CODES cm
 inner join TB_CODES c on c.id = cm.tb_codes 
 inner join tb_annotable_artefacts n on c.nameable_artefact_fk =  n.id
 inner join tb_item_schemes_versions iv on c.item_scheme_version_fk = iv.id
 inner join tb_annotable_artefacts n1 on iv.maintainable_artefact_fk =  n1.id
 inner join tb_m_variable_elements ve on cm.variable_element_fk = ve.id
 inner join tb_annotable_artefacts vea on ve.identifiable_artefact_fk =  vea.id
 inner join tb_m_variables v on ve.variable_fk = v.id
inner join tb_annotable_artefacts va on v.nameable_artefact_fk =  va.id
 where va.code = 'VR_TERRITORIO';
 
 --1.2.2) Clasificación con los códigos de medida para la dimensión de medida en indicadores.
 --sustituir XXX por el valor del recurso para la urn que contiene el nuevo parámetro de common-metadata "metamac.indicators.measure_values.default_codelist_urn"
 -- EJ si urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ISTAC:CL_INDICATOR_MEASURE_VALUES(01.000) se sustituirá XXX por el valor "CL_INDICATOR_MEASURE_VALUES"
  insert into temp_srm_codes(code, urn, type, code_title_es, code_title_ca, code_title_en, element_code) 
  select  n.code, n1.urn, 'structuralResources#codelist',
(select label from tb_localised_strings l where l.international_string_fk = n.name_fk and l.locale = 'es') as code_title_es,
(select label from tb_localised_strings l where l.international_string_fk = n.name_fk and l.locale = 'ca') as code_title_ca,
(select label from tb_localised_strings l where l.international_string_fk = n.name_fk and l.locale = 'en') as code_title_en,
null
 from TB_M_CODES cm
 inner join TB_CODES c on c.id = cm.tb_codes 
 inner join tb_annotable_artefacts n on c.nameable_artefact_fk =  n.id
 inner join tb_item_schemes_versions iv on c.item_scheme_version_fk = iv.id
 inner join tb_annotable_artefacts n1 on iv.maintainable_artefact_fk =  n1.id
 where n1.code = XXX;
 
 
 --1.3) Exportar los datos a fichero tsv.
 --ATENCIÓN!! Al exportar si hay valores nulos los convierte a vacío. Lo que puede dar problemas en pasos posteriores. 
--Es por eso que hay que indicar en la exportación que convierta los valores nulos al valor "null" Para ello:
-- 1.3.1 Exportar a CSV
-- 1.3.2 En la última pantalla de exportación, en "Exporting settings" al valor "NULL String" asignarle el valor null

--1.4 Importar la tabla anterior a la tabla "temp_srm_codes" a la base de datos de  indicators_data.
--ATENCIÓN!! Al importar si hay valores nulos los convierte a vacío. En la exportación se ha puesto "null" como valor asociado a nulo hay que asociarlo a las opciones de importación 
--Es por eso que hay que indicar en la importación que convierta los valores con el valor "null" a NULO Para ello:
-- 1.4.1 Importar a CSV
-- 1.4.2 En la última pantalla de importación, en "Importing settings" al valor "NULL value mark" asignarle el valor null

--1.4 Borrar tabla temp_srm_codes de la base de datos del srm. Situarse en la base de datos  de srm  y hacer:
drop table temp_srm_codes;


-------------A EJECUTAR EN BD DE INDICATORS-DATABASE

--2) Obtener los insert a realizar en la bd data a partir de la tabla temp_srm_codes
--2.1 Ejecutar la siguient consulta
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, "uuid", VERSION) VALUES (nextval(''seq_i18nstrs''), extensions.uuid_generate_v4(), 0);' ||  
case when c.code_title_es is not null then '  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION, "uuid") values (nextval(''SEQ_L10NSTRS''), ''' || replace(c.code_title_es, '''', '''''') || ''', ''es'', currval(''SEQ_I18NSTRS''), 1, extensions.uuid_generate_v4());' else '' end ||
case when c.code_title_ca is not null then '
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION, "uuid") values (nextval(''SEQ_L10NSTRS''), ''' || replace(c.code_title_ca, '''', '''''') || ''', ''ca'', currval(''SEQ_I18NSTRS''), 1, extensions.uuid_generate_v4());' else '' end || 
case when c.code_title_en is not null then '
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION, "uuid") values (nextval(''SEQ_L10NSTRS''), ''' || replace(c.code_title_en, '''', '''''') || ''', ''en'', currval(''SEQ_I18NSTRS''), 1, extensions.uuid_generate_v4());' else '' end || '
INSERT INTO tb_external_items
(id, "type", urn, "uuid", "version", creation_date_tz, creation_date, last_update_date_tz, last_update_date)
 VALUES(nextval(''SEQ_EXTERNAL_ITEMS''),'
|| '''' || c.type || ''', '
|| '''' || c.urn || ''', '
|| ' extensions.uuid_generate_v4(), 0, ''Europe/London'', current_timestamp, null, null);
INSERT INTO tb_external_items_codes
(id, "uuid", "version", external_item_fk, code, title_fk, element_code )
 VALUES(nextval(''SEQ_EXTERNAL_ITEMS_CODES''), extensions.uuid_generate_v4(), 0, currval(''SEQ_EXTERNAL_ITEMS''),'
|| '''' || c.code || ''', ' ||
'currval(''seq_i18nstrs''), '
|| '''' || c.element_code || ''');' 
from temp_srm_codes c;

--2.2 Ejecutar el resultado obtenido en el apartado anterior. El resultado puede ser bastante elevado así que evaluar si lanzarlo en servidor directamente y no en dbeaver.
-- Esta consulta puede dar problemas de rendimiento al hacer el copy. Por lo que se puede optar por exportar la consulta a un fichero. Para ello hacer lo siguiente
-- 2.2.1. Seleccionar la  consulta en dbeaver
-- 2.2.2. desplegar menú Ejecutar (Execute)
-- 2.2.3. Seleccionar submenú Exportar desde consulta (Execute from query)
-- 2.2.4. Seleccionar como tipo de salida "TXT"
-- 2.2.5. Ampliar el fetch size a 300000 que por defecto está en 10000
-- 2.2.6. Seleccionar directorio de salida
-- 2.2.7. Exportar y generará un fichero con las inserciones en el directorio de  salida.
-- 2.2.8. Genera la salida pero con un delimitador entre INSERT.
----2.2.8.1 Quitar la primera línea "|?column?    ".  
----2.2.8.2 Quitar el delimitador "¶" sustituyéndolo por "" en un editor de textos (sublime, visual studio code)
----2.2.8.3 Ir a consola de comandos y ejecutar la siguiente sentencia (donde estén los comandos para el dump. Por eje. en local hay que situarse en carpeta  con dump si no está mapeado ej: E:\program files\PostgreSQL\14\bin )
psql -U "indicators_data_bd" -W -h localhost indicators_data_bd < E:\mig\<NOMBRE_FICHERO_CREADO>
--EJ:  psql -U "indicators_data_bd" -W -h localhost -p 5432 indicators_data_bd < E:\mig\temp_srm_codes_data_indicators.sql
-- tiempo estimado: en desarrollo tardó 3 horas y 15 con un fichero con 69214 entradas

--2.3) Comprobar que el número de entradas en la tabla tb_external_items coincide con la de temp_srm_codes


--3) Si todo fue bien, borrar la tabla temporal de indicators-data
drop table temp_srm_codes;

--4 Seguir con la ejecución del el script 2-20250730_common_metadata_cron_expression_for_temporal_job.sql 
