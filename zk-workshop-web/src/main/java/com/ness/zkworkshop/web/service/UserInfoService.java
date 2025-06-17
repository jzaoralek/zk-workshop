package com.ness.zkworkshop.web.service;

import com.ness.zkworkshop.web.model.User;

public interface UserInfoService {

	/** find user by account **/
	public User findUser(String account);
	
	/** update user **/
	public User updateUser(User user);
}
