package com.spread.users.controller;

import com.spread.users.exception.InvalidRefreshTokenException;
import com.spread.users.model.User;
import com.spread.users.model.primitive.*;
import com.spread.users.rest.UserApi;
import com.spread.users.rest.model.RefreshToken;
import com.spread.users.rest.model.ReqUser;
import com.spread.users.rest.model.SignInReq;
import com.spread.users.rest.model.SignedInUser;
import com.spread.users.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
public class AuthController implements UserApi {
    private static final Logger logger =
            LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(final UserService userService,
                          final PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<SignedInUser> getAccessToken(@Valid RefreshToken refreshToken) {
        return ok(userService.getAccessToken(refreshToken)
                .orElseThrow(InvalidRefreshTokenException::new));
    }

    @Override
    public ResponseEntity<SignedInUser> signIn(@Valid SignInReq signInReq) {
        User userEntity = userService.findByName(
                        new Name(signInReq.getUsername()))
                .orElseThrow(
                        () -> new UsernameNotFoundException("invalid user"));

        if (passwordEncoder.matches(signInReq.getPassword(),
                userEntity.getPassword().getValue())) {
            return ok(userService.getSignedInUser(userEntity));
        }
        throw new InsufficientAuthenticationException("unauthorized");
    }

    @Override
    public ResponseEntity<Void> signOut(@Valid RefreshToken refreshToken) {
        userService.removeRefreshToken(refreshToken);
        return org.springframework.http.ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<SignedInUser> signUp(@Valid ReqUser reqUser) {
        return status(HttpStatus.CREATED).body(
                userService.createUser(new com.spread.users.model.User(
                        new IdChar(IdChar.generateUniqueID()),
                        new Name(reqUser.getName()),
                        new Role(com.spread.users.model.enums.Roles.USER),
                        new Password(passwordEncoder.encode(
                                reqUser.getPassword())),
                        new CreationDate(LocalDateTime.now()),
                        new ModifiedDate(LocalDateTime.now()))).get());
    }
}
