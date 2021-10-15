-- ###########################################
-- # Create
-- ###########################################
-- Create pk sequence
    


-- Create normal entities
    
        
CREATE TABLE TB_CONFIGURATION (
  ID BIGINT NOT NULL,
  LAST_SUCCESSFUL_GPE_QUERY56 TIMESTAMP NOT NULL
);



    	
CREATE TABLE TB_INTERNATIONAL_STRINGS (
  ID BIGINT NOT NULL,
  VERSION BIGINT NOT NULL
);


    	
CREATE TABLE TB_EXTERNAL_ITEMS (
  ID BIGINT NOT NULL,
  CODE VARCHAR(255) NOT NULL,
  CODE_NESTED VARCHAR(255),
  URI VARCHAR(4000) NOT NULL,
  URN VARCHAR(4000),
  URN_PROVIDER VARCHAR(4000),
  MANAGEMENT_APP_URL VARCHAR(4000),
  VERSION BIGINT NOT NULL,
  TITLE_FK BIGINT,
  TYPE VARCHAR(255) NOT NULL
);


    	
CREATE TABLE TB_INDICATORS (
  ID BIGINT NOT NULL,
  CODE VARCHAR(255) NOT NULL,
  VIEW_CODE VARCHAR(30) NOT NULL,
  PRODUCTION_ID BIGINT,
  PRODUCTION_VERSION_NUMBER VARCHAR(10),
  DIFFUSION_ID BIGINT,
  DIFFUSION_VERSION_NUMBER VARCHAR(10),
  IS_PUBLISHED BOOLEAN NOT NULL,
  NOTIFY_POPULATION_ERRORS BOOLEAN NOT NULL,
  UUID VARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR(50),
  VERSION BIGINT NOT NULL,
  PRODUCTION_PROC_STATUS VARCHAR(255),
  DIFFUSION_PROC_STATUS VARCHAR(255)
);


    	
CREATE TABLE TB_LIS_UNITS_MULTIPLIERS (
  ID BIGINT NOT NULL,
  UNIT_MULTIPLIER INTEGER,
  UPDATE_DATE_TZ VARCHAR(50),
  UPDATE_DATE TIMESTAMP,
  UUID VARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR(50),
  VERSION BIGINT NOT NULL,
  TITLE_FK BIGINT
);


    	
CREATE TABLE TB_LIS_QUANTITIES_UNITS (
  ID BIGINT NOT NULL,
  SYMBOL VARCHAR(255),
  UPDATE_DATE_TZ VARCHAR(50),
  UPDATE_DATE TIMESTAMP,
  UUID VARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR(50),
  VERSION BIGINT NOT NULL,
  TITLE_FK BIGINT NOT NULL,
  SYMBOL_POSITION VARCHAR(255)
);


    	
CREATE TABLE TB_LIS_GEOGR_GRANULARITIES (
  ID BIGINT NOT NULL,
  CODE VARCHAR(255) NOT NULL,
  UPDATE_DATE_TZ VARCHAR(50),
  UPDATE_DATE TIMESTAMP,
  UUID VARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR(50),
  VERSION BIGINT NOT NULL,
  TITLE_FK BIGINT
);


    	
CREATE TABLE TB_LIS_GEOGR_VALUES (
  ID BIGINT NOT NULL,
  CODE VARCHAR(255) NOT NULL,
  LATITUDE FLOAT8,
  LONGITUDE FLOAT8,
  GLOBAL_ORDER VARCHAR(255) NOT NULL,
  UPDATE_DATE_TZ VARCHAR(50),
  UPDATE_DATE TIMESTAMP,
  UUID VARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR(50),
  VERSION BIGINT NOT NULL,
  TITLE_FK BIGINT,
  GRANULARITY_FK BIGINT NOT NULL
);


    	
CREATE TABLE TB_QUANTITIES (
  ID BIGINT NOT NULL,
  SIGNIFICANT_DIGITS INTEGER,
  DECIMAL_PLACES INTEGER,
  MINIMUM INTEGER,
  MAXIMUM INTEGER,
  IS_PERCENTAGE BOOLEAN,
  BASE_VALUE INTEGER,
  BASE_TIME VARCHAR(255),
  UUID VARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR(50),
  VERSION BIGINT NOT NULL,
  UNIT_MULTIPLIER_FK BIGINT NOT NULL,
  PERCENTAGE_OF_FK BIGINT,
  UNIT_FK BIGINT,
  NUMERATOR_FK BIGINT,
  DENOMINATOR_FK BIGINT,
  BASE_QUANTITY_FK BIGINT,
  BASE_LOCATION_FK BIGINT,
  QUANTITY_TYPE VARCHAR(255)
);


    	
CREATE TABLE TB_INDICATORS_VERSIONS (
  ID BIGINT NOT NULL,
  VERSION_NUMBER VARCHAR(10) NOT NULL,
  SUBJECT_CODE VARCHAR(255) NOT NULL,
  IS_LAST_VERSION BOOLEAN NOT NULL,
  DATA_REPOSITORY_ID VARCHAR(255),
  DATA_REPOSITORY_TABLE_NAME VARCHAR(255),
  NEEDS_UPDATE BOOLEAN NOT NULL,
  PRODUCTION_VALIDATION_DATE_TZ VARCHAR(50),
  PRODUCTION_VALIDATION_DATE TIMESTAMP,
  PRODUCTION_VALIDATION_USER VARCHAR(255),
  DIFFUSION_VALIDATION_DATE_TZ VARCHAR(50),
  DIFFUSION_VALIDATION_DATE TIMESTAMP,
  DIFFUSION_VALIDATION_USER VARCHAR(255),
  PUBLICATION_DATE_TZ VARCHAR(50),
  PUBLICATION_DATE TIMESTAMP,
  PUBLICATION_USER VARCHAR(255),
  PUBLICATION_FAILED_DATE_TZ VARCHAR(50),
  PUBLICATION_FAILED_DATE TIMESTAMP,
  PUBLICATION_FAILED_USER VARCHAR(255),
  ARCHIVE_DATE_TZ VARCHAR(50),
  ARCHIVE_DATE TIMESTAMP,
  ARCHIVE_USER VARCHAR(255),
  UPDATE_DATE_TZ VARCHAR(50),
  UPDATE_DATE TIMESTAMP,
  LAST_POPULATE_DATE_TZ VARCHAR(50),
  LAST_POPULATE_DATE TIMESTAMP,
  UUID VARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR(50),
  VERSION BIGINT NOT NULL,
  TITLE_FK BIGINT NOT NULL,
  ACRONYM_FK BIGINT,
  SUBJECT_TITLE_FK BIGINT NOT NULL,
  CONCEPT_DESCRIPTION_FK BIGINT,
  COMMENTS_FK BIGINT,
  NOTES_FK BIGINT,
  INDICATOR_FK BIGINT NOT NULL,
  QUANTITY_FK BIGINT NOT NULL,
  PROC_STATUS VARCHAR(255) NOT NULL
);


    	
CREATE TABLE TB_RATES_DERIVATIONS (
  ID BIGINT NOT NULL,
  METHOD VARCHAR(255) NOT NULL,
  UUID VARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR(50),
  VERSION BIGINT NOT NULL,
  QUANTITY_FK BIGINT NOT NULL,
  METHOD_TYPE VARCHAR(255) NOT NULL,
  ROUNDING VARCHAR(255)
);


    	
CREATE TABLE TB_DATA_SOURCES (
  ID BIGINT NOT NULL,
  QUERY_UUID VARCHAR(255) NOT NULL,
  QUERY_URN VARCHAR(255) NOT NULL,
  TIME_VARIABLE VARCHAR(255),
  TIME_VALUE VARCHAR(255),
  GEOGRAPHICAL_VARIABLE VARCHAR(255),
  ABSOLUTE_METHOD VARCHAR(255),
  SOURCE_SURVEY_CODE VARCHAR(255) NOT NULL,
  SOURCE_SURVEY_URL VARCHAR(4000),
  PUBLISHERS VARCHAR(4000) NOT NULL,
  UPDATE_DATE_TZ VARCHAR(50),
  UPDATE_DATE TIMESTAMP,
  UUID VARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR(50),
  VERSION BIGINT NOT NULL,
  SOURCE_SURVEY_TITLE_FK BIGINT NOT NULL,
  SOURCE_SURVEY_ACRONYM_FK BIGINT,
  STAT_RESOURCE_FK BIGINT,
  INDICATOR_VERSION_FK BIGINT,
  ANNUAL_PUNTUAL_RATE_FK BIGINT,
  ANNUAL_PERCENTAGE_RATE_FK BIGINT,
  INTERPERIOD_PUNTUAL_RATE_FK BIGINT,
  INTERPERIOD_PERCENTAGE_RATE_FK BIGINT,
  GEOGRAPHICAL_VALUE_FK BIGINT,
  QUERY_ENVIRONMENT VARCHAR(255) NOT NULL,
  QUERY_TEXT VARCHAR(255) NULL
);


    	
CREATE TABLE TB_DATA_SOURCES_VARIABLES (
  ID BIGINT NOT NULL,
  VARIABLE VARCHAR(255) NOT NULL,
  CATEGORY VARCHAR(255) NOT NULL,
  UUID VARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR(50),
  VERSION BIGINT NOT NULL,
  DATA_SOURCE_FK BIGINT
);


    	
CREATE TABLE TB_INDICATORS_INSTANCES (
  ID BIGINT NOT NULL,
  CODE VARCHAR(255) NOT NULL,
  TIME_VALUES TEXT,
  UPDATE_DATE_TZ VARCHAR(50),
  UPDATE_DATE TIMESTAMP,
  LAST_POPULATE_DATE_TZ VARCHAR(50),
  LAST_POPULATE_DATE TIMESTAMP,
  UUID VARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR(50),
  VERSION BIGINT NOT NULL,
  TITLE_FK BIGINT NOT NULL,
  INDICATOR_FK BIGINT NOT NULL,
  GEOGRAPHICAL_GRANULARITY_FK BIGINT,
  TIME_GRANULARITY VARCHAR(255)
);


    	
CREATE TABLE TB_INDICATORS_SYSTEMS (
  ID BIGINT NOT NULL,
  CODE VARCHAR(255) NOT NULL,
  IS_PUBLISHED BOOLEAN NOT NULL,
  UUID VARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR(50),
  VERSION BIGINT NOT NULL,
  PRODUCTION_ID BIGINT,
  PRODUCTION_VERSION_NUMBER VARCHAR(10),
  DIFFUSION_ID BIGINT,
  DIFFUSION_VERSION_NUMBER VARCHAR(10)
);


    	
CREATE TABLE TB_INDIC_SYSTEMS_VERSIONS (
  ID BIGINT NOT NULL,
  VERSION_NUMBER VARCHAR(10) NOT NULL,
  IS_LAST_VERSION BOOLEAN NOT NULL,
  PRODUCTION_VALIDATION_DATE_TZ VARCHAR(50),
  PRODUCTION_VALIDATION_DATE TIMESTAMP,
  PRODUCTION_VALIDATION_USER VARCHAR(255),
  DIFFUSION_VALIDATION_DATE_TZ VARCHAR(50),
  DIFFUSION_VALIDATION_DATE TIMESTAMP,
  DIFFUSION_VALIDATION_USER VARCHAR(255),
  PUBLICATION_DATE_TZ VARCHAR(50),
  PUBLICATION_DATE TIMESTAMP,
  PUBLICATION_USER VARCHAR(255),
  ARCHIVE_DATE_TZ VARCHAR(50),
  ARCHIVE_DATE TIMESTAMP,
  ARCHIVE_USER VARCHAR(255),
  UUID VARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR(50),
  VERSION BIGINT NOT NULL,
  INDICATORS_SYSTEM_FK BIGINT NOT NULL,
  PROC_STATUS VARCHAR(255) NOT NULL
);


    	
CREATE TABLE TB_ELEMENTS_LEVELS (
  ID BIGINT NOT NULL,
  ORDER_IN_LEVEL BIGINT NOT NULL,
  UUID VARCHAR(36) NOT NULL,
  VERSION BIGINT NOT NULL,
  PARENT_FK BIGINT,
  DIMENSION_FK BIGINT,
  INDICATOR_INSTANCE_FK BIGINT,
  IND_SYSTEM_VERSION_ALL_FK BIGINT NOT NULL,
  IND_SYSTEM_VERSION_FIRST_FK BIGINT
);


    	
CREATE TABLE TB_DIMENSIONS (
  ID BIGINT NOT NULL,
  UPDATE_DATE_TZ VARCHAR(50),
  UPDATE_DATE TIMESTAMP,
  UUID VARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR(50),
  VERSION BIGINT NOT NULL,
  TITLE_FK BIGINT NOT NULL
);


    	
CREATE TABLE TB_INDIC_INST_LAST_VALUE (
  ID BIGINT NOT NULL,
  GEOGRAPHICAL_CODE VARCHAR(255) NOT NULL,
  LAST_TIME_VALUE VARCHAR(255) NOT NULL,
  LAST_DATA_UPDATED_TZ VARCHAR(50) NOT NULL,
  LAST_DATA_UPDATED TIMESTAMP NOT NULL,
  INDICATOR_INSTANCE_FK BIGINT NOT NULL
);


    	
CREATE TABLE TB_IND_VERSION_GEO_COV (
  ID BIGINT NOT NULL,
  GEOGRAPHICAL_VALUE_FK BIGINT NOT NULL,
  INDICATOR_VERSION_FK BIGINT NOT NULL
);


    	
CREATE TABLE TB_INDIC_VERSION_LAST_VALUE (
  ID BIGINT NOT NULL,
  GEOGRAPHICAL_CODE VARCHAR(255) NOT NULL,
  LAST_TIME_VALUE VARCHAR(255) NOT NULL,
  LAST_DATA_UPDATED_TZ VARCHAR(50) NOT NULL,
  LAST_DATA_UPDATED TIMESTAMP NOT NULL,
  INDICATOR_VERSION_FK BIGINT NOT NULL
);


    	
CREATE TABLE TB_TRANSLATIONS (
  ID BIGINT NOT NULL,
  CODE VARCHAR(255) NOT NULL,
  TITLE_FK BIGINT NOT NULL,
  TITLE_SUMMARY_FK BIGINT
);


    	
CREATE TABLE TB_IND_VERSION_MEAS_COV (
  ID BIGINT NOT NULL,
  MEASURE_CODE VARCHAR(255) NOT NULL,
  INDICATOR_VERSION_FK BIGINT NOT NULL,
  TRANSLATION BIGINT
);


    	
CREATE TABLE TB_IND_VERSION_TIME_COV (
  ID BIGINT NOT NULL,
  TIME_VALUE VARCHAR(255) NOT NULL,
  TIME_GRANULARITY VARCHAR(255) NOT NULL,
  INDICATOR_VERSION_FK BIGINT NOT NULL,
  TRANSLATION BIGINT,
  GRANULARITY_TRANSLATION BIGINT
);


    	
CREATE TABLE TB_INDICATORS_SYSTEMS_HIST (
  ID BIGINT NOT NULL,
  VERSION_NUMBER VARCHAR(255) NOT NULL,
  PUBLICATION_DATE_TZ VARCHAR(50) NOT NULL,
  PUBLICATION_DATE TIMESTAMP NOT NULL,
  INDICATORS_SYSTEM_FK BIGINT NOT NULL
);


    	
CREATE TABLE TB_LOCALISED_STRINGS (
  ID BIGINT NOT NULL,
  LABEL VARCHAR(4000) NOT NULL,
  LOCALE VARCHAR(255) NOT NULL,
  IS_UNMODIFIABLE BOOLEAN,
  VERSION BIGINT NOT NULL,
  INTERNATIONAL_STRING_FK BIGINT NOT NULL
);


    	
CREATE TABLE TB_TASKS (
  ID BIGINT NOT NULL,
  JOB VARCHAR(4000) NOT NULL,
  EXTENSION_POINT VARCHAR(4000),
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR(50),
  VERSION BIGINT NOT NULL,
  STATUS VARCHAR(255) NOT NULL
);



