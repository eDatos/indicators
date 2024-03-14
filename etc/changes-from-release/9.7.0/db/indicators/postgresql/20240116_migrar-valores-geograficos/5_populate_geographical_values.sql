-- --------------------------------------------------------------------------------------------------
-- EDATOS-3827 - Integración con códigos geográficos de e-Semántica
-- 
-- Script para rellenar la tabla maestra de valores geográficos "tb_lis_geogr_values"
-- --------------------------------------------------------------------------------------------------

-- 0 Crear los elementos de variable en la bd indicators. 
-- El proceso de crar las sentencias y volcarlas en la bd tb_lis_geogr_values directamente es bastante costoso ya que hay unos 135000 entradas a copiar. 
-- En desarrollo tarda unas 8 horas. 
-- por tanto, este paso se deberá hacer días antes en una tabla temporal de indicators y luego se traspasará a la tabla final el mismo día con un import que es más rápido.
--0.1) Crear una secuencia temporal. Por defecto empieza en 1.
create sequence TEMP_SEQ_GEOGR_VALUES; 
-- 0.2) Crear una tabla temporal para poner los valores que irán en tb_lis_geogr_values. Atención!! los internationalString sí se crearán inmediatamente.
CREATE TABLE temp_tb_lis_geogr_values (
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
	CONSTRAINT temp_pk_tb_lis_geogr_values PRIMARY KEY (id)
);

--0.3) Ejecutar la siguiente consulta en la bd del INDICATORS
--¡¡¡¡¡¡Atención!!!!!! puede dar problemas en dbeaver porque son muchas entradas. Lo que se puede hacer es volcarlo en un fichero (en sublime por ejemplo)
-- y ejecutarlo como script en dbeaver

