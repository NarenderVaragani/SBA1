package com.iiht.training.eloan.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iiht.training.eloan.dto.LoanDto;
import com.iiht.training.eloan.dto.LoanOutputDto;
import com.iiht.training.eloan.dto.ProcessingDto;
import com.iiht.training.eloan.dto.UserDto;
import com.iiht.training.eloan.entity.Loan;
import com.iiht.training.eloan.entity.ProcessingInfo;
import com.iiht.training.eloan.entity.Users;
import com.iiht.training.eloan.exception.ClerkNotFoundException;
import com.iiht.training.eloan.repository.LoanRepository;
import com.iiht.training.eloan.repository.ProcessingInfoRepository;
import com.iiht.training.eloan.repository.SanctionInfoRepository;
import com.iiht.training.eloan.repository.UsersRepository;
import com.iiht.training.eloan.service.ClerkService;

@Service
public class ClerkServiceImpl implements ClerkService {

	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private LoanRepository loanRepository;
	
	@Autowired
	private ProcessingInfoRepository processingInfoRepository;
	
	@Autowired
	private SanctionInfoRepository sanctionInfoRepository;
	
	@Override
	public List<LoanOutputDto> allAppliedLoans() {
		List<Loan> loans = this.loanRepository.findAllByStatus(0);
		List<LoanOutputDto> loanDtos = loans
										.stream()
										.map(this :: convertLoanEntityToOutputDto)
										.collect(Collectors.toList());
		return loanDtos;
	}

	@Override
	public ProcessingDto processLoan(Long clerkId, Long loanAppId, ProcessingDto processingDto) {
		
		ProcessingInfo processLoan = this.convertProcessInputDtoToEntity(clerkId, loanAppId, processingDto);
		ProcessingInfo processedLoan = this.processingInfoRepository.save(processLoan);
		ProcessingDto processedDto = this.convertProcessEntityToOutputDto(processedLoan);
		
		//Update Loan status to Processed
		Loan loan = this.loanRepository.findById(loanAppId).orElse(null);
		loan.setStatus(1);
		Loan newLoan = this.loanRepository.save(loan);
		// convert entity into dto
		//LoanOutputDto loanOutputDto = this.convertLoanEntityToOutputDto(newLoan);
		return processedDto;
	}
	
	@Override
	public UserDto fetchSingleClerk(Long clerkId) {
		if(this.usersRepository.existsById(clerkId)) {
			Users user = this.usersRepository.findByIdAndRole(clerkId, "Loan Clerk");
			if(user == null) {
				// throw custom exception
				throw new ClerkNotFoundException("Clerk not found with the specified Id: "+ clerkId);
			}
			UserDto userDto = this.convertUserEntityToOutputDto(user);
			return userDto;
		}
		return null;
	}

	@Override
	public LoanOutputDto fetchLoan(Long loanAppId) {
		if(this.loanRepository.existsById(loanAppId)) {
			Loan loan = this.loanRepository.findById(loanAppId).orElse(null);
			//get User Details tagged to the loanAppId
			Long customerId = loan.getCustomerId();
			Users user = this.usersRepository.findById(customerId).orElse(null);
			UserDto userDto = this.convertUserEntityToOutputDto(user);
			
			LoanOutputDto loanOutputDto = this.convertLoanEntityToOutputDto(loan);
			loanOutputDto.setUserDto(userDto);
			return loanOutputDto;
		}
		return null;
	}
	
	@Override
	public LoanOutputDto updateLoanStatus(Long loanAppId) {
		Loan loan = this.loanRepository.findById(loanAppId).orElse(null);
		loan.setStatus(1);
		LoanOutputDto loanOutputDto = this.convertLoanEntityToOutputDto(loan);
		return loanOutputDto;
	}
	
	//utility methods
	private LoanOutputDto convertLoanEntityToOutputDto(Loan loan) {
		//get Newly saved loan details into LoanDto object
		LoanDto newLoan = new LoanDto();
		newLoan.setLoanName(loan.getLoanName());
		newLoan.setLoanAmount(loan.getLoanAmount());
		newLoan.setLoanApplicationDate(loan.getLoanApplicationDate());
		newLoan.setBusinessStructure(loan.getBusinessStructure());
		newLoan.setBillingIndicator(loan.getBillingIndicator());
		newLoan.setTaxIndicator(loan.getTaxIndicator());
		
		//get User Details tagged to the customer id
		Users user = this.usersRepository.findById(loan.getCustomerId()).orElse(null);
		UserDto userDto = this.convertUserEntityToOutputDto(user);
		
		//convert loan entity to output
		LoanOutputDto loanOutputDto = new LoanOutputDto();
		loanOutputDto.setLoanAppId(loan.getId());
		loanOutputDto.setCustomerId(loan.getCustomerId());
			
		
		int loanStatusCode=loan.getStatus();
		if(loanStatusCode==0)
			loanOutputDto.setStatus("Applied");
		if(loanStatusCode==1)
			loanOutputDto.setStatus("Processed");
		if(loanStatusCode==2)
			loanOutputDto.setStatus("Sanctioned");
		if(loanStatusCode==-1)
			loanOutputDto.setStatus("Rejected");
		
		loanOutputDto.setLoanDto(newLoan);
		loanOutputDto.setUserDto(userDto);
		return loanOutputDto;
	}
	
	private UserDto convertUserEntityToOutputDto(Users user) {
		UserDto userOutputDto = new UserDto();
		userOutputDto.setId(user.getId());
		userOutputDto.setFirstName(user.getFirstName());
		userOutputDto.setLastName(user.getLastName());
		userOutputDto.setEmail(user.getEmail());
		userOutputDto.setMobile(user.getMobile());
		userOutputDto.setRole(user.getRole());
		return userOutputDto;
	}
	
	private ProcessingInfo convertProcessInputDtoToEntity(Long clerkId, Long loanAppId, ProcessingDto processingDto) {
		ProcessingInfo process = new ProcessingInfo();
		process.setLoanAppId(loanAppId);
		process.setLoanClerkId(clerkId);
		process.setAcresOfLand(processingDto.getAcresOfLand());
		process.setLandValue(processingDto.getLandValue());
		process.setAppraisedBy(processingDto.getAppraisedBy());
		process.setValuationDate(processingDto.getValuationDate());
		process.setAddressOfProperty(processingDto.getAddressOfProperty());
		process.setSuggestedAmountOfLoan(processingDto.getSuggestedAmountOfLoan());
		return process;			
	}
	
	private ProcessingDto convertProcessEntityToOutputDto(ProcessingInfo processedLoan) {
		ProcessingDto processOutputDto = new ProcessingDto();
		processOutputDto.setAcresOfLand(processedLoan.getAcresOfLand());
		processOutputDto.setLandValue(processedLoan.getLandValue());
		processOutputDto.setAppraisedBy(processedLoan.getAppraisedBy());
		processOutputDto.setValuationDate(processedLoan.getValuationDate());
		processOutputDto.setAddressOfProperty(processedLoan.getAddressOfProperty());
		processOutputDto.setSuggestedAmountOfLoan(processedLoan.getSuggestedAmountOfLoan());
		return processOutputDto;
	}

	
	
}
