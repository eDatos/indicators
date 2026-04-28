-- --------------------------------------------------------------------------------------------------
-- EDATOS-5608 - Añadir soporte a atributos a nivel de dimensión y de dataset para los indicadores
-- Añadir columna a tabla TB_DATASOURCE_ATTRIBUTE_METADATA de forma incremental.
-- --------------------------------------------------------------------------------------------------

ALTER TABLE TB_DATASOURCE_ATTRIBUTE_METADATA ADD COLUMN ENUM_LABEL_MAP TEXT;

COMMIT;