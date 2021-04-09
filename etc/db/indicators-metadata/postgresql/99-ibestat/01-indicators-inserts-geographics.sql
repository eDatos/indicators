-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- IBESTATNORMA-15 - Carga inicial indicadores
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- Fichero elaborado a partir de la clasificación ISTAC:CL_GRANULARIDADES_GEOGRAFICAS(02.001) e IBESTAT:CL_AREA_ES53(01.000)
-- Enlace. https://pre-ibestat.edatos.io/structural-resources-internal/#structuralResources/codelists/codelist;id=ISTAC%3ACL_GRANULARIDADES_GEOGRAFICAS(02.001)
-- Enlace. https://pre-ibestat.edatos.io/structural-resources-internal/#structuralResources/codelists/codelist;id=IBESTAT%253ACL_AREA_ES53(01.000)
-- 
-- Notas:
-- 1. Es necesario tener instalado la extensión uuid-ossp para la generación de los uuids de los elementos insertados 
-- -- Para ello es necesario ejecutar la siguiente sentencia como administrador de la bbdd CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
-- 2. Se eliminan de la clasificación de granularidades los códigos especiales (incluye WORLD) 
-- 3. Se eliminan de la clasificación de valores espaciales los códigos especiales.
-- 4. Se eliminan las granularidades sin códigos especiales. 
-- 5. Se elimina la granularidad GEO_OTROS
-- 6. Se complentan los FILL_ME de orden incluyendo el código del parent.
--        - Se remplaza FILL_ME por "ES"
-- 7. Este script ha sido generado en base al script localizado en la ruta etc/helpers/01-generate-inserts-geographics.sql dentro de este mismo proyecto.
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------

