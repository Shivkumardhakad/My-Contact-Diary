package com.smart.controller;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entity.Contact;
import com.smart.entity.User;
import com.smart.helper.Message;
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ContactRepository contactRepository;
	
	
	@ModelAttribute
	public void  addCommonData(Model model, Principal principal) {
		
		
		
        
		String username	=principal.getName();
		System.out.println("username : "+username);
			
		User user = userRepository.getUserByUserName(username);
		System.out.println("user : "+user);
			
			
		model.addAttribute("user",user);
		
	}
	
	
	// dasbord home
	
	@GetMapping("/index")
	public String  user_dasboard(Model model, Principal principal) {
	
		model.addAttribute("title","user_dashbord" );
		
		return "normal/user_dasboard";
	}
	
	// open add form handler
	
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {
		
		model.addAttribute("title","Add Contact");
		model.addAttribute("contact",new Contact());
		
		return "normal/add_contact_form";
	}

	// save add data
	@PostMapping("/process-contact")
	public String addContact(@ModelAttribute("contact") Contact contact,
			@RequestParam("profileImage")MultipartFile file
			,Principal principal,HttpSession session) {
		
		
		String type;
		
		if(contact!=null) {
		try {
		
		
		String  name =principal.getName();
		User user =userRepository.getUserByUserName(name);
		
		
		// process in and uploading file 
		
		if(file.isEmpty()) {
			
			System.out.println("Image file :" +"file does not receved");
		}
		else {
			
		contact.setImage(file.getOriginalFilename());
		
   File saveFile=new ClassPathResource("static/img").getFile();

   
   Path path= Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());	
   try (InputStream ins = file.getInputStream()) {
	    Files.copy(ins, path, StandardCopyOption.REPLACE_EXISTING);
	}


		}
		
			  contact.setUser(user);
		      user.getContacts().add(contact);
		     
		     
		     
			 userRepository.save(user);
			 session.setAttribute("message",new Message("Your Contact is added Sucessfully", "success"));
		     System.out.println("user  "+user);
		     System.out.println("user  "+contact);
     
		}
		catch(Exception e) {
			System.out.println("Exception "+e.getMessage() );
			session.setAttribute("message",new Message("Something went wrong try agin", "error"));
			e.printStackTrace();
			
			
		}
		 return "redirect:/user/add-contact";
		}
		
		return "/error";
		
	}
	
// show contact handler
	@GetMapping("/show_contacts")
	public String showContact(Model model,Principal principal) {
		
String name=principal.getName();
  User user =userRepository.getUserByUserName(name);
  int id = user.getId();
  
 List<Contact>  contacts=contactRepository.findByUserId(id);
 
 for(Contact contact:contacts) {
	 
	 System.out.println(contact);
 }   
      model.addAttribute("contacts", contacts);

		model.addAttribute("title","Show Contact");
		return "normal/show_contacts";
	}
	
// delete contact handler
@DeleteMapping("/contact/{id}")
public String deleteContact(@PathVariable("id") int id ) {
	
	
	return "";
}
	
	
}
