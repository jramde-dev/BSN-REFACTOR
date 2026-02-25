package dev.jramde.book_network.security;

import dev.jramde.book_network.user.AppUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * This class is designed to load the user from the database.
 */
// @Service
@RequiredArgsConstructor
public class JrUserDetailsServiceImpl {
    /* implements UserDetailsService {
    private final AppUserRepository userRepository;

    *//**
     * Load user from the database via transactional.
     *
     * @param userEmail : user email
     * @return user details
     * @throws UsernameNotFoundException : when user not found
     *//*
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return userRepository.findByEmail(userEmail).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + userEmail));
    }*/
}
