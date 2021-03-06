-- --------------------------------------------------------------------------------------------------
-- EDATOS-3351 - Indicadores no está preparada para tener un idioma por defecto distinto del español
-- --------------------------------------------------------------------------------------------------

-- Time granularities
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Anual', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_GRANULARITY.YEARLY'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Semestral', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_GRANULARITY.BIYEARLY'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Quadrimestral', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_GRANULARITY.FOUR_MONTHLY'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Trimestral', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_GRANULARITY.QUARTERLY'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Mensual', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_GRANULARITY.MONTHLY'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Setmanal', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_GRANULARITY.WEEKLY'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Diari', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_GRANULARITY.DAILY'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Cada hora', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_GRANULARITY.HOURLY'), 1);
    
-- Time value YEARLY
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy}', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_VALUE.YEARLY'), 1);

-- Time value BIYEARLY
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Primer semestre', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_VALUE.BIYEARLY.H1'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Segon semestre', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_VALUE.BIYEARLY.H2'), 1);

-- Time value FOUR MONTH
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Primer quadrimestre', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_VALUE.FOUR_MONTHLY.T1'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Segon quadrimestre', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_VALUE.FOUR_MONTHLY.T2'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Tercer quadrimestre', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_VALUE.FOUR_MONTHLY.T3'), 1);

-- Time value QUATERLY
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Primer trimestre', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_VALUE.QUARTERLY.Q1'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Segon trimestre', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_VALUE.QUARTERLY.Q2'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Tercer trimestre', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_VALUE.QUARTERLY.Q3'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Quart trimestre', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_VALUE.QUARTERLY.Q4'), 1);

-- Time value MONTHLY
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Gener', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_VALUE.MONTHLY.M01'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Febrer', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_VALUE.MONTHLY.M02'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Març', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_VALUE.MONTHLY.M03'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Abril', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_VALUE.MONTHLY.M04'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Maig', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_VALUE.MONTHLY.M05'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Juny', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_VALUE.MONTHLY.M06'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Juliol', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_VALUE.MONTHLY.M07'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Agost', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_VALUE.MONTHLY.M08'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Setembre', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_VALUE.MONTHLY.M09'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Octubre', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_VALUE.MONTHLY.M10'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Novembre', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_VALUE.MONTHLY.M11'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Desembre', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_VALUE.MONTHLY.M12'), 1);

-- Time value WEEKLY
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{yyyy} Setmana {ww}', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_VALUE.WEEKLY'), 1);

-- Time value DAILY
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{dd}/{MM}/{yyyy}', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_VALUE.DAILY'), 1);

-- Time value HOURLY
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), '{dd}/{MM}/{yyyy} - {HH}:{mm}:{ss}', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'TIME_VALUE.HOURLY'), 1);

-- Measure values
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Dada', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'MEASURE_DIMENSION.ABSOLUTE'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Taxa de variació anual', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'MEASURE_DIMENSION.ANNUAL_PERCENTAGE_RATE'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Taxa de variació interperiòdica', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'MEASURE_DIMENSION.INTERPERIOD_PERCENTAGE_RATE'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Variació anual', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'MEASURE_DIMENSION.ANNUAL_PUNTUAL_RATE'), 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (nextval('SEQ_L10NSTRS'), 'Variació interperiòdica', 'ca', (SELECT TITLE_FK FROM TB_TRANSLATIONS WHERE CODE = 'MEASURE_DIMENSION.INTERPERIOD_PUNTUAL_RATE'), 1);


commit;