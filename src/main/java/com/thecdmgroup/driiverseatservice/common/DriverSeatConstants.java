package com.thecdmgroup.driiverseatservice.common;

/**
 * @author OFS
 *
 */
public interface DriverSeatConstants {

	/** The Constant SESSION_ID. */
	public static final String SESSION_ID = "SESSION_ID";
	/** The Constant SESSION_ID. */
	public static final String REQUEST_ID = "REQUEST_ID";

	/** Start of Error Messages **/
	public static final String UPDATION_FAILED = "Unable to Update";
	/** End of Error Messages **/

	public static final String ENV_TEST = "TEST";
	public static final String ENV_PROD = "PROD";
	
	/** Properties **/
	public static final String VEEVA_WS_URL_TEST = "veeva.ws.url.test";
	public static final String VEEVA_WS_URL_PROD = "veeva.ws.url.prod";	
	
	/** INFORMATION **/
	public static final String SESSION = "Drii_Analytic_Session__c";
	public static final String APPACTIVITY = "Drii_App_Activity__c";
	public static final String PRESENTATION = "Drii_Presentation_Activity__c";
	public static final String HCPS = "Drii_HCPs__c";

	/** Status **/
	public static final String SUCCESS = "Success";
	public static final String ERROR = "Error";
	
	/** Veeva-Slaesforce Const  **/
	public static final String DETAIL_ONLY = "Detail Only";
	public static final String DEVICE_IPAD = "iPad";
	public static final String SUBMITTED = "Submitted";
	public static final String PERSON_ACCOUNT_VOD = "Person_Account_vod";
	public static final String EVENT_TYPE_PAGE = "PageEvent";
}
