package com.iiht.training.eloan.dto;

import javax.validation.constraints.NotNull;

public class SanctionDto {
	@NotNull(message = "Loan Amount Sanctioned is required!")
	//add not zero validation
	private Double loanAmountSanctioned;
	
	@NotNull(message = "Loan Term is required!")
	//add not zero validation
	private Double termOfLoan;
	
	private String paymentStartDate;
	public Double getLoanAmountSanctioned() {
		return loanAmountSanctioned;
	}
	public void setLoanAmountSanctioned(Double loanAmountSanctioned) {
		this.loanAmountSanctioned = loanAmountSanctioned;
	}
	public Double getTermOfLoan() {
		return termOfLoan;
	}
	public void setTermOfLoan(Double termOfLoan) {
		this.termOfLoan = termOfLoan;
	}
	public String getPaymentStartDate() {
		return paymentStartDate;
	}
	public void setPaymentStartDate(String paymentStartDate) {
		this.paymentStartDate = paymentStartDate;
	}
	
	
}
