-- ---------------------------------------------------------------------------------------------------
-- EDATOS-4185 - Añadir metadato elemento de tema a los indicadores
-- 
-- Script para realizar la migración de las áreas temáticas a elementos de tema provenientes del srm en PRE IESTADIS
-- Se obtienen las sentencias a ejecutar a partir de una consulta.
-- ---------------------------------------------------------------------------------------------------


-- 1 Población
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '1 - Población' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '1 - Population' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''POBLACION'', ''/latest/categoryelements/POBLACION'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=POBLACION'', ''/#structuralResources/categoryElement;id=POBLACION'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '01';

-- 1.1 Evolución y estructura de la población

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '1.1 - Evolución y estructura de la población' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '1.1 - Evolution and structure of the population' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''EVOLUCION_ESTRUCTURA_POBLACION'', ''/latest/categoryelements/EVOLUCION_ESTRUCTURA_POBLACION'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=EVOLUCION_ESTRUCTURA_POBLACION'', ''/#structuralResources/categoryElement;id=EVOLUCION_ESTRUCTURA_POBLACION'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '01_01';

-- 1.2 Censos de población y viviendas

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '1.2 - Censos de población y viviendas' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '1.2 - Population and housing census' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''CENSOS_POBLACION_VIVIENDAS'', ''/latest/categoryelements/CENSOS_POBLACION_VIVIENDAS'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=CENSOS_POBLACION_VIVIENDAS'', ''/#structuralResources/categoryElement;id=CENSOS_POBLACION_VIVIENDAS'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '01_02';


-- 1.3 Hogares y familias

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '1.3 - Hogares y familias' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '1.3 - Households and families' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''HOGARES_FAMILIAS'', ''/latest/categoryelements/HOGARES_FAMILIAS'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=HOGARES_FAMILIAS'', ''/#structuralResources/categoryElement;id=HOGARES_FAMILIAS'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '01_03';

-- 1.4 Nacimientos y defunciones

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '1.4 - Nacimientos y defunciones' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '1.4 - Births and deaths' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''NACIMIENTOS_DEFUNCIONES'', ''/latest/categoryelements/NACIMIENTOS_DEFUNCIONES'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=NACIMIENTOS_DEFUNCIONES'', ''/#structuralResources/categoryElement;id=NACIMIENTOS_DEFUNCIONES'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '01_04';

-- 1.5 Migraciones

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '1.5 - Migraciones' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '1.5 - Migrations' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''MIGRACIONES'', ''/latest/categoryelements/MIGRACIONES'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=MIGRACIONES'', ''/#structuralResources/categoryElement;id=MIGRACIONES'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '01_05';

-- 1.6 Población extranjera

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '1.6 - Población extranjera' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '1.6 - Foreign population' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''POBLACION_EXTRANJERA'', ''/latest/categoryelements/POBLACION_EXTRANJERA'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=POBLACION_EXTRANJERA'', ''/#structuralResources/categoryElement;id=POBLACION_EXTRANJERA'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '01_06';

-- 1.7 Movilidad y población estacional

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '1.7 - Movilidad y población estacional' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '1.7 - Mobility and seasonal population' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''MOVILIDAD_POBLACION_ESTACIONAL'', ''/latest/categoryelements/MOVILIDAD_POBLACION_ESTACIONAL'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=MOVILIDAD_POBLACION_ESTACIONAL'', ''/#structuralResources/categoryElement;id=MOVILIDAD_POBLACION_ESTACIONAL'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '01_07';

-- 1.8 Proyecciones de población

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '1.8 - Proyecciones de población' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '1.8 - Population projections' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''PROYECCIONES_POBLACION'', ''/latest/categoryelements/PROYECCIONES_POBLACION'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=PROYECCIONES_POBLACION'', ''/#structuralResources/categoryElement;id=PROYECCIONES_POBLACION'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '01_08';

-- 1.9 Onomástica

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '1.9 - Onomástica' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '1.9 - Onomastics' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''ONOMASTICA'', ''/latest/categoryelements/ONOMASTICA'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=ONOMASTICA'', ''/#structuralResources/categoryElement;id=ONOMASTICA'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '01_09';

-- 2 Economía

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '2 - Economía' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '2 - Economy' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''ECONOMIA'', ''/latest/categoryelements/ECONOMIA'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=ECONOMIA'', ''/#structuralResources/categoryElement;id=ECONOMIA'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '02';

