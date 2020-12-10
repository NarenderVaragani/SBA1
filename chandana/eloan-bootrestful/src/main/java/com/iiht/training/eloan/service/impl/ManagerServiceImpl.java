package com.iiht.training.eloan.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iiht.training.eloan.dto.LoanDto;
import com.iiht.training.eloan.dto.LoanOutputDto;
import com.iiht.training.eloan.dto.ProcessingDto;
import com.iiht.training.eloan.dto.RejectDto;
import com.iiht.training.eloan.dto.SanctionDto;
import com.iiht.training.eloan.dto.SanctionOutputDto;
import com.iiht.training.eloan.dto.UserDto;
import com.iiht.training.eloan.entity.Loan;
import com.iiht.training.eloan.entity.ProcessingInfo;
import com.iiht.training.eloan.entity.SanctionInfo;
import com.iiht.training.eloan.entity.Users;
import com.iiht.training.eloan.exception.ClerkNotFoundException;
import com.iiht.training.eloan.exception.LoanNotFoundException;
import com.iiht.training.eloan.exception.ManagerNotFoundException;
import com.iiht.training.eloan.repository.LoanRepository;
import com.iiht.training.eloan.repository.ProcessingInfoRepository;
import com.iiht.training.eloan.repository.SanctionInfoRepository;
import com.iiht.training.eloan.repository.UsersRepository;
import com.iiht.training.eloan.service.ManagerService;

@Service
public class ManagerServiceImpl implements ManagerService {

	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private LoanRepository loanRepository;
	
	@Autowired
	private ProcessingInfoRepository processingInfoRepository;
	
	@Autowired
	private SanctionInfoRepository sanctionInfoRepository;
	
	@Override
	public List<LoanOutputDto> allProcessedLoans() {
		List<Loan> loans = this.loanRepository.findAllByStatus(1);
		List<LoanOutputDto> loanDtos = loans
										.stream()
										.map(this :: convertLoanEntityToOutputDto)
										.collect(Collectors.toList());
		return loanDtos;
	}

	@Override
	public RejectDto rejectLoan(Long managerId, Long loanAppId, RejectDto rejectDto) {
		Loan loan = this.convertRejectDtoToEntity(loanAppId, rejectDto);
		Loan rejectLoan = this.loanRepository.save(loan);
		RejectDto rejectedLoan = this.convertLoanEntityToRejectDto(rejectLoan);
		return rejectedLoan;
	}

	@Override
	public SanctionOutputDto sanctionLoan(Long managerId, Long loanAppId, SanctionDto sanctionDto) throws Exception {
		SanctionInfo sanctionLoan = this.convertSanctionInputDtoToEntity(managerId, loanAppId, sanctionDto);
		SanctionInfo sanctionedLoan = this.sanctionInfoRepository.save(sanctionLoan);
		SanctionOutputDto sanctionOutputDto = this.convertSanctionEntityToOutputDto(sanctionedLoan);
		//Update Loan status to Sanctioned
		Loan loan = this.loanRepository.findById(loanAppId).orElse(null);
		loan.setStatus(2);
		Loan newLoan = this.loanRepository.save(loan);
		
		return sanctionOutputDto;
	}
	
