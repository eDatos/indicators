-- --------------------------------------------------------------------------------------------------
-- [EDATOS-4345] Borrar campos no usados en indicadores

-- Se eliminan los campos deprecados a partir de tarea EDATOS-3827
-- --------------------------------------------------------------------------------------------------

-- Eliminar columnas
alter table tb_data_sources  DROP deprecated_geographical_value_fk;
alter table tb_indic_inst_geo_values   DROP deprecated_geographical_value_fk;
alter table tb_ind_version_geo_cov   DROP deprecated_geographical_value_fk;
alter table tb_quantities   DROP deprecated_base_location_fk;
alter table tb_indic_inst_last_value   DROP deprecated_geographical_code;
alter table tb_indic_version_last_value   DROP deprecated_geographical_code;


commit;