-- ----------------------------------------------------------------------------------------------------------------------
-- INDISTAC-984 - Permitir gestionar aquellos indicadores de los que no se desean recibir notificaciones por errores
-- ----------------------------------------------------------------------------------------------------------------------

ALTER TABLE TB_INDICATORS ALTER COLUMN NOTIFY_POPULATION_ERRORS SET DEFAULT true;
