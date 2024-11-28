package in.springboot.Main.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import in.springboot.Main.dao.ContectRepository;
import in.springboot.Main.dao.UserRepository;
import in.springboot.Main.entitys.Contect;
import in.springboot.Main.entitys.User;
import in.springboot.Main.helper.Message;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserRepository userRepository;
	@Autowired
	ContectRepository contectRepository;
	
	@ModelAttribute
	public void addCommonData(Model m,Principal principal) {
		String name = principal.getName();
		
		User user= userRepository.getUserByUserName(name);
		
		m.addAttribute("user", user);
		
	}
	
	
	
	@RequestMapping("/index")
	public String dashBoard(Model model, Principal principal) {
		
		model.addAttribute("title","User_dashBoard");
		
		return "normal/user_deshBoard";
	}
	
	
	@GetMapping("/add-contect")
	public String openAddContectForm(Model model) {
			
		model.addAttribute("title", "Add Contect");
		model.addAttribute("contect",new Contect());
		return "normal/add_contect_form";
	}
	
	// Procesing add Contect form
	
	
	@PostMapping("/process-contect")
	public String processContact(@Param("name") String name,@Param("secondName") String secondName,
			@Param("work") String work,@Param("email") String email,
			@Param("phone") String phone,
			@Param("description") String description
			,@RequestParam("image") MultipartFile image,Model model,Principal principal,HttpSession session) {
				
			try {
		
			Contect contect =new Contect();
			contect.setName(name);
			contect.setSecondName(secondName);
			contect.setWork(work);
			contect.setPhone(phone);
			contect.setEmail(email);
			contect.setDescription(description);
			
			String name2 = principal.getName();
			
			User user2 = userRepository.getUserByUserName(name2);
			
			//processing and uploadng file
			if(image.isEmpty()) {
				
				System.out.println("image to select kr");
				contect.setImage("contact.png");
			}
			else {
				contect.setImage(image.getOriginalFilename());
				
				File file= new ClassPathResource("static/image").getFile();
				
				Path path=Paths.get(file.getAbsolutePath()+File.separator+image.getOriginalFilename());
				
				Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}
						
			contect.setUser(user2);
			
			user2.getContects().add(contect);
			
			userRepository.save(user2);
			
			System.out.println(contect);
			
			//success message
			session.setAttribute("message", new Message("Your contect added !! add more..","success"));
			
			}
			catch (Exception e) {
				e.printStackTrace();
				session.setAttribute("message", new Message("something want wrong !! try again..","danger"));
				System.out.println("Hello Raja");
			}
			
		return "normal/add_contect_form";
	}
	
	
	// Show contects
	// per page =5[n]
	// current page = 0[page]
	
	@GetMapping("/show-contect/{page}")
	public String showContacts(@PathVariable("page") int page, Model model,Principal principal) {
		
		/* this is one way to feaching data 
		 * String name = principal.getName();
		 * 
		 * User userByUserName = userRepository.getUserByUserName(name);
		 * 
		 * List<Contect> contects = userByUserName.getContects();
		 */	
		
		model.addAttribute("title", "View_All");
		
		String name = principal.getName();
		
		User userByUserName = userRepository.getUserByUserName(name);
		
		int id = userByUserName.getId();
		
		Pageable pageable = PageRequest.of(page, 4);
		
		Page<Contect> contects = this.contectRepository.findContectByUser(id,pageable);
		
		model.addAttribute("contect", contects);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contects.getTotalPages());
		
		return "normal/show_contect";
	}
	
	
	//showing particular contec details 
	
	@RequestMapping("/{cId}/contact")
	public String showContactDtails(@PathVariable("cId") Integer cId,Model m,Principal principal) {
		
		 m.addAttribute("title", "Contact_Detail");

		 Optional<Contect> contactOptional = contectRepository.findById(cId);
		
		 Contect contect = contactOptional.get();
		 
		 String name = principal.getName();
		 
		 User user = userRepository.getUserByUserName(name);
		 
		 if(user.getId()==contect.getUser().getId()) {
		 
			 m.addAttribute("contact", contect);
			 m.addAttribute("title", contect.getName());
		 }
		 
		return "normal/contact_detail";
	}
	
	
	//delete Handler
	@GetMapping("/delete/{id}")
	public String deleteContact(@PathVariable("id") Integer id, Model model,Principal principal,HttpSession session) {
		
		Optional<Contect> conOptional = contectRepository.findById(id);
		
		Contect contect = conOptional.get();
		
		String name = principal.getName();
		
		User user = this.userRepository.getUserByUserName(name);

		/*
		write logic here remove image;
		
		*/
		if(user.getId()==contect.getUser().getId()) {
		this.contectRepository.delete(contect);
		session.setAttribute("message", new Message("Contact delete successfully..","success"));
		}
		
		return "redirect:/user/show-contect/0";
	}
	
	// Update Form handler
	
	@PostMapping("/update-contact/{id}")
	public String updateform(@PathVariable("id") Integer id ,Model model) {
		
		model.addAttribute("title", "update_contact");
		
		Contect contact = contectRepository.findById(id).get();
		
		model.addAttribute("contact", contact);
		
		return "normal/update_form";
	}
	
	
	// process and save update contact
	
	@PostMapping("/update-contect")
	public String updateHandler(@Param("cId") Integer cId, @Param("name") String name,
			@Param("secondName") String secondName,
			@Param("work") String work,@Param("email") String email,
			@Param("phone") String phone,
			@Param("description") String description,@RequestParam("image") MultipartFile image,Model model,Principal principal,HttpSession session,Principal principal2) throws IOException 
	{
	
		Contect oldContect = this.contectRepository.findById(cId).get();
		
		Contect contect=new Contect();
		contect.setcId(cId);
		contect.setName(name);
		contect.setSecondName(secondName);
		contect.setWork(work);
		contect.setPhone(phone);
		contect.setEmail(email);
		contect.setDescription(description);
		
		System.out.println(contect);
		
		if(!image.isEmpty()) {
			
			// delete old photo and update new
			
			File deleteFile=new ClassPathResource("static/image").getFile();
			
			File file1=new File(deleteFile,oldContect.getImage()); 
			
			file1.delete();
			
			//update new photo
			
			File savaFile=new ClassPathResource("static/image").getFile();
			
			Path path=Paths.get(savaFile.getAbsolutePath()+ File.separator+ image.getOriginalFilename());
			
			Files.copy(image.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
			
			contect.setImage(image.getOriginalFilename());
		
			session.setAttribute("message",new Message("Your contact is updated","success"));
		}
		else {
			
			contect.setImage(oldContect.getImage());
		}
				
		User user = userRepository.getUserByUserName(principal.getName());
		
		contect.setUser(user);
		
		this.contectRepository.save(contect);
		
		System.out.println(contect.getcId());
		
		return "redirect:/user/"+contect.getcId()+"/contact";
	
	}
	
	@GetMapping("/profile")
	public String profileHandler(Model model) {
		
		model.addAttribute("title", "profile_page");
		return "normal/profile";
	}
	
}
