-- --------------------------------------------------------------------------------------------------
-- EDATOS-5154 - Añadir descripciones a las vistas de datos
-- 
-- Crear índice en claves foráneas a international string para mejorar el rendimiento. Ahora que va a haber numerosas entradas a esta table puede empeorar en borrados
--si las claves foráneas de cada tabla no tienen índice.

-- --------------------------------------------------------------------------------------------------

--indexes for better performance.
CREATE INDEX pk_tb_attributes_value_fk ON tb_attributes USING btree (value_fk);
CREATE INDEX pk_tb_localised_strings_international_string_fk ON tb_localised_strings USING btree (international_string_fk);

CREATE INDEX idx_tb_datasets_dataset_id ON tb_datasets(dataset_id);
CREATE INDEX idx_tb_dataset_dimensions_fk_col ON tb_dataset_dimensions(dataset_fk, column_name);
CREATE INDEX idx_tb_external_items_codes_fk_code ON tb_external_items_codes(external_item_fk, code);
CREATE UNIQUE INDEX tb_localised_strings_international_string_fk_locale ON tb_localised_strings (international_string_fk, locale);
commit;