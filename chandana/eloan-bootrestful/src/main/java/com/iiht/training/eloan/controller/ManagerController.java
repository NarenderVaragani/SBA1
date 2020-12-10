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
import com.iiht.training.eloan.dto.RejectDto;
import com.iiht.training.eloan.dto.SanctionDto;
import com.iiht.training.eloan.dto.SanctionOutputDto;
import com.iiht.training.eloan.dto.UserDto;
import com.iiht.training.eloan.dto.exception.ExceptionResponse;
import com.iiht.training.eloan.exception.AlreadyFinalizedException;
import com.iiht.training.eloan.exception.LoanNotFoundException;
import com.iiht.training.eloan.exception.ManagerNotFoundException;
import com.iiht.training.eloan.service.ManagerService;

@RestController
@RequestMapping("/manager")
public class ManagerController {

	@Autowired
	private ManagerService managerService;
	
	@GetMapping("/all-processed")
	public ResponseEntity<List<LoanOutputDto>> allProcessedLoans() {
		List<LoanOutputDto> allProcessedLoans = this.managerService.allProcessedLoans();
		ResponseEntity<List<LoanOutputDto>> response =
				new ResponseEntity<List<LoanOutputDto>>(allProcessedLoans, HttpStatus.OK);
		return response;
	}
	
	@PostMapping("/reject-loan/{managerId}/{loanAppId}")
	public ResponseEntity<RejectDto> rejectLoan(@PathVariable Long managerId,
												@PathVariable Long loanAppId,
												@Valid @RequestBody RejectDto rejectDto){
		UserDto userDto = this.managerService.fetchSingleManager(managerId);
		if(userDto == null) {
			// throw custom exception
			throw new ManagerNotFoundException("Manager not found with the specified Id: "+ managerId);
		}
		LoanOutputDto loanOutputDto = this.managerService.fetchLoan(loanAppId);
		if(loanOutputDto == null) {
			// throw custom exception
			throw new LoanNotFoundException("Loan not found with the specified Loan AppId: "+loanAppId);
		}
		
		if((loanOutputDto.getStatus().equalsIgnoreCase("Rejected")) || (loanOutputDto.getStatus().equalsIgnoreCase("Sanctioned"))) {
			// throw custom exception
			throw new AlreadyFinalizedException("Loan has already been Finalized for the Loan Id: "+loanAppId);
		}
		
		else {
		//reject the Loan
		RejectDto rejectLoanDto = this.managerService.rejectLoan(managerId, loanAppId, rejectDto);
		ResponseEntity<RejectDto> response =
				new ResponseEntity<RejectDto>(rejectLoanDto, HttpStatus.OK);
		return response;
		}
	}
	
	@PostMapping("/sanction-loan/{managerId}/{loanAppId}")
	public ResponseEntity<SanctionOutputDto> sanctionLoan(@PathVariable Long managerId,
												@PathVariable Long loanAppId,
												@Valid @RequestBody SanctionDto sanctionDto) throws Exception{
		UserDto userDto = this.managerService.fetchSingleManager(managerId);
		if(userDto == null) {
			// throw custom exception
			throw new ManagerNotFoundException("Manager not found with the specified Id: "+ managerId);
		}
		LoanOutputDto loanOutputDto = this.managerService.fetchLoan(loanAppId);
		if(loanOutputDto == null) {
			// throw custom exception
			throw new LoanNotFoundException("Loan not found with the specified Loan AppId: "+loanAppId);
		}
		
		if((loanOutputDto.getStatus().equalsIgnoreCase("Sanctioned")) || (loanOutputDto.getStatus().equalsIgnoreCase("Rejected"))) {
			// throw custom exception
			throw new AlreadyFinalizedException("Loan has already been Finalized for the Loan Id: "+loanAppId);
		}
		
		else {
		//sanction the Loan
		SanctionOutputDto sanctionOutputDto = this.managerService.sanctionLoan(managerId, loanAppId, sanctionDto);
		ResponseEntity<SanctionOutputDto> response =
				new ResponseEntity<SanctionOutputDto>(sanctionOutputDto, HttpStatus.OK);
		return response;
		}
	}
	
	@ExceptionHandler(ManagerNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handler(ManagerNotFoundException ex){
		ExceptionResponse exception = 
				new ExceptionResponse(ex.getMessage(),
									  System.currentTimeMillis(),
									  HttpStatus.NOT_FOUND.value());
		ResponseEntity<ExceptionResponse> response =
				new ResponseEntity<ExceptionResponse>(exception, HttpStatus.NOT_FOUND);
		return response;
	}
	
	@ExceptionHandler(AlreadyFinalizedException.class)
	public ResponseEntity<ExceptionResponse> handler(AlreadyFinalizedException ex){
		ExceptionResponse exception = 
				new ExceptionResponse(ex.getMessage(),
									  System.currentTimeMillis(),
									  HttpStatus.BAD_REQUEST.value());
		ResponseEntity<ExceptionResponse> response =
				new ResponseEntity<ExceptionResponse>(exception, HttpStatus.BAD_REQUEST);
		return response;
	}
}
