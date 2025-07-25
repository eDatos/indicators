 -- --------------------------------------------------------------------------------------------------
-- EDATOS-5104 Recargar los indicadores que tienen fuente GPE-Jaxi sólo cuando se hagan modificaciones sobre la misma
-- 
-- Script que añade el contenido para la nueva columna "resource_id" en la tabla tb_data_sources
-- --------------------------------------------------------------------------------------------------

--!!ATENCIÓN Para ejecutar esta  sentencia primero se han de ejecutar los scripts de base de datos. En especial el script que crea la columna "RESOURCE_ID"
 
 
/*
Extraer de URLs de este tipo "http://www.ibestat.com/ibestat/service/ibestat/pxcontent/c5fdac4b-7858-4016-8a15-1cecd64a86a9/I208013_m010.px" 
la última parte que es el DATASETID. Resultado será -> I208013_m010

- string_to_array(query_urn, '/'): divide la URL por / en un array.
- array_length(..., 1): obtiene la cantidad total de partes (índice del último elemento).
- split_part(query_urn, '/', index): obtiene la última parte del path (el archivo .px).
- regexp_replace(..., '\.px$', ''): elimina .px del final.
*/ 
 update tb_data_sources
 set resource_id = regexp_replace(split_part(query_urn, '/', array_length(string_to_array(query_urn, '/'), 1)),'\.px$','')
 where query_environment= 'JSON_STAT';
 
 
 -- Actualiza el campo resource_id con la parte del query_urn que va después del último ":"   
 -- regexp_replace(query_urn, '^.*:', ''): esta expresión regular elimina todo hasta el último : (gracias a .* que es greedy).
 -- Ej: urn:siemac:org.siemac.metamac.infomodel.statisticalresources.Dataset=ISTAC:C00010A_000025 se queda con C00010A_000025 
UPDATE tb_data_sources
SET resource_id = regexp_replace(query_urn, '^.*:', '')
where query_environment= 'METAMAC';
 
 commit;
  
