package com.iiht.training.eloan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iiht.training.eloan.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>{
	
	//@Query("SELECT * FROM Users  WHERE lastName= (:lName)")
    //List<Users> findByLastName(@Param("lName") String lName);
	
	List<Users> findByLastName(String lName);

	 List<Users> findByRole(String role);
}
