-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- EDATOS-3637 - IESTADIS. Cargar las tablas de valores en eIndicadores
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- Fichero elaborado a partir de la clasificación ISTAC:CL_GRANULARIDADES_GEOGRAFICAS(02.001) e IECM:253ACL_AREA_ES30(02.000)
-- Enlace. https://iestadis.edatos.io/structural-resources-internal/#structuralResources/codelists/codelist;id=ISTAC%253ACL_GRANULARIDADES_GEOGRAFICAS(02.001)
-- Enlace. https://iestadis.edatos.io/structural-resources-internal/#structuralResources/codelists/codelist;id=IECM%253ACL_AREA_ES30(02.000)
-- 
-- Notas:
-- 1. Es necesario tener instalado la extensión uuid-ossp para la generación de los uuids de los elementos insertados 
-- -- Para ello es necesario ejecutar la siguiente sentencia como administrador de la bbdd CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
-- 2. Se eliminan de la clasificación de granularidades los códigos especiales (incluye WORLD) 
-- 3. Se eliminan de la clasificación de valores espaciales los códigos especiales.
-- 4. Se eliminan las granularidades sin códigos especiales. 
-- 5. Se elimina la granularidad GEO_OTROS
-- 6. Se complentan los ES de orden incluyendo el código del parent.
--        - Se remplaza ES por "ES"
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
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Canarias', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Canary Islands', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES70', null, null, 'ES_ES70', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES64
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Melilla', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Melilla (Ciudad Autónoma de)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES64', null, null, 'ES_ES64', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES63_ES64
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ceuta y Melilla', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ceuta y Melilla (Ciudades Autónomas de)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES63_ES64', null, null, 'ES_ES63_ES64', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES63
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ceuta (Ciudad Autónoma de)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ceuta', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES63', null, null, 'ES_ES63', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES62
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Murcia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Murcia (Región de)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES62', null, null, 'ES_ES62', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES61
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Andalusia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Andalucía', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES61', null, null, 'ES_ES61', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES53
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Balears (Illes)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Balearic Islands', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES53', null, null, 'ES_ES53', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES52
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valencia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Comunitat Valenciana', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES52', null, null, 'ES_ES52', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES51
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cataluña', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Catalonia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES51', null, null, 'ES_ES51', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES43
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Extremadura', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Extremadura', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES43', null, null, 'ES_ES43', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES42
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Castilla - La Mancha', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Castile-La Mancha', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES42', null, null, 'ES_ES42', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES41
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Castilla y León', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Castile and Leon', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES41', null, null, 'ES_ES41', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES30
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Madrid (Comunidad de)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Madrid', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES30', null, null, 'ES_01ES30', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES24
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Aragón', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Aragon', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES24', null, null, 'ES_ES24', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES23
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Rioja (La)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Rioja', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES23', null, null, 'ES_ES23', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES22
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Navarra (Comunidad Foral de)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Navarre', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES22', null, null, 'ES_ES22', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES21
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'País Vasco', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Basque Country', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES21', null, null, 'ES_ES21', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES13
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cantabria', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cantabria', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES13', null, null, 'ES_ES13', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES12
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Asturias', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Asturias (Principado de)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES12', null, null, 'ES_ES12', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: REGIONS Valor geografico: ES11
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
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
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Cruz de Tenerife', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Cruz de Tenerife', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES702', null, null, 'ES_ES702', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES701
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Palmas (Las)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Las Palmas', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES701', null, null, 'ES_ES701', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES640
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Melilla', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Melilla', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES640', null, null, 'ES_ES640', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES630_ES640
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ceuta y Melilla', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ceuta y Melilla', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES630_ES640', null, null, 'ES_ES630_ES640', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES630
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ceuta', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ceuta', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES630', null, null, 'ES_ES630', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES620
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Murcia', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Murcia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES620', null, null, 'ES_ES620', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES618
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sevilla', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Seville', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES618', null, null, 'ES_ES618', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES617
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Málaga', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Málaga', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES617', null, null, 'ES_ES617', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES616
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Jaén', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Jaén', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES616', null, null, 'ES_ES616', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES615
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Huelva', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Huelva', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES615', null, null, 'ES_ES615', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES614
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Granada', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Granada', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES614', null, null, 'ES_ES614', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES613
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cordova', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Córdoba', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES613', null, null, 'ES_ES613', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES612
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cádiz', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cádiz', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES612', null, null, 'ES_ES612', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES611
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Almería', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Almería', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES611', null, null, 'ES_ES611', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES530
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Balears (Illes)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Balearic Islands', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES530', null, null, 'ES_ES530', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES523
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'València / Valencia', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'València / Valencia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES523', null, null, 'ES_ES523', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES522
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Castelló / Castellón', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Castelló / Castellón', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES522', null, null, 'ES_ES522', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES521
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alacant / Alicante', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alacant / Alicante', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES521', null, null, 'ES_ES521', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES514
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tarragona', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tarragona', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES514', null, null, 'ES_ES514', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES513
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lleida', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lleida', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES513', null, null, 'ES_ES513', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES512
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Girona', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Girona', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES512', null, null, 'ES_ES512', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES511
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Barcelona', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Barcelona', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES511', null, null, 'ES_ES511', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES432
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cáceres', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cáceres', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES432', null, null, 'ES_ES432', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES431
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Badajoz', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Badajoz', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES431', null, null, 'ES_ES431', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES425
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Toledo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Toledo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES425', null, null, 'ES_ES425', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES424
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Guadalajara', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Guadalajara', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES424', null, null, 'ES_ES424', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES423
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cuenca', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cuenca', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES423', null, null, 'ES_ES423', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES422
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ciudad Real', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ciudad Real', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES422', null, null, 'ES_ES422', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES421
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Albacete', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Albacete', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES421', null, null, 'ES_ES421', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES419
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Zamora', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Zamora', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES419', null, null, 'ES_ES419', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES418
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valladolid', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valladolid', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES418', null, null, 'ES_ES418', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES417
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Soria', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Soria', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES417', null, null, 'ES_ES417', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES416
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Segovia', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Segovia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES416', null, null, 'ES_ES416', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES415
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Salamanca', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Salamanca', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES415', null, null, 'ES_ES415', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES414
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Palencia', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Palencia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES414', null, null, 'ES_ES414', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES413
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'León', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'León', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES413', null, null, 'ES_ES413', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES412
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Burgos', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Burgos', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES412', null, null, 'ES_ES412', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES411
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ávila', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ávila', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES411', null, null, 'ES_ES411', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES300
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Madrid', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Madrid', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES300', null, null, 'ES_ES300', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES243
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Saragossa', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Zaragoza', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES243', null, null, 'ES_ES243', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES242
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Teruel', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Teruel', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES242', null, null, 'ES_ES242', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES241
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Huesca', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Huesca', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES241', null, null, 'ES_ES241', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES230
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Rioja (La)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Rioja', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES230', null, null, 'ES_ES230', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES220
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Navarre', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Navarra', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES220', null, null, 'ES_ES220', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES213
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Biscay', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Bizkaia', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES213', null, null, 'ES_ES213', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES212
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Gipuzkoa', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Gipuzkoa', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES212', null, null, 'ES_ES212', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES211
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Araba / Álava', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Araba / Álava', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES211', null, null, 'ES_ES211', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES130
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cantabria', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cantabria', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES130', null, null, 'ES_ES130', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES120
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Asturias', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Asturias', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES120', null, null, 'ES_ES120', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES114
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pontevedra', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pontevedra', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES114', null, null, 'ES_ES114', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES113
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ourense', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ourense', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES113', null, null, 'ES_ES113', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES112
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lugo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lugo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES112', null, null, 'ES_ES112', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: PROVINCES Valor geografico: ES111
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Coruña (A)', 'es', currval('SEQ_I18NSTRS'), 1);
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
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Pinar de El Hierro', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Pinar de El Hierro', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38901', null, null, 'ES_38901', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38053
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villa de Mazo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villa de Mazo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38053', null, null, 'ES_38053', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38052
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Vilaflor de Chasna', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Vilaflor de Chasna', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38052', null, null, 'ES_38052', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38051
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Victoria de Acentejo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Victoria de Acentejo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38051', null, null, 'ES_38051', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38050
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Vallehermoso', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Vallehermoso', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38050', null, null, 'ES_38050', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38049
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valle Gran Rey', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valle Gran Rey', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38049', null, null, 'ES_38049', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38048
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valverde', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valverde', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38048', null, null, 'ES_38048', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38047
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tijarafe', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tijarafe', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38047', null, null, 'ES_38047', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38046
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tegueste', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tegueste', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38046', null, null, 'ES_38046', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38045
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tazacorte', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tazacorte', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38045', null, null, 'ES_38045', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38044
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Tanque', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Tanque', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38044', null, null, 'ES_38044', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38043
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tacoronte', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tacoronte', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38043', null, null, 'ES_38043', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38042
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Los Silos', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Los Silos', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38042', null, null, 'ES_38042', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38041
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Sauzal', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Sauzal', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38041', null, null, 'ES_38041', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38040
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santiago del Teide', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santiago del Teide', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38040', null, null, 'ES_38040', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38039
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Úrsula', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Úrsula', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38039', null, null, 'ES_38039', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38038
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Cruz de Tenerife', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Cruz de Tenerife', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38038', null, null, 'ES_38038', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38037
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Cruz de La Palma', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Cruz de La Palma', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38037', null, null, 'ES_38037', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38036
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Sebastián de La Gomera', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Sebastián de La Gomera', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38036', null, null, 'ES_38036', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38035
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Miguel de Abona', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Miguel de Abona', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38035', null, null, 'ES_38035', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38034
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Juan de la Rambla', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Juan de la Rambla', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38034', null, null, 'ES_38034', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38033
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Andrés y Sauces', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Andrés y Sauces', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38033', null, null, 'ES_38033', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38032
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Rosario', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Rosario', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38032', null, null, 'ES_38032', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38031
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Los Realejos', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Los Realejos', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38031', null, null, 'ES_38031', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38030
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puntallana', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puntallana', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38030', null, null, 'ES_38030', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38029
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puntagorda', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puntagorda', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38029', null, null, 'ES_38029', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38028
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puerto de la Cruz', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puerto de la Cruz', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38028', null, null, 'ES_38028', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38027
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Paso', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Paso', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38027', null, null, 'ES_38027', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38026
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Orotava', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Orotava', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38026', null, null, 'ES_38026', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38025
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Matanza de Acentejo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Matanza de Acentejo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38025', null, null, 'ES_38025', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38024
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Los Llanos de Aridane', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Los Llanos de Aridane', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38024', null, null, 'ES_38024', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38023
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Cristóbal de La Laguna', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Cristóbal de La Laguna', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38023', null, null, 'ES_38023', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38022
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Icod de los Vinos', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Icod de los Vinos', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38022', null, null, 'ES_38022', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38021
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Hermigua', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Hermigua', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38021', null, null, 'ES_38021', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38020
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Güímar', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Güímar', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38020', null, null, 'ES_38020', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38019
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Guía de Isora', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Guía de Isora', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38019', null, null, 'ES_38019', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38018
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Guancha', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Guancha', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38018', null, null, 'ES_38018', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38017
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Granadilla de Abona', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Granadilla de Abona', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38017', null, null, 'ES_38017', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38016
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Garafía', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Garafía', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38016', null, null, 'ES_38016', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38015
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Garachico', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Garachico', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38015', null, null, 'ES_38015', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38014
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fuencaliente de La Palma', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fuencaliente de La Palma', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38014', null, null, 'ES_38014', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38013_2007
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Frontera', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Frontera', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38013_2007', null, null, 'ES_38013_2007', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38013_1912
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Frontera (hasta 2007)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Frontera (to 2007)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38013_1912', null, null, 'ES_38013_1912', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38012
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fasnia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fasnia', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38012', null, null, 'ES_38012', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38011
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Candelaria', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Candelaria', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38011', null, null, 'ES_38011', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38010
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Buenavista del Norte', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Buenavista del Norte', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38010', null, null, 'ES_38010', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38009
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Breña Baja', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Breña Baja', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38009', null, null, 'ES_38009', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38008
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Breña Alta', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Breña Alta', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38008', null, null, 'ES_38008', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38007
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Barlovento', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Barlovento', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38007', null, null, 'ES_38007', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38006
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arona', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arona', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38006', null, null, 'ES_38006', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38005
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arico', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arico', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38005', null, null, 'ES_38005', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38004
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arafo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arafo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38004', null, null, 'ES_38004', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38003
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alajeró', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alajeró', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38003', null, null, 'ES_38003', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38002
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Agulo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Agulo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38002', null, null, 'ES_38002', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 38001
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Adeje', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Adeje', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '38001', null, null, 'ES_38001', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35034
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Yaiza', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Yaiza', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35034', null, null, 'ES_35034', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35033
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Vega de San Mateo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Vega de San Mateo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35033', null, null, 'ES_35033', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35032
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valleseco', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valleseco', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35032', null, null, 'ES_35032', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35031
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valsequillo de Gran Canaria', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valsequillo de Gran Canaria', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35031', null, null, 'ES_35031', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35030
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tuineje', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tuineje', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35030', null, null, 'ES_35030', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35029
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tinajo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tinajo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35029', null, null, 'ES_35029', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35028
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tías', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tías', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35028', null, null, 'ES_35028', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35027
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Teror', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Teror', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35027', null, null, 'ES_35027', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35026
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Telde', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Telde', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35026', null, null, 'ES_35026', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35025
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tejeda', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tejeda', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35025', null, null, 'ES_35025', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35024
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Teguise', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Teguise', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35024', null, null, 'ES_35024', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35023
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa María de Guía de Gran Canaria', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa María de Guía de Gran Canaria', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35023', null, null, 'ES_35023', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35022
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Lucía de Tirajana', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Lucía de Tirajana', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35022', null, null, 'ES_35022', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35021
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Brígida', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Brígida', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35021', null, null, 'ES_35021', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35020
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Aldea de San Nicolás', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Aldea de San Nicolás', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35020', null, null, 'ES_35020', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35019
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Bartolomé de Tirajana', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Bartolomé de Tirajana', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35019', null, null, 'ES_35019', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35018
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Bartolomé', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Bartolomé', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35018', null, null, 'ES_35018', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35017
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puerto del Rosario', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puerto del Rosario', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35017', null, null, 'ES_35017', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35016
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Las Palmas de Gran Canaria', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Las Palmas de Gran Canaria', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35016', null, null, 'ES_35016', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35015
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pájara', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pájara', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35015', null, null, 'ES_35015', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35014
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Oliva', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Oliva', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35014', null, null, 'ES_35014', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35013
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Moya', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Moya', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35013', null, null, 'ES_35013', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35012
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Mogán', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Mogán', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35012', null, null, 'ES_35012', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35011
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ingenio', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ingenio', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35011', null, null, 'ES_35011', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35010
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Haría', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Haría', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35010', null, null, 'ES_35010', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35009
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Gáldar', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Gáldar', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35009', null, null, 'ES_35009', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35008
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Firgas', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Firgas', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35008', null, null, 'ES_35008', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35007
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Betancuria', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Betancuria', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35007', null, null, 'ES_35007', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35006
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arucas', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arucas', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35006', null, null, 'ES_35006', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35005
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Artenara', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Artenara', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35005', null, null, 'ES_35005', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35004
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arrecife', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arrecife', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35004', null, null, 'ES_35004', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35003
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Antigua', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Antigua', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35003', null, null, 'ES_35003', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35002
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Agüimes', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Agüimes', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35002', null, null, 'ES_35002', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 35001
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Agaete', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Agaete', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35001', null, null, 'ES_35001', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28903
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tres Cantos', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tres Cantos', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28903', null, null, 'ES_28903', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28902
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puentes Viejas', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puentes Viejas', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28902', null, null, 'ES_28902', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28901
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lozoyuela-Navas-Sieteiglesias', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lozoyuela-Navas-Sieteiglesias', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28901', null, null, 'ES_28901', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28183
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Zarzalejo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Zarzalejo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28183', null, null, 'ES_28183', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28182
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villavieja del Lozoya', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villavieja del Lozoya', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28182', null, null, 'ES_28182', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28181
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villaviciosa de Odón', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villaviciosa de Odón', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28181', null, null, 'ES_28181', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28180
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villarejo de Salvanés', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villarejo de Salvanés', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28180', null, null, 'ES_28180', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28179
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villar del Olmo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villar del Olmo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28179', null, null, 'ES_28179', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28178
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villanueva de Perales', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villanueva de Perales', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28178', null, null, 'ES_28178', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28177
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villanueva del Pardillo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villanueva del Pardillo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28177', null, null, 'ES_28177', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28176
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villanueva de la Cañada', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villanueva de la Cañada', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28176', null, null, 'ES_28176', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28175
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villamantilla', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villamantilla', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28175', null, null, 'ES_28175', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28174
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villamanta', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villamanta', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28174', null, null, 'ES_28174', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28173
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villamanrique de Tajo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villamanrique de Tajo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28173', null, null, 'ES_28173', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28172
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villalbilla', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villalbilla', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28172', null, null, 'ES_28172', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28171
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villa del Prado', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villa del Prado', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28171', null, null, 'ES_28171', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28170
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villaconejos', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Villaconejos', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28170', null, null, 'ES_28170', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28169
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Venturada', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Venturada', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28169', null, null, 'ES_28169', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28168
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Vellón (El)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Vellón (El)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28168', null, null, 'ES_28168', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28167
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Velilla de San Antonio', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Velilla de San Antonio', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28167', null, null, 'ES_28167', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28166
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valverde de Alcalá', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valverde de Alcalá', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28166', null, null, 'ES_28166', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28165
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valdilecha', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valdilecha', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28165', null, null, 'ES_28165', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28164
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valdetorres de Jarama', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valdetorres de Jarama', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28164', null, null, 'ES_28164', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28163
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valdepiélagos', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valdepiélagos', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28163', null, null, 'ES_28163', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28162
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valdeolmos-Alalpardo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valdeolmos-Alalpardo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28162', null, null, 'ES_28162', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28161
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valdemoro', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valdemoro', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28161', null, null, 'ES_28161', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28160
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valdemorillo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valdemorillo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28160', null, null, 'ES_28160', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28159
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valdemaqueda', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valdemaqueda', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28159', null, null, 'ES_28159', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28158
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valdemanco', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valdemanco', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28158', null, null, 'ES_28158', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28157
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valdelaguna', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valdelaguna', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28157', null, null, 'ES_28157', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28156
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valdeavero', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valdeavero', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28156', null, null, 'ES_28156', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28155
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valdaracete', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valdaracete', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28155', null, null, 'ES_28155', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28154
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Torres de la Alameda', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Torres de la Alameda', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28154', null, null, 'ES_28154', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28153
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Torremocha de Jarama', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Torremocha de Jarama', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28153', null, null, 'ES_28153', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28152
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Torrelodones', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Torrelodones', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28152', null, null, 'ES_28152', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28151
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Torrelaguna', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Torrelaguna', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28151', null, null, 'ES_28151', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28150
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Torrejón de Velasco', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Torrejón de Velasco', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28150', null, null, 'ES_28150', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28149
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Torrejón de la Calzada', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Torrejón de la Calzada', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28149', null, null, 'ES_28149', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28148
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Torrejón de Ardoz', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Torrejón de Ardoz', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28148', null, null, 'ES_28148', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28147
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Titulcia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Titulcia', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28147', null, null, 'ES_28147', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28146
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tielmes', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tielmes', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28146', null, null, 'ES_28146', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28145
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Talamanca de Jarama', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Talamanca de Jarama', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28145', null, null, 'ES_28145', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28144
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Soto del Real', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Soto del Real', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28144', null, null, 'ES_28144', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28143
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Somosierra', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Somosierra', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28143', null, null, 'ES_28143', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28141
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sevilla la Nueva', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sevilla la Nueva', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28141', null, null, 'ES_28141', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28140
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Serranillos del Valle', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Serranillos del Valle', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28140', null, null, 'ES_28140', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28138
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Serna del Monte (La)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Serna del Monte (La)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28138', null, null, 'ES_28138', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28137
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santos de la Humosa (Los)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santos de la Humosa (Los)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28137', null, null, 'ES_28137', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28136
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santorcaz', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santorcaz', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28136', null, null, 'ES_28136', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28135
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa María de la Alameda', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa María de la Alameda', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28135', null, null, 'ES_28135', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28134
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Sebastián de los Reyes', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Sebastián de los Reyes', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28134', null, null, 'ES_28134', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28133
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Martín de Valdeiglesias', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Martín de Valdeiglesias', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28133', null, null, 'ES_28133', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28132
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Martín de la Vega', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Martín de la Vega', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28132', null, null, 'ES_28132', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28131
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Lorenzo de El Escorial', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Lorenzo de El Escorial', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28131', null, null, 'ES_28131', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28130
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Fernando de Henares', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Fernando de Henares', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28130', null, null, 'ES_28130', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28129
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Agustín del Guadalix', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'San Agustín del Guadalix', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28129', null, null, 'ES_28129', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28128
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Rozas de Puerto Real', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Rozas de Puerto Real', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28128', null, null, 'ES_28128', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28127
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Rozas de Madrid (Las)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Rozas de Madrid (Las)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28127', null, null, 'ES_28127', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28126
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Robregordo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Robregordo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28126', null, null, 'ES_28126', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28125
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Robledo de Chavela', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Robledo de Chavela', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28125', null, null, 'ES_28125', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28124
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Robledillo de la Jara', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Robledillo de la Jara', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28124', null, null, 'ES_28124', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28123
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Rivas-Vaciamadrid', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Rivas-Vaciamadrid', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28123', null, null, 'ES_28123', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28122
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ribatejada', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ribatejada', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28122', null, null, 'ES_28122', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28121
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Redueña', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Redueña', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28121', null, null, 'ES_28121', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28120
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Rascafría', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Rascafría', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28120', null, null, 'ES_28120', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28119
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Quijorna', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Quijorna', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28119', null, null, 'ES_28119', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28118
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puebla de la Sierra', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puebla de la Sierra', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28118', null, null, 'ES_28118', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28117
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Prádena del Rincón', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Prádena del Rincón', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28117', null, null, 'ES_28117', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28116
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pozuelo del Rey', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pozuelo del Rey', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28116', null, null, 'ES_28116', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28115
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pozuelo de Alarcón', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pozuelo de Alarcón', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28115', null, null, 'ES_28115', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28114
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Piñuécar-Gandullas', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Piñuécar-Gandullas', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28114', null, null, 'ES_28114', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28113
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pinto', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pinto', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28113', null, null, 'ES_28113', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28112
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pinilla del Valle', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pinilla del Valle', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28112', null, null, 'ES_28112', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28111
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pezuela de las Torres', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pezuela de las Torres', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28111', null, null, 'ES_28111', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28110
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Perales de Tajuña', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Perales de Tajuña', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28110', null, null, 'ES_28110', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28109
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pelayos de la Presa', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pelayos de la Presa', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28109', null, null, 'ES_28109', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28108
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pedrezuela', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pedrezuela', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28108', null, null, 'ES_28108', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28107
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Patones', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Patones', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28107', null, null, 'ES_28107', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28106
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Parla', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Parla', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28106', null, null, 'ES_28106', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28104
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Paracuellos de Jarama', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Paracuellos de Jarama', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28104', null, null, 'ES_28104', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28102
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Orusco de Tajuña', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Orusco de Tajuña', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28102', null, null, 'ES_28102', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28101
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Olmeda de las Fuentes', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Olmeda de las Fuentes', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28101', null, null, 'ES_28101', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28100
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Nuevo Baztán', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Nuevo Baztán', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28100', null, null, 'ES_28100', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28099
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Navas del Rey', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Navas del Rey', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28099', null, null, 'ES_28099', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28097
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Navarredonda y San Mamés', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Navarredonda y San Mamés', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28097', null, null, 'ES_28097', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28096
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Navalcarnero', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Navalcarnero', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28096', null, null, 'ES_28096', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28095
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Navalagamella', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Navalagamella', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28095', null, null, 'ES_28095', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28094
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Navalafuente', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Navalafuente', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28094', null, null, 'ES_28094', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28093
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Navacerrada', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Navacerrada', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28093', null, null, 'ES_28093', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28092
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Móstoles', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Móstoles', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28092', null, null, 'ES_28092', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28091
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Morata de Tajuña', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Morata de Tajuña', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28091', null, null, 'ES_28091', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28090
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Moralzarzal', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Moralzarzal', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28090', null, null, 'ES_28090', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28089
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Moraleja de Enmedio', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Moraleja de Enmedio', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28089', null, null, 'ES_28089', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28088
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Montejo de la Sierra', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Montejo de la Sierra', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28088', null, null, 'ES_28088', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28087
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Molinos (Los)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Molinos (Los)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28087', null, null, 'ES_28087', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28086
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Molar (El)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Molar (El)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28086', null, null, 'ES_28086', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28085
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Miraflores de la Sierra', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Miraflores de la Sierra', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28085', null, null, 'ES_28085', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28084
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Mejorada del Campo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Mejorada del Campo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28084', null, null, 'ES_28084', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28083
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Meco', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Meco', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28083', null, null, 'ES_28083', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28082
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Manzanares el Real', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Manzanares el Real', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28082', null, null, 'ES_28082', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28080
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Majadahonda', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Majadahonda', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28080', null, null, 'ES_28080', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28079
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Madrid', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Madrid', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28079', null, null, 'ES_28079', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28078
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Madarcos', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Madarcos', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28078', null, null, 'ES_28078', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28076
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lozoya', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lozoya', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28076', null, null, 'ES_28076', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28075
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Loeches', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Loeches', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28075', null, null, 'ES_28075', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28074
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Leganés', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Leganés', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28074', null, null, 'ES_28074', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28073
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Humanes de Madrid', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Humanes de Madrid', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28073', null, null, 'ES_28073', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28072
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Hoyo de Manzanares', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Hoyo de Manzanares', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28072', null, null, 'ES_28072', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28071
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Horcajuelo de la Sierra', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Horcajuelo de la Sierra', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28071', null, null, 'ES_28071', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28070
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Horcajo de la Sierra-Aoslos', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Horcajo de la Sierra-Aoslos', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28070', null, null, 'ES_28070', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28069
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Hiruela (La)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Hiruela (La)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28069', null, null, 'ES_28069', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28068
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Guadarrama', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Guadarrama', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28068', null, null, 'ES_28068', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28067
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Guadalix de la Sierra', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Guadalix de la Sierra', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28067', null, null, 'ES_28067', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28066
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Griñón', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Griñón', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28066', null, null, 'ES_28066', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28065
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Getafe', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Getafe', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28065', null, null, 'ES_28065', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28064
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Gascones', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Gascones', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28064', null, null, 'ES_28064', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28063
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Gargantilla del Lozoya y Pinilla de Buitrago', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Gargantilla del Lozoya y Pinilla de Buitrago', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28063', null, null, 'ES_28063', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28062
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Garganta de los Montes', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Garganta de los Montes', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28062', null, null, 'ES_28062', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28061
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Galapagar', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Galapagar', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28061', null, null, 'ES_28061', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28060
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fuentidueña de Tajo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fuentidueña de Tajo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28060', null, null, 'ES_28060', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28059
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fuente el Saz de Jarama', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fuente el Saz de Jarama', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28059', null, null, 'ES_28059', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28058
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fuenlabrada', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fuenlabrada', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28058', null, null, 'ES_28058', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28057
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fresno de Torote', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fresno de Torote', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28057', null, null, 'ES_28057', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28056
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fresnedillas de la Oliva', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fresnedillas de la Oliva', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28056', null, null, 'ES_28056', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28055
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Estremera', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Estremera', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28055', null, null, 'ES_28055', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28054
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Escorial (El)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Escorial (El)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28054', null, null, 'ES_28054', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28053
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Daganzo de Arriba', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Daganzo de Arriba', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28053', null, null, 'ES_28053', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28052
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Chinchón', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Chinchón', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28052', null, null, 'ES_28052', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28051
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Chapinería', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Chapinería', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28051', null, null, 'ES_28051', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28050
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cubas de la Sagra', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cubas de la Sagra', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28050', null, null, 'ES_28050', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28049
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Coslada', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Coslada', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28049', null, null, 'ES_28049', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28048
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Corpa', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Corpa', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28048', null, null, 'ES_28048', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28047
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Collado Villalba', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Collado Villalba', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28047', null, null, 'ES_28047', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28046
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Collado Mediano', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Collado Mediano', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28046', null, null, 'ES_28046', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28045
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Colmenar Viejo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Colmenar Viejo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28045', null, null, 'ES_28045', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28044
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Colmenarejo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Colmenarejo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28044', null, null, 'ES_28044', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28043
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Colmenar de Oreja', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Colmenar de Oreja', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28043', null, null, 'ES_28043', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28042
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Colmenar del Arroyo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Colmenar del Arroyo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28042', null, null, 'ES_28042', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28041
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cobeña', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cobeña', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28041', null, null, 'ES_28041', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28040
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ciempozuelos', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ciempozuelos', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28040', null, null, 'ES_28040', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28039
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cervera de Buitrago', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cervera de Buitrago', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28039', null, null, 'ES_28039', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28038
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cercedilla', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cercedilla', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28038', null, null, 'ES_28038', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28037
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cenicientos', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cenicientos', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28037', null, null, 'ES_28037', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28036
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Casarrubuelos', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Casarrubuelos', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28036', null, null, 'ES_28036', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28035
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Carabaña', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Carabaña', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28035', null, null, 'ES_28035', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28034
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Canencia', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Canencia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28034', null, null, 'ES_28034', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28033
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Campo Real', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Campo Real', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28033', null, null, 'ES_28033', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28032
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Camarma de Esteruelas', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Camarma de Esteruelas', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28032', null, null, 'ES_28032', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28031
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cadalso de los Vidrios', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cadalso de los Vidrios', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28031', null, null, 'ES_28031', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28030
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cabrera (La)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cabrera (La)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28030', null, null, 'ES_28030', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28029
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cabanillas de la Sierra', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cabanillas de la Sierra', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28029', null, null, 'ES_28029', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28028
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Bustarviejo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Bustarviejo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28028', null, null, 'ES_28028', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28027
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Buitrago del Lozoya', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Buitrago del Lozoya', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28027', null, null, 'ES_28027', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28026
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Brunete', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Brunete', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28026', null, null, 'ES_28026', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28025
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Brea de Tajo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Brea de Tajo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28025', null, null, 'ES_28025', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28024
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Braojos', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Braojos', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28024', null, null, 'ES_28024', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28023
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Boalo (El)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Boalo (El)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28023', null, null, 'ES_28023', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28022
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Boadilla del Monte', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Boadilla del Monte', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28022', null, null, 'ES_28022', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28021
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Berrueco (El)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Berrueco (El)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28021', null, null, 'ES_28021', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28020
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Berzosa del Lozoya', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Berzosa del Lozoya', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28020', null, null, 'ES_28020', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28019
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Belmonte de Tajo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Belmonte de Tajo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28019', null, null, 'ES_28019', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28018
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Becerril de la Sierra', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Becerril de la Sierra', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28018', null, null, 'ES_28018', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28017
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Batres', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Batres', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28017', null, null, 'ES_28017', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28016
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Atazar (El)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Atazar (El)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28016', null, null, 'ES_28016', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28015
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arroyomolinos', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arroyomolinos', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28015', null, null, 'ES_28015', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28014
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arganda del Rey', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Arganda del Rey', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28014', null, null, 'ES_28014', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28013
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Aranjuez', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Aranjuez', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28013', null, null, 'ES_28013', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28012
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Anchuelo', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Anchuelo', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28012', null, null, 'ES_28012', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28011
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ambite', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ambite', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28011', null, null, 'ES_28011', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28010
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alpedrete', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alpedrete', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28010', null, null, 'ES_28010', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28009
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Algete', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Algete', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28009', null, null, 'ES_28009', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28008
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Aldea del Fresno', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Aldea del Fresno', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28008', null, null, 'ES_28008', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28007
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alcorcón', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alcorcón', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28007', null, null, 'ES_28007', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28006
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alcobendas', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alcobendas', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28006', null, null, 'ES_28006', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28005
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alcalá de Henares', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alcalá de Henares', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28005', null, null, 'ES_28005', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28004
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Álamo (El)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Álamo (El)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28004', null, null, 'ES_28004', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28003
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alameda del Valle', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alameda del Valle', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28003', null, null, 'ES_28003', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28002
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ajalvir', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ajalvir', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28002', null, null, 'ES_28002', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 28001
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Acebeda (La)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Acebeda (La)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '28001', null, null, 'ES_28001', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07902
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Es Migjorn Gran', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Es Migjorn Gran', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07902', null, null, 'ES_07902', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07901
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ariany', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ariany', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07901', null, null, 'ES_07901', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07065
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Vilafranca de Bonany', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Vilafranca de Bonany', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07065', null, null, 'ES_07065', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07064
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Es Castell', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Es Castell', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07064', null, null, 'ES_07064', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07063
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valldemossa', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Valldemossa', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07063', null, null, 'ES_07063', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07062
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Son Servera', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Son Servera', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07062', null, null, 'ES_07062', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07061
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sóller', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sóller', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07061', null, null, 'ES_07061', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07060
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sineu', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sineu', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07060', null, null, 'ES_07060', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07059
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ses Salines', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ses Salines', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07059', null, null, 'ES_07059', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07058
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Selva', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Selva', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07058', null, null, 'ES_07058', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07057
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santanyí', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santanyí', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07057', null, null, 'ES_07057', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07056
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Maria del Camí', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Maria del Camí', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07056', null, null, 'ES_07056', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07055
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Margalida', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Margalida', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07055', null, null, 'ES_07055', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07054
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Eulària del Ríu', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Eulària del Ríu', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07054', null, null, 'ES_07054', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07053
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Eugènia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Santa Eugènia', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07053', null, null, 'ES_07053', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07052
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Lluís', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Lluís', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07052', null, null, 'ES_07052', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07051
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Llorenç des Cardassar', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Llorenç des Cardassar', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07051', null, null, 'ES_07051', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07050
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Joan de Labritja', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Joan de Labritja', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07050', null, null, 'ES_07050', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07049
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Joan', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Joan', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07049', null, null, 'ES_07049', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07048
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Josep de sa Talaia', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Josep de sa Talaia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07048', null, null, 'ES_07048', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07047
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sencelles', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sencelles', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07047', null, null, 'ES_07047', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07046
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Antoni de Portmany', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sant Antoni de Portmany', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07046', null, null, 'ES_07046', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07045
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puigpunyent', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Puigpunyent', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07045', null, null, 'ES_07045', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07044
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sa Pobla', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Sa Pobla', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07044', null, null, 'ES_07044', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07043
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Porreres', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Porreres', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07043', null, null, 'ES_07043', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07042
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pollença', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Pollença', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07042', null, null, 'ES_07042', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07041
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Petra', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Petra', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07041', null, null, 'ES_07041', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07040
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Palma', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Palma', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07040', null, null, 'ES_07040', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07039
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Muro', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Muro', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07039', null, null, 'ES_07039', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07038
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Montuïri', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Montuïri', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07038', null, null, 'ES_07038', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07037
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Es Mercadal', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Es Mercadal', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07037', null, null, 'ES_07037', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07036
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Marratxí', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Marratxí', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07036', null, null, 'ES_07036', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07035
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Maria de la Salut', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Maria de la Salut', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07035', null, null, 'ES_07035', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07034
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Mancor de la Vall', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Mancor de la Vall', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07034', null, null, 'ES_07034', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07033
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Manacor', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Manacor', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07033', null, null, 'ES_07033', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07032
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Maó', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Maó', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07032', null, null, 'ES_07032', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07031
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Llucmajor', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Llucmajor', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07031', null, null, 'ES_07031', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07030
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Llubí', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Llubí', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07030', null, null, 'ES_07030', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07029
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lloseta', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lloseta', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07029', null, null, 'ES_07029', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07028
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lloret de Vistalegre', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lloret de Vistalegre', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07028', null, null, 'ES_07028', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07027
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Inca', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Inca', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07027', null, null, 'ES_07027', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07026
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ibiza', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Eivissa', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07026', null, null, 'ES_07026', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07025
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fornalutx', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fornalutx', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07025', null, null, 'ES_07025', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07024
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Formentera', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Formentera', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07024', null, null, 'ES_07024', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07023
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ferreries', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ferreries', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07023', null, null, 'ES_07023', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07022
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Felanitx', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Felanitx', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07022', null, null, 'ES_07022', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07021
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Estellencs', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Estellencs', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07021', null, null, 'ES_07021', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07020
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Esporles', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Esporles', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07020', null, null, 'ES_07020', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07019
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Escorca', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Escorca', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07019', null, null, 'ES_07019', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07018
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Deià', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Deià', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07018', null, null, 'ES_07018', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07017
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Costitx', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Costitx', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07017', null, null, 'ES_07017', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07016
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Consell', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Consell', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07016', null, null, 'ES_07016', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07015
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ciutadella de Menorca', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ciutadella de Menorca', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07015', null, null, 'ES_07015', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07014
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Capdepera', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Capdepera', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07014', null, null, 'ES_07014', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07013
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Campos', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Campos', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07013', null, null, 'ES_07013', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07012
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Campanet', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Campanet', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07012', null, null, 'ES_07012', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07011
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Calvià', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Calvià', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07011', null, null, 'ES_07011', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07010
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Bunyola', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Bunyola', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07010', null, null, 'ES_07010', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07009
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Búger', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Búger', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07009', null, null, 'ES_07009', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07008
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Binissalem', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Binissalem', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07008', null, null, 'ES_07008', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07007
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Banyalbufar', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Banyalbufar', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07007', null, null, 'ES_07007', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07006
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Artà', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Artà', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07006', null, null, 'ES_07006', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07005
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Andratx', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Andratx', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07005', null, null, 'ES_07005', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07004
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Algaida', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Algaida', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07004', null, null, 'ES_07004', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07003
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alcúdia', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alcúdia', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07003', null, null, 'ES_07003', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07002
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alaior', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Alaior', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '07002', null, null, 'ES_07002', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: MUNICIPALITIES Valor geografico: 07001
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
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
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tenerife', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tenerife', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES709', null, null, 'ES_ES709', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES708
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lanzarote', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Lanzarote', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES708', null, null, 'ES_ES708', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES707
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Palma', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Palma', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES707', null, null, 'ES_ES707', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES706_ES709
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Gomera and Tenerife', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Gomera y Tenerife', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES706_ES709', null, null, 'ES_ES706_ES709', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES706_ES703
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Gomera and El Hierro', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Gomera y El Hierro', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES706_ES703', null, null, 'ES_ES706_ES703', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES706
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Gomera', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Gomera', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES706', null, null, 'ES_ES706', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES705
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Gran Canaria', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Gran Canaria', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES705', null, null, 'ES_ES705', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES704
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fuerteventura', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Fuerteventura', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES704', null, null, 'ES_ES704', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES703
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Hierro', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'El Hierro', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES703', null, null, 'ES_ES703', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES533
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Menorca', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Minorca', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES533', null, null, 'ES_ES533', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES532
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Mallorca', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Majorca', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES532', null, null, 'ES_ES532', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES531_072
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ibiza', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Eivissa', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES531_072', null, null, 'ES_ES531_072', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES531_071
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Formentera', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Formentera', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES531_071', null, null, 'ES_ES531_071', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: ES531
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Eivissa y Formentera', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Ibiza y Formentera', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES531', null, null, 'ES_ES531', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ISLANDS Valor geografico: 35024_UP002500
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Graciosa', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'La Graciosa', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), '35024_UP002500', null, null, 'ES_35024_UP002500', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
     
