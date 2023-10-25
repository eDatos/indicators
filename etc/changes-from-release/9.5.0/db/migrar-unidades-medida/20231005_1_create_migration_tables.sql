-- --------------------------------------------------------------------------------------------------
-- EDATOS-4197 - Añadir metadato unidad de medida que utilice un external item de de un código de clasificación del srm.

--PRECONDICIÓN: deben haberse lanzado el resto de scripts asociados a la tarea
-- --------------------------------------------------------------------------------------------------

CREATE TABLE temp_mig_units (
  CODE VARCHAR(255) NOT NULL,
  CODE_NESTED VARCHAR(255),
  URI VARCHAR(4000) NOT NULL,
  URN VARCHAR(4000),
  URN_PROVIDER VARCHAR(4000),
  MANAGEMENT_APP_URL VARCHAR(4000),
  VERSION BIGINT NOT NULL,
  TYPE VARCHAR(255) NOT null,
  label_es VARCHAR(4000),
  label_ca VARCHAR(4000),
  label_en VARCHAR(4000),
  id_unit_tb_lis_quantities BIGINT 
);

commit;
