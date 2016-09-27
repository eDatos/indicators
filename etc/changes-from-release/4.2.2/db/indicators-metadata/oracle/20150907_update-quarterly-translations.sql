-- ----------------------------------------------------------------------------------------
-- INDISTAC-934 - Las traducciones de tiempo ponen cuatrimestre en lugar de trimestre
-- ----------------------------------------------------------------------------------------

UPDATE TB_LOCALISED_STRINGS SET LABEL = '{yyyy} Primer trimestre' WHERE LABEL = '{yyyy} Primer cuatrimestre' and LOCALE='es';
UPDATE TB_LOCALISED_STRINGS SET LABEL = '{yyyy} Segundo trimestre' WHERE LABEL = '{yyyy} Segundo cuatrimestre' and LOCALE='es';
UPDATE TB_LOCALISED_STRINGS SET LABEL = '{yyyy} Tercer trimestre' WHERE LABEL = '{yyyy} Tercer cuatrimestre' and LOCALE='es';
UPDATE TB_LOCALISED_STRINGS SET LABEL = '{yyyy} Cuarto trimestre' WHERE LABEL =  '{yyyy} Cuarto cuatrimestre' and LOCALE='es';
UPDATE TB_LOCALISED_STRINGS SET LABEL = 'Trimestral' WHERE LABEL =  'Cuatrimestral' and LOCALE='es';
commit;