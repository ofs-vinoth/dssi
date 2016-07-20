/**
 * 
 */
package com.thecdmgroup.driiverseatservice.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.sforce.soap.enterprise.sobject.Call2_Key_Message_vod__c;
import com.sforce.soap.enterprise.sobject.Call2_vod__c;
import com.sforce.soap.enterprise.sobject.Drii_Analytic_Session__c;
import com.sforce.soap.enterprise.sobject.Drii_App_Activity__c;
import com.sforce.soap.enterprise.sobject.Drii_HCPs__c;
import com.sforce.soap.enterprise.sobject.Drii_Presentation_Activity__c;
import com.thecdmgroup.driiverseatservice.common.DriverSeatConstants;
import com.thecdmgroup.driiverseatservice.common.ServiceException;
import com.thecdmgroup.driiverseatservice.domain.AnalyticSession;
import com.thecdmgroup.driiverseatservice.domain.Analytics;
import com.thecdmgroup.driiverseatservice.domain.AppActivity;
import com.thecdmgroup.driiverseatservice.domain.NewlyAddedObjects;
import com.thecdmgroup.driiverseatservice.domain.PresentationActivity;
import com.thecdmgroup.driiverseatservice.domain.ResponseObject;
import com.thecdmgroup.driiverseatservice.domain.User;
import com.thecdmgroup.driiverseatservice.service.AnalyticService;
import com.thecdmgroup.driiverseatservice.transport.jaxws.SalesforceProxyImpl;
import com.thecdmgroup.driiverseatservice.util.DriiverSeatUtil;

/**
 * @author vinothn
 *
 */
public class AnalyticServiceImpl implements AnalyticService {

	private static final Logger log = LogManager.getLogger(AnalyticServiceImpl.class);

	@Autowired
	private SalesforceProxyImpl proxy;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thecdmgroup.driiverseatservice.service.AnalticService#
	 * postPresentationActivities(java.util.List)
	 */

