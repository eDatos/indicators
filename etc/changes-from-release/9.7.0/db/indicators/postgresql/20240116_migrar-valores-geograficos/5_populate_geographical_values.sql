-- --------------------------------------------------------------------------------------------------
-- EDATOS-3827 - Integración con códigos geográficos de e-Semántica
-- 
-- Script para rellenar la tabla maestra de valores geográficos "tb_lis_geogr_values"
-- --------------------------------------------------------------------------------------------------

--1. Hacer copia de seguridad de tabla "tb_lis_geogr_values"
-- indicators_bd.tb_lis_geogr_values definition

--1.1) Crear copia de tabla
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

--1.2) Copiar los datos a la tabla de copia
INSERT INTO indicators_bd.tb_lis_geogr_values_copy
(id, code, latitude, longitude, global_order, update_date_tz, update_date, uuid, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version", title_fk, granularity_fk)
 SELECT id, code, latitude, longitude, global_order, update_date_tz, update_date, uuid, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version", title_fk, granularity_fk
FROM tb_lis_geogr_values;

--1.3) Borrar datos de la tabla  "tb_lis_geogr_values"
delete  from tb_lis_geogr_values;

-- 2 Ejecutar la siguiente consulta en la bd del INDICATORS
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
INSERT INTO tb_lis_geogr_values(id, code, latitude, longitude, global_order, update_date_tz, update_date, uuid, created_date_tz, created_date, created_by, last_updated_tz, last_updated, last_updated_by, "version", title_fk, granularity_fk)
 values (nextval(''SEQ_GEOGR_VALUES''),'
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

--3. Esta consulta puede dar problemas de rendimiento al hacer el copy. Por lo que se puede optar por exportar la consulta a un fichero. Para ello hacer lo siguiente
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
-- tiempo estimado: en desarrollo tardó 8 minutos con un fichero con 2159 entradas

--5) Crear nuevos campos en tablas relacionadas y deprecar antiguos
 ----5.1 tabla tb_data_sources
 ALTER TABLE tb_data_sources RENAME COLUMN geographical_value_fk TO deprecated_geographical_value_fk;
 ALTER TABLE tb_data_sources alter COLUMN deprecated_geographical_value_fk  drop not null;
 ALTER TABLE indicators_bd.tb_data_sources DROP CONSTRAINT fk_tb_data_sources_geographical_value_fk;
 ALTER TABLE tb_data_sources ADD COLUMN geographical_value_fk BIGINT;
  ALTER TABLE indicators_bd.tb_data_sources ADD CONSTRAINT fk_tb_data_sources_geographical_value_fk FOREIGN KEY (geographical_value_fk) REFERENCES indicators_bd.tb_lis_geogr_values(id);

 ----5.2 tabla tb_indic_inst_geo_values
ALTER TABLE tb_indic_inst_geo_values RENAME COLUMN geographical_value_fk TO deprecated_geographical_value_fk;
ALTER TABLE tb_indic_inst_geo_values DROP	CONSTRAINT pk_tb_indic_inst_geo_values;
ALTER TABLE tb_indic_inst_geo_values alter COLUMN deprecated_geographical_value_fk  drop not null;
ALTER TABLE indicators_bd.tb_indic_inst_geo_values DROP CONSTRAINT fk_tb_indic_inst_geo_values_geographical_value_fk;
 ALTER TABLE tb_indic_inst_geo_values ADD COLUMN geographical_value_fk BIGINT;
ALTER TABLE indicators_bd.tb_indic_inst_geo_values ADD CONSTRAINT fk_tb_indic_inst_geo_values_geographical_value_fk FOREIGN KEY (geographical_value_fk) REFERENCES indicators_bd.tb_lis_geogr_values(id);

 ----5.3 tabla tb_indic_inst_last_value
 ALTER TABLE tb_indic_inst_last_value RENAME COLUMN geographical_code TO deprecated_geographical_code;
ALTER TABLE tb_indic_inst_last_value drop constraint uq_tb_indic_inst_last_value;
ALTER TABLE tb_indic_inst_last_value alter COLUMN deprecated_geographical_code  drop not null;
 ALTER TABLE tb_indic_inst_last_value ADD COLUMN geographical_code varchar(255);
 
 ----5.4 tabla tb_ind_version_geo_cov
 ALTER TABLE tb_ind_version_geo_cov RENAME COLUMN geographical_value_fk TO deprecated_geographical_value_fk;
