CREATE OR REPLACE FUNCTION get_external_item_dimension_title(
    p_dimension_source_urn text,
    p_dimension_column_name text,
    p_dimension_value text,
    p_language text
)
RETURNS TEXT 
LANGUAGE plpgsql
AS $function$
DECLARE
    dimension_title TEXT;
    dim_id TEXT;
BEGIN

    SELECT dd.dimension_id
    INTO dim_id
    FROM TB_DATASETS d
    INNER JOIN TB_DATASET_DIMENSIONS dd 
        ON dd.dataset_fk = d.id AND dd.column_name = p_dimension_column_name
    WHERE d.dataset_id = p_dimension_source_urn;

    IF dim_id = 'GEOGRAPHICAL' THEN
        --  titles for geographical dimension must be searched by variable element
		SELECT ls.label
        INTO dimension_title
        FROM TB_DATASETS d 
        INNER JOIN TB_DATASET_DIMENSIONS dd 
            ON dd.dataset_fk = d.id AND dd.column_name = p_dimension_column_name
        INNER JOIN TB_EXTERNAL_ITEMS ei 
            ON ei.urn = dd.source_urn
        INNER JOIN TB_EXTERNAL_ITEMS_CODES eic 
            ON eic.external_item_fk = ei.id AND eic.element_code = p_dimension_value
        INNER JOIN TB_LOCALISED_STRINGS ls 
            ON ls.international_string_fk = eic.title_fk AND ls.locale = p_language
        WHERE d.dataset_id = p_dimension_source_urn;      
    ELSE
        -- For other cases must be searched by code of codelist
        SELECT ls.label
        INTO dimension_title
        FROM TB_DATASETS d 
        INNER JOIN TB_DATASET_DIMENSIONS dd 
            ON dd.dataset_fk = d.id AND dd.column_name = p_dimension_column_name
        INNER JOIN TB_EXTERNAL_ITEMS ei 
            ON ei.urn = dd.source_urn
        INNER JOIN TB_EXTERNAL_ITEMS_CODES eic 
            ON eic.external_item_fk = ei.id AND eic.code = p_dimension_value
        INNER JOIN TB_LOCALISED_STRINGS ls 
            ON ls.international_string_fk = eic.title_fk AND ls.locale = p_language
        WHERE d.dataset_id = p_dimension_source_urn;
    END IF;

    RETURN dimension_title;
END;
$function$;