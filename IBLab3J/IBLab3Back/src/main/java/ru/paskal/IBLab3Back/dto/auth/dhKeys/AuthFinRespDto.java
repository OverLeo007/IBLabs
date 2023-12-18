package ru.paskal.IBLab3Back.dto.auth.dhKeys;

import java.math.BigInteger;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.paskal.IBLab3Back.dto.auth.AuthBaseDto;

@Setter
@Getter
@ToString(callSuper = true)
public class AuthFinRespDto extends AuthBaseDto {
  private String B;


  public AuthFinRespDto(String sessionId, BigInteger B) {
    super(sessionId);
    this.B = B.toString();
  }
}
