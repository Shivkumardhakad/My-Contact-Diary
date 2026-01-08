package com.smart.controller;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
			contact.setImage("contact.png");
		}
		else {
			
		contact.setImage(file.getOriginalFilename());
		
   File saveFile=new ClassPathResource("static/img").getFile();

   

String uploadDir = System.getProperty("java.io.tmpdir"); // Temp folder use karega
Path path = Paths.get(uploadDir + File.separator + file.getOriginalFilename());
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
	@GetMapping("/show_contacts/{page}")
	public String showContact(@PathVariable("page") Integer page, Model model, Principal principal) {
	    
	    // Title set kiya
	    model.addAttribute("title", "Show Contact");

	    // User nikala
	    String name = principal.getName();
	    User user = userRepository.getUserByUserName(name);
	    
	    // Page Request banaya (Current Page, Records Per Page)
	    Pageable pageable = PageRequest.of(page, 5);
	    
	    // Data fetch kiya
	    Page<Contact> contacts = contactRepository.findByUserId(user.getId(), pageable);
	    
	    // Model attributes set kiye
	    model.addAttribute("contacts", contacts);
	    
	    // --- YAHAN CHANGE KIYA HAI (CamelCase use kiya hai) ---
	    model.addAttribute("currentPage", page);       // 'currentpage' nahi 'currentPage'
	    model.addAttribute("totalPages", contacts.getTotalPages()); // 'totalpages' nahi 'totalPages'
	    
	    return "normal/show_contacts";
	}
	
//    showing particular contact detail 
	@GetMapping("/{cId}/contact")
	public String showContactDetail(@PathVariable("cId")Integer cId,Model model,Principal principal) {
		     System.out.println("CID = "+cId);
		     
	Contact contact	    = contactRepository.findById(cId).get();
	                     
	
	String username =principal.getName();
	 User user = userRepository.getUserByUserName(username);
	
	 System.out.println(contact);
	 
	 if(user.getId()==contact.getUser().getId())
	 {
		 model.addAttribute("contact",contact);
		  model.addAttribute("title",contact.getName());
	 }
	 
	
		return "normal/contact_detail";
	}
	
	
	
	
	
	
	
	
// delete contact handler
@GetMapping("/delete/{cId}")
public String deleteContact(@PathVariable("cId") int cId ,HttpSession httpsession) {
	           Optional<Contact> optinalcontact = contactRepository.findById(cId);
	           
	                       Contact contact =optinalcontact.get();
	                      try {
	                   contactRepository.delete(contact);  
	                   System.out.println("deleteContact"+cId);
	                   httpsession.setAttribute("message",new Message("Contact Deleted Sucessfully ","success"));
	                   
	               	return "redirect:/user/show_contacts/0";
	                      }
	                      catch(Exception e) {
	                    	  e.printStackTrace();
	                    	throw   new RuntimeException("some thing went wrong data is not delted ");
	                      }
	                      
	
}

// update - form 
@GetMapping("/update/{cId}")
public String updateContact(@PathVariable("cId") int  cId,Model model) {
	
	Optional<Contact>   optionalcontact    =contactRepository.findById(cId);
	             Contact contact =optionalcontact.get();
	System.out.println("update is working  CID ="+cId);
	
	
	
	model.addAttribute("title","Update contact");
	model.addAttribute("contact",contact);
	return "normal/udate_contact";
}

// update process 

@PostMapping("/update-process")
public String updateProcess(@ModelAttribute Contact contact ,@RequestParam("file") MultipartFile file) {
	
	
	try {
	Contact upcontact    = contactRepository.findById(contact.getcId()).get();
	        upcontact.setName(contact.getName());
	        upcontact.setNickName(contact.getNickName());
	        upcontact.setPhone(contact.getPhone());
	        upcontact.setWork(contact.getWork());
	        upcontact.setEmail(contact.getEmail());
	        upcontact.setDescription(contact.getDescription());
	        
	        // image 
	        String old_image = upcontact.getImage();

	        if(!file.isEmpty()) {

	            File deleteFile=new ClassPathResource("static/img").getFile();
	            File file1= new File(deleteFile, old_image);

	            file1.delete();   //  ab sirf jab new file aayegi tab delete hoga

				// ✅ NAYA CODE (Ise Paste karo)
String uploadDir = System.getProperty("java.io.tmpdir");
Path path = Paths.get(uploadDir + File.separator + file.getOriginalFilename());
Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	            upcontact.setImage(file.getOriginalFilename());
	        }
	       

	       
	        
	        contactRepository.save(upcontact);
	        
	         
	}
	catch(Exception e) {
		
		e.printStackTrace();
	}
	
	return "redirect:/user/"+contact.getcId()+"/contact";

}

    //Your Profile Handler
    @GetMapping("/profile")
    public String yourProfile(Model model) {
        model.addAttribute("title", "Profile Page");
        return "normal/profile";
    }

    // Community Chat Handler
    @GetMapping("/chat")
    public String communityChat(Model model) {
        model.addAttribute("title", "Community Chat");
        return "normal/chat";
    }

}
