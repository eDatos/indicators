-- --------------------------------------------------------------------------------------------------
-- Fix incorrect PK constraint names (double TB prefix) on TB_EXTERNAL_ITEMS and
-- TB_EXTERNAL_ITEMS_CODES, and replace UNIQUE CONSTRAINTs with UNIQUE INDEXes
-- to align with the indicators DB schema style introduced in EDATOS-5154.
-- --------------------------------------------------------------------------------------------------

-- ATENCIÓN!!!!! Lanzar en la bd indicators_data

-- Rename PK: PK_TB_TB_EXTERNAL_ITEMS -> PK_TB_EXTERNAL_ITEMS
ALTER TABLE TB_EXTERNAL_ITEMS RENAME CONSTRAINT PK_TB_TB_EXTERNAL_ITEMS TO PK_TB_EXTERNAL_ITEMS;

-- Rename PK: PK_TB_TB_EXTERNAL_ITEMS_CODES -> PK_TB_EXTERNAL_ITEMS_CODES
ALTER TABLE TB_EXTERNAL_ITEMS_CODES RENAME CONSTRAINT PK_TB_TB_EXTERNAL_ITEMS_CODES TO PK_TB_EXTERNAL_ITEMS_CODES;

-- Replace UNIQUE CONSTRAINT with UNIQUE INDEX on TB_EXTERNAL_ITEMS(uuid)
ALTER TABLE TB_EXTERNAL_ITEMS DROP CONSTRAINT uq_tb_external_items;
CREATE UNIQUE INDEX uq_tb_external_items ON tb_external_items (uuid);

-- Replace UNIQUE CONSTRAINT with UNIQUE INDEX on TB_EXTERNAL_ITEMS_CODES(external_item_fk, code)
ALTER TABLE TB_EXTERNAL_ITEMS_CODES DROP CONSTRAINT uq_tb_external_items_codes;
CREATE UNIQUE INDEX uq_tb_external_items_codes ON tb_external_items_codes (external_item_fk, code);

COMMIT;
