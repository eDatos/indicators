CREATE ROLE INDICATORS_DATA_ROLE_TEST;

CREATE TABLE TB_DATA_CONFIGURATIONS (
  ID NUMBER(19) NOT NULL,
  CONF_KEY VARCHAR2(255 CHAR) NOT NULL,
  CONF_VALUE VARCHAR2(4000 CHAR),
  SYSTEM_PROPERTY NUMBER(1,0) NOT NULL,
  UPDATE_DATE_TZ VARCHAR2(50 CHAR),
  UPDATE_DATE TIMESTAMP ,
  CREATED_DATE_TZ VARCHAR2(50 CHAR),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR2(50 CHAR),
  LAST_UPDATED_TZ VARCHAR2(50 CHAR),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR2(50 CHAR),
  VERSION NUMBER(19) NOT NULL
);

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(1,1,1,'indicators.jaxi.local.url','http://URL_JAXI');
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(2,1,0,'indicators.dspl.provider.name','Instituto Canario de Estadística');
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(3,1,0,'indicators.dspl.provider.description','El Instituto Canario de Estadística (ISTAC), es el órgano central del sistema estadístico autonómico y centro oficial de investigación del Gobierno de Canarias, creado y regulado por la Ley 1/1991, de 28 de enero, de Estadística de la Comunidad Autónoma de Canarias (CAC)');
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(4,1,0,'indicators.dspl.provider.url','http://www.gobiernodecanarias.org/istac/');
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(5,1,1,'indicators.dspl.indicators.system.url','http://BASE_URL/[SYSTEM]');
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(6,1,1,'indicators.subjects.db.table','TV_AREAS_TEMATICAS');
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(7,1,1,'indicators.subjects.db.column_code','ID_AREA_TEMATICA');
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(8,1,1,'indicators.subjects.db.column_title','DESCRIPCION');
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(9,1,1,'indicators.bbbd.data_views_role','INDICATORS_DATA_ROLE_TEST');
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(9,1,0,'metamac.edition.languages','es,en,pt');

Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION) values ('10','metamac.statistical_operations.rest.internal','http://localhost:8080/metamac-statistical-operations-web/apis/operations-internal','1',null,null,null,null,null,null,null,null,'1');
Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION) values ('11','metamac.statistical_operations.rest.external','http://localhost:8080/metamac-statistical-operations-web/apis/operations-external','1',null,null,null,null,null,null,null,null,'1');
Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION) values ('12','metamac.notices.rest.internal','http://localhost:8080/metamac-notifications-web/apis','1',null,null,null,null,null,null,null,null,'1');
Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION) values ('13','indicators.web.internal.url','http://localhost:8080/indicators','1',null,null,null,null,null,null,null,null,'1');

Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION) values ('14','indicators.jaxi.local.url','http://localhost','1',null,null,null,null,null,null,null,null,'1');
Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION) values ('15','indicators.jaxi.local.url.indicator','http://localhost/tabla.do?indicador=INDICATOR','1',null,null,null,null,null,null,null,null,'1');
Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION) values ('16','indicators.jaxi.local.url.instance','DESCRIPCION','1',null,null,null,null,null,null,null,null,'1');
Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION) values ('17','indicators.jaxi.remote.url','http://localhost','1',null,null,null,null,null,null,null,null,'1');
Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION) values ('18','indicators.jaxi.remote.url.indicator','http://localhost/tabla.do?indicador=INDICATOR','1',null,null,null,null,null,null,null,null,'1');

Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION) values ('19','indicators.rest.external','http://localhost','1',null,null,null,null,null,null,null,null,'1');
Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION) values ('20','indicators.rest.internal','http://localhost','1',null,null,null,null,null,null,null,null,'1');
Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION) values ('21','idxmanager.search.form.url','http://localhost','1',null,null,null,null,null,null,null,null,'1');



commit;