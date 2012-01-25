package es.gobcan.istac.indicators.web.shared.db;

public enum IndicatorSystemStatus {

	BORRADOR("Borrador"),
	PENDIENTE_VAL_PRODUCCION("Pendiente de validación de producción"),
	PENDIENTE_VAL_DIFUSION("Pendiente de validación de difusión"),
	RECHAZO_VAL("Rechazo de validación"),
	PENDIENTE_PUBLICACION("Pendiente de publicación"),
	FALLO_PUBLICACION("Fallo de publicación"),
	PUBLICADO("Publicado"),
	ARCHIVADO("Archivado");
	
	private String value;
	private IndicatorSystemStatus(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
}
