-----------------------------------------------------------------
  EJECUCIÓN DE SCRIPTS DE CREACIÓN, CARGA... DE BASE DE DATOS
-----------------------------------------------------------------

1) ENTORNO LOCAL
	
	- Ejecutar los scripts *-indicators-schema-create*.sql
    - Ejecutar los scripts *-indicators-inserts*.sql
    - Ejecutar los scripts /local/*.sql
	Nota: para los tests no haría falta ejecutar los scripts 01-indicators-inserts*.sql
    
2) ENTORNO ISTAC o CIBER
    - Ejecutar los scripts *-indicators-schema-create*.sql
    - Ejecutar los scripts *-indicators-inserts*.sql
    - IMPORTANTE: NO ejecutar los scripts /local/*.sql, ya que las vistas ya estarán creadas.