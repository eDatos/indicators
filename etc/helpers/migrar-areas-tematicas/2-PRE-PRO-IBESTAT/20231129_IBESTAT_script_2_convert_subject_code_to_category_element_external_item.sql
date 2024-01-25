-- ---------------------------------------------------------------------------------------------------
-- EDATOS-4185 - Añadir metadato elemento de tema a los indicadores
-- 
-- Script para realizar la migración de las áreas temáticas a elementos de tema provenientes del srm en PRE/PRO IBESTAT
-- Se obtienen las sentencias a ejecutar a partir de una consulta.
-- ---------------------------------------------------------------------------------------------------

-- DEMOGRAFIA

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Demografía' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Demografia' || ''', ''ca'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Demography' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''DEMOGRAFIA'', ''/latest/categoryelements/DEMOGRAFIA'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=DEMOGRAFIA'', ''/#structuralResources/categoryElement;id=DEMOGRAFIA'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '010'
UNION ALL
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Economía' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Economia' || ''', ''ca'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Economy' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''ECONOMIA'', ''/latest/categoryelements/ECONOMIA'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=ECONOMIA'', ''/#structuralResources/categoryElement;id=ECONOMIA'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '020'
UNION ALL
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Estadísticas no desglosables por tema' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Estadístiques no desglossades per tema' || ''', ''ca'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''ESTADISTICAS_SIN_TEMA'', ''/latest/categoryelements/ESTADISTICAS_SIN_TEMA'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=ESTADISTICAS_SIN_TEMA'', ''/#structuralResources/categoryElement;id=ESTADISTICAS_SIN_TEMA'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '090'
UNION ALL
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Sociedad' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Societat ' || ''', ''ca'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Society' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''SOCIEDAD'', ''/latest/categoryelements/SOCIEDAD'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=SOCIEDAD'', ''/#structuralResources/categoryElement;id=SOCIEDAD'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '030'
UNION ALL
select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Territorio y medio ambiente' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Territori i medi ambient' || ''', ''ca'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Territory and environment' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''TERRITORIO_MEDIO_AMBIENTE'', ''/latest/categoryelements/TERRITORIO_MEDIO_AMBIENTE'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=TERRITORIO_MEDIO_AMBIENTE'', ''/#structuralResources/categoryElement;id=TERRITORIO_MEDIO_AMBIENTE'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = '040';

