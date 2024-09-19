package com.bootlabs.digest.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Bean // (1)
    public DigestAuthenticationEntryPoint digestEntryPoint() {
        DigestAuthenticationEntryPoint entryPoint = new DigestAuthenticationEntryPoint();
        entryPoint.setRealmName("demoDigestAuth");
        entryPoint.setKey("571b264a-6868-49e6-9e43-ce80a5749b8f");
        return entryPoint;
    }

    // (2)
    public DigestAuthenticationFilter digestAuthenticationFilter() {
        DigestAuthenticationFilter authenticationFilter = new DigestAuthenticationFilter();
        authenticationFilter.setUserDetailsService(userDetailsService);
        authenticationFilter.setCreateAuthenticatedToken(true);
        authenticationFilter.setAuthenticationEntryPoint(digestEntryPoint());
        return authenticationFilter;
    }


    @Bean // (3)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e.authenticationEntryPoint(digestEntryPoint()))
                .addFilterBefore(digestAuthenticationFilter(),DigestAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers(toH2Console()).permitAll()
                                .requestMatchers("/api/**").hasAnyRole("ADMIN","USER")
                                .anyRequest().authenticated()
                );
        return http.build();
    }

}
