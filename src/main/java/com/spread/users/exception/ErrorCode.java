package com.spread.users.exception;

/**
 * An enumeration of error codes and associated i18n message keys for order
 * related validation errors.
 *
 * @author : github.com/sharmasourabh
 * @project : Chapter06 - Modern API Development with Spring and Spring Boot
 **/
public enum ErrorCode {
  // Internal Errors: 1 to 16
  GENERIC_ERROR("SPREAD-01", "The system is unable to complete the request. Contact system support."),
  HTTP_MEDIATYPE_NOT_SUPPORTED("SPREAD-02", "Requested media type is not supported. Please use application/json or application/xml as 'Content-Type' header value"),
  HTTP_MESSAGE_NOT_WRITABLE("SPREAD-03", "Missing 'Accept' header. Please add 'Accept' header."),
  HTTP_MEDIA_TYPE_NOT_ACCEPTABLE("SPREAD-04", "Requested 'Accept' header value is not supported. Please use application/json or application/xml as 'Accept' value"),
  JSON_PARSE_ERROR("SPREAD-05", "Make sure request payload should be a valid JSON object."),
  HTTP_MESSAGE_NOT_READABLE("SPREAD-06", "Make sure request payload should be a valid JSON or XML object according to 'Content-Type'."),
  HTTP_REQUEST_METHOD_NOT_SUPPORTED("SPREAD-07", "Request method not supported."),
  CONSTRAINT_VIOLATION("SPREAD-08", "Validation failed."),
  ILLEGAL_ARGUMENT_EXCEPTION("SPREAD-09", "Invalid data passed."),
  RESOURCE_NOT_FOUND("SPREAD-10", "Requested resource not found."),
  CUSTOMER_NOT_FOUND("SPREAD-11", "Requested customer not found."),
  ITEM_NOT_FOUND("SPREAD-12", "Requested item not found."),
  GENERIC_ALREADY_EXISTS("SPREAD-13", "Already exists."),
  ACCESS_DENIED("SPREAD-14", "Access Denied."),
  UNAUTHORIZED("SPREAD-15", "Unauthorized"),
  DEPENDENT_RESOURCE_FOUND("SPREAD-16", "Dependent resource found.");

  private String errCode;
  private String errMsgKey;

  private ErrorCode(final String errCode, final String errMsgKey) {
    this.errCode = errCode;
    this.errMsgKey = errMsgKey;
  }

  /**
   * @return the errCode
   */
  public String getErrCode() {
    return errCode;
  }

  /**
   * @return the errMsgKey
   */
  public String getErrMsgKey() {
    return errMsgKey;
  }
}
