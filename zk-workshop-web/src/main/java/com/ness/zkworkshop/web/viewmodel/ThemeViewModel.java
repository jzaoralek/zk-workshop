package com.ness.zkworkshop.web.viewmodel;

import java.util.Arrays;
import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.theme.Themes;

public class ThemeViewModel {

	private List<String> themes = Arrays.asList(Themes.getThemes());
	private String themeSelected;

	@Init
	public void init() {
		themeSelected = Themes.getCurrentTheme();
	}

	@Command
	public void themeSelectedCmd() {
		Themes.setTheme(Executions.getCurrent(), themeSelected);
		Executions.sendRedirect("");	
	}
	
	public List<String> getThemes() {
		return themes;
	}
	
	public String getThemeSelected() {
		return themeSelected;
	}

	public void setThemeSelected(String themeSelected) {
		this.themeSelected = themeSelected;
	}
}
