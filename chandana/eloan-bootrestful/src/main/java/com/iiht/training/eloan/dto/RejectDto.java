package com.iiht.training.eloan.dto;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

public class RejectDto {
	@NotBlank(message = "Remark is required!")
	@Length(max = 100, min = 3)
	private String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
