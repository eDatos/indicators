-- --------------------------------------------------------------------------------------------------
-- [EDATOS-4345] Borrar campos no usados en indicadores

-- Se eliminan los campos deprecados a partir de tarea EDATOS-3827
-- --------------------------------------------------------------------------------------------------

-- Eliminar tablas temporales. Atención!! alguna de las tablas han podido ser borradas después del proceso de migración. Pero se comprueban aquí por asegurar que no hubo ningún olvido.

-- en bd indicators:
DROP TABLE IF EXISTS temp_tb_lis_geogr_values;
DROP TABLE IF EXISTS temp_mig_geo_values;
DROP TABLE IF EXISTS temp_mig_codes_with_var_element;
DROP TABLE IF EXISTS tb_lis_geogr_values_copy; -- to do backup copy before to csv file.
DROP TABLE IF EXISTS temp_mig_geo_granularities; 


-- en bd indicators_data:
DROP TABLE IF EXISTS temp_mig_codes_with_var_element;


commit;