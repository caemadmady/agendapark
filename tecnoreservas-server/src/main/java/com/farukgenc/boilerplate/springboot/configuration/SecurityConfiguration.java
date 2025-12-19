package com.farukgenc.boilerplate.springboot.configuration;

import com.farukgenc.boilerplate.springboot.security.jwt.JwtAuthenticationEntryPoint;
import com.farukgenc.boilerplate.springboot.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	private final JwtAuthenticationEntryPoint unauthorizedHandler;


	@Bean
	public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		//@formatter:off

		return http
				.csrf(CsrfConfigurer::disable)
                .cors(Customizer.withDefaults())
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.authorizeHttpRequests(request -> request.requestMatchers("/register",
																	      "/login",
																	      "/v3/api-docs/**",
																          "/swagger-ui/**",
																	      "/swagger-ui.html",
																	      "/actuator/**",
																	      "/notifications/stream",
                                                                          "/notifications",
                                                                          "/talents/create/reservation")
													   .permitAll()
                        //UserController
                        .requestMatchers(HttpMethod.GET, "/users/**").hasAuthority("SUPERADMIN")
                        // TalentController
                        .requestMatchers(HttpMethod.POST, "/talents/create").hasAnyAuthority("EXPERT","SUPERADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/talents/update/email/**").hasAnyAuthority("EXPERT","TALENT")
                        .requestMatchers(HttpMethod.PATCH, "/talents/change-password").hasAuthority("TALENT")
                        .requestMatchers(HttpMethod.GET, "/talents/talent/**").hasAuthority("TALENT")
                        .requestMatchers(HttpMethod.PATCH, "/talents/active/**").hasAuthority("EXPERT")
                        .requestMatchers(HttpMethod.PATCH, "/talents/inactive/**").hasAuthority("EXPERT")
                        .requestMatchers(HttpMethod.PATCH, "/talents/suspended/**").hasAuthority("EXPERT")
                        // ExpertController
                        .requestMatchers(HttpMethod.POST, "/experts/all").hasAnyAuthority("TALENT", "EXPERT")
                        .requestMatchers(HttpMethod.POST, "/experts/create-expert").hasAuthority("SUPERADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/experts/update/email/**").hasAuthority("EXPERT")
                        .requestMatchers(HttpMethod.PATCH, "/experts/change-password").hasAuthority("EXPERT")
                        .requestMatchers(HttpMethod.PATCH, "/experts/active/**").hasAuthority("SUPERADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/experts/inactive/**").hasAuthority("SUPERADMIN")
                        // EquipmentHistoryController
                        .requestMatchers(HttpMethod.POST, "/equipment/histories/createEquipmentHistory").hasAuthority("EXPERT")
                        .requestMatchers(HttpMethod.GET, "/equipment/histories/**").hasAuthority("EXPERT")
                        .requestMatchers(HttpMethod.DELETE, "/equipment/histories/**").hasAuthority("EXPERT")
                        // ReservationController
                        .requestMatchers(HttpMethod.GET, "/reservations/user").hasAnyAuthority("EXPERT", "TALENT")
                        .requestMatchers(HttpMethod.GET, "/reservations/serviceline/**").hasAnyAuthority("EXPERT", "TALENT")
                        .requestMatchers(HttpMethod.GET, "/reservations/dates").hasAnyAuthority("EXPERT", "TALENT")
                        .requestMatchers(HttpMethod.POST,"/reservations/create").hasAnyAuthority("EXPERT","TALENT")
                        .requestMatchers(HttpMethod.PATCH, "/reservations/modify/**").hasAnyAuthority("EXPERT","TALENT")
                        .requestMatchers(HttpMethod.PATCH,"/reservations/canceled/**").hasAnyAuthority("EXPERT","TALENT")
                        .requestMatchers(HttpMethod.PATCH, "/reservations/confirmed/**").hasAuthority("EXPERT")
                        .requestMatchers(HttpMethod.PATCH,"/reservations/fulfilled/**").hasAuthority("EXPERT")
                        .requestMatchers(HttpMethod.PATCH,"/reservations/missed/**").hasAuthority("EXPERT")
                        // ServiceLine Controller
                        .requestMatchers(HttpMethod.GET, "/service/**").hasAnyAuthority("EXPERT", "TALENT","SUPERADMIN","SECURITY")
                        //ResourceController
                        .requestMatchers(HttpMethod.GET, "/resources/**").hasAnyAuthority("EXPERT", "TALENT")
                        .requestMatchers(HttpMethod.POST, "/resources/**").hasAuthority("EXPERT")
                        .requestMatchers(HttpMethod.PATCH, "/resources/**").hasAuthority("EXPERT")
                        .requestMatchers(HttpMethod.DELETE, "/resources/**").hasAuthority("EXPERT")
                        .requestMatchers(HttpMethod.PATCH, "/superadmin/**").hasAuthority("SUPERADMIN")

													   .anyRequest()
													   .authenticated())
				.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(handler -> handler.authenticationEntryPoint(unauthorizedHandler))
				.build();

		//@formatter:on
	}

    // 👇 Configuración global de CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://frontend:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
