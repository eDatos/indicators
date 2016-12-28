-- ----------------------------------------------------------------------------------------------------------------------
-- METAMAC-2503 - Realizar integración entre staistical-resources e indicators
-- ----------------------------------------------------------------------------------------------------------------------

-- Añadir columna para identificar el origen de los DataSources, las filas actuales son todas del GPE
ALTER TABLE TB_DATA_SOURCES ADD QUERY_ENVIRONMENT VARCHAR2(255 CHAR) DEFAULT 'GPE' NOT NULL;

-- Añadir columna para enlazar un External Item con el que se relaciona en caso de una fuente Query de Metamac
ALTER TABLE TB_DATA_SOURCES ADD QUERY_ARTEFACT_FK NUMBER(19);

ALTER TABLE TB_DATA_SOURCES ADD CONSTRAINT FK_TB_DATA_SOURCES_QUERY_ART84
    FOREIGN KEY (QUERY_ARTEFACT_FK) REFERENCES TB_EXTERNAL_ITEMS (ID)
;

COMMIT;

