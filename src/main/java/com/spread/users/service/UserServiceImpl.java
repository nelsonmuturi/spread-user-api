package com.spread.users.service;

import com.spread.users.exception.GenericAlreadyExistsException;
import com.spread.users.exception.InvalidRefreshTokenException;
import com.spread.users.model.Id;
import com.spread.users.model.User;
import com.spread.users.model.primitive.CreationDate;
import com.spread.users.model.primitive.IdChar;
import com.spread.users.model.primitive.Name;
import com.spread.users.model.primitive.Token;
import com.spread.users.repository.RefreshTokenRepository;
import com.spread.users.repository.UserRepository;
import com.spread.users.rest.model.RefreshToken;
import com.spread.users.rest.model.SignedInUser;
import com.spread.users.security.JwtManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtManager jwtManager;

    public UserServiceImpl(final UserRepository userRepository,
                           final RefreshTokenRepository refreshTokenRepository,
                           final JwtManager jwtManager) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtManager = jwtManager;
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(new Id(new IdChar(id)));
    }

    @Override
    public Optional<User> findByName(Name name) {
        return userRepository.findByName(name);
    }

    @Transactional
    @Override
    public Optional<SignedInUser> createUser(User user) {
        if (userRepository.findByName(user.getName()).isPresent())
            throw new GenericAlreadyExistsException("user name already exists");

        return Optional.of(createSignedUserWithRefreshToken(
                userRepository.save(user)));
    }

    @Override
    public Optional<SignedInUser> getAccessToken(RefreshToken refreshToken) {
        return refreshTokenRepository.findByToken(
                        new Token(refreshToken.getRefreshToken()))
                .map(ut -> Optional.of(createSignedInUser(
                        ut.getUser()).refreshToken(refreshToken.getRefreshToken())))
                .orElseThrow(
                        () -> new InvalidRefreshTokenException("invalid token"));
    }

    @Override
    public SignedInUser getSignedInUser(User user) {
        return createSignedUserWithRefreshToken(user);
    }

    @Override
    public void removeRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.findByToken(
                        new Token(refreshToken.getRefreshToken()))
                .ifPresentOrElse(refreshTokenRepository::delete, () -> {
                    throw new InvalidRefreshTokenException("invalid token");
                });
    }

    private SignedInUser createSignedUserWithRefreshToken(User user) {
        return createSignedInUser(user).refreshToken(createRefreshToken(user));
    }

    private SignedInUser createSignedInUser(User user) {
        String token = jwtManager.create(
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getName().getValue())
                        .password(user.getPassword().getValue())
                        .authorities(Objects.nonNull(user.getRole()) ?
                                user.getRole().getValue().name() : "")
                        .build());
        return new SignedInUser()
                .username(user.getName().getValue())
                .accessToken(token)
                .userId(user.getId().getValue())
                .role(user.getRole().getValue().name());
    }

    private String createRefreshToken(User user) {
        // check if a token already exists for user
        var foundToken =
                refreshTokenRepository.findByUserId(user.getId());

        // if exists, delete it
        foundToken.ifPresent(refreshTokenRepository::delete);

        // otherwise generate a new one
        String token = RandomHolder.randomKey(128);
        refreshTokenRepository.save(
                new com.spread.users.model.RefreshToken(
                        new IdChar(IdChar.generateUniqueID()),
                        user,
                        new Token(token),
                        new CreationDate(LocalDateTime.now())));
        return token;
    }

    private static class RandomHolder {
        static final Random random = new SecureRandom();
        public static String randomKey(int length) {
            return String.format("%"+length+"s", new BigInteger(length*5/*base 32,2^5*/, random)
                    .toString(32)).replace('\u0020', '0');
        }
    }
}
