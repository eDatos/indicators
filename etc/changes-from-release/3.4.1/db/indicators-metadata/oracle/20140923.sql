-- ------------------------------------------------------------------------
-- INDISTAC-877 - Ordenación y búsquedas en tablas
-- ------------------------------------------------------------------------

SET DEFINE OFF

ALTER TABLE TB_INDICATORS
ADD ( 
  PRODUCTION_PROC_STATUS VARCHAR2(255 CHAR),
  DIFFUSION_PROC_STATUS VARCHAR2(255 CHAR)
);

UPDATE TB_INDICATORS ind
SET PRODUCTION_PROC_STATUS = (
  SELECT ind_ver.PROC_STATUS
  FROM TB_INDICATORS_VERSIONS ind_ver
  WHERE ind_ver.ID = ind.PRODUCTION_ID AND ind_ver.VERSION_NUMBER = ind.PRODUCTION_VERSION_NUMBER
);

UPDATE TB_INDICATORS ind
SET ind.DIFFUSION_PROC_STATUS = (
  SELECT ind_ver.PROC_STATUS
  FROM TB_INDICATORS_VERSIONS ind_ver
  WHERE ind_ver.ID = ind.DIFFUSION_ID AND ind_ver.VERSION_NUMBER = ind.DIFFUSION_VERSION_NUMBER
);

SET DEFINE ON

commit;