package ru.paskal.IBLab3Back.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
public class AuthBaseDto {
  private String sessionId;

  public AuthBaseDto(String sessionId) {
    this.sessionId = sessionId;
  }


}
