-- --------------------------------------------------------------------------------------------------
-- EDATOS-3827 - Integración con códigos geográficos de e-Semántica
-- 
-- Borrar las granularidades que no tienen valores geográficos asociados después de la migración
-- --------------------------------------------------------------------------------------------------

 delete from tb_lis_geogr_granularities where id not in(select tlgv.granularity_fk  from tb_lis_geogr_values tlgv );
 
 commit;
