-- --------------------------------------------------------------------------------------------------
-- EDATOS-5154 - Añadir descripciones a las vistas de datos
-- 
-- Crear las tablas donde se almacenará la relación de códigos de cada clasificación ó esquema de conceptos del srm.

-- --------------------------------------------------------------------------------------------------

-- Drop table

-- DROP TABLE TB_EXTERNAL_ITEMS;

CREATE TABLE TB_EXTERNAL_ITEMS (
	id int8 NOT NULL,
	type varchar(255) NOT NULL,
	urn varchar(4000) NOT NULL,
	"uuid" varchar(36) NOT NULL,
	"version" int8 NOT NULL,
	creation_date_tz varchar(50),
	creation_date timestamp,
	last_update_date_tz varchar(50),
	last_update_date timestamp,
	CONSTRAINT pk_tb_external_items PRIMARY KEY (id)
);
CREATE INDEX idx_tb_external_items_urn ON tb_external_items (urn);
CREATE UNIQUE INDEX uq_tb_external_items ON tb_external_items ("uuid");

--SEQUENCE
create sequence SEQ_EXTERNAL_ITEMS;


-- Drop table

-- DROP TABLE TB_EXTERNAL_ITEMS_CODES;

CREATE TABLE TB_EXTERNAL_ITEMS_CODES (
	id int8 NOT NULL,
	"uuid" varchar(36) NOT NULL,
	"version" int8 NOT NULL,
	external_item_fk int8 NOT NULL,
	code varchar(255) NOT NULL,
	title_fk int8 NOT NULL,
	element_code varchar(255) 
	CONSTRAINT pk_tb_external_items_codes PRIMARY KEY (id) 
);
CREATE INDEX idx_tb_external_items_codes_title_fk ON tb_external_items_codes  (title_fk);
CREATE UNIQUE INDEX uq_tb_external_items_codes on tb_external_items_codes (external_item_fk, code);
CREATE INDEX idx_tb_external_items_codes_element_code ON tb_external_items_codes  (element_code);

-- TB_EXTERNAL_ITEMS_CODES foreign keys

ALTER TABLE TB_EXTERNAL_ITEMS_CODES ADD CONSTRAINT fk_tb_external_items_codes_external_item_fk FOREIGN KEY (external_item_fk) REFERENCES tb_external_items(id) ON DELETE CASCADE;
ALTER TABLE TB_EXTERNAL_ITEMS_CODES ADD CONSTRAINT fk_tb_external_items_codes_title_fk FOREIGN KEY (title_fk) REFERENCES tb_international_strings(id) ON DELETE CASCADE;

--SEQUENCE
create sequence SEQ_EXTERNAL_ITEMS_CODES;


COMMIT;