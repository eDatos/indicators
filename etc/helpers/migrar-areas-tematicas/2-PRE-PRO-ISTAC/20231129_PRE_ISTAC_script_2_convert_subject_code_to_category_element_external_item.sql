-- ---------------------------------------------------------------------------------------------------
-- EDATOS-4185 - Añadir metadato elemento de tema a los indicadores
-- 
-- Script para realizar la migración de las áreas temáticas a elementos de tema provenientes del srm en PRE, PRO ISTAC y para el entorno de demo
-- Se obtienen las sentencias a ejecutar a partir de una consulta.
-- ---------------------------------------------------------------------------------------------------

-- 010 TERRITORIO Y MEDIO AMBIENTE

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '010 TERRITORIO Y MEDIO AMBIENTE' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '010 Territory and environment' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''TERRITORIO_MEDIO_AMBIENTE'', ''/latest/categoryelements/TERRITORIO_MEDIO_AMBIENTE'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=TERRITORIO_MEDIO_AMBIENTE'', ''/#structuralResources/categoryElement;id=TERRITORIO_MEDIO_AMBIENTE'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '010'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '011 Territorio y usos del suelo' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '011 Territory and land use' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''TERRITORIO_USOS_SUELO'', ''/latest/categoryelements/TERRITORIO_USOS_SUELO'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=TERRITORIO_USOS_SUELO'', ''/#structuralResources/categoryElement;id=TERRITORIO_USOS_SUELO'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '011'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '012 Medio ambiente' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '012 Environment' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''MEDIO_AMBIENTE'', ''/latest/categoryelements/MEDIO_AMBIENTE'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=MEDIO_AMBIENTE'', ''/#structuralResources/categoryElement;id=MEDIO_AMBIENTE'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '012'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Demografía' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Demography' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''DEMOGRAFIA'', ''/latest/categoryelements/DEMOGRAFIA'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=DEMOGRAFIA'', ''/#structuralResources/categoryElement;id=DEMOGRAFIA'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '020'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '021 Población' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '021 Population' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''POBLACION'', ''/latest/categoryelements/POBLACION'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=POBLACION'', ''/#structuralResources/categoryElement;id=POBLACION'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '021'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '022 Movimiento natural' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '022 Natural movement' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''MOVIMIENTO_NATURAL'', ''/latest/categoryelements/MOVIMIENTO_NATURAL'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=MOVIMIENTO_NATURAL'', ''/#structuralResources/categoryElement;id=MOVIMIENTO_NATURAL'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '022'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '023 Movimientos migratorios' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '023 Migration' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''MOVIMIENTOS_MIGRATORIOS'', ''/latest/categoryelements/MOVIMIENTOS_MIGRATORIOS'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=MOVIMIENTOS_MIGRATORIOS'', ''/#structuralResources/categoryElement;id=MOVIMIENTOS_MIGRATORIOS'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '023'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '030 Sociedad' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '030 Society' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''SOCIEDAD'', ''/latest/categoryelements/SOCIEDAD'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=SOCIEDAD'', ''/#structuralResources/categoryElement;id=SOCIEDAD'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '030'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '031 Calidad de vida' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '031 Quality of life' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''CALIDAD_VIDA'', ''/latest/categoryelements/CALIDAD_VIDA'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=CALIDAD_VIDA'', ''/#structuralResources/categoryElement;id=CALIDAD_VIDA'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '031'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '032 Salud' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '032 Health' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''SALUD'', ''/latest/categoryelements/SALUD'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=SALUD'', ''/#structuralResources/categoryElement;id=SALUD'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '032'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '033 Educación' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '033 Education' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''EDUCACION'', ''/latest/categoryelements/EDUCACION'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=EDUCACION'', ''/#structuralResources/categoryElement;id=EDUCACION'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '033'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '034 Cultura, deporte y ocio' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '034 Culture, sport and leisure' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''CULTURA_DEPORTE_OCIO'', ''/latest/categoryelements/CULTURA_DEPORTE_OCIO'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=CULTURA_DEPORTE_OCIO'', ''/#structuralResources/categoryElement;id=CULTURA_DEPORTE_OCIO'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '034'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '035 Participación ciudadana y elecciones' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '035 Elections and participation' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''PARTICIPACION_CIUDADANA_ELECCIONES'', ''/latest/categoryelements/PARTICIPACION_CIUDADANA_ELECCIONES'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=PARTICIPACION_CIUDADANA_ELECCIONES'', ''/#structuralResources/categoryElement;id=PARTICIPACION_CIUDADANA_ELECCIONES'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '035'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '036 Justicia y seguridad' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '036 Justice and security' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''JUSTICIA_SEGURIDAD'', ''/latest/categoryelements/JUSTICIA_SEGURIDAD'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=JUSTICIA_SEGURIDAD'', ''/#structuralResources/categoryElement;id=JUSTICIA_SEGURIDAD'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '036'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '037 Protección social' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '037 Social protection' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''PROTECCION_SOCIAL'', ''/latest/categoryelements/PROTECCION_SOCIAL'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=PROTECCION_SOCIAL'', ''/#structuralResources/categoryElement;id=PROTECCION_SOCIAL'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '037'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '040 Economía general' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '040 Economy' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''ECONOMIA_GENERAL'', ''/latest/categoryelements/ECONOMIA_GENERAL'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=ECONOMIA_GENERAL'', ''/#structuralResources/categoryElement;id=ECONOMIA_GENERAL'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '040'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '041 Cuentas económicas' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '041 Economic accounts' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''CUENTAS_ECONOMICAS'', ''/latest/categoryelements/CUENTAS_ECONOMICAS'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=CUENTAS_ECONOMICAS'', ''/#structuralResources/categoryElement;id=CUENTAS_ECONOMICAS'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '041'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '042 Precios, consumo e inversión' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '042 Prices, consumption and investment' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''PRECIOS_CONSUMO_INVERSION'', ''/latest/categoryelements/PRECIOS_CONSUMO_INVERSION'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=PRECIOS_CONSUMO_INVERSION'', ''/#structuralResources/categoryElement;id=PRECIOS_CONSUMO_INVERSION'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '042'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '043 Empresas y centros de trabajo' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '043 Companies and workplaces' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''EMPRESAS_CENTROS_TRABAJO'', ''/latest/categoryelements/EMPRESAS_CENTROS_TRABAJO'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=EMPRESAS_CENTROS_TRABAJO'', ''/#structuralResources/categoryElement;id=EMPRESAS_CENTROS_TRABAJO'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '043'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '050 Empleo' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '050 Labour market' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''EMPLEO_NIVEL_1'', ''/latest/categoryelements/EMPLEO_NIVEL_1'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=EMPLEO_NIVEL_1'', ''/#structuralResources/categoryElement;id=EMPLEO_NIVEL_1'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '050'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '051 Empleo' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '051 Labour market' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''EMPLEO_NIVEL_2'', ''/latest/categoryelements/EMPLEO_NIVEL_2'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=EMPLEO_NIVEL_2'', ''/#structuralResources/categoryElement;id=EMPLEO_NIVEL_2'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '051'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '060 Sector primario' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '060 Primary sector' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''SECTOR_PRIMARIO'', ''/latest/categoryelements/SECTOR_PRIMARIO'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=SECTOR_PRIMARIO'', ''/#structuralResources/categoryElement;id=SECTOR_PRIMARIO'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '060'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '061 Agricultura, ganadería, pesca y caza' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '061 Agriculture, livestock, fishing and hunting' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''AGRICULTURA_GANADERIA_PESCA_CAZA'', ''/latest/categoryelements/AGRICULTURA_GANADERIA_PESCA_CAZA'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=AGRICULTURA_GANADERIA_PESCA_CAZA'', ''/#structuralResources/categoryElement;id=AGRICULTURA_GANADERIA_PESCA_CAZA'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '061'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '070 Sector secundario' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '070 Secondary sector' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''SECTOR_SECUNDARIO'', ''/latest/categoryelements/SECTOR_SECUNDARIO'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=SECTOR_SECUNDARIO'', ''/#structuralResources/categoryElement;id=SECTOR_SECUNDARIO'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '070'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '071 Industria, energía y agua' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '071 Industry, energy and water' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''INDUSTRIA_ENERGIA_AGUA'', ''/latest/categoryelements/INDUSTRIA_ENERGIA_AGUA'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=INDUSTRIA_ENERGIA_AGUA'', ''/#structuralResources/categoryElement;id=INDUSTRIA_ENERGIA_AGUA'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '071'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '072 Construcción y vivienda' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '072 Construction and housing' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''CONSTRUCCION_VIVIENDA'', ''/latest/categoryelements/CONSTRUCCION_VIVIENDA'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=CONSTRUCCION_VIVIENDA'', ''/#structuralResources/categoryElement;id=CONSTRUCCION_VIVIENDA'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '072'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '080 Sector servicios' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '080 Service sector' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''SECTOR_SERVICIOS'', ''/latest/categoryelements/SECTOR_SERVICIOS'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=SECTOR_SERVICIOS'', ''/#structuralResources/categoryElement;id=SECTOR_SERVICIOS'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '080'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '081 Comercio' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '081 Trade' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''COMERCIO'', ''/latest/categoryelements/COMERCIO'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=COMERCIO'', ''/#structuralResources/categoryElement;id=COMERCIO'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '081'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '082 Hostelería y turismo' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '082 Hospitality and tourism' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''HOSTELERIA_TURISMO'', ''/latest/categoryelements/HOSTELERIA_TURISMO'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=HOSTELERIA_TURISMO'', ''/#structuralResources/categoryElement;id=HOSTELERIA_TURISMO'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '082'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '083 Transporte y comunicaciones' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '083 Transport and communications' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''TRANSPORTE_COMUNICACIONES'', ''/latest/categoryelements/TRANSPORTE_COMUNICACIONES'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=TRANSPORTE_COMUNICACIONES'', ''/#structuralResources/categoryElement;id=TRANSPORTE_COMUNICACIONES'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '083'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '084 Servicios financieros, monetarios y seguros' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '084 Financial and monetary servicies' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''SERVICIOS_FINANCIEROS_MONETARIOS_SEGUROS'', ''/latest/categoryelements/SERVICIOS_FINANCIEROS_MONETARIOS_SEGUROS'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=SERVICIOS_FINANCIEROS_MONETARIOS_SEGUROS'', ''/#structuralResources/categoryElement;id=SERVICIOS_FINANCIEROS_MONETARIOS_SEGUROS'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '084'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '090 Administración pública' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '090 Public administration' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''ADMINISTRACION_PUBLICA_NIVEL_1'', ''/latest/categoryelements/ADMINISTRACION_PUBLICA_NIVEL_1'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=ADMINISTRACION_PUBLICA_NIVEL_1'', ''/#structuralResources/categoryElement;id=ADMINISTRACION_PUBLICA_NIVEL_1'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '090'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '091 Administración pública' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '091 Public administration' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''ADMINISTRACION_PUBLICA_NIVEL_2'', ''/latest/categoryelements/ADMINISTRACION_PUBLICA_NIVEL_2'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=ADMINISTRACION_PUBLICA_NIVEL_2'', ''/#structuralResources/categoryElement;id=ADMINISTRACION_PUBLICA_NIVEL_2'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '091'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '100 Ciencia y tecnología' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '100 Science and technology' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''CIENCIA_TECNOLOGIA_NIVEL_1'', ''/latest/categoryelements/CIENCIA_TECNOLOGIA_NIVEL_1'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=CIENCIA_TECNOLOGIA_NIVEL_1'', ''/#structuralResources/categoryElement;id=CIENCIA_TECNOLOGIA_NIVEL_1'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '100'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '101 Ciencia y tecnología' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '101 Science and technology' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''CIENCIA_TECNOLOGIA_NIVEL_2'', ''/latest/categoryelements/CIENCIA_TECNOLOGIA_NIVEL_2'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=CIENCIA_TECNOLOGIA_NIVEL_2'', ''/#structuralResources/categoryElement;id=CIENCIA_TECNOLOGIA_NIVEL_2'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '101'
union all
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '900 Síntesis estadística' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || '900 Summary statistics' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''SINTESIS_ESTADISTICA'', ''/latest/categoryelements/SINTESIS_ESTADISTICA'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=SINTESIS_ESTADISTICA'', ''/#structuralResources/categoryElement;id=SINTESIS_ESTADISTICA'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '900';

