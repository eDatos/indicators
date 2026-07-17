-- ------------------------------------------------------------------------------------------------------------------------------
-- EDATOS-5725 - Ordenación de dimensión territorio en la salida de la api
--
-- Ejecutar DESPUÉS de que la aplicación haya recibido al menos un mensaje Kafka
-- del codelist de granularidades (para que GRANULARITY_ORDER esté relleno).
--
-- Al recibir el mensaje Kafka, la aplicación actualiza automáticamente tanto
-- GRANULARITY_ORDER (en TB_LIS_GEOGR_GRANULARITIES) como GLOBAL_ORDER
-- (en TB_LIS_GEOGR_VALUES) vía GeographicalValueRepository.updateGlobalOrderByGranularity.
-- No se requiere ningún UPDATE manual adicional.
-- ------------------------------------------------------------------------------------------------------------------------------

-- Paso 1. Recuperar la clasificación asociada a las granularidades geográficas indicada en la constante "metamac.default.codelist.geographical_granularity.urn"

-- Paso 2. Ir a SRM y asegurarse de que el equipo de auditoría ha creado el orden de visualización "CUSTOM" configurado en el script
--         /indicators/etc/changes-from-release/12.x.x/db/common-metadata/postgresql/20260607_insert_table_tb_data_configurations.sql

-- Paso 3. Reenviar el mensaje Kafka de la clasificación obtenida en el paso 1. Para ello ir a la app interna del SRM y reenviar el mensaje.
-- OJO! En este punto se requiere que la release esté desplegada, ya que la recepción del mensaje Kafka rellenará el nuevo campo GRANULARITY_ORDER
--      en TB_LIS_GEOGR_GRANULARITIES y actualizará GLOBAL_ORDER en TB_LIS_GEOGR_VALUES.

-- A partir de aquí todos los pasos se ejecutarán en la base de datos de indicadores INDICATORS_BD.

-- Paso 4. Verificar que TB_LIS_GEOGR_GRANULARITIES tiene GRANULARITY_ORDER relleno para todas las granularidades.
-- Todas las filas deben tener GRANULARITY_ORDER no nulo. En bd indicadores:
SELECT code, granularity_order FROM TB_LIS_GEOGR_GRANULARITIES ORDER BY granularity_order;

-- Paso 5. Verificar que no ha quedado ningún valor geográfico sin GLOBAL_ORDER de granularidad. No debe devolver nada. En bd indicadores:
SELECT gg.code AS granularity, gg.granularity_order, gv.code, gv.global_order
  FROM TB_LIS_GEOGR_VALUES gv
  JOIN TB_LIS_GEOGR_GRANULARITIES gg ON gv.granularity_fk = gg.id
 WHERE gg.granularity_order IS NULL;
