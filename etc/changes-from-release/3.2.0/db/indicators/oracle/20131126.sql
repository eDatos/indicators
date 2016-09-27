UPDATE tb_localised_strings
SET locale = 'en'
WHERE ID IN (
SELECT id FROM tb_localised_strings
WHERE locale = 'em'
AND international_string_fk = (SELECT title_fk FROM tb_translations
where code = 'TIME_VALUE.BIYEARLY.H2'));

UPDATE tb_localised_strings
SET locale = 'en'
WHERE ID IN (
SELECT id FROM tb_localised_strings
WHERE locale = 'em'
AND international_string_fk = (SELECT title_fk FROM tb_translations
where code = 'TIME_VALUE.BIYEARLY.H1'));