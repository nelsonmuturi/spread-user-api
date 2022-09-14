package com.spread.users.service;

import com.spread.users.model.User;
import com.spread.users.model.primitive.Name;
import com.spread.users.rest.model.RefreshToken;
import com.spread.users.rest.model.SignedInUser;

import java.util.Optional;

public interface UserService {

    public Optional<User> findById(String id);

    public Optional<User> findByName(Name name);
    public Optional<SignedInUser> createUser(User user);
    public Optional<SignedInUser> getAccessToken(RefreshToken refreshToken);
    public SignedInUser getSignedInUser(User user);

    public void removeRefreshToken(RefreshToken refreshToken);

}
