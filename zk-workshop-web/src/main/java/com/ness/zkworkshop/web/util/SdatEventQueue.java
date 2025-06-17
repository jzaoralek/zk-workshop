package com.ness.zkworkshop.web.util;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;

public class SdatEventQueue {

	private EventQueue<Event> queue;

	public SdatEventQueue(EventQueue<Event> queue) {
		this.queue = queue;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void subscribe(EventQueueHelper.SdatEvent event, SingleEventConsumer consumer) {
		EventListener listener = new TypifiedEventListener(event, consumer);
		queue.subscribe(listener);
	}

}
