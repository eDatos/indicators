-- !!!ATENCIÓN SCRIPT SÓLO A EJECUTAR EN ENTORNO DE DESARROLLO
-- ---------------------------------------------------------------------------------------------------
-- PASO 1:  A ejecutar en la base de datos del SRM. 
--Precondición: Se deben haber creado los elementos de tema asociados a los SUBJECT_CODES siguientes:
-- EMPLEO					
-- MEDIOAMBIENTE						
-- ECONOMIA							
-- SALUD										
-- DEMOGRAFIA						
-- EDUCACION	

--1) Crear el international string asociado al elemento de tema
--2) Crear el external item de dicha elemento de tema
--3) Actualizar la tabla tb_indicator_versions con el elemento de tema creado como external item-> category_element_fk
-- ---------------------------------------------------------------------------------------------------

-- PARO_REGISTRADO

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Empleo' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Empleo' || ''', ''ca'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Employment' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''EMPLEO'', ''/latest/categoryelements/EMPLEO'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=EMPLEO'', ''/#structuralResources/categoryElement;id=EMPLEO'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = 'PARO_REGISTRADO';

-- EDUCACION

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Educación' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Educación' || ''', ''ca'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Education' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''EDUCACION'', ''/latest/categoryelements/EDUCACION'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=EDUCACION'', ''/#structuralResources/categoryElement;id=EDUCACION'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = 'EDUCACION';

-- POLITICA_ECON

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Economía' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Economía' || ''', ''ca'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Economy' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''ECONOMIA'', ''/latest/categoryelements/ECONOMIA'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=ECONOMIA'', ''/#structuralResources/categoryElement;id=ECONOMIA'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = 'POLITICA_ECON';

-- SALUD

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Salud' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Salud' || ''', ''ca'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Health' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''SALUD'', ''/latest/categoryelements/SALUD'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=SALUD'', ''/#structuralResources/categoryElement;id=SALUD'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = 'SALUD';

-- MEDIO_AMBIENTE

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Medioambiente' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'medi ambient' || ''', ''ca'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Environment' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''MEDIOAMBIENTE'', ''/latest/categoryelements/MEDIOAMBIENTE'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=MEDIOAMBIENTE'', ''/#structuralResources/categoryElement;id=MEDIOAMBIENTE'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = 'MEDIO_AMBIENTE';

-- CIFRAS_PADRONAL

select  
'INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) VALUES (nextval(''SEQ_I18NSTRS''), 1);  
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Demografía' || ''', ''es'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Demografía' || ''', ''ca'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval(''SEQ_L10NSTRS''), ''' || 'Demography' || ''', ''en'', currval(''SEQ_I18NSTRS''), 1);
INSERT INTO TB_EXTERNAL_ITEMS(ID, CODE, URI, URN, MANAGEMENT_APP_URL, VERSION, TITLE_FK, TYPE) values (nextval(''SEQ_EXTERNAL_ITEMS''), ''DEMOGRAFIA'', ''/latest/categoryelements/DEMOGRAFIA'', ''urn:siemac:org.siemac.metamac.infomodel.structuralresources.CategoryElement=DEMOGRAFIA'', ''/#structuralResources/categoryElement;id=DEMOGRAFIA'', 1, currval(''SEQ_I18NSTRS''), ''structuralResources#categoryElement'');
UPDATE TB_INDICATORS_VERSIONS SET CATEGORY_ELEMENT_FK= currval(''SEQ_EXTERNAL_ITEMS'') where id =' || id || ';'
from TB_INDICATORS_VERSIONS where deprecated_subject_code = 'CIFRAS_PADRONAL';