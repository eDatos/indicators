-- --------------------------------------------------------------------------------------------------
-- EDATOS-4185 - Añadir metadato elemento de tema a los indicadores
-- 
-- Se desactivan las siguuientes propiedades:
-- indicators.subjects.db.table
-- indicators.subjects.db.column_code
-- indicators.subjects.db.column_title
-- --------------------------------------------------------------------------------------------------

update tb_data_configurations set conf_key = 'deprecated.indicators.subjects.db.table' where conf_key ='indicators.subjects.db.table';
update tb_data_configurations set conf_key = 'deprecated.indicators.subjects.db.column_code' where conf_key ='indicators.subjects.db.column_code';
update tb_data_configurations set conf_key = 'deprecated.indicators.subjects.db.column_title' where conf_key ='indicators.subjects.db.column_title';
commit;
