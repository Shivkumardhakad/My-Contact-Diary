package com.smart.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact,Integer>{

	public List<Contact> findByUserId(int id);
	
//	@Query("select  from Contact as c where c.user.id=:id ")
//	public List<Contact> loadContactsByUserId(@Param("id")int id);

	
}
