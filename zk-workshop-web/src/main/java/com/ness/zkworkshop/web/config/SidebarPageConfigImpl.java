package com.ness.zkworkshop.web.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.zkoss.util.resource.Labels;

/**
 * Konfigurace sidebar menu.
 *
 */
public class SidebarPageConfigImpl implements SidebarPageConfig{

	HashMap<String,SidebarPage> pageMap = new LinkedHashMap<String,SidebarPage>();
	public SidebarPageConfigImpl(){		
		pageMap.put("fn1",new SidebarPage("home",Labels.getLabel("web.dashboard"),"","/", "z-icon-home"));
		pageMap.put("fn2",new SidebarPage("form", Labels.getLabel("web.userProfile") + " (Formulář)","/sources/imgs/profile.png","/pages/user-profile.zul", "z-icon-user"));
		pageMap.put("fn3",new SidebarPage("todolist", Labels.getLabel("web.todoList") + " (CRUD)","/sources/imgs/todo.png","/pages/todolist.zul", "z-icon-list"));
		pageMap.put("fn4",new SidebarPage("modal", "Modál" ,null,"/pages/modal-demo.zul", "z-icon-window-maximize"));
		pageMap.put("fn5",new SidebarPage("tabbox", "Tabbox" ,null,"/pages/tabbox.zul", "z-icon-folder"));
		pageMap.put("fn6",new SidebarPage("tree", "Tree" ,null,"/pages/tree.zul", "z-icon-sitemap"));
		pageMap.put("fn7",new SidebarPage("chart", "Chart" ,null,"/pages/chart.zul", "z-icon-signal"));
		pageMap.put("fn8",new SidebarPage("spreadsheet", "Spreadsheet" ,null,"/pages/spreadsheet.zul", "z-icon-table"));
	}
	
	public List<SidebarPage> getPages(){
		return new ArrayList<>(pageMap.values());
	}
	
	public SidebarPage getPage(String name){
		return pageMap.get(name);
	}
}
