-- --------------------------------------------------------------------------------------------------
-- EDATOS-5034 - Número de caracteres admitidos como contenido en una dimensión
-- 
-- Ampliar a 255 caracteres el id de dimensiones y atributos así.
-- Ampliar a 4000 caracteres el contenido de los atributos.

-- --------------------------------------------------------------------------------------------------
  
  
--Los siguientes pasos se deberán realizar en las siguientes bases de datos:
----1) statistical_resources_data
----2) indicators_data

--1) Resguardo de todas las vistas
--1.1) Obtener la definición de cada una de las vistas que empiecen por "dv_"
SELECT 'CREATE OR REPLACE VIEW ' || quote_ident(viewname) || ' AS ' || definition AS create_sql
FROM pg_views
WHERE viewname IN (
 SELECT distinct v.viewname FROM pg_views v where v.viewname like 'dv_%'
);

--1.2) El resultado anterior guardarlo en un fichero resguardoVistasData.sql

--2) Borrado de todas las vistas
--2.1) Obtener script de borrado de vistas que empiezan por "dv_"
SELECT 'DROP VIEW IF EXISTS ' || quote_ident(viewname) || ' CASCADE;'
FROM pg_views
WHERE viewname IN (
 SELECT distinct v.viewname FROM pg_views v where v.viewname like 'dv_%'
);

--2.2) Ejecutar las sentencias obtenidas en el apartado anterior.


--3)  Obtener un alter table para cada columna del tipo dimensión en cada tabla de datos y cambiar el tamaño a 255 caracteres.

3.1) Ejecutar la siguiente consulta que generará sentencias ALTER TABLE para las columnas del tipo dimensión
select ' ALTER TABLE ' || a.table_name  || ' ALTER COLUMN ' || b.column_name  || ' TYPE VARCHAR(255);'   
 from tb_datasets a, tb_dataset_dimensions b 
 where a.id = b.dataset_fk 
  and table_name in (select upper(table_name) from INFORMATION_SCHEMA.COLUMNS);

--3.2) Ejecutar las sentencias ALTER TABLE obtenidas en el apartado anterior.

--4)  Obtener un alter table para cada columna del tipo atributo en cada tabla de datos y cambiar el tamaño a 255 caracteres.
--4.1) Ejecutar la siguiente consulta que generará sentencias ALTER TABLE para las columnas del tipo atributo
select ' ALTER TABLE ' || a.table_name  || ' ALTER COLUMN ' || b.column_name  || ' TYPE VARCHAR(255);'    
 from tb_datasets a, tb_dataset_attributes b 
 where a.id = b.dataset_fk 
  and table_name in (select upper(table_name) from INFORMATION_SCHEMA.COLUMNS);

--4.2) Ejecutar las sentencias ALTER TABLE obtenidas en el apartado anterior.

--5)  Obtener un alter table para cada columna con el valor del atributo atributo en cada tabla de datos y cambiar el tamaño a 4000 caracteres.
--5.1) Ejecutar la siguiente consulta que generará sentencias ALTER TABLE para las columnas del tipo atributo
select ' ALTER TABLE ' || a.table_name  || ' ALTER COLUMN ' || b.column_name || '_es'  || ' TYPE VARCHAR(4000);'    
 from tb_datasets a, tb_dataset_attributes b 
 where a.id = b.dataset_fk 
  and table_name in (select upper(table_name) from INFORMATION_SCHEMA.COLUMNS);

--5.2) Ejecutar las sentencias ALTER TABLE obtenidas en el apartado anterior.


--6) Regenerar las vistas borradas con el script donde se han guardado resguardoVistasData.sql

--7) Comprobar que ya no quedan columnas ni del tipo dimensión ni del tipo atributo. Para ello ejecutar la consulta del ANEXO apartado a1) y comprobar que no devuelve ningún resultado.
--OJO!! Se han detectado tablas en desarrollo que aparecen en esta consulta pero es porque no tienen dataset asociado en tb_datasets. En este caso no es problema y no será error.
-- Para detectarlo, comprobar con la consulta indicada en a1.1) del anexo que debe devolver cero resultados indicando que esas tablas no están asociadas a nada.


------ANEXO-----------

--a1) Consulta para comprobar que no quedan tablas que tengan algún campo "dimension_" o "attribute_" con 100 caracteres y ningún valor de atributo (que termine en _es) con 500 caracteres.

select distinct table_name from(
select distinct table_name
FROM 
    information_schema.columns
WHERE 
    (column_name LIKE 'dimension\_%' ESCAPE '\'
    or  column_name LIKE 'attribute\_%' ESCAPE '\')
    
    AND data_type = 'character varying'
    AND character_maximum_length = 100
union ALL
select distinct table_name
FROM 
    information_schema.columns
WHERE 
    (column_name LIKE 'dimension\_%' ESCAPE '\'
    or  column_name LIKE 'attribute\_%_es' ESCAPE '\')
    
    AND data_type = 'character varying'
    AND character_maximum_length = 500
   ) a
ORDER BY table_name;

        
--a1.1) buscar el dataset de las tablas deseadas
select 'select * from tb_datasets where dataset_id in(' || string_agg('''' || table_name || ''''::text, ',') || ');' from(
select distinct table_name
FROM 
    information_schema.columns
WHERE 
    (column_name LIKE 'dimension\_%' ESCAPE '\'
    or  column_name LIKE 'attribute\_%' ESCAPE '\')
    
    AND data_type = 'character varying'
    AND character_maximum_length = 100
union ALL
select distinct table_name
FROM 
    information_schema.columns
WHERE 
    (column_name LIKE 'dimension\_%' ESCAPE '\'
    or  column_name LIKE 'attribute\_%_es' ESCAPE '\')
    
    AND data_type = 'character varying'
    AND character_maximum_length = 500
   ) a



--a2) Consulta más específica por si surge algún problema.

SELECT
    table_schema,
    table_name,
    column_name
FROM 
    information_schema.columns
WHERE 
    (column_name LIKE 'dimension\_%' ESCAPE '\'
    or  column_name LIKE 'attribute\_%' ESCAPE '\')
    
    AND data_type = 'character varying'
    AND character_maximum_length = 100
ORDER BY 
    table_schema, table_name, column_name;
    

--a3) otras consulta interesante si se quiere saber el tamaño de las columnas de una tabla concreta
SELECT
    table_schema,
    table_name,
    column_name,
    character_maximum_length
FROM 
    information_schema.columns
WHERE 
    (column_name LIKE 'dimension\_%' ESCAPE '\'
    or  column_name LIKE 'attribute\_%' ESCAPE '\')
    
    AND data_type = 'character varying'
    and upper(table_name) = 'DATA_FF1I864I67FQPG0IC7EL1G8'
ORDER BY 
    table_schema, table_name, column_name;	


--a4) vista asociada a una tabla concreta y obtener el código de la vista
SELECT DISTINCT v.viewname, v.definition
FROM pg_views v
JOIN pg_depend d ON d.objid = (
    SELECT oid FROM pg_class WHERE relname = v.viewname LIMIT 1
)
JOIN pg_class t ON d.refobjid = t.oid
WHERE t.relname = 'data_e30103a_000004_001000';
----
SELECT 'CREATE OR REPLACE VIEW ' || quote_ident(viewname) || ' AS ' || definition AS create_sql
FROM pg_views
WHERE viewname IN (
  -- lista aquí las vistas encontradas en el paso anterior
  'dv_e30103a_000004' -- por ejemplo
);
