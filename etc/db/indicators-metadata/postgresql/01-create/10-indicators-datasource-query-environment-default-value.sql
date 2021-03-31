-- ----------------------------------------------------------------------------------------------------------------------
-- METAMAC-2503 - Realizar integración entre staistical-resources e indicators
-- ----------------------------------------------------------------------------------------------------------------------

ALTER TABLE TB_DATA_SOURCES ALTER COLUMN QUERY_ENVIRONMENT SET DEFAULT 'GPE';
