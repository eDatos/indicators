package es.gobcan.istac.indicadores.web.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

import es.gobcan.istac.indicadores.web.shared.db.IndicatorSystem;

public class SelectIndicatorSystemEvent extends GwtEvent<SelectIndicatorSystemEvent.SelectIndicatorSystemHandler> {
	
	public interface SelectIndicatorSystemHandler extends EventHandler {
		void onSelectIndicatorSystem(SelectIndicatorSystemEvent event);
	}
	
	private static Type<SelectIndicatorSystemHandler> TYPE = new Type<SelectIndicatorSystemHandler>();
	
	@Override
	public Type<SelectIndicatorSystemHandler> getAssociatedType() {
		return TYPE;
	}
	
	public static Type<SelectIndicatorSystemHandler> getType() {
		return TYPE;
	}
	
	private final IndicatorSystem indicatorSystem;
	
	public static void fire(HasHandlers source, IndicatorSystem indicatorSystem) {
		if (TYPE != null) {
			source.fireEvent(new SelectIndicatorSystemEvent(indicatorSystem));
		}
	}
	
	public SelectIndicatorSystemEvent(IndicatorSystem indicatorSystem) {
		this.indicatorSystem = indicatorSystem;
	}
	
	public IndicatorSystem getIndicatorSystem() {
		return indicatorSystem;
	}
	
	@Override
	protected void dispatch(SelectIndicatorSystemHandler handler) {
		handler.onSelectIndicatorSystem(this);
	}
	
	@Override
	public String toString() {
		return this.getClass().getName()+" "+super.toString();
	}
	
}
