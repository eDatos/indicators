-- --------------------------------------------------------------------------------------------------
-- EDATOS-3351 - Indicadores no está preparada para tener un idioma por defecto distinto del español
-- --------------------------------------------------------------------------------------------------

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Observation confidenciality', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Confidencialidad de la observación', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Confidencialitat de l''observació', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'METADATA_ATTRIBUTE.OBS_CONF', SEQ_I18NSTRS.currval);

commit;