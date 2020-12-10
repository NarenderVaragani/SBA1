package com.iiht.training.eloan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iiht.training.eloan.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>, UsersRepositoryCustom{
	
	//List<Users> findByRole(String role);
	
	@Query("select u from Users u where u.role=?1")
	List<Users> findAllByRole(@Param("role") String role);
	
	@Query("select u from Users u where u.id=?1 AND u.role=?2")
	Users findByIdAndRole(@Param("id") Long id, @Param("role") String role);
	
	
}
