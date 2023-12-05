-- --------------------------------------------------------------------------------------------------
-- EDATOS-4197 - Añadir metadato unidad de medida que utilice un external item de de un código de clasificación del srm.

--PRECONDICIÓN: deben haberse lanzado el resto de scripts asociados a la tarea. Debe estar creada la tabla "temp_mig_units" a través del script "20231005_1_create_migration_tables.sql"
-- --------------------------------------------------------------------------------------------------

-- PASO 1 Obtener los datos del srm. Lanzar la consulta en la bd del srm de este entorno
--ATENCIÓN EL WHERE CAMBIA POR ENTORNO RELLENAR CON FILL-ME SEGÚN SE INDICA

select 'insert into temp_mig_units( CODE, CODE_NESTED, URI, URN, URN_PROVIDER, MANAGEMENT_APP_URL, VERSION, TYPE, label_es, label_ca, label_en) values('
|| '''' || co_detail.code || ''','
|| 'null,' 
|| ''''  || '/latest/codelists/' || o_detail.code || '/' || c.code || '/' || c.version_logic || '/codes/' || co_detail.code || ''','
|| '''' || co_detail.urn || ''','
|| '''' || co_detail.urn_provider || ''','
|| ''''  || '/#structuralResources/codelists/codelist;id=' || o_detail.code || ':' || c.code || '(' || c.version_logic || ')' || '/code;id=' || co_detail.code  || ''','
|| '0, '
|| '''structuralResources#code'','
|| '''' || (select replace(label, '''', '''''') from tb_localised_strings co_title where co_title.international_string_fk = co_detail.name_fk and co_title.locale = 'es') || ''','
|| coalesce('''' || (select replace(label, '''', '''''')  from tb_localised_strings co_title where co_title.international_string_fk = co_detail.name_fk and co_title.locale = 'ca') || '''', 'null') || ','
|| coalesce('''' || (select replace(label, '''', '''''') from tb_localised_strings co_title where co_title.international_string_fk = co_detail.name_fk and co_title.locale = 'en') || '''', 'null') 
|| ');'
 from tb_item_schemes_versions a, tb_annotable_artefacts c, 
      tb_organisations o, tb_annotable_artefacts o_detail, tb_codelists_versions d, tb_codes co, tb_annotable_artefacts co_detail
 where  FILL-ME

/* ENTORNO DE DEMO TESTEADO
 where  a.maintainable_artefact_fk = c.id
and c.public_logic = true 
and d.tb_item_schemes_versions = a.id
and c.latest_version_number_public is not null
and c.code = 'CL_UNIDADES_MEDIDA'
and c.version_logic = '03.001'
and o.id = c.maintainer_fk
and o.nameable_artefact_fk = o_detail.id
and o_detail.code = 'ISTAC'
and co.item_scheme_version_fk = a.id
and co_detail.id = co.nameable_artefact_fk
and co_detail.code in('ANIOS', 'CABEZAS', 'CM', 'DIAS', 'EMPRESAS', 'ESP', 'ESTABLECIMIENTOS', 'EUR', 'EUR_PERSONA',  'HA', 'HORAS', 'INDICE', 'KG', 'KM', 'KM2', 'M', 'M2', 'M3', 'MESES', 'MINUTOS', 'MW', 'MWH', 'NOCHES', 'NUMERO', 'PERSONAS', 'PLAZAS', 'POR_CADA_1000', 'POR_CADA_100000',  'PORCENTAJE', 'PUESTOS_TRABAJO', 'PUNTOS', 'UG', 'T', 'UTA', 'UNIONES', 'USD', 'VEHICULOS', 'VEHICULOS_1000');

 */

 /* ENTORNO PRE-IBESTAT
 where  a.maintainable_artefact_fk = c.id
and c.public_logic = true 
and d.tb_item_schemes_versions = a.id
and c.latest_version_number_public is not null
and c.code = 'CL_UNIDADES_MEDIDA'
and c.version_logic = '02.004'
and o.id = c.maintainer_fk
and o.nameable_artefact_fk = o_detail.id
and o_detail.code = 'ISTAC'
and co.item_scheme_version_fk = a.id
and co_detail.id = co.nameable_artefact_fk
and co_detail.code in('ANIOS', 'CABEZAS', 'CM', 'DIAS', 'EMPRESAS', 'ESP', 'ESTABLECIMIENTOS', 'EUR', 'EUR_M2', 'EUR_MILES', 'EUR_MILLONES', 'EUR_PERSONA', 'G', 'GW', 'GWH', 'HA', 'HL', 'HORAS', 'HORAS_MILES', 'INDICE', 'KCAL', 'KG', 'KM', 'KM2', 'KW', 'KWH', 'L', 'M', 'M2', 'M3', 'MESES', 'MINUTOS', 'MW', 'MWH', 'NAC', 'NOCHES', 'NUMERO', 'OZ', 'PERSONAS', 'PERSONAS_MILES', 'PLAZAS', 'POR_CADA_1000', 'POR_CADA_10000', 'POR_CADA_100000', 'POR_MILLA', 'PORCENTAJE', 'PUESTOS_TRABAJO', 'PUNTOS', 'T', 'TJ', 'UNIONES', 'USD', 'VEHICULOS', 'VEHICULOS_1000');

 */
 
 /* ENTORNO DE PRE-IESTADIS
 where  a.maintainable_artefact_fk = c.id
and c.public_logic = true 
and d.tb_item_schemes_versions = a.id
and c.latest_version_number_public is not null
and c.code = 'CL_UNIDADES_MEDIDA'
and c.version_logic = '02.001'
and o.id = c.maintainer_fk
and o.nameable_artefact_fk = o_detail.id
and o_detail.code = 'ISTAC' --USA ISTAC
and co.item_scheme_version_fk = a.id
and co_detail.id = co.nameable_artefact_fk
and co_detail.code in('ANIOS', 'CABEZAS', 'CM', 'DIAS', 'EMPRESAS', 'ESP', 'ESTABLECIMIENTOS', 'EUR', 'EUR_M2', 'EUR_MILES', 'EUR_MILLONES', 'EUR_PERSONA', 'G', 'GW', 'GWH', 'HA', 'HORAS', 'HORAS_MILES', 'INDICE', 'KCAL', 'KG', 'KM', 'KM2', 'KW', 'KWH', 'L', 'M', 'M2', 'M3', 'MESES', 'MINUTOS', 'MW', 'MWH', 'NOCHES', 'NUMERO', 'OZ', 'PERSONAS', 'PLAZAS', 'POR_CADA_1000', 'POR_CADA_10000', 'POR_CADA_100000', 'POR_MILLA', 'PORCENTAJE', 'PUESTOS_TRABAJO', 'PUNTOS', 'T', 'TJ', 'UNIONES', 'USD', 'VEHICULOS', 'VEHICULOS_1000');

 */
 
 /* ENTORNO DE PRE ISTAC
 where  a.maintainable_artefact_fk = c.id
and c.public_logic = true 
and d.tb_item_schemes_versions = a.id
and c.latest_version_number_public is not null
and c.code = 'CL_UNIDADES_MEDIDA'
and c.version_logic = '03.002'
and o.id = c.maintainer_fk
and o.nameable_artefact_fk = o_detail.id
and o_detail.code = 'ISTAC'
and co.item_scheme_version_fk = a.id
and co_detail.id = co.nameable_artefact_fk
and co_detail.code in('ANIOS', 'CABEZAS', 'CM', 'DIAS', 'EMPRESAS', 'ESP', 'ESTABLECIMIENTOS', 'EUR', 'EUR_MILES', 'EUR_MILLONES', 'G', 'GW', 'GWH', 'HA', 'HL', 'HORAS', 'HORAS_MILES', 'INDICE', 'KCAL', 'KG', 'KM', 'KM2', 'KW', 'MILES', 'KWH', 'L', 'M', 'M2', 'M3', 'MESES', 'MINUTOS', 'MW', 'MWH', 'NAC', 'NOCHES', 'NUMERO', 'OZ', 'PERSONAS', 'PERSONAS_MILES', 'PLAZAS', 'POR_CADA_1000', 'POR_CADA_10000', 'POR_CADA_100000', 'POR_MILLA', 'PORCENTAJE', 'PUESTOS_TRABAJO', 'PUNTOS', 'UG', 'T', 'TJ', 'UTA', 'UNIONES', 'USD', 'VEHICULOS');

 */
 
 /* ENTORNO DE PRO IBESTAT
 where  a.maintainable_artefact_fk = c.id
and c.public_logic = true 
and d.tb_item_schemes_versions = a.id
and c.latest_version_number_public is not null
and c.code = 'CL_UNIDADES_MEDIDA'
and c.version_logic = '03.003'
and o.id = c.maintainer_fk
and o.nameable_artefact_fk = o_detail.id
and o_detail.code = 'ISTAC'
and co.item_scheme_version_fk = a.id
and co_detail.id = co.nameable_artefact_fk
and co_detail.code in('ANIOS', 'CABEZAS', 'CM', 'DIAS', 'EMPRESAS', 'ESP', 'ESTABLECIMIENTOS', 'EUR', 'EUR_M2', 'EUR_MILES', 'EUR_MILLONES', 'EUR_PERSONA', 'G', 'GW', 'GWH', 'HA', 'HL', 'HORAS', 'HORAS_MILES', 'INDICE', 'KCAL', 'KG', 'KM', 'KM2', 'KW', 'KWH', 'L', 'M', 'M2', 'M3', 'MESES', 'MINUTOS', 'MW', 'MWH', 'NAC', 'NOCHES', 'NUMERO', 'OZ', 'PERSONAS', 'PERSONAS_MILES', 'PLAZAS', 'POR_CADA_1000', 'POR_CADA_10000', 'POR_CADA_100000', 'POR_MILLA', 'PORCENTAJE', 'PUESTOS_TRABAJO', 'PUNTOS', 'T', 'TJ', 'UNIONES', 'USD', 'VEHICULOS', 'VEHICULOS_1000');

 */
 
 /* ENTORNO DE PRO IESTADIS
 where  a.maintainable_artefact_fk = c.id
and c.public_logic = true 
and d.tb_item_schemes_versions = a.id
and c.latest_version_number_public is not null
and c.code = 'CL_UNIDADES_MEDIDA'
and c.version_logic = '01.003'
and o.id = c.maintainer_fk
and o.nameable_artefact_fk = o_detail.id
and o_detail.code = 'IECM'
and co.item_scheme_version_fk = a.id
and co_detail.id = co.nameable_artefact_fk
and co_detail.code in('ANIOS', 'MESES', 'DIAS', 'NOCHES', 'HORAS', 'HORAS_MILES', 'MINUTOS', 'SEGUNDOS', 'CM', 'M', 'KM', 'M2', 'HA', 'KM2', 'M3', 'HM3', 'L', 'G', 'OZ', 'KG', 'T', 'KW', 'KWH', 'MW', 'MWH', 'GW', 'GWH', 'TJ', 'KCAL', 'ESP', 'EUR', 'EUR_MILES', 'EUR_MILLONES', 'EUR_M2', 'EUR_PERSONA', 'USD', 'CABEZAS', 'CAJETILLAS', 'CAPSULAS', 'EMPRESAS', 'ESTABLECIMIENTOS', 'HOGARES', 'INDICE', 'MUNICIPIOS', 'NUMERO', 'OPERACIONES', 'PERSONAS', 'PLAZAS', 'POR_CADA_1000', 'POR_CADA_10000', 'POR_CADA_100000', 'POR_CADA_1000000', 'PORCENTAJE', 'PUESTOS_TRABAJO', 'PUNTOS', 'REPRESENTANTES', 'REPRESENTANTES_CONCEJALES', 'REPRESENTANTES_DIPUTADOS', 'REPRESENTANTES_SENADORES', 'SUCESOS', 'UNIDADES', 'UNIONES', 'VEHICULOS', 'VEHICULOS_1000', 'VOTOS');

 */
 
  /* ENTORNO DE PRO ISTAC
 where  a.maintainable_artefact_fk = c.id
and c.public_logic = true 
and d.tb_item_schemes_versions = a.id
and c.latest_version_number_public is not null
and c.code = 'CL_UNIDADES_MEDIDA'
and c.version_logic = '03.003'
and o.id = c.maintainer_fk
and o.nameable_artefact_fk = o_detail.id
and o_detail.code = 'ISTAC'
and co.item_scheme_version_fk = a.id
and co_detail.id = co.nameable_artefact_fk
and co_detail.code in('ANIOS', 'CABEZAS', 'CM', 'DIAS', 'EMPRESAS', 'ESP', 'ESTABLECIMIENTOS', 'EUR', 'EUR_MILES', 'EUR_MILLONES', 'G', 'GW', 'GWH', 'HA', 'HL', 'HORAS', 'HORAS_MILES', 'INDICE', 'KCAL', 'KG', 'KM', 'KM2', 'KW', 'MILES', 'KWH', 'L', 'M', 'M2', 'M3', 'MESES', 'MINUTOS', 'MW', 'MWH', 'NAC', 'NOCHES', 'NUMERO', 'OZ', 'PERSONAS', 'PERSONAS_MILES', 'PLAZAS', 'POR_CADA_1000', 'POR_CADA_10000', 'POR_CADA_100000', 'POR_MILLA', 'PORCENTAJE', 'PUESTOS_TRABAJO', 'PUNTOS', 'UG', 'T', 'TJ', 'UTA', 'UNIONES', 'USD', 'VEHICULOS');
*/
 
 /* ENTORNO DE DESARROLLO
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
  */

--PASO 2 El resultado anterior copiarlo en la tabla que debe estar creada en INDICATORS denominada temp_mig_units
/*
Ejemplos:
insert into temp_mig_units( CODE, CODE_NESTED, URI, URN, URN_PROVIDER, MANAGEMENT_APP_URL, VERSION, TYPE, label_es, label_ca, label_en) values('FINCAS',null,'/latest/codelists/ISTAC/CL_UNIDADES_MEDIDA/01.004/codes/FINCAS','urn:sdmx:org.sdmx.infomodel.codelist.Code=ISTAC:CL_UNIDADES_MEDIDA(01.004).FINCAS','urn:sdmx:org.sdmx.infomodel.codelist.Code=ISTAC:CL_UNIDADES_MEDIDA(01.004).FINCAS','/#structuralResources/codelists/codelist;id=ISTAC:CL_UNIDADES_MEDIDA(01.004)/code;id=FINCAS',0, 'structuralResources#code','Fincas','null','null');
insert into temp_mig_units( CODE, CODE_NESTED, URI, URN, URN_PROVIDER, MANAGEMENT_APP_URL, VERSION, TYPE, label_es, label_ca, label_en) values('VOTOS',null,'/latest/codelists/ISTAC/CL_UNIDADES_MEDIDA/01.004/codes/VOTOS','urn:sdmx:org.sdmx.infomodel.codelist.Code=ISTAC:CL_UNIDADES_MEDIDA(01.004).VOTOS','urn:sdmx:org.sdmx.infomodel.codelist.Code=ISTAC:CL_UNIDADES_MEDIDA(01.004).VOTOS','/#structuralResources/codelists/codelist;id=ISTAC:CL_UNIDADES_MEDIDA(01.004)/code;id=VOTOS',0, 'structuralResources#code','Votos','null','null');
*/

--PASO 3 Asociar los códigos antiguos a los nuevos en temp_mig_units. Para ello asociarlo con esta consulta:
select 'UPDATE temp_mig_units set id_unit_tb_lis_quantities =' || q.id || ' where urn=''' || d.urn || ''';'
from tb_lis_quantities_units q, tb_localised_strings l, temp_mig_units d where q.title_fk = l.international_string_fk and l.locale = 'es'
and d."label_es" ilike l."label" || '%';   

-- PASO 4 ejecutar los UPDATE DEL PASO ANTERIOR EN LA BD INDICATORS.
/*
Ejemplos
UPDATE temp_mig_units set id_unit_tb_lis_quantities = 41 where urn=urn:sdmx:org.sdmx.infomodel.codelist.Code=ISTAC:CL_UNIDADES_MEDIDA(01.004).FINCAS;
UPDATE temp_mig_units set id_unit_tb_lis_quantities = 21 where urn=urn:sdmx:org.sdmx.infomodel.codelist.Code=ISTAC:CL_UNIDADES_MEDIDA(01.004).VOTOS;
*/

--Paso 5 Ajustar updates anteriores con errores detectados en distintos entornos. Ejecutar la siguiente consulta en la bd indicadores
select 'UPDATE temp_mig_units set id_unit_tb_lis_quantities =' || q.id || ' where urn=''' || d.urn || ''';'
from tb_lis_quantities_units q, tb_localised_strings l, temp_mig_units d where q.title_fk = l.international_string_fk and l.locale = 'es'
and d."label_es" ilike l."label" || '%'
and d."label_es" in('Gigavatios-hora', 'Euros por persona', 'Euros por metro cuadrado', 'Kilómetros cuadrados', 'Metros cuadrados', 'Kilovatios-hora', 'Vehículos por cada 1.000 personas', 'Metros cúbicos')  
and l."label" in('Megavatio hora', 'Gigavatios-hora', 'Euros por persona', 'Euros por metro cuadrado', 'Kilómetros cuadrados', 'Metros cuadrados', 'Kilovatios-hora', 'Vehículos por cada 1.000 personas', 'Metros cúbicos');

--Paso 5.1 Lanzar en la bd indicadores los UPDATE generados en el paso anterior

-- PASO 6 Asegurarse que todo está listo
select case when (select count(*) from tb_lis_quantities_units a where a.id not in(select coalesce(id_unit_tb_lis_quantities,0) from temp_mig_units)) = 0 then 'TODO CORRECTO. PUEDES SEGUIR ADELANTE.' else 'ERROR. HAY ENTRADAS QUE NO TIENEN RELACIÓN ENTRE UNIDAD ANTIGUA Y NUEVA' end 


-- PASO FINAL COMPROBACIONES DE DATOS
/*
 Con todo lo anterior se han obtenido en la tabla temp_mig_units todos los datos necesarios para empezar la migración. Aquí, por claridad hay que hacer ajustes.
 - En el paso 2, quitar del where en and co_detail.code in( los códigos que no tengan códigos asociados en indicators ya que no se usarán. No es necesario este paso ya que 
 si el valor en id_unit_tb_lis_quantities está vacío no se usará. Pero por claridad se aconseja.
 - Comprobar que el id_unit_tb_lis_quantities no se repite para ninguna asociación. Si se repite es porque problablemente sobre esa entrada ya que no está en la tabla 
 de indicators
 Con esta consulta se pueden ver las unidades existentes en indicadores en el momento actual para comprobar que todo es correcto
 select a.id, tls."label"  from tb_lis_quantities_units a, tb_localised_strings tls 
where tls.international_string_fk = a.title_fk 
and tls.locale ='es'
order by a.id
 
 */
commit;

