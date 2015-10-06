-- -----------------------------------------------------------------------------------------
-- INDISTAC-936 - Añadir al camino de hormigas la sección de Datos abiertos
-- -----------------------------------------------------------------------------------------

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,1,'indicators.search.form.url','FILL_ME');
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

commit;