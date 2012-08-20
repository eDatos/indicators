Cuando se cree la RELEASE, añadir estos pasos al manual de instalación:

1. Parar Tomcat

2. Cambios en Base de datos
	- Ejecutar scripts de updates-in-release:
	   · 01-translations-update.sql: traduce las etiquetas de la measure dimension a lo que quiere el ISTAC 
	   · 02-external-items.sql: crea la tabla TBL_EXTERNAL_ITEMS

3. Cambios en el data
	- Se han sustituido los clientes de ws de Statistical Operations por API Rest.
	
    - Eliminada propiedad "indicators.ws.metamac.statistical.operations.internal.endpoint" ([DATA]/conf/static/resources.xml)
    - Eliminada propiedad "indicators.ws.metamac.statistical.operations.external.endpoint" ([DATA]/conf/static/resources.xml)
    - Añadida propiedad "indicators.internal.edition.languages" ([DATA]/conf/static/resources.xml)
    - Añadida propiedad "indicators.clients.statistical.operations.rest.internal" ([DATA]/conf/static/resources.xml)
    - Añadida propiedad "indicators.clients.statistical.operations.rest.external" ([DATA]/conf/static/resources.xml)

99. Reiniciar Tomcat 