-- --------------------------------------------------------------------------------------------------
-- EDATOS-4197 - Añadir metadato unidad de medida que utilice un external item de de un código de clasificación del srm.
-- 
-- Se depreca el campo unit_fk como clave foránea a tb_lis_quantities_unit para poner el nuevo campo unit_fk como clave foránea a external items.
-- Se crea nuevo campo unit_fk como clave foránea a external items.
-- --------------------------------------------------------------------------------------------------

ALTER TABLE TB_QUANTITIES RENAME COLUMN unit_fk TO deprecated_unit_fk;

ALTER TABLE TB_QUANTITIES drop constraint fk_tb_quantities_unit_fk;

 ALTER TABLE TB_QUANTITIES ADD COLUMN UNIT_FK BIGINT;
  
ALTER TABLE TB_QUANTITIES ADD CONSTRAINT TB_QUANTITIES_UNIT_FK
	FOREIGN KEY (UNIT_FK) REFERENCES TB_EXTERNAL_ITEMS (ID);
	
commit;	