/**
 * 
 */
package com.thecdmgroup.driiverseatservice.serviceimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.sforce.soap.enterprise.sobject.Key_Message_vod__c;
import com.sforce.soap.enterprise.sobject.Product_vod__c;
import com.sforce.soap.enterprise.sobject.SObject;
import com.thecdmgroup.driiverseatservice.common.ServiceException;
import com.thecdmgroup.driiverseatservice.domain.VeevaiDetailMetaData;
import com.thecdmgroup.driiverseatservice.service.VeevaiDetailService;
import com.thecdmgroup.driiverseatservice.transport.jaxws.SalesforceProxyImpl;

/**
 * @author vinothn
 *
 */
public class VeevaiDetailServiceImpl implements VeevaiDetailService {

	private static final Logger log = LogManager.getLogger(VeevaiDetailServiceImpl.class);
	
	@Autowired
	private SalesforceProxyImpl proxy;
	
	@Override
	public VeevaiDetailMetaData getVeevaiDetailMetaData(String iDetatilName) throws ServiceException {
		// TODO Auto-generated method stub
		VeevaiDetailMetaData metaData = new VeevaiDetailMetaData(); 
		List<SObject> sObjects = null;
		try {

			String productQuery = "SELECT Id, Name, Description_vod__c FROM Product_vod__c where Name='"+ iDetatilName +"'";
			//String productQuery = "SELECT Id, Name, Description_vod__c FROM Product_vod__c where Name='Unbranded'";

			sObjects = proxy.getSalesforceObject(productQuery);
			Product_vod__c product = (Product_vod__c) sObjects.get(0);
			metaData.setProductId(product.getId());
			metaData.setProductName(product.getName());
			metaData.setDescription(product.getDescription_vod__c());
			
			String soqlQuery = "SELECT Id, Name, CLM_ID_vod__c, Product_vod__c FROM Key_Message_vod__c where Product_vod__c = '"+ product.getId() + "'";
			sObjects = proxy.getSalesforceObject(soqlQuery);
			Map<String, String> keyMessages = new HashMap<String, String>();
			for (SObject sObject : sObjects) {	
				Key_Message_vod__c keyMessage = (Key_Message_vod__c) sObject;
				keyMessages.put(keyMessage.getName(), keyMessage.getId());
			}
			metaData.setKeyMessages(keyMessages);
			
			return metaData;
		} catch (Exception ce) {

			log.debug("postPresentationActivities Exception" + ce);
			log.error(ce.getStackTrace());
			throw new ServiceException(ce);
		}
		
	}

}
