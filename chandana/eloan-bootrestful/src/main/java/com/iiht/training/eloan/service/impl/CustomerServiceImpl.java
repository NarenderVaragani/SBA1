package com.iiht.training.eloan.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iiht.training.eloan.dto.LoanDto;
import com.iiht.training.eloan.dto.LoanOutputDto;
import com.iiht.training.eloan.dto.UserDto;
import com.iiht.training.eloan.entity.Loan;
import com.iiht.training.eloan.entity.Users;
import com.iiht.training.eloan.exception.CustomerNotFoundException;
import com.iiht.training.eloan.repository.LoanRepository;
import com.iiht.training.eloan.repository.ProcessingInfoRepository;
import com.iiht.training.eloan.repository.SanctionInfoRepository;
import com.iiht.training.eloan.repository.UsersRepository;
import com.iiht.training.eloan.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private LoanRepository loanRepository;
	
	@Autowired
	private ProcessingInfoRepository pProcessingInfoRepository;
	
	@Autowired
	private SanctionInfoRepository sanctionInfoRepository;
	
	@Override
	public UserDto register(UserDto userInputDto) {
		// convert dto into entity
		userInputDto.setRole("Customer");
		Users user = this.convertUserInputDtoToEntity(userInputDto);
		// save into DB, returns newly added record
		Users newUser = this.usersRepository.save(user);
		// convert entity into dto
		UserDto userOutputDto = this.convertUserEntityToOutputDto(newUser);
		return userOutputDto;
	}

	@Override
	public LoanOutputDto applyLoan(Long customerId, LoanDto loanDto) {
		
		// convert input dto into entity
		Loan loan = this.convertLoanInputDtoToEntity(loanDto);
		loan.setCustomerId(customerId);
		loan.setStatus(0);
		// save loan into DB, returns newly added record
		Loan newLoan = this.loanRepository.save(loan);
		// convert entity into dto
		LoanOutputDto loanOutputDto = this.convertLoanEntityToOutputDto(newLoan);
		return loanOutputDto;
		
	}

	@Override
	public LoanOutputDto getStatus(Long loanAppId) {
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
	public List<LoanOutputDto> getStatusAll(Long customerId) {
		if(this.usersRepository.existsById(customerId)) {
			List<Loan> loans = this.loanRepository.findAll();
			
			//get User Details tagged to the customer id
			Users user = this.usersRepository.findById(customerId).orElse(null);
			UserDto userDto = this.convertUserEntityToOutputDto(user);
			
			List<LoanOutputDto> loanOutputDtos = new ArrayList<LoanOutputDto>();
			
			for(Loan loan : loans) {
				LoanOutputDto loanOutputDto = this.convertLoanEntityToOutputDto(loan);
				loanOutputDto.setUserDto(userDto);
				loanOutputDtos.add(loanOutputDto);
			}
			
			/*List<LoanOutputDto> loanOutputDtos = loans.stream()
														.map(this :: convertLoanEntityToOutputDto)
														.collect(Collectors.toList());*/
			
			return loanOutputDtos;
		}
		return null;
	}
	
	@Override
	public UserDto fetchSingleCustomer(Long customerId) {
		if(this.usersRepository.existsById(customerId)) {
			Users user = this.usersRepository.findByIdAndRole(customerId, "Customer");
			if(user == null) {
				// throw custom exception
				throw new CustomerNotFoundException("Customer not found with the specified Id: "+customerId);
			}
			UserDto userDto = this.convertUserEntityToOutputDto(user);
			return userDto;
		}
		return null;
	}
	
	//utility methods
	private Users convertUserInputDtoToEntity(UserDto userInputDto) {
		Users user = new Users();
		user.setId(userInputDto.getId());
		user.setFirstName(userInputDto.getFirstName());
		user.setLastName(userInputDto.getLastName());
		user.setEmail(userInputDto.getEmail());
		user.setMobile(userInputDto.getMobile());
		user.setRole(userInputDto.getRole());
		
		return user;
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
	
	private Loan convertLoanInputDtoToEntity(LoanDto loanInputDto) {
		Loan loan = new Loan();
		loan.setLoanName(loanInputDto.getLoanName());
		loan.setLoanAmount(loanInputDto.getLoanAmount());
		loan.setLoanApplicationDate(loanInputDto.getLoanApplicationDate());
		loan.setBusinessStructure(loanInputDto.getBusinessStructure());
		loan.setBillingIndicator(loanInputDto.getBillingIndicator());
		loan.setTaxIndicator(loanInputDto.getTaxIndicator());
		return loan;
	}

	private LoanOutputDto convertLoanEntityToOutputDto(Loan loan) {
		//get Newly saved loan details into LoanDto object
		LoanDto newLoan = new LoanDto();
		newLoan.setLoanName(loan.getLoanName());
		newLoan.setLoanAmount(loan.getLoanAmount());
		newLoan.setLoanApplicationDate(loan.getLoanApplicationDate());
		newLoan.setBusinessStructure(loan.getBusinessStructure());
		newLoan.setBillingIndicator(loan.getBillingIndicator());
		newLoan.setTaxIndicator(loan.getTaxIndicator());
			
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
		return loanOutputDto;
	}
}
