-- PENDIENTE ISTAC Lista de valores definitivos

INSERT INTO TBL_INTERNATIONAL_STRINGS (ID, VERSION) values (1, 1);
INSERT INTO TBL_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (1, 'kilometers', 'en',  1, 1);
INSERT INTO TBL_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (2, 'kilómetros', 'es',  1, 1);
INSERT INTO LIS_QUANTITIES_UNITS (ID, UUID, SYMBOL, SYMBOL_POSITION, TITLE_FK) values (1, '1', 'km', 'END', 1);

INSERT INTO TBL_INTERNATIONAL_STRINGS (ID, VERSION) values (2, 1);
INSERT INTO TBL_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (3, 'meters', 'en',  2, 1);
INSERT INTO TBL_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (4, 'metros', 'es',  2, 1);
INSERT INTO LIS_QUANTITIES_UNITS (ID, UUID, SYMBOL, SYMBOL_POSITION, TITLE_FK) values (2, '2', 'm', 'END', 2);

-- Update sequences
alter sequence SEQ_I18NSTRS increment by 2;
select SEQ_I18NSTRS.nextval from dual;
alter sequence SEQ_I18NSTRS increment by 1; 

alter sequence SEQ_L10NSTRS increment by 4;
select SEQ_L10NSTRS.nextval from dual;
alter sequence SEQ_L10NSTRS increment by 1;

alter sequence SEQ_QUANTITIES_UNITS increment by 2;
select SEQ_QUANTITIES_UNITS.nextval from dual;
alter sequence SEQ_QUANTITIES_UNITS increment by 1;