-- PENDIENTE ISTAC Lista de valores definitivos

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'kilometers', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'kilómetros', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LIS_QUANTITIES_UNITS (ID, UUID, SYMBOL, SYMBOL_POSITION, TITLE_FK) values (SEQ_QUANTITIES_UNITS.nextval, '1', 'km', 'END', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'meters', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'metros', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LIS_QUANTITIES_UNITS (ID, UUID, SYMBOL, SYMBOL_POSITION, TITLE_FK) values (SEQ_QUANTITIES_UNITS.nextval, '2', 'm', 'END', SEQ_I18NSTRS.currval);

commit;