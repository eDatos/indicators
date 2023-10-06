-- --------------------------------------------------------------------------------------------------
-- EDATOS-4197 - Añadir metadato unidad de medida que utilice un external item de de un código de clasificación del srm.

--PRECONDICIÓN: deben haberse lanzado el resto de scripts asociados a la tarea
-- --------------------------------------------------------------------------------------------------

-- PASO 1 Obtener los datos del srm.

select 'insert into temp_mig_units( CODE, CODE_NESTED, URI, URN, URN_PROVIDER, MANAGEMENT_APP_URL, VERSION, TYPE, label_es, label_ca, label_en) values('
|| '''' || co_detail.code || ''','
|| 'null,' 
|| ''''  || '/latest/codelists/' || o_detail.code || '/' || c.code || '/' || c.version_logic || '/codes/' || co_detail.code || ''','
|| '''' || co_detail.urn || ''','
|| '''' || co_detail.urn_provider || ''','
|| ''''  || '/#structuralResources/codelists/codelist;id=' || o_detail.code || ':' || c.code || '(' || c.version_logic || ')' || '/code;id=' || co_detail.code  || ''','
|| '0, '
|| '''structuralResources#code'','
|| '''' || (select "label" from tb_localised_strings co_title where co_title.international_string_fk = co_detail.name_fk and co_title.locale = 'es') || ''','
|| coalesce('''' || (select "label"  from tb_localised_strings co_title where co_title.international_string_fk = co_detail.name_fk and co_title.locale = 'ca') || '''', 'null') || ','
|| coalesce('''' || (select "label" from tb_localised_strings co_title where co_title.international_string_fk = co_detail.name_fk and co_title.locale = 'en') || '''', 'null') 
|| ');'
 from tb_item_schemes_versions a, tb_annotable_artefacts c, 
      tb_organisations o, tb_annotable_artefacts o_detail, tb_codelists_versions d, tb_codes co, tb_annotable_artefacts co_detail
 where  a.maintainable_artefact_fk = c.id
and c.public_logic = true 
and d.tb_item_schemes_versions = a.id
and c.latest_version_number_public is not null
and c.code = 'CL_UNIDADES_MEDIDA'
and c.version_logic = '01.002'
and o.id = c.maintainer_fk
and o.nameable_artefact_fk = o_detail.id
and o_detail.code = 'ISTAC'
and co.item_scheme_version_fk = a.id
and co_detail.id = co.nameable_artefact_fk
and co_detail.code in('PUESTOS_TRABAJO', 'KM', 'PORCENTAJE', 'M', 'PERSONAS', 'VOTOS', 'FINCAS');



--PASO 2 El resultado anterior copiarlo en la tabla que debe estar creada en INDICATORS denominada temp_mig_units
/*
Ejemplos:
insert into temp_mig_units( CODE, CODE_NESTED, URI, URN, URN_PROVIDER, MANAGEMENT_APP_URL, VERSION, TYPE, label_es, label_ca, label_en) values('FINCAS',null,'/latest/codelists/ISTAC/CL_UNIDADES_MEDIDA/01.004/codes/FINCAS','urn:sdmx:org.sdmx.infomodel.codelist.Code=ISTAC:CL_UNIDADES_MEDIDA(01.004).FINCAS','urn:sdmx:org.sdmx.infomodel.codelist.Code=ISTAC:CL_UNIDADES_MEDIDA(01.004).FINCAS','/#structuralResources/codelists/codelist;id=ISTAC:CL_UNIDADES_MEDIDA(01.004)/code;id=FINCAS',0, 'structuralResources#code','Fincas','null','null');
insert into temp_mig_units( CODE, CODE_NESTED, URI, URN, URN_PROVIDER, MANAGEMENT_APP_URL, VERSION, TYPE, label_es, label_ca, label_en) values('VOTOS',null,'/latest/codelists/ISTAC/CL_UNIDADES_MEDIDA/01.004/codes/VOTOS','urn:sdmx:org.sdmx.infomodel.codelist.Code=ISTAC:CL_UNIDADES_MEDIDA(01.004).VOTOS','urn:sdmx:org.sdmx.infomodel.codelist.Code=ISTAC:CL_UNIDADES_MEDIDA(01.004).VOTOS','/#structuralResources/codelists/codelist;id=ISTAC:CL_UNIDADES_MEDIDA(01.004)/code;id=VOTOS',0, 'structuralResources#code','Votos','null','null');
*/

--PASO 3 Asociar los códigos antiguos a los nuevos en temp_mig_units. Para ello asociarlo con esta consulta:
select 'UPDATE temp_mig_units set id_unit_tb_lis_quantities =' || q.id || ' where urn=''' || d.urn || ''';'
from tb_lis_quantities_units q, tb_localised_strings l, temp_mig_units d where q.title_fk = l.international_string_fk and l.locale = 'es'
and d."label_es" ilike l."label" || '%'   

-- PASO 4 ejecutar los UPDATE DEL PASO ANTERIOR EN LA BD INDICATORS.
/*
Ejemplos
UPDATE temp_mig_units set id_unit_tb_lis_quantities = 41 where urn=urn:sdmx:org.sdmx.infomodel.codelist.Code=ISTAC:CL_UNIDADES_MEDIDA(01.004).FINCAS;
UPDATE temp_mig_units set id_unit_tb_lis_quantities = 21 where urn=urn:sdmx:org.sdmx.infomodel.codelist.Code=ISTAC:CL_UNIDADES_MEDIDA(01.004).VOTOS;
*/

-- PASO 5 Asegurarse que todo está listo
select case when (select count(*) from tb_lis_quantities_units a where a.id not in(select coalesce(id_unit_tb_lis_quantities,0) from temp_mig_units)) = 0 then 'TODO CORRECTO. PUEDES SEGUIR ADELANTE.' else 'ERROR. HAY ENTRADAS QUE NO TIENEN RELACIÓN ENTRE UNIDAD ANTIGUA Y NUEVA' end 


-- PASO 6 Con todo lo anterior POR FIN! se puede empezar la migración

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

-- PASO 7 Ejecutar las sentencias generadas en el paso anterior para cada entrada en tb_quantities en la bd indicators Y 
 

-- PASO 8 Asegurarse que todo ha ido bien
select case when (select count(*) from tb_quantities where deprecated_unit_fk is not null and unit_fk is null) = 0 then 'PROCESO CORRECTO' else 'ERROR. HAY ENTRADAS QUE NO SE HAN MIGRADO' end 

-- PASO 9 HACER COMMIT;
commit;


