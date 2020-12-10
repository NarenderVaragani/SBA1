package com.iiht.training.eloan.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iiht.training.eloan.dto.UserDto;
import com.iiht.training.eloan.entity.Users;
import com.iiht.training.eloan.repository.UsersRepository;
import com.iiht.training.eloan.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private UsersRepository usersRepository;
	
	@Override
	public UserDto registerClerk(UserDto userDto) {
		// TODO Auto-generated method stub
		// Convert userDto to User entity
		Users user = userDto.convertToEntity();
		user.setRole("Loan Clerk");
		usersRepository.save(user);
		// user repository.save()
		
		return userDto;
	}

	@Override
	public UserDto registerManager(UserDto userDto) {
		Users user = userDto.convertToEntity();
		user.setRole("Manager");
		usersRepository.save(user);
		return userDto;
	}

	@Override
	public List<UserDto> getAllClerks() {
		// TODO Auto-generated method stub
		List<Users> users = usersRepository.findByRole("Loan Clerk");
		List<UserDto> userDtos = new ArrayList<>();
		for(Users user: users){
			UserDto userDto = new UserDto();
			userDto.convertToDto(user);
			userDtos.add(userDto);
		}
		return userDtos;
	}

	@Override
	public List<UserDto> getAllManagers() {
		List<Users> users = usersRepository.findByRole("Manager");
		List<UserDto> userDtos = new ArrayList<>();
		for(Users user: users){
			UserDto userDto = new UserDto();
			userDto.convertToDto(user);
			userDtos.add(userDto);
		}
		return userDtos;
	}

}
