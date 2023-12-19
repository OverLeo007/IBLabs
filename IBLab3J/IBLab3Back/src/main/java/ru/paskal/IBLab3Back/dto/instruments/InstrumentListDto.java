package ru.paskal.IBLab3Back.dto.instruments;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InstrumentListDto {

  List<InstrumentDto> instrumentsList;

}
