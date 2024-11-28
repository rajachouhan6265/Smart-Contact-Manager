package in.springboot.Main.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import in.springboot.Main.entitys.Contect;
import in.springboot.Main.entitys.User;

public interface UserRepository extends JpaRepository<User, Integer>{
		
	@Query("select u from User u where u.email=:email")
	public User getUserByUserName(@Param("email") String email);
}
