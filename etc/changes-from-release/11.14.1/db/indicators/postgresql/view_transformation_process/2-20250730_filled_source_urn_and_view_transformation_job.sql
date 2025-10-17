-- --------------------------------------------------------------------------------------------------
-- EDATOS-5154 - Añadir descripciones a las vistas de datos
-- 
-- Añadir fórmula cron para ejecutar el job temporal que carga las tablas con la nueva estructura en indicators data

-- NOTA.Para istac se decide modificar vistas de GPE también. Aunque ya está desactivado y no se usa existe un remanente de indicadores del GPE. Aunque no funcionan en la app para actualizar, si es
--conveniente que las vistas estén con las descripciones.
-- --------------------------------------------------------------------------------------------------

--1 Se van a modificar todas las vistas de datos. Las que empiezan por dv_ así que se debe hacer una copia de seguridad.
--1.1) Obtener la definición de cada una de las vistas que empiecen por "dv_"
SELECT 'CREATE OR REPLACE VIEW ' || quote_ident(viewname) || ' AS ' || definition AS create_sql
FROM pg_views
WHERE viewname IN (
 SELECT distinct v.viewname FROM pg_views v where v.viewname like 'dv_%'
);

--1.2) El resultado anterior guardarlo en un fichero resguardoVistasData.sql

--1.3) Borrado de todas las vistas
--1.3.1) Obtener script de borrado de vistas que empiezan por "dv_"
SELECT 'DROP VIEW IF EXISTS ' || quote_ident(viewname) || ' CASCADE;'
FROM pg_views
WHERE viewname IN (
 SELECT distinct v.viewname FROM pg_views v where v.viewname like 'dv_%'
);

--1.4) Ejecutar las sentencias obtenidas en el apartado anterior.

--2 Obtener la urn del codelist asociado a cada dimensión con representación enumerada de cada dataset. y regenerar las vistas con las descripciones 
-- Para ello se ha creado un job  temporal que va a realizar esta tarea. hay que programarlo una sóla vez. Se ha creado EDATOS-5200 para borrar el código asociado a posteriori ya que no será de utilidad más.
 ----2.1) Añadir a common-metadata la programación de job programado que actualiza la clasificación asociada a los códigos geográficos para las consultas que provienen de eDatos. Para ello ejecutar script indicado en 
 
 -- !! ATTENTION Configure cron expression
-- Ej: '0 30 20 29 AUG ? 2024' --0 30 20 29 AUG ? 2024  29 de agosto de 2024 a las 20:30   
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,true,'metamac.indicators.data_view_adjustment.cron_expression',xxxx,false);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

commit;
 
 /* SI SE QUIERE ACTUALIZAR A POSTERIORI
 update tb_data_configurations
set conf_value = '0 56 13 21 SEP ? 2025' --0 30 20 29 AUG ? 2023  29 de agosto de 2023 a las 20:30   
where conf_key = 'metamac.indicators.data_view_adjustment.cron_expression';
  */

--2.2 Ejecución de job.
--Tiempos en desarrollo:
/*

*/


--    2.2.1 Esperar a que se ejecute el job.
--    2.2.2 Asegurarse que todas entradas en tb_dataset_dimensions para cada dimensión con representación enumerada están rellenas.
	select * from tb_dataset_dimensions where dimension_id <> 'TIME' and source_urn is null;
    select * from tb_dataset_dimensions where dimension_id = 'TIME' and source_urn is not null;

--	Las consultas anteriores deberían dar 0 resultados idealmente aunque se han detectado casos en los que da resultado. O cuando no hay fuentes de datos todavía registradas o también hay casos donde hay una entrada y luego no hay ningún indicador asociado en la bd indicators_bd.
--Si devuelve algún dato la consulta anterior, se puede lanzar la siguiente consulta. Con los scripts generados lanzar alguna consulta en indicators_bd para comprobar que son los casos indicados:	
	/*
	NOTA: Reemplazar el último UNION por ; Si salen dobles comillas al copiar a otra ventana dbeaver quitarlos. Ejecutar en bd INDICATORS_BD
	
			select  'select s.query_environment, i.code, iv.* from tb_data_sources s, tb_indicators_versions iv, tb_indicators i where  
i.id = iv.indicator_fk and s.indicator_version_fk = iv.id and data_repository_id = ''' || d.dataset_id || ''' UNION 
' from tb_dataset_dimensions dd, tb_datasets d
		where d.id = dd.dataset_fk  
		and dimension_id <> 'TIME' and source_urn is null
		group by d.dataset_id;
	*/
	
-- Si ejecutando la select anterior no sale nada, es que esos datasets no tienen indicador asociado.
/* Por ejemplo en desarrollo/demo no tiene dataset asociado:
select s.query_environment, i.code, iv.* from tb_data_sources s, tb_indicators_versions iv, tb_indicators i where  
i.id = iv.indicator_fk and s.indicator_version_fk = iv.id and data_repository_id = 'dataset:63555b0d-f9c3-42a0-a78b-572e0af266db'; 
*/
	
--	2.2.3 Asegurarse de que las vistas están todas regeneradas. Comprobar dos o tres vistas que tengan las nuevas columnas con descripciones.


/* ERRORES (WARNINGS) que pueden aparecer
2025-08-28 13:48:57.463 [IndicatorsScheduler_Worker-1] ERROR e.g.i.i.c.s.IndicatorsDataServiceImpl - Indicators data repository [dataset:c991a2da-2a1e-414a-813d-3621ca4602a6] Error in data view adjustment for descriptions fields. ind code:datasetIII
org.fornax.cartridges.sculptor.framework.errorhandling.ApplicationException: Dataset with ID not found in repository data.  So sourceURN and VIEW will not be updated: dataset:c991a2da-2a1e-414a-813d-3621ca4602a6
        at es.gobcan.istac.edatos.dataset.repository.service.impl.DatasetRepositoriesServiceImpl.updateDatasetDimensionSourceUrn(DatasetRepositoriesServiceImpl.java:626) ~[edatos-dataset-repository-3.3.2-SNAPSHOT-client.jar:na]
*/
  
--3) Restaurar permisos sobre las vistas. Debido al mal rendimiento de la asignación de permisos por código ya que hace un select * de la vista que tarda en los datasets grandes, se hará manualmente.
-- 3.1) Restaurar los permisos sobre las vistas: existe un rol de base de datos definido en la propiedad indicators.bbbd.data_views_role del common-metadata que 
-- debe tener permisos de select sobre todas las vistas de los indicadores.
-- 3.1.1) Consultar en la base de datos del common-metadata el rol definido en la propiedad indicators.bbbd.data_views_role
select conf_value from tb_data_configurations where conf_key = 'indicators.bbbd.data_views_role';

--Lo normal es que la consulta anterior devuelva: INDICATORS_DATA_ROLE

-- 3.1.2) Sustituir el valor obtenido en la consulta anterior en el campo [FILL_ME_WITH_ROLE] la siguiente consulta y ejecutarla para obtener todos los grant necesarios 
-- para restaurar los permisos sobre las vistas
SELECT 'GRANT SELECT ON ' || quote_ident(viewname) || ' TO [FILL_ME_WITH_ROLE];' create_sql
FROM pg_views
WHERE viewname IN (
 SELECT distinct v.viewname FROM pg_views v where v.viewname like 'dv_%'
);

-- 3.1.3) Ejecutar las sentencias grant obtenidas en el apartado anterior   
