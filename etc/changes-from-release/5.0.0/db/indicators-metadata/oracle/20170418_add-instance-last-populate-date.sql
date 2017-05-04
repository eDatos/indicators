-- --------------------------------------------------------------------------------------------------------------------
-- INDISTAC-996 - El widget de últimos indicadores actualizados no funciona correctamente cuando se accede por sistema
-- --------------------------------------------------------------------------------------------------------------------

ALTER TABLE TB_INDICATORS_INSTANCES ADD (
  LAST_POPULATE_DATE_TZ VARCHAR2(50 CHAR),
  LAST_POPULATE_DATE TIMESTAMP
);

UPDATE TB_INDICATORS_INSTANCES SET LAST_POPULATE_DATE_TZ = LAST_UPDATED_TZ;
UPDATE TB_INDICATORS_INSTANCES SET LAST_POPULATE_DATE = LAST_UPDATED;

commit;