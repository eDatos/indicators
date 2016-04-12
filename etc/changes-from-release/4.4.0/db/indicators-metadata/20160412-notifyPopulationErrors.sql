-- ----------------------------------------------------------------------------------------------------------------------
-- INDISTAC-984 - Permitir gestionar aquellos indicadores de los que no se desean recibir notificaciones por errores
-- ----------------------------------------------------------------------------------------------------------------------

ALTER TABLE TB_INDICATORS ADD NOTIFY_POPULATION_ERRORS NUMBER(1,0) DEFAULT 1 NOT NULL;
COMMIT;