	/* (non-Javadoc)
	 * @see com.thecdmgroup.driiverseatservice.service.AnalyticService#postAnalytics(com.thecdmgroup.driiverseatservice.domain.Analytics)
	 */
	@Override
	public ResponseObject postAnalytics(Analytics activities) throws ServiceException {
		
		ResponseObject result = new ResponseObject();
		NewlyAddedObjects addedObjects = null;
		try {
			NewlyAddedObjects addedVeevaObjects = postVeevaDomainAnalytics(activities);
			result.setStatus(addedVeevaObjects.getStatus());
			result.setDescription(addedVeevaObjects.getErrors());
			
			if (DriverSeatConstants.SUCCESS.equals(addedVeevaObjects.getStatus())) {
				addedObjects = postDrivCustObjectAnalytics(activities);
				if (DriverSeatConstants.ERROR.equals(addedObjects.getStatus())) {
					
					proxy.bulkDelete(addedObjects.getIdsAsArray());
					result.setStatus(addedObjects.getStatus());
					result.setDescription(addedObjects.getErrors());
				}
			} 
			
		} catch (Exception ce) {
			
			log.debug("postAnalytics Exception" + ce);
			log.error(ce.getStackTrace());
			throw new ServiceException(ce);
		}
		
		return result;
	}
	
	
	private NewlyAddedObjects postVeevaDomainAnalytics(Analytics activities) throws ServiceException {
		
		//List<ResponseObject> result = null;
		NewlyAddedObjects addedObjects = null;
		String callId = null;
		AnalyticSession session = activities.getSession();
		String repId = session.getRepId();
		String productId = session.getProductId();
		Date date = session.getsTime();
		List<PresentationActivity> presentationactivities =   activities.getPresentationActivity();
		//List<AppActivity> appactivities = activities.getAppActivity();
		List<User> users = activities.getUsers();
		String userId = null;
		String parrentCallId = null;
		double attendees = (users != null && users.size() > 0) ? users.size() - 1 : 0;
		
		if (users == null || users.size() == 0) {
			
			addedObjects = new NewlyAddedObjects();
			addedObjects.setStatus(DriverSeatConstants.SUCCESS);
		}
		
		try {
			for (User user : users) {
				userId = user.getId();
				Call2_vod__c call = new Call2_vod__c();
				call.setAccount_vod__c(userId);
				call.setAdd_Detail_vod__c( productId + ";;EDetail_vod");
				call.setDetailed_Products_vod__c(session.getProductName());
				//String assoiciatekeyMessages = "a0DP0000004oiJ1MAI@@a00P0000002lJ2cIAE@@@@@@@@;;;a0DP000000550fZMAQ@@a00P0000002lJ2cIAE@@@@@@@@;;;a0DP000000550fFMAQ@@a00P0000002lJ2cIAE@@@@@@@@;;;a0DP000000550feMAA@@a00P0000002lJ2cIAE@@@@@@@@;;;a0DP000000550fPMAQ@@a00P0000002lJ2cIAE@@@@@@@@;;;a0DP000000550fUMAQ@@a00P0000002lJ2cIAE@@@@@@@@;;;a0DP000000550fKMAQ@@a00P0000002lJ2cIAE@@@@@@@@;;;a0DP000000550fjMAA@@a00P0000002lJ2cIAE@@@@@@@@;;;";
				call.setAdd_Key_Message_vod__c(session.getKeyMessages());
				call.setCall_Type_vod__c(DriverSeatConstants.DETAIL_ONLY);
				call.setLast_Device_vod__c(DriverSeatConstants.DEVICE_IPAD);
				call.setStatus_vod__c(DriverSeatConstants.SUBMITTED);
				call.setSubmitted_By_Mobile_vod__c(true);
				call.setAttendee_Type_vod__c(DriverSeatConstants.PERSON_ACCOUNT_VOD);
				call.setCall_Datetime_vod__c(DriiverSeatUtil.dateToCalendar(date));
				call.setDuration_vod__c(DriiverSeatUtil.minutesDiff(session.getsTime(), session.geteTime()));
				call.setCLM_vod__c(true);
				call.setMobile_Created_Datetime_vod__c(DriiverSeatUtil.dateToCalendar(date));
				call.setMobile_Last_Modified_Datetime_vod__c(DriiverSeatUtil.dateToCalendar(new Date()));
				call.setAttendees_vod__c(attendees);
				call.setParent_Call_vod__c(parrentCallId);
				//call.setCreatedById(repId);
							
				Call2_vod__c[] calls = new Call2_vod__c[1];
				calls[0] = call;
				
				addedObjects = proxy.postObject(calls);
				if (!addedObjects.getIds().isEmpty()) {
					
					if (DriverSeatConstants.SUCCESS.equals(addedObjects.getStatus())) {
						callId = addedObjects.getIds().get(0);
						if(parrentCallId == null) {
							parrentCallId = callId;
							attendees = 0;
						}
					} else {
						return addedObjects;
					}
				}
				//Call2_Key_Message_vod__c[] keyMessages = new Call2_Key_Message_vod__c[presentationactivities.size()];
				ArrayList<Call2_Key_Message_vod__c> keyMessages = new ArrayList<Call2_Key_Message_vod__c>();
				Call2_Key_Message_vod__c keyMessage;
				for (PresentationActivity activity : presentationactivities) {
					
					if(!DriverSeatConstants.EVENT_TYPE_PAGE.equals(activity.getEventType())) continue;
					
					keyMessage = new Call2_Key_Message_vod__c();
					keyMessage.setAccount_vod__c(userId);
					keyMessage.setCall2_vod__c(callId);
					keyMessage.setCall_Date_vod__c(DriiverSeatUtil.dateToCalendar(date));
					keyMessage.setDuration_vod__c(DriiverSeatUtil.minutesDiff(activity.getEventStartTime(), activity.getEventEndTime()));
					keyMessage.setProduct_vod__c(productId);
					//keyMessage.setName(activity.getHcpVRep());
					//keyMessage.setUser_vod__c(User_vod__c);
					keyMessage.setKey_Message_vod__c(activity.getPageID());		
					keyMessages.add(keyMessage);
				}
				
				addedObjects = proxy.postObject(keyMessages.toArray(new Call2_Key_Message_vod__c[keyMessages.size()]));
				if (!addedObjects.getIds().isEmpty()) {
									
					if (DriverSeatConstants.ERROR.equals(addedObjects.getStatus())) {						
						return addedObjects;
					}
				}
			}

			
			//keyMessage.setn
			//Key_Message_vod__c c = new Key_Message_vod__c();
			//c.getid;
						
			return addedObjects;
		} catch (Exception ce) {

			log.debug("postVeevaDomainAnalytics Exception" + ce);
			log.error(ce.getStackTrace());
			throw new ServiceException(ce);
		}
	}	
	
	
	private NewlyAddedObjects postDrivCustObjectAnalytics(Analytics activities) throws ServiceException {
		
		NewlyAddedObjects result = null;
		String callId = null;
		AnalyticSession session =   activities.getSession();
		List<PresentationActivity> presentationactivities =   activities.getPresentationActivity();
		List<AppActivity> appactivities = activities.getAppActivity();
		List<User> users = activities.getUsers();
		//User user = users.get(0);
		
		String sessionId = null;
		try {

			Drii_Analytic_Session__c[] driiSession = convertToDriiverSeatActivitySession(session);
			result = proxy.postObject(driiSession);
			if (!result.getIds().isEmpty()) {
				
				if (DriverSeatConstants.SUCCESS.equals(result.getStatus())) {
					sessionId = result.getIds().get(0);
				}
			}

			result = proxy.postAnalytics(session.getDriiUID(),
					convertToDriiverSeatAnalyticApp(appactivities, sessionId),
					convertToDriiverSeatPresentationActivity(presentationactivities, sessionId),
					convertToDriiverSeatHCP(users, session));

			return result;
		} catch (Exception ce) {

			log.debug("postDrivCustObjectAnalytics Exception" + ce);
			log.error(ce.getStackTrace());
			throw new ServiceException(ce);
		}
		
	}

