-- ---------------------------------------------------------------------------------------------------------------
-- INDISTAC-778 - [BBDD] Nombre semántico para las tablas generadas y acceso sólo a determinados usuarios
-- ---------------------------------------------------------------------------------------------------------------
-- The role has to be the same specified in DATA
CREATE ROLE <FILL_ME_WITH_ROLE_NAME>;

ALTER TABLE TB_INDICATORS ADD VIEW_CODE VARCHAR2(30 CHAR);

update TB_INDICATORS set VIEW_CODE = 'DV_'||substr(CODE,0,27);

ALTER TABLE TB_INDICATORS MODIFY VIEW_CODE VARCHAR2(30 CHAR) NOT NULL;

ALTER TABLE TB_INDICATORS ADD CONSTRAINT INDICATORS_CODE_VIEW UNIQUE(VIEW_CODE);