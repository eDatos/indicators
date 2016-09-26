create or replace 
PACKAGE                     REPOSITORY AUTHID CURRENT_USER
 AS
/******************************************************************************
   NAME:       REPOSITORY
   PURPOSE:

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        26/08/2013      Arte       1. Created this package
   1.1        09/09/2013      Arte       2. Add attribute definitions
   1.2        09/09/2013      Arte       3. Execute inserts specifying column names
   1.3        25/10/2013      Arte       4. Duplicate sequence for data mixing
******************************************************************************/
   
   FUNCTION RANDOM(Param1 IN NUMBER) RETURN VARCHAR2;
   PROCEDURE DumpTable(P_ORIGEN IN VARCHAR2, P_DESTINO IN VARCHAR2);
   PROCEDURE CREATE_VERSION(P_DATASET_OLD IN VARCHAR2, P_DATASET_NEW IN VARCHAR2);

END REPOSITORY;
/

create or replace 
PACKAGE BODY                     REPOSITORY
AS
   /******************************************************************************
      NAME:       REPOSITORY
      PURPOSE:

      REVISIONS:
      Ver        Date        Author           Description
      ---------  ----------  ---------------  ------------------------------------
      1.0        26/08/2013      Arte       1. Created this package body
      1.1        09/09/2013      Arte       2. Add attribute definitions
      1.2        09/09/2013      Arte       3. Execute inserts specifying column names
   ******************************************************************************/



   FUNCTION RANDOM (Param1 IN NUMBER)
      RETURN VARCHAR2
   IS
   BEGIN
      RETURN DBMS_RANDOM.STRING ('x', Param1);
   END;



   PROCEDURE DumpTable (P_ORIGEN IN VARCHAR2, P_DESTINO IN VARCHAR2)
   IS
      VA_QUERY       VARCHAR2 (1000);
      VA_DIMENSION   VARCHAR2 (1000);
      VA_TMP         VARCHAR2 (1000);
      VA_SEQ_DESTINO_NAME	VARCHAR2 (1000);
      VA_SEQ_ORIGEN_NAME	VARCHAR2 (1000);
      VA_SEQ		 NUMBER;

      TYPE cur_typ IS REF CURSOR;

      c              cur_typ;
   BEGIN
      /* Create table */
      VA_QUERY :=
            'CREATE TABLE '
         || P_DESTINO
         || ' AS (SELECT * FROM '
         || P_ORIGEN
         || ')';
      DBMS_UTILITY.EXEC_DDL_STATEMENT (VA_QUERY);

      /* Primary Key */
      VA_QUERY :=
            'ALTER TABLE '
         || P_DESTINO
         || ' ADD ( CONSTRAINT PK_'
         || SUBSTR (P_DESTINO, 6)
         || ' PRIMARY KEY (ID))';
      DBMS_UTILITY.EXEC_DDL_STATEMENT (VA_QUERY);

      /* Sequence */
      VA_SEQ_DESTINO_NAME := 'SEQ_' || SUBSTR (P_DESTINO, 6);
      VA_SEQ_ORIGEN_NAME := 'SEQ_' || SUBSTR (P_ORIGEN, 6);
      
      VA_QUERY := 'select ' || VA_SEQ_ORIGEN_NAME || '.nextval from dual';
      execute immediate VA_QUERY INTO VA_SEQ;
      
      VA_QUERY := 'create sequence ' || VA_SEQ_DESTINO_NAME || ' MINVALUE 0 START WITH ' || VA_SEQ || ' INCREMENT BY 1';
      DBMS_UTILITY.EXEC_DDL_STATEMENT (VA_QUERY);
      
      /* Index */
      VA_QUERY :=
            'select column_name from user_tab_columns where table_name = '''
         || P_DESTINO
         || ''' and column_name like ''DIMENSION%''';

      DBMS_OUTPUT.put_LINE (VA_QUERY);

      OPEN c FOR VA_QUERY;

      LOOP
         FETCH c INTO VA_DIMENSION;

         EXIT WHEN c%NOTFOUND;

         VA_TMP := SUBSTR (VA_DIMENSION, INSTR (VA_DIMENSION, '0'));

         VA_QUERY :=
               'CREATE INDEX IDX_'
            || SUBSTR (P_DESTINO, 6)
            || VA_TMP
            || ' ON '
            || P_DESTINO
            || ' (DIMENSION_'
            || VA_TMP
            || ')';

         DBMS_UTILITY.EXEC_DDL_STATEMENT (VA_QUERY);
      END LOOP;

      CLOSE c;
   END;



   FUNCTION CLONE_TB_DATASETS (P_OLD_ID        NUMBER,
                               P_DATASET_ID    VARCHAR2,
                               P_TABLE_NAME    VARCHAR2)
      RETURN NUMBER
   IS
      VA_SEQ   NUMBER;
   BEGIN
      SELECT SEQ_DATASETS.NEXTVAL INTO VA_SEQ FROM DUAL;


      INSERT INTO TB_DATASETS
      	 (ID, DATASET_ID, TABLE_NAME, MAX_ATTRIBUTES_OBSERVATION, LANGUAGES, UUID, VERSION)
         (SELECT VA_SEQ AS ID,
                 P_DATASET_ID AS DATASET_ID,
                 P_TABLE_NAME AS TABLE_NAME,
                 MAX_ATTRIBUTES_OBSERVATION AS MAX_ATTRIBUTES_OBSERVATION,
                 LANGUAGES AS LANGUAGES,
                 RANDOM (36) AS UUID,
                 VERSION AS VERSION
            FROM TB_DATASETS
           WHERE ID = P_OLD_ID);

      RETURN VA_SEQ;
   END;

   
   PROCEDURE CLONE_TB_DATASET_DIMENSIONS (P_OLD_ID NUMBER, P_NEW_ID NUMBER)
   IS
      VA_CONTADOR   NUMBER;
   BEGIN
      SELECT COUNT (1)
        INTO VA_CONTADOR
        FROM TB_DATASET_DIMENSIONS
       WHERE DATASET_FK = P_OLD_ID;

      IF VA_CONTADOR > 0
      THEN
         INSERT INTO TB_DATASET_DIMENSIONS
         	(ID, DIMENSION_ID, COLUMN_NAME, UUID, VERSION, DATASET_FK)
            (SELECT SEQ_DASET_DIMS.NEXTVAL AS ID,
                    DIMENSION_ID AS DIMENSION_ID,
                    COLUMN_NAME AS COLUMN_NAME,
                    RANDOM (36) AS UUID,
                    VERSION AS VERSION,
                    P_NEW_ID AS DATASET_FK
               FROM TB_DATASET_DIMENSIONS
              WHERE DATASET_FK = P_OLD_ID);
      END IF;
   END;
   

   PROCEDURE CLONE_TB_DATASET_ATTRIBUTES (P_OLD_ID NUMBER, P_NEW_ID NUMBER)
   IS
      VA_CONTADOR   NUMBER;
   BEGIN
      SELECT COUNT (1)
        INTO VA_CONTADOR
        FROM TB_DATASET_ATTRIBUTES
       WHERE DATASET_FK = P_OLD_ID;

      IF VA_CONTADOR > 0
      THEN
         INSERT INTO TB_DATASET_ATTRIBUTES
         	(ID, ATTRIBUTE_ID, COLUMN_NAME, COLUMN_INDEX, UUID, VERSION, DATASET_FK, ATTACHMENT_LEVEL)
            (SELECT SEQ_DATASET_ATTRIBUTES.NEXTVAL AS ID,
                    ATTRIBUTE_ID AS ATTRIBUTE_ID,
                    COLUMN_NAME AS COLUMN_NAME,
                    COLUMN_INDEX AS COLUMN_INDEX,
                    RANDOM (36) AS UUID,
                    VERSION AS VERSION,
                    P_NEW_ID AS DATASET_FK,
                    ATTACHMENT_LEVEL AS ATTACHMENT_LEVEL
               FROM TB_DATASET_ATTRIBUTES
              WHERE DATASET_FK = P_OLD_ID);
      END IF;
   END;


   FUNCTION CLONE_TB_INTERNACIONAL_STRINGS (P_OLD_ID NUMBER)
      RETURN NUMBER
   IS
      VA_SEQ        NUMBER;
      VA_CONTADOR   NUMBER;
   BEGIN
      SELECT COUNT (1)
        INTO VA_CONTADOR
        FROM TB_INTERNATIONAL_STRINGS
       WHERE ID = P_OLD_ID;

      IF VA_CONTADOR > 0
      THEN
         SELECT SEQ_I18NSTRS.NEXTVAL INTO VA_SEQ FROM DUAL;

         INSERT INTO TB_INTERNATIONAL_STRINGS
            (ID, UUID, VERSION)
            (SELECT VA_SEQ AS ID,
                    RANDOM (36) AS UUID,
                    VERSION AS VERSION
               FROM TB_INTERNATIONAL_STRINGS
              WHERE ID = P_OLD_ID);
      ELSE
         VA_SEQ := NULL;
      END IF;

      RETURN VA_SEQ;
   END;


   PROCEDURE CLONE_TB_LOCALISED_STRINGS (P_OLD_ID NUMBER, P_NEW_ID NUMBER)
   IS
      VA_CONTADOR   NUMBER;
   BEGIN
      SELECT COUNT (1)
        INTO VA_CONTADOR
        FROM TB_LOCALISED_STRINGS
       WHERE INTERNATIONAL_STRING_FK = P_OLD_ID;

      IF VA_CONTADOR > 0
      THEN
         INSERT INTO TB_LOCALISED_STRINGS
            (ID, LABEL, LOCALE, UUID, VERSION, INTERNATIONAL_STRING_FK)
            (SELECT SEQ_L10NSTRS.NEXTVAL AS ID,
                    LABEL AS LABEL,
                    LOCALE AS LOCALE,
                    RANDOM (36) AS UUID,
                    VERSION AS VERSION,
                    P_NEW_ID AS INTERNATIONAL_STRING_FK
               FROM TB_LOCALISED_STRINGS
              WHERE INTERNATIONAL_STRING_FK = P_OLD_ID);
      END IF;
   END;



   PROCEDURE CLONE_TB_ATTRIBUTE_DIMENSIONS (P_OLD_ATTRIBUTES_ID    NUMBER,
                                            P_NEW_ATTRIBUTES_ID    NUMBER)
   IS
      VA_CONTADOR   NUMBER;
   BEGIN
      SELECT COUNT (1)
        INTO VA_CONTADOR
        FROM TB_ATTRIBUTE_DIMENSIONS
       WHERE ATTRIBUTE_FK = P_OLD_ATTRIBUTES_ID;

      IF VA_CONTADOR > 0
      THEN
         INSERT INTO TB_ATTRIBUTE_DIMENSIONS
             (ID, DIMENSION_ID, CODE_DIMENSION_ID, UUID, VERSION, ATTRIBUTE_FK)
            (SELECT SEQ_ATTR_DIMS.NEXTVAL AS ID,
                    DIMENSION_ID AS DIMENSION_ID,
                    CODE_DIMENSION_ID AS CODE_DIMENSION_ID,
                    RANDOM (36) AS UUID,
                    VERSION AS VERSION,
                    P_NEW_ATTRIBUTES_ID AS ATTRIBUTE_FK
               FROM TB_ATTRIBUTE_DIMENSIONS
              WHERE ATTRIBUTE_FK = P_OLD_ATTRIBUTES_ID);
      END IF;
   END;

   PROCEDURE CLONE_TB_ATTRIBUTES (P_OLD_DATASET_ID    NUMBER,
                                  P_NEW_DATASET_ID    NUMBER)
   IS
      VA_SEQ                        NUMBER;
      VA_INTERNATIONAL_STRINGS_ID   NUMBER;

      CURSOR cursor_tb_attributes
      IS
         SELECT *
           FROM TB_ATTRIBUTES
          WHERE DATASET_FK = P_OLD_DATASET_ID;
   BEGIN
      FOR C1 IN cursor_tb_attributes
      LOOP
         SELECT SEQ_ATTRIBUTES.NEXTVAL INTO VA_SEQ FROM DUAL;

         VA_INTERNATIONAL_STRINGS_ID :=
            CLONE_TB_INTERNACIONAL_STRINGS (C1.VALUE_FK);
         CLONE_TB_LOCALISED_STRINGS (C1.VALUE_FK,
                                     VA_INTERNATIONAL_STRINGS_ID);

         INSERT INTO TB_ATTRIBUTES
         	  (ID, ATTRIBUTE_ID, UUID, VERSION, VALUE_FK, DATASET_FK)
              VALUES (VA_SEQ,
                      C1.ATTRIBUTE_ID,
                      RANDOM (36),
                      C1.VERSION,
                      VA_INTERNATIONAL_STRINGS_ID,
                      P_NEW_DATASET_ID);

         CLONE_TB_ATTRIBUTE_DIMENSIONS (C1.ID, VA_SEQ);
      END LOOP;
   END;



   PROCEDURE CREATE_VERSION (P_DATASET_OLD   IN VARCHAR2,
                             P_DATASET_NEW   IN VARCHAR2)
   IS
      VA_NEW_TABLE        VARCHAR2 (4000);
      VA_OLD_TABLE        VARCHAR2 (4000);
      VA_OLD_DATASET_ID   NUMBER;
      VA_NEW_DATASET_ID   NUMBER;
   BEGIN
      VA_NEW_TABLE := 'DATA_' || RANDOM (23);


      -- Obtenemos el nombre de la tabla vieja y el id

      SELECT ID, TABLE_NAME
        INTO VA_OLD_DATASET_ID, VA_OLD_TABLE
        FROM TB_DATASETS
       WHERE DATASET_ID = P_DATASET_OLD;

      VA_NEW_DATASET_ID :=
         CLONE_TB_DATASETS (VA_OLD_DATASET_ID, P_DATASET_NEW, VA_NEW_TABLE);


      CLONE_TB_DATASET_DIMENSIONS (VA_OLD_DATASET_ID, VA_NEW_DATASET_ID);
      
      CLONE_TB_DATASET_ATTRIBUTES (VA_OLD_DATASET_ID, VA_NEW_DATASET_ID);

      CLONE_TB_ATTRIBUTES (VA_OLD_DATASET_ID, VA_NEW_DATASET_ID);

      dumptable (VA_OLD_TABLE, VA_NEW_TABLE);
   END;

END REPOSITORY;
/