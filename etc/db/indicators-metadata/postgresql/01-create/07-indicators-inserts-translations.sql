-- Time granularities
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Yearly', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Anual', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Anual', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_GRANULARITY.YEARLY', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Biyearly', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Semestral', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Semestral', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_GRANULARITY.BIYEARLY', currval('SEQ_I18NSTRS'));
 
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Quaterly', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cuatrimestral', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Quadrimestral', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_GRANULARITY.FOUR_MONTHLY', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Quaterly', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Trimestral', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Trimestral', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_GRANULARITY.QUARTERLY', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Monthly', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Mensual', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Mensual', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_GRANULARITY.MONTHLY', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Weekly', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Semanal', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Setmanal', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_GRANULARITY.WEEKLY', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Daily', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Diario', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Diari', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_GRANULARITY.DAILY', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Hourly', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cada hora', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cada hora', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_GRANULARITY.HOURLY', currval('SEQ_I18NSTRS'));
    
-- Time value YEARLY
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy}', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy}', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy}', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_VALUE.YEARLY', currval('SEQ_I18NSTRS'));

-- Time value BIYEARLY
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} First semester', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Primer semestre', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Primer semestre', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_VALUE.BIYEARLY.S1', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Second semester', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Segundo semestre', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Segon semestre', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_VALUE.BIYEARLY.S2', currval('SEQ_I18NSTRS'));

-- Time value FOUR MONTH
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} First four month period', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Primer cuatrimestre', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Primer quadrimestre', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_VALUE.FOUR_MONTHLY.T1', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Second four month period', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Segundo cuatrimestre', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Segon quadrimestre', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_VALUE.FOUR_MONTHLY.T2', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Third four month period', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Tercer cuatrimestre', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Tercer quadrimestre', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_VALUE.FOUR_MONTHLY.T3', currval('SEQ_I18NSTRS'));

-- Time value QUATERLY
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} First quarter', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Primer trimestre', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Primer trimestre', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_VALUE.QUARTERLY.Q1', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Second quarter', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Segundo trimestre', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Segon trimestre', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_VALUE.QUARTERLY.Q2', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Third quarter', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Tercer trimestre', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Tercer trimestre', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_VALUE.QUARTERLY.Q3', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Fourth quarter', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Cuarto trimestre', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Quart trimestre', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_VALUE.QUARTERLY.Q4', currval('SEQ_I18NSTRS'));

-- Time value MONTHLY
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} January', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Enero', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Gener', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_VALUE.MONTHLY.M01', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} February', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Febrero', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Febrer', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_VALUE.MONTHLY.M02', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} March', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Marzo', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Mar??', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_VALUE.MONTHLY.M03', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} April', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Abril', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Abril', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_VALUE.MONTHLY.M04', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} May', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Mayo', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Maig', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_VALUE.MONTHLY.M05', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} June', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Junio', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Juny', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_VALUE.MONTHLY.M06', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} July', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Julio', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Juliol', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_VALUE.MONTHLY.M07', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} August', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Agosto', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Agost', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_VALUE.MONTHLY.M08', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} September', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Septiembre', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Setembre', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_VALUE.MONTHLY.M09', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} October', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Octubre', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Octubre', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_VALUE.MONTHLY.M10', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} November', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Noviembre', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Novembre', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_VALUE.MONTHLY.M11', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} December', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Diciembre', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Desembre', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_VALUE.MONTHLY.M12', currval('SEQ_I18NSTRS'));

-- Time value WEEKLY
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Week {ww}', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Semana {ww}', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Setmana {ww}', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_VALUE.WEEKLY', currval('SEQ_I18NSTRS'));

-- Time value DAILY
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{dd}/{MM}/{yyyy}', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{dd}/{MM}/{yyyy}', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{dd}/{MM}/{yyyy}', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_VALUE.DAILY', currval('SEQ_I18NSTRS'));

-- Time value HOURLY
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{dd}/{MM}/{yyyy} - {HH}:{mm}:{ss}', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{dd}/{MM}/{yyyy} - {HH}:{mm}:{ss}', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{dd}/{MM}/{yyyy} - {HH}:{mm}:{ss}', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'TIME_VALUE.HOURLY', currval('SEQ_I18NSTRS'));

-- Measure values
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Data', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Dato', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Dada', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'MEASURE_DIMENSION.ABSOLUTE', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Annual change rate', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tasa variaci??n anual', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Taxa de variaci?? anual', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'MEASURE_DIMENSION.ANNUAL_PERCENTAGE_RATE', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Interperiod change rate', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Tasa variaci??n interperi??dica', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Taxa de variaci?? interperi??dica', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'MEASURE_DIMENSION.INTERPERIOD_PERCENTAGE_RATE', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Annual change', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Variaci??n anual', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Variaci?? anual', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'MEASURE_DIMENSION.ANNUAL_PUNTUAL_RATE', currval('SEQ_I18NSTRS'));

INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Interperiod change', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Variaci??n interperi??dica', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Variaci?? interperi??dica', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'MEASURE_DIMENSION.INTERPERIOD_PUNTUAL_RATE', currval('SEQ_I18NSTRS'));

-- Metadata attributes
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (nextval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Observation confidenciality', 'en', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Confidencialidad de la observaci??n', 'es', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Confidencialitat de l''observaci??', 'ca', currval('SEQ_I18NSTRS'), 1);
INSERT INTO TB_TRANSLATIONS (ID, CODE, TITLE_FK) values (nextval('SEQ_TRANSLATIONS'), 'METADATA_ATTRIBUTE.OBS_CONF', currval('SEQ_I18NSTRS'));
