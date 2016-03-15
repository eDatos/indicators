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

UPDATE TB_INDICATORS ind2
SET ind2.PRODUCTION_PROC_STATUS = (
  SELECT case when ind_ver_prod.PROC_STATUS is null then ind_ver_diff.PROC_STATUS else ind_ver_prod.PROC_STATUS end
  from TB_INDICATORS ind
  left join TB_INDICATORS_VERSIONS ind_ver_diff on ind.diffusion_id = ind_ver_diff.ID AND  ind.diffusion_version_number = ind_ver_diff.version_number
  left join TB_INDICATORS_VERSIONS ind_ver_prod on ind.PRODUCTION_ID = ind_ver_prod.ID and ind.production_version_number = ind_ver_prod.version_number
  WHERE ind2.id = ind.id
);

SET DEFINE ON

commit;