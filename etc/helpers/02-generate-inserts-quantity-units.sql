-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- IBESTATNORMA-15 - Carga inicial indicadores
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- Esta consulta de BBDD permite generar los inserts de base de datos de las unidades en base a una determinada clasificación del SRM.
-- Se compone de varias sentencias consultas select que van generando los inserts necesarios.
-- Elementos a modificar/tener en cuenta:
-- -- La consulta debe ejecutarse en el esquema de BBDD del SRM.
-- -- La urn de la clasificación debe cambiarse con el valor apropiado en cada una de las consultas ANTES de ejecutar la consulta (taa.urn).
-- -- El valor FILL_ME de la columna SYMBOL con el valor de la unidad correspondiente DESPUES de ejecutar esta consulta.
-- -- El valor de la columna SYMBOL_POSITION tiene por defecto el valor END.
-- La consulta devuelve 3 columnas:
-- -- El código de la unidad.
-- -- El insert generado.
-- -- La posición relativa del insert a fin de mantener el orden correcto de las sentencias.
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- EJEMPLO DE SQL GENERADO
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- -- Unidad: VEHICULOS_1000
-- INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Vehículos por cada 1.000 personas', 'es', currval('SEQ_I18NSTRS'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Vehicles per cada 1.000 persones', 'ca', currval('SEQ_I18NSTRS'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Vehicles per each 1000 persons', 'en', currval('SEQ_I18NSTRS'), 1);
-- INSERT INTO TB_LIS_QUANTITIES_UNITS (ID, UUID, SYMBOL, SYMBOL_POSITION, TITLE_FK, VERSION) values (nextval('SEQ_QUANTITIES_UNITS'), uuid_generate_v4(), 'FILL_ME', 'END', currval('SEQ_I18NSTRS'), 0);
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------

(
select
	taa.code as code,
	'-- Unidad: ' || code as consulta,
	'1' as pos
from
	TB_ANNOTABLE_ARTEFACTS taa
where
	taa.urn like '%Code=ISTAC:CL_UNIDADES_MEDIDA%'
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
	taa.urn like '%Code=ISTAC:CL_UNIDADES_MEDIDA%'
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
	and taa.urn like '%Code=ISTAC:CL_UNIDADES_MEDIDA%'
order by
	taa.code desc,
	tis.id desc)
union (
select
	taa.code as code,
	'INSERT INTO TB_LIS_QUANTITIES_UNITS (ID, UUID, SYMBOL, SYMBOL_POSITION, TITLE_FK, VERSION) values (nextval(''SEQ_QUANTITIES_UNITS''), uuid_generate_v4(), ''FILL_ME'', ''END'', currval(''SEQ_I18NSTRS''), 0);' as consulta,
	'4' as pos
from
	TB_ANNOTABLE_ARTEFACTS taa
where
	taa.urn like '%Code=ISTAC:CL_UNIDADES_MEDIDA%'
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
	taa.urn like '%Code=ISTAC:CL_UNIDADES_MEDIDA%'
order by
	taa.code desc)
order by
code desc,
pos asc;