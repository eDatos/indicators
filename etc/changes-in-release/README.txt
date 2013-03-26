Cuando se cree la RELEASE, añadir estos pasos al manual de instalación:

1. Parar Tomcat

2. Base de datos:
	- Ejecutar el script 03-updates-in-release/01-change-symbol-restrictions.sql
	- Ejecutar el script 03-updates-in-release/02-update-localised-string.sql
	- Ejecutar el script 03-updates-in-release/03-update-localised-string.sql
	
3. DATA:
	- Cambiar los ficheros de logback en conf/dynamic tanto en la aplicación externa como interna. Por los nuevos ficheros
	- Nuevas propiedades para difusion relacionada con migas de pan
	- Nuevas propiedades en datasources.xml en app interna

99. Reiniciar Tomcat 