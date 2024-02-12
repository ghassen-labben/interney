package com.example.recruitment;

import com.example.recruitment.models.Admin;
import com.example.recruitment.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class RecruitmentApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(RecruitmentApplication.class, args);
	}
@Autowired
private AdminRepository adminRepository;
	@Override
	public void run(String... args) throws Exception {
try {
	BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
	Admin admin=new Admin("admin","admin@siga.com",bCryptPasswordEncoder.encode("admin")
			,"manager","rh");
	adminRepository.save(admin);
}
catch (Exception e)
{
	System.out.println(e.getMessage());
}
	}
}
