package com.smart.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact,Integer>{
//Current-page page 
//Contact per page -5 
	public Page<Contact> findByUserId(int id,Pageable pePageable);
	
	public Contact findBycId(String cId);


	
//	@Query("select  from Contact as c where c.user.id=:id ")
//	public List<Contact> loadContactsByUserId(@Param("id")int id);

	
}
