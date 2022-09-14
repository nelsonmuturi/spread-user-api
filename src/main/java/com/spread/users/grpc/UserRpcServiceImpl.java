package com.spread.users.grpc;

import com.spread.users.service.UserService;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserRpcServiceImpl extends UserRpcServiceGrpc.UserRpcServiceImplBase {
    private static final Logger logger =
            LoggerFactory.getLogger(UserRpcServiceImpl.class);

    private final UserService userService;

    public UserRpcServiceImpl(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public void findUserById(Users.UserId request,
                             StreamObserver<Users.User> responseObserver) {
        try {
            // attempt to find request user
            var user = userService.findById(request.getId());

            if (user.isPresent()) {
                responseObserver.onNext(com.spread.users.grpc.UserFactory.convertFromUser(user.get()));
            } else {
                responseObserver.onNext(
                        Users.User.newBuilder()
                                .setId("")
                                .build());
            }
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onNext(
                    Users.User.newBuilder()
                            .setId("")
                            .build());
            responseObserver.onCompleted();
        }
    }
    
}
