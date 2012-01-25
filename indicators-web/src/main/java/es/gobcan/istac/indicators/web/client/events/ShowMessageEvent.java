package es.gobcan.istac.indicadores.web.client.events;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

import es.gobcan.istac.indicadores.web.client.enums.MessageTypeEnum;

public class ShowMessageEvent extends GwtEvent<ShowMessageEvent.ShowMessageHandler> {

	public interface ShowMessageHandler extends EventHandler {
		void onShowMessage(ShowMessageEvent event);
	}

	private static Type<ShowMessageHandler> TYPE = new Type<ShowMessageHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ShowMessageHandler> getAssociatedType() {
		return TYPE;
	}
	
	// TODO HasEventBus should be used instead of HasHandlers ¿?
	public static void fire(HasHandlers source, List<String> messages, MessageTypeEnum messageType) {
		if (TYPE != null) {
			source.fireEvent(new ShowMessageEvent(messages, messageType));
		}
	}
	
	private final List<String> messages;
	private final MessageTypeEnum messageType;
	
	public ShowMessageEvent(List<String> messages, MessageTypeEnum messageType) {
		this.messages = messages;
		this.messageType = messageType;
	}
	
	public List<String> getMessages() {
		return messages;
	}
	
	public MessageTypeEnum getMessageType() {
		return messageType;
	}
	
	@Override
	protected void dispatch(ShowMessageHandler handler) {
		handler.onShowMessage(this);
	}
	
	public static Type<ShowMessageHandler> getType() {
		return TYPE;
	}
	
}
