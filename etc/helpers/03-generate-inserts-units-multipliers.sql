-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- IBESTATNORMA-15 - Carga inicial indicadores
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- Esta consulta de BBDD permite generar los inserts de base de datos de los multiplicadores de unidad en base a una determinada clasificación del SRM.
-- Se compone de varias sentencias consultas select que van generando los inserts necesarios.
-- Elementos a modificar/tener en cuenta: 
-- -- La consulta debe ejecutarse en el esquema de BBDD del SRM.
-- -- La urn de la clasificación que debe cambiarse con el valor apropiado en cada una de las consultas ANTES de ejecutar la consulta (taa.urn).
-- -- El valor FILL_ME de la columna UNIT_MULTIPLIER con el valor del multiplicador de unidad correspondiente DESPUES de ejecutar esta consulta.
-- La consulta devuelve 3 columnas:
-- -- El código del multiplicador de unidad.
-- -- El insert generado.
-- -- La posición relativa del insert a fin de mantener el orden correcto de las sentencias.
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- EJEMPLO DE SQL GENERADO
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- -- Multiplicador: 0
-- INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Multiplicadores', 'es', currval('SEQ_I18NSTRS'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Units', 'en', currval('SEQ_I18NSTRS'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Unitats', 'ca', currval('SEQ_I18NSTRS'), 1);
-- INSERT INTO TB_LIS_UNITS_MULTIPLIERS (VERSION, ID, UNIT_MULTIPLIER, UUID, TITLE_FK) values (0, nextval('SEQ_UNITS_MULTIPLIERS'), 'FILL_ME', uuid_generate_v4(), currval('SEQ_I18NSTRS'));
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------

(
select
	taa.code as code,
	'-- Unidad: ' || code as consulta,
	'1' as pos
from
	TB_ANNOTABLE_ARTEFACTS taa
where
	taa.urn like '%Code=SDMX:CL_UNIT_MULT%'
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
	taa.urn like '%Code=SDMX:CL_UNIT_MULT%'
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
	and taa.urn like '%Code=SDMX:CL_UNIT_MULT%'
order by
	taa.code desc,
	tis.id desc)
union (
select
	taa.code as code,
	'INSERT INTO TB_LIS_UNITS_MULTIPLIERS (VERSION, ID, UNIT_MULTIPLIER, UUID, TITLE_FK) values (0, nextval(''SEQ_UNITS_MULTIPLIERS''), ''FILL_ME'', uuid_generate_v4(), currval(''SEQ_I18NSTRS''));' as consulta,
	'4' as pos
from
	TB_ANNOTABLE_ARTEFACTS taa
where
	taa.urn like '%Code=SDMX:CL_UNIT_MULT%'
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
	taa.urn like '%Code=SDMX:CL_UNIT_MULT%'
order by
	taa.code desc)
order by
code desc,
pos asc;