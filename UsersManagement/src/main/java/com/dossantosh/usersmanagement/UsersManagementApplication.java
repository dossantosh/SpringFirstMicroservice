package com.dossantosh.usersmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class }, scanBasePackages = {
		"com.dossantosh.usersmanagement.common",
		"com.dossantosh.usersmanagement.controllers",
		"com.dossantosh.usersmanagement.models",
		"com.dossantosh.usersmanagement.projections",
		"com.dossantosh.usersmanagement.repositories",
		"com.dossantosh.usersmanagement.services"
})

@EnableMethodSecurity
@EnableTransactionManagement
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true)
// @EnableJdbcHttpSession
@EnableWebSecurity
public class UsersManagementApplication {

	public static void main(String[] args) {

/* 		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String pass = "Sb202582";
		String encodepass = "";
		encodepass = passwordEncoder.encode(pass);
		System.out.println(encodepass);
 */
		SpringApplication.run(UsersManagementApplication.class, args);
	}

}