-- PENDIENTE ISTAC Lista de valores definitivos

-- GEOGRAPHIC GRANULARITIES

-- Countries
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Countries', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Países', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LIS_GEOGR_GRANULARITIES (ID, CODE, UUID, TITLE_FK) values (SEQ_GEOGR_GRANULARITIES.nextval, 'COUNTRIES', '1', SEQ_I18NSTRS.currval);

	-- Country Geographical Values
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, 'ESPAÑA', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, 'ESPAÑA', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES', 20.0656233, -25.454564645, '1', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);

-- Regions
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Regions', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Comunidades autónomas', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LIS_GEOGR_GRANULARITIES (ID, CODE, UUID, TITLE_FK) values (SEQ_GEOGR_GRANULARITIES.nextval, 'REGIONS', '2', SEQ_I18NSTRS.currval);


	-- Regions Geographicla Values
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' ANDALUCÍA', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' ANDALUCÍA', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES61', 20.0656233, -25.454564645, '2', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' ARAGÓN', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' ARAGÓN', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES24', 20.0656233, -25.454564645, '11', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' ASTURIAS (PRINCIPADO DE)', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' ASTURIAS (PRINCIPADO DE)', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES12', 20.0656233, -25.454564645, '15', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' BALEARS (ILLES)', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' BALEARS (ILLES)', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES53', 20.0656233, -25.454564645, '16', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' CANARIAS', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' CANARIAS', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES70', 20.0656233, -25.454564645, '17', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' CANTABRIA', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' CANTABRIA', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES13', 20.0656233, -25.454564645, '20', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' CASTILLA Y LEÓN', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' CASTILLA Y LEÓN', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES41', 20.0656233, -25.454564645, '21', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' CASTILLA-LA MANCHA', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' CASTILLA-LA MANCHA', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES42', 20.0656233, -25.454564645, '31', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' CATALUÑA', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' CATALUÑA', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES51', 20.0656233, -25.454564645, '37', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' COMUNITAT VALENCIANA', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' COMUNITAT VALENCIANA', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES52', 20.0656233, -25.454564645, '42', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' EXTREMADURA', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' EXTREMADURA', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES43', 20.0656233, -25.454564645, '46', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' GALICIA', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' GALICIA', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES11', 20.0656233, -25.454564645, '49', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' MADRID (COMUNIDAD DE)', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' MADRID (COMUNIDAD DE)', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES30', 20.0656233, -25.454564645, '54', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' MURCIA (REGIÓN DE)', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' MURCIA (REGIÓN DE)', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES62', 20.0656233, -25.454564645, '55', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' NAVARRA (COMUNIDAD FORAL DE)', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' NAVARRA (COMUNIDAD FORAL DE)', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES22', 20.0656233, -25.454564645, '56', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' PAÍS VASCO', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' PAÍS VASCO', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES21', 20.0656233, -25.454564645, '57', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' RIOJA (LA)', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' RIOJA (LA)', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES23', 20.0656233, -25.454564645, '61', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' CEUTA', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' CEUTA', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES63', 20.0656233, -25.454564645, '62', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' MELILLA', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, ' MELILLA', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES64', 20.0656233, -25.454564645, '63', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);


