-- -----------------------------------------------------------------------------------------
-- INDISTAC-945 - Los widgets no funcionan cuando se embeben en páginas https
-- -----------------------------------------------------------------------------------------

-- Ejemplo de valor a insertar: //estadisticas.arte-consultores.com/indicators-visualizations
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,1,'indicators.web.external.url','FILL_ME');
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

commit;