-- --------------------------------------------------------------------------------------------------
-- EDATOS-3827 - Integración con códigos geográficos de e-Semántica
-- 
-- Script para rellenar la tabla maestra de valores geográficos "tb_lis_geogr_values"
-- --------------------------------------------------------------------------------------------------

--1) Crear nuevos campos en tablas relacionadas y deprecar antiguos
 ----1.1 tabla tb_data_sources
 ALTER TABLE tb_data_sources RENAME COLUMN geographical_value_fk TO deprecated_geographical_value_fk;
 ALTER TABLE tb_data_sources alter COLUMN deprecated_geographical_value_fk  drop not null;
 ALTER TABLE indicators_bd.tb_data_sources DROP CONSTRAINT fk_tb_data_sources_geographical_value_fk;
 ALTER TABLE tb_data_sources ADD COLUMN geographical_value_fk BIGINT;

 ----1.2 tabla tb_indic_inst_geo_values
ALTER TABLE tb_indic_inst_geo_values RENAME COLUMN geographical_value_fk TO deprecated_geographical_value_fk;
ALTER TABLE tb_indic_inst_geo_values DROP	CONSTRAINT pk_tb_indic_inst_geo_values;
ALTER TABLE tb_indic_inst_geo_values alter COLUMN deprecated_geographical_value_fk  drop not null;
ALTER TABLE indicators_bd.tb_indic_inst_geo_values DROP CONSTRAINT fk_tb_indic_inst_geo_values_geographical_value_fk;
 ALTER TABLE tb_indic_inst_geo_values ADD COLUMN geographical_value_fk BIGINT;

 ----1.3 tabla tb_indic_inst_last_value
 ALTER TABLE tb_indic_inst_last_value RENAME COLUMN geographical_code TO deprecated_geographical_code;
ALTER TABLE tb_indic_inst_last_value drop constraint uq_tb_indic_inst_last_value;
ALTER TABLE tb_indic_inst_last_value alter COLUMN deprecated_geographical_code  drop not null;
 ALTER TABLE tb_indic_inst_last_value ADD COLUMN geographical_code varchar(255);
 
 ----1.4 tabla tb_ind_version_geo_cov
 ALTER TABLE tb_ind_version_geo_cov RENAME COLUMN geographical_value_fk TO deprecated_geographical_value_fk;
ALTER TABLE tb_ind_version_geo_cov DROP CONSTRAINT fk_tb_ind_version_geo_cov_geographical_value_fk;
ALTER TABLE tb_ind_version_geo_cov DROP CONSTRAINT uq_tb_ind_version_geo_cov;
ALTER TABLE tb_ind_version_geo_cov alter COLUMN deprecated_geographical_value_fk  drop not null;
 ALTER TABLE tb_ind_version_geo_cov ADD COLUMN geographical_value_fk BIGINT;

 ----1.5 tabla tb_quantities
 ALTER TABLE tb_quantities RENAME COLUMN base_location_fk TO deprecated_base_location_fk;
ALTER TABLE tb_quantities DROP CONSTRAINT fk_tb_quantities_base_location_fk;
 ALTER TABLE tb_quantities ADD COLUMN base_location_fk BIGINT;

 ----1.6 tabla tb_indic_version_last_value
 ALTER TABLE tb_indic_version_last_value RENAME COLUMN geographical_code TO deprecated_geographical_code;
ALTER TABLE tb_indic_version_last_value drop constraint uq_tb_indic_version_last_value;
ALTER TABLE tb_indic_version_last_value alter COLUMN deprecated_geographical_code  drop not null;
 ALTER TABLE tb_indic_version_last_value ADD COLUMN geographical_code varchar(255);

  --2. Hacer copia de seguridad de tabla "tb_lis_geogr_values"
-- indicators_bd.tb_lis_geogr_values definition

--2.1) Crear copia de tabla
CREATE TABLE tb_lis_geogr_values_copy (
	id int8 NOT NULL,
	code varchar(255) NOT NULL,
	latitude float8,
	longitude float8,
	global_order varchar(255) NOT NULL,
	update_date_tz varchar(50),
	update_date timestamp,
	uuid varchar(36) NOT NULL,
	created_date_tz varchar(50),
	created_date timestamp,
	created_by varchar(50),
	last_updated_tz varchar(50),
	last_updated timestamp,
	last_updated_by varchar(50),
	"version" int8 NOT NULL,
	title_fk int8,
	granularity_fk int8 NOT NULL,
	CONSTRAINT pk_tb_lis_geogr_values_copy PRIMARY KEY (id)
);

