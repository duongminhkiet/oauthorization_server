/*
 * Copyright 2020-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zmk.oauthserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.zmk.oauthserver.jose.Jwks;

/**
 * @author Joe Grandja
 * @since 0.0.1
 */
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}
	
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http); 
		return http.formLogin(Customizer.withDefaults()).build();
	}

	@Bean
	public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
		JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);
		return registeredClientRepository;
	}

	@Bean
	public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
	                                                       RegisteredClientRepository registeredClientRepository) {
	    return new JdbcOAuth2AuthorizationService(jdbcTemplate, 
	            registeredClientRepository);
	}
	@Bean
	public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
		return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
	}
	@Bean
	public JWKSource<SecurityContext> jwkSource() {
		RSAKey rsaKey = Jwks.generateRsa();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	@Bean
	public ProviderSettings providerSettings() {
		return ProviderSettings.builder().issuer("http://auth-server:9000").build();
//		return ProviderSettings.builder().issuer("http://localhost:9000").build();
	}
//	 @Bean
//	  public RegisteredClientRepository registeredClientRepository() {
//	    // @formatter:off
//		 RegisteredClient registeredClient2 = RegisteredClient.withId(UUID.randomUUID().toString())
//			        .clientId("app1")
//					//.clientSecret("{noop}secret")
//					.clientSecret("$2a$10$MAlCzs8UH8XXXKd36fwTR.yP4P4P/XoByYK3bGFWhx0ohJ2TnPRry")
//			        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
//			        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//			        .redirectUri("https://oidcdebugger.com/debug")
//			        .scope(OidcScopes.OPENID)
//			        .build();
//	    // @formatter:on
//
//	    // @formatter:off
//		 RegisteredClient registeredClient3 = RegisteredClient.withId(UUID.randomUUID().toString())
//			        .clientId("app2")
//					//.clientSecret("{noop}secret")
//					.clientSecret("$2a$10$MAlCzs8UH8XXXKd36fwTR.yP4P4P/XoByYK3bGFWhx0ohJ2TnPRry")
//			        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
//			        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
////			        .tokenSettings(tokenSettings())
//			        .scope("access-read")
//			        .build();
//	    // @formatter:on
//
//	    return new InMemoryRegisteredClientRepository(registeredClient2, registeredClient3);
//	  }

	




//	@Bean
//	public EmbeddedDatabase embeddedDatabase() {
//		// @formatter:off
//		return new EmbeddedDatabaseBuilder()
//				.generateUniqueName(true)
//				.setType(EmbeddedDatabaseType.H2)
//				.setScriptEncoding("UTF-8")
//				.addScript("org/springframework/security/oauth2/server/authorization/oauth2-authorization-schema.sql")
//				.addScript("org/springframework/security/oauth2/server/authorization/oauth2-authorization-consent-schema.sql")
//				.addScript("org/springframework/security/oauth2/server/authorization/client/oauth2-registered-client-schema.sql")
//				.build();
//		// @formatter:on
//	}

}
