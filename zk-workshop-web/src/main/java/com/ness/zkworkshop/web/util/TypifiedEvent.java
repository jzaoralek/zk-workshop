package com.ness.zkworkshop.web.util;

import org.zkoss.zk.ui.event.Event;

public class TypifiedEvent extends Event {

	private static final long serialVersionUID = -7578476282619748677L;

	private EventQueueHelper.SdatEvent eventType;

	public TypifiedEvent(EventQueueHelper.SdatEvent eventType, Object data) {
		super(eventType.name(), null, data);
		this.eventType = eventType;
	}

	public EventQueueHelper.SdatEvent getEventType() {
		return eventType;
	}

}
