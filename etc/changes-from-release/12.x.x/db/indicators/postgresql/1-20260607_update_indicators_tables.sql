-- ------------------------------------------------------------------------------------------------------------------------------
-- EDATOS-5725 - Ordenación de dimensión territorio en la salida de la api
-- 
--  Añadir campo con ordenación de granularidades según el orden de visualización por defecto que se recoge en constante "indicators.geographical_granularity.default_visualisation_order"
-- ------------------------------------------------------------------------------------------------------------------------------


ALTER TABLE TB_LIS_GEOGR_GRANULARITIES ADD COLUMN GRANULARITY_ORDER INTEGER;

commit;
