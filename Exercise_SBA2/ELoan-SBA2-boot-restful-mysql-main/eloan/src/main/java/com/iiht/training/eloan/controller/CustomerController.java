package com.iiht.training.eloan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iiht.training.eloan.dto.LoanDto;
import com.iiht.training.eloan.dto.LoanOutputDto;
import com.iiht.training.eloan.dto.UserDto;
import com.iiht.training.eloan.dto.exception.ExceptionResponse;
import com.iiht.training.eloan.exception.CustomerNotFoundException;
import com.iiht.training.eloan.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@PostMapping("/register")
	public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) {
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

	@PostMapping("/apply-loan/{customerId}")
	public ResponseEntity<LoanOutputDto> applyLoan(@PathVariable Long customerId, @RequestBody LoanDto loanDto) {
		// Check customerid exist in DB, if not throw exception - Will do after
		// DB implementation
		// validate loanName, loanAmount as above
		String loanName =loanDto.getLoanName();
		if(loanName==null ||loanName.length()<3 ||loanName.length()>100){
			// Throw Invalid Data exception
		}
		Double loanAmount=loanDto.getLoanAmount();
		if(loanAmount==null ||loanAmount==0){
			// Throw Invalid Data exception
		}
		return null;
	}

	@GetMapping("/loan-status/{loanAppId}")
	public ResponseEntity<LoanOutputDto> getStatus(@PathVariable Long loanAppId) {
		//
		return null;
	}

	@GetMapping("/loan-status-all/{customerId}")
	public ResponseEntity<List<LoanOutputDto>> getStatusAll(@PathVariable Long customerId) {
		return null;
	}

	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handler(CustomerNotFoundException ex) {
		ExceptionResponse exception = new ExceptionResponse(ex.getMessage(), System.currentTimeMillis(),
				HttpStatus.NOT_FOUND.value());
		ResponseEntity<ExceptionResponse> response = new ResponseEntity<ExceptionResponse>(exception,
				HttpStatus.NOT_FOUND);
		return response;
	}
}
