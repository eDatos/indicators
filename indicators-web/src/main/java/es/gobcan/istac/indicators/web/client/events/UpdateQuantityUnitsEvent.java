package es.gobcan.istac.indicators.web.client.events;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

import es.gobcan.istac.indicators.core.dto.serviceapi.QuantityUnitDto;

public class UpdateQuantityUnitsEvent extends GwtEvent<UpdateQuantityUnitsEvent.UpdateQuantityUnitsHandler> {

	public interface UpdateQuantityUnitsHandler extends EventHandler {
		void onUpdateQuantityUnits(UpdateQuantityUnitsEvent event);
	}

	private static Type<UpdateQuantityUnitsHandler> TYPE = new Type<UpdateQuantityUnitsHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<UpdateQuantityUnitsHandler> getAssociatedType() {
		return TYPE;
	}
	
	// TODO HasEventBus should be used instead of HasHandlers ¿?
	public static void fire(HasHandlers source, List<QuantityUnitDto> quantityUnits) {
		if (TYPE != null) {
			source.fireEvent(new UpdateQuantityUnitsEvent(quantityUnits));
		}
	}

	@Override
	protected void dispatch(UpdateQuantityUnitsHandler handler) {
		handler.onUpdateQuantityUnits(this);
	}
	
	private final List<QuantityUnitDto> quantityUnits;
	
	public UpdateQuantityUnitsEvent(List<QuantityUnitDto> quantityUnits) {
		this.quantityUnits = quantityUnits;
	}
	
	public List<QuantityUnitDto> getQuantityUnits() {
		return quantityUnits;
	}

	public static Type<UpdateQuantityUnitsHandler> getType() {
		return TYPE;
	}

}
