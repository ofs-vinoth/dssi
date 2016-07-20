/**
 * 
 */
package com.thecdmgroup.driiverseatservice.transport.jaxws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import com.sforce.soap.enterprise.Connector;
import com.sforce.soap.enterprise.DeleteResult;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.Error;
import com.sforce.soap.enterprise.GetUserInfoResult;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.sobject.Contact;
import com.sforce.soap.enterprise.sobject.Drii_App_Activity__c;
import com.sforce.soap.enterprise.sobject.Drii_HCPs__c;
import com.sforce.soap.enterprise.sobject.Drii_Presentation_Activity__c;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import com.thecdmgroup.driiverseatservice.common.DriverSeatConstants;
import com.thecdmgroup.driiverseatservice.common.ExceptionInfo;
import com.thecdmgroup.driiverseatservice.common.ServiceException;
import com.thecdmgroup.driiverseatservice.domain.NewlyAddedObjects;
import com.thecdmgroup.driiverseatservice.domain.ResponseObject;
import com.thecdmgroup.driiverseatservice.domain.User;

/**
 * @author vinothn
 *
 */
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SalesforceProxyImpl {

	static final long serialVersionUID = -70348971907439L;

	private static final Logger log = LogManager.getLogger(SalesforceProxyImpl.class);

	private EnterpriseConnection connection;

	/**
	 * @return
	 */
	public boolean createConection(String username, String password, String veevaWSURL) throws ConnectionException {

		try {

			ConnectorConfig config = new ConnectorConfig();
			config.setAuthEndpoint(veevaWSURL);
			config.setUsername(username);
			config.setPassword(password);

			connection = Connector.newConnection(config);
			return true;

		} catch (ConnectionException ce) {

			log.debug("Connection Exception" + ce);
			throw ce;
		}
	}

	public boolean closeConnection() throws ConnectionException {

		try {
			connection.logout();
			log.debug("User Logged ut :");
			return true;
		} catch (ConnectionException ce) {
			log.debug("Connection Exception" + ce);
			throw ce;
		}
	}

	/**
	 * @return
	 */
	public User getLoggedInUser() throws ConnectionException {

		User user = null;
		try {

			GetUserInfoResult userInfo = connection.getUserInfo();
			user = new User();
			log.debug(
					"USER Logged In :" + "UserID: " + userInfo.getUserId() + " User Email: " + userInfo.getUserEmail());
			user.setEmail(userInfo.getUserEmail());
			user.setUsername(userInfo.getUserId());
			user.setFullName(userInfo.getUserFullName());
			user.setId(userInfo.getUserId());

		} catch (ConnectionException ce) {

			log.debug("login Exception" + ce);
			throw ce;
		}

		return user;
	}

	/**
	 * @return
	 * @throws ConnectionException
	 */
	public List<User> getAssoicateHCPs() throws ConnectionException {

		List<User> hcps = new ArrayList<User>();

		QueryResult qResult = null;
		try {
			String soqlQuery = "SELECT AccountId, FirstName, LastName, Email FROM Contact";
			qResult = connection.query(soqlQuery);
			boolean done = false;
			if (qResult.getSize() > 0) {
				System.out.println("Logged-in user can see a total of " + qResult.getSize() + " contact records.");
				while (!done) {

					SObject[] records = qResult.getRecords();
					User user = null;
					for (int i = 0; i < records.length; ++i) {

						Contact con = (Contact) records[i];
						//con.getAffiliation_vod1__r();
						 //con.getMailingAddress();
						user = new User();
						user.setFullName(con.getFirstName() + " " + con.getLastName());
						user.setEmail(con.getEmail());
						user.setId(con.getAccountId());
						hcps.add(user);
					}
					if (qResult.isDone()) {
						done = true;
						log.debug("Contacts :" + hcps.toString());
					} else {
						qResult = connection.queryMore(qResult.getQueryLocator());
					}
				}
			} else {
				System.out.println("No records found.");
			}
			System.out.println("\nQuery succesfully executed.");
		} catch (ConnectionException ce) {
			log.debug("Connection Exception" + ce);
			throw ce;
		}

		// TODO Auto-generated method stub
		return hcps;
	}
	
	/**
	 * @return
	 * @throws ConnectionException
	 */
	public List<SObject> getSalesforceObject(String soqlQuery) throws ConnectionException {

		List<SObject> sObjects = new ArrayList<SObject>();

		QueryResult qResult = null;
		try {
			qResult = connection.query(soqlQuery);
			boolean done = false;
			if (qResult.getSize() > 0) {
				System.out.println("Logged-in user can see a total of " + qResult.getSize() + " contact records.");
				while (!done) {

					SObject[] records = qResult.getRecords();
					User user = null;
					for (int i = 0; i < records.length; ++i) {

						SObject object = (SObject) records[i];						
						sObjects.add(object);
					}
					if (qResult.isDone()) {
						done = true;
						log.debug("Contacts :" + sObjects.toString());
					} else {
						qResult = connection.queryMore(qResult.getQueryLocator());
					}
				}
			} else {
				System.out.println("No records found.");
			}
			System.out.println("\nQuery succesfully executed.");
		} catch (ConnectionException ce) {
			log.debug("Connection Exception" + ce);
			throw ce;
		}

		// TODO Auto-generated method stub
		return sObjects;
	}

	/**
	 * @param sObjects
	 * @return
	 * @throws ConnectionException
	 */
	public NewlyAddedObjects postObject(SObject[] sObjects) throws Exception {

		//List<ResponseObject> responseList = new ArrayList<ResponseObject>();
		NewlyAddedObjects addedObjects = new NewlyAddedObjects(); 

		try {
			SaveResult[] results = bulkCreate(sObjects);

			processPostActivies(addedObjects, results, sObjects[0].getClass().getName());
			if (DriverSeatConstants.ERROR.equals(addedObjects.getStatus())) {
				bulkDelete(addedObjects.getIdsAsArray());
			}
			
			return addedObjects;

		} catch (ConnectionException ce) {

			log.debug("Connection Exception" + ce);
			throw ce;
		}

	}

	/**
	 * @param sObjects
	 * @return
	 * @throws ConnectionException
	 */

	public NewlyAddedObjects postAnalytics(String driiUID, Drii_App_Activity__c[] appActivity,
			Drii_Presentation_Activity__c[] appPresentation, Drii_HCPs__c[] hcPs) throws Exception {

		//List<ResponseObject> responseList = new ArrayList<ResponseObject>();
		NewlyAddedObjects addedObjects = new NewlyAddedObjects();
		String driiUid = driiUID;

		try {
			SaveResult[] results = bulkCreate(appActivity);
			if (processPostActivies(addedObjects, results, DriverSeatConstants.APPACTIVITY)) {

				results = bulkCreate(appPresentation);
				if (processPostActivies(addedObjects, results, DriverSeatConstants.PRESENTATION)) {
					
					results = bulkCreate(hcPs);
					if (processPostActivies(addedObjects, results, DriverSeatConstants.HCPS)) {
						
						return addedObjects;
					} else {
						deleteActivities(DriverSeatConstants.HCPS, driiUid);
					}
				} else {
					
					deleteActivities(DriverSeatConstants.PRESENTATION, driiUid);
				}

			} else {

				deleteActivities(DriverSeatConstants.APPACTIVITY, driiUid);
			}

			return addedObjects;

		} catch (ConnectionException ce) {

			deleteActivities(DriverSeatConstants.SESSION, driiUid);
			addedObjects = null;
			log.debug("Connection Exception" + ce);
			throw ce;		
		}

	}

	/**
	 * @param query
	 * @return
	 * @throws ServiceException
	 */
	public List<ResponseObject> removeActivities(String query) throws Exception {

		try {

			SObject object = null;
			List<ResponseObject> responseList = new ArrayList<ResponseObject>();
			QueryResult queryResults = connection.query(query);

			if (queryResults.getSize() > 0) {
				String[] ids = new String[queryResults.getRecords().length];
				for (int i = 0; i < queryResults.getRecords().length; i++) {
					// cast the SObject to a strongly-typed Lead
					object = (SObject) queryResults.getRecords()[i];
					// add the Lead Id to the array to be deleted
					ids[i] = object.getId();
					log.debug("Deleting Id: " + object.getId());
				}
				// delete the records in Salesforce.com by passing an array of
				// Ids
				DeleteResult[] deleteResults = bulkDelete(ids);
				responseList = processPostActivies(deleteResults);
				
			}

			return responseList;
		} catch (Exception ce) {

			log.debug("postAppActivities Exception" + ce);
			throw ce;
		}
	}

	/**
	 * @param results
	 *            of type SaveResult[]
	 * @return responseList
	 * 
	 */
	public boolean processPostActivies(NewlyAddedObjects addedObjects, SaveResult[] results, String description) {

		boolean isSuccess = true;
		if (results.length > 0) {

			System.out.println("Logged-in user can see a total of " + results.length + " contact records.");
			//ResponseObject activityResult = null;

			for (int i = 0; i < results.length; ++i) {

				SaveResult result = results[i];
				//activityResult = new ResponseObject();
				//activityResult.setId(result.getId());
				addedObjects.addId(result.getId());
				if (!DriverSeatConstants.ERROR.equals(addedObjects.getStatus())) 
					addedObjects.setStatus(result.getSuccess() ? DriverSeatConstants.SUCCESS : DriverSeatConstants.ERROR);
				//activityResult.setDescription(description);

				if (!result.getSuccess()) {
					isSuccess = false;
					Status status = Status.INTERNAL_SERVER_ERROR;
					Error[] error = result.getErrors();
					//List<ExceptionInfo> errorList = null;
					for (int err = 0; err < error.length; err++) {
						addedObjects.setErrors(error[err].getMessage());
						//errorList = new ArrayList<ExceptionInfo>();
						//String fields = Arrays.asList(error[err].getFields()).toString();
						//ExceptionInfo e = new ExceptionInfo(status.getStatusCode(), DriverSeatConstants.UPDATION_FAILED,
								//error[err].getMessage() + fields);
						//errorList.add(e);
					}
					//activityResult.setErrors(errorList);
				}
				
				log.debug("result Success " + result.getId() + ": " + result.getSuccess());
				log.debug("result Error " + result.getId() + ": " + result.getErrors());
			}
		}
		return isSuccess;
	}

	/**
	 * TODO: Need to delete operation by NewlyAddedObjects ids
	 * 
	 * @param tableName
	 * @param driiUId
	 * @return
	 * @throws Exception
	 */
	private List<ResponseObject> deleteActivities(String tableName, String driiUId) throws Exception {

		try {

			switch (tableName) {
			
			case DriverSeatConstants.HCPS:

				removeActivities(
						"SELECT ID FROM " + DriverSeatConstants.HCPS + " WHERE DriiUID__c = '" + driiUId + "'");
				
			case DriverSeatConstants.PRESENTATION:

				removeActivities(
						"SELECT ID FROM " + DriverSeatConstants.PRESENTATION + " WHERE DriiUID__c = '" + driiUId + "'");

				
			case DriverSeatConstants.APPACTIVITY:

				removeActivities(
						"SELECT ID FROM " + DriverSeatConstants.APPACTIVITY + " WHERE DriiUID__c = '" + driiUId + "'");
				
			case DriverSeatConstants.SESSION:

				removeActivities(
						"SELECT ID FROM " + DriverSeatConstants.SESSION + " WHERE DriiUID__c = '" + driiUId + "'");
				
			}
			

		} catch (Exception ce) {
			log.debug("postAppActivities Exception" + ce);
			log.error(ce.getStackTrace());
			throw ce;
		}

		return null;
	}

	/**
	 * @param results
	 *            of type SaveResult[]
	 * @return responseList
	 * 
	 */
	public List<ResponseObject> processPostActivies(DeleteResult[] results) {

		List<ResponseObject> responseList = new ArrayList<ResponseObject>();

		if (results.length > 0) {

			System.out.println("Logged-in user can see a total of " + results.length + " contact records.");
			ResponseObject activityResult = null;
			for (int i = 0; i < results.length; ++i) {

				DeleteResult result = results[i];
				activityResult = new ResponseObject();
				activityResult.setId(result.getId());
				activityResult.setStatus(result.getSuccess() ? "Success" : "Error");

				if (!result.getSuccess()) {

					Status status = Status.INTERNAL_SERVER_ERROR;
					Error[] error = result.getErrors();
					List<ExceptionInfo> errorList = null;
					for (int err = 0; err < error.length; err++) {

						errorList = new ArrayList<ExceptionInfo>();
						String fields = Arrays.asList(error[err].getFields()).toString();
						ExceptionInfo e = new ExceptionInfo(status.getStatusCode(), DriverSeatConstants.UPDATION_FAILED,
								error[err].getMessage() + fields);
						errorList.add(e);
					}
					activityResult.setErrors(errorList);
				}
				responseList.add(activityResult);

				log.debug("result Success " + result.getId() + ": " + result.getSuccess());
				log.debug("result Error " + result.getId() + ": " + result.getErrors());
			}
		}
		return responseList;
	}
	
	private SaveResult[] bulkCreate(SObject[] bulkSObjects) throws Exception {
		
		SaveResult[] result = null;
		
		try {
			List<SaveResult> responseResult = new ArrayList<SaveResult>();
			for (int index = 0; index < bulkSObjects.length; index += 200) {
				
				int endRange = index + 200;
				if (bulkSObjects.length < endRange) endRange = bulkSObjects.length;
				
				SObject[] sObjects = Arrays.copyOfRange(bulkSObjects, index, endRange );
				SaveResult[] results = connection.create(sObjects);
				responseResult.addAll(Arrays.asList(results));
			}
			
			result = responseResult.toArray(new SaveResult[responseResult.size()]);
			
		} catch (ConnectionException ce) {
			log.error("Connection Exception" + ce);
			throw ce;
		} catch(Exception ce) {
			log.error("UnKnown Exception" + ce);
			throw ce;
		}
		
		return result;
	}
	
	
	public DeleteResult[] bulkDelete(String[] ids) throws Exception {
		
		DeleteResult[] result = null;
		
		try {
			List<DeleteResult> responseResult = new ArrayList<DeleteResult>();
			for (int index = 0; index < ids.length; index += 200) {
				
				int endRange = index + 200;
				if (ids.length < endRange) endRange = ids.length;
				
				String[] sObjects = Arrays.copyOfRange(ids, index, endRange );
				DeleteResult[] results = connection.delete(sObjects);
				responseResult.addAll(Arrays.asList(results));
			}
			
			result = responseResult.toArray(new DeleteResult[responseResult.size()]);
			
		} catch (ConnectionException ce) {
			log.error("Connection Exception" + ce);
			throw ce;
		} catch(Exception ce) {
			log.error("UnKnown Exception" + ce);
			throw ce;
		}
		
		return result;
	}
}
