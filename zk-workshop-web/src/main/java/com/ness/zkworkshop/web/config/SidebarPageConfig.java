package com.ness.zkworkshop.web.config;

import java.util.List;

public interface SidebarPageConfig {
	/** get pages of this application **/
	public List<SidebarPage> getPages();
	
	/** get page **/
	public SidebarPage getPage(String name);
}