-- --------------------------------------------------------------------------------------------------
-- [EDATOS-4345] Borrar propiedad de configuración de job que desaparece
-- Para la migración de edatos-3827 se crea un job temporal para migrar los codelists asociados a cada fuente de datos de cada indicador.
-- Una vez se realice la migración el código del job se eliminará por lo que hay que eliminar la propiedad de common-metadata que programa el job 
-- --------------------------------------------------------------------------------------------------

-- Eliminar propiedades
delete from tb_data_configurations where conf_key = 'indicators.geographical_values_migration.cron_expression';
commit;