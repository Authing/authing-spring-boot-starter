package cn.authing.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthingException extends RuntimeException {
  private final String message;
  private final Integer code;

  public AuthingException(String message, HttpStatus httpStatus) {
    super(message);
    this.message = message;
    this.code = httpStatus.value();
  }
}