	private Drii_Analytic_Session__c[] convertToDriiverSeatActivitySession(AnalyticSession session) {
		// TODO Auto-generated method stub

		Drii_Analytic_Session__c[] sessionAnalytics = new Drii_Analytic_Session__c[1];

		Drii_Analytic_Session__c data = new Drii_Analytic_Session__c();
		data.setDriiUID__c(session.getDriiUID());
		data.setApp_Name__c(session.getAppName());
		data.setApp_Version__c(session.getAppVersion());
		data.setBundle_Identifier__c(session.getBundleIdentifier());
		data.setDevice_Name__c(session.getDeviceName());
		data.setDevice_Version__c(session.getDeviceVersion());
		data.setClient__c(session.getClientName());
		data.setSTime__c(DriiverSeatUtil.dateToCalendar(session.getsTime()));
		data.setETime__c(DriiverSeatUtil.dateToCalendar(session.geteTime()));
		sessionAnalytics[0] = data;

		return sessionAnalytics;

	}

	private Drii_Presentation_Activity__c[] convertToDriiverSeatPresentationActivity(
			List<PresentationActivity> activities, String sessionId) {

		Drii_Presentation_Activity__c[] activitiesData = new Drii_Presentation_Activity__c[activities.size()];
		int index = 0;
		for (PresentationActivity activity : activities) {

			Drii_Presentation_Activity__c data = new Drii_Presentation_Activity__c();
			data.setSession__c(sessionId);
			data.setDriiUID__c(activity.getDriiUID());
			data.setPresentation_Name__c(activity.getPresentationName());
			data.setHCP_v_REP__c(activity.getHcpVRep());
			data.setPage_Name__c(activity.getPageName());
			data.setCategory__c(activity.getCategory());
			data.setAction__c(activity.getAction());
			data.setLabel__c(activity.getLabel());
			data.setEvent_Type__c(activity.getEventType());
			data.setEvent_sTime__c(DriiverSeatUtil.dateToCalendar(activity.getEventStartTime()));
			data.setEvent_eTime__c(DriiverSeatUtil.dateToCalendar(activity.getEventEndTime()));
			data.setDevice_Name__c(activity.getDeviceName());
			data.setPerformed_By__c(activity.getPerformedBy());

			activitiesData[index] = data;
			index++;
		}

		return activitiesData;
	}

	private Drii_App_Activity__c[] convertToDriiverSeatAnalyticApp(List<AppActivity> activities, String sessionId) {

		Drii_App_Activity__c[] activitiesData = new Drii_App_Activity__c[activities.size()];
		int index = 0;
		for (AppActivity activity : activities) {

			Drii_App_Activity__c data = new Drii_App_Activity__c();

			data.setSession__c(sessionId);
			data.setDriiUID__c(activity.getDriiUID());
			data.setAction__c(activity.getAction());
			data.setETime__c(DriiverSeatUtil.dateToCalendar(activity.geteTime()));
			data.setSTime__c(DriiverSeatUtil.dateToCalendar(activity.getsTime()));
			data.setLabel__c(activity.getLabel());

			activitiesData[index] = data;
			index++;
		}

		return activitiesData;
	}
	
	private Drii_HCPs__c[] convertToDriiverSeatHCP(List<User> users, AnalyticSession session) {

		Drii_HCPs__c[] hcps = new Drii_HCPs__c[users.size()];
		int index = 0;
		for (User user : users) {

			Drii_HCPs__c data = new Drii_HCPs__c();

			data.setDriiUID__c(session.getDriiUID());
			data.setHCP__c(user.getId());

			hcps[index] = data;
			index++;
		}

		return hcps;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thecdmgroup.driiverseatservice.service.AnalyticService#
	 * removeActivities(java.lang.String)
	 */
	@Override
	public List<ResponseObject> removeActivities(String query) throws ServiceException {

		List<ResponseObject> result = null;
		try {
			
			result = proxy.removeActivities(query);
			return result;
		} catch (Exception ce) {

			log.debug("postAppActivities Exception" + ce);
			log.error(ce.getStackTrace());
			throw new ServiceException(ce);
		}
	}

}
