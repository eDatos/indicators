-- ###########################################
-- # Create
-- ###########################################
-- Create pk sequence
    


-- Create normal entities
    
CREATE TABLE TB_INTERNATIONAL_STRINGS (
  ID NUMBER(19) NOT NULL,
  UUID VARCHAR2(36 CHAR) NOT NULL,
  VERSION NUMBER(19) NOT NULL
);


CREATE TABLE TB_DATASETS (
  ID NUMBER(19) NOT NULL,
  DATASET_ID VARCHAR2(4000 CHAR) NOT NULL,
  TABLE_NAME VARCHAR2(255 CHAR) NOT NULL,
  MAX_ATTRIBUTES_OBSERVATION NUMBER(10) NOT NULL,
  LANGUAGES VARCHAR2(255 CHAR) NOT NULL,
  UUID VARCHAR2(36 CHAR) NOT NULL,
  VERSION NUMBER(19) NOT NULL
);


CREATE TABLE TB_ATTRIBUTES (
  ID NUMBER(19) NOT NULL,
  ATTRIBUTE_ID VARCHAR2(255 CHAR) NOT NULL,
  UUID VARCHAR2(36 CHAR) NOT NULL,
  VERSION NUMBER(19) NOT NULL,
  VALUE_FK NUMBER(19) NOT NULL,
  DATASET_FK NUMBER(19) NOT NULL
);


CREATE TABLE TB_ATTRIBUTE_DIMENSIONS (
  ID NUMBER(19) NOT NULL,
  DIMENSION_ID VARCHAR2(255 CHAR) NOT NULL,
  CODE_DIMENSION_ID VARCHAR2(255 CHAR),
  UUID VARCHAR2(36 CHAR) NOT NULL,
  VERSION NUMBER(19) NOT NULL,
  ATTRIBUTE_FK NUMBER(19) NOT NULL
);


CREATE TABLE TB_DATASET_ATTRIBUTES (
  ID NUMBER(19) NOT NULL,
  ATTRIBUTE_ID VARCHAR2(255 CHAR) NOT NULL,
  COLUMN_NAME VARCHAR2(255 CHAR),
  COLUMN_INDEX NUMBER(10),
  UUID VARCHAR2(36 CHAR) NOT NULL,
  VERSION NUMBER(19) NOT NULL,
  DATASET_FK NUMBER(19) NOT NULL,
  ATTACHMENT_LEVEL VARCHAR2(255 CHAR) NOT NULL
);


CREATE TABLE TB_DATASET_DIMENSIONS (
  ID NUMBER(19) NOT NULL,
  DIMENSION_ID VARCHAR2(255 CHAR) NOT NULL,
  COLUMN_NAME VARCHAR2(255 CHAR) NOT NULL,
  UUID VARCHAR2(36 CHAR) NOT NULL,
  VERSION NUMBER(19) NOT NULL,
  DATASET_FK NUMBER(19) NOT NULL
);


CREATE TABLE TB_LOCALISED_STRINGS (
  ID NUMBER(19) NOT NULL,
  LABEL VARCHAR2(4000 CHAR) NOT NULL,
  LOCALE VARCHAR2(10 CHAR) NOT NULL,
  UUID VARCHAR2(36 CHAR) NOT NULL,
  VERSION NUMBER(19) NOT NULL,
  INTERNATIONAL_STRING_FK NUMBER(19) NOT NULL
);



-- Create many to many relations
    

-- Primary keys
    
ALTER TABLE TB_INTERNATIONAL_STRINGS ADD CONSTRAINT PK_TB_INTERNATIONAL_STRINGS
	PRIMARY KEY (ID)
;

ALTER TABLE TB_DATASETS ADD CONSTRAINT PK_TB_DATASETS
	PRIMARY KEY (ID)
;

ALTER TABLE TB_ATTRIBUTES ADD CONSTRAINT PK_TB_ATTRIBUTES
	PRIMARY KEY (ID)
;

ALTER TABLE TB_ATTRIBUTE_DIMENSIONS ADD CONSTRAINT PK_TB_ATTRIBUTE_DIMENSIONS
	PRIMARY KEY (ID)
;

ALTER TABLE TB_DATASET_ATTRIBUTES ADD CONSTRAINT PK_TB_DATASET_ATTRIBUTES
	PRIMARY KEY (ID)
;

ALTER TABLE TB_DATASET_DIMENSIONS ADD CONSTRAINT PK_TB_DATASET_DIMENSIONS
	PRIMARY KEY (ID)
;

ALTER TABLE TB_LOCALISED_STRINGS ADD CONSTRAINT PK_TB_LOCALISED_STRINGS
	PRIMARY KEY (ID)
;

    

-- Unique constraints

ALTER TABLE TB_INTERNATIONAL_STRINGS
    ADD CONSTRAINT UQ_TB_INTERNATIONAL_STRINGS UNIQUE (UUID)
;


ALTER TABLE TB_DATASETS
    ADD CONSTRAINT UQ_TB_DATASETS UNIQUE (UUID)
;


ALTER TABLE TB_ATTRIBUTES
    ADD CONSTRAINT UQ_TB_ATTRIBUTES UNIQUE (UUID)
;


ALTER TABLE TB_ATTRIBUTE_DIMENSIONS
    ADD CONSTRAINT UQ_TB_ATTRIBUTE_DIMENSIONS UNIQUE (UUID)
;


ALTER TABLE TB_DATASET_ATTRIBUTES
    ADD CONSTRAINT UQ_TB_DATASET_ATTRIBUTES UNIQUE (UUID)
;


ALTER TABLE TB_DATASET_DIMENSIONS
    ADD CONSTRAINT UQ_TB_DATASET_DIMENSIONS UNIQUE (UUID)
;


ALTER TABLE TB_LOCALISED_STRINGS
    ADD CONSTRAINT UQ_TB_LOCALISED_STRINGS UNIQUE (UUID)
;



-- Foreign key constraints
    

  
  
  
  
ALTER TABLE TB_ATTRIBUTES ADD CONSTRAINT FK_TB_ATTRIBUTES_VALUE_FK
	FOREIGN KEY (VALUE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_ATTRIBUTES ADD CONSTRAINT FK_TB_ATTRIBUTES_DATASET_FK
	FOREIGN KEY (DATASET_FK) REFERENCES TB_DATASETS (ID) ON DELETE CASCADE
;

  
ALTER TABLE TB_ATTRIBUTE_DIMENSIONS ADD CONSTRAINT FK_TB_ATTRIBUTE_DIMENSIONS_A50
	FOREIGN KEY (ATTRIBUTE_FK) REFERENCES TB_ATTRIBUTES (ID) ON DELETE CASCADE
;

  
ALTER TABLE TB_DATASET_ATTRIBUTES ADD CONSTRAINT FK_TB_DATASET_ATTRIBUTES_DAT68
	FOREIGN KEY (DATASET_FK) REFERENCES TB_DATASETS (ID) ON DELETE CASCADE
;

  
ALTER TABLE TB_DATASET_DIMENSIONS ADD CONSTRAINT FK_TB_DATASET_DIMENSIONS_DAT02
	FOREIGN KEY (DATASET_FK) REFERENCES TB_DATASETS (ID) ON DELETE CASCADE
;

  
ALTER TABLE TB_LOCALISED_STRINGS ADD CONSTRAINT FK_TB_LOCALISED_STRINGS_INTE13
	FOREIGN KEY (INTERNATIONAL_STRING_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID) ON DELETE CASCADE
;

  

    

-- Index


