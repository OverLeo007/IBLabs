package ru.paskal.IBLab3Back.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
public class AuthRegDto extends AuthBaseDto {
  @NotEmpty
  private String username;
  @NotEmpty
  private String password;

  public AuthRegDto(String sessionId, String username, String password) {
    super(sessionId);
    this.username = username;
    this.password = password;
  }
}
