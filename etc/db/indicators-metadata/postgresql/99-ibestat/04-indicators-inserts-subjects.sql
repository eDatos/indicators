-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- IBESTATNORMA-15 - Carga inicial indicadores
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------
-- Fichero elaborado a partir del esquema de temas IBESTAT:TEMAS_BALEARS(01.000)
-- Enlace. https://pre-ibestat.edatos.io/structural-resources-internal/#structuralResources/categorySchemes/categoryScheme;id=IBESTAT%3ATEMAS_BALEARS(01.000) 
-- 
-- Notas:
-- 1. Es necesario tener instalado la extensión uuid-ossp para la generación de los uuids de los elementos insertados 
-- -- Para ello es necesario ejecutar la siguiente sentencia como administrador de la bbdd CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
-- 2. Únicamente se cargan los temas de nivel 2
-- 3. Se excluye el valor 100 - Infraestructura estadística / Infraestructura estadística
-- 4. La descripción de los registros a insertar se construye de la forma [NOMBRE_DEL_TEMA_EN_CATALÁN] / [NOMBRE_DEL_TEMA_EN_ESPAÑOL]
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------------

-- Tema: 090
INSERT INTO TV_AREAS_TEMATICAS (ID_AREA_TEMATICA, DESCRIPCION) VALUES ('090', 'Estadístiques no desglossades per tema / Estadísticas no desglosables por tema');
 
-- Tema: 040
INSERT INTO TV_AREAS_TEMATICAS (ID_AREA_TEMATICA, DESCRIPCION) VALUES ('040', 'Territori i medi ambient / Territorio y medio ambiente');
 
-- Tema: 030
INSERT INTO TV_AREAS_TEMATICAS (ID_AREA_TEMATICA, DESCRIPCION) VALUES ('030', 'Societat / Sociedad');
 
-- Tema: 020
INSERT INTO TV_AREAS_TEMATICAS (ID_AREA_TEMATICA, DESCRIPCION) VALUES ('020', 'Economia / Economía');
 
-- Tema: 010
INSERT INTO TV_AREAS_TEMATICAS (ID_AREA_TEMATICA, DESCRIPCION) VALUES ('010', 'Demografia / Demografía');

commit;