-- Granularidad geografica: REGIONS
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Comunidades autónomas', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Regions', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Comunitats autònomes', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LIS_GEOGR_GRANULARITIES (VERSION, ID, CODE, UUID, TITLE_FK) values (0, nextval('SEQ_GEOGR_GRANULARITIES'), 'REGIONS', uuid_generate_v4(), currval('SEQ_I18NSTRS'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES70
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Canàries', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Canary Islands', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Canarias', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES70', null, null, 'ES_ES70', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES64
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ciutat Autònoma de Melilla', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ciudad Autónoma de Melilla', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Melilla', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES64', null, null, 'ES_ES64', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES63_ES64
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ceuta y Melilla', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ciudades Autónomas de Ceuta y Melilla', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ciutats Autònomes de Ceuta i Melilla', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES63_ES64', null, null, 'ES_ES63_ES64', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES63
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ciutat Autònoma de Ceuta', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ciudad Autónoma de Ceuta', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ceuta', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES63', null, null, 'ES_ES63', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES62
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Murcia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Región de Murcia', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Regió de Múrcia', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES62', null, null, 'ES_ES62', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES61
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Andalusia', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Andalusia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Andalucía', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES61', null, null, 'ES_ES61', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES53
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Illes Balears', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Illes Balears', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Balearic Islands', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES53', null, null, 'ES_ES53', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES52
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valencia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Comunidad Valenciana', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Comunitat Valenciana', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES52', null, null, 'ES_ES52', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES51
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Catalunya', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cataluña', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Catalonia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES51', null, null, 'ES_ES51', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES43
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Extremadura', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Extremadura', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Extremadura', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES43', null, null, 'ES_ES43', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES42
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Castella-la Manxa', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Castile-La Mancha', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Castilla - La Mancha', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES42', null, null, 'ES_ES42', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES41
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Castella i Lleó', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Castile and Leon', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Castilla y León', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES41', null, null, 'ES_ES41', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES30
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Comunitat de Madrid', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Comunidad de Madrid', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Madrid', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES30', null, null, 'ES_ES30', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES24
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Aragon', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Aragón', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Aragó', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES24', null, null, 'ES_ES24', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES23
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Rioja', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Rioja', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Rioja', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES23', null, null, 'ES_ES23', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES22
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Comunidad Foral de Navarra', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Navarre', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Comunitat foral de Navarra', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES22', null, null, 'ES_ES22', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES21
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'País Vasco', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'País Basc', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Basque Country', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES21', null, null, 'ES_ES21', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES13
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cantabria', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cantabria', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cantabria', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES13', null, null, 'ES_ES13', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES12
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Principado de Asturias', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Asturias', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Principat d''Astúries', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES12', null, null, 'ES_ES12', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES11
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Galícia', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Galicia', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Galicia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES11', null, null, 'ES_ES11', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
-- Granularidad geografica: PROVINCES
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Províncies', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Provincias', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Provinces', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LIS_GEOGR_GRANULARITIES (VERSION, ID, CODE, UUID, TITLE_FK) values (0, nextval('SEQ_GEOGR_GRANULARITIES'), 'PROVINCES', uuid_generate_v4(), currval('SEQ_I18NSTRS'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES702
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Cruz de Tenerife', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Cruz de Tenerife', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Cruz de Tenerife', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES702', null, null, 'ES_ES702', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES701
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Las Palmas', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Las Palmas', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Las Palmas', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES701', null, null, 'ES_ES701', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES640
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Melilla', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Melilla', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Melilla', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES640', null, null, 'ES_ES640', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES630_ES640
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ceuta y Melilla', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ceuta i Melilla', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ceuta y Melilla', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES630_ES640', null, null, 'ES_ES630_ES640', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES630
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ceuta', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ceuta', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ceuta', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES630', null, null, 'ES_ES630', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES620
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Murcia', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Murcia', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Murcia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES620', null, null, 'ES_ES620', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES618
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Seville', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sevilla', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Seville', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES618', null, null, 'ES_ES618', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES617
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Málaga', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Málaga', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Málaga', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES617', null, null, 'ES_ES617', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES616
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Jaén', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Jaén', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Jaén', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES616', null, null, 'ES_ES616', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES615
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Huelva', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Huelva', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Huelva', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES615', null, null, 'ES_ES615', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES614
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Granada', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Granada', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Granada', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES614', null, null, 'ES_ES614', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES613
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cordova', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Córdoba', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cordova', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES613', null, null, 'ES_ES613', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES612
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cádiz', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cádiz', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cádiz', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES612', null, null, 'ES_ES612', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES611
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Almería', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Almería', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Almería', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES611', null, null, 'ES_ES611', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES530
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Balearic Islands', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Illes Balears', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Illes Balears', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES530', null, null, 'ES_ES530', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES523
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'València / Valencia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'València', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'València / Valencia', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES523', null, null, 'ES_ES523', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES522
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Castelló', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Castelló / Castellón', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Castelló / Castellón', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES522', null, null, 'ES_ES522', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES521
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alacant / Alicante', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alacant', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alacant / Alicante', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES521', null, null, 'ES_ES521', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES514
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tarragona', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tarragona', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tarragona', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES514', null, null, 'ES_ES514', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES513
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lleida', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lleida', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lleida', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES513', null, null, 'ES_ES513', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES512
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Girona', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Girona', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Girona', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES512', null, null, 'ES_ES512', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES511
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Barcelona', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Barcelona', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Barcelona', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES511', null, null, 'ES_ES511', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES432
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cáceres', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cáceres', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cáceres', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES432', null, null, 'ES_ES432', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES431
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Badajoz', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Badajoz', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Badajoz', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES431', null, null, 'ES_ES431', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES425
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Toledo', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Toledo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Toledo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES425', null, null, 'ES_ES425', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES424
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Guadalajara', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Guadalajara', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Guadalajara', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES424', null, null, 'ES_ES424', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES423
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cuenca', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cuenca', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cuenca', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES423', null, null, 'ES_ES423', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES422
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ciudad Real', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ciudad Real', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ciudad Real', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES422', null, null, 'ES_ES422', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES421
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Albacete', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Albacete', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Albacete', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES421', null, null, 'ES_ES421', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES419
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Zamora', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Zamora', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Zamora', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES419', null, null, 'ES_ES419', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES418
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valladolid', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valladolid', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valladolid', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES418', null, null, 'ES_ES418', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES417
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Soria', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Soria', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Soria', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES417', null, null, 'ES_ES417', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES416
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Segovia', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Segovia', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Segovia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES416', null, null, 'ES_ES416', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES415
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Salamanca', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Salamanca', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Salamanca', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES415', null, null, 'ES_ES415', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES414
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Palencia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Palencia', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Palencia', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES414', null, null, 'ES_ES414', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES413
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'León', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'León', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'León', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES413', null, null, 'ES_ES413', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES412
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Burgos', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Burgos', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Burgos', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES412', null, null, 'ES_ES412', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES411
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ávila', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ávila', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ávila', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES411', null, null, 'ES_ES411', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES300
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Madrid', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Madrid', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Madrid', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES300', null, null, 'ES_ES300', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES243
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Saragossa', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Zaragoza', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Zaragoza', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES243', null, null, 'ES_ES243', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES242
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Teruel', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Teruel', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Teruel', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES242', null, null, 'ES_ES242', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES241
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Huesca', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Huesca', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Huesca', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES241', null, null, 'ES_ES241', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES230
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Rioja', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Rioja', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Rioja', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES230', null, null, 'ES_ES230', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES220
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Navarre', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Navarra', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Navarra', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES220', null, null, 'ES_ES220', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES213
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Bizkaia', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Bizkaia', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Biscay', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES213', null, null, 'ES_ES213', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES212
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Gipuzkoa', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Gipuzkoa', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Gipuzkoa', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES212', null, null, 'ES_ES212', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES211
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Araba / Álava', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Araba / Álava', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Araba / Álava', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES211', null, null, 'ES_ES211', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES130
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cantabria', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cantabria', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cantabria', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES130', null, null, 'ES_ES130', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES120
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Asturias', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Astúries', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Asturias', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES120', null, null, 'ES_ES120', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES114
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pontevedra', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pontevedra', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pontevedra', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES114', null, null, 'ES_ES114', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES113
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ourense', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ourense', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ourense', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES113', null, null, 'ES_ES113', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES112
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lugo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lugo', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lugo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES112', null, null, 'ES_ES112', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES111
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'A Coruña', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'A Coruña', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Corunna', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES111', null, null, 'ES_ES111', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
  
