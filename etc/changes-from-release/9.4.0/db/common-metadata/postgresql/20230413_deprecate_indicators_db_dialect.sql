-- --------------------------------------------------------------------------------------------------
-- EDATOS-2234 - [CORE] Comprobar si se puede eliminar el dialecto de las propiedades de BBDD de todas las aplicaciones
-- 
-- Se depreca la propiedad con el valor metamac.access_control.db.dialect
-- --------------------------------------------------------------------------------------------------

update tb_data_configurations set conf_key = 'deprecated.indicators.core.db.dialect' where conf_key ='indicators.core.db.dialect';
commit;

update tb_data_configurations set conf_key = 'deprecated.indicators.dsrepo.db.dialect' where conf_key ='indicators.dsrepo.db.dialect';
commit;