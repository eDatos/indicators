-- --------------------------------------------------------------------------------------------------
-- EDATOS-5034 - Número de caracteres admitidos como contenido en una dimensión
-- 

-- Ampliar los id de atributos y dimensiones así como el contenido de atributos y dimensiones para que acepten hasta 255 caracteres.

-- --------------------------------------------------------------------------------------------------
  
  
-- ATENCIÓN!!!!! Lanzar en la bd indicators_data   
  
ALTER TABLE TB_ATTRIBUTES ALTER COLUMN ATTRIBUTE_ID  TYPE VARCHAR(255);

ALTER TABLE TB_ATTRIBUTE_DIMENSIONS ALTER COLUMN DIMENSION_ID  TYPE VARCHAR(255);
ALTER TABLE TB_ATTRIBUTE_DIMENSIONS ALTER COLUMN CODE_DIMENSION_ID  TYPE VARCHAR(255);

ALTER TABLE TB_DATASET_ATTRIBUTES ALTER COLUMN ATTRIBUTE_ID  type VARCHAR(255);

ALTER TABLE TB_DATASET_DIMENSIONS ALTER COLUMN DIMENSION_ID  type VARCHAR(255);


COMMIT;