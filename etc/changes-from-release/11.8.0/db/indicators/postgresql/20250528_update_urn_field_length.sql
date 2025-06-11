-- --------------------------------------------------------------------------------------------------
-- EDATOS-5034 - Número de caracteres admitidos como contenido en una dimensión
-- 

-- Ampliar la urn en las tablas a 4000 caracteres

-- --------------------------------------------------------------------------------------------------
  
ALTER TABLE TB_DATA_SOURCES ALTER COLUMN QUERY_UUID  TYPE VARCHAR(4000);
ALTER TABLE TB_DATA_SOURCES ALTER COLUMN QUERY_URN  TYPE VARCHAR(4000);
ALTER TABLE TB_DATA_SOURCES ALTER COLUMN GEOGRAPHICAL_CODELIST_URN  TYPE VARCHAR(4000);



COMMIT;