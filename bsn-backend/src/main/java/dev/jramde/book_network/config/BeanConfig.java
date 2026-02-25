package dev.jramde.book_network.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;


@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    // From security.core.userdetails
    /*private final UserDetailsService userDetailsService;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
        daoAuthProvider.setUserDetailsService(this.userDetailsService);
        daoAuthProvider.setPasswordEncoder(this.passwordEncoder());
        return daoAuthProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    *//**
     * AuthenticationManager is used to processes an Authentication request.
     *
     * @param authConfig : Configuration for authentication.
     * @return AuthenticationManager
     * @throws Exception : If the authentication manager cannot be created.
     *//*
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }*/

    /**
     * Cors definition which will filter requests coming from Angular client.
     * By doing that, requests will filter by securityFilterChain defined in JrSecurityConfig.
     *
     * @return CorsFilter.
     */
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource urlSource = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowCredentials(true);
        corsConfig.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
        corsConfig.setAllowedHeaders(Arrays.asList(
                HttpHeaders.ORIGIN,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT,
                HttpHeaders.AUTHORIZATION
        ));
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        urlSource.registerCorsConfiguration("/**", corsConfig);
        return new CorsFilter(urlSource);
    }

    /**
     * Bean sur la classe d'audit.
     *
     * @return l'utilisateur qui a orchestré l'action.
     */
    @Bean
    public AuditorAware<Integer> auditorAware() {
        return new ApplicationAuditAware(); // Our class we created above
    }
}
