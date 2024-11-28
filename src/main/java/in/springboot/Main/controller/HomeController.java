package in.springboot.Main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.springboot.Main.dao.UserRepository;
import in.springboot.Main.entitys.User;
import in.springboot.Main.helper.Message;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	
		@Autowired
		private BCryptPasswordEncoder bCryptPasswordEncoder;
	
		@Autowired
		private UserRepository userRepository;
	
		@RequestMapping("/")
		public String home(Model model) {
			
			model.addAttribute("title", "Home_Page");
			
			return "home";
		}
		
		@RequestMapping("/about")
		public String about(Model model) {
			
			model.addAttribute("title", "Contect_Page");
			
			return "about";
		}
		
		@RequestMapping("/register")
		public String singup(Model model) {
			model.addAttribute("title", "Signup_Page");
			model.addAttribute("user", new User());
			return "Registretion";
		}
	
		
		// Handling For Regiter User
		
		@PostMapping("/regProcess")
		public String processReg(@Valid @ModelAttribute("user") User user,BindingResult result1,
				@RequestParam(value = "agreement",defaultValue = "false") boolean agreement,
				Model model, HttpSession session) {
			
			try {
				
				if(!agreement)
				{
					System.out.println("You Have not agree terms and Condition");
					throw new Exception("You Have not agree terms and Condition");
				}
				
				if(result1.hasErrors()) {
					System.out.println("ye result hai "+result1);
					return "Registretion";
				}
				
				user.setRole("ROLE_USER");
				user.setEnable(true);
				user.setImageUrl("defalt.png");
				user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
				
				User result = this.userRepository.save(user);
								
				model.addAttribute("user", new User());
				
				session.setAttribute("message",new Message("Successfully Registered !!", "alert-success"));
				
				return "Registretion";
				
			}
			catch (Exception e) {
				model.addAttribute("user",user);
				session.setAttribute("message",new Message("something went wrong !!"+e.getMessage(),"alert-danger"));
				e.printStackTrace();
				return "Registretion";
			}
			
		}
		
		
		@RequestMapping("/login")
		public String login(Model model) {
			model.addAttribute("title","Login_page");
			return "login";
		}
		
}
