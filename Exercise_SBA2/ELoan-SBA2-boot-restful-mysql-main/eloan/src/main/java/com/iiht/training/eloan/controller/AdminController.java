package com.iiht.training.eloan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iiht.training.eloan.dto.UserDto;
import com.iiht.training.eloan.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;
	
	@PostMapping("/register-clerk")
	public ResponseEntity<UserDto> registerClerk(@RequestBody UserDto userDto){
		// get firstName, lastName, email, mobile and validate by rules
				String firstName = userDto.getFirstName();
				if (firstName == null || firstName.length() < 3 || firstName.length() > 100) {
					// Throw Invalid Data exception

				String lastName = userDto.getLastName();
				if (lastName == null || lastName.length() < 3 || lastName.length() > 100) {
						// Throw Invalid Data exception
					}
				String email = userDto.getEmail();
				if(email == null|| email.length()< 3 || email.length()>100 ){
					// Throw Invalid Data exception
				}

				String mobile = userDto.getMobile();
				if(mobile ==null ||mobile.length() <10 || mobile.length()>10){
					// Throw Invalid Data exception
				}
				}
				return null;
	}
	
	@PostMapping("/register-manager")
	public ResponseEntity<UserDto> registerManager(@RequestBody UserDto userDto){
		// get firstName, lastName, email, mobile and validate by rules
		String firstName = userDto.getFirstName();
		if (firstName == null || firstName.length() < 3 || firstName.length() > 100) {
			// Throw Invalid Data exception

		String lastName = userDto.getLastName();
		if (lastName == null || lastName.length() < 3 || lastName.length() > 100) {
				// Throw Invalid Data exception
			}
		String email = userDto.getEmail();
		if(email == null|| email.length()< 3 || email.length()>100 ){
			// Throw Invalid Data exception
		}

		String mobile = userDto.getMobile();
		if(mobile ==null ||mobile.length() <10 || mobile.length()>10){
			// Throw Invalid Data exception
		}
		}
		return null;
	}
	
	@GetMapping("/all-clerks")
	public ResponseEntity<List<UserDto>> getAllClerks(){
		return null;
	}
	
	@GetMapping("/all-managers")
	public ResponseEntity<List<UserDto>> getAllManagers(){
		return null;
	}
	
	
}