-- 2.1 Cuentas económicas

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '2.1 - Cuentas económicas' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '2.1 - Economic accounts' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''CUENTAS_ECONOMICAS'', ''/latest/categoryelements/CUENTAS_ECONOMICAS'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=CUENTAS_ECONOMICAS'', ''/#structuralResources/categoryElement;id=CUENTAS_ECONOMICAS'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '02_01';

-- 2.2 Empresas. Estructura

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '2.2 - Empresas. Estructura' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '2.2 - Companies. Structure' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''EMPRESAS_ESTRUCTURA'', ''/latest/categoryelements/EMPRESAS_ESTRUCTURA'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=EMPRESAS_ESTRUCTURA'', ''/#structuralResources/categoryElement;id=EMPRESAS_ESTRUCTURA'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '02_02';

-- 2.3 Coyuntura económica

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '2.3 - Coyuntura económica' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '2.3 - Economic situation' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''COYUNTURA_ECONOMICA'', ''/latest/categoryelements/COYUNTURA_ECONOMICA'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=COYUNTURA_ECONOMICA'', ''/#structuralResources/categoryElement;id=COYUNTURA_ECONOMICA'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '02_03';

-- 2.4 Precios

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '2.4 - Precios' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '2.4 - Prices' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''PRECIOS'', ''/latest/categoryelements/PRECIOS'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=PRECIOS'', ''/#structuralResources/categoryElement;id=PRECIOS'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '02_04';

-- 2.5 Sector exterior

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '2.5 - Sector exterior' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '2.5 - Foreign sector' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''SECTOR_EXTERIOR'', ''/latest/categoryelements/SECTOR_EXTERIOR'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=SECTOR_EXTERIOR'', ''/#structuralResources/categoryElement;id=SECTOR_EXTERIOR'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '02_05';

-- 2.6 Inversión

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '2.6 - Inversión' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '2.6 - Investment' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''INVERSION'', ''/latest/categoryelements/INVERSION'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=INVERSION'', ''/#structuralResources/categoryElement;id=INVERSION'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '02_06';


-- 2.7 I+D+I

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '2.7 - I+D+I' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '2.7 - R+D+I' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''IDI'', ''/latest/categoryelements/IDI'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=IDI'', ''/#structuralResources/categoryElement;id=IDI'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '02_07';

-- 2.8 TIC

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '2.8 - TIC' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '2.8 - ICT' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''TIC'', ''/latest/categoryelements/TIC'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=TIC'', ''/#structuralResources/categoryElement;id=TIC'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '02_08';

-- 2.9 Fiscalidad

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '2.9 - Fiscalidad' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '2.9 - Taxation' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''FISCALIDAD'', ''/latest/categoryelements/FISCALIDAD'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=FISCALIDAD'', ''/#structuralResources/categoryElement;id=FISCALIDAD'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '02_09';


-- 3 Sectores económicos

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '3 - Sectores económicos' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '3 - Economic sectors' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''SECTORES_ECONOMICOS'', ''/latest/categoryelements/SECTORES_ECONOMICOS'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=SECTORES_ECONOMICOS'', ''/#structuralResources/categoryElement;id=SECTORES_ECONOMICOS'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '03';

-- 3.1 Sector agrario

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '3.1 - Sector agrario' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '3.1 - Agricultural sector' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''SECTOR_AGRARIO'', ''/latest/categoryelements/SECTOR_AGRARIO'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=SECTOR_AGRARIO'', ''/#structuralResources/categoryElement;id=SECTOR_AGRARIO'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '03_01';

-- 3.2 Energía

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '3.2 - Energía' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '3.2 - Energy' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''ENERGIA'', ''/latest/categoryelements/ENERGIA'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=ENERGIA'', ''/#structuralResources/categoryElement;id=ENERGIA'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '03_02';

-- 3.3 Industria

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '3.3 - Industria' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '3.3 - Industry' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''INDUSTRIA'', ''/latest/categoryelements/INDUSTRIA'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=INDUSTRIA'', ''/#structuralResources/categoryElement;id=INDUSTRIA'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '03_03';

-- 3.4 Construcción

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '3.4 - Construcción' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '3.4 - Construction' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''CONSTRUCCION'', ''/latest/categoryelements/CONSTRUCCION'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=CONSTRUCCION'', ''/#structuralResources/categoryElement;id=CONSTRUCCION'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '03_04';

