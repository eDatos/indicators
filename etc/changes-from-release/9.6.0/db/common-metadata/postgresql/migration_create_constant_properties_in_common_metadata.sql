-- --------------------------------------------------------------------------------------------------
-- EDATOS-3827 - Integración con códigos geográficos de e-Semántica
-- 
-- Script para migrar la clasificación utilizada por cada dataset asociado a las consultas de eDatos asociado a los indicadores.
-- Tarea que sólo se ejecutará una vez durante la migración. Por tanto programar una sóla vez para que se ejecute en un momento determinado.
-- --------------------------------------------------------------------------------------------------

-- Add job configuration

-- !! ATTENTION Configure cron expression
-- Ej: '0 30 20 29 AUG ? 2024' --0 30 20 29 AUG ? 2024  29 de agosto de 2024 a las 20:30   
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'indicators.geographical_values_migration.cron_expression',xxxxxx,false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

commit;

----------------------------------------------
--EXAMPLE IN DEV 
-- 0 0 21 * * ? A las 21 cada día
--insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'indicators.geographical_values_migration.cron_expression','0 30 20 29 AUG ? 2024',false);
--UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';
--commit;
---------------------------------------------
