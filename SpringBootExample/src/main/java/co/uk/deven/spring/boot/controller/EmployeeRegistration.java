package co.uk.deven.spring.boot.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import co.uk.deven.spring.boot.model.User;

@RestController
public class EmployeeRegistration {
	@RequestMapping(method = RequestMethod.POST, value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
	public void registerEmployee(@RequestBody User user) {
		System.out.println("Hello World! " + user.getLastName());
	}

	@ExceptionHandler
	void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.BAD_REQUEST.value());

	}
}
