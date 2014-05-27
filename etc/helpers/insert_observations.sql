
declare
  geo_code varchar(255);
  time_code varchar(255);
  val varchar(255);
begin
  for geo_seq in 1..100 loop
    geo_code := 'GRID_'||lpad(geo_seq,10,0);
    for time_year in 1980..2030 loop
      for time_month in 1..12 loop
        time_code := to_char(time_year) || 'M' ||lpad(to_char(time_month),2,0);
        val := trunc(DBMS_RANDOM.VALUE(0,5000),3);
        Insert into DATA_WD6ESHSAKLSNINYLJO8ZZX2 (ID,DIMENSION_00,DIMENSION_01,DIMENSION_02,PRIMARY_MEASURE,ATTRIBUTE_0,ATTRIBUTE_0_ES,ATTRIBUTE_1,ATTRIBUTE_1_ES) values (SEQ_WD6ESHSAKLSNINYLJO8ZZX2.nextval,geo_code,time_code,'ABSOLUTE',val,null,null,'CODE','raw_code');
                val := trunc(DBMS_RANDOM.VALUE(0,5000),3);
        Insert into DATA_WD6ESHSAKLSNINYLJO8ZZX2 (ID,DIMENSION_00,DIMENSION_01,DIMENSION_02,PRIMARY_MEASURE,ATTRIBUTE_0,ATTRIBUTE_0_ES,ATTRIBUTE_1,ATTRIBUTE_1_ES) values (SEQ_WD6ESHSAKLSNINYLJO8ZZX2.nextval,geo_code,time_code,'ANNUAL_PERCENTAGE_RATE',val,null,null,'CODE','raw_code');
                val := trunc(DBMS_RANDOM.VALUE(0,5000),3);
        Insert into DATA_WD6ESHSAKLSNINYLJO8ZZX2 (ID,DIMENSION_00,DIMENSION_01,DIMENSION_02,PRIMARY_MEASURE,ATTRIBUTE_0,ATTRIBUTE_0_ES,ATTRIBUTE_1,ATTRIBUTE_1_ES) values (SEQ_WD6ESHSAKLSNINYLJO8ZZX2.nextval,geo_code,time_code,'INTERPERIOD_PERCENTAGE_RATE',val,null,null,'CODE','raw_code');
                val := trunc(DBMS_RANDOM.VALUE(0,5000),3);
        Insert into DATA_WD6ESHSAKLSNINYLJO8ZZX2 (ID,DIMENSION_00,DIMENSION_01,DIMENSION_02,PRIMARY_MEASURE,ATTRIBUTE_0,ATTRIBUTE_0_ES,ATTRIBUTE_1,ATTRIBUTE_1_ES) values (SEQ_WD6ESHSAKLSNINYLJO8ZZX2.nextval,geo_code,time_code,'ANNUAL_PUNTUAL_RATE',val,null,null,'CODE','raw_code');
                        val := trunc(DBMS_RANDOM.VALUE(0,5000),3);
        Insert into DATA_WD6ESHSAKLSNINYLJO8ZZX2 (ID,DIMENSION_00,DIMENSION_01,DIMENSION_02,PRIMARY_MEASURE,ATTRIBUTE_0,ATTRIBUTE_0_ES,ATTRIBUTE_1,ATTRIBUTE_1_ES) values (SEQ_WD6ESHSAKLSNINYLJO8ZZX2.nextval,geo_code,time_code,'INTERPERIOD_PUNTUAL_RATE',val,null,null,'CODE','raw_code');
      end loop;
    end loop;
  end loop;
end;
/