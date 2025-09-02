-- --------------------------------------------------------------------------------------------------
-- EDATOS-5154 - Añadir descripciones a las vistas de datos
-- 
-- Actualiza función utilizada en el versionado de indicadores para clonar tablas. En particular actualiza la función que clona la tabla tb_dataset_dimensions

-- --------------------------------------------------------------------------------------------------


CREATE OR REPLACE FUNCTION clone_tb_dataset_dimensions(p_old_id numeric, p_new_id numeric)
 RETURNS void
 LANGUAGE plpgsql
AS $function$
DECLARE
	VA_COUNTER   numeric;
BEGIN
	SELECT COUNT (1)
	INTO VA_COUNTER
	FROM TB_DATASET_DIMENSIONS
	WHERE DATASET_FK = P_OLD_ID;

	IF VA_COUNTER > 0
	THEN
		INSERT INTO TB_DATASET_DIMENSIONS
			(ID, DIMENSION_ID, COLUMN_NAME, UUID, VERSION, DATASET_FK, SOURCE_URN)
			(SELECT nextval('SEQ_DASET_DIMS') AS ID,
                    DIMENSION_ID AS DIMENSION_ID,
                    COLUMN_NAME AS COLUMN_NAME,
                    RANDOM_STRING(36) AS UUID,
                    VERSION AS VERSION,
                    P_NEW_ID AS DATASET_FK,
                    SOURCE_URN
			FROM TB_DATASET_DIMENSIONS
			WHERE DATASET_FK = P_OLD_ID);
	END IF;
END;
$function$;

COMMIT;