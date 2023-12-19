package ru.paskal.IBLab3Back.dto.instruments;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.paskal.IBLab3Back.dto.auth.AuthVerifyDto;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
public class InstrumentCreateDto extends AuthVerifyDto {

  private String name;
  private String type;
  private String brand;
  private String price;
  private String quantity;

  public InstrumentCreateDto(String sessionId,
      String token,
      String name,
      String type,
      String brand,
      String price,
      String quantity) {
    super(sessionId, token);
    this.name = name;
    this.type = type;
    this.brand = brand;
    this.price = price;
    this.quantity = quantity;
  }


}
