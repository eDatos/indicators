-- --------------------------------------------------------------------------------------------------
-- EDATOS-4550 - Inclusión de metadato en indicadores para establecer si es un indicador principal
-- 
--  Crear nuevo campo indicador principal para un indicador.
-- --------------------------------------------------------------------------------------------------


ALTER TABLE TB_INDICATORS_VERSIONS
    ADD IS_MAIN_INDICATOR BOOL DEFAULT FALSE NOT NULL;

commit;