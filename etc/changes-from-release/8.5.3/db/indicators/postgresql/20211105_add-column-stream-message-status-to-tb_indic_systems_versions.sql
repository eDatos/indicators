-- --------------------------------------------------------------------------------------------------
-- EDATOS-3485 - Conector de kafka para la publicación de indicadores
-- --------------------------------------------------------------------------------------------------

-- Añade nueva columna a la tabla de versiones de sistemas de indicadores que determina el estado de transmisión a través de Kafka

ALTER TABLE TB_INDIC_SYSTEMS_VERSIONS
ADD COLUMN STREAM_MESSAGE_STATUS VARCHAR(255);

-- 'Update' statement without 'where' updates all table rows at once
UPDATE TB_INDIC_SYSTEMS_VERSIONS
SET STREAM_MESSAGE_STATUS = 'PENDING';

ALTER TABLE TB_INDIC_SYSTEMS_VERSIONS
ALTER COLUMN STREAM_MESSAGE_STATUS SET NOT NULL;

commit;
