package ru.paskal.IBLab3Back.dto.instruments;

import lombok.Data;

@Data
public class InstrumentDto {
  private String id;
  private String name;
  private String type;
  private String brand;
  private String price;
  private String quantity;
}
