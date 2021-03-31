CREATE ROLE INDICATORS_DATA_ROLE_TEST;

CREATE TABLE TB_DATA_CONFIGURATIONS (
  ID BIGINT NOT NULL,
  CONF_KEY VARCHAR(255) NOT NULL,
  CONF_VALUE VARCHAR(4000),
  SYSTEM_PROPERTY BOOLEAN NOT NULL,
  EXTERNALLY_PUBLISHED BOOLEAN NOT NULL,
  UPDATE_DATE_TZ VARCHAR(50),
  UPDATE_DATE TIMESTAMP,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR(50),
  VERSION BIGINT NOT NULL
);

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(1,1,true,'indicators.jaxi.local.url','http://URL_JAXI',false);
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(2,1,false,'indicators.dspl.provider.name','Instituto Canario de Estadística',false);
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(3,1,false,'indicators.dspl.provider.description','El Instituto Canario de Estadística (ISTAC), es el órgano central del sistema estadístico autonómico y centro oficial de investigación del Gobierno de Canarias, creado y regulado por la Ley 1/1991, de 28 de enero, de Estadística de la Comunidad Autónoma de Canarias (CAC)',false);
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(4,1,false,'indicators.dspl.provider.url','http://www.gobiernodecanarias.org/istac/',false);
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(5,1,true,'indicators.dspl.indicators.system.url','http://BASE_URL/[SYSTEM]',false);
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(6,1,true,'indicators.subjects.db.table','TV_AREAS_TEMATICAS',false);
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(7,1,true,'indicators.subjects.db.column_code','ID_AREA_TEMATICA',false);
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(8,1,true,'indicators.subjects.db.column_title','DESCRIPCION',false);
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(9,1,true,'indicators.bbbd.data_views_role','INDICATORS_DATA_ROLE_TEST',false);
insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE,EXTERNALLY_PUBLISHED) values(9,1,false,'metamac.edition.languages','es,en,pt',false);

Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION,EXTERNALLY_PUBLISHED) values ('10','metamac.statistical_operations.rest.internal','http://localhost:8080/metamac-statistical-operations-web/apis/operations-internal',true,null,null,null,null,null,null,null,null,'1',false);
Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION,EXTERNALLY_PUBLISHED) values ('11','metamac.statistical_operations.rest.external','http://localhost:8080/metamac-statistical-operations-web/apis/operations-external',true,null,null,null,null,null,null,null,null,'1',false);
Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION,EXTERNALLY_PUBLISHED) values ('12','metamac.notices.rest.internal','http://localhost:8080/metamac-notifications-web/apis',true,null,null,null,null,null,null,null,null,'1',false);
Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION,EXTERNALLY_PUBLISHED) values ('13','indicators.web.internal.url','http://localhost:8080/indicators',true,null,null,null,null,null,null,null,null,'1',false);

Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION,EXTERNALLY_PUBLISHED) values ('14','indicators.jaxi.local.url','http://localhost',true,null,null,null,null,null,null,null,null,'1',false);
Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION,EXTERNALLY_PUBLISHED) values ('15','indicators.jaxi.local.url.indicator','http://localhost/tabla.do?indicador=INDICATOR',true,null,null,null,null,null,null,null,null,'1',false);
Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION,EXTERNALLY_PUBLISHED) values ('16','indicators.jaxi.local.url.instance','DESCRIPCION',true,null,null,null,null,null,null,null,null,'1',false);
Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION,EXTERNALLY_PUBLISHED) values ('17','indicators.jaxi.remote.url','http://localhost',true,null,null,null,null,null,null,null,null,'1',false);
Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION,EXTERNALLY_PUBLISHED) values ('18','indicators.jaxi.remote.url.indicator','http://localhost/tabla.do?indicador=INDICATOR',true,null,null,null,null,null,null,null,null,'1',false);

Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION,EXTERNALLY_PUBLISHED) values ('19','indicators.rest.external','http://localhost',true,null,null,null,null,null,null,null,null,'1',false);
Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION,EXTERNALLY_PUBLISHED) values ('20','indicators.rest.internal','http://localhost',true,null,null,null,null,null,null,null,null,'1',false);
Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION,EXTERNALLY_PUBLISHED) values ('21','idxmanager.search.form.url','http://localhost',true,null,null,null,null,null,null,null,null,'1',false);

Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION,EXTERNALLY_PUBLISHED) values ('27','org.quartz.scheduler.instanceName','IndicatorsScheduler',true,null,null,null,null,null,null,null,null,'1',false);
Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION,EXTERNALLY_PUBLISHED) values ('28','indicators.update.quartz.expression','0 0 23 * * ?',true,'Europe/Paris',to_timestamp('16/09/16 13:22:02,171000000','DD/MM/RR HH24:MI:SSXFF'),'Europe/Paris',to_timestamp('16/09/16 13:19:52,409000000','DD/MM/RR HH24:MI:SSXFF'),'admin','Europe/Paris',to_timestamp('16/09/16 13:22:02,172000000','DD/MM/RR HH24:MI:SSXFF'),'admin','3',false);
Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION,EXTERNALLY_PUBLISHED) values ('29','org.quartz.scheduler.instanceId','AUTO',true,null,null,null,null,null,null,null,null,'1',false);
Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION,EXTERNALLY_PUBLISHED) values ('30','org.quartz.scheduler.rmi.export','false',true,null,null,null,null,null,null,null,null,'1',false);
Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION,EXTERNALLY_PUBLISHED) values ('31','org.quartz.scheduler.rmi.proxy','false',true,null,null,null,null,null,null,null,null,'1',false);
Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION,EXTERNALLY_PUBLISHED) values ('32','org.quartz.threadPool.class','org.quartz.simpl.SimpleThreadPool',true,null,null,null,null,null,null,null,null,'1',false);
Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION,EXTERNALLY_PUBLISHED) values ('33','org.quartz.threadPool.threadCount','25',true,null,null,null,null,null,null,null,null,'1',false);
Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION,EXTERNALLY_PUBLISHED) values ('34','org.quartz.threadPool.threadPriority','5',true,null,null,null,null,null,null,null,null,'1',false);
Insert into TB_DATA_CONFIGURATIONS (ID,CONF_KEY,CONF_VALUE,SYSTEM_PROPERTY,UPDATE_DATE_TZ,UPDATE_DATE,CREATED_DATE_TZ,CREATED_DATE,CREATED_BY,LAST_UPDATED_TZ,LAST_UPDATED,LAST_UPDATED_BY,VERSION,EXTERNALLY_PUBLISHED) values ('35','org.quartz.jobStore.class','org.quartz.simpl.RAMJobStore',true,null,null,null,null,null,null,null,null,'1',false);