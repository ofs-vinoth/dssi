package com.thecdmgroup.driiverseatservice.domain;

import java.util.List;

public class Analytics {

	private AnalyticSession session;
	private List<AppActivity> appActivity;
	private List<PresentationActivity> presentationActivity;
	private List<User> users;
	
	
	/**
	 * 
	 * @return session
	 */
	public AnalyticSession getSession() {
		return session;
	}
	
	/**
	 * 
	 * @param session
	 */
	public void setSession(AnalyticSession session) {
		this.session = session;
	}
	
	/**
	 * 
	 * @return appActivity
	 */
	public List<AppActivity> getAppActivity() {
		return appActivity;
	}
	
	/**
	 * 
	 * @param appActivity
	 */
	public void setAppActivity(List<AppActivity> appActivity) {
		this.appActivity = appActivity;
	}
	
	/**
	 * 
	 * @return presentationActivity
	 */
	public List<PresentationActivity> getPresentationActivity() {
		return presentationActivity;
	}
	
	/**
	 * 
	 * @param presentationActivity
	 */
	public void setPresentationActivity(List<PresentationActivity> presentationActivity) {
		this.presentationActivity = presentationActivity;
	}

	/**
	 * @return the users
	 */
	public List<User> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}
		
}
