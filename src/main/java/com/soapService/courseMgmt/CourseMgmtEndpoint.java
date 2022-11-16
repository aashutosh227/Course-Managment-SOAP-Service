package com.soapService.courseMgmt;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.coursemgmt.courses.CourseInfo;
import com.coursemgmt.courses.DeleteCourseInfoRequest;
import com.coursemgmt.courses.DeleteCourseInfoResponse;
import com.coursemgmt.courses.GetAllCourseInfoRequest;
import com.coursemgmt.courses.GetAllCourseInfoResponse;
import com.coursemgmt.courses.GetCourseInfoRequest;
import com.coursemgmt.courses.GetCourseInfoResponse;
import com.soapService.courseMgmt.Exception.CourseNotFoundException;
import com.soapService.courseMgmt.bean.Course;
import com.soapService.courseMgmt.service.CourseDetailsService;
import com.soapService.courseMgmt.service.CourseDetailsService.Status;


@Endpoint
public class CourseMgmtEndpoint {
	
	@Autowired
	private CourseDetailsService service;

	//Method
	//input - GetCourseInfoRequest
	//output - GetCourseInfoResponse
	
	@PayloadRoot(namespace="http://www.courseMgmt.com/courses", 
			localPart="GetCourseInfoRequest")
	@ResponsePayload
	public GetCourseInfoResponse 
	processRequest(@RequestPayload GetCourseInfoRequest request) {		
		Course course = service.findById(request.getId());
		
		if(course==null) {
			throw new CourseNotFoundException("Course with ID "+request.getId()+" not found");
		}
		return mapCourseInfo(course);
	}
	

	@PayloadRoot(namespace="http://www.courseMgmt.com/courses",
			localPart="GetAllCourseInfoRequest")
	@ResponsePayload
	public GetAllCourseInfoResponse 
	processRequest(@RequestPayload GetAllCourseInfoRequest request) {
		List<Course> courses = service.findAll();
		return mapCourseInfo(courses);
	}
	
	@PayloadRoot(namespace="http://www.courseMgmt.com/courses",
			localPart="DeleteCourseInfoRequest")
	@ResponsePayload
	public DeleteCourseInfoResponse 
		processRequest(@RequestPayload DeleteCourseInfoRequest request) {
		Status status = service.deleteById(request.getId());
		
		DeleteCourseInfoResponse response = new DeleteCourseInfoResponse();
		response.setStatus(mapStatus(status));
		return response;
	}
	
	public GetCourseInfoResponse mapCourseInfo(Course course) {
		GetCourseInfoResponse response = new GetCourseInfoResponse();

		response.setCourseInfo(mapCourse(course));
		
		return response;
	}
	
	public GetAllCourseInfoResponse mapCourseInfo(List<Course> courses) {
		GetAllCourseInfoResponse response = new GetAllCourseInfoResponse();

		List<CourseInfo> courseInfo = new ArrayList<CourseInfo>();
		courses.forEach((c)->courseInfo.add(mapCourse(c)));
		
		response.setCourseInfo(courseInfo);
		return response;
	}
	
	public CourseInfo mapCourse(Course course) {		
		CourseInfo courseInfo = new CourseInfo();
		courseInfo.setId(course.getId());
		courseInfo.setDescription(course.getDescription());
		courseInfo.setName(course.getName());
		
		return courseInfo;
	}
	
	public com.coursemgmt.courses.Status mapStatus(Status status) {
		if(status == Status.SUCCESS) {
			return com.coursemgmt.courses.Status.SUCCESS;
		}
		else {
			return com.coursemgmt.courses.Status.FAILURE;
		}
	}
	
}
