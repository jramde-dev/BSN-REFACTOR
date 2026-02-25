package dev.jramde.book_network.config;

import dev.jramde.book_network.user.AppUser;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Since the @CreatedDate and @LastModifiedDate annotations are not enough to
 * track the user who created or modified an entity even if we @EnableJpaAuditing,
 * we need to use the ApplicationAuditAware class.
 * It is because Spring does not know how to get the current user by default.
 * To make it work, we need to declare this class as a bean (in ApplicationConfig for example),
 * and then provide the bean name as attribute to the @EnableJpaAuditing annotation.
 */
public class ApplicationAuditAware implements AuditorAware<String> {
    /**
     * Get the current user's email.
     * @return Optional of the current user's email
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        // AppUser userPrincipal = (AppUser) authentication.getPrincipal();
        return Optional.ofNullable(authentication.getName());
    }
}
