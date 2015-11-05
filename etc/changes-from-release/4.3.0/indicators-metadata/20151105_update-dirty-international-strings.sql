-- --------------------------------------------------------------------------------------------------
-- INDISTAC-956 - El metadato notes se cumplimenta automáticamente en el gestor con el valor "null"
-- --------------------------------------------------------------------------------------------------

-- TB_INDICATORS_VERSIONS.TITLE_FK
-- ..................................................................................................................................

delete from TB_LOCALISED_STRINGS loc
where loc.ID = (select loc.ID as LOCALISED_STRING_ID
                from TB_INDICATORS_VERSIONS ind left join TB_INTERNATIONAL_STRINGS ins on ins.ID = ind.TITLE_FK left join TB_LOCALISED_STRINGS loc on loc.INTERNATIONAL_STRING_FK = ins.ID
                where loc.LABEL like 'null');

update TB_INDICATORS_VERSIONS indver
set TITLE_FK = null
where indver.TITLE_FK = (select ins.ID
                            from TB_INTERNATIONAL_STRINGS ins
                            where ins.ID NOT IN (select loc.INTERNATIONAL_STRING_FK
                                                from TB_LOCALISED_STRINGS loc));


-- TB_INDICATORS_VERSIONS.ACRONYM_FK
-- ..................................................................................................................................

delete from TB_LOCALISED_STRINGS loc
where loc.ID = (select loc.ID as LOCALISED_STRING_ID
                from TB_INDICATORS_VERSIONS ind left join TB_INTERNATIONAL_STRINGS ins on ins.ID = ind.ACRONYM_FK left join TB_LOCALISED_STRINGS loc on loc.INTERNATIONAL_STRING_FK = ins.ID
                where loc.LABEL like 'null');

update TB_INDICATORS_VERSIONS indver
set ACRONYM_FK = null
where indver.ACRONYM_FK = (select ins.ID
                            from TB_INTERNATIONAL_STRINGS ins
                            where ins.ID NOT IN (select loc.INTERNATIONAL_STRING_FK
                                                from TB_LOCALISED_STRINGS loc));


-- TB_INDICATORS_VERSIONS.SUBJECT_TITLE_FK
-- ..................................................................................................................................

delete from TB_LOCALISED_STRINGS loc
where loc.ID = (select loc.ID as LOCALISED_STRING_ID
                from TB_INDICATORS_VERSIONS ind left join TB_INTERNATIONAL_STRINGS ins on ins.ID = ind.SUBJECT_TITLE_FK left join TB_LOCALISED_STRINGS loc on loc.INTERNATIONAL_STRING_FK = ins.ID
                where loc.LABEL like 'null');

update TB_INDICATORS_VERSIONS indver
set SUBJECT_TITLE_FK = null
where indver.SUBJECT_TITLE_FK = (select ins.ID
                            from TB_INTERNATIONAL_STRINGS ins
                            where ins.ID NOT IN (select loc.INTERNATIONAL_STRING_FK
                                                from TB_LOCALISED_STRINGS loc));


-- TB_INDICATORS_VERSIONS.CONCEPT_DESCRIPTION_FK
-- ..................................................................................................................................

delete from TB_LOCALISED_STRINGS loc
where loc.ID = (select loc.ID as LOCALISED_STRING_ID
                from TB_INDICATORS_VERSIONS ind left join TB_INTERNATIONAL_STRINGS ins on ins.ID = ind.CONCEPT_DESCRIPTION_FK left join TB_LOCALISED_STRINGS loc on loc.INTERNATIONAL_STRING_FK = ins.ID
                where loc.LABEL like 'null');

update TB_INDICATORS_VERSIONS indver
set CONCEPT_DESCRIPTION_FK = null
where indver.CONCEPT_DESCRIPTION_FK = (select ins.ID
                            from TB_INTERNATIONAL_STRINGS ins
                            where ins.ID NOT IN (select loc.INTERNATIONAL_STRING_FK
                                                from TB_LOCALISED_STRINGS loc));


-- TB_INDICATORS_VERSIONS.COMMENT_FK
-- ..................................................................................................................................

delete from TB_LOCALISED_STRINGS loc
where loc.ID = (select loc.ID as LOCALISED_STRING_ID
                from TB_INDICATORS_VERSIONS ind left join TB_INTERNATIONAL_STRINGS ins on ins.ID = ind.COMMENTS_FK left join TB_LOCALISED_STRINGS loc on loc.INTERNATIONAL_STRING_FK = ins.ID
                where loc.LABEL like 'null');

