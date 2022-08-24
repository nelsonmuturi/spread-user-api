package com.spread.users.repository;

import com.spread.users.model.Id;
import com.spread.users.model.RefreshToken;
import com.spread.users.model.primitive.IdChar;
import com.spread.users.model.primitive.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Id> {

    public Optional<RefreshToken> findByToken(Token token);
    public Optional<RefreshToken> findByUserId(IdChar userId);
}
