Cuando se cree la RELEASE, añadir estos pasos al manual de instalación:

1. Parar Tomcat

2. Base de datos:
	- Ejecutar el script 03-updates-in-release/01-update-columns-length.sql, tanto para Oracle como MSSql (Cambiado tamaño de Enums y otra columna de ExternalItem)

3. Configuración del data
	- Añadir las nuevas propiedades con su explicación
		  	<entry key="indicators.dspl.provider.name">Instituto Canario de Estadística</entry>
    		<entry key="indicators.dspl.provider.description">Instituto Canario de Estadística</entry>
    		<entry key="indicators.dspl.provider.url">http://www.gobiernodecanarias.org/istac/</entry>
    		<entry key="indicators.dspl.indicators.system.url">http://RELLENAR_HOST/indicators-web/indicatorsSystems/[SYSTEM]/</entry>

99. Reiniciar Tomcat 