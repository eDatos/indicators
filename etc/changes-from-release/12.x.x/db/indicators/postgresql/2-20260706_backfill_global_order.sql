-- ------------------------------------------------------------------------------------------------------------------------------
-- EDATOS-5725 - Ordenación de dimensión territorio en la salida de la api
-- 
-- Ejecutar DESPUÉS de que la aplicación haya recibido al menos un mensaje Kafka                                                                                                                                                          │
-- del codelist de granularidades (para que GRANULARITY_ORDER esté relleno)
-- ------------------------------------------------------------------------------------------------------------------------------

-- Paso 1. Recuperar  clasficación asociada las granularidades geográficas indicada en constante "metamac.default.codelist.geographical_granularity.urn"

-- Paso 2. Ir a SRM y asegurarse que equipo de auditoría a creado orden de visualización "CUSTOM" configurado en script /indicators/etc/changes-from-release/12.x.x/db/common-metadata/postgresql/20260607_insert_table_tb_data_configurations.sql

-- Paso 3. Reenviar mensaje de kafka de esa clasificación obtenida en paso 1. Para ello ir a la app interna del SRM y reenviar mensaje. 
-- OJO! En este punto se requiere que la release esté desplegada ya que la recepción del mensaje de kafka rellenará un nuevo campo adicional GRANULARITY_ORDER de la tabla  "TB_LIS_GEOGR_GRANULARITIES"

-- A partir de aquí todos los pasos se ejecutarán en la base de datos de indicadores INDICATORS_BD.

-- Paso 4. Asegurarse que se ha actualizado la tabla  TB_LIS_GEOGR_GRANULARITIES y el nuevo campo GRANULARITY_ORDER tiene valor para todas las granularidades.
-- Todas las filas deben tener GRANULARITY_ORDER no nulo. Si alguna sale NULL, repetir el paso 3. En bd indicadores:
SELECT code, granularity_order FROM TB_LIS_GEOGR_GRANULARITIES ORDER BY granularity_order;


-- Paso 5. Ejecutar el siguiente script de adecuación de datos. En bd indicadores::
WITH granularity_orders AS (
    SELECT id, granularity_order
    FROM   TB_LIS_GEOGR_GRANULARITIES
    WHERE  granularity_order IS NOT NULL
)
UPDATE TB_LIS_GEOGR_VALUES
SET    GLOBAL_ORDER = LPAD(CAST(g.granularity_order AS TEXT), 5, '0') || '_' || CODE,
       VERSION      = VERSION + 1
FROM   granularity_orders g
WHERE  GRANULARITY_FK = g.id;
																								  
-- Paso 6. Comprobar que no ha quedado ningún valor geográfico sin orden de granularidad. No debe devolver nada. En bd indicadores:
SELECT gg.code AS granularity, gg.granularity_order, gv.code, gv.global_order
  FROM TB_LIS_GEOGR_VALUES gv
  JOIN TB_LIS_GEOGR_GRANULARITIES gg ON gv.granularity_fk = gg.id
  where granularity_order is null;

commit;
