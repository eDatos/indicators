-- --------------------------------------------------------------------------------------------------
-- INDISTAC-960 - Lentitud en el aplicativo de interno de indicadores
-- --------------------------------------------------------------------------------------------------

-- Eliminar datasource de subjects
DELETE FROM TB_DATA_CONFIGURATIONS WHERE CONF_KEY = 'indicators.subjects.db.url';
DELETE FROM TB_DATA_CONFIGURATIONS WHERE CONF_KEY = 'indicators.subjects.db.driver_name';
DELETE FROM TB_DATA_CONFIGURATIONS WHERE CONF_KEY = 'indicators.subjects.db.username';
DELETE FROM TB_DATA_CONFIGURATIONS WHERE CONF_KEY = 'indicators.subjects.db.password';

commit;