-- Granularidad geografica: ECONOMIC_ZONES
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Economic zones', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Zonas económicas', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Zones econòmiques', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LIS_GEOGR_GRANULARITIES (VERSION, ID, CODE, UUID, TITLE_FK) values (0, nextval('SEQ_GEOGR_GRANULARITIES'), 'ECONOMIC_ZONES', uuid_generate_v4(), currval('SEQ_I18NSTRS'));

     -- Granularidad geografica: ECONOMIC_ZONES Valor geografico: EUROPE_XEU
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Europa (excluida la Unión Europea)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Europe (European Union excluded)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'EUROPE_XEU', null, null, 'ES_EUROPE_XEU', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ECONOMIC_ZONES Valor geografico: EU28_XES
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Unión Europea - 28 (excluida España)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'European Union - 28 (Spain excluded)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'EU28_XES', null, null, 'ES_EU28_XES', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ECONOMIC_ZONES Valor geografico: EU27_2020_XES
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'European Union - 27 (Spain excluded)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Unión Europea - 27 (excluida España)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'EU27_2020_XES', null, null, 'ES_EU27_2020_XES', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ECONOMIC_ZONES Valor geografico: EU27_2007_XES
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'European Union - 27 (2007 - 2013) - (Spain excluded)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Unión Europea - 27 (2007 - 2013) - (excluida España)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'EU27_2007_XES', null, null, 'ES_EU27_2007_XES', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ECONOMIC_ZONES Valor geografico: EU25_XES
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'European Union - 25 (Spain excluded)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Unión Europea - 25 (excluida España)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'EU25_XES', null, null, 'ES_EU25_XES', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ECONOMIC_ZONES Valor geografico: EU15_XES
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Unión Europea - 15 (excluida España)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'European Union - 15 (Spain excluded)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'EU15_XES', null, null, 'ES_EU15_XES', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ECONOMIC_ZONES Valor geografico: EU12_XES
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Unión Europea - 12 (excluida España)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'European Union - 12 (Spain excluded)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'EU12_XES', null, null, 'ES_EU12_XES', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ECONOMIC_ZONES Valor geografico: EEC6
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Comunidad Económica Europea - 6', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'European Economic Community - 6', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'EEC6', null, null, 'ES_EEC6', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ECONOMIC_ZONES Valor geografico: EC9
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Comunidad Europea - 9', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'European Community - 9', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'EC9', null, null, 'ES_EC9', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ECONOMIC_ZONES Valor geografico: EC12_XES
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'European Community - 12 (Spain excluded)', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Comunidad Europea - 12 (excluida España)', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'EC12_XES', null, null, 'ES_EC12_XES', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
     -- Granularidad geografica: ECONOMIC_ZONES Valor geografico: EC10
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Comunidad Europea - 10', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'European Community - 10', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'EC10', null, null, 'ES_EC10', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));
 
-- Granularidad geografica: COUNTRIES
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Countries', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Países', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Països', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LIS_GEOGR_GRANULARITIES (VERSION, ID, CODE, UUID, TITLE_FK) values (0, nextval('SEQ_GEOGR_GRANULARITIES'), 'COUNTRIES', uuid_generate_v4(), currval('SEQ_I18NSTRS'));

     -- Granularidad geografica: COUNTRIES Valor geografico: ES
     INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'España', 'es', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Spain', 'en', currval('SEQ_I18NSTRS'), 1);
     INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, nextval('SEQ_GEOGR_VALUES'), 'ES', null, null, 'ES_ES', uuid_generate_v4(), currval('SEQ_I18NSTRS'), currval('SEQ_GEOGR_GRANULARITIES'));

commit;