ALTER TABLE tb_ind_version_geo_cov DROP CONSTRAINT fk_tb_ind_version_geo_cov_geographical_value_fk;
ALTER TABLE tb_ind_version_geo_cov DROP CONSTRAINT uq_tb_ind_version_geo_cov;
ALTER TABLE tb_ind_version_geo_cov alter COLUMN deprecated_geographical_value_fk  drop not null;
 ALTER TABLE tb_ind_version_geo_cov ADD COLUMN geographical_value_fk BIGINT;
ALTER TABLE tb_ind_version_geo_cov ADD CONSTRAINT fk_tb_ind_version_geo_cov_geographical_value_fk FOREIGN KEY (geographical_value_fk) REFERENCES indicators_bd.tb_lis_geogr_values(id); 

 ----5.5 tabla tb_quantities
 ALTER TABLE tb_quantities RENAME COLUMN base_location_fk TO deprecated_base_location_fk;
ALTER TABLE tb_quantities DROP CONSTRAINT fk_tb_quantities_base_location_fk;
 ALTER TABLE tb_quantities ADD COLUMN base_location_fk BIGINT;
ALTER TABLE tb_quantities ADD CONSTRAINT fk_tb_quantities_base_location_fk FOREIGN KEY (base_location_fk) REFERENCES indicators_bd.tb_lis_geogr_values(id); 
  
 
 --6 Rellenar nuevos campos creados
 --PENDIENTE!!!!!!! VER LO QUE NO TIENE TRADUCCIÓN ACTULAMENTE
 select t.code from tb_lis_geogr_values_copy t where t.code not in(select code from temp_mig_codes_with_var_element);
 
 --nota. En desarrollo se encuentran las siguientes:
 /*
  select t.code, t.title_fk, tls.* from tb_lis_geogr_values_copy t, tb_localised_strings tls 
where t.code not in(select code from temp_mig_codes_with_var_element)
and tls.international_string_fk = t.title_fk
and tls.locale ='es';
 
 
 09	1204	1404	Canarias	es		0	1204
05	1205	1405	El Hierro	es		1	1205
01	1206	1406	Fuerteventura	es		0	1206
03	1207	1407	Gran Canaria	es		0	1207
04	1208	1408	La Gomera	es		0	1208
06	1209	1409	La Palma	es		0	1209
02	1210	1410	Lanzarote	es		0	1210
07	1211	1411	Tenerife	es		0	1211
08	1212	1412	CAC	es		0	1212

Se soluciona añadiendo a mano
insert into temp_mig_codes_with_var_element values('MANUAL', '09', 'CANARIAS');
insert into temp_mig_codes_with_var_element values('MANUAL', '05', 'EL_HIERRO');
insert into temp_mig_codes_with_var_element values('MANUAL', '01', 'FUERTEVENTURA');
insert into temp_mig_codes_with_var_element values('MANUAL', '03', 'GRAN_CANARIA');
insert into temp_mig_codes_with_var_element values('MANUAL', '04', 'LA_GOMERA');
insert into temp_mig_codes_with_var_element values('MANUAL', '06', 'LA_PALMA');
insert into temp_mig_codes_with_var_element values('MANUAL', '02', 'LANZAROTE');
insert into temp_mig_codes_with_var_element values('MANUAL', '07', 'TENERIFE');
insert into temp_mig_codes_with_var_element values('MANUAL', '08', 'GEO_OTROS');
 */
 
 
 -- En principio no debería ocurrir pero si pasa añadir a la no normalizada. Se añaden a la clasificación no normalizada. Hablar con Vicky para asociar.
 
 
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
 
 ----6.1) tabla tb_data_sources Se debe rellenar a partir de la tabla antigua guardada en tb_lis_geogr_values_copy 
update tb_data_sources d
set geographical_value_fk = (
               select newG.id  
                 from tb_lis_geogr_values_copy l, temp_mig_codes_with_var_element t, tb_lis_geogr_values newG 
                where d.deprecated_geographical_value_fk = l.id  
                  and l.code = t.code 
                  and t.variable_element_code = newG.code limit 1  )
where deprecated_geographical_value_fk is not null and geographical_value_fk  is null;
 
 
 ----6.2) tabla tb_indic_inst_geo_values
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

 ----6.3) tabla tb_indic_inst_last_value
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
 
 
 ----6.4) tabla tb_ind_version_geo_cov
 
 update tb_ind_version_geo_cov d
