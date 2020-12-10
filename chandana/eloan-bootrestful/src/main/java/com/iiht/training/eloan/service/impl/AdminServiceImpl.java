package com.iiht.training.eloan.service.impl;

import java.util.List;
import java.util.stream.Collectors;

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
		// convert dto into entity
		userDto.setRole("Loan Clerk");
		Users user = this.convertUserInputDtoToEntity(userDto);
		// save into DB, returns newly added record
		Users newUser = this.usersRepository.save(user);
		// convert entity into dto
		UserDto userOutputDto = this.convertUserEntityToDto(newUser);
		return userOutputDto;
	}

	@Override
	public UserDto registerManager(UserDto userDto) {
		// convert dto into entity
		userDto.setRole("Manager");
		Users user = this.convertUserInputDtoToEntity(userDto);
		// save into DB, returns newly added record
		Users newUser = this.usersRepository.save(user);
		// convert entity into dto
		UserDto userOutputDto = this.convertUserEntityToDto(newUser);
		return userOutputDto;
	}

	@Override
	public List<UserDto> getAllClerks() {
		List<Users> clerks = this.usersRepository.findAllByRole("Loan Clerk");
		
		//convert entity to dto list
			/*List<UserDto> userDtos = new ArrayList<UserDto>();
			for(Users user:users) {
				UserDto userDto = this.convertUserEntityToDto(user);
				userDtos.add(userDto);
			}*/
		List<UserDto> userDtos = clerks
									.stream()
									.map(this :: convertUserEntityToDto)
									.collect(Collectors.toList());
		return userDtos;
	}

	@Override
	public List<UserDto> getAllManagers() {
		List<Users> managers = this.usersRepository.findAllByRole("Manager");
		List<UserDto> userDtos = managers
									.stream()
									.map(this :: convertUserEntityToDto)
									.collect(Collectors.toList());
		return userDtos;
	}

	//utility methods
	private UserDto convertUserEntityToDto(Users user) {
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setEmail(user.getEmail());
		userDto.setMobile(user.getMobile());
		userDto.setRole(user.getRole());
		
		return userDto;
	}
	
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
		
}