-- Granularidad geografica: MUNICIPALITIES
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Municipis', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Municipalities', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Municipios', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LIS_GEOGR_GRANULARITIES (VERSION, ID, CODE, UUID, TITLE_FK) values (0, nextval('SEQ_GEOGR_GRANULARITIES'), 'MUNICIPALITIES', uuid_generate_v4(), currval('SEQ_I18NSTRS'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38901
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Pinar de El Hierro', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Pinar de El Hierro', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Pinar de El Hierro', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38901', null, null, 'ES_38901', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38053
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villa de Mazo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villa de Mazo', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villa de Mazo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38053', null, null, 'ES_38053', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38052
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Vilaflor de Chasna', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Vilaflor de Chasna', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Vilaflor de Chasna', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38052', null, null, 'ES_38052', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38051
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Victoria de Acentejo', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Victoria de Acentejo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Victoria de Acentejo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38051', null, null, 'ES_38051', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38050
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Vallehermoso', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Vallehermoso', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Vallehermoso', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38050', null, null, 'ES_38050', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38049
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valle Gran Rey', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valle Gran Rey', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valle Gran Rey', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38049', null, null, 'ES_38049', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38048
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valverde', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valverde', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valverde', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38048', null, null, 'ES_38048', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38047
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tijarafe', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tijarafe', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tijarafe', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38047', null, null, 'ES_38047', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38046
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tegueste', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tegueste', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tegueste', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38046', null, null, 'ES_38046', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38045
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tazacorte', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tazacorte', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tazacorte', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38045', null, null, 'ES_38045', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38044
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Tanque', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Tanque', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Tanque', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38044', null, null, 'ES_38044', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38043
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tacoronte', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tacoronte', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tacoronte', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38043', null, null, 'ES_38043', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38042
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Los Silos', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Los Silos', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Los Silos', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38042', null, null, 'ES_38042', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38041
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Sauzal', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Sauzal', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Sauzal', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38041', null, null, 'ES_38041', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38040
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santiago del Teide', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santiago del Teide', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santiago del Teide', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38040', null, null, 'ES_38040', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38039
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa úrsula', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa úrsula', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa úrsula', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38039', null, null, 'ES_38039', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38038
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Cruz de Tenerife', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Cruz de Tenerife', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Cruz de Tenerife', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38038', null, null, 'ES_38038', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38037
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Cruz de La Palma', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Cruz de La Palma', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Cruz de La Palma', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38037', null, null, 'ES_38037', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38036
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Sebastián de La Gomera', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Sebastián de La Gomera', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Sebastián de La Gomera', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38036', null, null, 'ES_38036', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38035
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Miguel de Abona', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Miguel de Abona', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Miguel de Abona', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38035', null, null, 'ES_38035', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38034
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Juan de la Rambla', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Juan de la Rambla', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Juan de la Rambla', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38034', null, null, 'ES_38034', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38033
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Andrés y Sauces', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Andrés y Sauces', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Andrés y Sauces', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38033', null, null, 'ES_38033', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38032
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Rosario', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Rosario', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Rosario', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38032', null, null, 'ES_38032', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38031
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Los Realejos', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Los Realejos', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Los Realejos', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38031', null, null, 'ES_38031', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38030
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puntallana', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puntallana', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puntallana', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38030', null, null, 'ES_38030', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38029
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puntagorda', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puntagorda', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puntagorda', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38029', null, null, 'ES_38029', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38028
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puerto de la Cruz', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puerto de la Cruz', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puerto de la Cruz', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38028', null, null, 'ES_38028', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38027
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Paso', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Paso', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Paso', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38027', null, null, 'ES_38027', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38026
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Orotava', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Orotava', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Orotava', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38026', null, null, 'ES_38026', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38025
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Matanza de Acentejo', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Matanza de Acentejo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Matanza de Acentejo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38025', null, null, 'ES_38025', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38024
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Los Llanos de Aridane', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Los Llanos de Aridane', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Los Llanos de Aridane', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38024', null, null, 'ES_38024', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38023
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Cristóbal de La Laguna', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Cristóbal de La Laguna', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Cristóbal de La Laguna', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38023', null, null, 'ES_38023', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38022
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Icod de los Vinos', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Icod de los Vinos', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Icod de los Vinos', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38022', null, null, 'ES_38022', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38021
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Hermigua', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Hermigua', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Hermigua', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38021', null, null, 'ES_38021', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38020
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Güímar', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Güímar', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Güímar', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38020', null, null, 'ES_38020', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38019
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Guía de Isora', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Guía de Isora', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Guía de Isora', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38019', null, null, 'ES_38019', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38018
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Guancha', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Guancha', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Guancha', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38018', null, null, 'ES_38018', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38017
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Granadilla de Abona', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Granadilla de Abona', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Granadilla de Abona', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38017', null, null, 'ES_38017', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38016
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Garafía', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Garafía', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Garafía', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38016', null, null, 'ES_38016', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38015
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Garachico', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Garachico', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Garachico', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38015', null, null, 'ES_38015', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38014
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fuencaliente de La Palma', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fuencaliente de La Palma', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fuencaliente de La Palma', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38014', null, null, 'ES_38014', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38013_2007
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Frontera', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Frontera', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Frontera', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38013_2007', null, null, 'ES_38013_2007', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38013_1912
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Frontera (to 2007)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Frontera (hasta 2007)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Frontera (to 2007)', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38013_1912', null, null, 'ES_38013_1912', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38012
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fasnia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fasnia', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fasnia', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38012', null, null, 'ES_38012', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38011
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Candelaria', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Candelaria', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Candelaria', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38011', null, null, 'ES_38011', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38010
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Buenavista del Norte', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Buenavista del Norte', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Buenavista del Norte', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38010', null, null, 'ES_38010', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38009
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Breña Baja', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Breña Baja', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Breña Baja', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38009', null, null, 'ES_38009', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38008
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Breña Alta', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Breña Alta', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Breña Alta', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38008', null, null, 'ES_38008', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38007
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Barlovento', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Barlovento', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Barlovento', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38007', null, null, 'ES_38007', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38006
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arona', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arona', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arona', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38006', null, null, 'ES_38006', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38005
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arico', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arico', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arico', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38005', null, null, 'ES_38005', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38004
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arafo', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arafo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arafo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38004', null, null, 'ES_38004', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38003
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alajeró', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alajeró', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alajeró', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38003', null, null, 'ES_38003', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38002
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Agulo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Agulo', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Agulo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38002', null, null, 'ES_38002', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38001
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Adeje', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Adeje', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Adeje', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38001', null, null, 'ES_38001', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35034
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Yaiza', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Yaiza', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Yaiza', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35034', null, null, 'ES_35034', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35033
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Vega de San Mateo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Vega de San Mateo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Vega de San Mateo', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35033', null, null, 'ES_35033', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35032
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valleseco', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valleseco', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valleseco', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35032', null, null, 'ES_35032', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35031
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valsequillo de Gran Canaria', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valsequillo de Gran Canaria', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valsequillo de Gran Canaria', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35031', null, null, 'ES_35031', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35030
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tuineje', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tuineje', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tuineje', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35030', null, null, 'ES_35030', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35029
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tinajo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tinajo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tinajo', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35029', null, null, 'ES_35029', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35028
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tías', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tías', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tías', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35028', null, null, 'ES_35028', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35027
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Teror', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Teror', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Teror', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35027', null, null, 'ES_35027', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35026
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Telde', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Telde', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Telde', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35026', null, null, 'ES_35026', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35025
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tejeda', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tejeda', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tejeda', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35025', null, null, 'ES_35025', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35024
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Teguise', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Teguise', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Teguise', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35024', null, null, 'ES_35024', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35023
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa María de Guía de Gran Canaria', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa María de Guía de Gran Canaria', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa María de Guía de Gran Canaria', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35023', null, null, 'ES_35023', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35022
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Lucía de Tirajana', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Lucía de Tirajana', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Lucía de Tirajana', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35022', null, null, 'ES_35022', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35021
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Brígida', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Brígida', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Brígida', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35021', null, null, 'ES_35021', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35020
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Aldea de San Nicolás', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Aldea de San Nicolás', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Aldea de San Nicolás', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35020', null, null, 'ES_35020', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35019
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Bartolomé de Tirajana', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Bartolomé de Tirajana', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Bartolomé de Tirajana', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35019', null, null, 'ES_35019', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35018
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Bartolomé', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Bartolomé', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Bartolomé', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35018', null, null, 'ES_35018', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35017
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puerto del Rosario', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puerto del Rosario', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puerto del Rosario', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35017', null, null, 'ES_35017', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35016
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Las Palmas de Gran Canaria', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Las Palmas de Gran Canaria', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Las Palmas de Gran Canaria', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35016', null, null, 'ES_35016', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35015
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pájara', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pájara', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pájara', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35015', null, null, 'ES_35015', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35014
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Oliva', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Oliva', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Oliva', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35014', null, null, 'ES_35014', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35013
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Moya', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Moya', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Moya', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35013', null, null, 'ES_35013', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35012
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Mogán', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Mogán', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Mogán', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35012', null, null, 'ES_35012', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35011
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ingenio', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ingenio', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ingenio', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35011', null, null, 'ES_35011', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35010
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Haría', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Haría', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Haría', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35010', null, null, 'ES_35010', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35009
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Gáldar', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Gáldar', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Gáldar', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35009', null, null, 'ES_35009', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35008
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Firgas', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Firgas', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Firgas', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35008', null, null, 'ES_35008', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35007
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Betancuria', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Betancuria', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Betancuria', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35007', null, null, 'ES_35007', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35006
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arucas', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arucas', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arucas', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35006', null, null, 'ES_35006', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35005
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Artenara', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Artenara', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Artenara', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35005', null, null, 'ES_35005', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35004
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arrecife', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arrecife', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arrecife', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35004', null, null, 'ES_35004', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35003
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Antigua', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Antigua', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Antigua', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35003', null, null, 'ES_35003', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35002
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Agüimes', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Agüimes', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Agüimes', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35002', null, null, 'ES_35002', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35001
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Agaete', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Agaete', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Agaete', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35001', null, null, 'ES_35001', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07902
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Es Migjorn Gran', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Es Migjorn Gran', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Es Migjorn Gran', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07902', null, null, 'ES_07902', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07901
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ariany', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ariany', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ariany', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07901', null, null, 'ES_07901', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07065
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Vilafranca de Bonany', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Vilafranca de Bonany', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Vilafranca de Bonany', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07065', null, null, 'ES_07065', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07064
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Es Castell', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Es Castell', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Es Castell', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07064', null, null, 'ES_07064', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07063
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valldemossa', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valldemossa', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valldemossa', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07063', null, null, 'ES_07063', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07062
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Son Servera', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Son Servera', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Son Servera', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07062', null, null, 'ES_07062', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07061
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sóller', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sóller', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sóller', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07061', null, null, 'ES_07061', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07060
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sineu', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sineu', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sineu', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07060', null, null, 'ES_07060', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07059
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ses Salines', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ses Salines', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ses Salines', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07059', null, null, 'ES_07059', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07058
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Selva', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Selva', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Selva', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07058', null, null, 'ES_07058', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07057
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santanyí', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santanyí', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santanyí', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07057', null, null, 'ES_07057', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07056
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Maria del Camí', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Maria del Camí', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Maria del Camí', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07056', null, null, 'ES_07056', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07055
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Margalida', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Margalida', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Margalida', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07055', null, null, 'ES_07055', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07054
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Eulària del Ríu', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Eulària del Ríu', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Eulària del Ríu', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07054', null, null, 'ES_07054', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07053
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Eugènia', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Eugènia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Eugènia', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07053', null, null, 'ES_07053', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07052
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Lluís', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Lluís', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Lluís', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07052', null, null, 'ES_07052', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07051
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Llorenç des Cardassar', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Llorenç des Cardassar', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Llorenç des Cardassar', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07051', null, null, 'ES_07051', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07050
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Joan de Labritja', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Joan de Labritja', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Joan de Labritja', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07050', null, null, 'ES_07050', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07049
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Joan', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Joan', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Joan', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07049', null, null, 'ES_07049', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07048
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Josep de sa Talaia', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Josep de sa Talaia', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Josep de sa Talaia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07048', null, null, 'ES_07048', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07047
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sencelles', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sencelles', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sencelles', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07047', null, null, 'ES_07047', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07046
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Antoni de Portmany', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Antoni de Portmany', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Antoni de Portmany', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07046', null, null, 'ES_07046', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07045
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puigpunyent', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puigpunyent', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puigpunyent', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07045', null, null, 'ES_07045', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07044
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sa Pobla', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sa Pobla', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sa Pobla', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07044', null, null, 'ES_07044', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07043
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Porreres', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Porreres', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Porreres', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07043', null, null, 'ES_07043', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07042
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pollença', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pollença', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pollença', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07042', null, null, 'ES_07042', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07041
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Petra', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Petra', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Petra', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07041', null, null, 'ES_07041', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07040
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Palma', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Palma', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Palma', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07040', null, null, 'ES_07040', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07039
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Muro', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Muro', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Muro', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07039', null, null, 'ES_07039', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07038
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Montuïri', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Montuïri', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Montuïri', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07038', null, null, 'ES_07038', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07037
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Es Mercadal', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Es Mercadal', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Es Mercadal', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07037', null, null, 'ES_07037', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07036
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Marratxí', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Marratxí', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Marratxí', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07036', null, null, 'ES_07036', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07035
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Maria de la Salut', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Maria de la Salut', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Maria de la Salut', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07035', null, null, 'ES_07035', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07034
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Mancor de la Vall', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Mancor de la Vall', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Mancor de la Vall', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07034', null, null, 'ES_07034', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07033
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Manacor', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Manacor', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Manacor', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07033', null, null, 'ES_07033', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07032
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Maó', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Maó', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Maó', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07032', null, null, 'ES_07032', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07031
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Llucmajor', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Llucmajor', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Llucmajor', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07031', null, null, 'ES_07031', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07030
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Llubí', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Llubí', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Llubí', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07030', null, null, 'ES_07030', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07029
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lloseta', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lloseta', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lloseta', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07029', null, null, 'ES_07029', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07028
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lloret de Vistalegre', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lloret de Vistalegre', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lloret de Vistalegre', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07028', null, null, 'ES_07028', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07027
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Inca', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Inca', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Inca', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07027', null, null, 'ES_07027', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07026
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ibiza', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Eivissa', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Eivissa', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07026', null, null, 'ES_07026', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07025
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fornalutx', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fornalutx', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fornalutx', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07025', null, null, 'ES_07025', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07024
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Formentera', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Formentera', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Formentera', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07024', null, null, 'ES_07024', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07023
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ferreries', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ferreries', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ferreries', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07023', null, null, 'ES_07023', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07022
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Felanitx', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Felanitx', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Felanitx', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07022', null, null, 'ES_07022', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07021
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Estellencs', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Estellencs', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Estellencs', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07021', null, null, 'ES_07021', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07020
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Esporles', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Esporles', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Esporles', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07020', null, null, 'ES_07020', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07019
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Escorca', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Escorca', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Escorca', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07019', null, null, 'ES_07019', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07018
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Deià', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Deià', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Deià', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07018', null, null, 'ES_07018', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07017
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Costitx', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Costitx', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Costitx', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07017', null, null, 'ES_07017', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07016
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Consell', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Consell', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Consell', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07016', null, null, 'ES_07016', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07015
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ciutadella de Menorca', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ciutadella de Menorca', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ciutadella de Menorca', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07015', null, null, 'ES_07015', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07014
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Capdepera', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Capdepera', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Capdepera', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07014', null, null, 'ES_07014', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07013
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Campos', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Campos', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Campos', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07013', null, null, 'ES_07013', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07012
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Campanet', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Campanet', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Campanet', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07012', null, null, 'ES_07012', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07011
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Calvià', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Calvià', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Calvià', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07011', null, null, 'ES_07011', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07010
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Bunyola', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Bunyola', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Bunyola', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07010', null, null, 'ES_07010', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07009
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Búger', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Búger', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Búger', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07009', null, null, 'ES_07009', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07008
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Binissalem', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Binissalem', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Binissalem', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07008', null, null, 'ES_07008', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07007
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Banyalbufar', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Banyalbufar', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Banyalbufar', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07007', null, null, 'ES_07007', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07006
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Artà', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Artà', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Artà', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07006', null, null, 'ES_07006', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07005
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Andratx', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Andratx', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Andratx', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07005', null, null, 'ES_07005', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07004
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Algaida', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Algaida', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Algaida', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07004', null, null, 'ES_07004', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07003
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alcúdia', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alcúdia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alcúdia', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07003', null, null, 'ES_07003', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07002
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alaior', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alaior', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alaior', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07002', null, null, 'ES_07002', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07001
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alaró', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alaró', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alaró', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07001', null, null, 'ES_07001', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
-- Granularidad geografica: MICRO_TOURISTIC_ENTITIES
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Microdestino turístico - entidades', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Microdestination touristic - entities', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Microdestinació turística - entitats', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LIS_GEOGR_GRANULARITIES (VERSION, ID, CODE, UUID, TITLE_FK) values (0, nextval('SEQ_GEOGR_GRANULARITIES'), 'MICRO_TOURISTIC_ENTITIES', uuid_generate_v4(), currval('SEQ_I18NSTRS'));
 
