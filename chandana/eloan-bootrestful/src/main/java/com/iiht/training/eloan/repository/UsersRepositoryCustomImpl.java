package com.iiht.training.eloan.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

import com.iiht.training.eloan.entity.Users;

public class UsersRepositoryCustomImpl implements UsersRepositoryCustom {
	
	// special bean exposed by JPA for custom DB interaction
	@Autowired
	private EntityManager entityManager;

	/*@Override
	public List<Users> roleQuery(String role) {
		// TODO Auto-generated method stub
		return null;
	}*/

}
