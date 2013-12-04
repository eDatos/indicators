-- ---------------------------------------------------------------------------------------------------------------
-- INDISTAC-778 - [BBDD] Nombre semántico para las tablas generadas y acceso sólo a determinados usuarios
-- ---------------------------------------------------------------------------------------------------------------
CREATE ROLE INDICATORS_DATA;

ALTER TABLE TB_INDICATORS ADD VIEW_CODE VARCHAR2(30 CHAR) NOT NULL;
ALTER TABLE TB_INDICATORS ADD CONSTRAINT INDICATORS_CODE_VIEW UNIQUE(VIEW_CODE);