package com.spread.users.grpc;

import com.spread.users.grpc.interceptor.AuthorizationInterceptor;
import com.spread.users.grpc.interceptor.ExceptionInterceptor;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author : github.com/sharmasourabh
 * @project : chapter11-server - Modern API Development with Spring and Spring Boot
 **/
@Component
public class GrpcServer {

  private final Logger LOG = LoggerFactory.getLogger(getClass());

  @Value("${spread.grpc.port}")
  private int port;

  private Server server;

  private UserRpcServiceImpl userRpcService;
  private ExceptionInterceptor exceptionInterceptor;
  private AuthorizationInterceptor authorizationInterceptor;

  public GrpcServer(UserRpcServiceImpl userRpcService, ExceptionInterceptor exceptionInterceptor,
                    AuthorizationInterceptor authorizationInterceptor) {
    this.userRpcService = userRpcService;
    this.exceptionInterceptor = exceptionInterceptor;
    this.authorizationInterceptor = authorizationInterceptor;
  }

  public void start() throws IOException, InterruptedException {
    LOG.info("gRPC server is starting on port: {}.", port);
    server = ServerBuilder.forPort(port)
            .addService(userRpcService)
            .intercept(authorizationInterceptor)
            .intercept(exceptionInterceptor)
            .build().start();
    LOG.info("gRPC server started and listening on port: {}.", port);
    LOG.info("Following service are available: ");
    server.getServices()
        .forEach(s -> LOG.info("Service Name: {}", s.getServiceDescriptor().getName()));
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      LOG.info("Shutting down gRPC server.");
      GrpcServer.this.stop();
      LOG.info("gRPC server shut down successfully.");
    }));
  }

  private void stop() {
    if (server != null) {
      server.shutdown();
    }
  }

  public void block() throws InterruptedException {
    if (server != null) {
      // received the request until application is terminated
      server.awaitTermination();
    }
  }
}
