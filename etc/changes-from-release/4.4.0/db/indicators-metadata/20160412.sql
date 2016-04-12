-- ------------------------------------------------------------------------------
-- INDISTAC-979 - Eliminar metadato inconsistentData
-- ------------------------------------------------------------------------------

alter table TB_INDICATORS_VERSIONS drop column INCONSISTENT_DATA;
commit;