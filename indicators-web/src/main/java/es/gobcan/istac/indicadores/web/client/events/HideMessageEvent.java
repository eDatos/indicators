package es.gobcan.istac.indicadores.web.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class HideMessageEvent extends GwtEvent<HideMessageEvent.HideMessageHandler> {

	public interface HideMessageHandler extends EventHandler {
		void onHideMessage(HideMessageEvent event);
	}

	private static Type<HideMessageHandler> TYPE = new Type<HideMessageHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<HideMessageHandler> getAssociatedType() {
		return TYPE;
	}
	
	// TODO HasEventBus should be used instead of HasHandlers ¿?
	public static void fire(HasHandlers source) {
		if (TYPE != null) {
			source.fireEvent(new HideMessageEvent());
		}
	}
	
	public HideMessageEvent() {
		
	}

	@Override
	protected void dispatch(HideMessageHandler handler) {
		handler.onHideMessage(this);
	}
	
	public static Type<HideMessageHandler> getType() {
		return TYPE;
	}
	
}
