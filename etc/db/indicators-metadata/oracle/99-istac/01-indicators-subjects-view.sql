-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- EDATOS-3406 - Migración de datos a Postgresql
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- Script para la creación de la vista de áreas en Oracle
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
  
  CREATE OR REPLACE FORCE VIEW "ISTAC_INDICADORES_META"."TV_AREAS_TEMATICAS" ("ID_AREA_TEMATICA", "DESCRIPCION") AS 
  SELECT ID_AREA_TEMATICA, DESCRIPCION
       FROM ( SELECT ID_AREA_TEMATICA,
                    ID_AREA_TEMATICA || ' ' || UPPER (DESCRIPCION)
                       AS DESCRIPCION
               FROM (  SELECT ID_AREA_TEMATICA,
                              DESCRIPCION
                         FROM ISTAC_JAXI2.TV_AREAS_TEMATICAS
                        WHERE     LENGTH (ID_AREA_TEMATICA) = 3
                              AND SUBSTR (ID_AREA_TEMATICA, 0, 3) <> '000')
             UNION
             SELECT    SUBSTR (ID_AREA_TEMATICA, 0, 2)
                    || SUBSTR (ID_AREA_TEMATICA, 6, 1)
                       AS COL,
                       SUBSTR (ID_AREA_TEMATICA, 0, 2)
                    || SUBSTR (ID_AREA_TEMATICA, 6, 1)
                    || ' '
                    || DESCRIPCION
                       AS DESCRIPCION
               FROM (  SELECT ID_AREA_TEMATICA,
                              DESCRIPCION
                         FROM ISTAC_JAXI2.TV_AREAS_TEMATICAS
                        WHERE     LENGTH (ID_AREA_TEMATICA) = 7
                              AND SUBSTR (ID_AREA_TEMATICA, 0, 3) <> '000'
                              AND SUBSTR (ID_AREA_TEMATICA, 0, 1) <> '9'))
   ORDER BY ID_AREA_TEMATICA ASC;
   
   commit;