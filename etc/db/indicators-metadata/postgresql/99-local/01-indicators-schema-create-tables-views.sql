-- En el Istac será una vista de una tabla

-- Creación de la vista en el ISTAC:
-- CREATE OR REPLACE FORCE VIEW tv_areas_tematicas (id_area_tematica,descripcion) AS SELECT id_catalogo, descripcion FROM tb_catalogo_operacion_estadist;
-- Area tematica
CREATE TABLE TV_AREAS_TEMATICAS (
  ID_AREA_TEMATICA VARCHAR(15) NOT NULL,
  DESCRIPCION VARCHAR(200) NOT NULL
);

ALTER TABLE TV_AREAS_TEMATICAS ADD CONSTRAINT PK_TV_AREAS_TEMATICAS
  PRIMARY KEY (ID_AREA_TEMATICA)
;

-- Consultas del gpe, OJO!!!! se puede simular de dos formas 
-- 1) simulando una tabla:
CREATE TABLE TV_CONSULTA (
  ID_CONSULTA BIGINT NOT NULL,
  UUID_CONSULTA VARCHAR(255),
  NOMBRE_CONSULTA VARCHAR(255),
  TIPO_CONSULTA VARCHAR(255),
  CONSULTA TEXT,
  URI_PX VARCHAR(255) NOT NULL,
  VERSION_PX VARCHAR(255),
  ID_OPERACION VARCHAR(7) NOT NULL,
  USUARIO_CREACION VARCHAR(255) NOT NULL,
  FECHA_CREACION TIMESTAMP(6) NOT NULL,
  USUARIO_MODIFICACION VARCHAR(255),
  FECHA_MODIFICACION TIMESTAMP(6),
  FECHA_MODIFICACION_DATOS TIMESTAMP(6),
  AUTOINCREMENTO VARCHAR(100),
  FECHA_DISPONIBLE_INICIO TIMESTAMP(6),
  FECHA_DISPONIBLE_FIN TIMESTAMP(6),
  IS_PART_OF TEXT
);

ALTER TABLE TV_CONSULTA ADD CONSTRAINT PK_TV_CONSULTA
  PRIMARY KEY (ID_CONSULTA)
;

-- 2) O con una vista a otro esquema, pero necesita permisos para ver la tabla
grant select on USUARIO_PROPIETARIO.tb_consulta to USUARIO_ACTUAL with grant option

CREATE OR REPLACE VIEW tv_consulta (id_consulta,
autoincremento, consulta, fecha_creacion, fecha_disponible_fin,
fecha_disponible_inicio, fecha_modificacion, fecha_modificacion_datos,
id_operacion, uri_px, is_part_of, nombre_consulta, tipo_consulta,
usuario_creacion, usuario_modificacion, uuid_consulta, version_px)
AS
SELECT id_consulta, autoincremento, consulta, fecha_creacion,
fecha_disponible_fin, fecha_disponible_inicio, fecha_modificacion, fecha_modificacion_datos,
id_operacion, uri_px, is_part_of, nombre_consulta, tipo_consulta,
usuario_creacion, usuario_modificacion, uuid_consulta, version_px
FROM USUARIO_PROPIETARIO.tB_consulta
ORDER BY id_consulta;

