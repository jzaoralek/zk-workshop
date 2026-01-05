package com.ness.zkworkshop.web.controller;

import org.zkoss.json.JSONObject;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;

import com.ness.zkworkshop.web.util.EventQueueHelper;
import com.ness.zkworkshop.web.util.WebUtils;

public class CursorPositionComposer extends SelectorComposer {

	private Textbox thetextbox;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		thetextbox = (Textbox) comp;
		thetextbox.addEventListener("onCursorPos", event -> handleCursorPos((Event) event));
		thetextbox.addEventListener("onFocus", event -> handleFocus((Event) event));
	}

	@Listen("onCursorPos = #thetextbox")
	public void handleCursorPos(Event e) {
		int pos = 0;
		if (e.getData() != null) {
			pos = ((int) ((JSONObject) e.getData()).get("cursorPos"));
		}
		thetextbox.setAttribute("cursorPos", pos);
	}
	
	@Listen("onFocus = #thetextbox")
	public void handleFocus(Event e) {
		Integer pos = (Integer)Executions.getCurrent().getSession().getAttribute("position");
		if (pos != null) {
			Clients.evalJavaScript("setCursorPosition('"+thetextbox.getId()+"',"+pos+")");
			thetextbox.setAttribute("cursorPos", pos);
			Executions.getCurrent().getSession().removeAttribute("position");
		}
	}
}