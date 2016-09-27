--------------------------------------------------------------------------------
-- INDISTAC-986 - Eliminar propiedades de configuración que se han quedado obsoletas
--------------------------------------------------------------------------------

DELETE FROM TB_DATA_CONFIGURATIONS WHERE CONF_KEY = 'indicators.data.path';
DELETE FROM TB_DATA_CONFIGURATIONS WHERE CONF_KEY = 'indicators.data.docs.path';

commit;