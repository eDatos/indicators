 -- --------------------------------------------------------------------------------------------------
-- EDATOS-3827 - Integración con códigos geográficos de e-Semántica
-- 
-- Se añade índice sobre tb_localised_strings por el campo international_string_fk
-- --------------------------------------------------------------------------------------------------


CREATE INDEX tb_localised_strings_international_string_fk ON tb_localised_strings (international_string_fk);
