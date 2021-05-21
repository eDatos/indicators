-- --------------------------------------------------------------------------------------------------
-- EDATOS-3351 - Indicadores no está preparada para tener un idioma por defecto distinto del español
-- --------------------------------------------------------------------------------------------------

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Observation confidenciality', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Confidencialidad de la observación', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Confidencialitat de l''observació', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'METADATA_ATTRIBUTE.OBS_CONF', currval('SEQ_I18NSTRS'));

commit;