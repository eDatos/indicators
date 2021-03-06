
    
-- ###########################################
-- # Drop
-- ###########################################
-- Drop index


-- Drop many to many relations
    
-- Drop normal entities
    
DROP TABLE TB_LOCALISED_STRINGS CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_DATASET_DIMENSIONS CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_DATASET_ATTRIBUTES CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_ATTRIBUTE_DIMENSIONS CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_ATTRIBUTES CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_DATASETS CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_INTERNATIONAL_STRINGS CASCADE CONSTRAINTS PURGE;


-- Drop pk sequence
    

DROP sequence SEQ_DATASETS;
DROP sequence SEQ_DASET_DIMS;
DROP sequence SEQ_DATASET_ATTRIBUTES;
DROP sequence SEQ_ATTRIBUTES;
DROP sequence SEQ_ATTR_DIMS;
DROP sequence SEQ_I18NSTRS;
DROP sequence SEQ_L10NSTRS;