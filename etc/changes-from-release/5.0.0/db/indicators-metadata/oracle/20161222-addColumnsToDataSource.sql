-- ----------------------------------------------------------------------------------------------------------------------
-- METAMAC-2503 - Realizar integración entre staistical-resources e indicators
-- ----------------------------------------------------------------------------------------------------------------------

-- Añadir columna para identificar el origen de los DataSources, las filas actuales son todas del GPE
ALTER TABLE TB_DATA_SOURCES ADD QUERY_ENVIRONMENT VARCHAR2(255 CHAR) DEFAULT 'GPE' NOT NULL;

-- Añadir columna para enlazar un External Item con el que se relaciona en caso de una fuente Query de Metamac
ALTER TABLE TB_DATA_SOURCES ADD STAT_RESOURCE_FK NUMBER(19);

ALTER TABLE TB_DATA_SOURCES ADD CONSTRAINT FK_TB_DATA_SOURCES_QUERY_ART84
    FOREIGN KEY (STAT_RESOURCE_FK) REFERENCES TB_EXTERNAL_ITEMS (ID)
;

-- RENAME PX_URI TO QUERYABLE_URN 
ALTER TABLE TB_DATA_SOURCES RENAME COLUMN PX_URI to QUERY_URN;

-- RENAME DATA_GPE_UUID TO QUERYABLE_UUID
ALTER TABLE TB_DATA_SOURCES RENAME COLUMN DATA_GPE_UUID to QUERY_UUID;


COMMIT;