package com.spread.users.exception;

/**
 * @author : github.com/sharmasourabh
 * @project : Chapter06 - Modern API Development with Spring and Spring Boot
 **/
public class DependentResourceFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  private final String errMsgKey;
  private final String errorCode;

  public DependentResourceFoundException(ErrorCode code) {
    super(code.getErrMsgKey());
    this.errMsgKey = code.getErrMsgKey();
    this.errorCode = code.getErrCode();
  }

  public DependentResourceFoundException(final String message) {
    super(message);
    this.errMsgKey = ErrorCode.RESOURCE_NOT_FOUND.getErrMsgKey();
    this.errorCode = ErrorCode.RESOURCE_NOT_FOUND.getErrCode();
  }

  public String getErrMsgKey() {
    return errMsgKey;
  }

  public String getErrorCode() {
    return errorCode;
  }
}
