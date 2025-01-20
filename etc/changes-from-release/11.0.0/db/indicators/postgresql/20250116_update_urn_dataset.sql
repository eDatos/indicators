-- --------------------------------------------------------------------------------------------------
-- [EDATOS-4787] Permitir enlazar la última versión del dataset al usarlo como fuente de datos en un indicador

-- Script para actualizar la urn de los indicadores con fuente de datos "DATASET"  para que no tengan la versión. Siempre se deberá consultar por la última versión del dataset por lo que no se debe grabar la versión que está en el momento de la creación.
-- --------------------------------------------------------------------------------------------------

-- Actualizar los campos query_uuid, y query_urn para el tipo de fuente 'DATASET'  quitándole la versión (todo lo que esté del primer paréntesis en adelante)

update tb_data_sources 
set query_uuid = split_part(query_uuid , '(', 1),
query_urn = split_part(query_urn , '(', 1)
where metamac_type = 'DATASET'
and (query_uuid like '%(%'
or query_urn like '%(%');

commit;

--Comprobar que todos las fuentes han quedado sin versión:
---- select query_uuid, query_urn from tb_data_sources tds where metamac_type = 'DATASET';
