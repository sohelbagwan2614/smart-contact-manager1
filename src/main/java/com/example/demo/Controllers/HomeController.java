package com.example.demo.Controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Dao.UserRepository;
import com.example.demo.Helper.Message;
import com.example.demo.entities.User;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController 
{
	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/home")
	public String home()
	{
		return "home";
	}
	
	@GetMapping("/about")
	public String about(Model model)
	{
		model.addAttribute("title", "This is the project of Smart contact manager");
		return "about";
	}
	
	@GetMapping("/signup")
	public String signup(Model model)
	{
		model.addAttribute("title", "Sign up Page of Smart contact manager");
		model.addAttribute("user", new User());
		return "signup";
	}
	
	@RequestMapping(value="/do_register",method=RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult result1,@RequestParam(value="agreement",defaultValue="false")boolean agreement,Model model,HttpSession session)
	{
		try	{
			if(!agreement) {
				System.out.println("you are not Agreed terms and Condition ");
				throw new Exception("you are not Agreed terms and Condition");
			}
			
			if(result1.hasErrors())
			{
				System.out.println("Errors:"+result1.toString());
				model.addAttribute("user", user);
				return "signup";
			}
			
			user.setRole("Role_User");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			System.out.println("Agreement"+agreement);
			System.out.println("User"+user);
			
			User result=this.userRepository.save(user);
			
			System.out.println(result);
			
			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Successfully Registered !!!","alert-success"));
			
			Message message = (Message) session.getAttribute("message");
		    if (message != null) {
		        
		        model.addAttribute("message", message);
		        
		        session.removeAttribute("message");
		    }
					
			return "signup";
		}catch(Exception e){
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Something Went wrong !!"+e.getMessage(),"alert-danger"));
			Message message = (Message) session.getAttribute("message");
		    if (message != null) {
		        
		        model.addAttribute("message", message);
		        
		        session.removeAttribute("message");
		    }
			return "signup";
		}
	}
}
