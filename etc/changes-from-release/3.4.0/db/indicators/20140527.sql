alter table TB_INDIC_INST_LAST_VALUE add GEOGRAPHICAL_CODE VARCHAR2(255 CHAR);


update tb_indic_inst_last_value inst1
set geographical_code = (select geoVal.code 
                        from tb_indic_inst_last_value instLast 
                          inner join tb_lis_geogr_values geoVal on geoVal.id = instLast.GEOGRAPHICAL_VALUE_FK
                          where instLast.id = inst1.id);
                          
                          
           
alter table TB_INDIC_INST_LAST_VALUE drop constraint FK_TB_INDIC_INST_LAST_VALUE_43;    
           
alter table TB_INDIC_INST_LAST_VALUE drop constraint UQ_TB_INDIC_INST_LAST_VALUE;    
  
alter table TB_INDIC_INST_LAST_VALUE drop column GEOGRAPHICAL_VALUE_FK;

alter table TB_INDIC_INST_LAST_VALUE modify GEOGRAPHICAL_CODE VARCHAR2(255 CHAR) not null;

ALTER TABLE TB_INDIC_INST_LAST_VALUE
	ADD CONSTRAINT UQ_TB_INDIC_INST_LAST_VALUE UNIQUE (GEOGRAPHICAL_CODE, INDICATOR_INSTANCE_FK)
;



alter table TB_INDIC_VERSION_LAST_VALUE add GEOGRAPHICAL_CODE VARCHAR2(255 CHAR);


update TB_INDIC_VERSION_LAST_VALUE inst1
set geographical_code = (select geoVal.code 
                        from TB_INDIC_VERSION_LAST_VALUE instLast 
                          inner join tb_lis_geogr_values geoVal on geoVal.id = instLast.GEOGRAPHICAL_VALUE_FK
                          where instLast.id = inst1.id);
                          
           
alter table TB_INDIC_VERSION_LAST_VALUE drop constraint FK_TB_INDIC_VERSION_LAST_VAL07;    
           
alter table TB_INDIC_VERSION_LAST_VALUE drop constraint UQ_TB_INDIC_VERSION_LAST_VALUE;    
  
alter table TB_INDIC_VERSION_LAST_VALUE drop column GEOGRAPHICAL_VALUE_FK;

alter table TB_INDIC_VERSION_LAST_VALUE modify GEOGRAPHICAL_CODE VARCHAR2(255 CHAR) not null;

ALTER TABLE TB_INDIC_VERSION_LAST_VALUE
	ADD CONSTRAINT UQ_TB_INDIC_VERSION_LAST_VALUE UNIQUE (GEOGRAPHICAL_CODE, INDICATOR_VERSION_FK)
;
