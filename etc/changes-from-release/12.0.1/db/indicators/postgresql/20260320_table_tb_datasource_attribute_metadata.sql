-- --------------------------------------------------------------------------------------------------
-- EDATOS-5608 - Añadir soporte a atributos a nivel de dimensión y de dataset para los indicadores
-- Migración: reemplaza la tabla TB_DATASOURCE_ATTRIBUTE_METADATA (clave DATA_REPOSITORY_ID volátil)
-- por una nueva estructura asociada a TB_DATA_SOURCES (FK estable), siguiendo el patrón de
-- TB_DATA_SOURCES_VARIABLES.
-- --------------------------------------------------------------------------------------------------

--EJECUTAR en la bd indicators_bd

-- Eliminar la tabla anterior si existe (puede haber sido creada por una versión previa del script)
DROP TABLE IF EXISTS TB_DATASOURCE_ATTRIBUTE_METADATA;

CREATE TABLE TB_DATASOURCE_ATTRIBUTE_METADATA (
    ID               BIGINT        NOT NULL,
    ATTRIBUTE_ID     VARCHAR(255)  NOT NULL,
    IS_MULTILINGUAL  BOOLEAN       NOT NULL DEFAULT FALSE,
    ENUM_LABEL       VARCHAR(4000),
    UUID             VARCHAR(36)   NOT NULL,
    CREATED_DATE     TIMESTAMP,
    CREATED_DATE_TZ  VARCHAR(50),
    CREATED_BY       VARCHAR(50),
    LAST_UPDATED     TIMESTAMP,
    LAST_UPDATED_TZ  VARCHAR(50),
    LAST_UPDATED_BY  VARCHAR(50),
    VERSION          BIGINT        NOT NULL DEFAULT 0,
    DATA_SOURCE_FK   BIGINT
);

ALTER TABLE TB_DATASOURCE_ATTRIBUTE_METADATA
    ADD CONSTRAINT PK_TB_DATASOURCE_ATTR_METADATA
    PRIMARY KEY (ID);

ALTER TABLE TB_DATASOURCE_ATTRIBUTE_METADATA
    ADD CONSTRAINT UQ_TB_DATASOURCE_ATTR_META_UUID UNIQUE (UUID);

ALTER TABLE TB_DATASOURCE_ATTRIBUTE_METADATA
    ADD CONSTRAINT FK_TB_DS_ATTR_META_DATA_SOURCE
    FOREIGN KEY (DATA_SOURCE_FK) REFERENCES TB_DATA_SOURCES (ID);

CREATE SEQUENCE SEQ_DATASOURCE_ATTRIBUTE_METADATA;

COMMIT;
