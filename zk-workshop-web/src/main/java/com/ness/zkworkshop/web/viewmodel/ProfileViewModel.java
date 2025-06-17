package com.ness.zkworkshop.web.viewmodel;

import java.io.Serializable;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;

import com.ness.zkworkshop.web.model.User;
import com.ness.zkworkshop.web.service.AuthenticationService;
import com.ness.zkworkshop.web.service.AuthenticationServiceImpl;
import com.ness.zkworkshop.web.service.CommonInfoService;
import com.ness.zkworkshop.web.service.UserCredential;
import com.ness.zkworkshop.web.service.UserInfoService;
import com.ness.zkworkshop.web.service.UserInfoServiceImpl;

import lombok.Getter;
import lombok.Setter;

public class ProfileViewModel implements Serializable{
	private static final long serialVersionUID = 1L;

	//services
	private AuthenticationService authService = new AuthenticationServiceImpl();
	private UserInfoService userInfoService = new UserInfoServiceImpl();
	
	//data for the view
	private User currentUser;
	private String htmlContent;
	@Getter
	@Setter
	private String textval;
	
	@Wire
	public Vlayout cursorPosVlayout;
	
	public User getCurrentUser(){
		return currentUser;
	}
	
	@Init
	public void init() {
		UserCredential userCredential = authService.getUserCredential();
		currentUser = userInfoService.findUser(userCredential.getAccount());
		textval = "";
	}
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
	}
	
	/**
	 * Retrieve all known country names. 
	 * @return a list of country name
	 */
	public List<String> getCountryList(){
		return CommonInfoService.getCountryList();
	}

	@Command //@Command annotates a command method 
	@NotifyChange("currentUser") //@NotifyChange annotates data changed notification after calling this method 
	public void save(@BindingParam("element") Vlayout cursorPosVlayout){
		// Events.echoEvent("xxx", cursorPosVlayout, null); 
		Clients.showBusy(cursorPosVlayout, "Pracuji");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		currentUser = userInfoService.updateUser(currentUser);
		Clients.showNotification(Labels.getLabel("web.msg.info.changesSaved", new Object[] {currentUser.getAccount()}),
				Clients.NOTIFICATION_TYPE_INFO, 
				null, 
				null, 
				2000);
		Clients.clearBusy(cursorPosVlayout);
	}

	@Command 
	@NotifyChange("currentUser")
	public void reload(){
		UserCredential cre = authService.getUserCredential();
		currentUser = userInfoService.findUser(cre.getAccount());
		Clients.clearBusy(cursorPosVlayout);
	}
	
	@NotifyChange("textval")
	@Command
	public void insertValue(@BindingParam("element") Textbox textbox) {		
		int pos = (int) textbox.getAttribute("cursorPos");
		String insertTxt = "[MY NEW STRING TO INSERT AT CURSOR]";
		Executions.getCurrent().getSession().setAttribute("position", pos + insertTxt.length());
      	textval = textval.substring(0,pos).concat(insertTxt.concat(textval.substring(pos)));
      	textbox.focus();
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}
}
