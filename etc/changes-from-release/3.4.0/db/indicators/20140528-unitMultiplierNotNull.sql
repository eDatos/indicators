-- -------------------------------------------------------------------------
-- INDISTAC-863 - Evitar que el unitMultiplier pueda ser nulo en BBDD
-- -------------------------------------------------------------------------

ALTER TABLE TB_QUANTITIES MODIFY UNIT_MULTIPLIER NUMBER(10) NOT NULL;