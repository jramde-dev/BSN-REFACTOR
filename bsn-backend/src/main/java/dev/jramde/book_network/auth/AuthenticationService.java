package dev.jramde.book_network.auth;

import dev.jramde.book_network.email.EmailService;
import dev.jramde.book_network.enums.EEmailTemplateName;
import dev.jramde.book_network.role.Role;
import dev.jramde.book_network.role.RoleRepository;
import dev.jramde.book_network.security.JrJwtService;
import dev.jramde.book_network.user.AppUser;
import dev.jramde.book_network.user.AppUserRepository;
import dev.jramde.book_network.user.Token;
import dev.jramde.book_network.user.TokenRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

// @Service
@RequiredArgsConstructor
public class AuthenticationService {

   /* // Interface used to encode and verify password.
    // Must be declared as bean in the project (BeanConfig).
    private final PasswordEncoder passwordEncoder;

    // Interface used to processes an Authentication request.
    // Must be declared as bean in the project (BeanConfig).
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final AppUserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final JrJwtService jwtService;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    *//**
     * Register a new user.
     *
     * @param registrationRequest : registration request
     *//*
    public void register(RegistrationRequest registrationRequest) throws MessagingException {
        // 1. Retrieve the default user role
        // todo - make better exception handling
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("User role not initialized yet."));

        // 2. Create the user and save it
        AppUser user = AppUser.builder()
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);

        // 3. Send validation email
        sendValidationEmail(user);
    }

    *//**
     * Email the user to validate their account.
     *
     * @param user : user
     *//*
    private void sendValidationEmail(AppUser user) throws MessagingException {
        String newToken = generateAndSaveActivationToken(user);
        // Send email
        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EEmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Activate your account"
        );
    }

    *//**
     * Generate an activation code and save it to the database.
     *
     * @param user : user for which the activation code will be generated
     * @return activation code
     *//*
    private String generateAndSaveActivationToken(AppUser user) {
        String generatedToken = generateActivationCode(6);
        Token token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    *//**
     * Auto generate an activation code using SecureRandom generator.
     *
     * @param length : the length of the code
     * @return code in type of character
     *//*
    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        // To ensure that the code generated will be cryptographically encoded
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }

    *//**
     * Authenticate a user.
     *
     * @param authRequest : the authentication request
     * @return token
     *//*
    public AuthenticationResponse authenticate(AuthenticationRequest authRequest) {
        // Return the authentication token if the authentication request matches the user credentials
        Authentication authenticationToken = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );

        var claims = new HashMap<String, Object>();
        // Do not fetch the user from the database, but extract it from the authentication token
        AppUser user = (AppUser) authenticationToken.getPrincipal();
        claims.put("fullName", user.getFullName());
        String jwtToken = jwtService.generateBuiltToken(claims, user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    *//**
     * Activate a user account.
     *
     * @param token : the activation token
     *//*
    public void activateAccount(String token) throws MessagingException {
        // TODO - make better exception handling
        Token userToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (LocalDateTime.now().isAfter(userToken.getExpiredAt())) {
            sendValidationEmail(userToken.getUser());
            throw new RuntimeException("Token is expired. A new token is sent to the same email address.");
        }

        AppUser user = userRepository.findById(userToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);

        userToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(userToken);
    }*/
}
