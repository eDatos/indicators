-- --------------------------------------------------------------------------------------------------
-- [EDATOS-4334] Borrar campos no usados en indicadores

-- Se eliminan propiedades no usadas en common-metadata.
-- --------------------------------------------------------------------------------------------------

-- Eliminar propiedades
delete from tb_data_configurations where conf_key = 'deprecated.indicators.subjects.db.table';
delete from tb_data_configurations where conf_key = 'deprecated.indicators.subjects.db.column_code';
delete from tb_data_configurations where conf_key = 'deprecated.indicators.subjects.db.column_title';
commit;