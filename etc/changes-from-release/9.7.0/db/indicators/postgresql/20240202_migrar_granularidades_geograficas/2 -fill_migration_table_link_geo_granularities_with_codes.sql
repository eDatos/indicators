-- --------------------------------------------------------------------------------------------------
-- EDATOS-4376 - Integración con granularidades de e-Semántica
-- 
-- Script con tabla temporal para migración de datos de códigos de granularidad de la última versión de granularidades.

 --!!!!!!!Es necesario tener instalado la extensión uuid-ossp para la generación de los uuids de los elementos insertados 
-- Para ello es necesario ejecutar la siguiente sentencia como administrador de la bbdd CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
-- --------------------------------------------------------------------------------------------------

-- PRECONDICIÓN: Asegurarse de que la tabla de migración está creada en las bases de datos indicadas en el script: 1-create_migration_table_geographical_granularities.sql 

-- 1 Ejecutar la siguiente consulta en la bd del SRM para obtener los códigos de granularidades de la versión de codelist indicada.
--1.1 Habilitar extensión uuid-ossp ejecutando:
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
-- 1.2 Se debe añadir al campo code el valor del código de la clasificación de granularidades que se usa por defecto y que se encuentra en la propiedad
-- del common-metadata "metamac.default.codelist.geographical_granularity.urn"
-- Ej: CL_GRANULARIDADES_GEOGRAFICAS
insert into  temp_mig_geo_granularities(uuid, created_date_tz, created_date, created_by, "version", granularity_code, label_es, label_ca, label_en) 
select uuid_generate_v4(),
 'Europe/London', 
  now(),
'initial-migration', 
  '0', 
  a_codeGranul.code, 
 (select "label" from tb_localised_strings co_title where co_title.international_string_fk = a_codeGranul.name_fk  and co_title.locale = 'es'), 
 (select "label" from tb_localised_strings co_title where co_title.international_string_fk = a_codeGranul.name_fk and co_title.locale = 'ca'),
 (select "label" from tb_localised_strings co_title where co_title.international_string_fk = a_codeGranul.name_fk and co_title.locale = 'en')
 from tb_item_schemes_versions a, tb_annotable_artefacts c, 
 tb_codes codeGranul, tb_annotable_artefacts a_codeGranul
 where  a.maintainable_artefact_fk = c.id
and c.code  = <CODIGO DE metamac.default.codelist.geographical_granularity.urn> -- ej: 'CL_GRANULARIDADES_GEOGRAFICAS'
and c.latest_version_number_public = true   -- Última versión publicada externamente.
and codeGranul.item_scheme_version_fk = a.id
and codeGranul.nameable_artefact_fk  = a_codeGranul.id;

--1.2 Exportar la tabla a CSV.
--ATENCIÓN!! Al exportar si hay valores nulos los convierte a vacío. Lo que puede dar problemas en pasos posteriores. 
--Es por eso que hay que indicar en la exportación que convierta los valores nulos al valor "null" Para ello:
-- 1.2.1 Exportar a CSV
-- 1.2.2 En la última pantalla de exportación, en "Exporting settings" al valor "NULL String" asignarle el valor null

--1.3 Importar la tabla anterior a la tabla "temp_mig_geo_granularities" en indicators.
--ATENCIÓN!! Al importar si hay valores nulos los convierte a vacío. En la exportación se ha puesto "null" como valor asociado a nulo hay que asociarlo a las opciones de importación 
--Es por eso que hay que indicar en la importación que convierta los valores con el valor "null" a NULO Para ello:
-- 1.2.1 Importar a CSV
-- 1.2.2 En la última pantalla de importación, en "Importing settings" al valor "NULL value mark" asignarle el valor null

--1.4 Borrar tabla temp_mig_geo_granularities de la base de datos del srm. Situarse en la base de datos metamac_structural_resources y hacer:
drop table temp_mig_geo_granularities;
 

--2 Ir a la bd indicators. Crear las granularidades en indicators que no existen previamente.
select 
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);' ||
case when t.label_es is not null then '  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || replace(t.label_es, '''', '''''') || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);' else '' end ||
case when t.label_ca is not null then '
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || replace(t.label_ca, '''', '''''') || ''', ''ca'', currval(''SEQ_I18NSTRS''), 1);' else '' end || 
case when t.label_en is not null then '
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || replace(t.label_en, '''', '''''') || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);' else '' end || '
INSERT INTO tb_lis_geogr_granularities(id, code, update_date_tz, update_date, uuid, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version", title_fk)
 values (nextval(''SEQ_GEOGR_GRANULARITIES''),'
|| '''' || t.granularity_code || ''', '
|| 'null, '
|| 'null, '
|| '''' || t.uuid || ''', '
|| '''' || t.created_date_tz || ''', '
|| '''' || t.created_date || ''', '
|| '''' || t.created_by || ''', '
|| '''' || t.created_date_tz || ''', '
|| '''' || t.created_date || ''', '
|| '''' || t.created_by || ''', '
|| '''' || t.version || ''', '
|| 'currval(''SEQ_I18NSTRS'')' 
|| ');'
from  temp_mig_geo_granularities t
where t.granularity_code not in(select code from tb_lis_geogr_granularities tlgg)
;

--3 Ejecutar los scripts generados en el paso anterior en la bd indicators.
--ATENCIÓN El editor de dbeaver parece que pone doble comillas en su configuración por defecto a los inserts y puede eliminar campos entrecomillados como "version"
--Por ello comprobar la confiugración de dbeaver que esté así:
-- En la parte en la que se han obtenido los resultados de la select anterior pulsar botón derecho y seleccionar "Advanced copy" -> "Advanced copy" Asegurarse que las siguientes opciones están desmarcadas:
---- Copy header
---- Copy row Numbers
---- Quote cell values
---- Always quote values
---- Copy as HTML

/*
 *Ejemplo
 INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval('SEQ_I18NSTRS'), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Mundo', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'World', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO tb_lis_geogr_granularities(id, code, update_date_tz, update_date, uuid, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version", title_fk)
 values (nextval('SEQ_GEOGR_VALUES'),'WORLD', null, null, '7381bbd5-40e3-4a13-ac8b-85d6eadcbb27', 'Europe/London', '2024-02-02 08:33:58.809', 'initial-migration', 'Europe/London', '2024-02-02 08:33:58.809', 'initial-migration', '0', currval('SEQ_I18NSTRS'));

 */


--Borrar tabla temporal de migración de la bd indicators.
drop table temp_mig_geo_granularities;