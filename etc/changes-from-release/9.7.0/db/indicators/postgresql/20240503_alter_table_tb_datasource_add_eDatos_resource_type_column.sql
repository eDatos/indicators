 -- --------------------------------------------------------------------------------------------------
-- EDATOS-3826 - Permitir usar datasets como fuentes de datos de indicadores
-- 
-- Script que añade columna para identificar el tipo de recursos de eDatos que actúa como fuente de datos. Tendrá dos valores posibles
   -- Consulta
   -- Dataset
   
   --Por defecto para todas las fuentes de datos con origen en eDatos se le pondrá "QUERY" porque es lo único que hay hasta ahora.
-- --------------------------------------------------------------------------------------------------

 
ALTER TABLE TB_DATA_SOURCES ADD COLUMN METAMAC_TYPE varchar(255);
 
UPDATE TB_DATA_SOURCES SET METAMAC_TYPE = 'QUERY' WHERE QUERY_ENVIRONMENT = 'METAMAC';

-- Nota adicional: El valor del enumerado correspondiente a la columna QUERY_ENVIRONMENT ha sido renombrado pasando del valor 
-- CONSULTA al valor QUERY para mantener los valores del mismo en inglés. Con la siguiente consulta se pueden adecuar los 
-- registros existentes con el antiguo valor que se hubiesen generado por haber instalado previamente este desarrollo,
-- es decir, la siguiente consulta solo deberá ejecutarse si el desarrollo EDATOS-3826 se instaló previamente en el entorno donde
-- se está desplegando esta versión de la aplicación.

-- UPDATE TB_DATA_SOURCES SET METAMAC_TYPE = 'QUERY' WHERE QUERY_ENVIRONMENT = 'METAMAC' AND METAMAC_TYPE = 'CONSULTA';
 
commit;