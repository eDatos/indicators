package es.gobcan.istac.indicadores.web.client.utils;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.dispatch.shared.ServiceException;

public class ErrorUtils {

	/**
	 * Returns error message
	 * 
	 * @param caught
	 * @param message
	 */
	public static String getErrorMessage(Throwable caught, String message) {
		if (caught instanceof ServiceException) {
			return message;
		} else {
			return caught.getMessage();
		}
	}
	
	/**
	 * 
	 * @param messages
	 * @return
	 */
	public static List<String> getMessageList(Throwable caught, String ...messages) {
		List<String> messageList = new ArrayList<String>();
		if (!(caught instanceof ServiceException)) {
			messageList.add(caught.getMessage());
		} else {
			for (String message : messages) {
				messageList.add(message);
			}
		}
		return messageList;
	}
	
	/**
	 * 
	 * @param messages
	 * @return
	 */
	public static List<String> getMessageList(String ...messages) {
		List<String> messageList = new ArrayList<String>();
		for (String message : messages) {
			messageList.add(message);
		}
		return messageList;
	}

}
