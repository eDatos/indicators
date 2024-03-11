-- --------------------------------------------------------------------------------------------------
-- EDATOS-3827 - Integración con códigos geográficos de e-Semántica
-- 
-- Script con tabla temporal para migración de datos de valores geográficos de srm a indicators. Elementos de variable  de la variable VR_TERRITORIO
-- --------------------------------------------------------------------------------------------------

--1 Ejecutar este script 
----1.1 en la base de datos tb_indicators
----1.2 en la base de datos metamac_structural_resources

CREATE TABLE temp_mig_geo_values (
	code varchar(255) NOT NULL,
	latitude float8,
	longitude float8,
	global_order varchar(255) NOT NULL,
	uuid varchar(36) NOT NULL,
	created_date_tz varchar(50),
	created_date timestamp,
	created_by varchar(50),
	"version" int8 NOT NULL,
	granularity_code varchar(255) NOT NULL,
	label_es VARCHAR(4000),
	label_ca VARCHAR(4000),
	label_en VARCHAR(4000),
	granularity_fk BIGINT
);

commit;