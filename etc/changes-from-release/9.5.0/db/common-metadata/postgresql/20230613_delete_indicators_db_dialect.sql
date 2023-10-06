-- --------------------------------------------------------------------------------------------------
-- EDATOS-4125 - Eliminar la propiedad del dialecto de BBDD
-- 
-- Se elimina las propiedades con los valores indicators.core.db.dialect y indicators.dsrepo.db.dialect
-- --------------------------------------------------------------------------------------------------

delete from tb_data_configurations where conf_key = 'deprecated.indicators.core.db.dialect';
delete from tb_data_configurations where conf_key = 'deprecated.indicators.dsrepo.db.dialect';
commit;