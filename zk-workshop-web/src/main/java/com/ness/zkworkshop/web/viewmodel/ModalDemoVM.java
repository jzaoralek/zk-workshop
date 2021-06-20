package com.ness.zkworkshop.web.viewmodel;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

public class ModalDemoVM {

	private String modalArg;

	@Command
	public void openModalCmd() {
		Map<String, Object> args = new HashMap<>();
		args.put("modalArg", modalArg);
		openModal("/pages/modal.zul", args);
	}
	
	public static void openModal(String page, Map<String, Object> args) {
		Window window = (Window) Executions.createComponents(page, null, args);
		window.doModal();
	}
	
	public String getModalArg() {
		return modalArg;
	}

	public void setModalArg(String modalArg) {
		this.modalArg = modalArg;
	}
}
