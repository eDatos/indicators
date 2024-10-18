-- --------------------------------------------------------------------------------------------------
-- EDATOS-4684 - Mejora del rendimiento de la recarga de indicadores al recibir mensaje de kafka sobre modificaciones de clasificaciones geográficas
-- 
-- Script para reducir el número de jobs que quartz puede ejecutar concurremente cuando se hacen con el planificador. Se reduce a 1 para evitar problemas de bloqueo indicados en la tarea.
-- --------------------------------------------------------------------------------------------------

update tb_data_configurations set conf_value = 1 where conf_key = 'org.quartz.threadPool.threadCount';

commit;