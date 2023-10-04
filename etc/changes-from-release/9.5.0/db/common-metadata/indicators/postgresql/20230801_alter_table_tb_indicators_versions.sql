-- --------------------------------------------------------------------------------------------------
-- EDATOS-4185 - Añadir metadato elemento de tema a los indicadores
-- 
--  Crear nuevo campo elemento de tema para un indicador.
-- --------------------------------------------------------------------------------------------------

 ALTER TABLE TB_INDICATORS_VERSIONS ADD COLUMN CATEGORY_ELEMENT_FK BIGINT;
  
ALTER TABLE TB_INDICATORS_VERSIONS ADD CONSTRAINT FK_TB_INDICATORS_VERSIONS_CATEGORY_ELEMENT_FK
	FOREIGN KEY (CATEGORY_ELEMENT_FK) REFERENCES TB_EXTERNAL_ITEMS (ID);
	
CREATE INDEX IX_TB_INDICATORS_VERSIONS_CATEGORY_ELEMENT_FK ON TB_INDICATORS_VERSIONS(CATEGORY_ELEMENT_FK);

commit;