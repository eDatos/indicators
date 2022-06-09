-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- EDATOS-3637 - IESTADIS. Cargar las tablas de valores en eIndicadores
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- Notas: 
-- 1. Se actualiza el valor del orden para algunos valores geográficos existentes previamente
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------

----------------------------------------------------------------
-- Global order update
----------------------------------------------------------------

UPDATE TB_LIS_GEOGR_VALUES SET GLOBAL_ORDER = 'ES_01ES30' WHERE CODE = 'ES30';
UPDATE TB_LIS_GEOGR_VALUES SET GLOBAL_ORDER = 'ES_02ES61' WHERE CODE = 'ES61';
UPDATE TB_LIS_GEOGR_VALUES SET GLOBAL_ORDER = 'ES_03ES24' WHERE CODE = 'ES24';
UPDATE TB_LIS_GEOGR_VALUES SET GLOBAL_ORDER = 'ES_04ES12' WHERE CODE = 'ES12';
UPDATE TB_LIS_GEOGR_VALUES SET GLOBAL_ORDER = 'ES_05ES53' WHERE CODE = 'ES53';
UPDATE TB_LIS_GEOGR_VALUES SET GLOBAL_ORDER = 'ES_06ES70' WHERE CODE = 'ES70';
UPDATE TB_LIS_GEOGR_VALUES SET GLOBAL_ORDER = 'ES_07ES13' WHERE CODE = 'ES13';
UPDATE TB_LIS_GEOGR_VALUES SET GLOBAL_ORDER = 'ES_08ES41' WHERE CODE = 'ES41';
UPDATE TB_LIS_GEOGR_VALUES SET GLOBAL_ORDER = 'ES_09ES42' WHERE CODE = 'ES42';
UPDATE TB_LIS_GEOGR_VALUES SET GLOBAL_ORDER = 'ES_10ES51' WHERE CODE = 'ES51';
UPDATE TB_LIS_GEOGR_VALUES SET GLOBAL_ORDER = 'ES_11ES52' WHERE CODE = 'ES52';
UPDATE TB_LIS_GEOGR_VALUES SET GLOBAL_ORDER = 'ES_12ES43' WHERE CODE = 'ES43';
UPDATE TB_LIS_GEOGR_VALUES SET GLOBAL_ORDER = 'ES_13ES11' WHERE CODE = 'ES11';
UPDATE TB_LIS_GEOGR_VALUES SET GLOBAL_ORDER = 'ES_14ES62' WHERE CODE = 'ES62';
UPDATE TB_LIS_GEOGR_VALUES SET GLOBAL_ORDER = 'ES_15ES22' WHERE CODE = 'ES22';
UPDATE TB_LIS_GEOGR_VALUES SET GLOBAL_ORDER = 'ES_16ES21' WHERE CODE = 'ES21';
UPDATE TB_LIS_GEOGR_VALUES SET GLOBAL_ORDER = 'ES_17ES23' WHERE CODE = 'ES23';
UPDATE TB_LIS_GEOGR_VALUES SET GLOBAL_ORDER = 'ES_18ES63' WHERE CODE = 'ES63';
UPDATE TB_LIS_GEOGR_VALUES SET GLOBAL_ORDER = 'ES_19ES64' WHERE CODE = 'ES64';
UPDATE TB_LIS_GEOGR_VALUES SET GLOBAL_ORDER = 'ES_20ES63_ES64' WHERE CODE = 'ES63_ES64';

commit;