--2.2) Copiar los datos a la tabla de copia
INSERT INTO indicators_bd.tb_lis_geogr_values_copy
(id, code, latitude, longitude, global_order, update_date_tz, update_date, uuid, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version", title_fk, granularity_fk)
 SELECT id, code, latitude, longitude, global_order, update_date_tz, update_date, uuid, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version", title_fk, granularity_fk
FROM tb_lis_geogr_values;

--2.3) Borrar datos de la tabla  "tb_lis_geogr_values"
delete  from tb_lis_geogr_values;

--3) Continuación del paso 0 hecho días antes para volvar en tabla tb_lis_geogr_values los valores finales
  --El día de despliegue se deberá exportar a CSV la tabla "temp_tb_lis_geogr_values". y luego importar a tb_lis_geogr_values. Para ello:
-- 0.6.1) Exportar tabla temp_tb_lis_geogr_values a CSV (incrementar elementos a 300000) si es menor el número.
-- 0.6.2) Importar a tb_lis_geogr_values que está vacía en estos momentos.
-- 0.6.3) Obtener el último valor 
select max(id) from tb_lis_geogr_values ;
-- 0.6.4) con el valor dado anteriormente, cambiar la secuencia para poner el valor anterior mas uno que será el siguiente valor de la secuencia. 
ALTER SEQUENCE SEQ_GEOGR_VALUES RESTART WITH PONER_AQUI_VALOR_DE_PASO_ANTERIOR + 1;
 
 --3.1 Con la importación anterior se comprueba que el campo update_date_tz que estaba a nulo originalmente, se ha migrado con un espacio en blanco. Hay que poner a nulo para evitar error en la aplicación con las fechas. Para ello ejecutar:
 update tb_lis_geogr_values set update_date_tz = null where update_date_tz = '';
 
 --4 Rellenar nuevos campos creados
--4.1) Comprobaciones previas
--4.1.1) Comprobar que todos los valores que estaban antes tienen traducción en la nueva tabla de valores.
 select t.code from tb_lis_geogr_values_copy t where t.code not in(select code from temp_mig_codes_with_var_element);
 
 -- Si sale algún valor en la consulta anterior, buscar el elemento de variable asociado y añadir entrada a  temp_mig_codes_with_var_element con la información. Ej: insert into temp_mig_codes_with_var_element(urn_codelist, code, variable_element_code) values('', 'TENERIFE', 'ISLA_TENERIFE');

