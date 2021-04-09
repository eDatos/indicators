-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- IBESTATNORMA-15 - Carga inicial indicadores
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- Fichero elaborado a partir de la clasificación SDMX:CL_UNIT_MULT(1.0)
-- Enlace. https://pre-ibestat.edatos.io/structural-resources-internal/#structuralResources/codelists/codelist;id=SDMX%3ACL_UNIT_MULT(1.0)
-- 
-- Notas:
-- 1. Es necesario tener instalado la extensión uuid-ossp para la generación de los uuids de los elementos insertados 
-- -- Para ello es necesario ejecutar la siguiente sentencia como administrador de la bbdd CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
-- 2. Los valores relativos a los trillones y los cuatrillones no se han cargado dado que actualmente la BBDD no soporta el tipo de dato a cargar
-- 3. Este script ha sido generado en base al script localizado en la ruta etc/helpers/03-generate-inserts-units-multipliers.sql dentro de este mismo proyecto.
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------

-- Multiplicador: 0
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Multiplicadores', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Units', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Unitats', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LIS_UNITS_MULTIPLIERS (VERSION, ID, UNIT_MULTIPLIER, UUID, TITLE_FK) values (0, nextval('SEQ_UNITS_MULTIPLIERS'), '1', uuid_generate_v4(), currval('SEQ_I18NSTRS'));
 
-- Multiplicador: 1
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tens', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Decenas', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Desenes', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LIS_UNITS_MULTIPLIERS (VERSION, ID, UNIT_MULTIPLIER, UUID, TITLE_FK) values (0, nextval('SEQ_UNITS_MULTIPLIERS'), '10', uuid_generate_v4(), currval('SEQ_I18NSTRS'));
 
-- Multiplicador: 2
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Centenes', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Centenas', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Hundreds', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LIS_UNITS_MULTIPLIERS (VERSION, ID, UNIT_MULTIPLIER, UUID, TITLE_FK) values (0, nextval('SEQ_UNITS_MULTIPLIERS'), '100', uuid_generate_v4(), currval('SEQ_I18NSTRS'));

-- Multiplicador: 3
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Miles', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Milers', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Thousands', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LIS_UNITS_MULTIPLIERS (VERSION, ID, UNIT_MULTIPLIER, UUID, TITLE_FK) values (0, nextval('SEQ_UNITS_MULTIPLIERS'), '1000', uuid_generate_v4(), currval('SEQ_I18NSTRS'));

-- Multiplicador: 4
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Decenas de miles', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tens of thousands', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Desenes de milers', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LIS_UNITS_MULTIPLIERS (VERSION, ID, UNIT_MULTIPLIER, UUID, TITLE_FK) values (0, nextval('SEQ_UNITS_MULTIPLIERS'), '10000', uuid_generate_v4(), currval('SEQ_I18NSTRS'));

-- Multiplicador: 6
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Milions', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Millones', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Millions', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LIS_UNITS_MULTIPLIERS (VERSION, ID, UNIT_MULTIPLIER, UUID, TITLE_FK) values (0, nextval('SEQ_UNITS_MULTIPLIERS'), '1000000', uuid_generate_v4(), currval('SEQ_I18NSTRS'));

-- Multiplicador: 9
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Billones', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Bilions', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Billions', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LIS_UNITS_MULTIPLIERS (VERSION, ID, UNIT_MULTIPLIER, UUID, TITLE_FK) values (0, nextval('SEQ_UNITS_MULTIPLIERS'), '1000000000', uuid_generate_v4(), currval('SEQ_I18NSTRS'));
 
-- Multiplicador: 12 NO SOPORTADO TIPO DE DATO EN BBDD INT4
-- INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Trillones', 'es', currval('SEQ_I18NSTRS'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Trilions', 'ca', currval('SEQ_I18NSTRS'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Trillions', 'en', currval('SEQ_I18NSTRS'), 1);
-- INSERT INTO TB_LIS_UNITS_MULTIPLIERS (VERSION, ID, UNIT_MULTIPLIER, UUID, TITLE_FK) values (0, nextval('SEQ_UNITS_MULTIPLIERS'), '1000000000000', uuid_generate_v4(), currval('SEQ_I18NSTRS'));
  
-- Multiplicador: 15 NO SOPORTADO TIPO DE DATO EN BBDD INT4
-- INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cuatrillones', 'es', currval('SEQ_I18NSTRS'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Quadrillions', 'en', currval('SEQ_I18NSTRS'), 1);
-- INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Quatrilions', 'ca', currval('SEQ_I18NSTRS'), 1);
-- INSERT INTO TB_LIS_UNITS_MULTIPLIERS (VERSION, ID, UNIT_MULTIPLIER, UUID, TITLE_FK) values (0, nextval('SEQ_UNITS_MULTIPLIERS'), '1000000000000000', uuid_generate_v4(), currval('SEQ_I18NSTRS'));
 
commit;