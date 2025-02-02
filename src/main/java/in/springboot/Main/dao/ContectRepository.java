package in.springboot.Main.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.springboot.Main.entitys.Contect;

public interface ContectRepository extends JpaRepository<Contect,Integer>{
	
	//pagination...
	@Query("from Contect as c where c.user.id =:userId")
	public Page<Contect> findContectByUser(@Param("userId") int userId,Pageable pageable);
	
	
}
