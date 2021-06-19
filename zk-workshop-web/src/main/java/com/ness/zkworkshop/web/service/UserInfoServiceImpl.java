package com.ness.zkworkshop.web.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ness.zkworkshop.web.model.User;

public class UserInfoServiceImpl implements UserInfoService,Serializable{
	private static final long serialVersionUID = 1L;
	
	protected static List<User> userList = new ArrayList<>();  
	
	static{
		userList.add(new User("anonymous","1234","Anonymous","anonumous@your.com"));
		userList.add(new User("admin","1234","Admin","admin@your.com"));
		userList.add(new User("zkoss","1234","ZKOSS","info@zkoss.org"));
	}
	
	/** synchronized is just because we use static userList in this demo to prevent concurrent access **/
	public synchronized User findUser(String account){
		int s = userList.size();
		for(int i=0;i<s;i++){
			User u = userList.get(i);
			if(account.equals(u.getAccount())){
				return User.clone(u);
			}
		}
		return null;
	}
	
	/** synchronized is just because we use static userList in this demo to prevent concurrent access **/
	public synchronized User updateUser(User user){
		int s = userList.size();
		for(int i=0;i<s;i++){
			User u = userList.get(i);
			if(user.getAccount().equals(u.getAccount())){
				userList.set(i,u = User.clone(user));
				return u;
			}
		}
		throw new RuntimeException("user not found "+user.getAccount());
	}
}
