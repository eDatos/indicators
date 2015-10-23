-- -----------------------------------------------------------------------------------------
-- METAMAC-2415 - Añadir propiedad de configuración del endpoint del buscador
-- Véase también INDISTAC-935
-- -----------------------------------------------------------------------------------------

-- Ejemplo de valor a insertar: http://www.gobiernodecanarias.org/istac/buscador/busca
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,1,'idxmanager.search.form.url','FILL_ME');
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

commit;