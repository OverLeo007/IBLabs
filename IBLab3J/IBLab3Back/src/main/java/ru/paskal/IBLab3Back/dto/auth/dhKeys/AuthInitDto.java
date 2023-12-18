package ru.paskal.IBLab3Back.dto.auth.dhKeys;

import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.paskal.IBLab3Back.dto.auth.AuthBaseDto;

@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class AuthInitDto extends AuthBaseDto {
  private String p;
  private String g;

  public AuthInitDto(String sessionId, BigInteger p, BigInteger g) {
    super(sessionId);
    this.p = p.toString();
    this.g = g.toString();
  }
}
