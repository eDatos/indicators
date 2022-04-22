-- --------------------------------------------------------------------------------------------------
-- EDATOS-3485 - Conector de kafka para la publicación de indicadores
-- --------------------------------------------------------------------------------------------------

-- Añade nueva columna a la tabla de versiones de indicadores que determina el estado de transmisión a través de Kafka

ALTER TABLE TB_INDICATORS_VERSIONS
ADD STREAM_MESSAGE_STATUS VARCHAR2(255 CHAR);

-- 'Update' statement without 'where' updates all table rows at once
UPDATE TB_INDICATORS_VERSIONS
SET STREAM_MESSAGE_STATUS = 'PENDING';

ALTER TABLE TB_INDICATORS_VERSIONS
MODIFY STREAM_MESSAGE_STATUS VARCHAR2(255 CHAR) NOT NULL;

commit;
