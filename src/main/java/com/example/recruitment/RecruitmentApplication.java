package com.example.recruitment;

import com.example.recruitment.models.Authority;
import com.example.recruitment.models.User;
import com.example.recruitment.repositories.AuthorityRepository;
import com.example.recruitment.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
@SpringBootApplication
public class RecruitmentApplication implements CommandLineRunner {

	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(RecruitmentApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		createAuthorityIfNotExists("ROLE_USER");
		createAuthorityIfNotExists("ROLE_STAGIAIRE");
		createAuthorityIfNotExists("ROLE_ENCADRANT");
		createAuthorityIfNotExists("ROLE_DIRECTEUR");
		createAuthorityIfNotExists("ROLE_ADMIN");

		createAuthorityIfNotExists("ROLE_RH");
		createAdminIfNotExists("admin");
	}

	private void createAuthorityIfNotExists(String name) {
		if (!authorityRepository.existsByName(name)) {
			Authority authority = new Authority("ROLE_USER");
			authority.setName(name);
			authorityRepository.save(authority);
		}
	}
	private void createAdminIfNotExists(String username) {
		if (!userRepository.existsByUsername(username)) {
			User user= new User("admin","admin","admin@siga.tn");
			Optional<Authority> admin=authorityRepository.findById("ROLE_ADMIN");
			Optional<Authority> userrole=authorityRepository.findById("ROLE_USER");
			Set<Authority> authoritySet=new HashSet<>();
			if(admin.isPresent())
				authoritySet.add(admin.get());
			if(userrole.isPresent())
				authoritySet.add(userrole.get());
			BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			user.setAuthorities(authoritySet);
			userRepository.save(user);
		}
	}
}
