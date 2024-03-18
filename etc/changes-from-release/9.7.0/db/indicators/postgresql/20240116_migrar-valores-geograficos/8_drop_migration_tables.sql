-- --------------------------------------------------------------------------------------------------
-- EDATOS-3827 - Integración con códigos geográficos de e-Semántica
-- 
-- Borrar tablas auxiliares creadas para la migración.
-- --------------------------------------------------------------------------------------------------

 --1) Borrar tablas en base de datos indicators
 DROP TABLE temp_mig_geo_values;
 DROP TABLE temp_mig_codes_with_var_element;
 
  --2) Borrar tablas en base de datos indicators_data
 DROP TABLE temp_mig_codes_with_var_element;