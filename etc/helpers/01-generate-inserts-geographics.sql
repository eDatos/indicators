-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- IBESTATNORMA-15 - Carga inicial indicadores
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- Estas dos consultas de BBDD permite generar los inserts de base de datos de los las granularidades geográficas y los valores geográficos en base a dos clasificaciones del SRM.
-- Cada consulta se compone de varias sentencias consultas select que van generando los inserts necesarios.
-- Elementos a modificar/tener en cuenta en la consulta de granularidades geográficas:
-- -- La consulta debe ejecutarse en el esquema de BBDD del SRM.
-- -- La urn de la clasificación debe cambiarse con el valor apropiado en cada una de las consultas ANTES de ejecutar la consulta (taa.urn).
-- Elementos a modificar/tener en cuenta en la consulta de valores geográficos:
-- -- La consulta debe ejecutarse en el esquema de BBDD del SRM.
-- -- La urn de la clasificación debe cambiarse con el valor apropiado en cada una de las consultas ANTES de ejecutar la consulta (taa.urn).
-- -- El valor FILL_ME de la columna GLOBAL_ORDER del valor geográfico DESPUES de ejecutar esta consulta.
-- -- Las columnas LATITUDE y LONGITUDE se rellenan con el valor existente en el elemento de variable asociado a cada valor geográfico
-- Cada consulta devuelve 3 columnas:
-- -- El código de la unidad.
-- -- El insert generado.
-- -- La posición relativa del insert a fin de mantener el orden correcto de las sentencias.
-- IMPORTANTE: Para construir el script de BBDD final es necesario intercalar a mano el resultado de ambas consultas teniendo en cuenta que:
-- -- Primero deben especificarse los inserts generados por la por la consulta de granularidades geográficas.
-- -- A continuación deben especificarse los inserts generados para la consulta de valores geográficos DE LA GRANULARIDAD GEOGRÁFICA especificada anteriormente.
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- EJEMPLO DE SQL GENERADO E INTERCALADO MANUALMENTE
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- -- Granularidad geografica: REGIONS
-- INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Comunidades autónomas', 'es', currval('SEQ_I18NSTRS'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Regions', 'en', currval('SEQ_I18NSTRS'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Comunitats autònomes', 'ca', currval('SEQ_I18NSTRS'), 1);
-- INSERT INTO TB_LIS_GEOGR_GRANULARITIES (VERSION, ID, CODE, UUID, TITLE_FK) values (0, nextval('SEQ_GEOGR_GRANULARITIES'), 'REGIONS', uuid_generate_v4(), currval('SEQ_I18NSTRS'));

--     -- Granularidad geografica: REGIONS Valor geografico: ES70
--     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
--     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Canàries', 'ca', currval('SEQ_I18NSTRS'), 1);
--     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Canary Islands', 'en', currval('SEQ_I18NSTRS'), 1);
--     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Canarias', 'es', currval('SEQ_I18NSTRS'), 1);
--     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES70', null, null, 'FILL_ME_ES70', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));

-- -- Granularidad geografica: PROVINCES
-- INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Províncies', 'ca', currval('SEQ_I18NSTRS'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Provincias', 'es', currval('SEQ_I18NSTRS'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Provinces', 'en', currval('SEQ_I18NSTRS'), 1);
-- INSERT INTO TB_LIS_GEOGR_GRANULARITIES (VERSION, ID, CODE, UUID, TITLE_FK) values (0, nextval('SEQ_GEOGR_GRANULARITIES'), 'PROVINCES', uuid_generate_v4(), currval('SEQ_I18NSTRS'));
 
--      -- Granularidad geografica: PROVINCES Valor geografico: ES702
--      INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
--      INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Cruz de Tenerife', 'ca', currval('SEQ_I18NSTRS'), 1);
--      INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Cruz de Tenerife', 'es', currval('SEQ_I18NSTRS'), 1);
--      INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Cruz de Tenerife', 'en', currval('SEQ_I18NSTRS'), 1);
--      INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES702', null, null, 'FILL_ME_ES702', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------

-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
--  GRANULARIDADES GEOGRÁFICAS
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
(
select
	taa.code as code,
	'-- Granularidad geografica: ' || code as consulta,
	'1' as pos
from
	TB_ANNOTABLE_ARTEFACTS taa
where
	taa.urn like '%Code=ISTAC:CL_GRANULARIDADES_GEOGRAFICAS%'
order by
	taa.code desc)
union (
select
	taa.code as code,
	'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval(''SEQ_I18NSTRS''), 1);' as consulta,
	'2' as pos
from
	TB_ANNOTABLE_ARTEFACTS taa
where
	taa.urn like '%Code=ISTAC:CL_GRANULARIDADES_GEOGRAFICAS%'
order by
	taa.code desc)
union (
select
	taa.code as code,
	'INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || REPLACE (tls."label", '''', '''''') || ''', ''' || tls.locale || ''', currval(''SEQ_I18NSTRS''), 1);' as consulta,
	'3' as pos
from
	tb_international_strings tis,
	tb_localised_strings tls,
	TB_ANNOTABLE_ARTEFACTS taa
where
	tis.id = tls.international_string_fk
	and tis.id = taa.name_fk
	and taa.urn like '%Code=ISTAC:CL_GRANULARIDADES_GEOGRAFICAS%'
order by
	taa.code desc,
	tis.id desc)
union (
select
	taa.code as code,
	'INSERT INTO TB_LIS_GEOGR_GRANULARITIES (VERSION, ID, CODE, UUID, TITLE_FK) values (0, nextval(''SEQ_GEOGR_GRANULARITIES''), '''||taa.code ||''', uuid_generate_v4(), currval(''SEQ_I18NSTRS''));' as consulta,
	'4' as pos
from
	TB_ANNOTABLE_ARTEFACTS taa
where
	taa.urn like '%Code=ISTAC:CL_GRANULARIDADES_GEOGRAFICAS%'
order by
	taa.code desc)
union (
select
	taa.code as code,
	' ' as consulta,
	'5' as pos
from
	TB_ANNOTABLE_ARTEFACTS taa
where
	taa.urn like '%Code=ISTAC:CL_GRANULARIDADES_GEOGRAFICAS%'
order by
	taa.code desc)
order by
code desc,
pos asc;

-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
--  VALORES GEOGRÁFICOS
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
(
select
	taa2.code as granularity_value,
	taa.code as geo_value,
	'     -- Granularidad geografica: ' || taa2.code|| ' Valor geografico: ' || taa.code  as consulta ,
	'1' as pos
from tb_m_codes tmc, tb_codes tc,tb_annotable_artefacts taa, tb_m_variable_elements tmve, tb_codes tc2, tb_annotable_artefacts taa2
where tmc.tb_codes = tc.id 
and tc.nameable_artefact_fk = taa.id 
and taa.urn like '%Code=IBESTAT:CL_AREA_ES53%'
and tmc.variable_element_fk = tmve.id
and tmve.geographical_granularity_fk = tc2.id
and tc2.nameable_artefact_fk = taa2.id
) union (	
select
	taa2.code as granularity_value,	
    taa.code as geo_value,
	'     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval(''SEQ_I18NSTRS''), 1);' as consulta,
	'2' as pos
from tb_m_codes tmc, tb_codes tc,tb_annotable_artefacts taa, tb_m_variable_elements tmve, tb_codes tc2, tb_annotable_artefacts taa2
where tmc.tb_codes = tc.id 
and tc.nameable_artefact_fk = taa.id 
and taa.urn like '%Code=IBESTAT:CL_AREA_ES53%'
and tmc.variable_element_fk = tmve.id
and tmve.geographical_granularity_fk = tc2.id
and tc2.nameable_artefact_fk = taa2.id
)
union (	
select
	taa2.code as granularity_value,	
    taa.code as geo_value,
	'     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || REPLACE (tls."label", '''', '''''') || ''', ''' || tls.locale || ''', currval(''SEQ_I18NSTRS''), 1);' as consulta,
	'3' as pos
from tb_m_codes tmc, tb_codes tc,tb_annotable_artefacts taa, tb_m_variable_elements tmve, tb_codes tc2, tb_annotable_artefacts taa2, tb_international_strings tis, tb_localised_strings tls
where tmc.tb_codes = tc.id 
and tc.nameable_artefact_fk = taa.id 
and taa.urn like '%Code=IBESTAT:CL_AREA_ES53%'
and tmc.variable_element_fk = tmve.id
and tmve.geographical_granularity_fk = tc2.id
and tc2.nameable_artefact_fk = taa2.id
and tis.id = tls.international_string_fk 
and tis.id = taa.name_fk
)
union (	
select
	taa2.code as granularity_value,	
    taa.code as geo_value,
	'     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval(''SEQ_GEOGR_VALUES''), '''||taa.code||''', '|| case when tmve.latitude is null then 'null' else ''|| tmve.latitude end ||', '||case when tmve.longitude is null then 'null' else ''|| tmve.longitude end ||', ''FILL_ME_'||taa.code||''', uuid_generate_v4(), currval(''SEQ_I18NSTRS''), currval(''SEQ_GEOGR_GRANULARITIES''));' as consulta,
	'4' as pos
from tb_m_codes tmc, tb_codes tc,tb_annotable_artefacts taa, tb_m_variable_elements tmve, tb_codes tc2, tb_annotable_artefacts taa2
where tmc.tb_codes = tc.id 
and tc.nameable_artefact_fk = taa.id 
and taa.urn like '%Code=IBESTAT:CL_AREA_ES53%'
and tmc.variable_element_fk = tmve.id
and tmve.geographical_granularity_fk = tc2.id
and tc2.nameable_artefact_fk = taa2.id
)union (	
select
	taa2.code as granularity_value,	
    taa.code as geo_value,
	' ' as consulta,
	'5' as pos
from tb_m_codes tmc, tb_codes tc,tb_annotable_artefacts taa, tb_m_variable_elements tmve, tb_codes tc2, tb_annotable_artefacts taa2
where tmc.tb_codes = tc.id 
and tc.nameable_artefact_fk = taa.id 
and taa.urn like '%Code=IBESTAT:CL_AREA_ES53%'
and tmc.variable_element_fk = tmve.id
and tmve.geographical_granularity_fk = tc2.id
and tc2.nameable_artefact_fk = taa2.id
)
order by granularity_value desc, geo_value desc, pos asc;