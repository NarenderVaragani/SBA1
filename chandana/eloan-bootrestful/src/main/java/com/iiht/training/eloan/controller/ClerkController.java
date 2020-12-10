package com.iiht.training.eloan.controller;

import java.util.List;

import javax.validation.Valid;

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

import com.iiht.training.eloan.dto.LoanOutputDto;
import com.iiht.training.eloan.dto.ProcessingDto;
import com.iiht.training.eloan.dto.UserDto;
import com.iiht.training.eloan.dto.exception.ExceptionResponse;
import com.iiht.training.eloan.exception.AlreadyProcessedException;
import com.iiht.training.eloan.exception.ClerkNotFoundException;
import com.iiht.training.eloan.exception.LoanNotFoundException;
import com.iiht.training.eloan.service.ClerkService;

@RestController
@RequestMapping("/clerk")
public class ClerkController {
	
	@Autowired
	private ClerkService clerkService;
	
	@GetMapping("/all-applied")
	public ResponseEntity<List<LoanOutputDto>> allAppliedLoans() {
		List<LoanOutputDto> allAppliedLoans = this.clerkService.allAppliedLoans();
		ResponseEntity<List<LoanOutputDto>> response =
				new ResponseEntity<List<LoanOutputDto>>(allAppliedLoans, HttpStatus.OK);
		return response;
	}
	
	@PostMapping("/process/{clerkId}/{loanAppId}")
	public ResponseEntity<ProcessingDto> processLoan(@PathVariable Long clerkId,
													 @PathVariable Long loanAppId,
													@Valid @RequestBody ProcessingDto processingDto) {
		UserDto userDto = this.clerkService.fetchSingleClerk(clerkId);
		if(userDto == null) {
			// throw custom exception
			throw new ClerkNotFoundException("Clerk not found with the specified Id: "+ clerkId);
		}
		LoanOutputDto loanOutputDto = this.clerkService.fetchLoan(loanAppId);
		if(loanOutputDto == null) {
			// throw custom exception
			throw new LoanNotFoundException("Loan not found with the specified Loan AppId: "+loanAppId);
		}
		
		if((loanOutputDto.getStatus().equalsIgnoreCase("Processed"))) {
			// throw custom exception
			throw new AlreadyProcessedException("Loan has already been processed for the Loan Id: "+loanAppId);
		}
		
		else {
		//if found valid, process the Loan
		ProcessingDto processingLoanDto = this.clerkService.processLoan(clerkId, loanAppId, processingDto);
		ResponseEntity<ProcessingDto> response =
				new ResponseEntity<ProcessingDto>(processingLoanDto, HttpStatus.OK);
		return response;
		}
	}
	@ExceptionHandler(ClerkNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handler(ClerkNotFoundException ex){
		ExceptionResponse exception = 
				new ExceptionResponse(ex.getMessage(),
									  System.currentTimeMillis(),
									  HttpStatus.NOT_FOUND.value());
		ResponseEntity<ExceptionResponse> response =
				new ResponseEntity<ExceptionResponse>(exception, HttpStatus.NOT_FOUND);
		return response;
	}
	
	@ExceptionHandler(AlreadyProcessedException.class)
	public ResponseEntity<ExceptionResponse> handler(AlreadyProcessedException ex){
		ExceptionResponse exception = 
				new ExceptionResponse(ex.getMessage(),
									  System.currentTimeMillis(),
									  HttpStatus.BAD_REQUEST.value());
		ResponseEntity<ExceptionResponse> response =
				new ResponseEntity<ExceptionResponse>(exception, HttpStatus.BAD_REQUEST);
		return response;
	}
}
