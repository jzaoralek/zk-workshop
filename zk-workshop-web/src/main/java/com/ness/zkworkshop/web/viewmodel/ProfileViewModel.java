package com.ness.zkworkshop.web.viewmodel;

import org.zkoss.bind.annotation.*;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.util.Clients;

import com.ness.zkworkshop.web.model.User;
import com.ness.zkworkshop.web.service.AuthenticationService;
import com.ness.zkworkshop.web.service.AuthenticationServiceImpl;
import com.ness.zkworkshop.web.service.CommonInfoService;
import com.ness.zkworkshop.web.service.UserCredential;
import com.ness.zkworkshop.web.service.UserInfoService;
import com.ness.zkworkshop.web.service.UserInfoServiceImpl;

import java.io.Serializable;
import java.util.List;

public class ProfileViewModel implements Serializable{
	private static final long serialVersionUID = 1L;
	
	//services
	private AuthenticationService authService = new AuthenticationServiceImpl();
	private UserInfoService userInfoService = new UserInfoServiceImpl();
	
	//data for the view
	private User currentUser;
	
	@Init
	public void init() {
		UserCredential userCredential = authService.getUserCredential();
		currentUser = userInfoService.findUser(userCredential.getAccount());
		if(currentUser==null){
			return;
		}
	}
	
	public User getCurrentUser(){
		return currentUser;
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
	public void save(){
		currentUser = userInfoService.updateUser(currentUser);
		Clients.showNotification(Labels.getLabel("web.msg.info.changesSaved"), 
				Clients.NOTIFICATION_TYPE_INFO, 
				null, 
				null, 
				2000);
	}

	@Command 
	@NotifyChange("currentUser")
	public void reload(){
		UserCredential cre = authService.getUserCredential();
		currentUser = userInfoService.findUser(cre.getAccount());
	}
}
