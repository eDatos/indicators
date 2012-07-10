Cuando se cree la RELEASE, añadir estos pasos al manual de instalación:

1. Parar Tomcat

2. Cambios en Base de datos
	- Ejecutar script de updates-in-release/01-translations-update.sql que traduce las etiquetas de la measure dimension a lo que quiere el istac y que crea la tabla TBL_EXTERNAL_ITEMS.

99. Reiniciar Tomcat