-- 3.5 Comercio

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '3.5 - Comercio' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '3.5 - Commerce' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''COMERCIO'', ''/latest/categoryelements/COMERCIO'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=COMERCIO'', ''/#structuralResources/categoryElement;id=COMERCIO'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '03_05';

-- 3.6 Servicios

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '3.6 - Servicios' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '3.6 - Services' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''SERVICIOS'', ''/latest/categoryelements/SERVICIOS'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=SERVICIOS'', ''/#structuralResources/categoryElement;id=SERVICIOS'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '03_06';

-- 3.7 Transporte

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '3.7 - Transporte' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '3.7 - Transport' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''TRANSPORTE'', ''/latest/categoryelements/TRANSPORTE'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=TRANSPORTE'', ''/#structuralResources/categoryElement;id=TRANSPORTE'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '03_07';

-- 3.8 Turismo

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '3.8 - Turismo' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '3.8 - Tourism' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''TURISMO'', ''/latest/categoryelements/TURISMO'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=TURISMO'', ''/#structuralResources/categoryElement;id=TURISMO'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '03_08';

-- 3.9 Sector financiero

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '3.9 - Sector financiero' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '3.9 - Financial sector' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''SECTOR_FINANCIERO'', ''/latest/categoryelements/SECTOR_FINANCIERO'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=SECTOR_FINANCIERO'', ''/#structuralResources/categoryElement;id=SECTOR_FINANCIERO'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '03_09';

-- 3.10 Sector público

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '3.10 - Sector público' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '3.10 - Public sector' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''SECTOR_PUBLICO'', ''/latest/categoryelements/SECTOR_PUBLICO'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=SECTOR_PUBLICO'', ''/#structuralResources/categoryElement;id=SECTOR_PUBLICO'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '03_10';

-- 4 Trabajo y sociedad

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '4 - Trabajo y sociedad' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '4 - Work and society' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''TRABAJO_SOCIEDAD'', ''/latest/categoryelements/TRABAJO_SOCIEDAD'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=TRABAJO_SOCIEDAD'', ''/#structuralResources/categoryElement;id=TRABAJO_SOCIEDAD'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '04';

-- 4.1 Relación con la actividad

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '4.1 - Relación con la actividad' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '4.1 - Relationship with activity' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''RELACION_CON_ACTIVIDAD'', ''/latest/categoryelements/RELACION_CON_ACTIVIDAD'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=RELACION_CON_ACTIVIDAD'', ''/#structuralResources/categoryElement;id=RELACION_CON_ACTIVIDAD'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '04_01';


-- 4.2 Coste laboral y salarios

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '4.2 - Coste laboral y salarios' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '4.2 - Labor costs and wages' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''COSTE_LABORAL_SALARIOS'', ''/latest/categoryelements/COSTE_LABORAL_SALARIOS'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=COSTE_LABORAL_SALARIOS'', ''/#structuralResources/categoryElement;id=COSTE_LABORAL_SALARIOS'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '04_02';

-- 4.3 Condiciones laborales

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '4.3 - Condiciones laborales' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '4.3 - Labor conditions' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''CONDICIONES_LABORALES'', ''/latest/categoryelements/CONDICIONES_LABORALES'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=CONDICIONES_LABORALES'', ''/#structuralResources/categoryElement;id=CONDICIONES_LABORALES'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '04_03';


-- 4.4 Educación y formación (incluye formación continua en el trabajo)

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '4.4 - Educación y formación (incluye formación continua en el trabajo)' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '4.4 - Education and training (including continuing training at work)' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''EDUCACION_FORMACION'', ''/latest/categoryelements/EDUCACION_FORMACION'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=EDUCACION_FORMACION'', ''/#structuralResources/categoryElement;id=EDUCACION_FORMACION'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '04_04';

-- 4.5 Uso del tiempo

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '4.5 - Uso del tiempo' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '4.5 - Use of time' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''USO_TIEMPO'', ''/latest/categoryelements/USO_TIEMPO'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=USO_TIEMPO'', ''/#structuralResources/categoryElement;id=USO_TIEMPO'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '04_05';

