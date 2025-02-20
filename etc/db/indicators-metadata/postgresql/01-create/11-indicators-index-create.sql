-- ###########################################
-- # Create indexes
-- ###########################################

CREATE INDEX IX_TB_INDICATORS_VERSIONS_CATEGORY_ELEMENT_FK ON TB_INDICATORS_VERSIONS(CATEGORY_ELEMENT_FK);
CREATE INDEX IX_TB_EXTERNAL_ITEMS_CODE ON TB_EXTERNAL_ITEMS(CODE);

-- TO BETTER PERFORMANCE OF delete in tb_international_string table
CREATE INDEX pk_TB_CATEGORY_CACHE_TITLE_FK ON TB_CATEGORY_CACHE USING btree (TITLE_FK);
CREATE INDEX pk_TB_CATEGORY_CACHE_CATEGORY_ELEMENT_CODE ON TB_CATEGORY_CACHE USING btree (CATEGORY_ELEMENT_CODE);
CREATE INDEX pk_TB_CATEGORY_CACHE_CATEGORY_CODE ON TB_CATEGORY_CACHE USING btree (CATEGORY_CODE);

CREATE INDEX pk_tb_indic_systems_versions_title_fk ON tb_indic_systems_versions USING btree (title_fk);
CREATE INDEX pk_tb_indic_systems_versions_acronym_fk ON tb_indic_systems_versions USING btree (acronym_fk);
CREATE INDEX pk_tb_indic_systems_versions_description_fk ON tb_indic_systems_versions USING btree (description_fk); 
CREATE INDEX pk_tb_indic_systems_versions_objective_fk ON tb_indic_systems_versions USING btree (objective_fk); 

