-- --------------------------------------------------------------------------------------------------
-- EDATOS-4185 - Añadir metadato elemento de tema a los indicadores
-- 
-- Script para realizar la migración de las áreas temáticas a elementos de tema provenientes del srm

-- Precondición. El equipo de consultoría ha creado los elementos de tema en el srm y los ha asociado al esquema de temas por defecto definido.
-- --------------------------------------------------------------------------------------------------

-- Paso 1 Se deben ejecutar, para cada entorno las consultas que generan los elementos de tema como external items a partir de las áreas temáticas existentes. Para ello ejecutar el script correspondiente para cada entorno:
-- * PRE / PRO - IBESTAT: /indicators/etc/helpers/migrar-areas-tematicas/2-PRE-PRO-IBESTAT/20231129_IBESTAT_script_2_convert_subject_code_to_category_element_external_item.sql
-- * PRE / PRO - IESTADIS: /indicators/etc/helpers/migrar-areas-tematicas/2-PRE-PRO-IESTADIS/20231129_PRE_IESTADIS_script_2_convert_subject_code_to_category_element_external_item.sql
-- * DEMO/ PRE / PRO - ISTAC: /indicators/etc/helpers/migrar-areas-tematicas/2-PRE-PRO-ISTAC/20231129_PRE_ISTAC_script_2_convert_subject_code_to_category_element_external_item.sql

-- Paso 2. Para cada entorno, ejecutar las sentencias obtenidas en el paso anterior.