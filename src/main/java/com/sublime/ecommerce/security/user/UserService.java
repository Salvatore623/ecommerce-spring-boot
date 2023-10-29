package com.sublime.ecommerce.security.user;

import com.sublime.ecommerce.security.auth.AuthResponse;
import com.sublime.ecommerce.security.config.JwtService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse changeFullName(@Size(min = 1, max = 50) @NotBlank String fullName) {
        var user = extractUserFromToken();
        user.setFullName(fullName);
        userRepository.save(user);
        var token = jwtService.generateToken(user, user.getFullName());
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse changeEmail(@Size(min = 1, max = 50) @NotBlank @Email String newEmail) {
        var user = extractUserFromToken();
        userRepository.findByEmail(newEmail)
                .ifPresent(u -> {
                    if(user.getEmail().equals(newEmail)){
                        throw new EntityExistsException(u.getUsername() + " è già la tua email");
                    }
                    throw new EntityExistsException(u.getUsername() + " è già in uso");
                });

        user.setEmail(newEmail);
        userRepository.save(user);
        var token = jwtService.generateToken(user, user.getFullName());
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse changePassword(changePasswordReq req) {
        var user = extractUserFromToken();
        if (!passwordEncoder.matches(req.password(), user.getPassword())){
            throw new IllegalArgumentException("La password attuale non è corretta");
        }

        user.setPassword(passwordEncoder.encode(req.newPassword()));
        userRepository.save(user);
        var token = jwtService.generateToken(user, user.getFullName());
        return AuthResponse.builder()
                .token(token)
                .build();

    }

    public User extractUserFromToken(){
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("L'utente non esiste"));
    }
}
