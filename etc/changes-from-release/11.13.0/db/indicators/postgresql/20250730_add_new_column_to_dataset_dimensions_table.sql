-- --------------------------------------------------------------------------------------------------
-- EDATOS-5154 - Añadir descripciones a las vistas de datos
-- Añade nueva columna a la tabla tb_dataset_dimensions
-- --------------------------------------------------------------------------------------------------

ALTER TABLE TB_DATASET_DIMENSIONS ADD COLUMN SOURCE_URN VARCHAR(4000);


CREATE INDEX tb_dataset_dimensiones_source_urn ON TB_DATASET_DIMENSIONS (source_urn);


commit;