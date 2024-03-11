 -- --------------------------------------------------------------------------------------------------
-- EDATOS-3827 - Integración con códigos geográficos de e-Semántica
-- 
-- Script que añade columna para identificar la clasificación asociada los códigos geográficos.
-- --------------------------------------------------------------------------------------------------

 
 ALTER TABLE TB_DATA_SOURCES ADD COLUMN GEOGRAPHICAL_CODELIST_URN varchar(4000);
 
 CREATE INDEX IX_TB_DATA_SOURCES_GEOGRAPHICAL_CODELIST_URN_FK ON TB_DATA_SOURCES(GEOGRAPHICAL_CODELIST_URN);

 
 commit;
  
