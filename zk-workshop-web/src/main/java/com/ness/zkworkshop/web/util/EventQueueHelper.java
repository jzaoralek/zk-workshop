package com.ness.zkworkshop.web.util;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;

public final class EventQueueHelper {

	public enum SdatEventQueues {
		DASHBOARD_QUEUE;
	}

	public enum SdatEvent {
		ADD_WIDGET(SdatEventQueues.DASHBOARD_QUEUE),
		EDIT_MODE(SdatEventQueues.DASHBOARD_QUEUE),
		DASHBOARD_RENAME(SdatEventQueues.DASHBOARD_QUEUE);

		private SdatEventQueues parentQueue;

		private SdatEvent(SdatEventQueues parent) {
	        this.parentQueue = parent;
	    }

	    public SdatEventQueues getParentQueue() {
			return parentQueue;
		}
	}

	private EventQueueHelper() {}

	public static void publish(SdatEvent event, Object data) {
		publish(event, data, EventQueues.DESKTOP);
	}

	public static void publish(SdatEvent event, Object data, String scope) {
		EventQueue<Event> eq = EventQueues.lookup(event.getParentQueue().name(), scope, true);
		eq.publish(new TypifiedEvent(event, data));
	}

	public static SdatEventQueue queueLookup(SdatEventQueues queue) {
		return queueLookup(queue, EventQueues.DESKTOP);
	}

	public static SdatEventQueue queueLookup(SdatEventQueues queue, String scope) {
		return new SdatEventQueue(EventQueues.lookup(queue.name(), EventQueues.DESKTOP, true));
	}

}