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
   -- Obtener urn de codelist no normalizada. Lo tiene que dar Vicky. Se ha decidido que sea CL_NN_VALORES_GEOGRAFICOS
   -- Actualizar metadato geographical_codelist_urn
   update tb_data_sources set geographical_codelist_urn = 'urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ISTAC:CL_NN_VALORES_GEOGRAFICOS(XXX)'  where query_environment  in ('GPE', 'JSON_STAT');

-- 2 Obtener la información de todos los elementos de variable asociados del tipo geográfico. Ejecutar la siguiente consulta en la bd del SRM para los codelists indicados. 
--2.1 Habilitar extensión uuid-ossp ejecutando:
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
-- 2.2 Rellenar la tabla "temp_mig_geo_values" de la base de datos metamac_structural_resources con la siguiente consulta
  INSERT INTO temp_mig_geo_values(code, latitude, longitude, global_order, uuid, created_date_tz, created_date, created_by, "version", granularity_code, label_es, label_ca, label_en)
select 
t_ve.code, 
ve.latitude,
ve.longitude,
 t_ve.code,
 uuid_generate_v4(), 
 'Europe/London', 
  now(),
'initial-migration', 
  '0', 
  t_granularity.code, 
 (select "label" from tb_localised_strings co_title where co_title.international_string_fk = ve.short_name_fk and co_title.locale = 'es'), 
 (select "label" from tb_localised_strings co_title where co_title.international_string_fk = ve.short_name_fk and co_title.locale = 'ca'),
 (select "label" from tb_localised_strings co_title where co_title.international_string_fk = ve.short_name_fk and co_title.locale = 'en')
 from   tb_m_variables v, tb_annotable_artefacts t_v, tb_m_variable_elements ve, tb_annotable_artefacts t_ve, tb_codes granularity, tb_annotable_artefacts t_granularity
 where
 v.nameable_artefact_fk = t_v.id
 and v.variable_type = 'GEOGRAPHICAL'
 and t_v.code = 'TERRITORIO'
 and ve.variable_fk = v.id 
and ve.identifiable_artefact_fk = t_ve.id 
and ve.geographical_granularity_fk = granularity.id 
and granularity.nameable_artefact_fk  = t_granularity.id;
--2.3 Exportar la tabla a CSV.
--2.4 Importar la tabla anterior a la tabla "temp_mig_geo_values" en indicators.
--2.5 Borrar tabla temp_mig_geo_values de la base de datos del srm. Situarse en la base de datos metamac_structural_resources y hacer:
drop table temp_mig_geo_values;

--3) Obtener la asociación de granularity_code con su id en la tabla "tb_lis_geogr_granularities" de tb_indicators
-- 3.1 Comprobar que se encuentran las siguientes granularidades
---- DISTRITOS
---- SECCIONES
----_O
----GEOGRAPHICAL_ZONES
-- Si no están, añadirlas desde administración
--3.2 asociación de granularidades. Ejecutar la siguiente sentencia:
update temp_mig_geo_values mig
set granularity_fk = (select g.id from tb_lis_geogr_granularities g where g.code = mig.granularity_code);

-- 3.3 Comprobar la correcta asociación de granularities
select case when (select count(*) from temp_mig_geo_values where granularity_fk is null) = 0 then 'PROCESO CORRECTO' else 'ERROR. HAY GRANULARIDADES QUE NO SE HAN ENCONTRADO EN tb_lis_geogr_granularities' end; 

--Si sale algún valor hay que hablar con equipo de auditoría para solucionarlo y darle valor en administración de indicadores (actualmente las granularidades no están migradas aunque se espera que estén en el futuro)
----Se detecta que faltan DISTRITOS Y SECCIONES POR SEPARADO.

 