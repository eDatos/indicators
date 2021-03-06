
update tb_indicators_versions
set needs_update = 1;

ALTER TABLE TB_IND_VERSION_GEO_COV DROP CONSTRAINT FK_TB_IND_VERSION_GEO_COV_IN03;

ALTER TABLE TB_IND_VERSION_GEO_COV ADD CONSTRAINT FK_TB_IND_VERSION_GEO_COV_IN03
	FOREIGN KEY (INDICATOR_VERSION_FK) REFERENCES TB_INDICATORS_VERSIONS (ID) ON DELETE CASCADE;
	
	
	
ALTER TABLE TB_IND_VERSION_TIME_COV DROP CONSTRAINT FK_TB_IND_VERSION_TIME_COV_I71;

ALTER TABLE TB_IND_VERSION_TIME_COV ADD CONSTRAINT FK_TB_IND_VERSION_TIME_COV_I71
	FOREIGN KEY (INDICATOR_VERSION_FK) REFERENCES TB_INDICATORS_VERSIONS (ID) ON DELETE CASCADE;



ALTER TABLE TB_IND_VERSION_MEAS_COV DROP CONSTRAINT FK_TB_IND_VERSION_MEAS_COV_I46;
ALTER TABLE TB_IND_VERSION_MEAS_COV ADD CONSTRAINT FK_TB_IND_VERSION_MEAS_COV_I46
	FOREIGN KEY (INDICATOR_VERSION_FK) REFERENCES TB_INDICATORS_VERSIONS (ID) ON DELETE CASCADE;

commit;