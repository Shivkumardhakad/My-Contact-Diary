package com.smart.controller;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entity.User;
import com.smart.helper.Message;

@Controller
public class HomeController {

@Autowired
 UserRepository userRepository;

@Autowired 
  BCryptPasswordEncoder passwordEncoder;
@GetMapping("/")
public String home(Model model) {
	
	model.addAttribute("title", "Home smart Contect-Manger");
	return "home";
}

@GetMapping("/about")
public String about(Model model) {
	
	model.addAttribute("title", "About smart Contect-Manger");
	return "about";
}

@GetMapping("/signup")
public String singup(Model model) {
	
	model.addAttribute("title", "Register smart Contect-Manger");
	model.addAttribute("user",new User());
	return "singup";
}

//login 

@GetMapping("/signin")
public String login(Model model) {
	
	model.addAttribute("title","Login Smart Contect Manger");
	return "login";
}



@PostMapping("/do-register")
public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult result,@RequestParam(value="agreement",defaultValue="false") boolean agreement,Model model,HttpSession session ) {
	
	
	
	try {
		System.out.println("User "+user);
		System.out.println("aggrement"+agreement);
		
		if(!agreement) {
			System.out.println("you are not accepting agreement ");
			throw new Exception("you are not accepting term and condition ");
		}
		if(result.hasErrors()) {
			System.out.println("validataion error "+result.getAllErrors());
			model.addAttribute("user",user);
			return "singup";
		}
		

		
		user.setRole("ROLE_USER");
		user.setEnabled(true);
		user.setImageUrl("default.png");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
	this.userRepository.save(user);
		model.addAttribute("user",new User());
		
		session.setAttribute("message",new Message("Sucessfull register ", "alert-success"));
		return "singup";
	}catch(Exception e) {
		e.printStackTrace();
		model.addAttribute("user",user);
		session.setAttribute("message",new Message("Some thing went wrong"+e.getMessage(), "alert-danger"));
		return "singup";
	}
	}

}
	