select
'
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);' ||
case when t.label_es is not null then '  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || replace(t.label_es, '''', '''''') || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);' else '' end ||
case when t.label_ca is not null then '
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || replace(t.label_ca, '''', '''''') || ''', ''ca'', currval(''SEQ_I18NSTRS''), 1);' else '' end || 
case when t.label_en is not null then '
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || replace(t.label_en, '''', '''''') || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);' else '' end || '
INSERT INTO temp_tb_lis_geogr_values(id, code, latitude, longitude, global_order, update_date_tz, update_date, uuid, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version", title_fk, granularity_fk)
 values (nextval(''TEMP_SEQ_GEOGR_VALUES''),'
|| '''' || t.code || ''', '
|| coalesce('''' || t.latitude || '''', 'null') || ', '
|| coalesce('''' || t.longitude || '''', 'null') || ', ' 
|| '''' || t.code || ''', '
|| 'null, '
|| 'null, '
|| '''' || t.uuid || ''', '
|| '''' || t.created_date_tz || ''', '
|| '''' || t.created_date || ''', '
|| '''' || t.created_by || ''', '
|| '''' || t.created_date_tz || ''', '
|| '''' || t.created_date || ''', '
|| '''' || t.created_by || ''', '
|| '''' || t.version || ''', '
|| 'currval(''SEQ_I18NSTRS'')' || ', '
|| t.granularity_fk
|| ');'

from  temp_mig_geo_values t;

--0.4. Esta consulta puede dar problemas de rendimiento al hacer el copy. Por lo que se puede optar por exportar la consulta a un fichero. Para ello hacer lo siguiente
-- 1. Seleccionar la  consulta en dbeaver
-- 2. desplegar menú Ejecutar (Execute)
-- 3. Seleccionar submenú Ejecutar desde consulta (Execute from query)
-- 4. Seleccionar como tipo de salida "TXT"
-- 5. Ampliar el fetch size a 300000 que por defecto está en 10000
-- 6. Seleccionar directorio de salida
-- 7. Exportar y generará un fichero con las inserciones en el directorio de  salida.
-- 8. Genera la salida pero con un delimitador entre INSERT.
----8.1 Quitar la primera línea "|?column?    ".  
----8.2 Quitar el delimitador "|" sustituyéndolo por "" en un editor de textos (sublime, visual studio code)
----8.3 Quitar el delimitador "¶" sustituyéndolo por "" en un editor de textos (sublime, visual studio code)
----8.4 Ir a consola de comandos y ejecutar la siguiente sentencia (donde estén los comandos para el dump. Por eje. en local hay que situarse en carpeta  con dump si no está mapeado ej: E:\program files\PostgreSQL\14\bin )
psql -U "indicators_bd" -W -h localhost indicators_bd < E:\mig\<NOMBRE_FICHERO_CREADO>
--EJ:  psql -U "indicators_bd" -W -h localhost -p 5433 indicators_bd < E:\mig\temp_mig_geo_values_202401101144.txt
-- tiempo estimado: en desarrollo tardó 8 horas con un fichero con 134159 entradas
  
-- 0.5. Borrar secuencia temporal que ya no se va a usar  "TEMP_SEQ_GEOGR_VALUES"
 drop  sequence TEMP_SEQ_GEOGR_VALUES;
-- En este punto, se tendrán los valores en una tabla temporal "temp_tb_lis_geogr_values"

---------------------------
--!!!!!!!!!!!!!!!!!!!!!!!!!!!ATENCIÓN!!!!!!!!!!!!!!!!!!!!
-- Para realizar los próximos pasos asegurarse que anteriormente se ha creado la tabla temp_tb_lis_geogr_values con todos los elementos de variable de la variable VR_TERRITORIO de srm
---------------------------

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
-- 0.6.3) Obtener el último valor select max(id) from tb_lis_geogr_values 
-- 0.6.4) con el valor dado anteriormente
ALTER SEQUENCE SEQ_GEOGR_VALUES RESTART WITH PONER_AQUI_VALOR_DE_PASO_ANTERIOR;
 
 --4 Rellenar nuevos campos creados
 --PENDIENTE!!!!!!! VER LO QUE NO TIENE TRADUCCIÓN ACTULAMENTE
 select t.code from tb_lis_geogr_values_copy t where t.code not in(select code from temp_mig_codes_with_var_element);
 
 --nota. En desarrollo se encuentran las siguientes:
 /*
TENERIFE
CANARIAS
ESPANA
  
Lo ideal es buscar en "temp_mig_codes_with_var_element" una asociación con la siguiente select:
 select * from temp_mig_codes_with_var_element where variable_element_code  like '%ESPA%'
 select * from temp_mig_codes_with_var_element where variable_element_code  like '%TENERIFE%'


y luego se crea  a mano
insert into temp_mig_codes_with_var_element(urn_codelist, code, variable_element_code) values('', 'TENERIFE', 'ISLA_TENERIFE');
insert into temp_mig_codes_with_var_element(urn_codelist, code, variable_element_code) values('', 'CANARIAS', 'CCAA_CANARIAS');
insert into temp_mig_codes_with_var_element(urn_codelist, code, variable_element_code) values('', 'ESPANA', '2016_ESPANIA');
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
 
 --A ejecutar en bd INDICATORS_BD:
 
 ----4.1) tabla tb_data_sources Se debe rellenar a partir de la tabla antigua guardada en tb_lis_geogr_values_copy 
update tb_data_sources d
set geographical_value_fk = (
               select newG.id  
                 from tb_lis_geogr_values_copy l, temp_mig_codes_with_var_element t, tb_lis_geogr_values newG 
                where d.deprecated_geographical_value_fk = l.id  
                  and l.code = t.code 
                  and t.variable_element_code = newG.code limit 1  )
where deprecated_geographical_value_fk is not null and geographical_value_fk  is null;
 
 
 ----4.2) tabla tb_indic_inst_geo_values
 update tb_indic_inst_geo_values d
set geographical_value_fk = (
               select newG.id  
                 from tb_lis_geogr_values_copy l, temp_mig_codes_with_var_element t, tb_lis_geogr_values newG, tb_indicators_instances ii, tb_indicators i, tb_indicators_versions iv
                where d.indicator_instance_fk = ii.id 
                  and ii.indicator_fk = i.id
                  and iv.indicator_fk = i.id 
                  and d.deprecated_geographical_value_fk = l.id  
                  and l.code = t.code 
                  and t.variable_element_code = newG.code limit 1  )
where deprecated_geographical_value_fk is not null and geographical_value_fk  is null;

 ----4.3) tabla tb_indic_inst_last_value
 update tb_indic_inst_last_value d
set geographical_code = (
               select newG.code  
                 from tb_lis_geogr_values_copy l, temp_mig_codes_with_var_element t, tb_lis_geogr_values newG, tb_indicators_instances ii, tb_indicators i, tb_indicators_versions iv
                where d.indicator_instance_fk = ii.id 
                  and ii.indicator_fk = i.id
                  and iv.indicator_fk = i.id 
                  and d.deprecated_geographical_code = l.code  
                  and l.code = t.code 
                  and t.variable_element_code = newG.code limit 1  )
where deprecated_geographical_code is not null and geographical_code  is null;
 
 
 ----4.4) tabla tb_ind_version_geo_cov
 
 update tb_ind_version_geo_cov d
set geographical_value_fk = (
               select newG.id  
                 from tb_lis_geogr_values_copy l, temp_mig_codes_with_var_element t, tb_lis_geogr_values newG 
                where d.deprecated_geographical_value_fk = l.id  
                  and l.code = t.code 
                  and t.variable_element_code = newG.code limit 1 )
where deprecated_geographical_value_fk is not null and geographical_value_fk  is null;
 
 ----4.5) tabla tb_quantities
  update tb_quantities d
set base_location_fk  = (
               select newG.id  
                 from tb_lis_geogr_values_copy l, temp_mig_codes_with_var_element t, tb_lis_geogr_values newG 
                where d.deprecated_base_location_fk = l.id  
                  and l.code = t.code 
                  and t.variable_element_code = newG.code limit 1 )
where deprecated_base_location_fk is not null and base_location_fk  is null;
 
 --4.6. Comprobar que  se han migrado todos los valores para cada una de la tablas anteriores.
 ----4.6.1) 
 select 'tb_indic_inst_last_value', deprecated_geographical_code, geographical_code from tb_indic_inst_last_value where deprecated_geographical_code is not null and geographical_code is null;
 ----4.6.2)
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
 
 
 --4.7. Si durante el paso 4 se ha tenido que modificar la tabla temp_mig_codes_with_var_element porque se detectaron códigos sin asignación, habrá que replicar esta table en la base de datos indicators_data
 --ATENCIÓN!!! sólo si en indicators_bd se ha cambiado la tabla migrada inicialmente desde srm hacer lo siguiente si no, obviar este paso
 -- 4.7.1. Ir a la bd indicators_data y borrar el contenido de la tabla temp_mig_codes_with_var_element
 -- 4.7.2 Exportar la tabla temp_mig_codes_with_var_element de la bd INDICATORS_BD a CSV
 -- 4.7.3 Importar en la tabla temp_mig_codes_with_var_element de INDICATORS_DATA el fichero CSV obtenido en el paso anterior.
 
 
 
 
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
 
 --6) Actualizar valores geográficos con los códigos de elementos de variable en la bd indicators para cada tabla "..data" de cada fuente de datos.
 --6.0) Asegurarse de que hay relación con su elemento de variable para todos los códigos:
 -- 6.0.1) Ejecutar esta consulta:
select ' SELECT ''' || a.table_name || ''', t.' || b.column_name || ' FROM ' || a.table_name  || ' t  where t.' || b.column_name  || ' not in(select code from temp_mig_codes_with_var_element) ' || ' union all' 
 from tb_datasets a, tb_dataset_dimensions b 
 where a.id = b.dataset_fk 
 and dimension_id = 'GEOGRAPHICAL';  
  --6.0.2) Quitar el último "union all" generado en el paso anterior y sustituirlo por ";"
 --6.0.3) Ejecutar las consultas generadas en el paso anterior. Se puede dar la  circunstancia que alguna tabla de datos no exista. En este caso, quitar la que da error y ejecutar de nuevo la consulta.
 --6.0.4) Si la consulta no da resultados todo ok y se puede continuar.
 --6.0.5) Si la consulta devuelve algún dato es que alguna entrada no tiene elemento de variable asociado. Hay que hablar con equipo de consultoría para buscar la relación.
 --6.1) renombrar columna con valores geográficos de cada tabla
 ----6.1.1 obtener alter table para renombrar
select ' ALTER TABLE ' || a.table_name  || ' RENAME COLUMN ' || b.column_name  || ' TO deprecated_'  ||  b.column_name || ';' 
 from tb_datasets a, tb_dataset_dimensions b 
 where a.id = b.dataset_fk 
 and dimension_id = 'GEOGRAPHICAL';
 ----6.1.2 Ejecutar resultados anteriores en la bd indicators_data
 --!! ATENCIÓN!! se han detectado que alguna tabla de datos luego no existe. En este caso habrá que eliminar esa línea de los resultados anteriores y volver a lanzar el script. Las entradas antes del fallo se habrán ejecutado por lo que quitar y seguir a partir de ahí.
  --6.2) crear columna con valores geográficos de cada tabla
 ----6.2.1 obtener alter table con nueva columna
select ' ALTER TABLE ' || a.table_name  || ' ADD COLUMN ' || b.column_name  || ' varchar(100)' || ';' 
 from tb_datasets a, tb_dataset_dimensions b 
 where a.id = b.dataset_fk 
 and dimension_id = 'GEOGRAPHICAL';
  ----6.2.2 Ejecutar resultados anteriores en la bd indicators_data
 --!! ATENCIÓN!! se han detectado que alguna tabla de datos luego no existe. En este caso habrá que eliminar esa línea de los resultados anteriores y volver a lanzar el script. Las entradas antes del fallo se habrán ejecutado por lo que quitar y seguir a partir de ahí.

--6.3 Actualizar valores geográficos para el nuevo campo creado anteriormente a partir del campo antiguo. Dependiendo del nuevo de fuentes de datos puede tardar.
select ' UPDATE ' || a.table_name  || ' tn set ' || b.column_name  || ' = (select variable_element_code from temp_mig_codes_with_var_element t where t.code = tn.deprecated_' || b.column_name || ' limit 1)' || ';' 
 from tb_datasets a, tb_dataset_dimensions b
 where a.id = b.dataset_fk 
 and dimension_id = 'GEOGRAPHICAL';
  --!! ATENCIÓN!! se han detectado que alguna tabla de datos luego no existe. En este caso habrá que eliminar esa línea de los resultados anteriores y volver a lanzar el script. Las entradas antes del fallo se habrán ejecutado por lo que quitar y seguir a partir de ahí.
  
 --6.4 comprobar que todos los valores tienen correspondencia. No deben salir entradas. En caso contrario hay que estudiar los casos para asociar los códigos a elementos de variable.
select ' SELECT ''' ||  a.table_name || ''',' || b.column_name || ', deprecated_'  || b.column_name ||  ' from ' || a.table_name  || ' where ' || b.column_name || ' is null union all ' 
 from tb_datasets a, tb_dataset_dimensions b
 where a.id = b.dataset_fk 
 and dimension_id = 'GEOGRAPHICAL';
 
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
  
  