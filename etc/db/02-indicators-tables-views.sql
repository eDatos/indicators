-- En el Istac será una vista de una tabla

-- Creación de la vista en el ISTAC:
-- CREATE OR REPLACE FORCE VIEW tv_areas_tematicas (id_area_tematica,descripcion) AS SELECT id_catalogo, descripcion FROM tb_catalogo_operacion_estadist;

CREATE TABLE TV_AREAS_TEMATICAS (
  ID_AREA_TEMATICA VARCHAR2(15 BYTE) NOT NULL,
  DESCRIPCION VARCHAR2(200 BYTE) NOT NULL
);

ALTER TABLE TV_AREAS_TEMATICAS ADD CONSTRAINT PK_TV_AREAS_TEMATICAS
  PRIMARY KEY (ID_AREA_TEMATICA)
;

