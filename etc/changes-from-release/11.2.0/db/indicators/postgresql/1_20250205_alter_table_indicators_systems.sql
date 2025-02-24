 -- --------------------------------------------------------------------------------------------------
-- EDATOS-4833 - Permitir crear sistemas de indicadores no asociados a operaciones estadísticas
-- 
-- Script que añade  columnas en la tabla de sistemas de indicadores que permitan registrar un sistema que no tenga operación estadística asociada.
-- --------------------------------------------------------------------------------------------------

 --1) Crear campos para tabla TB_INDICATORS_SYSTEMS
 ALTER TABLE TB_INDICATORS_SYSTEMS ADD COLUMN IS_OPERATIONAL BOOLEAN;

    
 --2) Actualizar valor boolean
 UPDATE TB_INDICATORS_SYSTEMS 
SET IS_OPERATIONAL = TRUE;

--3) Poner campo boolean a not null
  ALTER TABLE TB_INDICATORS_SYSTEMS ALTER COLUMN IS_OPERATIONAL set NOT NULL;
 
 
--4) Crear campos para tabla TB_INDIC_SYSTEMS_VERSIONS
--4.1) international strings
 ALTER TABLE TB_INDIC_SYSTEMS_VERSIONS ADD COLUMN ACRONYM_FK BIGINT;
 ALTER TABLE TB_INDIC_SYSTEMS_VERSIONS ADD COLUMN DESCRIPTION_FK BIGINT;
 ALTER TABLE TB_INDIC_SYSTEMS_VERSIONS ADD COLUMN TITLE_FK BIGINT;
 ALTER TABLE TB_INDIC_SYSTEMS_VERSIONS ADD COLUMN OBJECTIVE_FK BIGINT;

--4.2) crear constraint a tabla international strings
ALTER TABLE TB_INDIC_SYSTEMS_VERSIONS ADD CONSTRAINT FK_TB_INDIC_SYSTEMS_VERSIONS_TITLE_FK
	FOREIGN KEY (TITLE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_INDIC_SYSTEMS_VERSIONS ADD CONSTRAINT FK_TB_INDIC_SYSTEMS_VERSIONS_DESCRIPTION_FK
	FOREIGN KEY (DESCRIPTION_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_INDIC_SYSTEMS_VERSIONS ADD CONSTRAINT FK_TB_INDIC_SYSTEMS_VERSIONS_ACRONYM_FK
	FOREIGN KEY (ACRONYM_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_INDIC_SYSTEMS_VERSIONS ADD CONSTRAINT FK_TB_INDIC_SYSTEMS_VERSIONS_OBJECTIVE_FK
	FOREIGN KEY (OBJECTIVE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;

--4.3) Crear índices de rendimiento en borrados para los nuevos campos
CREATE INDEX pk_tb_indic_systems_versions_title_fk ON tb_indic_systems_versions USING btree (title_fk);
CREATE INDEX pk_tb_indic_systems_versions_acronym_fk ON tb_indic_systems_versions USING btree (acronym_fk);
CREATE INDEX pk_tb_indic_systems_versions_description_fk ON tb_indic_systems_versions USING btree (description_fk); 
CREATE INDEX pk_tb_indic_systems_versions_objective_fk ON tb_indic_systems_versions USING btree (objective_fk); 


--4.4) Crear campos optimistic version para guardar en  TB_INDIC_SYSTEMS_VERSIONS
 ALTER TABLE TB_INDIC_SYSTEMS_VERSIONS ADD COLUMN UPDATE_DATE_TZ VARCHAR(50);
 ALTER TABLE TB_INDIC_SYSTEMS_VERSIONS ADD COLUMN UPDATE_DATE TIMESTAMP;

 commit;
  