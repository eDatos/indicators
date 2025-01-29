-- ------------------------------------------------------------------------------------------------------------------------------
-- EDATOS-4698 - Mapear los elementos de variable a códigos normalizados en e-Indicadores usando una clasificación por defecto
-- 
--  Crear nuevo campo en el common metadata para un indicador.
-- ------------------------------------------------------------------------------------------------------------------------------


insert into TB_DATA_CONFIGURATIONS (ID, VERSION, SYSTEM_PROPERTY, CONF_KEY, CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 1, true, 'indicators.geographical_values.code_list.urn', 'FILL_ME', false);
                
-- DESARROLLO
-- insert into TB_DATA_CONFIGURATIONS (ID, VERSION, SYSTEM_PROPERTY, CONF_KEY, CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 1, true, 'indicators.geographical_values.code_list.urn', 'urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ISTAC:CL_AREA_ES(02.003)', false);
        
-- DEMO
-- insert into TB_DATA_CONFIGURATIONS (ID, VERSION, SYSTEM_PROPERTY, CONF_KEY, CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 1, true, 'indicators.geographical_values.code_list.urn', 'urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ISTAC:CL_AREA_ES(04.001)', false);

-- PRE ISTAC
-- insert into TB_DATA_CONFIGURATIONS (ID, VERSION, SYSTEM_PROPERTY, CONF_KEY, CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 1, true, 'indicators.geographical_values.code_list.urn', 'urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ISTAC:CL_AREA_ES(02.000)', false);

-- PRO ISTAC
-- insert into TB_DATA_CONFIGURATIONS (ID, VERSION, SYSTEM_PROPERTY, CONF_KEY, CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 1, true, 'indicators.geographical_values.code_list.urn', 'urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ISTAC:CL_AREA_ES(05.001)', false);
        
-- PRE IBESTAT
-- insert into TB_DATA_CONFIGURATIONS (ID, VERSION, SYSTEM_PROPERTY, CONF_KEY, CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 1, true, 'indicators.geographical_values.code_list.urn', 'urn:sdmx:org.sdmx.infomodel.codelist.Codelist=IBESTAT:CL_AREA_ES53(01.000)', false);
        
-- PRO IBESTAT
-- insert into TB_DATA_CONFIGURATIONS (ID, VERSION, SYSTEM_PROPERTY, CONF_KEY, CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 1, true, 'indicators.geographical_values.code_list.urn', 'urn:sdmx:org.sdmx.infomodel.codelist.Codelist=IBESTAT:CL_AREA_ES53(01.001)', false);
        
-- PRE IESTADIS
-- insert into TB_DATA_CONFIGURATIONS (ID, VERSION, SYSTEM_PROPERTY, CONF_KEY, CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 1, true, 'indicators.geographical_values.code_list.urn', 'urn:sdmx:org.sdmx.infomodel.codelist.Codelist=IECM:CL_AREA_ES30(02.001)', false);
        
-- PRO IESTADIS
-- insert into TB_DATA_CONFIGURATIONS (ID, VERSION, SYSTEM_PROPERTY, CONF_KEY, CONF_VALUE, EXTERNALLY_PUBLISHED) values (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 1, true, 'indicators.geographical_values.code_list.urn', 'urn:sdmx:org.sdmx.infomodel.codelist.Codelist=IECM:CL_AREA_ES30(02.004)', false);
     
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';
commit;