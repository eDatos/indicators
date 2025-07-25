-- ---------------------------------------------------------------------------------------------------
-- EDATOS-5104 Recargar los indicadores que tienen fuente GPE-Jaxi sólo cuando se hagan modificaciones sobre la misma

-- Variable de entorno para permitir deshabilitar el consumidor de kafka de jaxi. Deshabilitará el consumidor pero avanzando el offset de tal manera que los mensajes
--que se reciban mientras está deshabilitado no se recuperarán cuando se vuelva a habilitar. De esta manera se permitirá que, cuando se lancen proceso masivos,
--este consumidor no haga nada (por ejemplo desde el ckan)
-- ---------------------------------------------------------------------------------------------------

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.indicators.kafka.jaxi_publication_consumer_disabled', false,false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

commit;