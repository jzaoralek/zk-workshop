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
		pageMap.put("fn1",new SidebarPage("zk","ZK","/sources/imgs/site.png","http://www.zkoss.org/"));
		pageMap.put("fn2",new SidebarPage("demo","ZK Demo","/sources/imgs/demo.png","http://www.zkoss.org/zkdemo"));
		pageMap.put("fn3",new SidebarPage("devref", Labels.getLabel("web.zkDeveloperReference"),"/sources/imgs/doc.png","http://books.zkoss.org/wiki/ZK_Developer's_Reference"));
	}
	
	public List<SidebarPage> getPages(){
		return new ArrayList<>(pageMap.values());
	}
	
	public SidebarPage getPage(String name){
		return pageMap.get(name);
	}
}
