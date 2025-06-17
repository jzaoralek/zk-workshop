package com.ness.zkworkshop.web.viewmodel;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

public class ModalDemoVM extends BaseVM {

	private String modalArg;
	private boolean messagesVisible = true;

	@Command
	public void openModalCmd() {
		Map<String, Object> args = new HashMap<>();
		args.put("modalArg", modalArg);
		openModal("/pages/modal.zul", args);
	}

	@NotifyChange("messagesVisible")
	@Command
	public void detailMessagesCmd() {
		messagesVisible = true;
	}

	@NotifyChange("messagesVisible")
	@Command
	public void detailSubjectListCmd() {
		messagesVisible = false;
	}

	public String getModalArg() {
		return modalArg;
	}

	public void setModalArg(String modalArg) {
		this.modalArg = modalArg;
	}

	public boolean isMessagesVisible() {
		return messagesVisible;
	}
}
