package com.example.recruitment;

import com.example.recruitment.models.Authority;
import com.example.recruitment.repositories.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class RecruitmentApplication implements CommandLineRunner {

	@Autowired
	private AuthorityRepository authorityRepository;

	public static void main(String[] args) {
		SpringApplication.run(RecruitmentApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		createAuthorityIfNotExists("ROLE_USER");
		createAuthorityIfNotExists("ROLE_STAGIAIRE");
		createAuthorityIfNotExists("ROLE_ENCADRANT");
		createAuthorityIfNotExists("ROLE_DIRECTEUR");
		createAuthorityIfNotExists("ROLE_RH");
	}

	private void createAuthorityIfNotExists(String name) {
		if (!authorityRepository.existsByName(name)) {
			Authority authority = new Authority("ROLE_USER");
			authority.setName(name);
			authorityRepository.save(authority);
		}
	}
}
