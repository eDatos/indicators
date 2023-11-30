-- --------------------------------------------------------------------------------------------------
-- EDATOS-4297 - Registros pendientes en tb_tasks de indicadores
-- 
--  Se eliminan todos los registros de tb_tasks
-- --------------------------------------------------------------------------------------------------

delete from tb_tasks where job like 'exports_dspl_job%';
commit;