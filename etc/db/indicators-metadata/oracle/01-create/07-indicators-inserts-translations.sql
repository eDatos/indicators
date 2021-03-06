-- Time granularities
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Yearly', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Anual', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Anual', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_GRANULARITY.YEARLY', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Biyearly', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Semestral', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Semestral', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_GRANULARITY.BIYEARLY', SEQ_I18NSTRS.currval);
 
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Quaterly', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Cuatrimestral', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Quadrimestral', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_GRANULARITY.FOUR_MONTHLY', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Quaterly', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Trimestral', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Trimestral', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_GRANULARITY.QUARTERLY', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Monthly', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Mensual', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Mensual', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_GRANULARITY.MONTHLY', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Weekly', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Semanal', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Setmanal', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_GRANULARITY.WEEKLY', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Daily', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Diario', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Diari', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_GRANULARITY.DAILY', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Hourly', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Cada hora', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Cada hora', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_GRANULARITY.HOURLY', SEQ_I18NSTRS.currval);
    
-- Time value YEARLY
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy}', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy}', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy}', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_VALUE.YEARLY', SEQ_I18NSTRS.currval);

-- Time value BIYEARLY
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} First semester', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Primer semestre', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Primer semestre', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_VALUE.BIYEARLY.S1', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Second semester', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Segundo semestre', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Segon semestre', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_VALUE.BIYEARLY.S2', SEQ_I18NSTRS.currval);

-- Time value FOUR MONTH
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} First four month period', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Primer cuatrimestre', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Primer quadrimestre', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_VALUE.FOUR_MONTHLY.T1', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Second four month period', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Segundo cuatrimestre', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Segon quadrimestre', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_VALUE.FOUR_MONTHLY.T2', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Third four month period', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Tercer cuatrimestre', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Tercer quadrimestre', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_VALUE.FOUR_MONTHLY.T3', SEQ_I18NSTRS.currval);

-- Time value QUATERLY
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} First quarter', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Primer trimestre', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Primer trimestre', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_VALUE.QUARTERLY.Q1', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Second quarter', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Segundo trimestre', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Segon trimestre', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_VALUE.QUARTERLY.Q2', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Third quarter', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Tercer trimestre', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Tercer trimestre', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_VALUE.QUARTERLY.Q3', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Fourth quarter', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Cuarto trimestre', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Quart trimestre', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_VALUE.QUARTERLY.Q4', SEQ_I18NSTRS.currval);

-- Time value MONTHLY
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} January', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Enero', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Gener', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_VALUE.MONTHLY.M01', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} February', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Febrero', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Febrer', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_VALUE.MONTHLY.M02', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} March', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Marzo', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Mar??', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_VALUE.MONTHLY.M03', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} April', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Abril', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Abril', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_VALUE.MONTHLY.M04', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} May', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Mayo', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Maig', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_VALUE.MONTHLY.M05', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} June', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Junio', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Juny', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_VALUE.MONTHLY.M06', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} July', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Julio', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Juliol', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_VALUE.MONTHLY.M07', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} August', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Agosto', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Agost', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_VALUE.MONTHLY.M08', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} September', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Septiembre', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Setembre', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_VALUE.MONTHLY.M09', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} October', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Octubre', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Octubre', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_VALUE.MONTHLY.M10', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} November', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Noviembre', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Novembre', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_VALUE.MONTHLY.M11', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} December', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Diciembre', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Desembre', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_VALUE.MONTHLY.M12', SEQ_I18NSTRS.currval);

-- Time value WEEKLY
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Week {ww}', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Semana {ww}', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{yyyy} Setmana {ww}', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_VALUE.WEEKLY', SEQ_I18NSTRS.currval);

-- Time value DAILY
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{dd}/{MM}/{yyyy}', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{dd}/{MM}/{yyyy}', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{dd}/{MM}/{yyyy}', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_VALUE.DAILY', SEQ_I18NSTRS.currval);

-- Time value HOURLY
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{dd}/{MM}/{yyyy} - {HH}:{mm}:{ss}', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{dd}/{MM}/{yyyy} - {HH}:{mm}:{ss}', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, '{dd}/{MM}/{yyyy} - {HH}:{mm}:{ss}', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'TIME_VALUE.HOURLY', SEQ_I18NSTRS.currval);

-- Measure values
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Data', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Dato', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Dada', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'MEASURE_DIMENSION.ABSOLUTE', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Annual change rate', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Tasa variaci??n anual', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Taxa de variaci?? anual', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'MEASURE_DIMENSION.ANNUAL_PERCENTAGE_RATE', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Interperiod change rate', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Tasa variaci??n interperi??dica', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Taxa de variaci?? interperi??dica', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'MEASURE_DIMENSION.INTERPERIOD_PERCENTAGE_RATE', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Annual change', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Variaci??n anual', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Variaci?? anual', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'MEASURE_DIMENSION.ANNUAL_PUNTUAL_RATE', SEQ_I18NSTRS.currval);

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Interperiod change', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Variaci??n interperi??dica', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Variaci?? interperi??dica', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'MEASURE_DIMENSION.INTERPERIOD_PUNTUAL_RATE', SEQ_I18NSTRS.currval);

-- Metadata attributes
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Observation confidenciality', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Confidencialidad de la observaci??n', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Confidencialitat de l''observaci??', 'ca', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (SEQ_TRANSLATIONS.nextval, 'METADATA_ATTRIBUTE.OBS_CONF', SEQ_I18NSTRS.currval);

commit;