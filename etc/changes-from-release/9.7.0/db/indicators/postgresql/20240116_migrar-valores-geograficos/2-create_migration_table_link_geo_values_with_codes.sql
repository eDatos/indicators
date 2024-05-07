-- --------------------------------------------------------------------------------------------------
-- EDATOS-3827 - Integración con códigos geográficos de e-Semántica
-- 
-- Script con tabla temporal para migración de relación de códigos de codelist con elementos de variable
-- Es necesario tanto en la bd tb_indicators como en tb_indicators_data
-- --------------------------------------------------------------------------------------------------

--1 Ejecutar este script en las siguientes bases de datos:
----1.1 tb_indicators
----1.2 tb_indicators_data
----1.3 en la base de datos metamac_structural_resources
----1.4 en la base de datos metamac_portal_bd (para la migración de permalinks)

CREATE TABLE temp_mig_codes_with_var_element (
	urn_codelist varchar(4000),
	code varchar(255) NOT NULL,
	variable_element_code varchar(255) NOT NULL
);

commit;