/* Consultas más específicas para ver donde se usan. Las siguientes consultas no deben retornar valores.
-- la siguiente consulta no debe retornar valores.
select * from tb_data_sources tds, tb_lis_geogr_values_copy tlg 
where  tds.deprecated_geographical_value_fk = tlg.id and tlg.code  not in(select tempc.code  from temp_mig_codes_with_var_element tempc);

-- la siguiente consulta no debe retornar valores
select * from tb_indic_inst_geo_values gv, tb_lis_geogr_values_copy tlg where gv.deprecated_geographical_value_fk = tlg.id 
and tlg.code not in(select tempc.code  from temp_mig_codes_with_var_element tempc);

-- la siguiente consulta no debe retornar valores
select * from tb_indic_inst_last_value lv, tb_lis_geogr_values_copy tlg where lv.deprecated_geographical_code  = tlg.code 
 and tlg.code not in(select tempc.code  from temp_mig_codes_with_var_element tempc);

-- la siguiente consulta no debe retornar valores
select * from tb_ind_version_geo_cov gc, tb_lis_geogr_values_copy tlg where gc.deprecated_geographical_value_fk = tlg.id 
and tlg.code not in(select tempc.code  from temp_mig_codes_with_var_element tempc);

-- la siguiente consulta no debe retornar valores
select * from tb_quantities q, tb_lis_geogr_values_copy tlg where q.base_location_fk = tlg.id 
and tlg.code not in(select tempc.code  from temp_mig_codes_with_var_element tempc);


*/
 
 --nota. En demo se encuentran las siguientes sin traducción:
 /*
1. Código geográfico: Gran Canaria - Área Metropolitana   ES705A11   Comarcas

1.1) Se busca con la siguiente query una equivalencia select * from temp_mig_geo_values tmgv where upper(label_es) like '%GRAN CANARIA%'
se encuentra: COM_GRAN_CANARIA_AREA_METROPOLITANA   granuralidad: COUNTIES
se busca la equivalencia con select * from temp_mig_codes_with_var_element tmcwve where variable_element_code in('COM_GRAN_CANARIA_AREA_METROPOLITANA')
y se encuentra el código ES705A10

1.2) Se crea  a mano una equivalencia similar para el código geográfico inexistente
insert into temp_mig_codes_with_var_element(urn_codelist, code, variable_element_code) values('', 'ES705A11', 'COM_GRAN_CANARIA_AREA_METROPOLITANA');

2. Código geográfico: Tenerife - Área Metropolitana ES709A11 Comarcas
2.1) Se busca con la siguiente query una equivalencia select * from temp_mig_geo_values tmgv where upper(label_es) like '%TENERIFE%'
se encuentra: COM_TENERIFE_AREA_METROPOLITANA   granuralidad: COUNTIES
se busca la equivalencia con select * from temp_mig_codes_with_var_element tmcwve where variable_element_code in('COM_TENERIFE_AREA_METROPOLITANA')
y se encuentra el código ES709A10

2.2) Se crea  a mano una equivalencia similar para el código geográfico inexistente
insert into temp_mig_codes_with_var_element(urn_codelist, code, variable_element_code) values('', 'ES709A11', 'COM_TENERIFE_AREA_METROPOLITANA');

Si hay dudas pregunta a equipo consultoría para asociar.

 */
 
 -- Si son de GPE Y JSONSTAT ASOCIARLOS A LA NUEVA CLASIFICACIÓN QUE SE CREARÁ.
  select t.code from tb_lis_geogr_values_copy t where t.code not in(select code from temp_mig_codes_with_var_element)
and t.id in(select a.geographical_value_fk  from tb_ind_version_geo_cov a, tb_indicators_versions b,  tb_data_sources d
where a.indicator_version_fk = b.id
and d.indicator_version_fk = b.id 
and d.query_environment <> 'METAMAC');

 --SI SON DE METAMAC VER COMO ASOCIAR
  select t.code from tb_lis_geogr_values_copy t where t.code not in(select code from temp_mig_codes_with_var_element)
and t.id in(select a.geographical_value_fk  from tb_ind_version_geo_cov a, tb_indicators_versions b,  tb_data_sources d
where a.indicator_version_fk = b.id
and d.indicator_version_fk = b.id 
and d.query_environment = 'METAMAC');
 
 --4 Rellenar los datos de las distintas tablas sustituyendo el valor geográfico por el elemento de variable asociado.
 --A ejecutar en bd INDICATORS_BD:
  
 ----4.2) tabla tb_data_sources Se debe rellenar a partir de la tabla antigua guardada en tb_lis_geogr_values_copy 
update tb_data_sources d
set geographical_value_fk = (
               select newG.id  
                 from tb_lis_geogr_values_copy l, temp_mig_codes_with_var_element t, tb_lis_geogr_values newG 
                where d.deprecated_geographical_value_fk = l.id  
                  and l.code = t.code 
                  and t.variable_element_code = newG.code limit 1  )
where deprecated_geographical_value_fk is not null and geographical_value_fk  is null;
 
 
 ----4.3) tabla tb_indic_inst_geo_values
 update tb_indic_inst_geo_values d
set geographical_value_fk = (
               select newG.id  
                 from tb_lis_geogr_values_copy l, temp_mig_codes_with_var_element t, tb_lis_geogr_values newG
                where d.deprecated_geographical_value_fk = l.id  
                  and l.code = t.code 
                  and t.variable_element_code = newG.code limit 1  )
where deprecated_geographical_value_fk is not null and geographical_value_fk  is null;

 ----4.4) tabla tb_indic_inst_last_value
 update tb_indic_inst_last_value d
set geographical_code = (
               select newG.code  
                 from tb_lis_geogr_values_copy l, temp_mig_codes_with_var_element t, tb_lis_geogr_values newG
                where d.deprecated_geographical_code = l.code  
                  and l.code = t.code 
                  and t.variable_element_code = newG.code limit 1  )
where deprecated_geographical_code is not null and geographical_code  is null;
 
 
 ----4.5) tabla tb_ind_version_geo_cov
 
 update tb_ind_version_geo_cov d