-- Create many to many relations
    
    	
CREATE TABLE TB_INDIC_INST_GEO_VALUES (
  GEOGRAPHICAL_VALUE_FK BIGINT NOT NULL,
  INDICATOR_INSTANCE_FK BIGINT NOT NULL
);



-- Primary keys
    
		
ALTER TABLE TB_CONFIGURATION ADD CONSTRAINT PK_TB_CONFIGURATION
	PRIMARY KEY (ID)
;


		
ALTER TABLE TB_INTERNATIONAL_STRINGS ADD CONSTRAINT PK_TB_INTERNATIONAL_STRINGS
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_EXTERNAL_ITEMS ADD CONSTRAINT PK_TB_EXTERNAL_ITEMS
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_INDICATORS ADD CONSTRAINT PK_TB_INDICATORS
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_LIS_UNITS_MULTIPLIERS ADD CONSTRAINT PK_TB_LIS_UNITS_MULTIPLIERS
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

		
ALTER TABLE TB_IND_VERSION_GEO_COV ADD CONSTRAINT PK_TB_IND_VERSION_GEO_COV
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_INDIC_VERSION_LAST_VALUE ADD CONSTRAINT PK_TB_INDIC_VERSION_LAST_VALUE
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_TRANSLATIONS ADD CONSTRAINT PK_TB_TRANSLATIONS
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_IND_VERSION_MEAS_COV ADD CONSTRAINT PK_TB_IND_VERSION_MEAS_COV
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_IND_VERSION_TIME_COV ADD CONSTRAINT PK_TB_IND_VERSION_TIME_COV
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_INDICATORS_SYSTEMS_HIST ADD CONSTRAINT PK_TB_INDICATORS_SYSTEMS_HIST
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_LOCALISED_STRINGS ADD CONSTRAINT PK_TB_LOCALISED_STRINGS
	PRIMARY KEY (ID)
