Cuando se cree la RELEASE, añadir estos pasos al manual de instalación:

1. Parar Tomcat

2. DATA
	Eliminado DATA de la aplicación web externa. Se configura en un fichero de propiedades dentro del WAR. También el fichero de configuración de logs.
	Cambio por completo de la estructura del DATA.
	Las propiedades del DATA pasan a estar en BBDD.

3. BBDD
	Ejecutar scripts de actualización de indicators.
	Es necesario recrear por completo el esquema de indicators-data

99. Reiniciar Tomcat 