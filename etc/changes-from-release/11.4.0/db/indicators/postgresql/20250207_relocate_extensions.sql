-- --------------------------------------------------------------------------------------------------
-- EDATOS-4804 - Mover las extensiones actualmente instaladas en los esquemas de bbdd al esquema de
-- extensiones
-- DEBE EJECUTARSE SIEMPRE después de los cambios introducidos en EDATOS-4658
-----------------------------------------------

-- DESARROLLO
-- BBDD: indicators_bd
-- extensiones: uuid-ossp

-- psql -U postgres -d indicators_data_bd
ALTER EXTENSION "uuid-ossp" SET SCHEMA extensions;

-----------------------------------------------

-- BBDD: indicators_data_bd
-- extensiones: pg_stat_statements

-- psql -U postgres -d indicators_data_bd
ALTER EXTENSION "pg_stat_statements" SET SCHEMA extensions;

-----------------------------------------------
-- DEMO
-- NADA

-----------------------------------------------
-- PRE IBESTAT
-- BBDD: indicators_bd
-- extensiones: uuid-ossp

-- psql -U postgres -d indicators_bd
ALTER EXTENSION "uuid-ossp" SET SCHEMA extensions;

-----------------------------------------------
-- PRO IBESTAT
-- BBDD: indicators_bd
-- extensiones: uuid-ossp

-- psql -U postgres -d indicators_bd
ALTER EXTENSION "uuid-ossp" SET SCHEMA extensions;

-----------------------------------------------
-- PRE IESTADIS
-- BBDD: indicators_bd
-- extensiones: uuid-ossp

-- psql -U postgres -d indicators_bd
ALTER EXTENSION "uuid-ossp" SET SCHEMA extensions;

-----------------------------------------------
-- PRO IESTADIS
-- BBDD: indicators_bd
-- extensiones: uuid-ossp

-- psql -U postgres -d indicators_bd
ALTER EXTENSION "uuid-ossp" SET SCHEMA extensions;

-----------------------------------------------
-- PRE ISTAC
-- NADA

-----------------------------------------------
-- PRO ISTAC
-- NADA