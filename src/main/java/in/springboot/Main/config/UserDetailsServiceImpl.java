package in.springboot.Main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import in.springboot.Main.dao.UserRepository;
import in.springboot.Main.entitys.User;

public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		// feaching the data from databases
		User userByUserName = userRepository.getUserByUserName(username);
		
		if(userByUserName==null) {
			
			throw new UsernameNotFoundException("could not found user !!");
		}
		
		CustomUserDeteils customUserDeteils=new CustomUserDeteils(userByUserName);
		
		return customUserDeteils;
	}

}
