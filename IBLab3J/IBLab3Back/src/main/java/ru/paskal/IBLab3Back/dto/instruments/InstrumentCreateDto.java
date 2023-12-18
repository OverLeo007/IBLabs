package ru.paskal.IBLab3Back.dto.instruments;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class InstrumentCreateDto {

  @Size(min = 3, max = 30, message = "len of name is not between 3 and 30")
  private String name;

  @Size(min = 3, max = 30, message = "len of type is not between 3 and 30")
  private String type;

  @Size(min = 3, max = 30, message = "len of brand is not between 3 and 30")
  private String brand;

  @Min(value = 1, message = "we cant sell it for free!")
  private float price;

  @Min(value = 0, message = "What's on your mind?")
  private int quantity;

}
