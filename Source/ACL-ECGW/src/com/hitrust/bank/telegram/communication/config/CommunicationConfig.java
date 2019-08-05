package com.hitrust.bank.telegram.communication.config;

import com.hitrust.util.DOM;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class CommunicationConfig {
	// protected static Document commnunicationDoc =
	// DOM.loadDoc(Thread.currentThread().getContextClassLoader().getResourceAsStream("communication.xml"));
	protected static Document commnunicationDoc = DOM.loadDoc("ICNB");
	protected static HashMap sysCommunication;
	private static int state = 0;

//	static {
//		loadCommunication();
//	}

	private static void loadCommunication() {

		if (state == 0) {
			try {
				sysCommunication = new HashMap();

				NodeList communicationList = commnunicationDoc
						.getElementsByTagName("communication");
				for (int i = 0; i < communicationList.getLength(); i++) {
					Element communicationItem = (Element) communicationList
							.item(i);

					String communicationName = communicationItem
							.getAttribute("name");

					NodeList paramList = communicationItem
							.getElementsByTagName("parameter");
					HashMap paramMap = new HashMap();
					for (int j = 0; j < paramList.getLength(); j++) {
						Element paramItem = (Element) paramList.item(j);
						paramMap.put(paramItem.getAttribute("name"),
								paramItem.getAttribute("value"));
					}
					sysCommunication.put(communicationName, paramMap);
				}
				state = 1;
			} catch (Throwable e) {
				state = 2;
				e.printStackTrace();
			}

		}
	}

	public static HashMap getCommunicationParameters(String communication_name) {
		loadCommunication();
		if (sysCommunication.containsKey(communication_name)) {
			return (HashMap) sysCommunication.get(communication_name);
		}
		return null;
	}

	public static String getCommunicationParameterValue(
			String communication_name, String parameter_name) {
		loadCommunication();
		if (sysCommunication.containsKey(communication_name)) {
			HashMap paramMap = (HashMap) sysCommunication
					.get(communication_name);
			if (paramMap.containsKey(parameter_name)) {
				return paramMap.get(parameter_name).toString();
			}
			return null;
		}

		return null;
	}
}