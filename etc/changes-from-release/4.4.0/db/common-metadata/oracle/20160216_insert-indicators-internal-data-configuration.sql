-- --------------------------------------------------------------------------------------------------
-- INDISTAC-972 - [data configurations] Crear una propiedad para almacenar el endpoint de la api interna de indicadores así como de la web interna
-- --------------------------------------------------------------------------------------------------

-- Añadir propiedad con el endpoint de la api interna de indicadores
-- Ejemplo: http://estadisticas.arte-consultores.com/indicators-internal/internal/api/indicators
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,1,'indicators.rest.internal','http://FILL_ME_WITH_INDICATORS_INTERNAL_WAR/internal/api/indicators');
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

-- Añadir propiedad con el endpoint de la web interna de indicadores 
-- Ejemplo: http://estadisticas.arte-consultores.com/indicators-internal
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,1,'indicators.web.internal.url','http://FILL_ME_WITH_INDICATORS_INTERNAL_WAR');
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

commit;

