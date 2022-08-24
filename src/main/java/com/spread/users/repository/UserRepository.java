package com.spread.users.repository;

import com.spread.users.model.Id;
import com.spread.users.model.User;
import com.spread.users.model.primitive.Name;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Id> {

    Optional<User> findByName(Name name);
}