-- Provinces
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Provinces', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Provincias', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LIS_GEOGR_GRANULARITIES (ID, CODE, UUID, TITLE_FK) values (SEQ_GEOGR_GRANULARITIES.nextval, 'PROVINCES', '3', SEQ_I18NSTRS.currval);


	-- Province Geographical values
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Almería', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Almería', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES611', 20.0656233, -25.454564645, '3', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Cádiz', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Cádiz', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES612', 20.0656233, -25.454564645, '4', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Córdoba', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Córdoba', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES613', 20.0656233, -25.454564645, '5', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Granada', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Granada', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES614', 20.0656233, -25.454564645, '6', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Huelva', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Huelva', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES615', 20.0656233, -25.454564645, '7', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Jaén', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Jaén', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES616', 20.0656233, -25.454564645, '8', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Málaga', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Málaga', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES617', 20.0656233, -25.454564645, '9', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Sevilla', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Sevilla', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES618', 20.0656233, -25.454564645, '10', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Huesca', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Huesca', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES241', 20.0656233, -25.454564645, '12', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Teruel', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Teruel', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES242', 20.0656233, -25.454564645, '13', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Zaragoza', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Zaragoza', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES243', 20.0656233, -25.454564645, '14', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Palmas (Las)', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Palmas (Las)', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES701', 20.0656233, -25.454564645, '18', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Santa Cruz de Tenerife', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Santa Cruz de Tenerife', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES702', 20.0656233, -25.454564645, '19', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Ávila', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Ávila', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES411', 20.0656233, -25.454564645, '22', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Burgos', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Burgos', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES412', 20.0656233, -25.454564645, '23', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  León', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  León', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES413', 20.0656233, -25.454564645, '24', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Palencia', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Palencia', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES414', 20.0656233, -25.454564645, '25', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Salamanca', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Salamanca', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES415', 20.0656233, -25.454564645, '26', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Segovia', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Segovia', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES416', 20.0656233, -25.454564645, '27', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Soria', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Soria', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES417', 20.0656233, -25.454564645, '28', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Valladolid', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Valladolid', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES418', 20.0656233, -25.454564645, '29', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Zamora', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Zamora', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES419', 20.0656233, -25.454564645, '30', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Albacete', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Albacete', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES421', 20.0656233, -25.454564645, '32', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Ciudad Real', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Ciudad Real', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES422', 20.0656233, -25.454564645, '33', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Cuenca', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Cuenca', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES423', 20.0656233, -25.454564645, '34', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Guadalajara', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Guadalajara', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES424', 20.0656233, -25.454564645, '35', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Toledo', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Toledo', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES425', 20.0656233, -25.454564645, '36', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Barcelona', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Barcelona', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES511', 20.0656233, -25.454564645, '38', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Girona', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Girona', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES512', 20.0656233, -25.454564645, '39', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Lleida', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Lleida', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES513', 20.0656233, -25.454564645, '40', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Tarragona', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Tarragona', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES514', 20.0656233, -25.454564645, '41', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Alicante/Alacant', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Alicante/Alacant', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES521', 20.0656233, -25.454564645, '43', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Castellón/Castelló', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Castellón/Castelló', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES522', 20.0656233, -25.454564645, '44', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Valencia/València', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Valencia/València', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES523', 20.0656233, -25.454564645, '45', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Badajoz', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Badajoz', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES431', 20.0656233, -25.454564645, '47', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Cáceres', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Cáceres', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES432', 20.0656233, -25.454564645, '48', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Coruña (A)', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Coruña (A)', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES111', 20.0656233, -25.454564645, '50', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Lugo', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Lugo', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES112', 20.0656233, -25.454564645, '51', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Ourense', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Ourense', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES113', 20.0656233, -25.454564645, '52', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Pontevedra', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Pontevedra', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES114', 20.0656233, -25.454564645, '53', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Álava', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Álava', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES211', 20.0656233, -25.454564645, '58', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Guipúzcoa', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Guipúzcoa', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES212', 20.0656233, -25.454564645, '59', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);
	
	
	INSERT INTO Tb_International_Strings (Id, Version) values (Seq_I18nstrs.Nextval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Vizcaya', 'en', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Localised_Strings (Id, Label, Locale, International_String_Fk, Version) values (Seq_L10nstrs.Nextval, '  Vizcaya', 'es', Seq_I18nstrs.Currval, 1);
	INSERT INTO Tb_Lis_Geogr_Values (Id, Code, Latitude, Longitude, Uuid, Title_Fk, Granularity_Fk) values (Seq_Geogr_Values.Nextval, 'ES213', 20.0656233, -25.454564645, '60', SEQ_I18NSTRS.currval, SEQ_GEOGR_GRANULARITIES.currval);


-- Municipalities
INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (SEQ_I18NSTRS.nextval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Municipalities', 'en', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, INTERNATIONAL_STRING_FK, VERSION) values (SEQ_L10NSTRS.nextval, 'Municipios', 'es', SEQ_I18NSTRS.currval, 1);
INSERT INTO TB_LIS_GEOGR_GRANULARITIES (ID, CODE, UUID, TITLE_FK) values (SEQ_GEOGR_GRANULARITIES.nextval, 'MUNICIPALITIES', '4', SEQ_I18NSTRS.currval);
