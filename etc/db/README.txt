-----------------------------------------------------------------
  EJECUCIÓN DE SCRIPTS DE CREACIÓN, CARGA... DE BASE DE DATOS
-----------------------------------------------------------------

1) ENTORNO LOCAL
	
	- Ejecutar los scripts de la carpeta "01-create"
    - Ejecutar los scripts de la carpeta "99-local"
	Nota: para los tests no haría falta ejecutar los scripts 01-indicators-inserts*.sql
    
2) ENTORNO ISTAC o CIBER
    - Ejecutar los scripts de la carpeta "01-create"
    - IMPORTANTE: NO ejecutar los scripts de la carpeta "99-local", ya que las vistas ya estarán creadas.
    
3) RELEASES:
    - Ejecutar los scripts de la carpeta "03-updates-in-release"
    