set geographical_value_fk = (
               select newG.id  
                 from tb_lis_geogr_values_copy l, temp_mig_codes_with_var_element t, tb_lis_geogr_values newG 
                where d.deprecated_geographical_value_fk = l.id  
                  and l.code = t.code 
                  and t.variable_element_code = newG.code limit 1 )
where deprecated_geographical_value_fk is not null and geographical_value_fk  is null;
 
 ----6.5) tabla tb_quantities
  update tb_quantities d
set base_location_fk  = (
               select newG.id  
                 from tb_lis_geogr_values_copy l, temp_mig_codes_with_var_element t, tb_lis_geogr_values newG 
                where d.deprecated_base_location_fk = l.id  
                  and l.code = t.code 
                  and t.variable_element_code = newG.code limit 1 )
where deprecated_base_location_fk is not null and base_location_fk  is null;
 
 --6.6. Comprobar que  se han migrado todos los valores para cada una de la tablas anteriores.
 ----6.6.1) 
 select 'tb_indic_inst_last_value', deprecated_geographical_code, geographical_code from tb_indic_inst_last_value where deprecated_geographical_code is not null and geographical_code is null;
 ----6.6.2)
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
 
 
 --6.7. Si durante el paso 6 se ha tenido que modificar la tabla temp_mig_codes_with_var_element porque se detectaron códigos sin asignación, habrá que replicar esta table en la base de datos indicators_data
 --ATENCIÓN!!! sólo si en indicators_bd se ha cambiado la tabla migrada inicialmente desde srm hacer lo siguiente si no, obviar este paso
 -- 6.7.1. Ir a la bd indicators_data y borrar el contenido de la tabla temp_mig_codes_with_var_element
 -- 6.7.2 Exportar la tabla temp_mig_codes_with_var_element de la bd INDICATORS_BD a CSV
 -- 6.7.3 Importar en la tabla temp_mig_codes_with_var_element de INDICATORS_DATA el fichero CSV obtenido en el paso anterior.
 
 
 
 
--7) Añadir primary keys eliminadas
--7.1) tabla tb_indic_inst_geo_values
ALTER TABLE tb_indic_inst_geo_values alter COLUMN geographical_value_fk  set not null;
  ALTER TABLE tb_indic_inst_geo_values ADD	CONSTRAINT pk_tb_indic_inst_geo_values PRIMARY KEY (geographical_value_fk,indicator_instance_fk);
--7.2) tabla tb_indic_inst_last_value
ALTER TABLE tb_indic_inst_last_value alter COLUMN geographical_code  set not null;
CREATE UNIQUE INDEX uq_tb_indic_inst_last_value ON tb_indic_inst_last_value (geographical_code,indicator_instance_fk);
--7.3) tabla tb_ind_version_geo_cov 
ALTER TABLE tb_ind_version_geo_cov alter COLUMN geographical_value_fk  set not null;
 CREATE UNIQUE INDEX uq_tb_ind_version_geo_cov ON tb_ind_version_geo_cov (geographical_value_fk,indicator_version_fk);
 
 --8) Actualizar valores geográficos con los códigos de elementos de variable en la bd indicators para cada tabla "..data" de cada fuente de datos.
 --8.0) Asegurarse de que hay relación con su elemento de variable para todos los códigos:
 -- 8.0.1) Ejecutar esta consulta:
