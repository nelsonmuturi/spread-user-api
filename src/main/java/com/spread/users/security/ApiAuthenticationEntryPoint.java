package com.spread.users.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spread.users.exception.Error;
import com.spread.users.exception.ErrorCode;
import com.spread.users.exception.ErrorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;

import static com.spread.users.service.Constants.TOKEN_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author : github.com/sharmasourabh
 * @project : Chapter06 - Modern API Development with Spring and Spring Boot
 **/
@Component
public class ApiAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private ObjectMapper mapper;

  public ApiAuthenticationEntryPoint(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public void commence(HttpServletRequest req,
      HttpServletResponse res, AuthenticationException e)
      throws IOException, ServletException {
    logger.info("exception = " + e);
    logger.info("exception.getCause() = " + e.getCause());
    String errorMsg = "";
    if (e instanceof InsufficientAuthenticationException) {
      errorMsg = e.getMessage();
    } else {
      errorMsg = ErrorCode.UNAUTHORIZED.getErrMsgKey();
    }
    Error error = ErrorUtils
        .createError(errorMsg, ErrorCode.UNAUTHORIZED.getErrCode(),
            HttpStatus.UNAUTHORIZED.value()).setUrl(TOKEN_URL)
        .setReqMethod(req.getMethod())
        .setTimestamp(Instant.now());
    res.setContentType(APPLICATION_JSON_VALUE);
    res.setCharacterEncoding("UTF-8");
    OutputStream out = res.getOutputStream();
    mapper.writeValue(out, error);
    out.flush();
  }
}
