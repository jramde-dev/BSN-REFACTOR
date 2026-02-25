package dev.jramde.book_network;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * This auditorAware references to the method auditorAware in BeanConfig config class.
 * We tell Spring Boot that we want to track user activities, and then to catch the information of the user who acts,
 * scans the bean auditorAware to know what we want as information
 */
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@SpringBootApplication
@EnableAsync // Enable Asynchronous Execution
public class BookNetworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookNetworkApplication.class, args);
    }

   /* @Bean
    public CommandLineRunner commandLineRunner(RoleRepository roleRepository) {
        return args -> {

            // Default role
            if (roleRepository.findByName("USER").isEmpty()) {
                roleRepository.save(Role.builder().name("USER").build());
            }
        };
    }*/

}
