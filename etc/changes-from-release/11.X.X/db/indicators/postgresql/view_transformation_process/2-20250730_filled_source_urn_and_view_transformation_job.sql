-- --------------------------------------------------------------------------------------------------
-- EDATOS-5154 - Añadir descripciones a las vistas de datos
-- 
-- Añadir fórmula cron para ejecutar el job temporal que carga las tablas con la nueva estructura en indicators data

-- --------------------------------------------------------------------------------------------------

--1 Se van a modificar todas las vistas de datos. Las que empiezan por dv_ así que se debe hacer una copia de seguridad.
--1.1) Obtener la definición de cada una de las vistas que empiecen por "dv_"
SELECT 'CREATE OR REPLACE VIEW ' || quote_ident(viewname) || ' AS ' || definition AS create_sql
FROM pg_views
WHERE viewname IN (
 SELECT distinct v.viewname FROM pg_views v where v.viewname like 'dv_%'
);

--1.2) El resultado anterior guardarlo en un fichero resguardoVistasData.sql


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

    2.2.1 Esperar a que se ejecute el job.
    2.2.2 Asegurarse que todas entradas en tb_dataset_dimensions para cada dimensión con representación enumerada están rellenas.
	Las siguientes consultas deben dar 0 resultados.
	select * from tb_dataset_dimensions where dimension_id <> 'TIME' and source_urn is null;
    select * from tb_dataset_dimensions where dimension_id = 'TIME' and source_urn is not null;
	
	2.2.3 Asegurarse de que las vistas están todas regeneradas. Comprobar dos o tres vistas que tengan las nuevas columnas con descripciones.

  
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
