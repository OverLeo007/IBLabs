package ru.paskal.IBLab3Back.dto.instruments;

import lombok.Getter;
import lombok.Setter;
import ru.paskal.IBLab3Back.dto.auth.AuthVerifyDto;

@Getter
@Setter
public class InstrumentDeleteDto extends AuthVerifyDto {
  private String id;

  public InstrumentDeleteDto(String sessionId, String token, String id) {
    super(sessionId, token);
    this.id = id;
  }
}
