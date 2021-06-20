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
//		pageMap.put("fn1",new SidebarPage("zk","ZK","/sources/imgs/site.png","http://www.zkoss.org/", null));
//		pageMap.put("fn2",new SidebarPage("demo","ZK Demo","/sources/imgs/demo.png","http://www.zkoss.org/zkdemo", null));
//		pageMap.put("fn3",new SidebarPage("devref", Labels.getLabel("web.zkDeveloperReference"),"/sources/imgs/doc.png","http://books.zkoss.org/wiki/ZK_Developer's_Reference", null));
		pageMap.put("fn1",new SidebarPage("home","Home","","/", "z-icon-home"));
		pageMap.put("fn4",new SidebarPage("form", Labels.getLabel("web.userProfile") + " (MVVM)","/sources/imgs/profile.png","/pages/user-profile.zul", "z-icon-user"));
		pageMap.put("fn5",new SidebarPage("todolist", Labels.getLabel("web.todoList") + " (MVVM)","/sources/imgs/todo.png","/pages/todolist.zul", "z-icon-list"));
		pageMap.put("fn6",new SidebarPage("tree", "Tree" ,null,"/pages/tree.zul", "z-icon-sitemap"));
		pageMap.put("fn7",new SidebarPage("tabbox", "Tabbox" ,null,"/pages/tabbox.zul", "z-icon-table"));
	}
	
	public List<SidebarPage> getPages(){
		return new ArrayList<>(pageMap.values());
	}
	
	public SidebarPage getPage(String name){
		return pageMap.get(name);
	}
}
