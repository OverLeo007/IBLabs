package ru.paskal.IBLab3Back.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
