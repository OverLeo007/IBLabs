package ru.paskal.IBLab3Back.dto.auth;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString(callSuper = true)
public class AuthVerifyDto extends AuthBaseDto {

  private String token;

  public AuthVerifyDto(String sessionId, String token) {
    super(sessionId);
    this.token = token;
  }
}
