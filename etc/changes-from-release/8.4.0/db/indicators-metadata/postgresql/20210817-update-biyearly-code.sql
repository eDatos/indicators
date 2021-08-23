-- --------------------------------------------------------------------------------------------------
-- EDATOS-3037 - Problemas varios asociados al código de MetamacTimeUtils
-- --------------------------------------------------------------------------------------------------

UPDATE TB_TRANSLATIONS SET CODE = 'TIME_VALUE.BIYEARLY.H1' where code = 'TIME_VALUE.BIYEARLY.S1';
UPDATE TB_TRANSLATIONS SET CODE = 'TIME_VALUE.BIYEARLY.H2' where code = 'TIME_VALUE.BIYEARLY.S2';

commit;