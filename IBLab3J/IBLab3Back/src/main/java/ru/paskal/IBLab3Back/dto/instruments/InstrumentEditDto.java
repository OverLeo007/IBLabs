package ru.paskal.IBLab3Back.dto.instruments;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class InstrumentEditDto extends InstrumentCreateDto {

  private String id;

  public InstrumentEditDto(String sessionId, String token, String name, String type, String brand,
      String price, String quantity, String id) {
    super(sessionId, token, name, type, brand, price, quantity);
    this.id = id;
  }
}
