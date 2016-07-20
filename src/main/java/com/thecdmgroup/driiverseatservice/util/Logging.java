package com.thecdmgroup.driiverseatservice.util;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;

public class Logging {
	private static final Logger log = LogManager.getLogger(Logging.class);
	/** 
	* This is the method which I would like to execute
	* before a selected method execution.
	*/
	public void beforeAdvice(JoinPoint joinPoint){
		//System.out.println("Requesting Service.");
		/*log.debug("//////////////////////Requesting Service////////////////// \n" + 
				  "Class : " + joinPoint.getSignature().getDeclaringTypeName() + 
				  " > Method :" + joinPoint.getSignature().getName() +
				  "Args :" + Arrays.toString(joinPoint.getArgs()));*/
	}
	
	/** 
	* This is the method which I would like to execute
	* after a selected method execution.
	*/
	public void afterAdvice(){
		//System.out.println("Student profile has been setup.");
	}
	
	/** 
	* This is the method which I would like to execute
	* when any method returns.
	*/
	public void afterReturningAdvice(Object retVal){
		//System.out.println("Returning:" + retVal.toString() );
		log.debug("Returning:" + retVal.toString() );
	}
	
	/**
	* This is the method which I would like to execute
	* if there is an exception raised.
	*/
	public void AfterThrowingAdvice(JoinPoint joinPoint, Exception ex) throws Throwable {
		//System.out.println("There has been an exception: " + ex.toString());
		log.debug("\n \t\t\t //////////////////////start Service Exception////////////////// \n" + 
				  "\n Class : " + joinPoint.getSignature().getDeclaringTypeName() + 
				  " > Method :" + joinPoint.getSignature().getName() +
				  "Args :" + Arrays.toString(joinPoint.getArgs()));
		log.debug("\n There has been an exception: " + ex.toString()); 
		log.debug("\n \t\t\t //////////////////////end Service Exception////////////////// \n"); 
	}
}
