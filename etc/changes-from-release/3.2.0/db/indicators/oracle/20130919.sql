-- ----------------------------------------------------------------------
-- INDISTAC-765: Corregir locale de las traducciones de tipo BIYEARLY
-- ----------------------------------------------------------------------

En el script 07-indicators-inserts-translations.sql se había insertado un locale como 'em' en lugar de 'en' para los siguientes códigos:
	- TIME_VALUE.BIYEARLY.H1
	- TIME_VALUE.BIYEARLY.H2

Buscar en la tabla TB_LOCALISED_STRINGS las etiquetas siguientes y cambiar el locale 'em' por 'en':
	- {yyyy} First semester
	- {yyyy} Second semester
