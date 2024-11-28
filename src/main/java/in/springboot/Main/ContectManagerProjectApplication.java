package in.springboot.Main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import in.springboot.Main.dao.UserRepository;


@SpringBootApplication
public class ContectManagerProjectApplication {
	
	@Autowired
	private static UserRepository userRepository;
	
	public static void main(String[] args) 
	{
		SpringApplication.run(ContectManagerProjectApplication.class, args);
	
	}

}
