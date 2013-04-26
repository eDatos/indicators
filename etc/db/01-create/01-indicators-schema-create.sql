
-- Create normal entities
    
    	
CREATE TABLE TB_CONFIGURATION (
  ID NUMBER(19) NOT NULL,
  LAST_SUCCESSFUL_GPE_QUERY56 TIMESTAMP(6) NOT NULL
);



    	
CREATE TABLE TB_INTERNATIONAL_STRINGS (
  ID NUMBER(19) NOT NULL,
  VERSION NUMBER(19) NOT NULL
);


    	
CREATE TABLE TB_INDICATORS (
  ID NUMBER(19) NOT NULL,
  CODE VARCHAR2(255) NOT NULL,
  IS_PUBLISHED NUMBER(1,0) NOT NULL,
  UUID VARCHAR2(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR2(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR2(50),
  LAST_UPDATED_TZ VARCHAR2(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR2(50),
  VERSION NUMBER(19) NOT NULL,
  PRODUCTION_ID NUMBER(19),
  PRODUCTION_VERSION_NUMBER VARCHAR2(10),
  DIFFUSION_ID NUMBER(19),
  DIFFUSION_VERSION_NUMBER VARCHAR2(10)
);


    	
CREATE TABLE TB_LIS_QUANTITIES_UNITS (
  ID NUMBER(19) NOT NULL,
  SYMBOL VARCHAR2(255),
  UUID VARCHAR2(36) NOT NULL,
  TITLE_FK NUMBER(19) NOT NULL,
  SYMBOL_POSITION VARCHAR2(255)
);


    	
CREATE TABLE TB_LIS_GEOGR_GRANULARITIES (
  ID NUMBER(19) NOT NULL,
  CODE VARCHAR2(255) NOT NULL,
  UUID VARCHAR2(36) NOT NULL,
  TITLE_FK NUMBER(19)
);


    	
CREATE TABLE TB_LIS_GEOGR_VALUES (
  ID NUMBER(19) NOT NULL,
  CODE VARCHAR2(255) NOT NULL,
  LATITUDE FLOAT(126),
  LONGITUDE FLOAT(126),
  GLOBAL_ORDER VARCHAR2(255) NOT NULL,
  UUID VARCHAR2(36) NOT NULL,
  TITLE_FK NUMBER(19),
  GRANULARITY_FK NUMBER(19) NOT NULL
);


    	
CREATE TABLE TB_QUANTITIES (
  ID NUMBER(19) NOT NULL,
  UNIT_MULTIPLIER NUMBER(10),
  SIGNIFICANT_DIGITS NUMBER(10),
  DECIMAL_PLACES NUMBER(10),
  MINIMUM NUMBER(10),
  MAXIMUM NUMBER(10),
  IS_PERCENTAGE NUMBER(1,0),
  BASE_VALUE NUMBER(10),
  BASE_TIME VARCHAR2(255),
  UUID VARCHAR2(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR2(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR2(50),
  LAST_UPDATED_TZ VARCHAR2(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR2(50),
  VERSION NUMBER(19) NOT NULL,
  PERCENTAGE_OF_FK NUMBER(19),
  UNIT_FK NUMBER(19),
  NUMERATOR_FK NUMBER(19),
  DENOMINATOR_FK NUMBER(19),
  BASE_QUANTITY_FK NUMBER(19),
  BASE_LOCATION_FK NUMBER(19),
  QUANTITY_TYPE VARCHAR2(255)
);


    	
CREATE TABLE TB_INDICATORS_VERSIONS (
  ID NUMBER(19) NOT NULL,
  VERSION_NUMBER VARCHAR2(10) NOT NULL,
  SUBJECT_CODE VARCHAR2(255) NOT NULL,
  COMMENTS_URL VARCHAR2(4000),
  NOTES_URL VARCHAR2(4000),
  IS_LAST_VERSION NUMBER(1,0) NOT NULL,
  DATA_REPOSITORY_ID VARCHAR2(255),
  DATA_REPOSITORY_TABLE_NAME VARCHAR2(255),
  NEEDS_UPDATE NUMBER(1,0) NOT NULL,
  INCONSISTENT_DATA NUMBER(1,0) NOT NULL,
  PRODUCTION_VALIDATION_DATE_TZ VARCHAR2(50),
  PRODUCTION_VALIDATION_DATE TIMESTAMP,
  PRODUCTION_VALIDATION_USER VARCHAR2(255),
  DIFFUSION_VALIDATION_DATE_TZ VARCHAR2(50),
  DIFFUSION_VALIDATION_DATE TIMESTAMP,
  DIFFUSION_VALIDATION_USER VARCHAR2(255),
  PUBLICATION_DATE_TZ VARCHAR2(50),
  PUBLICATION_DATE TIMESTAMP,
  PUBLICATION_USER VARCHAR2(255),
  PUBLICATION_FAILED_DATE_TZ VARCHAR2(50),
  PUBLICATION_FAILED_DATE TIMESTAMP,
  PUBLICATION_FAILED_USER VARCHAR2(255),
  ARCHIVE_DATE_TZ VARCHAR2(50),
  ARCHIVE_DATE TIMESTAMP,
  ARCHIVE_USER VARCHAR2(255),
  UPDATE_DATE_TZ VARCHAR2(50),
  UPDATE_DATE TIMESTAMP,
  LAST_POPULATE_DATE_TZ VARCHAR2(50),
  LAST_POPULATE_DATE TIMESTAMP,
  UUID VARCHAR2(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR2(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR2(50),
  LAST_UPDATED_TZ VARCHAR2(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR2(50),
  VERSION NUMBER(19) NOT NULL,
  TITLE_FK NUMBER(19) NOT NULL,
  ACRONYM_FK NUMBER(19),
  SUBJECT_TITLE_FK NUMBER(19) NOT NULL,
  CONCEPT_DESCRIPTION_FK NUMBER(19),
  COMMENTS_FK NUMBER(19),
  NOTES_FK NUMBER(19),
  INDICATOR_FK NUMBER(19) NOT NULL,
  QUANTITY_FK NUMBER(19) NOT NULL,
  PROC_STATUS VARCHAR2(255) NOT NULL
);


    	
CREATE TABLE TB_RATES_DERIVATIONS (
  ID NUMBER(19) NOT NULL,
  METHOD VARCHAR2(255) NOT NULL,
  UUID VARCHAR2(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR2(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR2(50),
  LAST_UPDATED_TZ VARCHAR2(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR2(50),
  VERSION NUMBER(19) NOT NULL,
  QUANTITY_FK NUMBER(19) NOT NULL,
  METHOD_TYPE VARCHAR2(255) NOT NULL,
  ROUNDING VARCHAR2(255)
);


    	
CREATE TABLE TB_DATA_SOURCES (
  ID NUMBER(19) NOT NULL,
  DATA_GPE_UUID VARCHAR2(255) NOT NULL,
  PX_URI VARCHAR2(255) NOT NULL,
  TIME_VARIABLE VARCHAR2(255),
  TIME_VALUE VARCHAR2(255),
  GEOGRAPHICAL_VARIABLE VARCHAR2(255),
  ABSOLUTE_METHOD VARCHAR2(255),
  SOURCE_SURVEY_CODE VARCHAR2(255) NOT NULL,
  SOURCE_SURVEY_URL VARCHAR2(4000),
  PUBLISHERS VARCHAR2(4000) NOT NULL,
  UPDATE_DATE_TZ VARCHAR2(50),
  UPDATE_DATE TIMESTAMP,
  UUID VARCHAR2(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR2(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR2(50),
  LAST_UPDATED_TZ VARCHAR2(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR2(50),
  VERSION NUMBER(19) NOT NULL,
  SOURCE_SURVEY_TITLE_FK NUMBER(19) NOT NULL,
  SOURCE_SURVEY_ACRONYM_FK NUMBER(19),
  INDICATOR_VERSION_FK NUMBER(19),
  ANNUAL_PUNTUAL_RATE_FK NUMBER(19),
  ANNUAL_PERCENTAGE_RATE_FK NUMBER(19),
  INTERPERIOD_PUNTUAL_RATE_FK NUMBER(19),
  INTERPERIOD_PERCENTAGE_RATE_FK NUMBER(19),
  GEOGRAPHICAL_VALUE_FK NUMBER(19)
);

    	
CREATE TABLE TB_INDIC_INST_GEO_VALUES (
  GEOGRAPHICAL_VALUE_FK NUMBER(19) NOT NULL,
  INDICATOR_INSTANCE_FK NUMBER(19) NOT NULL
);

    	
CREATE TABLE TB_DATA_SOURCES_VARIABLES (
  ID NUMBER(19) NOT NULL,
  VARIABLE VARCHAR2(255) NOT NULL,
  CATEGORY VARCHAR2(255) NOT NULL,
  UUID VARCHAR2(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR2(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR2(50),
  LAST_UPDATED_TZ VARCHAR2(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR2(50),
  VERSION NUMBER(19) NOT NULL,
  DATA_SOURCE_FK NUMBER(19)
);


    	
CREATE TABLE TB_INDICATORS_INSTANCES (
  ID NUMBER(19) NOT NULL,
  CODE VARCHAR2(255) NOT NULL,
  TIME_VALUES CLOB,
  UPDATE_DATE_TZ VARCHAR2(50),
  UPDATE_DATE TIMESTAMP,
  UUID VARCHAR2(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR2(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR2(50),
  LAST_UPDATED_TZ VARCHAR2(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR2(50),
  VERSION NUMBER(19) NOT NULL,
  TITLE_FK NUMBER(19) NOT NULL,
  INDICATOR_FK NUMBER(19) NOT NULL,
  GEOGRAPHICAL_GRANULARITY_FK NUMBER(19),
  TIME_GRANULARITY VARCHAR2(255)
);


    	
CREATE TABLE TB_INDICATORS_SYSTEMS (
  ID NUMBER(19) NOT NULL,
  CODE VARCHAR2(255) NOT NULL,
  IS_PUBLISHED NUMBER(1,0) NOT NULL,
  UUID VARCHAR2(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR2(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR2(50),
  LAST_UPDATED_TZ VARCHAR2(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR2(50),
  VERSION NUMBER(19) NOT NULL,
  PRODUCTION_ID NUMBER(19),
  PRODUCTION_VERSION_NUMBER VARCHAR2(10),
  DIFFUSION_ID NUMBER(19),
  DIFFUSION_VERSION_NUMBER VARCHAR2(10)
);


    	
CREATE TABLE TB_INDIC_SYSTEMS_VERSIONS (
  ID NUMBER(19) NOT NULL,
  VERSION_NUMBER VARCHAR2(10) NOT NULL,
  IS_LAST_VERSION NUMBER(1,0) NOT NULL,
  PRODUCTION_VALIDATION_DATE_TZ VARCHAR2(50),
  PRODUCTION_VALIDATION_DATE TIMESTAMP,
  PRODUCTION_VALIDATION_USER VARCHAR2(255),
  DIFFUSION_VALIDATION_DATE_TZ VARCHAR2(50),
  DIFFUSION_VALIDATION_DATE TIMESTAMP,
  DIFFUSION_VALIDATION_USER VARCHAR2(255),
  PUBLICATION_DATE_TZ VARCHAR2(50),
  PUBLICATION_DATE TIMESTAMP,
  PUBLICATION_USER VARCHAR2(255),
  ARCHIVE_DATE_TZ VARCHAR2(50),
  ARCHIVE_DATE TIMESTAMP,
  ARCHIVE_USER VARCHAR2(255),
  UUID VARCHAR2(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR2(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR2(50),
  LAST_UPDATED_TZ VARCHAR2(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR2(50),
  VERSION NUMBER(19) NOT NULL,
  INDICATORS_SYSTEM_FK NUMBER(19) NOT NULL,
  PROC_STATUS VARCHAR2(255) NOT NULL
);


    	
CREATE TABLE TB_ELEMENTS_LEVELS (
  ID NUMBER(19) NOT NULL,
  ORDER_IN_LEVEL NUMBER(19) NOT NULL,
  UUID VARCHAR2(36) NOT NULL,
  VERSION NUMBER(19) NOT NULL,
  PARENT_FK NUMBER(19),
  DIMENSION_FK NUMBER(19),
  INDICATOR_INSTANCE_FK NUMBER(19),
  IND_SYSTEM_VERSION_ALL_FK NUMBER(19) NOT NULL,
  IND_SYSTEM_VERSION_FIRST_FK NUMBER(19)
);


    	
CREATE TABLE TB_DIMENSIONS (
  ID NUMBER(19) NOT NULL,
  UPDATE_DATE_TZ VARCHAR2(50),
  UPDATE_DATE TIMESTAMP,
  UUID VARCHAR2(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR2(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR2(50),
  LAST_UPDATED_TZ VARCHAR2(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR2(50),
  VERSION NUMBER(19) NOT NULL,
  TITLE_FK NUMBER(19) NOT NULL
);


    	
CREATE TABLE TB_INDIC_INST_LAST_VALUE (
  ID NUMBER(19) NOT NULL,
  LAST_TIME_VALUE VARCHAR2(255) NOT NULL,
  LAST_DATA_UPDATED_TZ VARCHAR2(50)
NOT NULL,
  LAST_DATA_UPDATED TIMESTAMP NOT NULL,
  GEOGRAPHICAL_VALUE_FK NUMBER(19) NOT NULL,
  INDICATOR_INSTANCE_FK NUMBER(19) NOT NULL
);


    	
CREATE TABLE TB_INDIC_VERSION_LAST_VALUE (
  ID NUMBER(19) NOT NULL,
  LAST_TIME_VALUE VARCHAR2(255) NOT NULL,
  LAST_DATA_UPDATED_TZ VARCHAR2(50)
NOT NULL,
  LAST_DATA_UPDATED TIMESTAMP NOT NULL,
  GEOGRAPHICAL_VALUE_FK NUMBER(19) NOT NULL,
  INDICATOR_VERSION_FK NUMBER(19) NOT NULL
);


    	
CREATE TABLE TB_INDICATORS_SYSTEMS_HIST (
  ID NUMBER(19) NOT NULL,
  VERSION_NUMBER VARCHAR2(255) NOT NULL,
  PUBLICATION_DATE_TZ VARCHAR2(50)
NOT NULL,
  PUBLICATION_DATE TIMESTAMP NOT NULL,
  INDICATORS_SYSTEM_FK NUMBER(19) NOT NULL
);



    	
CREATE TABLE TB_TRANSLATIONS (
  ID NUMBER(19) NOT NULL,
  CODE VARCHAR2(255) NOT NULL,
  TITLE_FK NUMBER(19) NOT NULL,
  TITLE_SUMMARY_FK NUMBER(19)
);


    	
CREATE TABLE TB_LIS_UNITS_MULTIPLIERS (
  ID NUMBER(19) NOT NULL,
  UNIT_MULTIPLIER NUMBER(10),
  UUID VARCHAR2(36) NOT NULL,
  TITLE_FK NUMBER(19)
);


    	
CREATE TABLE TB_EXTERNAL_ITEMS (
  ID NUMBER(19) NOT NULL,
  CODE VARCHAR2(255) NOT NULL,
  URI VARCHAR2(4000) NOT NULL,
  URN VARCHAR2(4000) NOT NULL,
  MANAGEMENT_APP_URL VARCHAR2(4000),
  VERSION NUMBER(19) NOT NULL,
  TITLE_FK NUMBER(19),
  TYPE VARCHAR2(255) NOT NULL
);


    	
CREATE TABLE TB_LOCALISED_STRINGS (
  ID NUMBER(19) NOT NULL,
  LABEL VARCHAR2(4000) NOT NULL,
  LOCALE VARCHAR2(255) NOT NULL,
  IS_UNMODIFIABLE NUMBER(1,0),
  VERSION NUMBER(19) NOT NULL,
  INTERNATIONAL_STRING_FK NUMBER(19) NOT NULL
);



-- Create many to many relations


-- Primary keys
    
		
ALTER TABLE TB_CONFIGURATION ADD CONSTRAINT PK_TB_CONFIGURATION
	PRIMARY KEY (ID)
;


		
ALTER TABLE TB_INTERNATIONAL_STRINGS ADD CONSTRAINT PK_TB_INTERNATIONAL_STRINGS
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_INDICATORS ADD CONSTRAINT PK_TB_INDICATORS
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_LIS_QUANTITIES_UNITS ADD CONSTRAINT PK_TB_LIS_QUANTITIES_UNITS
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_LIS_GEOGR_GRANULARITIES ADD CONSTRAINT PK_TB_LIS_GEOGR_GRANULARITIES
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_LIS_GEOGR_VALUES ADD CONSTRAINT PK_TB_LIS_GEOGR_VALUES
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_QUANTITIES ADD CONSTRAINT PK_TB_QUANTITIES
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_INDICATORS_VERSIONS ADD CONSTRAINT PK_TB_INDICATORS_VERSIONS
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_RATES_DERIVATIONS ADD CONSTRAINT PK_TB_RATES_DERIVATIONS
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_DATA_SOURCES ADD CONSTRAINT PK_TB_DATA_SOURCES
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_DATA_SOURCES_VARIABLES ADD CONSTRAINT PK_TB_DATA_SOURCES_VARIABLES
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_INDICATORS_INSTANCES ADD CONSTRAINT PK_TB_INDICATORS_INSTANCES
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_INDICATORS_SYSTEMS ADD CONSTRAINT PK_TB_INDICATORS_SYSTEMS
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_INDIC_SYSTEMS_VERSIONS ADD CONSTRAINT PK_TB_INDIC_SYSTEMS_VERSIONS
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_ELEMENTS_LEVELS ADD CONSTRAINT PK_TB_ELEMENTS_LEVELS
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_DIMENSIONS ADD CONSTRAINT PK_TB_DIMENSIONS
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_INDIC_INST_LAST_VALUE ADD CONSTRAINT PK_TB_INDIC_INST_LAST_VALUE
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_INDIC_VERSION_LAST_VALUE ADD CONSTRAINT PK_TB_INDIC_VERSION_LAST_VALUE
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_INDICATORS_SYSTEMS_HIST ADD CONSTRAINT PK_TB_INDICATORS_SYSTEMS_HIST
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_TRANSLATIONS ADD CONSTRAINT PK_TB_TRANSLATIONS
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_LIS_UNITS_MULTIPLIERS ADD CONSTRAINT PK_TB_LIS_UNITS_MULTIPLIERS
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_EXTERNAL_ITEMS ADD CONSTRAINT PK_TB_EXTERNAL_ITEMS
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_LOCALISED_STRINGS ADD CONSTRAINT PK_TB_LOCALISED_STRINGS
	PRIMARY KEY (ID)
;

    
ALTER TABLE TB_INDIC_INST_GEO_VALUES ADD CONSTRAINT PK_TB_INDIC_INST_GEO_VALUES
	PRIMARY KEY (GEOGRAPHICAL_VALUE_FK, INDICATOR_INSTANCE_FK)
;


-- Unique constraints
    




ALTER TABLE TB_INDICATORS
    ADD CONSTRAINT UQ_TB_INDICATORS UNIQUE (UUID)
;



ALTER TABLE TB_LIS_QUANTITIES_UNITS
    ADD CONSTRAINT UQ_TB_LIS_QUANTITIES_UNITS UNIQUE (UUID)
;



ALTER TABLE TB_LIS_GEOGR_GRANULARITIES
    ADD CONSTRAINT UQ_TB_LIS_GEOGR_GRANULARITIES UNIQUE (UUID)
;



ALTER TABLE TB_LIS_GEOGR_VALUES
    ADD CONSTRAINT UQ_TB_LIS_GEOGR_VALUES UNIQUE (UUID)
;



ALTER TABLE TB_QUANTITIES
    ADD CONSTRAINT UQ_TB_QUANTITIES UNIQUE (UUID)
;



ALTER TABLE TB_INDICATORS_VERSIONS
    ADD CONSTRAINT UQ_TB_INDICATORS_VERSIONS UNIQUE (UUID)
;



ALTER TABLE TB_RATES_DERIVATIONS
    ADD CONSTRAINT UQ_TB_RATES_DERIVATIONS UNIQUE (UUID)
;



ALTER TABLE TB_DATA_SOURCES
    ADD CONSTRAINT UQ_TB_DATA_SOURCES UNIQUE (UUID)
;



ALTER TABLE TB_DATA_SOURCES_VARIABLES
    ADD CONSTRAINT UQ_TB_DATA_SOURCES_VARIABLES UNIQUE (UUID)
;



ALTER TABLE TB_INDICATORS_INSTANCES
    ADD CONSTRAINT UQ_TB_INDICATORS_INSTANCES UNIQUE (UUID)
;



ALTER TABLE TB_INDICATORS_SYSTEMS
    ADD CONSTRAINT UQ_TB_INDICATORS_SYSTEMS UNIQUE (UUID)
;



ALTER TABLE TB_INDIC_SYSTEMS_VERSIONS
    ADD CONSTRAINT UQ_TB_INDIC_SYSTEMS_VERSIONS UNIQUE (UUID)
;



ALTER TABLE TB_ELEMENTS_LEVELS
    ADD CONSTRAINT UQ_TB_ELEMENTS_LEVELS UNIQUE (UUID)
;



ALTER TABLE TB_DIMENSIONS
    ADD CONSTRAINT UQ_TB_DIMENSIONS UNIQUE (UUID)
;



ALTER TABLE TB_INDIC_INST_LAST_VALUE
	ADD CONSTRAINT UQ_TB_INDIC_INST_LAST_VALUE UNIQUE (GEOGRAPHICAL_VALUE_FK, INDICATOR_INSTANCE_FK)
;



ALTER TABLE TB_INDIC_VERSION_LAST_VALUE
	ADD CONSTRAINT UQ_TB_INDIC_VERSION_LAST_VALUE UNIQUE (GEOGRAPHICAL_VALUE_FK, INDICATOR_VERSION_FK)
;



ALTER TABLE TB_INDICATORS_SYSTEMS_HIST
	ADD CONSTRAINT UQ_TB_INDICATORS_SYSTEMS_HIST UNIQUE (VERSION_NUMBER, INDICATORS_SYSTEM_FK)
;




ALTER TABLE TB_LIS_UNITS_MULTIPLIERS
    ADD CONSTRAINT UQ_TB_LIS_UNITS_MULTIPLIERS UNIQUE (UUID)
;





-- Foreign key constraints
    

  
  
  
  
  
  
  
  
ALTER TABLE TB_LIS_QUANTITIES_UNITS ADD CONSTRAINT FK_TB_LIS_QUANTITIES_UNITS_T68
	FOREIGN KEY (TITLE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;

  
ALTER TABLE TB_LIS_GEOGR_GRANULARITIES ADD CONSTRAINT FK_TB_LIS_GEOGR_GRANULARITIE56
	FOREIGN KEY (TITLE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;

  
ALTER TABLE TB_LIS_GEOGR_VALUES ADD CONSTRAINT FK_TB_LIS_GEOGR_VALUES_TITLE28
	FOREIGN KEY (TITLE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_LIS_GEOGR_VALUES ADD CONSTRAINT FK_TB_LIS_GEOGR_VALUES_GRANU86
	FOREIGN KEY (GRANULARITY_FK) REFERENCES TB_LIS_GEOGR_GRANULARITIES (ID)
;

  
ALTER TABLE TB_QUANTITIES ADD CONSTRAINT FK_TB_QUANTITIES_PERCENTAGE_15
	FOREIGN KEY (PERCENTAGE_OF_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_QUANTITIES ADD CONSTRAINT FK_TB_QUANTITIES_UNIT_FK
	FOREIGN KEY (UNIT_FK) REFERENCES TB_LIS_QUANTITIES_UNITS (ID)
;
ALTER TABLE TB_QUANTITIES ADD CONSTRAINT FK_TB_QUANTITIES_NUMERATOR_FK
	FOREIGN KEY (NUMERATOR_FK) REFERENCES TB_INDICATORS (ID)
;
ALTER TABLE TB_QUANTITIES ADD CONSTRAINT FK_TB_QUANTITIES_DENOMINATOR57
	FOREIGN KEY (DENOMINATOR_FK) REFERENCES TB_INDICATORS (ID)
;
ALTER TABLE TB_QUANTITIES ADD CONSTRAINT FK_TB_QUANTITIES_BASE_QUANTI92
	FOREIGN KEY (BASE_QUANTITY_FK) REFERENCES TB_INDICATORS (ID)
;
ALTER TABLE TB_QUANTITIES ADD CONSTRAINT FK_TB_QUANTITIES_BASE_LOCATI14
	FOREIGN KEY (BASE_LOCATION_FK) REFERENCES TB_LIS_GEOGR_VALUES (ID)
;

  
ALTER TABLE TB_INDICATORS_VERSIONS ADD CONSTRAINT FK_TB_INDICATORS_VERSIONS_TI18
	FOREIGN KEY (TITLE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_INDICATORS_VERSIONS ADD CONSTRAINT FK_TB_INDICATORS_VERSIONS_AC61
	FOREIGN KEY (ACRONYM_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_INDICATORS_VERSIONS ADD CONSTRAINT FK_TB_INDICATORS_VERSIONS_SU53
	FOREIGN KEY (SUBJECT_TITLE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_INDICATORS_VERSIONS ADD CONSTRAINT FK_TB_INDICATORS_VERSIONS_CO81
	FOREIGN KEY (CONCEPT_DESCRIPTION_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_INDICATORS_VERSIONS ADD CONSTRAINT FK_TB_INDICATORS_VERSIONS_CO50
	FOREIGN KEY (COMMENTS_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_INDICATORS_VERSIONS ADD CONSTRAINT FK_TB_INDICATORS_VERSIONS_NO19
	FOREIGN KEY (NOTES_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_INDICATORS_VERSIONS ADD CONSTRAINT FK_TB_INDICATORS_VERSIONS_IN37
	FOREIGN KEY (INDICATOR_FK) REFERENCES TB_INDICATORS (ID)
;
ALTER TABLE TB_INDICATORS_VERSIONS ADD CONSTRAINT FK_TB_INDICATORS_VERSIONS_QU99
	FOREIGN KEY (QUANTITY_FK) REFERENCES TB_QUANTITIES (ID)
;

  
ALTER TABLE TB_RATES_DERIVATIONS ADD CONSTRAINT FK_TB_RATES_DERIVATIONS_QUAN21
	FOREIGN KEY (QUANTITY_FK) REFERENCES TB_QUANTITIES (ID)
;

  
ALTER TABLE TB_DATA_SOURCES ADD CONSTRAINT FK_TB_DATA_SOURCES_SOURCE_SU24
	FOREIGN KEY (SOURCE_SURVEY_TITLE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_DATA_SOURCES ADD CONSTRAINT FK_TB_DATA_SOURCES_SOURCE_SU31
	FOREIGN KEY (SOURCE_SURVEY_ACRONYM_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_DATA_SOURCES ADD CONSTRAINT FK_TB_DATA_SOURCES_INDICATOR85
	FOREIGN KEY (INDICATOR_VERSION_FK) REFERENCES TB_INDICATORS_VERSIONS (ID)
;
ALTER TABLE TB_DATA_SOURCES ADD CONSTRAINT FK_TB_DATA_SOURCES_ANNUAL_PU85
	FOREIGN KEY (ANNUAL_PUNTUAL_RATE_FK) REFERENCES TB_RATES_DERIVATIONS (ID)
;
ALTER TABLE TB_DATA_SOURCES ADD CONSTRAINT FK_TB_DATA_SOURCES_ANNUAL_PE96
	FOREIGN KEY (ANNUAL_PERCENTAGE_RATE_FK) REFERENCES TB_RATES_DERIVATIONS (ID)
;
ALTER TABLE TB_DATA_SOURCES ADD CONSTRAINT FK_TB_DATA_SOURCES_INTERPERI49
	FOREIGN KEY (INTERPERIOD_PUNTUAL_RATE_FK) REFERENCES TB_RATES_DERIVATIONS (ID)
;
ALTER TABLE TB_DATA_SOURCES ADD CONSTRAINT FK_TB_DATA_SOURCES_INTERPERI64
	FOREIGN KEY (INTERPERIOD_PERCENTAGE_RATE_FK) REFERENCES TB_RATES_DERIVATIONS (ID)
;
ALTER TABLE TB_DATA_SOURCES ADD CONSTRAINT FK_TB_DATA_SOURCES_GEOGRAPHI67
	FOREIGN KEY (GEOGRAPHICAL_VALUE_FK) REFERENCES TB_LIS_GEOGR_VALUES (ID)
;

ALTER TABLE TB_DATA_SOURCES_VARIABLES ADD CONSTRAINT FK_TB_DATA_SOURCES_VARIABLES25
	FOREIGN KEY (DATA_SOURCE_FK) REFERENCES TB_DATA_SOURCES (ID)
;

  
  
ALTER TABLE TB_INDICATORS_INSTANCES ADD CONSTRAINT FK_TB_INDICATORS_INSTANCES_T05
	FOREIGN KEY (TITLE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_INDICATORS_INSTANCES ADD CONSTRAINT FK_TB_INDICATORS_INSTANCES_I52
	FOREIGN KEY (INDICATOR_FK) REFERENCES TB_INDICATORS (ID)
;
ALTER TABLE TB_INDICATORS_INSTANCES ADD CONSTRAINT FK_TB_INDICATORS_INSTANCES_G00
	FOREIGN KEY (GEOGRAPHICAL_GRANULARITY_FK) REFERENCES TB_LIS_GEOGR_GRANULARITIES (ID)
;

  
  
  
ALTER TABLE TB_INDIC_SYSTEMS_VERSIONS ADD CONSTRAINT FK_TB_INDIC_SYSTEMS_VERSIONS40
	FOREIGN KEY (INDICATORS_SYSTEM_FK) REFERENCES TB_INDICATORS_SYSTEMS (ID)
;

  
ALTER TABLE TB_ELEMENTS_LEVELS ADD CONSTRAINT FK_TB_ELEMENTS_LEVELS_PARENT65
	FOREIGN KEY (PARENT_FK) REFERENCES TB_ELEMENTS_LEVELS (ID)
;
ALTER TABLE TB_ELEMENTS_LEVELS ADD CONSTRAINT FK_TB_ELEMENTS_LEVELS_DIMENS53
	FOREIGN KEY (DIMENSION_FK) REFERENCES TB_DIMENSIONS (ID)
;
ALTER TABLE TB_ELEMENTS_LEVELS ADD CONSTRAINT FK_TB_ELEMENTS_LEVELS_INDICA54
	FOREIGN KEY (INDICATOR_INSTANCE_FK) REFERENCES TB_INDICATORS_INSTANCES (ID)
;
ALTER TABLE TB_ELEMENTS_LEVELS ADD CONSTRAINT FK_TB_ELEMENTS_LEVELS_IND_SY93
	FOREIGN KEY (IND_SYSTEM_VERSION_ALL_FK) REFERENCES TB_INDIC_SYSTEMS_VERSIONS (ID)
;
ALTER TABLE TB_ELEMENTS_LEVELS ADD CONSTRAINT FK_TB_ELEMENTS_LEVELS_IND_SY10
	FOREIGN KEY (IND_SYSTEM_VERSION_FIRST_FK) REFERENCES TB_INDIC_SYSTEMS_VERSIONS (ID)
;

  
ALTER TABLE TB_DIMENSIONS ADD CONSTRAINT FK_TB_DIMENSIONS_TITLE_FK
	FOREIGN KEY (TITLE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;

  
ALTER TABLE TB_INDIC_INST_LAST_VALUE ADD CONSTRAINT FK_TB_INDIC_INST_LAST_VALUE_43
	FOREIGN KEY (GEOGRAPHICAL_VALUE_FK) REFERENCES TB_LIS_GEOGR_VALUES (ID)
;
ALTER TABLE TB_INDIC_INST_LAST_VALUE ADD CONSTRAINT FK_TB_INDIC_INST_LAST_VALUE_86
	FOREIGN KEY (INDICATOR_INSTANCE_FK) REFERENCES TB_INDICATORS_INSTANCES (ID)
;

  
ALTER TABLE TB_INDIC_VERSION_LAST_VALUE ADD CONSTRAINT FK_TB_INDIC_VERSION_LAST_VAL07
	FOREIGN KEY (GEOGRAPHICAL_VALUE_FK) REFERENCES TB_LIS_GEOGR_VALUES (ID)
;
ALTER TABLE TB_INDIC_VERSION_LAST_VALUE ADD CONSTRAINT FK_TB_INDIC_VERSION_LAST_VAL93
	FOREIGN KEY (INDICATOR_VERSION_FK) REFERENCES TB_INDICATORS_VERSIONS (ID)
;

  
ALTER TABLE TB_INDICATORS_SYSTEMS_HIST ADD CONSTRAINT FK_TB_INDICATORS_SYSTEMS_HIS62
	FOREIGN KEY (INDICATORS_SYSTEM_FK) REFERENCES TB_INDICATORS_SYSTEMS (ID)
;

  
ALTER TABLE TB_TRANSLATIONS ADD CONSTRAINT FK_TB_TRANSLATIONS_TITLE_FK
	FOREIGN KEY (TITLE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_TRANSLATIONS ADD CONSTRAINT FK_TB_TRANSLATIONS_TITLE_SUM77
	FOREIGN KEY (TITLE_SUMMARY_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;

  
ALTER TABLE TB_LIS_UNITS_MULTIPLIERS ADD CONSTRAINT FK_TB_LIS_UNITS_MULTIPLIERS_37
	FOREIGN KEY (TITLE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;

  
ALTER TABLE TB_EXTERNAL_ITEMS ADD CONSTRAINT FK_TB_EXTERNAL_ITEMS_TITLE_FK
	FOREIGN KEY (TITLE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;

  
ALTER TABLE TB_LOCALISED_STRINGS ADD CONSTRAINT FK_TB_LOCALISED_STRINGS_INTE13
	FOREIGN KEY (INTERNATIONAL_STRING_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;

  

ALTER TABLE TB_INDIC_INST_GEO_VALUES ADD CONSTRAINT FK_TB_INDIC_INST_GEO_VALUES_75
	FOREIGN KEY (GEOGRAPHICAL_VALUE_FK) REFERENCES TB_LIS_GEOGR_VALUES (ID)
;
ALTER TABLE TB_INDIC_INST_GEO_VALUES ADD CONSTRAINT FK_TB_INDIC_INST_GEO_VALUES_46
	FOREIGN KEY (INDICATOR_INSTANCE_FK) REFERENCES TB_INDICATORS_INSTANCES (ID)
;

  


-- Index
  
  
  
  


    