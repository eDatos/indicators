--------------------------------------------------------------------------------
-- INDISTAC-981 - Añadir el botón de previsualizar datos al entorno de difusión
--------------------------------------------------------------------------------

-- IMPORTANTE modificar el FILL_ME. 
-- Ejemplo de valor: http://estadisticas.arte-consultores.com/jaxi-ciber/tabla.do?indicador=[INDICATOR]
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,1,'indicators.jaxi.remote.url.indicator','http://FILL_ME/tabla.do?indicador=[INDICATOR]');
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

commit;
