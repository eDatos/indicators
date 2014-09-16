-- -----------------------------------------------------------------------------------------
-- INDISTAC-870 - Separar el WAR de la APP externa en dos WAR: APP y API
-- -----------------------------------------------------------------------------------------

SET DEFINE OFF

-- Ejemplo: http://<server>/<app>/api/indicators
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,1,'indicators.rest.external','FILL_ME');
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

SET DEFINE ON

commit;