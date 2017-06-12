--------------------------------------------------------------------------------
-- INDISTAC-1023 - Utilizar el visualizador de eDatos para visualizar los datos de indicadores
--------------------------------------------------------------------------------

DELETE FROM TB_DATA_CONFIGURATIONS WHERE CONF_KEY = 'indicators.jaxi.remote.url';
DELETE FROM TB_DATA_CONFIGURATIONS WHERE CONF_KEY = 'indicators.jaxi.remote.url.indicator';
DELETE FROM TB_DATA_CONFIGURATIONS WHERE CONF_KEY = 'indicators.jaxi.local.url.indicator';
DELETE FROM TB_DATA_CONFIGURATIONS WHERE CONF_KEY = 'indicators.jaxi.local.url.instance';

commit;