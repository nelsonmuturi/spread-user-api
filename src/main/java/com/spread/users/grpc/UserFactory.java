package com.spread.users.grpc;

import com.spread.users.model.User;

public class UserFactory {

    public static Users.User convertFromUser(User user) {
        return Users.User.newBuilder()
                .setId(user.getId().getValue())
                .setName(user.getName().getValue())
                .build();
    }

}