;

		
ALTER TABLE TB_TASKS ADD CONSTRAINT PK_TB_TASKS
	PRIMARY KEY (ID)
;

    
ALTER TABLE TB_INDIC_INST_GEO_VALUES ADD CONSTRAINT PK_TB_INDIC_INST_GEO_VALUES
	PRIMARY KEY (GEOGRAPHICAL_VALUE_FK, INDICATOR_INSTANCE_FK)
;


-- Unique constraints
    





ALTER TABLE TB_INDICATORS
    ADD CONSTRAINT UQ_TB_INDICATORS UNIQUE (UUID)
;



ALTER TABLE TB_LIS_UNITS_MULTIPLIERS
    ADD CONSTRAINT UQ_TB_LIS_UNITS_MULTIPLIERS UNIQUE (UUID)
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
	ADD CONSTRAINT UQ_TB_INDIC_INST_LAST_VALUE UNIQUE (GEOGRAPHICAL_CODE, INDICATOR_INSTANCE_FK)
;



ALTER TABLE TB_IND_VERSION_GEO_COV
	ADD CONSTRAINT UQ_TB_IND_VERSION_GEO_COV UNIQUE (GEOGRAPHICAL_VALUE_FK, INDICATOR_VERSION_FK)
;



ALTER TABLE TB_INDIC_VERSION_LAST_VALUE
	ADD CONSTRAINT UQ_TB_INDIC_VERSION_LAST_VALUE UNIQUE (GEOGRAPHICAL_CODE, INDICATOR_VERSION_FK)
