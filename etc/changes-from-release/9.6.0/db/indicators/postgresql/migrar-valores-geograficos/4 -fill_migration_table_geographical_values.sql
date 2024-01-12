-- --------------------------------------------------------------------------------------------------
-- EDATOS-3827 - Integración con códigos geográficos de e-Semántica
-- 
-- Script para migrar ls

 --!!!!!!!Es necesario tener instalado la extensión uuid-ossp para la generación de los uuids de los elementos insertados 
-- Para ello es necesario ejecutar la siguiente sentencia como administrador de la bbdd CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
-- --------------------------------------------------------------------------------------------------

--1 Obtener las urn de los codelists de cada consulta asociada a las distintas fuentes de datos de los indicadores existentes para rellenar el nuevo campo tb_data_sources.codelisturn
-- Para ello se ha creado un job en indicators que obtiene las consulta del statitiscal-resources y el dsd asociado a partir del cual se puede obtener la urn aprovechando el código existente.
 ----1.1) Añadir a common-metadata la programación de job programado que actualiza la clasificación asociada a los códigos geográficos para las consultas que provienen de eDatos. Para ello ejecutar script indicado en 
 update tb_data_configurations
set conf_value = '0 56 13 21 JAN ? 2024' --0 30 20 29 AUG ? 2023  29 de agosto de 2023 a las 20:30   
where conf_key = 'indicators.geographical_values_migration.cron_expression';
    -- Esperar a que se ejecute el job.
    -- Asegurarse que todas entradas con tipo "METAMAC" tienen valor para el metadato geographical_codelist_urn para ello lanzar la consulta y comprobar que no salen valores.
select * from tb_data_sources tds where query_environment = 'METAMAC' and geographical_codelist_urn  is  null;

 ----1.2) actualizar codelist para  datos que proceden de GPE y JSON_STAT
   -- Obtener urn de codelist no normalizada. Lo tiene que dar Vicky. En local será ( 'urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ISTAC:CL_AREA_GPE_JSONSTAT_ISTAC(01.001)')
   -- Actualizar metadato geographical_codelist_urn
   update tb_data_sources set geographical_codelist_urn = 'urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ISTAC:CL_AREA_GPE_JSONSTAT_ISTAC(01.001)'  where query_environment  in ('GPE', 'JSON_STAT');

-- 2 Obtener la información de todos los elementos de variable asociados del tipo geográfico. Ejecutar la siguiente consulta en la bd del SRM para los codelists indicados. 
--¡¡¡¡¡¡Atención!!!!!! puede dar problemas en dbeaver porque son muchas entradas. Lo que se puede hacer es volcarlo en un fichero (en sublime por ejemplo)
-- y ejecutarlo como script en dbeaver

--2.1 Habilitar extensión uuid-ossp ejecutando:
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 2.2 Ejecutar consulta de la siguiente manera
-- Esta consulta puede dar problemas de rendimiento al hacer el copy. Por lo que se puede optar por exportar la consulta a un fichero. Para ello hacer lo siguiente
-- 1. Seleccionar la  consulta en dbeaver
-- 2. desplegar menú Ejecutar (Execute)
-- 3. Seleccionar submenú Ejecutar desde consulta (Execute from query)
-- 4. Seleccionar como tipo de salida "TXT"
-- 5. Ampliar el fetch size a 300000 que por defecto está en 10000
-- 6. Seleccionar directorio de salida
-- 7. Exportar y generará un fichero con las inserciones en el directorio de  salida.
-- 8. Genera la salida pero con un delimitador entre INSERT. Quitar la primera línea "|?column?    ".  Quitar el delimitador sustituyéndolo por "" en un editor de textos (sublime, visual studio code)

  select ' INSERT INTO temp_mig_geo_values(code, latitude, longitude, global_order, uuid, created_date_tz, created_date, created_by, "version", granularity_code, label_es, label_ca, label_en) VALUES('
|| '''' || t_ve.code || ''', '
|| coalesce('''' || ve.latitude || '''', 'null') || ', '
|| coalesce('''' || ve.longitude || '''', 'null') || ', ' 
|| '''' || t_ve.code || ''', '
|| '''' || uuid_generate_v4() || ''', '
|| '''' || 'Europe/London' || ''', '
||  'now()' || ', '
|| '''' || 'initial-migration' || ''', '
|| '''' || '0' || ''', '
|| '''' || t_granularity.code || ''', '
|| coalesce('''' || (select replace("label", '''', '''''') from tb_localised_strings co_title where co_title.international_string_fk = ve.short_name_fk and co_title.locale = 'es') || '''', 'null') || ','
|| coalesce('''' || (select replace("label", '''', '''''') from tb_localised_strings co_title where co_title.international_string_fk = ve.short_name_fk and co_title.locale = 'ca') || '''', 'null') || ','
|| coalesce('''' || (select replace("label", '''', '''''') from tb_localised_strings co_title where co_title.international_string_fk = ve.short_name_fk and co_title.locale = 'en') || '''', 'null')
|| ');'
 from   tb_m_variables v, tb_annotable_artefacts t_v, tb_m_variable_elements ve, tb_annotable_artefacts t_ve, tb_codes granularity, tb_annotable_artefacts t_granularity
 where
 v.nameable_artefact_fk = t_v.id
 and v.variable_type = 'GEOGRAPHICAL'
 and t_v.code = 'VR_TERRITORIO'
 and ve.variable_fk = v.id 
and ve.identifiable_artefact_fk = t_ve.id 
and ve.geographical_granularity_fk = granularity.id 
and granularity.nameable_artefact_fk  = t_granularity.id; 


--3) Ejecutar las consultas del proceso anterior en la base de datos indicators tabla. Se puede hacer con un DUMP 
--3.1  Ir a consola de comandos y ejecutar la siguiente sentencia (hay que situarse en carpeta  con dump si no está mapeado ej: E:\program files\PostgreSQL\14\bin )
psql -U "indicators_bd" -W -h localhost indicators_bd < E:\mig\data_geo_values.txt


--4) Obtener la asociación de granularity_code con su id en la tabla "tb_lis_geogr_granularities" de tb_indicators
update temp_mig_geo_values mig
set granularity_fk = (select g.id from tb_lis_geogr_granularities g where g.code = mig.granularity_code);

-- 4.1 Comprobar la correcta asociación de granularities
select case when (select count(*) from temp_mig_geo_values where granularity_fk is null) = 0 then 'PROCESO CORRECTO' else 'ERROR. HAY GRANULARIDADES QUE NO SE HAN ENCONTRADO EN tb_lis_geogr_granularities' end; 

--Si sale algún valor hay que hablar con equipo de auditoría para solucionarlo y darle valor en administración de indicadores (actualmente las granularidades no están migradas aunque se espera que estén en el futuro)

 