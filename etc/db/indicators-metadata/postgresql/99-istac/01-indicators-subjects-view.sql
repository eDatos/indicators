-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- EDATOS-3406 - Migración de datos a Postgresql
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- Script para la creación de la vista de áreas en Postgresql
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------

create or replace view indicators_bd.tv_areas_tematicas as
select tvat.id_area_tematica, tvat.descripcion
from
	(
	select tvat1.id_area_tematica, (tvat1.id_area_tematica::text || ' '::text) || upper(tvat1.descripcion::text) as descripcion
	from
		(
		select tb_jaxi_areas_tematicas.id_area_tematica, tb_jaxi_areas_tematicas.descripcion
		from
			tb_jaxi_areas_tematicas
		where
			length(tb_jaxi_areas_tematicas.id_area_tematica::text) = 3
			and substr(tb_jaxi_areas_tematicas.id_area_tematica::text, 1, 3) <> '000'::text) tvat1
union
	select substr(tvat2.id_area_tematica::text, 1, 2) || substr(tvat2.id_area_tematica::text, 6, 1) as col,
		((substr(tvat2.id_area_tematica::text, 1, 2) || substr(tvat2.id_area_tematica::text, 6, 1)) || ' '::text) || tvat2.descripcion::text as descripcion
	from
		(
		select tb_jaxi_areas_tematicas.id_area_tematica, tb_jaxi_areas_tematicas.descripcion
		from
			tb_jaxi_areas_tematicas
		where
			length(tb_jaxi_areas_tematicas.id_area_tematica::text) = 7 
				and substr(tb_jaxi_areas_tematicas.id_area_tematica::text, 1, 3) <> '000'::text
					and substr(tb_jaxi_areas_tematicas.id_area_tematica::text, 1, 1) <> '9'::text) tvat2) tvat
order by tvat.id_area_tematica;
	
commit;