;




ALTER TABLE TB_IND_VERSION_MEAS_COV
	ADD CONSTRAINT UQ_TB_IND_VERSION_MEAS_COV UNIQUE (MEASURE_CODE, INDICATOR_VERSION_FK)
;



ALTER TABLE TB_IND_VERSION_TIME_COV
	ADD CONSTRAINT UQ_TB_IND_VERSION_TIME_COV UNIQUE (TIME_VALUE, INDICATOR_VERSION_FK)
;



ALTER TABLE TB_INDICATORS_SYSTEMS_HIST
	ADD CONSTRAINT UQ_TB_INDICATORS_SYSTEMS_HIST UNIQUE (VERSION_NUMBER, INDICATORS_SYSTEM_FK)
;




ALTER TABLE TB_TASKS
	ADD CONSTRAINT UQ_TB_TASKS UNIQUE (JOB)
;



-- Foreign key constraints
    

  
  
  
  
  
  
ALTER TABLE TB_EXTERNAL_ITEMS ADD CONSTRAINT FK_TB_EXTERNAL_ITEMS_TITLE_FK
	FOREIGN KEY (TITLE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;

  
  
  
ALTER TABLE TB_LIS_UNITS_MULTIPLIERS ADD CONSTRAINT FK_TB_LIS_UNITS_MULTIPLIERS_TITLE_FK
	FOREIGN KEY (TITLE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;

  
ALTER TABLE TB_LIS_QUANTITIES_UNITS ADD CONSTRAINT FK_TB_LIS_QUANTITIES_UNITS_TITLE_FK
	FOREIGN KEY (TITLE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;

  
ALTER TABLE TB_LIS_GEOGR_GRANULARITIES ADD CONSTRAINT FK_TB_LIS_GEOGR_GRANULARITIES_TITLE_FK
	FOREIGN KEY (TITLE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;

  
ALTER TABLE TB_LIS_GEOGR_VALUES ADD CONSTRAINT FK_TB_LIS_GEOGR_VALUES_TITLE_FK
	FOREIGN KEY (TITLE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_LIS_GEOGR_VALUES ADD CONSTRAINT FK_TB_LIS_GEOGR_VALUES_GRANULARITY_FK
	FOREIGN KEY (GRANULARITY_FK) REFERENCES TB_LIS_GEOGR_GRANULARITIES (ID)
;

  
ALTER TABLE TB_QUANTITIES ADD CONSTRAINT FK_TB_QUANTITIES_UNIT_MULTIPLIER_FK
	FOREIGN KEY (UNIT_MULTIPLIER_FK) REFERENCES TB_LIS_UNITS_MULTIPLIERS (ID)
;
ALTER TABLE TB_QUANTITIES ADD CONSTRAINT FK_TB_QUANTITIES_PERCENTAGE_OF_FK
	FOREIGN KEY (PERCENTAGE_OF_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_QUANTITIES ADD CONSTRAINT FK_TB_QUANTITIES_UNIT_FK
	FOREIGN KEY (UNIT_FK) REFERENCES TB_LIS_QUANTITIES_UNITS (ID)
;
ALTER TABLE TB_QUANTITIES ADD CONSTRAINT FK_TB_QUANTITIES_NUMERATOR_FK
	FOREIGN KEY (NUMERATOR_FK) REFERENCES TB_INDICATORS (ID)
;
ALTER TABLE TB_QUANTITIES ADD CONSTRAINT FK_TB_QUANTITIES_DENOMINATOR_FK
	FOREIGN KEY (DENOMINATOR_FK) REFERENCES TB_INDICATORS (ID)
;
ALTER TABLE TB_QUANTITIES ADD CONSTRAINT FK_TB_QUANTITIES_BASE_QUANTITY_FK
	FOREIGN KEY (BASE_QUANTITY_FK) REFERENCES TB_INDICATORS (ID)
;
ALTER TABLE TB_QUANTITIES ADD CONSTRAINT FK_TB_QUANTITIES_BASE_LOCATION_FK
	FOREIGN KEY (BASE_LOCATION_FK) REFERENCES TB_LIS_GEOGR_VALUES (ID)
;

  
ALTER TABLE TB_INDICATORS_VERSIONS ADD CONSTRAINT FK_TB_INDICATORS_VERSIONS_TITLE_FK
	FOREIGN KEY (TITLE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_INDICATORS_VERSIONS ADD CONSTRAINT FK_TB_INDICATORS_VERSIONS_ACRONYM_FK
	FOREIGN KEY (ACRONYM_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_INDICATORS_VERSIONS ADD CONSTRAINT FK_TB_INDICATORS_VERSIONS_SUBJECT_TITLE_FK
	FOREIGN KEY (SUBJECT_TITLE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_INDICATORS_VERSIONS ADD CONSTRAINT FK_TB_INDICATORS_VERSIONS_CONCEPT_DESCRIPTION_FK
	FOREIGN KEY (CONCEPT_DESCRIPTION_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_INDICATORS_VERSIONS ADD CONSTRAINT FK_TB_INDICATORS_VERSIONS_COMMENTS_FK
	FOREIGN KEY (COMMENTS_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_INDICATORS_VERSIONS ADD CONSTRAINT FK_TB_INDICATORS_VERSIONS_NOTES_FK
	FOREIGN KEY (NOTES_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_INDICATORS_VERSIONS ADD CONSTRAINT FK_TB_INDICATORS_VERSIONS_INDICATOR_FK
	FOREIGN KEY (INDICATOR_FK) REFERENCES TB_INDICATORS (ID)
;
ALTER TABLE TB_INDICATORS_VERSIONS ADD CONSTRAINT FK_TB_INDICATORS_VERSIONS_QUANTITY_FK
	FOREIGN KEY (QUANTITY_FK) REFERENCES TB_QUANTITIES (ID)
;

  
ALTER TABLE TB_RATES_DERIVATIONS ADD CONSTRAINT FK_TB_RATES_DERIVATIONS_QUANTITY_FK
	FOREIGN KEY (QUANTITY_FK) REFERENCES TB_QUANTITIES (ID)
;

  
ALTER TABLE TB_DATA_SOURCES ADD CONSTRAINT FK_TB_DATA_SOURCES_SOURCE_SURVEY_TITLE_FK
	FOREIGN KEY (SOURCE_SURVEY_TITLE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_DATA_SOURCES ADD CONSTRAINT FK_TB_DATA_SOURCES_SOURCE_SURVEY_ACRONYM_FK
	FOREIGN KEY (SOURCE_SURVEY_ACRONYM_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_DATA_SOURCES ADD CONSTRAINT FK_TB_DATA_SOURCES_STAT_RESOURCE_FK
	FOREIGN KEY (STAT_RESOURCE_FK) REFERENCES TB_EXTERNAL_ITEMS (ID)
;
ALTER TABLE TB_DATA_SOURCES ADD CONSTRAINT FK_TB_DATA_SOURCES_INDICATOR_VERSION_FK
	FOREIGN KEY (INDICATOR_VERSION_FK) REFERENCES TB_INDICATORS_VERSIONS (ID)
;
ALTER TABLE TB_DATA_SOURCES ADD CONSTRAINT FK_TB_DATA_SOURCES_ANNUAL_PUNTUAL_RATE_FK
	FOREIGN KEY (ANNUAL_PUNTUAL_RATE_FK) REFERENCES TB_RATES_DERIVATIONS (ID)
;
ALTER TABLE TB_DATA_SOURCES ADD CONSTRAINT FK_TB_DATA_SOURCES_ANNUAL_PERCENTAGE_RATE_FK
	FOREIGN KEY (ANNUAL_PERCENTAGE_RATE_FK) REFERENCES TB_RATES_DERIVATIONS (ID)
;
ALTER TABLE TB_DATA_SOURCES ADD CONSTRAINT FK_TB_DATA_SOURCES_INTERPERIOD_PUNTUAL_RATE_FK
	FOREIGN KEY (INTERPERIOD_PUNTUAL_RATE_FK) REFERENCES TB_RATES_DERIVATIONS (ID)
;
ALTER TABLE TB_DATA_SOURCES ADD CONSTRAINT FK_TB_DATA_SOURCES_INTERPERIOD_PERCENTAGE_RATE_FK
	FOREIGN KEY (INTERPERIOD_PERCENTAGE_RATE_FK) REFERENCES TB_RATES_DERIVATIONS (ID)
;
ALTER TABLE TB_DATA_SOURCES ADD CONSTRAINT FK_TB_DATA_SOURCES_GEOGRAPHICAL_VALUE_FK
	FOREIGN KEY (GEOGRAPHICAL_VALUE_FK) REFERENCES TB_LIS_GEOGR_VALUES (ID)
;

ALTER TABLE TB_DATA_SOURCES_VARIABLES ADD CONSTRAINT FK_TB_DATA_SOURCES_VARIABLES_TB_DATA_SOURCES
	FOREIGN KEY (DATA_SOURCE_FK) REFERENCES TB_DATA_SOURCES (ID)
;

  
  
ALTER TABLE TB_INDICATORS_INSTANCES ADD CONSTRAINT FK_TB_INDICATORS_INSTANCES_TITLE_FK
	FOREIGN KEY (TITLE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_INDICATORS_INSTANCES ADD CONSTRAINT FK_TB_INDICATORS_INSTANCES_INDICATOR_FK
	FOREIGN KEY (INDICATOR_FK) REFERENCES TB_INDICATORS (ID)
;
ALTER TABLE TB_INDICATORS_INSTANCES ADD CONSTRAINT FK_TB_INDICATORS_INSTANCES_GEOGRAPHICAL_GRANULARITY_FK
	FOREIGN KEY (GEOGRAPHICAL_GRANULARITY_FK) REFERENCES TB_LIS_GEOGR_GRANULARITIES (ID)
;

  
  
  
ALTER TABLE TB_INDIC_SYSTEMS_VERSIONS ADD CONSTRAINT FK_TB_INDIC_SYSTEMS_VERSIONS_INDICATORS_SYSTEM_FK
	FOREIGN KEY (INDICATORS_SYSTEM_FK) REFERENCES TB_INDICATORS_SYSTEMS (ID)
;

  
ALTER TABLE TB_ELEMENTS_LEVELS ADD CONSTRAINT FK_TB_ELEMENTS_LEVELS_PARENT_FK
	FOREIGN KEY (PARENT_FK) REFERENCES TB_ELEMENTS_LEVELS (ID)
;
ALTER TABLE TB_ELEMENTS_LEVELS ADD CONSTRAINT FK_TB_ELEMENTS_LEVELS_DIMENSION_FK
	FOREIGN KEY (DIMENSION_FK) REFERENCES TB_DIMENSIONS (ID)
;
ALTER TABLE TB_ELEMENTS_LEVELS ADD CONSTRAINT FK_TB_ELEMENTS_LEVELS_INDICATOR_INSTANCE_FK
	FOREIGN KEY (INDICATOR_INSTANCE_FK) REFERENCES TB_INDICATORS_INSTANCES (ID)
;
ALTER TABLE TB_ELEMENTS_LEVELS ADD CONSTRAINT FK_TB_ELEMENTS_LEVELS_IND_SYSTEM_VERSION_ALL_FK
	FOREIGN KEY (IND_SYSTEM_VERSION_ALL_FK) REFERENCES TB_INDIC_SYSTEMS_VERSIONS (ID)
;
ALTER TABLE TB_ELEMENTS_LEVELS ADD CONSTRAINT FK_TB_ELEMENTS_LEVELS_IND_SYSTEM_VERSION_FIRST_FK
	FOREIGN KEY (IND_SYSTEM_VERSION_FIRST_FK) REFERENCES TB_INDIC_SYSTEMS_VERSIONS (ID)
;

  
ALTER TABLE TB_DIMENSIONS ADD CONSTRAINT FK_TB_DIMENSIONS_TITLE_FK
	FOREIGN KEY (TITLE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;

  
ALTER TABLE TB_INDIC_INST_LAST_VALUE ADD CONSTRAINT FK_TB_INDIC_INST_LAST_VALUE_INDICATOR_INSTANCE_FK
	FOREIGN KEY (INDICATOR_INSTANCE_FK) REFERENCES TB_INDICATORS_INSTANCES (ID)
;

  
ALTER TABLE TB_IND_VERSION_GEO_COV ADD CONSTRAINT FK_TB_IND_VERSION_GEO_COV_GEOGRAPHICAL_VALUE_FK
	FOREIGN KEY (GEOGRAPHICAL_VALUE_FK) REFERENCES TB_LIS_GEOGR_VALUES (ID)
;
ALTER TABLE TB_IND_VERSION_GEO_COV ADD CONSTRAINT FK_TB_IND_VERSION_GEO_COV_INDICATOR_VERSION_FK
	FOREIGN KEY (INDICATOR_VERSION_FK) REFERENCES TB_INDICATORS_VERSIONS (ID)
;

  
ALTER TABLE TB_INDIC_VERSION_LAST_VALUE ADD CONSTRAINT FK_TB_INDIC_VERSION_LAST_VALUE_INDICATOR_VERSION_FK
	FOREIGN KEY (INDICATOR_VERSION_FK) REFERENCES TB_INDICATORS_VERSIONS (ID)
;

  
ALTER TABLE TB_TRANSLATIONS ADD CONSTRAINT FK_TB_TRANSLATIONS_TITLE_FK
	FOREIGN KEY (TITLE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_TRANSLATIONS ADD CONSTRAINT FK_TB_TRANSLATIONS_TITLE_SUMMARY_FK
	FOREIGN KEY (TITLE_SUMMARY_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;

  
ALTER TABLE TB_IND_VERSION_MEAS_COV ADD CONSTRAINT FK_TB_IND_VERSION_MEAS_COV_INDICATOR_VERSION_FK
	FOREIGN KEY (INDICATOR_VERSION_FK) REFERENCES TB_INDICATORS_VERSIONS (ID)
;
ALTER TABLE TB_IND_VERSION_MEAS_COV ADD CONSTRAINT FK_TB_IND_VERSION_MEAS_COV_TRANSLATION
	FOREIGN KEY (TRANSLATION) REFERENCES TB_TRANSLATIONS (ID)
;

  
ALTER TABLE TB_IND_VERSION_TIME_COV ADD CONSTRAINT FK_TB_IND_VERSION_TIME_COV_INDICATOR_VERSION_FK
	FOREIGN KEY (INDICATOR_VERSION_FK) REFERENCES TB_INDICATORS_VERSIONS (ID)
;
ALTER TABLE TB_IND_VERSION_TIME_COV ADD CONSTRAINT FK_TB_IND_VERSION_TIME_COV_TRANSLATION
	FOREIGN KEY (TRANSLATION) REFERENCES TB_TRANSLATIONS (ID)
;
ALTER TABLE TB_IND_VERSION_TIME_COV ADD CONSTRAINT FK_TB_IND_VERSION_TIME_COV_GRANULARITY_TRANSLATION
	FOREIGN KEY (GRANULARITY_TRANSLATION) REFERENCES TB_TRANSLATIONS (ID)
;

  
ALTER TABLE TB_INDICATORS_SYSTEMS_HIST ADD CONSTRAINT FK_TB_INDICATORS_SYSTEMS_HIST_INDICATORS_SYSTEM_FK
	FOREIGN KEY (INDICATORS_SYSTEM_FK) REFERENCES TB_INDICATORS_SYSTEMS (ID)
;

  
ALTER TABLE TB_LOCALISED_STRINGS ADD CONSTRAINT FK_TB_LOCALISED_STRINGS_INTERNATIONAL_STRING_FK
	FOREIGN KEY (INTERNATIONAL_STRING_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;

  
  
  

ALTER TABLE TB_INDIC_INST_GEO_VALUES ADD CONSTRAINT FK_TB_INDIC_INST_GEO_VALUES_GEOGRAPHICAL_VALUE_FK
	FOREIGN KEY (GEOGRAPHICAL_VALUE_FK) REFERENCES TB_LIS_GEOGR_VALUES (ID)
;
ALTER TABLE TB_INDIC_INST_GEO_VALUES ADD CONSTRAINT FK_TB_INDIC_INST_GEO_VALUES_INDICATOR_INSTANCE_FK
	FOREIGN KEY (INDICATOR_INSTANCE_FK) REFERENCES TB_INDICATORS_INSTANCES (ID)
;

  


-- Index
  
  


