-- ------------------------------------------------------------------------
-- NDISTAC-953 - Exportación a XLS de los resultados de la búsqueda de indicadores
-- ------------------------------------------------------------------------

SET DEFINE OFF

UPDATE TB_INDICATORS ind
SET ind.DIFFUSION_PROC_STATUS = (
  SELECT ind_ver.PROC_STATUS
  FROM TB_INDICATORS_VERSIONS ind_ver
  WHERE ind_ver.ID = ind.DIFFUSION_ID AND ind_ver.VERSION_NUMBER = ind.DIFFUSION_VERSION_NUMBER
);

UPDATE TB_INDICATORS ind
SET ind.PRODUCTION_PROC_STATUS = (
  SELECT ind_ver.PROC_STATUS
  FROM TB_INDICATORS_VERSIONS ind_ver
  WHERE ind_ver.ID = ind.PRODUCTION_ID AND ind_ver.VERSION_NUMBER = ind.PRODUCTION_VERSION_NUMBER
);

SET DEFINE ON

commit;