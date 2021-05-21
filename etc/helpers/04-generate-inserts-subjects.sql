-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- IBESTATNORMA-15 - Carga inicial indicadores
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- Esta consulta de BBDD permite generar los inserts de base de datos de los temas en base a una determinada clasificación del SRM.
-- Se compone de varias sentencias consultas select que van generando los inserts necesarios.
-- Elementos a modificar/tener en cuenta:
-- -- La consulta debe ejecutarse en el esquema de BBDD del SRM.
-- -- La urn de la clasificación debe cambiarse con el valor apropiado en cada una de las consultas ANTES de ejecutar la consulta (taa.urn).
-- -- La descripción de los temas se devuelve en todos los idiomas que estuviesen disponibles en la clasificación separados cada uno de ellos mediante el carácter /
-- La consulta devuelve 3 columnas:
-- -- El código de la unidad.
-- -- El insert generado.
-- -- La posición relativa del insert a fin de mantener el orden correcto de las sentencias.
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- EJEMPLO DE SQL GENERADO
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- -- Tema: 090
-- INSERT INTO TV_AREAS_TEMATICAS (ID_AREA_TEMATICA, DESCRIPCION) VALUES ('090', 'Estadístiques no desglossades per tema / Estadísticas no desglosables por tema');
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------

(
select
	taa.code as code,
	'-- Tema: ' || code as consulta,
	'1' as pos
from
	TB_ANNOTABLE_ARTEFACTS taa
where
	taa.urn like '%Category=IBESTAT:TEMAS_BALEARS%' and length(taa.code) = 3
order by
	taa.code desc)
union (
select
	taa.code as code,
	'INSERT INTO TV_AREAS_TEMATICAS (ID_AREA_TEMATICA, DESCRIPCION) VALUES ('''||taa.code||''', '''||string_agg(REPLACE (tls."label", '''', '''''') , ' / ')||''');' as consulta,
	'2' as pos
from
	tb_international_strings tis,
	tb_localised_strings tls,
	TB_ANNOTABLE_ARTEFACTS taa
where
	tis.id = tls.international_string_fk
	and tis.id = taa.name_fk
	and taa.urn like '%Category=IBESTAT:TEMAS_BALEARS%'
	and length(taa.code) = 3
group by
	taa.code)
union (
select
	taa.code as code,
	' ' as consulta,
	'3' as pos
from
	TB_ANNOTABLE_ARTEFACTS taa
where
	taa.urn like '%Category=IBESTAT:TEMAS_BALEARS%' and length(taa.code) = 3
order by
	taa.code desc)
order by
code desc,
pos asc;