select ' SELECT ''' || a.table_name || ''', t.' || b.column_name || ' FROM ' || a.table_name  || ' t  where t.' || b.column_name  || ' not in(select code from temp_mig_codes_with_var_element) ' || ' union all' 
 from tb_datasets a, tb_dataset_dimensions b 
 where a.id = b.dataset_fk 
 and dimension_id = 'GEOGRAPHICAL';  
  --8.0.2) Quitar el último "union all" generado en el paso anterior y sustituirlo por ";"
 --8.0.3) Ejecutar las consultas generadas en el paso anterior. Se puede dar la  circunstancia que alguna tabla de datos no exista. En este caso, quitar la que da error y ejecutar de nuevo la consulta.
 --8.0.4) Si la consulta no da resultados todo ok y se puede continuar.
 --8.0.5) Si la consulta devuelve algún dato es que alguna entrada no tiene elemento de variable asociado. Hay que hablar con equipo de consultoría para buscar la relación.
 --8.1) renombrar columna con valores geográficos de cada tabla
 ----8.1.1 obtener alter table para renombrar
select ' ALTER TABLE ' || a.table_name  || ' RENAME COLUMN ' || b.column_name  || ' TO deprecated_'  ||  b.column_name || ';' 
 from tb_datasets a, tb_dataset_dimensions b 
 where a.id = b.dataset_fk 
 and dimension_id = 'GEOGRAPHICAL';
 ----8.1.2 Ejecutar resultados anteriores en la bd indicators_data
 --!! ATENCIÓN!! se han detectado que alguna tabla de datos luego no existe. En este caso habrá que eliminar esa línea de los resultados anteriores y volver a lanzar el script. Las entradas antes del fallo se habrán ejecutado por lo que quitar y seguir a partir de ahí.
  --8.2) crear columna con valores geográficos de cada tabla
 ----8.2.1 obtener alter table con nueva columna
select ' ALTER TABLE ' || a.table_name  || ' ADD COLUMN ' || b.column_name  || ' varchar(100)' || ';' 
 from tb_datasets a, tb_dataset_dimensions b 
 where a.id = b.dataset_fk 
 and dimension_id = 'GEOGRAPHICAL';
  ----8.2.2 Ejecutar resultados anteriores en la bd indicators_data
 --!! ATENCIÓN!! se han detectado que alguna tabla de datos luego no existe. En este caso habrá que eliminar esa línea de los resultados anteriores y volver a lanzar el script. Las entradas antes del fallo se habrán ejecutado por lo que quitar y seguir a partir de ahí.

--8.3 Actualizar valores geográficos para el nuevo campo creado anteriormente a partir del campo antiguo. Dependiendo del nuevo de fuentes de datos puede tardar.
select ' UPDATE ' || a.table_name  || ' tn set ' || b.column_name  || ' = (select variable_element_code from temp_mig_codes_with_var_element t where t.code = tn.deprecated_' || b.column_name || ' limit 1)' || ';' 
 from tb_datasets a, tb_dataset_dimensions b
 where a.id = b.dataset_fk 
 and dimension_id = 'GEOGRAPHICAL';
  --!! ATENCIÓN!! se han detectado que alguna tabla de datos luego no existe. En este caso habrá que eliminar esa línea de los resultados anteriores y volver a lanzar el script. Las entradas antes del fallo se habrán ejecutado por lo que quitar y seguir a partir de ahí.
  
 --8.4 comprobar que todos los valores tienen correspondencia. No deben salir entradas. En caso contrario hay que estudiar los casos para asociar los códigos a elementos de variable.
select ' SELECT ''' ||  a.table_name || ''',' || b.column_name || ', deprecated_'  || b.column_name ||  ' from ' || a.table_name  || ' where ' || b.column_name || ' is null union all ' 
 from tb_datasets a, tb_dataset_dimensions b
 where a.id = b.dataset_fk 
 and dimension_id = 'GEOGRAPHICAL';
 
 --8.5 Para todo el resultado obtenido, quitar el último UNION ALL y sustituirlo por ";"
 --8.6 Ejecutar el resultado anterior.
 --!! ATENCIÓN!! se han detectado que alguna tabla de datos luego no existe. En este caso habrá que eliminar esa línea de los resultados anteriores y volver a lanzar la consulta. Las entradas antes del fallo se habrán ejecutado por lo que quitar y seguir a partir de ahí.
 --AYUDA. Si la consulta anterior devuelve resultados, se pueden sacar los valores distintos de esta manera
 -- 8.X.1. Ejecutar
 select ' SELECT deprecated_'  || b.column_name ||  ' valueD from ' || a.table_name  || ' where ' || b.column_name || ' is null union all ' 
 from tb_datasets a, tb_dataset_dimensions b
 where a.id = b.dataset_fk 
 and dimension_id = 'GEOGRAPHICAL';
 -- 8.x.2 quitar el último UNION ALL sin poner ";"
 -- 8.x.3 el resultado anterior ponerlo dentro de esta consulta
  select distinct(t.valueD) from (
    PONER AQUÍ consultas obtenidas del apartado anterior.
 ) t
 order by valueD;
 
 --!!PUEDE que alguna consulta de error porque la tabla no exista (se ha detectado alguna en desarrollo) En estos  casos quitar la consulta asociada al a la tabla que da error.
 
 -- Si todo va bien se obtienen los códigos de clasificación para los cuales no se encontró asociación con un código de elemento de variable. Hay que resolverlo con Vicky.
  
  