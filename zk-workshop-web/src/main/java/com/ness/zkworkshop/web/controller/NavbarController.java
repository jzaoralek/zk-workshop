package com.ness.zkworkshop.web.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkmax.zul.Navbar;
import org.zkoss.zkmax.zul.Navitem;

import com.ness.zkworkshop.web.config.SidebarPage;
import com.ness.zkworkshop.web.config.SidebarPageConfig;
import com.ness.zkworkshop.web.config.SidebarPageConfigImpl;

public class NavbarController extends SelectorComposer<Component>{

	private static final long serialVersionUID = 1L;
	
	// wire components
	// nazev promenne odpovida id komponenty na sidebar.zul
	@Wire
	private Navbar navbar;
	
	//config provider
	private SidebarPageConfig pageConfig = new SidebarPageConfigImpl();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception{
		// perform inicialization like wiring
		super.doAfterCompose(comp);
		
		// dynamicke naplněni rows z konfigurace
		for(SidebarPage page:pageConfig.getPages()){
			Navitem row = constructSidebarRow(page.getLabel(),page.getIconSclass(),page.getUri());
			navbar.appendChild(row);
		}
	}

	private Navitem constructSidebarRow(String label, String iconSclass, final String locationUri) {
		
		//construct component and hierarchy
		Navitem navitem = new Navitem();
		navitem.setLabel(label);
		navitem.setIconSclass(iconSclass);
		navitem.setStyle("margin-top: 15px; margin-bottom: 15px;");
		
		//create and register event listener
		EventListener<Event> actionListener = new SerializableEventListener<Event>() {
			private static final long serialVersionUID = 1L;

			public void onEvent(Event event) throws Exception {
				//redirect current url to new location
				Executions.getCurrent().sendRedirect(locationUri);
			}
		};
		
		navitem.addEventListener(Events.ON_CLICK, actionListener);

		return navitem;
	}
}