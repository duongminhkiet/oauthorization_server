package com.zmk.oauthserver;

import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;

@SpringBootApplication
public class Application implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired
	RegisteredClientRepository registeredClientRepository;
//	@Autowired
//	PasswordEncoder passwordEncoder;
	@Override
	public void run(String... args) throws Exception {
		createData();
	}
	private void createData() {
		/*
		Permission p1 = new Permission(); 
		p1.setName("READ_CATS");
		Permission p2 = new Permission(); 
		p2.setName("WRITE_CATS");
		Permission p3 = new Permission(); 
		p3.setName("EXEC_CATS");
		Role r1 = new Role();
		r1.setName("USER");
		Role r2 = new Role();
		r2.setName("ADMIN");
		List<Permission> lPR1 = new ArrayList<Permission>();lPR1.add(p1);lPR1.add(p3);
		List<Permission> lPR2 = new ArrayList<Permission>();lPR2.add(p1);lPR2.add(p2);lPR2.add(p3);
		r1.setPermissions(lPR1);
		r2.setPermissions(lPR2);
		List<Role> lRoles1 = new ArrayList<>();lRoles1.add(r1);
		List<Role> lRoles2 = new ArrayList<>();lRoles2.add(r2);
		User u1 = new User();u1.setUsername("user");u1.setPassword(passwordEncoder.encode("user"));u1.setRoles(lRoles1);u1.setEnabled(true);u1.setEmail("user@test.com");u1.setCredentialsNonExpired(true);u1.setAccountNonLocked(true);u1.setAccountNonExpired(true);
		User u2 = new User();u2.setUsername("admin");u2.setPassword(passwordEncoder.encode("admin"));u2.setRoles(lRoles2);u1.setEnabled(true);u2.setEmail("admin@test.com");u2.setCredentialsNonExpired(true);u2.setAccountNonLocked(true);u2.setAccountNonExpired(true);
		
		List<User> lUsers = new ArrayList<>();lUsers.add(u1);lUsers.add(u2);
		userRepository.saveAll(lUsers);
		*/
		
		RegisteredClient registeredClient = null;
		registeredClient = registeredClientRepository.findByClientId("messaging-client");
		if(registeredClient == null) {
			registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
					.clientId("messaging-client")
					//.clientSecret("{noop}secret")
					.clientSecret("$2a$10$MAlCzs8UH8XXXKd36fwTR.yP4P4P/XoByYK3bGFWhx0ohJ2TnPRry")
					.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)// can login SSO webform
					.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)// can get access_token postman
					.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)//can get code from client
					.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)//
					.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)//access direct to resource not get code
					.redirectUri("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc")
					.redirectUri("http://127.0.0.1:8080/authorized")
					.tokenSettings(tokenSettings())
					.scope(OidcScopes.OPENID)
					.scope("message.read")
					.scope("message.write")
					.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
					.build();
			registeredClientRepository.save(registeredClient);
		}
		RegisteredClient registeredClient2 = null;
		registeredClient2 = registeredClientRepository.findByClientId("app1");
		if(registeredClient2 == null) {
			registeredClient2 = RegisteredClient.withId(UUID.randomUUID().toString())
			        .clientId("app1")
					//.clientSecret("{noop}secret")
					.clientSecret("$2a$10$MAlCzs8UH8XXXKd36fwTR.yP4P4P/XoByYK3bGFWhx0ohJ2TnPRry")
			        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
			        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			        .redirectUri("https://oidcdebugger.com/debug")
			        .scope(OidcScopes.OPENID)
			        .build();
			registeredClientRepository.save(registeredClient2);
		}
		RegisteredClient registeredClient3 = null;
		registeredClient3 = registeredClientRepository.findByClientId("app2");
		if(registeredClient3 == null) {
			registeredClient3 = RegisteredClient.withId(UUID.randomUUID().toString())
			        .clientId("app2")
					//.clientSecret("{noop}secret")
					.clientSecret("$2a$10$MAlCzs8UH8XXXKd36fwTR.yP4P4P/XoByYK3bGFWhx0ohJ2TnPRry")
			        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
			        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//			        .tokenSettings(tokenSettings())
			        .scope("access-read")
			        .build();
			registeredClientRepository.save(registeredClient3);
		}
		     
	}
	  @Bean
	  public TokenSettings tokenSettings() {
	    //@formatter:off
	    return TokenSettings.builder()
	        .accessTokenTimeToLive(Duration.ofMinutes(2L))
	        .build();
	    // @formatter:on
	  }
}
