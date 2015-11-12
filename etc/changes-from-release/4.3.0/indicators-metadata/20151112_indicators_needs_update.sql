-- -------------------------------------------------------------------------------------------------
-- INDISTAC-950 - Los puntos que se muestran en Jaxi no se corresponden con las notas al pie
-- -------------------------------------------------------------------------------------------------

-- Se deben marcar todos los indicadores no archivados como que necesitan ser actualizados porque ahora
-- los indicadores almacenan código de puntos en lugar de sus correspondientes traducciones (literales).

UPDATE tb_indicators_versions
  SET NEEDS_UPDATE = 1
  WHERE PROC_STATUS != 'ARCHIVED';
  
commit;