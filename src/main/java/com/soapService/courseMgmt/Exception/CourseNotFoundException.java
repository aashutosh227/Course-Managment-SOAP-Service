package com.soapService.courseMgmt.Exception;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode=FaultCode.CUSTOM, 
			customFaultCode="{http://www.courseMgmt.com/courses}100_COURSE_NOT_FOUND")
public class CourseNotFoundException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CourseNotFoundException(String message) {
		super(message);
	}

}
