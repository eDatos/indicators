
-- Unit multipliers
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Unidades', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LIS_UNITS_MULTIPLIERS (ID, UNIT_MULTIPLIER, UUID, TITLE_FK) values (SEQ_UNITS_MULTIPLIERS.nextval, 1, '1', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Decenas', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LIS_UNITS_MULTIPLIERS (ID, UNIT_MULTIPLIER, UUID, TITLE_FK) values (SEQ_UNITS_MULTIPLIERS.nextval, 10, '2', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Cientos', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LIS_UNITS_MULTIPLIERS (ID, UNIT_MULTIPLIER, UUID, TITLE_FK) values (SEQ_UNITS_MULTIPLIERS.nextval, 100, '3', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Miles', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LIS_UNITS_MULTIPLIERS (ID, UNIT_MULTIPLIER, UUID, TITLE_FK) values (SEQ_UNITS_MULTIPLIERS.nextval, 1000, '4', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Decenas de miles', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LIS_UNITS_MULTIPLIERS (ID, UNIT_MULTIPLIER, UUID, TITLE_FK) values (SEQ_UNITS_MULTIPLIERS.nextval, 10000, '5', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Cientos de miles', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LIS_UNITS_MULTIPLIERS (ID, UNIT_MULTIPLIER, UUID, TITLE_FK) values (SEQ_UNITS_MULTIPLIERS.nextval, 100000, '6', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Millones', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LIS_UNITS_MULTIPLIERS (ID, UNIT_MULTIPLIER, UUID, TITLE_FK) values (SEQ_UNITS_MULTIPLIERS.nextval, 1000000, '7', SEQ_I18NSTRS.currval);

commit;