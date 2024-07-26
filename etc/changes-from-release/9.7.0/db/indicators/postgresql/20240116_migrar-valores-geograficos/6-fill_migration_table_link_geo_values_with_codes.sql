-- --------------------------------------------------------------------------------------------------
-- EDATOS-3827 - Integración con códigos geográficos de e-Semántica
-- 
-- Script para migrar la relación entre códigos de los codelists indicados y sus elementos de variable asociados.
-- --------------------------------------------------------------------------------------------------

-- PRECONDICIÓN: Asegurarse de que las tablas de migración están creadas en las bases de datos indicadas en cada uno de los scripts: 1-create_migration_table_geographical_values.sql y 2-create_migration_table_link_geo_values_with_codes.sql 

-- 1 Ejecutar la siguiente consulta en la bd del SRM para los codelists indicados
 insert into  temp_mig_codes_with_var_element(urn_codelist, code, variable_element_code) 
 select 
 c.urn, 
 t_codes.code,
 t_ve.code 
 from tb_item_schemes_versions a, tb_annotable_artefacts c, tb_codelists_versions d, tb_m_codelists_versions g, 
      tb_codes e , tb_annotable_artefacts t_codes, tb_m_codes f, 
      tb_m_variable_elements ve, tb_annotable_artefacts t_ve, tb_m_variables v, tb_annotable_artefacts t_v
 where  a.maintainable_artefact_fk = c.id
and d.tb_item_schemes_versions = a.id
and g.tb_codelists_versions  = d.tb_item_schemes_versions
and g.variable_fk = v.id 
and v.nameable_artefact_fk  = t_v.id
and v.variable_type = 'GEOGRAPHICAL'
and t_v.code = 'VR_TERRITORIO'
and e.item_scheme_version_fk = d.tb_item_schemes_versions
and f.tb_codes  = e.id
and e.nameable_artefact_fk = t_codes.id 
and ve.id = f.variable_element_fk 
and ve.identifiable_artefact_fk = t_ve.id; 
/* Si a posteriori se ve la necesidad de obtener algún codelist que no se añadió, descomentar el siguiente filtro añadiendo la urn de dicho codelist a exportar.
and c.urn IN
 ('urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ISTAC:CL_AREA_ES70_DS_20190526(01.001)',
  XXX -- clasificación nor normalizada con los valores del GPE y jsonstat
 )*/ 
 
 --1.2 Exportar la tabla a CSV.
 --ATENCIÓN!! Al exportar si hay valores nulos los convierte a vacío. Lo que puede dar problemas en pasos posteriores. 
--Es por eso que hay que indicar en la exportación que convierta los valores nulos al valor "null" Para ello:
--  Exportar a CSV
--  En la última pantalla de exportación, en "Exporting settings" al valor "NULL String" asignarle el valor null
 
--1.3 Importar la tabla anterior a la tabla "temp_mig_codes_with_var_element" en indicators.
--ATENCIÓN!! Al importar si hay valores nulos los convierte a vacío. En la exportación se ha puesto "null" como valor asociado a nulo hay que asociarlo a las opciones de importación 
--Es por eso que hay que indicar en la importación que convierta los valores con el valor "null" a NULO Para ello:
--  Importar a CSV
--  En la última pantalla de importación, en "Importing settings" al valor "NULL value mark" asignarle el valor null
--1.4 Importar la tabla anterior a la tabla "temp_mig_codes_with_var_element" en indicators_data.
--1.5 Borrar tabla temp_mig_codes_with_var_element de la base de datos del srm. Situarse en la base de datos metamac_structural_resources y hacer:
drop table temp_mig_codes_with_var_element;


-- 2 Comprobación de duplicados. 
/*
En migración de pre-ibestat se comprobó que la clasificación para gpe y jsonstat tenía códigos que apuntaban al mismo elemento de variable. 
Y luego había indicadores que utilizaban ambos códigos que apuntaban al mismo elemento de variable. ésto da error porque se queda el indicador con dos elementos iguales.
Al recuperar el índice único al final del proceso realizado en el fichero 7_populate_geographical_values.sql no se pueden volver a poner los índices de clave única por este motivo

Aunque es más probable que el error se de con la clasificación del gpe y jsonstat CL_NN_VALORES_GEOGRAFICOS al ser códigos no normalizados, la siguiente consulta comprueba cualquier indicador.

Es por ello que se debe lanzar la siguiente consulta, para averiguar si van a haber duplicados. Esta consulta no debe devolver valores. Si los devuelve hay que resolver los conflictos.

*/
select i1.indicator_version_fk, ti.code as indicator_code, tls."label" as indicator_title , c1.code as duplicateCode1, c2.code as duplicateCode2, 
       (select variable_element_code from temp_mig_codes_with_var_element m where m.code = c1.code limit 1) 
from tb_ind_version_geo_cov i1, tb_ind_version_geo_cov i2, tb_lis_geogr_values c1,  tb_lis_geogr_values c2, tb_indicators_versions tiv, tb_indicators ti, tb_localised_strings tls 
where i1.indicator_version_fk = i2.indicator_version_fk 
  and i1.id != i2.id 
  and i1.deprecated_geographical_value_fk != i2.deprecated_geographical_value_fk   
  and c1.id = i1.deprecated_geographical_value_fk
  and c2.id = i2.deprecated_geographical_value_fk
  and i1.indicator_version_fk = tiv.id
  and tls.international_string_fk = tiv.title_fk 
    and tls.locale = 'es'
  and ti.id = tiv.indicator_fk 
  and (select variable_element_code from temp_mig_codes_with_var_element m where m.code = c1.code limit 1) =
  (select variable_element_code from temp_mig_codes_with_var_element m2 where m2.code = c2.code  limit 1)
  order by indicator_version_fk; 
    
  
  




 
 