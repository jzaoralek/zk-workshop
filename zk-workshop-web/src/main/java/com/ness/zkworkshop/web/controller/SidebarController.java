package com.ness.zkworkshop.web.controller;

import org.zkoss.zk.ui.select.annotation.Wire;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import com.ness.zkworkshop.web.config.SidebarPage;
import com.ness.zkworkshop.web.config.SidebarPageConfig;
import com.ness.zkworkshop.web.config.SidebarPageConfigImpl;

public class SidebarController extends SelectorComposer<Component>{

	private static final long serialVersionUID = 1L;
	
	// wire components
	// nazev promenne odpovida id komponenty na sidebar.zul
	@Wire
	private Grid sidebar;
	
	//config provider
	private SidebarPageConfig pageConfig = new SidebarPageConfigImpl();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception{
		// perform inicialization like wiring
		super.doAfterCompose(comp);
		
		//initialize view after view construction.
		Rows rows = sidebar.getRows();
		
		// dynamicke naplněni rows z konfigurace
		for(SidebarPage page:pageConfig.getPages()){
			Row row = constructSidebarRow(page.getLabel(),page.getIconUri(),page.getUri());
			rows.appendChild(row);
		}
	}

	private Row constructSidebarRow(String label, String imageSrc, final String locationUri) {
		
		//construct component and hierarchy
		Row row = new Row();
		Image image = new Image(imageSrc);
		Label lab = new Label(label);
		
		row.appendChild(image);
		row.appendChild(lab);
		
		//set style attribute
		row.setSclass("sidebar-fn");
		
		//create and register event listener
		EventListener<Event> actionListener = new SerializableEventListener<Event>() {
			private static final long serialVersionUID = 1L;

			public void onEvent(Event event) throws Exception {
				//redirect current url to new location
				Executions.getCurrent().sendRedirect(locationUri);
			}
		};
		
		row.addEventListener(Events.ON_CLICK, actionListener);

		return row;
	}
}