-- Granularidad geografica: MICRO_TOURISTIC_CENTRES
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Microdestinació turística - nuclis', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Microdestino turístico - núcleos', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Microdestination touristic - centres', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LIS_GEOGR_GRANULARITIES (VERSION, ID, CODE, UUID, TITLE_FK) values (0, nextval('SEQ_GEOGR_GRANULARITIES'), 'MICRO_TOURISTIC_CENTRES', uuid_generate_v4(), currval('SEQ_I18NSTRS'));
 
-- Granularidad geografica: MESH_BLOCKS
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Mesh blocks', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Mesh blocks', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Mesh blocks', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LIS_GEOGR_GRANULARITIES (VERSION, ID, CODE, UUID, TITLE_FK) values (0, nextval('SEQ_GEOGR_GRANULARITIES'), 'MESH_BLOCKS', uuid_generate_v4(), currval('SEQ_I18NSTRS'));
 
-- Granularidad geografica: LARGE_COUNTIES
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Grans comarques', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Grandes comarcas', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Large counties', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LIS_GEOGR_GRANULARITIES (VERSION, ID, CODE, UUID, TITLE_FK) values (0, nextval('SEQ_GEOGR_GRANULARITIES'), 'LARGE_COUNTIES', uuid_generate_v4(), currval('SEQ_I18NSTRS'));
 
