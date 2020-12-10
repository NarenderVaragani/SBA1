package com.iiht.training.eloan.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class UserDto {
	private Long id;
	
	@NotBlank(message = "First Name is required!")
	@Length(max = 100, min = 3)
	private String firstName;
	
	@NotBlank(message = "Last Name is required!")
	@Length(max = 100, min = 3)
	private String lastName;
	
	@NotBlank(message = "Email is required!")
	@Length(max = 100, min = 3)
	@Email
	private String email;
	
	@NotNull(message = "Mobile is required!")
	@Length(max = 10, min = 10)
	private String mobile;
	
	//@NotBlank(message = "Role is required!")
	private String role;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	
}
