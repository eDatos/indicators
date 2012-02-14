-- Create normal entities
    
CREATE TABLE TBL_INTERNATIONAL_STRINGS (
  ID NUMBER(19) NOT NULL,
  UUID VARCHAR2(36) NOT NULL,
  VERSION NUMBER(19) NOT NULL
);


CREATE TABLE TBL_INDICATORS_SYSTEMS (
  ID NUMBER(19) NOT NULL,
  CODE VARCHAR2(100) NOT NULL,
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
  INDICATORS_SYSTEMS_FK NUMBER(19) NOT NULL,
  STATE VARCHAR2(40) NOT NULL
);


CREATE TABLE TBL_DIMENSIONS (
  ID NUMBER(19) NOT NULL,
  ORDER_IN_LEVEL NUMBER(19) NOT NULL,
  UUID VARCHAR2(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR2(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR2(50),
  LAST_UPDATED_TZ VARCHAR2(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR2(50),
  VERSION NUMBER(19) NOT NULL,
  TITLE_FK NUMBER(19) NOT NULL,
  INDICATORS_SYSTEM_VERSION_FK NUMBER(19),
  PARENT_FK NUMBER(19)
);


CREATE TABLE TBL_INDICATORS (
  ID NUMBER(19) NOT NULL,
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


CREATE TABLE TBL_INDICATOR_VERSIONS (
  ID NUMBER(19) NOT NULL,
  VERSION_NUMBER VARCHAR2(10) NOT NULL,
  CODE VARCHAR2(100) NOT NULL,
  SUBJECT_CODE VARCHAR2(100),
  NOTE_URL VARCHAR2(100),
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
  NAME_FK NUMBER(19) NOT NULL,
  ACRONYM_FK NUMBER(19),
  COMMENTARY_FK NUMBER(19),
  NOTES_FK NUMBER(19),
  INDICATOR_FK NUMBER(19) NOT NULL,
  STATE VARCHAR2(40) NOT NULL
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

ALTER TABLE TBL_INDICATORS_SYSTEMS ADD CONSTRAINT PK_TBL_INDICATORS_SYSTEMS
	PRIMARY KEY (ID)
;

ALTER TABLE TBL_INDIC_SYSTEMS_VERSIONS ADD CONSTRAINT PK_TBL_INDIC_SYSTEMS_VERSIONS
	PRIMARY KEY (ID)
;

ALTER TABLE TBL_DIMENSIONS ADD CONSTRAINT PK_TBL_DIMENSIONS
	PRIMARY KEY (ID)
;

ALTER TABLE TBL_INDICATORS ADD CONSTRAINT PK_TBL_INDICATORS
	PRIMARY KEY (ID)
;

ALTER TABLE TBL_INDICATOR_VERSIONS ADD CONSTRAINT PK_TBL_INDICATOR_VERSIONS
	PRIMARY KEY (ID)
;

ALTER TABLE TBL_LOCALISED_STRINGS ADD CONSTRAINT PK_TBL_LOCALISED_STRINGS
	PRIMARY KEY (ID)
;

    

-- Unique constraints

ALTER TABLE TBL_INTERNATIONAL_STRINGS
    ADD CONSTRAINT UQ_TBL_INTERNATIONAL_STRINGS UNIQUE (UUID)
;


ALTER TABLE TBL_INDICATORS_SYSTEMS
    ADD CONSTRAINT UQ_TBL_INDICATORS_SYSTEMS UNIQUE (UUID)
;


ALTER TABLE TBL_INDIC_SYSTEMS_VERSIONS
    ADD CONSTRAINT UQ_TBL_INDIC_SYSTEMS_VERSIONS UNIQUE (UUID)
;


ALTER TABLE TBL_DIMENSIONS
    ADD CONSTRAINT UQ_TBL_DIMENSIONS UNIQUE (UUID)
;


ALTER TABLE TBL_INDICATORS
    ADD CONSTRAINT UQ_TBL_INDICATORS UNIQUE (UUID)
;


ALTER TABLE TBL_INDICATOR_VERSIONS
    ADD CONSTRAINT UQ_TBL_INDICATOR_VERSIONS UNIQUE (UUID)
;


ALTER TABLE TBL_LOCALISED_STRINGS
    ADD CONSTRAINT UQ_TBL_LOCALISED_STRINGS UNIQUE (UUID)
;



-- Foreign key constraints
    

  
ALTER TABLE TBL_LOCALISED_STRINGS ADD CONSTRAINT FK_TBL_LOCALISED_STRINGS_TBL13
	FOREIGN KEY (INTERNATIONAL_STRING_FK) REFERENCES TBL_INTERNATIONAL_STRINGS (ID)
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
ALTER TABLE TBL_INDIC_SYSTEMS_VERSIONS ADD CONSTRAINT FK_TBL_INDIC_SYSTEMS_VERSION67
	FOREIGN KEY (INDICATORS_SYSTEMS_FK) REFERENCES TBL_INDICATORS_SYSTEMS (ID)
;

  
ALTER TABLE TBL_DIMENSIONS ADD CONSTRAINT FK_TBL_DIMENSIONS_TITLE_FK
	FOREIGN KEY (TITLE_FK) REFERENCES TBL_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TBL_DIMENSIONS ADD CONSTRAINT FK_TBL_DIMENSIONS_PARENT_FK
	FOREIGN KEY (PARENT_FK) REFERENCES TBL_DIMENSIONS (ID)
;
ALTER TABLE TBL_DIMENSIONS ADD CONSTRAINT FK_TBL_DIMENSIONS_INDICATORS90
	FOREIGN KEY (INDICATORS_SYSTEM_VERSION_FK) REFERENCES TBL_INDIC_SYSTEMS_VERSIONS (ID)
;

  
  
ALTER TABLE TBL_INDICATOR_VERSIONS ADD CONSTRAINT FK_TBL_INDICATOR_VERSIONS_NA42
	FOREIGN KEY (NAME_FK) REFERENCES TBL_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TBL_INDICATOR_VERSIONS ADD CONSTRAINT FK_TBL_INDICATOR_VERSIONS_AC76
	FOREIGN KEY (ACRONYM_FK) REFERENCES TBL_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TBL_INDICATOR_VERSIONS ADD CONSTRAINT FK_TBL_INDICATOR_VERSIONS_CO28
	FOREIGN KEY (COMMENTARY) REFERENCES TBL_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TBL_INDICATOR_VERSIONS ADD CONSTRAINT FK_TBL_INDICATOR_VERSIONS_NO14
	FOREIGN KEY (NOTES) REFERENCES TBL_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TBL_INDICATOR_VERSIONS ADD CONSTRAINT FK_TBL_INDICATOR_VERSIONS_IN48
	FOREIGN KEY (INDICATOR_FK) REFERENCES TBL_INDICATORS (ID)
;

  
  
  

    

-- Index
  
  
  
  


