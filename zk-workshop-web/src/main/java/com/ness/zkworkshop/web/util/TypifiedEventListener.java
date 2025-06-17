package com.ness.zkworkshop.web.util;

import org.zkoss.zk.ui.event.EventListener;

public class TypifiedEventListener implements EventListener<TypifiedEvent> {
	
	private EventQueueHelper.SdatEvent eventType;
	private SingleEventConsumer singleEventConsumer;

	public TypifiedEventListener(EventQueueHelper.SdatEvent eventType, SingleEventConsumer singleEventConsumer) {
		this.eventType = eventType;
		this.singleEventConsumer = singleEventConsumer;
	}

	@Override
	public void onEvent(TypifiedEvent event) throws Exception {
		if (event.getEventType() == eventType) {
			singleEventConsumer.accept(event.getData());
		}
	}
}
