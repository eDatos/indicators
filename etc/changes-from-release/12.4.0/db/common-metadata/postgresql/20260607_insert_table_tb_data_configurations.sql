-- ------------------------------------------------------------------------------------------------------------------------------
-- EDATOS-5725 - Ordenación de dimensión territorio en la salida de la api
-- 
--  Añadir constante que indique el nombre del orden de visualización por defecto de la clasificación de granularidades geográficas registrado en el srm como orden
-- a utilizar en indicadores para ordenar por jerarquía de granularidad
-- ------------------------------------------------------------------------------------------------------------------------------


insert into TB_DATA_CONFIGURATIONS (ID, VERSION, SYSTEM_PROPERTY, CONF_KEY, CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 1, true, 'indicators.geographical_granularity.default_visualisation_order', 'CUSTOM', false);
                 
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';
commit;
