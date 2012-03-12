-- Create normal entities
    
CREATE TABLE TBL_INTERNATIONAL_STRINGS (
  ID NUMBER(19) NOT NULL,
  UUID VARCHAR2(36) NOT NULL,
  VERSION NUMBER(19) NOT NULL
);


CREATE TABLE TBL_INDICATORS (
  ID NUMBER(19) NOT NULL,
  CODE VARCHAR2(100) NOT NULL,
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


CREATE TABLE LIS_QUANTITIES_UNITS (
  ID NUMBER(19) NOT NULL,
  SYMBOL VARCHAR2(100),
  UUID VARCHAR2(36) NOT NULL,
  VERSION NUMBER(19) NOT NULL,
  TITLE_FK NUMBER(19),
  SYMBOL_POSITION VARCHAR2(40) NOT NULL
);


CREATE TABLE LIS_GEOGR_GRANULARITIES (
  ID NUMBER(19) NOT NULL,
  CODE VARCHAR2(100) NOT NULL,
  UUID VARCHAR2(36) NOT NULL,
  VERSION NUMBER(19) NOT NULL,
  TITLE_FK NUMBER(19)
);


CREATE TABLE LIS_GEOGR_VALUES (
  ID NUMBER(19) NOT NULL,
  CODE VARCHAR2(100) NOT NULL,
  LATITUDE VARCHAR2(100),
  LONGITUDE VARCHAR2(100),
  UUID VARCHAR2(36) NOT NULL,
  VERSION NUMBER(19) NOT NULL,
  TITLE_FK NUMBER(19),
  GRANULARITY_FK NUMBER(19) NOT NULL
);


CREATE TABLE TBL_QUANTITIES (
  ID NUMBER(19) NOT NULL,
  UNIT_MULTIPLIER NUMBER(10) NOT NULL,
  SIGNIFICANT_DIGITS NUMBER(10),
  DECIMAL_PLACES NUMBER(10),
  MINIMUM NUMBER(10),
  MAXIMUM NUMBER(10),
  IS_PERCENTAGE NUMBER(1,0),
  BASE_VALUE NUMBER(10),
  BASE_TIME VARCHAR2(100),
  UUID VARCHAR2(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR2(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR2(50),
  LAST_UPDATED_TZ VARCHAR2(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR2(50),
  VERSION NUMBER(19) NOT NULL,
  PERCENTAGE_OF_FK NUMBER(19),
  UNIT_FK NUMBER(19) NOT NULL,
  NUMERATOR_FK NUMBER(19),
  DENOMINATOR_FK NUMBER(19),
  BASE_QUANTITY_FK NUMBER(19),
  BASE_LOCATION_FK NUMBER(19),
  QUANTITY_TYPE VARCHAR2(40) NOT NULL
);


CREATE TABLE TBL_INDICATORS_VERSIONS (
  ID NUMBER(19) NOT NULL,
  VERSION_NUMBER VARCHAR2(10) NOT NULL,
  SUBJECT_CODE VARCHAR2(100) NOT NULL,
  COMMENTS_URL VARCHAR2(100),
  NOTES_URL VARCHAR2(100),
  PRODUCTION_VALIDATION_DATE_TZ VARCHAR2(50),
  PRODUCTION_VALIDATION_DATE TIMESTAMP,
  PRODUCTION_VALIDATION_USER VARCHAR2(100),
  DIFFUSION_VALIDATION_DATE_TZ VARCHAR2(50),
  DIFFUSION_VALIDATION_DATE TIMESTAMP,
  DIFFUSION_VALIDATION_USER VARCHAR2(100),
  PUBLICATION_DATE_TZ VARCHAR2(50),
  PUBLICATION_DATE TIMESTAMP,
  PUBLICATION_USER VARCHAR2(100),
  ARCHIVE_DATE_TZ VARCHAR2(50),
  ARCHIVE_DATE TIMESTAMP,
  ARCHIVE_USER VARCHAR2(100),
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
  PROC_STATUS VARCHAR2(40) NOT NULL
);


CREATE TABLE TBL_RATES_DERIVATIONS (
  ID NUMBER(19) NOT NULL,
  METHOD VARCHAR2(100) NOT NULL,
  UUID VARCHAR2(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR2(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR2(50),
  LAST_UPDATED_TZ VARCHAR2(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR2(50),
  VERSION NUMBER(19) NOT NULL,
  QUANTITY_FK NUMBER(19) NOT NULL,
  METHOD_TYPE VARCHAR2(40) NOT NULL,
  ROUNDING VARCHAR2(40)
);


CREATE TABLE TBL_DATA_SOURCES (
  ID NUMBER(19) NOT NULL,
  QUERY_GPE VARCHAR2(100) NOT NULL,
  PX VARCHAR2(100) NOT NULL,
  TIME_VARIABLE VARCHAR2(100) NOT NULL,
  GEOGRAPHICAL_VARIABLE VARCHAR2(100) NOT NULL,
  UUID VARCHAR2(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR2(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR2(50),
  LAST_UPDATED_TZ VARCHAR2(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR2(50),
  VERSION NUMBER(19) NOT NULL,
  INDICATOR_VERSION_FK NUMBER(19),
  INTERPERIOD_RATE_FK NUMBER(19) NOT NULL,
  ANNUAL_RATE_FK NUMBER(19) NOT NULL
);


CREATE TABLE TBL_DATA_SOURCES_VARIABLES (
  ID NUMBER(19) NOT NULL,
  VARIABLE VARCHAR2(100) NOT NULL,
  CATEGORY VARCHAR2(100) NOT NULL,
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


CREATE TABLE TBL_INDICATORS_INSTANCES (
  ID NUMBER(19) NOT NULL,
  TIME_VALUE VARCHAR2(100),
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
  GEOGRAPHICAL_VALUE_FK NUMBER(19),
  TIME_GRANULARITY VARCHAR2(40)
);


CREATE TABLE TBL_INDICATORS_SYSTEMS (
  ID NUMBER(19) NOT NULL,
  CODE VARCHAR2(100) NOT NULL,
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


CREATE TABLE TBL_INDIC_SYSTEMS_VERSIONS (
  ID NUMBER(19) NOT NULL,
  VERSION_NUMBER VARCHAR2(10) NOT NULL,
  URI_GOPESTAT VARCHAR2(100),
  PRODUCTION_VALIDATION_DATE_TZ VARCHAR2(50),
  PRODUCTION_VALIDATION_DATE TIMESTAMP,
  PRODUCTION_VALIDATION_USER VARCHAR2(100),
  DIFFUSION_VALIDATION_DATE_TZ VARCHAR2(50),
  DIFFUSION_VALIDATION_DATE TIMESTAMP,
  DIFFUSION_VALIDATION_USER VARCHAR2(100),
  PUBLICATION_DATE_TZ VARCHAR2(50),
  PUBLICATION_DATE TIMESTAMP,
  PUBLICATION_USER VARCHAR2(100),
  ARCHIVE_DATE_TZ VARCHAR2(50),
  ARCHIVE_DATE TIMESTAMP,
  ARCHIVE_USER VARCHAR2(100),
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
  OBJETIVE_FK NUMBER(19),
  DESCRIPTION_FK NUMBER(19),
  INDICATORS_SYSTEM_FK NUMBER(19) NOT NULL,
  PROC_STATUS VARCHAR2(40) NOT NULL
);


CREATE TABLE TBL_ELEMENTS_LEVELS (
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


CREATE TABLE TBL_DIMENSIONS (
  ID NUMBER(19) NOT NULL,
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


CREATE TABLE TBL_LOCALISED_STRINGS (
  ID NUMBER(19) NOT NULL,
  LABEL VARCHAR2(4000) NOT NULL,
  LOCALE VARCHAR2(100) NOT NULL,
  UUID VARCHAR2(36) NOT NULL,
  VERSION NUMBER(19) NOT NULL,
  INTERNATIONAL_STRING_FK NUMBER(19)
);



-- Create many to many relations
    

-- Primary keys
    
ALTER TABLE TBL_INTERNATIONAL_STRINGS ADD CONSTRAINT PK_TBL_INTERNATIONAL_STRINGS
	PRIMARY KEY (ID)
;

ALTER TABLE TBL_INDICATORS ADD CONSTRAINT PK_TBL_INDICATORS
	PRIMARY KEY (ID)
;

ALTER TABLE LIS_QUANTITIES_UNITS ADD CONSTRAINT PK_LIS_QUANTITIES_UNITS
	PRIMARY KEY (ID)
;

ALTER TABLE LIS_GEOGR_GRANULARITIES ADD CONSTRAINT PK_LIS_GEOGR_GRANULARITIES
	PRIMARY KEY (ID)
;

ALTER TABLE LIS_GEOGR_VALUES ADD CONSTRAINT PK_LIS_GEOGR_VALUES
	PRIMARY KEY (ID)
;

ALTER TABLE TBL_QUANTITIES ADD CONSTRAINT PK_TBL_QUANTITIES
	PRIMARY KEY (ID)
;

ALTER TABLE TBL_INDICATORS_VERSIONS ADD CONSTRAINT PK_TBL_INDICATORS_VERSIONS
	PRIMARY KEY (ID)
;

ALTER TABLE TBL_RATES_DERIVATIONS ADD CONSTRAINT PK_TBL_RATES_DERIVATIONS
	PRIMARY KEY (ID)
;

ALTER TABLE TBL_DATA_SOURCES ADD CONSTRAINT PK_TBL_DATA_SOURCES
	PRIMARY KEY (ID)
;

ALTER TABLE TBL_DATA_SOURCES_VARIABLES ADD CONSTRAINT PK_TBL_DATA_SOURCES_VARIABLES
	PRIMARY KEY (ID)
;

ALTER TABLE TBL_INDICATORS_INSTANCES ADD CONSTRAINT PK_TBL_INDICATORS_INSTANCES
	PRIMARY KEY (ID)
;

ALTER TABLE TBL_INDICATORS_SYSTEMS ADD CONSTRAINT PK_TBL_INDICATORS_SYSTEMS
	PRIMARY KEY (ID)
;

ALTER TABLE TBL_INDIC_SYSTEMS_VERSIONS ADD CONSTRAINT PK_TBL_INDIC_SYSTEMS_VERSIONS
	PRIMARY KEY (ID)
;

ALTER TABLE TBL_ELEMENTS_LEVELS ADD CONSTRAINT PK_TBL_ELEMENTS_LEVELS
	PRIMARY KEY (ID)
;

ALTER TABLE TBL_DIMENSIONS ADD CONSTRAINT PK_TBL_DIMENSIONS
	PRIMARY KEY (ID)
;

ALTER TABLE TBL_LOCALISED_STRINGS ADD CONSTRAINT PK_TBL_LOCALISED_STRINGS
	PRIMARY KEY (ID)
;

    

-- Unique constraints

ALTER TABLE TBL_INTERNATIONAL_STRINGS
    ADD CONSTRAINT UQ_TBL_INTERNATIONAL_STRINGS UNIQUE (UUID)
;


ALTER TABLE TBL_INDICATORS
    ADD CONSTRAINT UQ_TBL_INDICATORS UNIQUE (UUID)
;


ALTER TABLE LIS_QUANTITIES_UNITS
    ADD CONSTRAINT UQ_LIS_QUANTITIES_UNITS UNIQUE (UUID)
;


ALTER TABLE LIS_GEOGR_GRANULARITIES
    ADD CONSTRAINT UQ_LIS_GEOGR_GRANULARITIES UNIQUE (UUID)
;


ALTER TABLE LIS_GEOGR_VALUES
    ADD CONSTRAINT UQ_LIS_GEOGR_VALUES UNIQUE (UUID)
;


ALTER TABLE TBL_QUANTITIES
    ADD CONSTRAINT UQ_TBL_QUANTITIES UNIQUE (UUID)
;


ALTER TABLE TBL_INDICATORS_VERSIONS
    ADD CONSTRAINT UQ_TBL_INDICATORS_VERSIONS UNIQUE (UUID)
;


ALTER TABLE TBL_RATES_DERIVATIONS
    ADD CONSTRAINT UQ_TBL_RATES_DERIVATIONS UNIQUE (UUID)
;


ALTER TABLE TBL_DATA_SOURCES
    ADD CONSTRAINT UQ_TBL_DATA_SOURCES UNIQUE (UUID)
;


ALTER TABLE TBL_DATA_SOURCES_VARIABLES
    ADD CONSTRAINT UQ_TBL_DATA_SOURCES_VARIABLES UNIQUE (UUID)
;


ALTER TABLE TBL_INDICATORS_INSTANCES
    ADD CONSTRAINT UQ_TBL_INDICATORS_INSTANCES UNIQUE (UUID)
;


ALTER TABLE TBL_INDICATORS_SYSTEMS
    ADD CONSTRAINT UQ_TBL_INDICATORS_SYSTEMS UNIQUE (UUID)
;


ALTER TABLE TBL_INDIC_SYSTEMS_VERSIONS
    ADD CONSTRAINT UQ_TBL_INDIC_SYSTEMS_VERSIONS UNIQUE (UUID)
;


ALTER TABLE TBL_ELEMENTS_LEVELS
    ADD CONSTRAINT UQ_TBL_ELEMENTS_LEVELS UNIQUE (UUID)
;


ALTER TABLE TBL_DIMENSIONS
    ADD CONSTRAINT UQ_TBL_DIMENSIONS UNIQUE (UUID)
;


ALTER TABLE TBL_LOCALISED_STRINGS
    ADD CONSTRAINT UQ_TBL_LOCALISED_STRINGS UNIQUE (UUID)
;



-- Foreign key constraints
    

  
ALTER TABLE TBL_LOCALISED_STRINGS ADD CONSTRAINT FK_TBL_LOCALISED_STRINGS_TBL13
	FOREIGN KEY (INTERNATIONAL_STRING_FK) REFERENCES TBL_INTERNATIONAL_STRINGS (ID)
;

  
  
ALTER TABLE LIS_QUANTITIES_UNITS ADD CONSTRAINT FK_LIS_QUANTITIES_UNITS_TITL41
	FOREIGN KEY (TITLE_FK) REFERENCES TBL_INTERNATIONAL_STRINGS (ID)
;

  
ALTER TABLE LIS_GEOGR_GRANULARITIES ADD CONSTRAINT FK_LIS_GEOGR_GRANULARITIES_T77
	FOREIGN KEY (TITLE_FK) REFERENCES TBL_INTERNATIONAL_STRINGS (ID)
;

  
ALTER TABLE LIS_GEOGR_VALUES ADD CONSTRAINT FK_LIS_GEOGR_VALUES_TITLE_FK
	FOREIGN KEY (TITLE_FK) REFERENCES TBL_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE LIS_GEOGR_VALUES ADD CONSTRAINT FK_LIS_GEOGR_VALUES_GRANULAR93
	FOREIGN KEY (GRANULARITY_FK) REFERENCES LIS_GEOGR_GRANULARITIES (ID)
;

  
ALTER TABLE TBL_QUANTITIES ADD CONSTRAINT FK_TBL_QUANTITIES_PERCENTAGE09
	FOREIGN KEY (PERCENTAGE_OF_FK) REFERENCES TBL_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TBL_QUANTITIES ADD CONSTRAINT FK_TBL_QUANTITIES_UNIT_FK
	FOREIGN KEY (UNIT_FK) REFERENCES LIS_QUANTITIES_UNITS (ID)
;
ALTER TABLE TBL_QUANTITIES ADD CONSTRAINT FK_TBL_QUANTITIES_NUMERATOR_FK
	FOREIGN KEY (NUMERATOR_FK) REFERENCES TBL_INDICATORS (ID)
;
ALTER TABLE TBL_QUANTITIES ADD CONSTRAINT FK_TBL_QUANTITIES_DENOMINATO61
	FOREIGN KEY (DENOMINATOR_FK) REFERENCES TBL_INDICATORS (ID)
;
ALTER TABLE TBL_QUANTITIES ADD CONSTRAINT FK_TBL_QUANTITIES_BASE_QUANT16
	FOREIGN KEY (BASE_QUANTITY_FK) REFERENCES TBL_INDICATORS (ID)
;
ALTER TABLE TBL_QUANTITIES ADD CONSTRAINT FK_TBL_QUANTITIES_BASE_LOCAT58
	FOREIGN KEY (BASE_LOCATION_FK) REFERENCES LIS_GEOGR_VALUES (ID)
;

  
ALTER TABLE TBL_INDICATORS_VERSIONS ADD CONSTRAINT FK_TBL_INDICATORS_VERSIONS_T78
	FOREIGN KEY (TITLE_FK) REFERENCES TBL_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TBL_INDICATORS_VERSIONS ADD CONSTRAINT FK_TBL_INDICATORS_VERSIONS_A27
	FOREIGN KEY (ACRONYM_FK) REFERENCES TBL_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TBL_INDICATORS_VERSIONS ADD CONSTRAINT FK_TBL_INDICATORS_VERSIONS_S99
	FOREIGN KEY (SUBJECT_TITLE_FK) REFERENCES TBL_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TBL_INDICATORS_VERSIONS ADD CONSTRAINT FK_TBL_INDICATORS_VERSIONS_C27
	FOREIGN KEY (CONCEPT_DESCRIPTION_FK) REFERENCES TBL_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TBL_INDICATORS_VERSIONS ADD CONSTRAINT FK_TBL_INDICATORS_VERSIONS_C42
	FOREIGN KEY (COMMENTS_FK) REFERENCES TBL_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TBL_INDICATORS_VERSIONS ADD CONSTRAINT FK_TBL_INDICATORS_VERSIONS_N77
	FOREIGN KEY (NOTES_FK) REFERENCES TBL_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TBL_INDICATORS_VERSIONS ADD CONSTRAINT FK_TBL_INDICATORS_VERSIONS_I59
	FOREIGN KEY (INDICATOR_FK) REFERENCES TBL_INDICATORS (ID)
;
ALTER TABLE TBL_INDICATORS_VERSIONS ADD CONSTRAINT FK_TBL_INDICATORS_VERSIONS_Q05
	FOREIGN KEY (QUANTITY_FK) REFERENCES TBL_QUANTITIES (ID)
;

  
ALTER TABLE TBL_RATES_DERIVATIONS ADD CONSTRAINT FK_TBL_RATES_DERIVATIONS_QUA71
	FOREIGN KEY (QUANTITY_FK) REFERENCES TBL_QUANTITIES (ID)
;

  
ALTER TABLE TBL_DATA_SOURCES ADD CONSTRAINT FK_TBL_DATA_SOURCES_INDICATO27
	FOREIGN KEY (INDICATOR_VERSION_FK) REFERENCES TBL_INDICATORS_VERSIONS (ID)
;
ALTER TABLE TBL_DATA_SOURCES ADD CONSTRAINT FK_TBL_DATA_SOURCES_INTERPER69
	FOREIGN KEY (INTERPERIOD_RATE_FK) REFERENCES TBL_RATES_DERIVATIONS (ID)
;
ALTER TABLE TBL_DATA_SOURCES ADD CONSTRAINT FK_TBL_DATA_SOURCES_ANNUAL_R77
	FOREIGN KEY (ANNUAL_RATE_FK) REFERENCES TBL_RATES_DERIVATIONS (ID)
;

ALTER TABLE TBL_DATA_SOURCES_VARIABLES ADD CONSTRAINT FK_TBL_DATA_SOURCES_VARIABLE65
	FOREIGN KEY (DATA_SOURCE_FK) REFERENCES TBL_DATA_SOURCES (ID)
;

  
  
ALTER TABLE TBL_INDICATORS_INSTANCES ADD CONSTRAINT FK_TBL_INDICATORS_INSTANCES_99
	FOREIGN KEY (TITLE_FK) REFERENCES TBL_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TBL_INDICATORS_INSTANCES ADD CONSTRAINT FK_TBL_INDICATORS_INSTANCES_40
	FOREIGN KEY (INDICATOR_FK) REFERENCES TBL_INDICATORS (ID)
;
ALTER TABLE TBL_INDICATORS_INSTANCES ADD CONSTRAINT FK_TBL_INDICATORS_INSTANCES_32
	FOREIGN KEY (GEOGRAPHICAL_GRANULARITY_FK) REFERENCES LIS_GEOGR_GRANULARITIES (ID)
;
ALTER TABLE TBL_INDICATORS_INSTANCES ADD CONSTRAINT FK_TBL_INDICATORS_INSTANCES_21
	FOREIGN KEY (GEOGRAPHICAL_VALUE_FK) REFERENCES LIS_GEOGR_VALUES (ID)
;

  
  
  
ALTER TABLE TBL_INDIC_SYSTEMS_VERSIONS ADD CONSTRAINT FK_TBL_INDIC_SYSTEMS_VERSION90
	FOREIGN KEY (TITLE_FK) REFERENCES TBL_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TBL_INDIC_SYSTEMS_VERSIONS ADD CONSTRAINT FK_TBL_INDIC_SYSTEMS_VERSION13
	FOREIGN KEY (ACRONYM_FK) REFERENCES TBL_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TBL_INDIC_SYSTEMS_VERSIONS ADD CONSTRAINT FK_TBL_INDIC_SYSTEMS_VERSION56
	FOREIGN KEY (OBJETIVE_FK) REFERENCES TBL_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TBL_INDIC_SYSTEMS_VERSIONS ADD CONSTRAINT FK_TBL_INDIC_SYSTEMS_VERSION98
	FOREIGN KEY (DESCRIPTION_FK) REFERENCES TBL_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TBL_INDIC_SYSTEMS_VERSIONS ADD CONSTRAINT FK_TBL_INDIC_SYSTEMS_VERSION60
	FOREIGN KEY (INDICATORS_SYSTEM_FK) REFERENCES TBL_INDICATORS_SYSTEMS (ID)
;

  
ALTER TABLE TBL_ELEMENTS_LEVELS ADD CONSTRAINT FK_TBL_ELEMENTS_LEVELS_PAREN61
	FOREIGN KEY (PARENT_FK) REFERENCES TBL_ELEMENTS_LEVELS (ID)
;
ALTER TABLE TBL_ELEMENTS_LEVELS ADD CONSTRAINT FK_TBL_ELEMENTS_LEVELS_DIMEN43
	FOREIGN KEY (DIMENSION_FK) REFERENCES TBL_DIMENSIONS (ID)
;
ALTER TABLE TBL_ELEMENTS_LEVELS ADD CONSTRAINT FK_TBL_ELEMENTS_LEVELS_INDIC58
	FOREIGN KEY (INDICATOR_INSTANCE_FK) REFERENCES TBL_INDICATORS_INSTANCES (ID)
;
ALTER TABLE TBL_ELEMENTS_LEVELS ADD CONSTRAINT FK_TBL_ELEMENTS_LEVELS_IND_S25
	FOREIGN KEY (IND_SYSTEM_VERSION_ALL_FK) REFERENCES TBL_INDIC_SYSTEMS_VERSIONS (ID)
;
ALTER TABLE TBL_ELEMENTS_LEVELS ADD CONSTRAINT FK_TBL_ELEMENTS_LEVELS_IND_S06
	FOREIGN KEY (IND_SYSTEM_VERSION_FIRST_FK) REFERENCES TBL_INDIC_SYSTEMS_VERSIONS (ID)
;

  
ALTER TABLE TBL_DIMENSIONS ADD CONSTRAINT FK_TBL_DIMENSIONS_TITLE_FK
	FOREIGN KEY (TITLE_FK) REFERENCES TBL_INTERNATIONAL_STRINGS (ID)
;

  
  
  

    

-- Index
  
  
  
  


