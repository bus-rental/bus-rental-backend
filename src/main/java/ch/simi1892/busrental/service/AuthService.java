package ch.simi1892.busrental.service;

import ch.simi1892.busrental.config.auth.TokenProvider;
import ch.simi1892.busrental.dto.auth.LoginDto;
import ch.simi1892.busrental.dto.auth.UserDto;
import ch.simi1892.busrental.dto.auth.UserRegistrationDto;
import ch.simi1892.busrental.entity.UserDbo;
import ch.simi1892.busrental.exception.EmailAlreadyInUseException;
import ch.simi1892.busrental.exception.InvalidEmailException;
import ch.simi1892.busrental.mapper.UserMapper;
import ch.simi1892.busrental.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

@Service
public class AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    private final UserRepository userRepository;
    private final TokenProvider tokenService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(UserRepository userRepository, TokenProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.tokenService = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Transactional(readOnly = true)
    public String login(LoginDto dto) {
        try {
            UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
            Authentication authUser = authenticationManager.authenticate(usernamePassword);
            UserDbo user = (UserDbo) authUser.getPrincipal();
            LOGGER.debug("User '{}' logged in", user.getUsername());
            return tokenService.generateAccessToken(new User(user.getUsername(), user.getPassword(), user.getAuthorities()));
        } catch (BadCredentialsException ex) {
            throw new IllegalArgumentException("Invalid credentials");
        }
    }

    @Transactional
    public UserDto registerUser(UserRegistrationDto dto) {
        validateEmail(dto.email());
        validatePassword(dto.password(), dto.passwordConfirmation());
        userRepository.findByEmail(dto.email()).ifPresent(existingUser -> {
            throw new EmailAlreadyInUseException("Email already exists");
        });

        UserDbo newUser = createDbo(dto);
        LOGGER.debug("Create user '{}' with id: {}", newUser.getUsername(), newUser.getId());
        UserDbo createdUser = userRepository.save(newUser);
        return UserMapper.toDto(createdUser);
    }

    private void validateEmail(String email) {
        if (!StringUtils.hasText(email) || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidEmailException(email);
        }
    }

    private void validatePassword(String password, String passwordConfirmation) {
        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("Password must not be empty.");
        }
        if (!password.equals(passwordConfirmation)) {
            throw new IllegalArgumentException("Password and confirmation do not match.");
        }
    }

    private UserDbo createDbo(UserRegistrationDto dto) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
        UserDbo newUser = UserMapper.toDbo(dto);
        newUser.setPassword(encryptedPassword);
        newUser.setActive(true);
        newUser.setUserRole(UserDbo.UserRole.CUSTOMER);
        return newUser;
    }
}