set geographical_value_fk = (
               select newG.id  
                 from tb_lis_geogr_values_copy l, temp_mig_codes_with_var_element t, tb_lis_geogr_values newG 
                where d.deprecated_geographical_value_fk = l.id  
                  and l.code = t.code 
                  and t.variable_element_code = newG.code limit 1 )
where deprecated_geographical_value_fk is not null and geographical_value_fk  is null;
 
 ----4.6) tabla tb_quantities
  update tb_quantities d
set base_location_fk  = (
               select newG.id  
                 from tb_lis_geogr_values_copy l, temp_mig_codes_with_var_element t, tb_lis_geogr_values newG 
                where d.deprecated_base_location_fk = l.id  
                  and l.code = t.code 
                  and t.variable_element_code = newG.code limit 1 )
where deprecated_base_location_fk is not null and base_location_fk  is null;
 
  ----4.7) tabla tb_indic_version_last_value
 update tb_indic_version_last_value d
set geographical_code = (
               select newG.code  
                 from tb_lis_geogr_values_copy l, temp_mig_codes_with_var_element t, tb_lis_geogr_values newG
                where d.deprecated_geographical_code = l.code  
                  and l.code = t.code 
                  and t.variable_element_code = newG.code limit 1  )
where deprecated_geographical_code is not null and geographical_code  is null;
 
 --4.7. Comprobar que  se han migrado todos los valores para cada una de la tablas anteriores.
 ----4.7.1) 
 select 'tb_indic_inst_last_value', deprecated_geographical_code, geographical_code from tb_indic_inst_last_value where deprecated_geographical_code is not null and geographical_code is null;
 select 'tb_indic_version_last_value', deprecated_geographical_code, geographical_code from tb_indic_version_last_value where deprecated_geographical_code is not null and geographical_code is null;
 ----4.7.2)
 select 'tb_data_sources', deprecated_geographical_value_fk, geographical_value_fk from tb_data_sources where deprecated_geographical_value_fk is not null and geographical_value_fk is null
 union all
 select 'tb_indic_inst_geo_values', deprecated_geographical_value_fk, geographical_value_fk from tb_indic_inst_geo_values where deprecated_geographical_value_fk is not null and geographical_value_fk is null
 union all
 select 'tb_ind_version_geo_cov', deprecated_geographical_value_fk, geographical_value_fk from tb_ind_version_geo_cov where deprecated_geographical_value_fk is not null and geographical_value_fk is null
 union all
 select 'tb_quantities', deprecated_base_location_fk, base_location_fk from tb_quantities where deprecated_base_location_fk is not null and base_location_fk is null;

--y para saber código asociado
 select 'tb_data_sources', deprecated_geographical_value_fk, geographical_value_fk, b.code, t.variable_element_code  from tb_data_sources, tb_lis_geogr_values_copy b, temp_mig_codes_with_var_element t where b.code = t.code and deprecated_geographical_value_fk = b.id and   deprecated_geographical_value_fk is not null and geographical_value_fk is null
 union all
 select 'tb_indic_inst_geo_values', deprecated_geographical_value_fk, geographical_value_fk, b.code, t.variable_element_code from tb_indic_inst_geo_values, tb_lis_geogr_values_copy b, temp_mig_codes_with_var_element t where b.code = t.code and deprecated_geographical_value_fk = b.id and deprecated_geographical_value_fk is not null and geographical_value_fk is null
 union all
 select 'tb_ind_version_geo_cov', deprecated_geographical_value_fk, geographical_value_fk, b.code, t.variable_element_code from tb_ind_version_geo_cov, tb_lis_geogr_values_copy b, temp_mig_codes_with_var_element t where b.code = t.code and deprecated_geographical_value_fk = b.id and deprecated_geographical_value_fk is not null and geographical_value_fk is null
 union all
 select 'tb_quantities', deprecated_base_location_fk, base_location_fk, b.code, t.variable_element_code from tb_quantities, tb_lis_geogr_values_copy b, temp_mig_codes_with_var_element t where b.code = t.code and deprecated_base_location_fk = b.id and deprecated_base_location_fk is not null and base_location_fk is null;
 
 
 --4.8. Si durante el paso 4 se ha tenido que modificar la tabla temp_mig_codes_with_var_element porque se detectaron códigos sin asignación, habrá que replicar esta table en la base de datos indicators_data
 --ATENCIÓN!!! sólo si en indicators_bd se ha cambiado la tabla migrada inicialmente desde srm hacer lo siguiente si no, obviar este paso
 -- 4.8.1. Ir a la bd indicators_data y borrar el contenido de la tabla temp_mig_codes_with_var_element
 -- 4.8.2 Exportar la tabla temp_mig_codes_with_var_element de la bd INDICATORS_BD a CSV
 -- 4.8.3 Importar en la tabla temp_mig_codes_with_var_element de INDICATORS_DATA el fichero CSV obtenido en el paso anterior.
 
 --4.9 exportar la tabla temp_mig_codes_with_var_element e importarla en la base de datos metamac_portal_bd
 
 