update TB_INDICATORS_VERSIONS indver
set COMMENTS_FK = null
where indver.COMMENTS_FK = (select ins.ID
                            from TB_INTERNATIONAL_STRINGS ins
                            where ins.ID NOT IN (select loc.INTERNATIONAL_STRING_FK
                                                from TB_LOCALISED_STRINGS loc));


-- TB_INDICATORS_VERSIONS.NOTES_FK
-- ..................................................................................................................................

delete from TB_LOCALISED_STRINGS loc
where loc.ID = (select loc.ID as LOCALISED_STRING_ID
                from TB_INDICATORS_VERSIONS ind left join TB_INTERNATIONAL_STRINGS ins on ins.ID = ind.NOTES_FK left join TB_LOCALISED_STRINGS loc on loc.INTERNATIONAL_STRING_FK = ins.ID
                where loc.LABEL like 'null');

update TB_INDICATORS_VERSIONS indver
set NOTES_FK = null
where indver.NOTES_FK = (select ins.ID
                            from TB_INTERNATIONAL_STRINGS ins
                            where ins.ID NOT IN (select loc.INTERNATIONAL_STRING_FK
                                                from TB_LOCALISED_STRINGS loc));


-- TB_LIS_UNITS_MULTIPLIERS.TITLE_FK
-- ..................................................................................................................................

delete from TB_LOCALISED_STRINGS loc
where loc.ID = (select loc.ID as LOCALISED_STRING_ID
                from TB_LIS_UNITS_MULTIPLIERS ind left join TB_INTERNATIONAL_STRINGS ins on ins.ID = ind.TITLE_FK left join TB_LOCALISED_STRINGS loc on loc.INTERNATIONAL_STRING_FK = ins.ID
                where loc.LABEL like 'null');

update TB_LIS_UNITS_MULTIPLIERS indver
set TITLE_FK = null
where indver.TITLE_FK = (select ins.ID
                            from TB_INTERNATIONAL_STRINGS ins
                            where ins.ID NOT IN (select loc.INTERNATIONAL_STRING_FK
                                                from TB_LOCALISED_STRINGS loc));


-- TB_LIS_QUANTITIES_UNITS.TITLE_FK
-- ..................................................................................................................................

delete from TB_LOCALISED_STRINGS loc
where loc.ID = (select loc.ID as LOCALISED_STRING_ID
                from TB_LIS_QUANTITIES_UNITS ind left join TB_INTERNATIONAL_STRINGS ins on ins.ID = ind.TITLE_FK left join TB_LOCALISED_STRINGS loc on loc.INTERNATIONAL_STRING_FK = ins.ID
                where loc.LABEL like 'null');

update TB_LIS_QUANTITIES_UNITS indver
set TITLE_FK = null
where indver.TITLE_FK = (select ins.ID
                            from TB_INTERNATIONAL_STRINGS ins
                            where ins.ID NOT IN (select loc.INTERNATIONAL_STRING_FK
                                                from TB_LOCALISED_STRINGS loc));


-- TB_LIS_GEOGR_GRANULARITIES.TITLE_FK
-- ..................................................................................................................................

delete from TB_LOCALISED_STRINGS loc
where loc.ID = (select loc.ID as LOCALISED_STRING_ID
                from TB_LIS_GEOGR_GRANULARITIES ind left join TB_INTERNATIONAL_STRINGS ins on ins.ID = ind.TITLE_FK left join TB_LOCALISED_STRINGS loc on loc.INTERNATIONAL_STRING_FK = ins.ID
                where loc.LABEL like 'null');

update TB_LIS_GEOGR_GRANULARITIES indver
set TITLE_FK = null
where indver.TITLE_FK = (select ins.ID
                            from TB_INTERNATIONAL_STRINGS ins
                            where ins.ID NOT IN (select loc.INTERNATIONAL_STRING_FK
                                                from TB_LOCALISED_STRINGS loc));


-- TB_LIS_GEOGR_VALUES.TITLE_FK
-- ..................................................................................................................................

delete from TB_LOCALISED_STRINGS loc
where loc.ID = (select loc.ID as LOCALISED_STRING_ID
                from TB_LIS_GEOGR_VALUES ind left join TB_INTERNATIONAL_STRINGS ins on ins.ID = ind.TITLE_FK left join TB_LOCALISED_STRINGS loc on loc.INTERNATIONAL_STRING_FK = ins.ID
                where loc.LABEL like 'null');