-- 4.6 Cultura

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '4.6 - Cultura' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '4.6 - Culture' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''CULTURA'', ''/latest/categoryelements/CULTURA'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=CULTURA'', ''/#structuralResources/categoryElement;id=CULTURA'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '04_06';


-- 4.7 Elecciones y participación ciudadana

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '4.7 - Elecciones y participación ciudadana' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '4.7 - Elections and citizen participation' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''ELECCIONES_PARTICIPACION_CIUDADANA'', ''/latest/categoryelements/ELECCIONES_PARTICIPACION_CIUDADANA'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=ELECCIONES_PARTICIPACION_CIUDADANA'', ''/#structuralResources/categoryElement;id=ELECCIONES_PARTICIPACION_CIUDADANA'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '04_07';

-- 4.8 Justicia y seguridad

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '4.8 - Justicia y seguridad' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '4.8 - Justice and security' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''JUSTICIA_SEGURIDAD'', ''/latest/categoryelements/JUSTICIA_SEGURIDAD'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=JUSTICIA_SEGURIDAD'', ''/#structuralResources/categoryElement;id=JUSTICIA_SEGURIDAD'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '04_08';

-- 4.9 Deportes

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '4.9 - Deportes' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '4.9 - Sport' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''DEPORTES'', ''/latest/categoryelements/DEPORTES'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=DEPORTES'', ''/#structuralResources/categoryElement;id=DEPORTES'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '04_09';

-- 4.10 Juventud

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '4.10 - Juventud' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '4.10 - Youth' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''JUVENTUD'', ''/latest/categoryelements/JUVENTUD'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=JUVENTUD'', ''/#structuralResources/categoryElement;id=JUVENTUD'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '04_10';

-- 5 Condiciones de vida y protección social

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '5 - Condiciones de vida y protección social' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '5 - Living conditions and social protection' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''CONDICIONES_VIDA_PROTECCION_SOCIAL'', ''/latest/categoryelements/CONDICIONES_VIDA_PROTECCION_SOCIAL'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=CONDICIONES_VIDA_PROTECCION_SOCIAL'', ''/#structuralResources/categoryElement;id=CONDICIONES_VIDA_PROTECCION_SOCIAL'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '05';

-- 5.1 Ingresos y condiciones de vida de los hogares

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '5.1 - Ingresos y condiciones de vida de los hogares' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '5.1 - Income and living conditions of households' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''INGRESOS_CONDICIONES_VIDA_HOGARES'', ''/latest/categoryelements/INGRESOS_CONDICIONES_VIDA_HOGARES'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=INGRESOS_CONDICIONES_VIDA_HOGARES'', ''/#structuralResources/categoryElement;id=INGRESOS_CONDICIONES_VIDA_HOGARES'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '05_01';

-- 5.2 Servicios sociales y protección social

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '5.2 - Servicios sociales y protección social' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '5.2 - Social services and social protection' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''SERVICIOS_SOCIALES_PROTECCION_SOCIAL'', ''/latest/categoryelements/SERVICIOS_SOCIALES_PROTECCION_SOCIAL'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=SERVICIOS_SOCIALES_PROTECCION_SOCIAL'', ''/#structuralResources/categoryElement;id=SERVICIOS_SOCIALES_PROTECCION_SOCIAL'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '05_02';

-- 5.3 Salud

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '5.3 - Salud' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '5.3 - Health' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''SALUD'', ''/latest/categoryelements/SALUD'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=SALUD'', ''/#structuralResources/categoryElement;id=SALUD'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '05_03';

-- 5.4 Servicios sanitarios

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '5.4 - Servicios sanitarios' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '5.4 - Health services' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''SERVICIOS_SANITARIOS'', ''/latest/categoryelements/SERVICIOS_SANITARIOS'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=SERVICIOS_SANITARIOS'', ''/#structuralResources/categoryElement;id=SERVICIOS_SANITARIOS'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '05_04';

-- 6 Territorio y medio ambiente

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '6 - Territorio y medio ambiente' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '6 - Territory and environment' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''TERRITORIO_MEDIO_AMBIENTE'', ''/latest/categoryelements/TERRITORIO_MEDIO_AMBIENTE'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=TERRITORIO_MEDIO_AMBIENTE'', ''/#structuralResources/categoryElement;id=TERRITORIO_MEDIO_AMBIENTE'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '06';

