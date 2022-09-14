package com.spread.users.grpc.interceptor;

import com.spread.users.grpc.exception.ExceptionUtils;
import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author : github.com/sharmasourabh
 * @project : Chapter11 - Modern API Development with Spring and Spring Boot
 **/
@Component
public class ExceptionInterceptor implements ServerInterceptor {

  private static final Logger LOG = LoggerFactory.getLogger(ExceptionInterceptor.class);

  @Override
  // RQT - Request Type; RST - Response Type
  public <RQT, RST> ServerCall.Listener<RQT> interceptCall(ServerCall<RQT, RST> serverCall,
      Metadata metadata, ServerCallHandler<RQT, RST> serverCallHandler) {
    ServerCall.Listener<RQT> listener = serverCallHandler.startCall(serverCall, metadata);
    return new ExceptionHandlingServerCallListener<>(listener, serverCall, metadata);
  }

  private class ExceptionHandlingServerCallListener<RQT, RST>
      extends ForwardingServerCallListener.SimpleForwardingServerCallListener<RQT> {

    private final ServerCall<RQT, RST> serverCall;
    private final Metadata metadata;

    ExceptionHandlingServerCallListener(ServerCall.Listener<RQT> listener,
        ServerCall<RQT, RST> serverCall, Metadata metadata) {
      super(listener);
      this.serverCall = serverCall;
      this.metadata = metadata;
    }

    @Override
    public void onHalfClose() {
      try {
        super.onHalfClose();
      } catch (RuntimeException e) {
        handleException(e, serverCall, metadata);
        throw e;
      }
    }

    @Override
    public void onReady() {
      try {
        super.onReady();
      } catch (RuntimeException e) {
        handleException(e, serverCall, metadata);
        throw e;
      }
    }

    private void handleException(RuntimeException e, ServerCall<RQT, RST> serverCall,
        Metadata metadata) {
      StatusRuntimeException status = ExceptionUtils.traceException(e);
      serverCall.close(status.getStatus(), metadata);
    }
  }
}
