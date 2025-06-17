package com.ness.zkworkshop.web.viewmodel;

import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;

public class ModalVM {

	private String modalArg;

	@Init
	public void init() {
		modalArg = (String)Executions.getCurrent().getArg().get("modalArg");
	}
	
	public String getModalArg() {
		return modalArg;
	}
	
}