-- 6.1 Entorno físico y clima

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '6.1 - Entorno físico y clima' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '6.1 - Physical environment and climate' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''ENTORNO_FISICO_CLIMA'', ''/latest/categoryelements/ENTORNO_FISICO_CLIMA'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=ENTORNO_FISICO_CLIMA'', ''/#structuralResources/categoryElement;id=ENTORNO_FISICO_CLIMA'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '06_01';

-- 6.2 Medio ambiente

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '6.2 - Medio ambiente' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '6.2 - Environment' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''MEDIO_AMBIENTE'', ''/latest/categoryelements/MEDIO_AMBIENTE'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=MEDIO_AMBIENTE'', ''/#structuralResources/categoryElement;id=MEDIO_AMBIENTE'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '06_02';

-- 6.3 Divisiones geográficas y administrativas

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '6.3 - Divisiones geográficas y administrativas' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '6.3 - Geographical and administrative divisions' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''DIVISIONES_GEOGRAFICAS_ADMINISTRATIVAS'', ''/latest/categoryelements/DIVISIONES_GEOGRAFICAS_ADMINISTRATIVAS'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=DIVISIONES_GEOGRAFICAS_ADMINISTRATIVAS'', ''/#structuralResources/categoryElement;id=DIVISIONES_GEOGRAFICAS_ADMINISTRATIVAS'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '06_03';

-- 6.4 Usos del suelo y planificación

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '6.4 - Usos del suelo y planificación' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '6.4 - Uses of soil and planning' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''USOS_SUELO_PLANIFICACION'', ''/latest/categoryelements/USOS_SUELO_PLANIFICACION'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=USOS_SUELO_PLANIFICACION'', ''/#structuralResources/categoryElement;id=USOS_SUELO_PLANIFICACION'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '06_04';

-- 6.5 Viviendas, edificios y locales

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '6.5 - Viviendas, edificios y locales' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '6.5 - Houses, buildings and premises' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''VIVIENDAS_EDIFICIOS_LOCALES'', ''/latest/categoryelements/VIVIENDAS_EDIFICIOS_LOCALES'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=VIVIENDAS_EDIFICIOS_LOCALES'', ''/#structuralResources/categoryElement;id=VIVIENDAS_EDIFICIOS_LOCALES'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '06_05';

-- 6.6 Infraestructuras

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '6.6 - Infraestructuras' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '6.6 - infrastructures' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''INFRAESTRUCTURAS'', ''/latest/categoryelements/INFRAESTRUCTURAS'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=INFRAESTRUCTURAS'', ''/#structuralResources/categoryElement;id=INFRAESTRUCTURAS'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '06_06';

-- 7 Metodología y normalización

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '7 - Metodología y normalización' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '7 - Methodology and standardization' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''METODOLOGIA_NORMALIZACION_NIVEL_1'', ''/latest/categoryelements/METODOLOGIA_NORMALIZACION_NIVEL_1'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=METODOLOGIA_NORMALIZACION_NIVEL_1'', ''/#structuralResources/categoryElement;id=METODOLOGIA_NORMALIZACION_NIVEL_1'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '07';

-- 7.1 Metodología y normalización

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '7.1 - Metodología y normalización' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '7.1 - Methodology and standardization' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''METODOLOGIA_NORMALIZACION_NIVEL_2'', ''/latest/categoryelements/METODOLOGIA_NORMALIZACION_NIVEL_2'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=METODOLOGIA_NORMALIZACION_NIVEL_2'', ''/#structuralResources/categoryElement;id=METODOLOGIA_NORMALIZACION_NIVEL_2'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '07_01';

-- 8 Registros estadísticos y directorios

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '8 - Registros estadísticos y directorios' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '8 - Statistical records and directories' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''REGISTROS_ESTADISTICOS_DIRECTORIOS_NIVEL_1'', ''/latest/categoryelements/REGISTROS_ESTADISTICOS_DIRECTORIOS_NIVEL_1'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=REGISTROS_ESTADISTICOS_DIRECTORIOS_NIVEL_1'', ''/#structuralResources/categoryElement;id=REGISTROS_ESTADISTICOS_DIRECTORIOS_NIVEL_1'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '08';

