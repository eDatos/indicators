ALTER TABLE TBL_LOCALISED_STRINGS ADD CONSTRAINT LOCALE_INTERNATIONAL UNIQUE(LOCALE, INTERNATIONAL_STRING_FK) DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE TBL_INDICATORS_SYSTEMS ADD CONSTRAINT INDICATORS_SYSTEMS_CODE UNIQUE(CODE);