package com.dossantosh.springfirstmicroservise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class }, scanBasePackages = {
		"com.dossantosh.springfirstmicroservice.common",
		"com.dossantosh.springfirstmicroservice.dtos",
		"com.dossantosh.springfirtsmicroservice.models",
		"com.dossantosh.springfirstmicroservice.repositories",
		"com.dossantosh.springfirstmicroservice.services"
})


@EnableMethodSecurity
@EnableTransactionManagement
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true)
// @EnableJdbcHttpSession
@EnableWebSecurity
public class SpringFirstMicroserviseApplication {

	public static void main(String[] args) {

		// BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		// String pass = "Sb202582";
		// String encodepass = "";
		// encodepass = passwordEncoder.encode(pass);
		// System.out.println(encodepass);

		// 1) Cargar .env usando dotenv-java
		// Dotenv dotenv = Dotenv.configure()
		// 		.ignoreIfMalformed() // ignora si el .env tiene líneas mal formateadas
		// 		.ignoreIfMissing() // no falle si no encuentra .env
		// 		.load();

		// // 2) Poner cada par clave=valor de .env en System.properties
		// dotenv.entries().forEach(entry -> {
		// 	String key = entry.getKey();
		// 	String value = entry.getValue();
		// 	// Solo definir si no existe ya la propiedad en System
		// 	if (System.getProperty(key) == null) {
		// 		System.setProperty(key, value);
		// 	}
		// });

		SpringApplication.run(SpringFirstMicroserviseApplication.class, args);
	}

}