	@Override
	public UserDto fetchSingleManager(Long managerId) {
		if(this.usersRepository.existsById(managerId)) {
			Users user = this.usersRepository.findByIdAndRole(managerId, "Manager");
			if(user == null) {
				// throw custom exception
				throw new ManagerNotFoundException("Manager not found with the specified Id: "+ managerId);
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
		
		//get User Details tagged to the customer id into UserDto object
		Users user = this.usersRepository.findById(loan.getCustomerId()).orElse(null);
		UserDto userDto = this.convertUserEntityToOutputDto(user);
		
		//get Processing Info Details tagged to the LoanApp id into ProcessingDto object
		ProcessingInfo processInfo = this.processingInfoRepository.findByLoanAppId(loan.getId());
		ProcessingDto processDto = this.convertProcessEntityToDto(processInfo);
				
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
		loanOutputDto.setProcessingDto(processDto);
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
	
	private ProcessingDto convertProcessEntityToDto(ProcessingInfo processInfo) {
		ProcessingDto processedDto = new ProcessingDto();
		processedDto.setAcresOfLand(processInfo.getAcresOfLand());
		processedDto.setLandValue(processInfo.getLandValue());
		processedDto.setAppraisedBy(processInfo.getAppraisedBy());
		processedDto.setValuationDate(processInfo.getValuationDate());
		processedDto.setAddressOfProperty(processInfo.getAddressOfProperty());
		processedDto.setSuggestedAmountOfLoan(processInfo.getSuggestedAmountOfLoan());
		return processedDto;
	}
	
	private Loan convertRejectDtoToEntity(Long loanAppId, RejectDto rejectDto) {
		
		if(this.loanRepository.existsById(loanAppId)) {
			Loan loan = this.loanRepository.findById(loanAppId).orElse(null);
			loan.setRemark(rejectDto.getRemark());
			loan.setStatus(-1);
			Loan rejectedLoan = this.loanRepository.save(loan);
			return rejectedLoan;
		}
		return null;
	}
	
	private RejectDto convertLoanEntityToRejectDto(Loan rejectedLoan) {
		RejectDto rejectDto = new RejectDto();
		rejectDto.setRemark(rejectedLoan.getRemark());
		return rejectDto;
		
	}
	
	private SanctionInfo convertSanctionInputDtoToEntity(Long managerId, Long loanAppId, SanctionDto sanctionDto) throws ParseException {
		SanctionInfo sanction = new SanctionInfo();
		sanction.setLoanAppId(loanAppId);
		sanction.setManagerId(managerId);
		sanction.setLoanAmountSanctioned(sanctionDto.getLoanAmountSanctioned());
		sanction.setTermOfLoan(sanctionDto.getTermOfLoan());
		sanction.setPaymentStartDate(sanctionDto.getPaymentStartDate());
		
		//Formulated
		//Double loanTermMonths = ((sanctionDto.getTermOfLoan())*12); //5*12 = 60
		Double loanTerm = sanctionDto.getTermOfLoan();//5
		Double amountSanctioned = sanctionDto.getLoanAmountSanctioned(); //5,00,000
		String paymentStartDate = sanctionDto.getPaymentStartDate();
		String loanClosureDate;
		Double monthlyPayment;
		Double termPayment;
		Double interestRate = (double) 10;
		
		Double interestPercent = 1+(interestRate/100); //1.1
		Double termPaymentPartB = Math.pow(interestPercent, loanTerm); //1.1^5 = 1.61051
		termPayment = amountSanctioned * termPaymentPartB;//8,05,255
		monthlyPayment = (termPayment/loanTerm)/12;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = dateFormat.parse(paymentStartDate);
		// Convert Date to Calendar
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.YEAR, 15);
        Date closureDate = c.getTime();
        loanClosureDate = dateFormat.format(closureDate);
				
		sanction.setLoanClosureDate(loanClosureDate);
		sanction.setMonthlyPayment(monthlyPayment);

		return sanction;			
	}
	
	private SanctionOutputDto convertSanctionEntityToOutputDto(SanctionInfo sanctionedLoan) {
		SanctionOutputDto sanctionOutputDto = new SanctionOutputDto();
		sanctionOutputDto.setLoanAmountSanctioned(sanctionedLoan.getLoanAmountSanctioned());
		sanctionOutputDto.setTermOfLoan(sanctionedLoan.getTermOfLoan());
		sanctionOutputDto.setPaymentStartDate(sanctionedLoan.getPaymentStartDate());
		sanctionOutputDto.setLoanClosureDate(sanctionedLoan.getLoanClosureDate());
		sanctionOutputDto.setMonthlyPayment(sanctionedLoan.getMonthlyPayment());
		return sanctionOutputDto;
	}

}
