package com.soapService.courseMgmt;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.security.xwss.XwsSecurityInterceptor;
import org.springframework.ws.soap.security.xwss.callback.SimplePasswordValidationCallbackHandler;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

//Enable Spring Web services
@EnableWs
//Enable Spring Configuration
@Configuration
public class CourseMgmtServiceConfig extends WsConfigurerAdapter {
	
	@Autowired
	private ApplicationContext context;
	
	//MessageDispatcherServlet
		//ApplicationContext
	//URI = /ws/*
	
	@Bean
	ServletRegistrationBean messageDispatcherServlet(ApplicationContext context) {
		MessageDispatcherServlet messageDispatcherServlet= new MessageDispatcherServlet();
		messageDispatcherServlet.setApplicationContext(context);
		messageDispatcherServlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(messageDispatcherServlet,"/ws/*");
	}
	
	@Bean
	public XsdSchema courseSchema() {
		return new SimpleXsdSchema(new ClassPathResource("courseInfo.xsd"));
	}
	
	//ws/courses.wsdl
	//port
	//XSD schema
	@Bean(name="courses")
	public DefaultWsdl11Definition defaultWSDL11Definition(XsdSchema schema) {
		DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
		definition.setPortTypeName("CoursePort");
		definition.setTargetNamespace("http://www.courseMgmt.com/courses");
		definition.setSchema(schema);
		definition.setLocationUri("/ws");
		return definition;
	}
	
	@Bean(name="securityInterceptor")
	public XwsSecurityInterceptor securityInterceptor() {
		XwsSecurityInterceptor securityInterceptor = new XwsSecurityInterceptor();
		
		//Callback -> SimplePasswordValidationCallbackHandler
		securityInterceptor.setCallbackHandler(callBackHandler());
		//Setting the security policy
		securityInterceptor.setPolicyConfiguration(new ClassPathResource("security.xml"));
		return securityInterceptor;
	}
	
	@Bean(name="securityCallbackHandler")
	public SimplePasswordValidationCallbackHandler callBackHandler() {
		SimplePasswordValidationCallbackHandler handler =  new SimplePasswordValidationCallbackHandler();
		handler.setUsersMap(Collections.singletonMap("abc", "qwerty"));
		
		return handler;
	}
	
	@Override
	public void addInterceptors(List<EndpointInterceptor> interceptors) {
		interceptors.add((XwsSecurityInterceptor) context.getBean("securityInterceptor"));
	};
 
}
