 -- --------------------------------------------------------------------------------------------------
-- EDATOS-3826 - Permitir usar datasets como fuentes de datos de indicadores
-- 
-- Script que añade columna para identificar el tipo de recursos de eDatos que actúa como fuente de datos. Tendrá dos valores posibles
   -- Consulta
   -- Dataset
   
   --Por defecto para todas las fuentes de datos con origen en eDatos se le pondrá "CONSULTA" porque es lo único que hay hasta ahora.
-- --------------------------------------------------------------------------------------------------

 
 ALTER TABLE TB_DATA_SOURCES ADD COLUMN METAMAC_TYPE varchar(255);
 
 
 UPDATE TB_DATA_SOURCES SET METAMAC_TYPE = 'CONSULTA' WHERE QUERY_ENVIRONMENT = 'METAMAC';
 
 commit;