--5) Añadir primary keys eliminadas y foreign keys
--5.0) añadir foreign keys
 ----5.0.1 tabla tb_data_sources
  ALTER TABLE indicators_bd.tb_data_sources ADD CONSTRAINT fk_tb_data_sources_geographical_value_fk FOREIGN KEY (geographical_value_fk) REFERENCES indicators_bd.tb_lis_geogr_values(id);
 ----5.0.2 tabla tb_indic_inst_geo_values
ALTER TABLE indicators_bd.tb_indic_inst_geo_values ADD CONSTRAINT fk_tb_indic_inst_geo_values_geographical_value_fk FOREIGN KEY (geographical_value_fk) REFERENCES indicators_bd.tb_lis_geogr_values(id);
  ----5.0.4 tabla tb_ind_version_geo_cov
ALTER TABLE tb_ind_version_geo_cov ADD CONSTRAINT fk_tb_ind_version_geo_cov_geographical_value_fk FOREIGN KEY (geographical_value_fk) REFERENCES indicators_bd.tb_lis_geogr_values(id); 
 ----5.0.5 tabla tb_quantities
ALTER TABLE tb_quantities ADD CONSTRAINT fk_tb_quantities_base_location_fk FOREIGN KEY (base_location_fk) REFERENCES indicators_bd.tb_lis_geogr_values(id); 
 
 
--5.1) tabla tb_indic_inst_geo_values
ALTER TABLE tb_indic_inst_geo_values alter COLUMN geographical_value_fk  set not null;
  ALTER TABLE tb_indic_inst_geo_values ADD	CONSTRAINT pk_tb_indic_inst_geo_values PRIMARY KEY (geographical_value_fk,indicator_instance_fk);
--5.2) tabla tb_indic_inst_last_value
ALTER TABLE tb_indic_inst_last_value alter COLUMN geographical_code  set not null;
CREATE UNIQUE INDEX uq_tb_indic_inst_last_value ON tb_indic_inst_last_value (geographical_code,indicator_instance_fk);
--5.3) tabla tb_ind_version_geo_cov 
ALTER TABLE tb_ind_version_geo_cov alter COLUMN geographical_value_fk  set not null;
 CREATE UNIQUE INDEX uq_tb_ind_version_geo_cov ON tb_ind_version_geo_cov (geographical_value_fk,indicator_version_fk);
--5.4) tabla tb_indic_version_last_value
ALTER TABLE tb_indic_version_last_value alter COLUMN geographical_code  set not null;
CREATE UNIQUE INDEX uq_tb_indic_version_last_value ON tb_indic_version_last_value (geographical_code, indicator_version_fk);

 
 --6) Actualizar valores geográficos con los códigos de elementos de variable en la bd indicators para cada tabla "..data" de cada fuente de datos.
 --Crear índice en tabla temporal "temp_mig_codes_with_var_element" para acelerar búsquedas. En bd indicators_data_bd:
 CREATE INDEX IX_temp_mig_codes_with_var_element ON temp_mig_codes_with_var_element(code);

 --6.0) Asegurarse de que hay relación con su elemento de variable para todos los códigos:
 -- 6.0.1) Ejecutar esta consulta:
select ' select code from (SELECT distinct(t.' || b.column_name || ') as code FROM ' || a.table_name  || ' t) as d  where code not in(select code from temp_mig_codes_with_var_element) ' || ' union all' 
 from tb_datasets a, tb_dataset_dimensions b 
 where a.id = b.dataset_fk 
 and dimension_id = 'GEOGRAPHICAL'
  and table_name in (select upper(table_name) from INFORMATION_SCHEMA.COLUMNS);
 
  --6.0.2) Quitar el último "union all" generado en el paso anterior y sustituirlo por ";"
 --6.0.3) Ejecutar las consultas generadas en el paso anterior. Se puede dar la  circunstancia que alguna tabla de datos no exista. En este caso, quitar la que da error y ejecutar de nuevo la consulta.
 --6.0.4) Si la consulta no da resultados todo ok y se puede continuar.
 --6.0.5) Si la consulta devuelve algún dato es que alguna entrada no tiene elemento de variable asociado. Hay que hablar con equipo de consultoría para buscar la relación.
 --PRUEBAS EN DEMO:
 ----tiempo que tardó la consulta: 751 tablas de datos y la consulta tardó  52 segundos
 --6.1) renombrar columna con valores geográficos de cada tabla
 ----6.1.1 obtener alter table para renombrar
select ' ALTER TABLE ' || a.table_name  || ' RENAME COLUMN ' || b.column_name  || ' TO deprecated_'  ||  b.column_name || ';' 
 from tb_datasets a, tb_dataset_dimensions b 
 where a.id = b.dataset_fk 
 and dimension_id = 'GEOGRAPHICAL'
 and table_name in (select upper(table_name) from INFORMATION_SCHEMA.COLUMNS);
 ----6.1.2 Ejecutar resultados anteriores en la bd indicators_data
 --!! ATENCIÓN!! se han detectado que alguna tabla de datos luego no existe. En este caso habrá que eliminar esa línea de los resultados anteriores y volver a lanzar el script. Las entradas antes del fallo se habrán ejecutado por lo que quitar y seguir a partir de ahí.
  --6.2) crear columna con valores geográficos de cada tabla
 ----6.2.1 obtener alter table con nueva columna
select ' ALTER TABLE ' || a.table_name  || ' ADD COLUMN ' || b.column_name  || ' varchar(100)' || ';' 
 from tb_datasets a, tb_dataset_dimensions b 
 where a.id = b.dataset_fk 
 and dimension_id = 'GEOGRAPHICAL'
 and table_name in (select upper(table_name) from INFORMATION_SCHEMA.COLUMNS);
  ----6.2.2 Ejecutar resultados anteriores en la bd indicators_data
 --!! ATENCIÓN!! se han detectado que alguna tabla de datos luego no existe. En este caso habrá que eliminar esa línea de los resultados anteriores y volver a lanzar el script. Las entradas antes del fallo se habrán ejecutado por lo que quitar y seguir a partir de ahí.

--6.3 Actualizar valores geográficos para el nuevo campo creado anteriormente a partir del campo antiguo. Dependiendo del nuevo de fuentes de datos puede tardar.
select ' UPDATE ' || a.table_name  || ' tn set ' || b.column_name  || ' = (select variable_element_code from temp_mig_codes_with_var_element t where t.code = tn.deprecated_' || b.column_name || ' limit 1)' || ';' 
 from tb_datasets a, tb_dataset_dimensions b
 where a.id = b.dataset_fk 
 and dimension_id = 'GEOGRAPHICAL'
 and table_name in (select upper(table_name) from INFORMATION_SCHEMA.COLUMNS); 
  --!! ATENCIÓN!! se han detectado que alguna tabla de datos luego no existe. En este caso habrá que eliminar esa línea de los resultados anteriores y volver a lanzar el script. Las entradas antes del fallo se habrán ejecutado por lo que quitar y seguir a partir de ahí.
  
  -- !!Estimación de tiempo dado en demo: 8 minutos.
  
 --6.4 comprobar que todos los valores tienen correspondencia. No deben salir entradas. En caso contrario hay que estudiar los casos para asociar los códigos a elementos de variable.
