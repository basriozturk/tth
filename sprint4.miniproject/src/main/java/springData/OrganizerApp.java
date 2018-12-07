package springData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import springData.domain.Organizer;
import springData.domain.OrganizerUser;
import springData.domain.Role;
import springData.repository.RoleRepository;
import springData.repository.UserRepository;

@SpringBootApplication
public class OrganizerApp implements CommandLineRunner  { 
	/**
	 * An organizer object for everyone to use.
	 */
	public static Organizer organizer = new Organizer();
	
	@Autowired
	private UserRepository userRepo;
	@Autowired
	RoleRepository roleRepo;
	
	public static void main(String[] args) {
        SpringApplication.run(OrganizerApp.class, args);
        
    }

	@Override
	public void run(String... args) throws Exception {
		
		// TODO Task 
		BCryptPasswordEncoder pe = new BCryptPasswordEncoder();
		
		Role admin = new Role();
		admin.setRole("ADMIN");
		admin.setId(0);
		roleRepo.save(admin);
		
		Role manager = new Role();
		admin.setRole("MANAGER");
		admin.setId(1);
		roleRepo.save(manager);
		
		Role assistant = new Role();
		admin.setRole("ASSISTANT");
		admin.setId(2);
		roleRepo.save(assistant);
		
		OrganizerUser u = new OrganizerUser();
		u.setRole(admin);
		u.setLogin("admin");
		u.setPassword(pe.encode("admin"));
		userRepo.save(u);

	}
}
