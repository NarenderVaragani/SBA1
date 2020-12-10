package com.iiht.training.eloan.dto;

import com.iiht.training.eloan.entity.Users;

public class UserDto {
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String mobile;
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
	
	public UserDto convertToDto(Users userEntity){
		this.setFirstName(userEntity.getFirstName());
		this.setLastName(userEntity.getLastName());
		this.setEmail(userEntity.getEmail());
		this.setMobile(userEntity.getMobile());
		return this;
	}
	
	public Users convertToEntity(){
		Users userEntity = new Users();
		userEntity.setFirstName(this.getFirstName());
		userEntity.setLastName(this.getLastName());
		userEntity.setEmail(this.getEmail());
		userEntity.setMobile(this.getMobile());
		return userEntity;
	}
	
	
}
