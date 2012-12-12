Cuando se cree la RELEASE, añadir estos pasos al manual de instalación:

1. Parar Tomcat

2. Base de datos:
	- Ejecutar el script 03-updates-in-release/01-update-columns-length.sql, tanto para Oracle como MSSql (Cambiado tamaño de Enums y otra columna de ExternalItem)

99. Reiniciar Tomcat 