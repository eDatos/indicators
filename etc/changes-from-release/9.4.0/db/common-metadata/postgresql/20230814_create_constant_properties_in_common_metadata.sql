-- --------------------------------------------------------------------------------------------------
-- EDATOS-4185 - Añadir metadato elemento de tema a los indicadores
-- 
-- Añadir propiedad en el commón metadata que indique el esquema de temas por defecto en el SRM que se utilizará para obtener los elementos de tema y temas asociados. 
-- Añadir propiedad de ejecución del job que permite cachear los temas que tienen indicadores asociados al elemento de tema relacionado a dicho tema.
-- --------------------------------------------------------------------------------------------------

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,false,
'edatos.srm.category.scheme.default','FILL_ME',false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

commit;


----------------------------------------------
--EXAMPLE IN DEV ENVIRONMENT
--insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,false,
--'edatos.srm.category.scheme.default','urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=ISTAC:TEMAS_CANARIAS(01.001)',false);
--UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

---------------------------------------------

-- Add job configuration

!! ATENTION Configure cron expression
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'edatos.indicators.category_cache_refresh.cron_expression',xxxxxx,false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

commit;

----------------------------------------------
--EXAMPLE IN DEV 
/*
-- 0 0 * ? * * Every hour
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'edatos.indicators.category_cache_refresh.cron_expression','0 */5 * ? * *',false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';
commit;
*/

---------------------------------------------
