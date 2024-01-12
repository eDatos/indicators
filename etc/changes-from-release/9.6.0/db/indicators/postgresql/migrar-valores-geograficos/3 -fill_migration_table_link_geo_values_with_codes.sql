-- --------------------------------------------------------------------------------------------------
-- EDATOS-3827 - Integración con códigos geográficos de e-Semántica
-- 
-- Script para migrar la relación entre códigos de los codelists indicados y sus elementos de variable asociados.
-- --------------------------------------------------------------------------------------------------

-- 1 Asegurarse de que las tablas de migración están creadas: 1-create_migration_table_geographical_values.sql y 2-create_migration_table_link_geo_values_with_codes.sql en la bd indicators
-- El script 2-create_migration_table_link_geo_values_with_codes.sql se debe ejecutar también la bd indicators_data creando la tabla "temp_mig_codes_with_var_element"

-- 2 Ejecutar la siguiente consulta en la bd del SRM para los codelists indicados

  select ' INSERT INTO temp_mig_codes_with_var_element(urn_codelist, code, variable_element_code) VALUES('
|| '''' || c.urn || ''','
|| '''' || t_codes.code || ''',' 
|| '''' || t_ve.code || ''');' 
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
 
 --3. Volcar resultados obtenidos en apartado anterior.
 -- 3.1)El resultado anterior volcarlo sobre la base de datos indicators. Puede tardar tiempo.
 -- 3.2)El resultado anterior volcarlo sobre la base de datos indicators_data. Puede tardar tiempo. En pruebas se detecta que es más rápido exportar la tabla creada en el apartado anterior en tb_indicators a CSV y luego importarla en esta base de datos.