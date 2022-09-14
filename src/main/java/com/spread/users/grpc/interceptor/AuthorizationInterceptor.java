package com.spread.users.grpc.interceptor;

import com.spread.users.service.Constants;
import io.grpc.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// courtesy: https://sultanov.dev/blog/securing-java-grpc-services-with-jwt-based-authentication/
@Component
public class AuthorizationInterceptor
    implements ServerInterceptor {
    private static final Logger logger =
            LoggerFactory.getLogger(AuthorizationInterceptor.class);

    @Value("${spread.grpc.jwt-signing-key}")
    private String signingKey;

    public AuthorizationInterceptor() {
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call,
                                                                 Metadata header,
                                                                 ServerCallHandler<ReqT, RespT> next) {
        String value = header.get(Constants.AUTHORIZATION_METADATA_KEY);
        Status status;
        if (value == null) {
            status = Status.UNAUTHENTICATED.withDescription("Authorization token is missing.");
        } else  if (!value.startsWith(Constants.BEARER_TYPE)) {
            status = Status.UNAUTHENTICATED.withDescription("Unknown authorization type.");
        } else {
            try {
                String token = value.substring(Constants.BEARER_TYPE.length()).trim();
                // Jws<Claims> claims = parser.parseClaimsJws(token);
                Jws<Claims> claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);
                Context ctx = Context.current().withValue(Constants.CLIENT_ID_CONTEXT_KEY, claims.getBody().getSubject());
                return Contexts.interceptCall(ctx, call, header, next);
            } catch (Exception e) {
                status = Status.UNAUTHENTICATED.withDescription(e.getMessage()).withCause(e);
            }
        }

        call.close(status, header);
        return new ServerCall.Listener<>() {
            // noop
        };
    }


}
