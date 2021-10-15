-- --------------------------------------------------------------------------------------------------
-- EDATOS-3406 - Migración de datos a Postgresql
-- --------------------------------------------------------------------------------------------------
-- Este script está destinado a actualizar el campo query_text de las fuentes de datos provenientes
-- del GPE con valor nombre_consulta de la vista TV_CONSULTA. Para ello es necesario realizar los 
-- siguientes pasos:
-- 1. Determinar el número de registros a actualizar con el sql CONSULTA 1.
-- 2. Determinar el número de registros a actualizar cuya fuente de datos existe en la vista TV_CONSULTA con el sql CONSULTA 2.
-- 3. Determinar el número de registros a actualizar cuya fuente de datos NO existe en la vista TV_CONSULTA con el sql CONSULTA 3.
-- 4. Si la suma de los registros afectados de los pasos 2 y 3 coincide con los del paso 1, continuar con la ejecución de los
--    siguientes pasos. En caso contrario determinar el motivo de la discrepancia en el número de registros y actuar en consecuencia.
-- 5. Generar las sentencias sql update de los registros a actualizar del paso 2 con el sql CONSULTA 4 y copiar los resultados a un fichero de texto.
-- 6. Generar las sentencias sql update de los registros a actualizar del paso 3 con el sql CONSULTA 5 y copiar los resultados a un fichero de texto.
-- 7. Ejecutar la sentencias sql update generadas en los pasos 5 y 6 en una nueva hoja de trabajo añadiendo la sentencia commit;
--    al final de la misma.    

-- CONSULTA 1
select count(1) from tb_data_sources where query_environment = 'GPE';

-- CONSULTA 2
select count(1) from tb_data_sources tds, tv_consulta tc where tds.query_environment = 'GPE' and tds.query_uuid = tc.uuid_consulta;

-- CONSULTA 3
select count(1) from tb_data_sources tds where tds.query_environment = 'GPE' and tds.query_uuid not in (select uuid_consulta from tv_consulta);

-- CONSULTA 4
select 'update tb_data_sources set query_text = ''' || tc.nombre_consulta || ''' where id = ' || tds.id || ';' from tb_data_sources tds, tv_consulta tc where tds.query_environment = 'GPE' and tds.query_uuid = tc.uuid_consulta;

-- CONSULTA 5
select 'update tb_data_sources set query_text = ''Consulta no encontrada'' where id = ' || tds.id || ';' from tb_data_sources tds where tds.query_environment = 'GPE' and tds.query_uuid not in (select uuid_consulta from tv_consulta);

