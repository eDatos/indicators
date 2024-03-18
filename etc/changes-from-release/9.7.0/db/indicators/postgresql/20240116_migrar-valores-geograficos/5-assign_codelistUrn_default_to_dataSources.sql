-- --------------------------------------------------------------------------------------------------
-- EDATOS-3827 - Integración con códigos geográficos de e-Semántica
-- 
-- Script para migrar las clasificaciones asociadas a la query de cada fuente de datos.

-- ATENCIÓN!! Para este paso es necesario tener el código de indicadores ya desplegado.
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
