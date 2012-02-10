-- OJO! En el script de creación de base de datos Sculptor genera incorrectamente dos constraints en la tabla TBL_DIMENSION
-- sobre la columna PARENT_FK.
-- En el script '01-indicators-create.sql' hay que eliminar una de ellas

ALTER TABLE TBL_DIMENSIONS ADD CONSTRAINT FK_TBL_DIMENSIONS_TBL_DIMENS37
	FOREIGN KEY (PARENT_FK) REFERENCES TBL_DIMENSIONS (ID)
;
ALTER TABLE TBL_DIMENSIONS ADD CONSTRAINT FK_TBL_DIMENSIONS_TBL_DIMENS38
	FOREIGN KEY (PARENT_FK) REFERENCES TBL_DIMENSIONS (ID)
;