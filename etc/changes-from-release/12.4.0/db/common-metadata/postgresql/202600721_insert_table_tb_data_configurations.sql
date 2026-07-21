-- ------------------------------------------------------------------------------------------------------------------------------
-- EDATOS-5811 - Permitir que la clasificación por defecto para GPE y JSONSTAT sea nula en los entornos que no la necesiten
--
--  deprecar constante metamac.srm.default.gpe.jsonstat.codelist.urn
-- crear nueva propiedad "indicators.default.jsonstat.codelist.urn"
-- ------------------------------------------------------------------------------------------------------------------------------

-- Deprecar propiedad antigua
UPDATE tb_data_configurations SET conf_key = 'deprecated.metamac.srm.default.gpe.jsonstat.codelist.urn'
WHERE conf_key = 'metamac.srm.default.gpe.jsonstat.codelist.urn';

-- Crear nueva propiedad (FILL_ME — can be null if the environment does not use it)
INSERT INTO TB_DATA_CONFIGURATIONS (ID, VERSION, SYSTEM_PROPERTY, CONF_KEY, CONF_VALUE, EXTERNALLY_PUBLISHED)
VALUES (GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'), 1, false, 'indicators.default.jsonstat.codelist.urn', 'FILL_ME', false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

-- Copiar valor de la propiedad deprecada a la nueva
UPDATE tb_data_configurations SET conf_value = (
    SELECT conf_value FROM tb_data_configurations WHERE conf_key = 'deprecated.metamac.srm.default.gpe.jsonstat.codelist.urn'
) WHERE conf_key = 'indicators.default.jsonstat.codelist.urn';

commit;
