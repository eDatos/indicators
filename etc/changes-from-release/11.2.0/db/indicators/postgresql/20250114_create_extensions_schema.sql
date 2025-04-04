-- --------------------------------------------------------------------------------------------------
-- EDATOS-4658 - Búsquedas sin tener en cuenta mayúsculas, minúsculas y tildes
-- Unaccent instalation: USE POSTGRE USER to have the needed privileges
-- The extension may be created in a schema that is included in the search_path. This is the recommended
-- way to create the extension, if 'unaccent' is also needed in other schemas. An example to create a schema
-- to store the required extensions is shown below


------------------------------------------------------------------------------------------------------------------------
-- DESARROLLO, DEMO, PRE & PRO ISTAC, IESTADIS E IBESTAT
------------------------------------------------------------------------------------------------------------------------
-- Ejecutar el siguiente script desde fichero:
-- psql -U postgres -d indicators_bd --set ON_ERROR_STOP=1 -f ./script_indicators_bd.sql

-- CREATE SCHEMA extensions;

-- Extension 'unaccent' needs to be created, a superuser or user with create privilege should execute the following:
-- (unaccent is a trusted extension)
-- CREATE extension unaccent WITH SCHEMA extensions;
-- Make sure indicators_bd and indicators_own_bd can use everything in the extensions schema
-- GRANT USAGE ON SCHEMA extensions TO public;
-- GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA extensions TO indicators_bd, indicators_own_bd;

-- Include future extensions
-- ALTER DEFAULT PRIVILEGES IN SCHEMA extensions
-- GRANT EXECUTE ON FUNCTIONS TO indicators_bd, indicators_own_bd;

-- ALTER DEFAULT PRIVILEGES IN SCHEMA extensions
-- GRANT USAGE ON TYPES TO indicators_bd, indicators_own_bd;

-- DO $$
-- DECLARE
-- current_search_path TEXT;
-- BEGIN
    -- Obtén el search_path actual
-- SELECT current_setting('search_path') INTO current_search_path;

-- Genera el nuevo search_path y actualiza la base de datos
-- EXECUTE format('ALTER DATABASE "indicators_bd" SET search_path = %s, extensions', current_search_path
--         );
-- END $$;


-- Make sure indicators_data_bd and indicators_data_own_bd can use everything in the extensions schema
-- GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA extensions TO indicators_data_bd, indicators_data_own_bd;

-- Include future extensions
-- ALTER DEFAULT PRIVILEGES IN SCHEMA extensions
-- GRANT EXECUTE ON FUNCTIONS TO indicators_data_bd, indicators_data_own_bd;

-- ALTER DEFAULT PRIVILEGES IN SCHEMA extensions
-- GRANT USAGE ON TYPES TO indicators_data_bd, indicators_data_own_bd;

-- DO $$
-- DECLARE
-- current_search_path TEXT;
-- BEGIN

-- Obtén el search_path actual
-- SELECT current_setting('search_path') INTO current_search_path;

-- Genera el nuevo search_path y actualiza la base de datos
-- EXECUTE format('ALTER DATABASE "indicators_data_bd" SET search_path = %s, extensions', current_search_path
--         );
-- END $$;
