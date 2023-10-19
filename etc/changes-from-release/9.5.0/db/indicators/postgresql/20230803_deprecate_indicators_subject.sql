-- --------------------------------------------------------------------------------------------------
-- EDATOS-4185 - Añadir metadato elemento de tema a los indicadores
-- 
-- Se depreca el campo subject_code y subject_title 
-- --------------------------------------------------------------------------------------------------

ALTER TABLE TB_INDICATORS_VERSIONS RENAME COLUMN subject_code TO deprecated_subject_code;
ALTER TABLE TB_INDICATORS_VERSIONS RENAME COLUMN subject_title_fk TO deprecated_subject_title_fk;

ALTER TABLE TB_INDICATORS_VERSIONS ALTER COLUMN deprecated_subject_code drop not null;
ALTER TABLE TB_INDICATORS_VERSIONS ALTER COLUMN deprecated_subject_title_fk drop not null;
commit;