-- --------------------------------------------------------------------------------------------------
-- [EDATOS-4334] Borrar campos no usados en indicadores

-- Se eliminan los campos deprecados a partir de tareas EDATOS-4185  y EDATOS-4197
-- --------------------------------------------------------------------------------------------------

-- Eliminar columnas
alter table TB_INDICATORS_VERSIONS DROP deprecated_subject_code;
alter table TB_INDICATORS_VERSIONS DROP deprecated_subject_title_fk;
alter table TB_QUANTITIES DROP deprecated_unit_fk;

commit;