update TB_LIS_GEOGR_VALUES indver
set TITLE_FK = null
where indver.TITLE_FK = (select ins.ID
                            from TB_INTERNATIONAL_STRINGS ins
                            where ins.ID NOT IN (select loc.INTERNATIONAL_STRING_FK
                                                from TB_LOCALISED_STRINGS loc));

-- TB_LIS_GEOGR_VALUES.TITLE_FK
-- ..................................................................................................................................

delete from TB_LOCALISED_STRINGS loc
where loc.ID = (select loc.ID as LOCALISED_STRING_ID
                from TB_LIS_GEOGR_VALUES ind left join TB_INTERNATIONAL_STRINGS ins on ins.ID = ind.TITLE_FK left join TB_LOCALISED_STRINGS loc on loc.INTERNATIONAL_STRING_FK = ins.ID
                where loc.LABEL like 'null');

update TB_LIS_GEOGR_VALUES indver
set TITLE_FK = null
where indver.TITLE_FK = (select ins.ID
                            from TB_INTERNATIONAL_STRINGS ins
                            where ins.ID NOT IN (select loc.INTERNATIONAL_STRING_FK
                                                from TB_LOCALISED_STRINGS loc));


-- TB_QUANTITIES.PERCENTAGE_OF_FK
-- ..................................................................................................................................

delete from TB_LOCALISED_STRINGS loc
where loc.ID = (select loc.ID as LOCALISED_STRING_ID
                from TB_QUANTITIES ind left join TB_INTERNATIONAL_STRINGS ins on ins.ID = ind.PERCENTAGE_OF_FK left join TB_LOCALISED_STRINGS loc on loc.INTERNATIONAL_STRING_FK = ins.ID
                where loc.LABEL like 'null');

update TB_QUANTITIES indver
set PERCENTAGE_OF_FK = null
where indver.PERCENTAGE_OF_FK = (select ins.ID
                            from TB_INTERNATIONAL_STRINGS ins
                            where ins.ID NOT IN (select loc.INTERNATIONAL_STRING_FK
                                                from TB_LOCALISED_STRINGS loc));


-- TB_DATA_SOURCES.SOURCE_SURVEY_TITLE_FK
-- ..................................................................................................................................

delete from TB_LOCALISED_STRINGS loc
where loc.ID = (select loc.ID as LOCALISED_STRING_ID
                from TB_DATA_SOURCES ind left join TB_INTERNATIONAL_STRINGS ins on ins.ID = ind.SOURCE_SURVEY_TITLE_FK left join TB_LOCALISED_STRINGS loc on loc.INTERNATIONAL_STRING_FK = ins.ID
                where loc.LABEL like 'null');

update TB_DATA_SOURCES indver
set SOURCE_SURVEY_TITLE_FK = null
where indver.SOURCE_SURVEY_TITLE_FK = (select ins.ID
                            from TB_INTERNATIONAL_STRINGS ins
                            where ins.ID NOT IN (select loc.INTERNATIONAL_STRING_FK
                                                from TB_LOCALISED_STRINGS loc));

-- TB_DATA_SOURCES.SOURCE_SURVEY_ACRONYM_FK
-- ..................................................................................................................................

delete from TB_LOCALISED_STRINGS loc
where loc.ID = (select loc.ID as LOCALISED_STRING_ID
                from TB_DATA_SOURCES ind left join TB_INTERNATIONAL_STRINGS ins on ins.ID = ind.SOURCE_SURVEY_ACRONYM_FK left join TB_LOCALISED_STRINGS loc on loc.INTERNATIONAL_STRING_FK = ins.ID
                where loc.LABEL like 'null');

update TB_DATA_SOURCES indver
set SOURCE_SURVEY_ACRONYM_FK = null
where indver.SOURCE_SURVEY_ACRONYM_FK = (select ins.ID
                            from TB_INTERNATIONAL_STRINGS ins
                            where ins.ID NOT IN (select loc.INTERNATIONAL_STRING_FK
                                                from TB_LOCALISED_STRINGS loc));

-- TB_INDICATORS_INSTANCES.TITLE_FK
-- ..................................................................................................................................

