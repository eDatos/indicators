 -- --------------------------------------------------------------------------------------------------
-- EDATOS-5104 Recargar los indicadores que tienen fuente GPE-Jaxi sólo cuando se hagan modificaciones sobre la misma
-- 
-- Script que añade columna para copiar el id del dataset en el caso de dataset que provienen de jaxi (jsonstat)
-- Se crea índice ya que al llegar un mensaje de kafka de jaxi se deberán buscar los indicadores que tengan el RESOURCE_ID que venga en el mensaje
-- --------------------------------------------------------------------------------------------------

 
 ALTER TABLE TB_DATA_SOURCES ADD COLUMN RESOURCE_ID varchar(4000);
 
 CREATE INDEX IX_TB_DATA_SOURCES_RESOURCE_ID ON TB_DATA_SOURCES(RESOURCE_ID);

 
 commit;
  
