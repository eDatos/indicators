CREATE TABLE TB_DATA_CONFIGURATIONS (
  CONF_KEY   VARCHAR2(255 CHAR) NOT NULL PRIMARY KEY,
  CONF_VALUE VARCHAR2(4000 CHAR)
);


insert into TB_DATA_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values('indicators.jaxi.local.url','http://URL_JAXI');
insert into TB_DATA_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values('indicators.dspl.provider.name','Instituto Canario de Estadística');
insert into TB_DATA_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values('indicators.dspl.provider.description','El Instituto Canario de Estadística (ISTAC), es el órgano central del sistema estadístico autonómico y centro oficial de investigación del Gobierno de Canarias, creado y regulado por la Ley 1/1991, de 28 de enero, de Estadística de la Comunidad Autónoma de Canarias (CAC)');
insert into TB_DATA_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values('indicators.dspl.provider.url','http://www.gobiernodecanarias.org/istac/');
insert into TB_DATA_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values('indicators.dspl.indicators.system.url','http://BASE_URL/[SYSTEM]');
insert into TB_DATA_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values('indicators.subjects.db.table','TV_AREAS_TEMATICAS');
insert into TB_DATA_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values('indicators.subjects.db.column_code','ID_AREA_TEMATICA');
insert into TB_DATA_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values('indicators.subjects.db.column_title','DESCRIPCION');
insert into TB_DATA_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values('indicators.bbbd.data_views_role','INDICATORS_DATA_ROLE_TEST');
    