select ' SELECT ''' ||  a.table_name || ''',' || b.column_name || ', deprecated_'  || b.column_name ||  ' from ' || a.table_name  || ' where ' || b.column_name || ' is null union all ' 
 from tb_datasets a, tb_dataset_dimensions b
 where a.id = b.dataset_fk 
 and dimension_id = 'GEOGRAPHICAL'
  and table_name in (select upper(table_name) from INFORMATION_SCHEMA.COLUMNS); 
 
 --6.5 Para todo el resultado obtenido, quitar el último UNION ALL y sustituirlo por ";"
 --6.6 Ejecutar el resultado anterior.
 --!! ATENCIÓN!! se han detectado que alguna tabla de datos luego no existe. En este caso habrá que eliminar esa línea de los resultados anteriores y volver a lanzar la consulta. Las entradas antes del fallo se habrán ejecutado por lo que quitar y seguir a partir de ahí.
 --AYUDA. Si la consulta anterior devuelve resultados, se pueden sacar los valores distintos de esta manera
 -- 6.X.1. Ejecutar
 select ' SELECT deprecated_'  || b.column_name ||  ' valueD from ' || a.table_name  || ' where ' || b.column_name || ' is null union all ' 
 from tb_datasets a, tb_dataset_dimensions b
 where a.id = b.dataset_fk 
 and dimension_id = 'GEOGRAPHICAL';
 -- 6.x.2 quitar el último UNION ALL sin poner ";"
 -- 6.x.3 el resultado anterior ponerlo dentro de esta consulta
  select distinct(t.valueD) from (
    PONER AQUÍ consultas obtenidas del apartado anterior.
 ) t
 order by valueD;
 
 --!!PUEDE que alguna consulta de error porque la tabla no exista (se ha detectado alguna en desarrollo) En estos  casos quitar la consulta asociada al a la tabla que da error.
 
 -- Si todo va bien se obtienen los códigos de clasificación para los cuales no se encontró asociación con un código de elemento de variable. Hay que resolverlo con Vicky.
  
  
  --6.7. Se detecta que al renombrar el campo en cada tabla data, la vista asociada a cada tabla se modifica automáticamente poniendo un alias al nuevo campo deprecado. Se añade una consulta de 
  --este estilo: -----data_y67chqktpnp0cnnpdkdciz6.deprecated_dimension_00 AS dimension_00,------
  --Hay que reestablecer todas las vistas afectadas para que consulten el campo dimension_00 directamente.
  --6.7.1. Obtener las vistas afectadas con la siguiente consulta 
  select 'CREATE OR REPLACE VIEW ' || table_name || ' AS' || view_definition,
       table_schema as schema_name, table_name as view_name, view_definition
    from information_schema.views
   where table_schema not in ('information_schema', 'pg_catalog')
     and view_definition like '%deprecated_%'
order by schema_name, view_name;
    --6.7.2  Coger todos los resultados de la primera columna de la consulta anterior y ponerlo en un editor de texto.
    --6.7.3  Sustituir la siguiente cadena
           -- deprecated_dimension_00 AS dimension_00,
           -- por
           -- dimension_00,
    --6.7.4 Ejecutar la recreación de las vistas con la modificación realizada en el paso anterior. Con todo ésto las vistas ya se quedan adecuadamente creadas y se puede avanzar al siguiente paso.  
         
         
 --7) Eliminación de columna deprecated del data.
 -- Si el paso 6 fue bien, se deberá borrar la columna deprecated del data. Ésto es debido a que el data tiene una vista asociada. Y ciertas operaciones en la aplicación la modifican quedando con este campo deprecado.
 --Para ello, 
 --7.1 Obtener script de borrado de la columna de cada tabla
 select ' ALTER TABLE ' || a.table_name  || ' DROP COLUMN deprecated_'  ||  b.column_name || ';' 
 from tb_datasets a, tb_dataset_dimensions b 
 where a.id = b.dataset_fk 
 and dimension_id = 'GEOGRAPHICAL'
   and table_name in (select upper(table_name) from INFORMATION_SCHEMA.COLUMNS); 
 
 --7.2 . Ejecutar los scripts obtenidos en el apartado anterior que borrará todas las columnas deprecadas.
 
 -- Anexo paso 7.  No necesario este paso si el paso 7 fue bien. 
 --Todas deberían tener el campo deprecado. Pero si en el algún entorno se necesita recuperar tablas específicas con este campo por lo que sea
  select '''' || upper(table_name) || ''','
from INFORMATION_SCHEMA.COLUMNS where column_name like '%deprecated%'

-- Luego habría que obtener el script de esas tablas únicamente
 select ' ALTER TABLE ' || a.table_name  || ' DROP COLUMN deprecated_'  ||  b.column_name || ';' 
 from tb_datasets a, tb_dataset_dimensions b 
 where a.id = b.dataset_fk 
 and dimension_id = 'GEOGRAPHICAL'
 and a.table_name in(<poner aquí el resultado de la select anterior> ); 
  