-- Granularidad geografica: ISLANDS
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Illes', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Islas', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'ISLANDS', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LIS_GEOGR_GRANULARITIES (VERSION, ID, CODE, UUID, TITLE_FK) values (0, nextval('SEQ_GEOGR_GRANULARITIES'), 'ISLANDS', uuid_generate_v4(), currval('SEQ_I18NSTRS'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES709
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tenerife', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tenerife', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tenerife', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES709', null, null, 'ES_ES709', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES708
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lanzarote', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lanzarote', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lanzarote', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES708', null, null, 'ES_ES708', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES707
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Palma', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Palma', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Palma', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES707', null, null, 'ES_ES707', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES706_ES709
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Gomera and Tenerife', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Gomera i Tenerife', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Gomera y Tenerife', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES706_ES709', null, null, 'ES_ES706_ES709', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES706_ES703
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Gomera i El Hierro', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Gomera y El Hierro', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Gomera and El Hierro', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES706_ES703', null, null, 'ES_ES706_ES703', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES706
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Gomera', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Gomera', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Gomera', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES706', null, null, 'ES_ES706', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES705
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Gran Canaria', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Gran Canaria', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Gran Canaria', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES705', null, null, 'ES_ES705', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES704
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fuerteventura', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fuerteventura', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fuerteventura', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES704', null, null, 'ES_ES704', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES703
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Hierro', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Hierro', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Hierro', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES703', null, null, 'ES_ES703', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES533
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Menorca', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Minorca', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Menorca', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES533', null, null, 'ES_ES533', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES532
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Mallorca', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Mallorca', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Majorca', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES532', null, null, 'ES_ES532', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES531_072
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Eivissa', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Eivissa', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ibiza', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES531_072', null, null, 'ES_ES531_072', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES531_071
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Formentera', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Formentera', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Formentera', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES531_071', null, null, 'ES_ES531_071', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES531
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ibiza y Formentera', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Eivissa i Formentera', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Eivissa y Formentera', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES531', null, null, 'ES_ES531', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
-- Granularidad geografica: ECONOMIC_ZONES
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Economic zones', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Zonas económicas', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Zones econòmiques', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LIS_GEOGR_GRANULARITIES (VERSION, ID, CODE, UUID, TITLE_FK) values (0, nextval('SEQ_GEOGR_GRANULARITIES'), 'ECONOMIC_ZONES', uuid_generate_v4(), currval('SEQ_I18NSTRS'));
 
     -- Granularidad geografica: ECONOMIC_ZONES Valor geografico: EUROPE_XEU
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Europe (European Union excluded)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Europa (excluida la Unión Europea)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Europa (exclosa la Unió Europea)', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'EUROPE_XEU', null, null, 'ES_EUROPE_XEU', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ECONOMIC_ZONES Valor geografico: EU28_XES
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Unión Europea - 28 (excluida España)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'European Union - 28 (Spain excluded)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Unió Europea - 28 (exclosa Espanya)', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'EU28_XES', null, null, 'ES_EU28_XES', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ECONOMIC_ZONES Valor geografico: EU27_2020_XES
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Unió Europea - 27 (exclosa Espanya)', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Unión Europea - 27 (excluida España)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'European Union - 27 (Spain excluded)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'EU27_2020_XES', null, null, 'ES_EU27_2020_XES', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ECONOMIC_ZONES Valor geografico: EU27_2007_XES
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'European Union - 27 (Spain excluded)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Unió Europea - 27 (exclosa Espanya)', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Unión Europea - 27 (excluida España)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'EU27_2007_XES', null, null, 'ES_EU27_2007_XES', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ECONOMIC_ZONES Valor geografico: EU25_XES
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Unió Europea - 25 (exclosa Espanya)', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'European Union - 25 (Spain excluded)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Unión Europea - 25 (excluida España)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'EU25_XES', null, null, 'ES_EU25_XES', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ECONOMIC_ZONES Valor geografico: EU15_XES
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'European Union - 15 (Spain excluded)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Unió Europea - 15 (exclosa Espanya)', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Unión Europea - 15 (excluida España)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'EU15_XES', null, null, 'ES_EU15_XES', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ECONOMIC_ZONES Valor geografico: EU12_XES
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Unió Europea - 12 (exclosa Espanya)', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'European Union - 12 (Spain excluded)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Unión Europea - 12 (excluida España)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'EU12_XES', null, null, 'ES_EU12_XES', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ECONOMIC_ZONES Valor geografico: EEC6
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Comunidad Económica Europea - 6', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'European Economic Community - 6', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Comunitat Econòmica Europea - 6', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'EEC6', null, null, 'ES_EEC6', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ECONOMIC_ZONES Valor geografico: EC9
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Comunitat Europea - 9', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'European Community - 9', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Comunidad Europea - 9', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'EC9', null, null, 'ES_EC9', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ECONOMIC_ZONES Valor geografico: EC12_XES
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'European Community - 12 (Spain excluded)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Comunitat Europea - 12 (exclosa Espanya)', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Comunidad Europea - 12 (excluida España)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'EC12_XES', null, null, 'ES_EC12_XES', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ECONOMIC_ZONES Valor geografico: EC10
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Comunitat Europea - 10', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'European Community - 10', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Comunidad Europea - 10', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'EC10', null, null, 'ES_EC10', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
  
-- Granularidad geografica: COUNTRIES
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Countries', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Países', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Països', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LIS_GEOGR_GRANULARITIES (VERSION, ID, CODE, UUID, TITLE_FK) values (0, nextval('SEQ_GEOGR_GRANULARITIES'), 'COUNTRIES', uuid_generate_v4(), currval('SEQ_I18NSTRS'));
 
     -- Granularidad geografica: COUNTRIES Valor geografico: ES
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Spain', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Espanya', 'ca', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'España', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES', null, null, 'ES_ES', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
     
commit;