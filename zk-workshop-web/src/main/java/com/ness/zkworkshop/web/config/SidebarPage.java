package com.ness.zkworkshop.web.config;

import java.io.Serializable;

public class SidebarPage implements Serializable{
	private static final long serialVersionUID = 1L;
	String name;
	String label;
	String iconUri;
	String uri;
	String iconSclass;

	public SidebarPage(String name, String label, String iconUri, String uri, String iconSclass) {
		this.name = name;
		this.label = label;
		this.iconUri = iconUri;
		this.uri = uri;
		this.iconSclass = iconSclass;
	}

	public String getName() {
		return name;
	}

	public String getLabel() {
		return label;
	}

	public String getIconUri() {
		return iconUri;
	}

	public String getUri() {
		return uri;
	}
	
	public String getIconSclass() {
		return iconSclass;
	}
}