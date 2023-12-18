package ru.paskal.IBLab3Back.dto.instruments;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class InstrumentEditDto extends InstrumentCreateDto {
  private int id;

}
