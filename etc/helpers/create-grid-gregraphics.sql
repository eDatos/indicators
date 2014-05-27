DECLARE 
  seq varchar(10);
BEGIN

-- Countries
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Grids', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Mallas', 'es', SEQ_I18NSTRS.currval, 1);
-- Replace with valid uuid
INSERT INTO TB_LIS_GEOGR_GRANULARITIES (VERSION, ID, CODE, UUID, TITLE_FK) values (0, SEQ_GEOGR_GRANULARITIES.nextval, 'GRIDS', '6', SEQ_I18NSTRS.currval);

FOR x IN 1..125000 LOOP
    seq := lpad(x,10,0);
    INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (Seq_I18nstrs.Nextval, 1);
    INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.NEXTVAL, 'Grid '|| seq, 'en', Seq_I18nstrs.Currval, 1);
    INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.NEXTVAL, 'Malla ' || seq, 'es', Seq_I18nstrs.Currval, 1);
    INSERT INTO TB_LIS_GEOGR_VALUES (VERSION, ID, CODE, LATITUDE, LONGITUDE, GLOBAL_ORDER, UUID, TITLE_FK, GRANULARITY_FK) values (0, SEQ_GEOGR_VALUES.NEXTVAL, 'GRID_' || seq, 20.0656233, -25.454564645, 'GRID_'||seq, seq, SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
  
END LOOP;
END;
/