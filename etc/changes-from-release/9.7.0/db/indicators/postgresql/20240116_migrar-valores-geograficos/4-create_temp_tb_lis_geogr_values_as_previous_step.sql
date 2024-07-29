-- --------------------------------------------------------------------------------------------------
-- EDATOS-3827 - Integración con códigos geográficos de e-Semántica
-- 
-- Script para rellenar la tabla maestra de valores geográficos "tb_lis_geogr_values"

-- ATENCIÓN!! Dado el mal rendimiento de este script que en desarrollo tardó 8 horas para 134000 elementos, lo mejor es ejecutarlo días antes y tenerlo preparado.
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
-- 3. Seleccionar submenú Exportar desde consulta (Execute from query)
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

