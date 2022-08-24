package com.spread.users;

import com.spread.users.exception.GenericAlreadyExistsException;
import com.spread.users.exception.InvalidRefreshTokenException;
import com.spread.users.model.User;
import com.spread.users.model.primitive.*;
import com.spread.users.repository.RefreshTokenRepository;
import com.spread.users.repository.UserRepository;
import com.spread.users.rest.model.RefreshToken;
import com.spread.users.security.JwtManager;
import com.spread.users.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    private static User userEntity;
    private static com.spread.users.model.RefreshToken refreshTokenEntity;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtManager jwtManager;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeAll
    public static void setup() {
        userEntity = new User(
                new IdChar("kslss8rui2"),
                new Name("Test User"),
                new Role(com.spread.users.model.enums.Roles.USER),
                new Password("$2a$12$SEmKJAZgzhytB1FhdexsG.lR74pojMhfreFchXATYmVvwLsQqqv8e"),
                new CreationDate(LocalDateTime.parse("2021-11-26 03:57:02",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))),
                new ModifiedDate(LocalDateTime.parse("2021-11-26 03:57:02",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

        refreshTokenEntity = new com.spread.users.model.RefreshToken(
                new IdChar("8sushe4j9z"),
                userEntity,
                new Token("qvfjjehe6jt7ps1rvv5i93g0mdpoo240922r1b48u3p0mjtah12rcjt163u8fq14rbavulr6ple9eq0k1lnbfn1lrs1qsmd0121254ld9lf6gmj7ooliqcolhu0t1pqk"),
                new CreationDate(LocalDateTime.now()));
    }

    @Test
    @DisplayName("finding user by existing name returns the entity")
    public void finding_user_by_existing_name_returns_the_entity() {

        when(userRepository.findByName(any(com.spread.users.model.primitive.Name.class)))
                .thenReturn(Optional.of(userEntity));

        // act
        Optional<User> returnedUser = userService.findByName(userEntity.getName());

        // assert
        assertFalse(returnedUser.isEmpty(), "user is empty");
        assertTrue(returnedUser.isPresent(),"user was not found");
        assertSame(returnedUser.get(), userEntity, "user returned was not the same as the mock");
    }

    @Test
    @DisplayName("finding user by non-existent name returns empty")
    public void finding_user_by_nonexistent_name_returns_empty() {

        when(userRepository.findByName(any(com.spread.users.model.primitive.Name.class)))
                .thenReturn(Optional.empty());

        // act
        Optional<User> returnedUser = userService.findByName(new Name("Some Some User"));

        // assert
        assertTrue(returnedUser.isEmpty(), "user is not empty");

    }

    @Test
    @DisplayName("creating an new user succeeds if the user does not already exist")
    public void creating_an_new_user_succeeds_if_the_user_does_not_already_exist() {

        when(userRepository.save(any(com.spread.users.model.User.class)))
                .thenReturn(userEntity);

        // act
        Optional<com.spread.users.rest.model.SignedInUser> signedInUser =
                userService.createUser(userEntity);

        // assert
        assertTrue(signedInUser.isPresent(), "failed to save user");
    }

    @Test
    @DisplayName("creating an new user fails if name already exists")
    public void creating_an_new_user_fails_if_name_already_exists() {

        GenericAlreadyExistsException exception =
                assertThrows(GenericAlreadyExistsException.class, () -> {

                    when(userRepository.findByName(any(com.spread.users.model.primitive.Name.class)))
                            .thenReturn(Optional.of(userEntity));

                    // act
                    Optional<com.spread.users.rest.model.SignedInUser> signedInUser =
                            userService.createUser(userEntity);
                });

        // assert
        assertEquals("user name already exists", exception.getMessage());
    }

    @Test
    @DisplayName("getting an signed-in user creates a new refresh token")
    public void getting_an_signed_in_user_creates_a_new_refresh_token() {

        when(refreshTokenRepository.findByUserId(any(com.spread.users.model.primitive.IdChar.class)))
                .thenReturn(Optional.of(refreshTokenEntity));

        // act
        Optional<com.spread.users.rest.model.SignedInUser> signedInUser =
                Optional.ofNullable(userService.getSignedInUser(userEntity));

        // assert
        assertTrue(signedInUser.isPresent(), "failed to create new refresh token");
    }

    @Test
    @DisplayName("getting refresh token returns existing token")
    public void getting_refresh_token_returns_existing_token() {

        when(refreshTokenRepository.findByToken(any(com.spread.users.model.primitive.Token.class)))
                .thenReturn(Optional.of(refreshTokenEntity));

        // act
        Optional<com.spread.users.rest.model.SignedInUser> signedInUser =
                userService.getAccessToken(new RefreshToken()
                        .refreshToken(refreshTokenEntity.getToken().getValue()));

        // assert
        assertTrue(signedInUser.isPresent(), "failed to create new refresh token");
    }

    @Test
    @DisplayName("getting an refresh token fails if the token does not exist")
    public void getting_an_refresh_token_fails_if_the_token_does_not_exist() {

        InvalidRefreshTokenException exception =
                assertThrows(InvalidRefreshTokenException.class, () -> {

                    when(refreshTokenRepository.findByToken(any(com.spread.users.model.primitive.Token.class)))
                            .thenReturn(Optional.empty());

                    // act
                    Optional<com.spread.users.rest.model.SignedInUser> signedInUser =
                            userService.getAccessToken(new RefreshToken()
                                    .refreshToken(refreshTokenEntity.getToken().getValue()));
                });

        // assert
        assertEquals("invalid token", exception.getMessage());
    }
}
