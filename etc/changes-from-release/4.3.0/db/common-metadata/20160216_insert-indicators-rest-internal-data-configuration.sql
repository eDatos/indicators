-- --------------------------------------------------------------------------------------------------
-- INDISTAC-972 - [data configurations] Crear una propiedad para almacenar el endpoint de la api interna de indicadores
-- --------------------------------------------------------------------------------------------------

-- Añadir propiedad con el endpoint de la api interna de indicadores

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,1,'indicators.rest.internal','FILL_ME');
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

commit;