-- 8.1 Registros estadísticos y directorios

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '8.1 - Registros estadísticos y directorios' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '8.1 - Statistical records and directories' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''REGISTROS_ESTADISTICOS_DIRECTORIOS_NIVEL_2'', ''/latest/categoryelements/REGISTROS_ESTADISTICOS_DIRECTORIOS_NIVEL_2'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=REGISTROS_ESTADISTICOS_DIRECTORIOS_NIVEL_2'', ''/#structuralResources/categoryElement;id=REGISTROS_ESTADISTICOS_DIRECTORIOS_NIVEL_2'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '08_01';

-- 9 Georreferenciación y estadística de base territorial

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '9 - Georreferenciación y estadística de base territorial' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '9 - Georeferencing and territorial base statistics' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''GEORREFERENCIACION_ESTADISTICA_BASE_TERRITORIAL_NIVEL_1'', ''/latest/categoryelements/GEORREFERENCIACION_ESTADISTICA_BASE_TERRITORIAL_NIVEL_1'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=GEORREFERENCIACION_ESTADISTICA_BASE_TERRITORIAL_NIVEL_1'', ''/#structuralResources/categoryElement;id=GEORREFERENCIACION_ESTADISTICA_BASE_TERRITORIAL_NIVEL_1'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '09';

-- 9.1 Georreferenciación y estadística de base territorial

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '9.1 - Georreferenciación y estadística de base territorial' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '9.1 - Georeferencing and territorial base statistics' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''GEORREFERENCIACION_ESTADISTICA_BASE_TERRITORIAL_NIVEL_2'', ''/latest/categoryelements/GEORREFERENCIACION_ESTADISTICA_BASE_TERRITORIAL_NIVEL_2'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=GEORREFERENCIACION_ESTADISTICA_BASE_TERRITORIAL_NIVEL_2'', ''/#structuralResources/categoryElement;id=GEORREFERENCIACION_ESTADISTICA_BASE_TERRITORIAL_NIVEL_2'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '09_01';

-- 10 Indicadores y estadística de síntesis

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '10 - Indicadores y estadística de síntesis' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '10 - Indicators and statistical synthesis' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''INDICADORES_ESTADISTICA_SINTESIS'', ''/latest/categoryelements/INDICADORES_ESTADISTICA_SINTESIS'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=INDICADORES_ESTADISTICA_SINTESIS'', ''/#structuralResources/categoryElement;id=INDICADORES_ESTADISTICA_SINTESIS'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '10';

-- 10.1 Estadística de síntesis

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '10.1 - Estadística de síntesis' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '10.1 - Statistical synthesis' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''ESTADISTICA_SINTESIS'', ''/latest/categoryelements/ESTADISTICA_SINTESIS'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=ESTADISTICA_SINTESIS'', ''/#structuralResources/categoryElement;id=ESTADISTICA_SINTESIS'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '10_01';

-- 10.2 Sistemas de indicadores estadísticos

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '10.2 - Sistemas de indicadores estadísticos' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '10.2 - Systems of statistical indicators' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''SISTEMAS_INDICADORES_ESTADISTICOS'', ''/latest/categoryelements/SISTEMAS_INDICADORES_ESTADISTICOS'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=SISTEMAS_INDICADORES_ESTADISTICOS'', ''/#structuralResources/categoryElement;id=SISTEMAS_INDICADORES_ESTADISTICOS'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '10_02';

-- 11 Difusión y promoción de la estadística

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '11 - Difusión y promoción de la estadística' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '11 - Dissemination and promotion of statistics' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''DIFUSION_PROMOCION_ESTADISTICA_NIVEL_1'', ''/latest/categoryelements/DIFUSION_PROMOCION_ESTADISTICA_NIVEL_1'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=DIFUSION_PROMOCION_ESTADISTICA_NIVEL_1'', ''/#structuralResources/categoryElement;id=DIFUSION_PROMOCION_ESTADISTICA_NIVEL_1'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '11';

-- 11.1 Difusión y promoción de la estadística

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '11.1 - Difusión y promoción de la estadística' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '11.1 - Dissemination and promotion of statistics' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''DIFUSION_PROMOCION_ESTADISTICA_NIVEL_2'', ''/latest/categoryelements/DIFUSION_PROMOCION_ESTADISTICA_NIVEL_2'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=DIFUSION_PROMOCION_ESTADISTICA_NIVEL_2'', ''/#structuralResources/categoryElement;id=DIFUSION_PROMOCION_ESTADISTICA_NIVEL_2'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '11_01';