package com.lazuardifachri.bps.lekdarjo;

import com.lazuardifachri.bps.lekdarjo.repository.RoleRepository;
import com.lazuardifachri.bps.lekdarjo.repository.UserRepository;
import com.lazuardifachri.bps.lekdarjo.security.model.ERole;
import com.lazuardifachri.bps.lekdarjo.security.model.Role;
import com.lazuardifachri.bps.lekdarjo.security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class LekdarjoApplication implements CommandLineRunner {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(LekdarjoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		if (!userRepository.existsByUsername("bpskabupatensidoarjo@gmail.com")){
			User admin = new User("bpskabupatensidoarjo@gmail.com",
					passwordEncoder.encode("@Hero140"));

			Set<Role> roles = new HashSet<>();

			Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));

			roles.add(adminRole);
			roles.add(userRole);


			admin.setRoles(roles);
			userRepository.save(admin);
		}


	}
}
