package es.gobcan.istac.indicators.web.client.events;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalGranularityDto;

public class UpdateGeographicalGranularitiesEvent extends GwtEvent<UpdateGeographicalGranularitiesEvent.UpdateGeographicalGranularitiesHandler> {

	public interface UpdateGeographicalGranularitiesHandler extends EventHandler {
		void onUpdateGeographicalGranularities(UpdateGeographicalGranularitiesEvent event);
	}

	private static Type<UpdateGeographicalGranularitiesHandler> TYPE = new Type<UpdateGeographicalGranularitiesHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<UpdateGeographicalGranularitiesHandler> getAssociatedType() {
		return TYPE;
	}
	
	// TODO HasEventBus should be used instead of HasHandlers ¿?
	public static void fire(HasHandlers source, List<GeographicalGranularityDto> geoGranularities) {
		if (TYPE != null) {
			source.fireEvent(new UpdateGeographicalGranularitiesEvent(geoGranularities));
		}
	}

	@Override
	protected void dispatch(UpdateGeographicalGranularitiesHandler handler) {
		handler.onUpdateGeographicalGranularities(this);
	}
	
	private final List<GeographicalGranularityDto> geoGranularities;
	
	public UpdateGeographicalGranularitiesEvent(List<GeographicalGranularityDto> geoGranularities) {
		this.geoGranularities = geoGranularities;
	}
	
	public List<GeographicalGranularityDto> getGeographicalGranularities() {
		return geoGranularities;
	}

	public static Type<UpdateGeographicalGranularitiesHandler> getType() {
		return TYPE;
	}

}
