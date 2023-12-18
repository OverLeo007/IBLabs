package ru.paskal.IBLab3Back.dto.auth.dhKeys;

import java.math.BigInteger;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.paskal.IBLab3Back.dto.auth.AuthBaseDto;

@Setter
@Getter
@ToString(callSuper = true)
public class AuthFinDto extends AuthBaseDto {
  private String A;


  public AuthFinDto(String sessionId, BigInteger A) {
    super(sessionId);
    this.A = A.toString();
  }
}
