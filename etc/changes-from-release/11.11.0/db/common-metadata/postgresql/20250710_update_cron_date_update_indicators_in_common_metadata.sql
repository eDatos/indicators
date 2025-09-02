-- ---------------------------------------------------------------------------------------------------
-- EDATOS-5104 Recargar los indicadores que tienen fuente GPE-Jaxi sólo cuando se hagan modificaciones sobre la misma

-- Variable de entorno para poner la tarea programada que actualiza JSONSTAT procedentes de sistema JAXI a frecuencia infinita.
-- Esta tarea programada no será usada ya que la actualización será llevada a cabo mediante kafka pero se mantendrá por si en un futuro otras organizaciones
-- quieren crear indicadores a partir de JSONSTAT sirva como base para su actualización.

-- ---------------------------------------------------------------------------------------------------

-- update job configuration to execute in 2099 year

update tb_data_configurations
set conf_value = '0 0 23 * * ? 2099'   
where conf_key = 'indicators.update.quartz.expression';
----------------------------------------------

--Actualmente la programación era conf_value = "0 0 23 * * ?" todos los días a las 23:00, sin límite de año.


commit;