delete from TB_LOCALISED_STRINGS loc
where loc.ID = (select loc.ID as LOCALISED_STRING_ID
                from TB_INDICATORS_INSTANCES ind left join TB_INTERNATIONAL_STRINGS ins on ins.ID = ind.TITLE_FK left join TB_LOCALISED_STRINGS loc on loc.INTERNATIONAL_STRING_FK = ins.ID
                where loc.LABEL like 'null');

update TB_INDICATORS_INSTANCES indver
set TITLE_FK = null
where indver.TITLE_FK = (select ins.ID
                            from TB_INTERNATIONAL_STRINGS ins
                            where ins.ID NOT IN (select loc.INTERNATIONAL_STRING_FK
                                                from TB_LOCALISED_STRINGS loc));



-- TB_DIMENSIONS.TITLE_FK
-- ..................................................................................................................................

delete from TB_LOCALISED_STRINGS loc
where loc.ID = (select loc.ID as LOCALISED_STRING_ID
                from TB_DIMENSIONS ind left join TB_INTERNATIONAL_STRINGS ins on ins.ID = ind.TITLE_FK left join TB_LOCALISED_STRINGS loc on loc.INTERNATIONAL_STRING_FK = ins.ID
                where loc.LABEL like 'null');

update TB_DIMENSIONS indver
set TITLE_FK = null
where indver.TITLE_FK = (select ins.ID
                            from TB_INTERNATIONAL_STRINGS ins
                            where ins.ID NOT IN (select loc.INTERNATIONAL_STRING_FK
                                                from TB_LOCALISED_STRINGS loc));

-- TB_TRANSLATIONS.TITLE_FK
-- ..................................................................................................................................

delete from TB_LOCALISED_STRINGS loc
where loc.ID = (select loc.ID as LOCALISED_STRING_ID
                from TB_TRANSLATIONS ind left join TB_INTERNATIONAL_STRINGS ins on ins.ID = ind.TITLE_FK left join TB_LOCALISED_STRINGS loc on loc.INTERNATIONAL_STRING_FK = ins.ID
                where loc.LABEL like 'null');

update TB_TRANSLATIONS indver
set TITLE_FK = null
where indver.TITLE_FK = (select ins.ID
                            from TB_INTERNATIONAL_STRINGS ins
                            where ins.ID NOT IN (select loc.INTERNATIONAL_STRING_FK
                                                from TB_LOCALISED_STRINGS loc));


-- TB_TRANSLATIONS.TITLE_SUMMARY_FK
-- ..................................................................................................................................

delete from TB_LOCALISED_STRINGS loc
where loc.ID = (select loc.ID as LOCALISED_STRING_ID
                from TB_TRANSLATIONS ind left join TB_INTERNATIONAL_STRINGS ins on ins.ID = ind.TITLE_SUMMARY_FK left join TB_LOCALISED_STRINGS loc on loc.INTERNATIONAL_STRING_FK = ins.ID
                where loc.LABEL like 'null');

update TB_TRANSLATIONS indver
set TITLE_SUMMARY_FK = null
where indver.TITLE_SUMMARY_FK = (select ins.ID
                            from TB_INTERNATIONAL_STRINGS ins
                            where ins.ID NOT IN (select loc.INTERNATIONAL_STRING_FK
                                                from TB_LOCALISED_STRINGS loc));


-- TB_EXTERNAL_ITEMS.TITLE_FK
-- ..................................................................................................................................

delete from TB_LOCALISED_STRINGS loc
where loc.ID = (select loc.ID as LOCALISED_STRING_ID
                from TB_EXTERNAL_ITEMS ind left join TB_INTERNATIONAL_STRINGS ins on ins.ID = ind.TITLE_FK left join TB_LOCALISED_STRINGS loc on loc.INTERNATIONAL_STRING_FK = ins.ID
                where loc.LABEL like 'null');

update TB_EXTERNAL_ITEMS indver
set TITLE_FK = null
where indver.TITLE_FK = (select ins.ID
                            from TB_INTERNATIONAL_STRINGS ins
                            where ins.ID NOT IN (select loc.INTERNATIONAL_STRING_FK
                                                from TB_LOCALISED_STRINGS loc));



-- Delete empty TB_INTERNATIONAL_STRINGS
-- ..................................................................................................................................

delete from TB_INTERNATIONAL_STRINGS ins
where ins.ID not IN (select loc.INTERNATIONAL_STRING_FK
                      from TB_LOCALISED_STRINGS loc);


commit;

