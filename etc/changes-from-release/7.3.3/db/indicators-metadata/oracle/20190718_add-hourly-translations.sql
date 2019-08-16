-- ----------------------------------------------------------------------------------------
-- INDISTAC-1065 - Modificación de las granularidades temporales para dar soporte a la granularidad horaria
-- ----------------------------------------------------------------------------------------

-- Time granularity
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Hourly', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Cada hora', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_GRANULARITY.HOURLY', SEQ_I18NSTRS.currval);

-- Time value HOURLY
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{dd}/{MM}/{yyyy} at {HH}:{mm}:{ss}', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{dd}/{MM}/{yyyy} a las {HH}:{mm}:{ss}', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_VALUE.HOURLY', SEQ_I18NSTRS.currval);

commit;