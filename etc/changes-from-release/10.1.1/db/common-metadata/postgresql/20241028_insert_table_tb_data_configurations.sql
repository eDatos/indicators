-- --------------------------------------------------------------------------------------------------
-- EDATOS-4698 - Inclusión de metadato en indicadores para establecer si es un indicador principal
-- 
--  Crear nuevo campo en el common metadata para un indicador.
-- --------------------------------------------------------------------------------------------------


insert into TB_DATA_CONFIGURATIONS (ID, VERSION, SYSTEM_PROPERTY, CONF_KEY, CONF_VALUE, EXTERNALLY_PUBLISHED)
values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 1, true, 'indicators.geographical_values.code_list.urn',
        'FILL_ME', false);
UPDATE TB_SEQUENCES
SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1
WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';
commit;