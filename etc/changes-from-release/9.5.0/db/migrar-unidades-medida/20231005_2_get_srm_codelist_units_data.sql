-- --------------------------------------------------------------------------------------------------
-- EDATOS-4197 - Añadir metadato unidad de medida que utilice un external item de de un código de clasificación del srm.

--PRECONDICIÓN: deben haberse lanzado el resto de scripts asociados a la tarea
-- --------------------------------------------------------------------------------------------------

-- PASO 0 Ejecutar script en bd indicators que  crea tabla de migración "20231005_1_create_migration_tables.sql"

-- PASO 1 Ejecutar los pasos del script "/indicators/etc/helpers/migrar-unidades-medidas/20231005_2_precarga_demo_get_srm_codelist_units_data.sql" que cargarán
-- en la tabla temporal temp_mig_units los datos necesarios para la migración.

-- PASO 2 Con todo lo anterior se puede empezar la migración

select
'
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);' ||
case when t.label_es is not null then '  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || t.label_es || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);' else '' end ||
case when t.label_ca is not null then '
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || t.label_ca || ''', ''ca'', currval(''SEQ_I18NSTRS''), 1);' else '' end || 
case when t.label_en is not null then '
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || t.label_en || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);' else '' end || '
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''),'
|| '''' || t.CODE || ''','
|| '''' || t.URI|| ''',' 
|| '''' || t.URN || ''','
|| '''' || t.MANAGEMENT_APP_URL || ''','
|| '''' || t.VERSION || ''','
|| 'currval(''SEQ_I18NSTRS'')' || ','
|| '''' || t.TYPE || ''');
UPDATE TB_QUANTITIES SET unit_fk= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';
'
from TB_QUANTITIES q, temp_mig_units t  where deprecated_unit_fk is not null
and q.deprecated_unit_fk = t.id_unit_tb_lis_quantities;

-- PASO 3 Ejecutar las sentencias generadas en el paso anterior para cada entrada en tb_quantities en la bd indicators

-- PASO 4 Asegurarse que todo ha ido bien
select case when (select count(*) from tb_quantities where deprecated_unit_fk is not null and unit_fk is null) = 0 then 'PROCESO CORRECTO' else 'ERROR. HAY ENTRADAS QUE NO SE HAN MIGRADO' end 

-- PASO 5 HACER COMMIT;
commit;

-- PASO 6 Borrar tabla temporal
DROP TABLE temp_mig_units;
