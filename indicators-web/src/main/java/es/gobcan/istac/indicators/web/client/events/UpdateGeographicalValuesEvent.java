package es.gobcan.istac.indicators.web.client.events;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

import es.gobcan.istac.indicators.core.dto.serviceapi.GeographicalGranularityDto;


public class UpdateGeographicalValuesEvent extends GwtEvent<UpdateGeographicalValuesEvent.UpdateGeographicalValuesHandler> {

    public interface UpdateGeographicalValuesHandler extends EventHandler {
        void onUpdateGeographicalValues(UpdateGeographicalValuesEvent event);
    }

    private static Type<UpdateGeographicalValuesHandler> TYPE = new Type<UpdateGeographicalValuesHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<UpdateGeographicalValuesHandler> getAssociatedType() {
        return TYPE;
    }
    
    // TODO HasEventBus should be used instead of HasHandlers ¿?
    public static void fire(HasHandlers source, List<GeographicalGranularityDto> geoValues) {
        if (TYPE != null) {
            source.fireEvent(new UpdateGeographicalValuesEvent(geoValues));
        }
    }

    @Override
    protected void dispatch(UpdateGeographicalValuesHandler handler) {
        handler.onUpdateGeographicalValues(this);
    }
    
    private final List<GeographicalGranularityDto> geoValues;
    
    public UpdateGeographicalValuesEvent(List<GeographicalGranularityDto> geoValues) {
        this.geoValues = geoValues;
    }
    
    public List<GeographicalGranularityDto> getGeographicalValues() {
        return geoValues;
    }

    public static Type<UpdateGeographicalValuesHandler> getType() {
        return TYPE;
    }
    
}